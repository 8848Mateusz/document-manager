package individual.p_n_2.Web;

import individual.p_n_2.Model.InvoiceData;
import individual.p_n_2.Service.InvoiceInteractionService;
import individual.p_n_2.Service.PdfInvoiceParserService;
import individual.p_n_2.Service.SymfoniaInvoiceService;
import individual.p_n_2.Service.UserService;
import individual.p_n_2.Domain.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class DashBoardController {

    @Autowired
    private UserService userService;

    @Autowired
    private PdfInvoiceParserService pdfParser;

    @Autowired
    private InvoiceInteractionService interactionService;

    @Autowired
    private SymfoniaInvoiceService symfoniaInvoiceService;

    private static final String FOLDER = System.getProperty("user.home") + "/Desktop/faktury-testowe";

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        addCommonAttributes(model, authentication);
        return "dashboard";
    }

    @GetMapping("/dashboard/load")
    public String loadDashboardData(@RequestParam(required = false) String from,
                                    @RequestParam(required = false) String to,
                                    Model model,
                                    Authentication authentication) {

        DateTimeFormatter htmlFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fromDate = (from != null && !from.isEmpty()) ? LocalDate.parse(from, htmlFormatter) : null;
        LocalDate toDate = (to != null && !to.isEmpty()) ? LocalDate.parse(to, htmlFormatter) : null;

        File folder = new File(FOLDER);
        File[] pdfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

        List<InvoiceData> invoices = new ArrayList<>();
        Map<String, Integer> commentCounts = new HashMap<>();

        if (pdfFiles != null) {
            for (File file : pdfFiles) {
                try {
                    InvoiceData data = pdfParser.parse(file);
                    if (data != null && data.getInvoiceNumber() != null) {
                        boolean isUnpaidAndOverdue = symfoniaInvoiceService.isInvoiceUnpaidAndOverdue(data.getInvoiceNumber());
                        if (isUnpaidAndOverdue) {

                            if (data.getGrossAmount() == null) data.setGrossAmount(BigDecimal.ZERO);
                            BigDecimal rozliczone = symfoniaInvoiceService.getRozliczoneForInvoice(data.getInvoiceNumber());
                            BigDecimal kwotaDoZaplaty = data.getGrossAmount().subtract(rozliczone).max(BigDecimal.ZERO);
                            data.setAmountDue(kwotaDoZaplaty);

                            int phoneCount = interactionService.countPhoneCallsForInvoice(data.getInvoiceNumber());
                            int commentCount = interactionService.countCommentsForInvoice(data.getInvoiceNumber());
                            data.setPhoneCalls(phoneCount);
                            commentCounts.put(data.getInvoiceNumber(), commentCount);

                            // Data wystawienia: wyciągamy z Symfonii lub zostawiamy null
                            String issueDate = symfoniaInvoiceService.getInvoiceIssueDate(data.getInvoiceNumber());
                            if (issueDate != null && !issueDate.isEmpty()) {
                                try {
                                    LocalDate issueDateParsed = LocalDate.parse(issueDate);
                                    data.setInvoiceIssueDate(issueDateParsed.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                                } catch (Exception e) {
                                    data.setInvoiceIssueDate(issueDate);
                                }
                            }

                            // Data płatności
                            if (data.getPaymentDate() != null && !data.getPaymentDate().isEmpty()) {
                                try {
                                    LocalDate paymentDateParsed = LocalDate.parse(data.getPaymentDate());
                                    data.setPaymentDate(paymentDateParsed.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                                } catch (Exception e) {
                                }
                            }

                            // UWAGA: Usunąłem filtr odrzucający faktury z brakami!
                            // Dzięki temu wyświetlą się wszystkie, nawet jeśli brakuje np. paymentDate.

                            // Filtrowanie dat płatności
                            if (fromDate != null || toDate != null) {
                                if (data.getPaymentDate() != null && !data.getPaymentDate().isEmpty()) {
                                    LocalDate paymentDate = LocalDate.parse(data.getPaymentDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                                    if ((fromDate == null || !paymentDate.isBefore(fromDate)) &&
                                            (toDate == null || !paymentDate.isAfter(toDate))) {
                                        invoices.add(data);
                                    }
                                } else {
                                    invoices.add(data); // Dodaj również jeśli brakuje daty
                                }
                            } else {
                                invoices.add(data);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Błąd parsowania pliku " + file.getName() + ": " + e.getMessage());
                }
            }
        }

        // Aktualizacja
        LocalDateTime aktualizacja = LocalDateTime.now();
        DateTimeFormatter aktualizacjaFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", new Locale("pl"));
        String aktualizacjaFormatted = aktualizacja.format(aktualizacjaFormatter);
        model.addAttribute("aktualizacja", aktualizacjaFormatted);

        // Liczba pozycji
        model.addAttribute("invoiceCount", invoices.size());
        model.addAttribute("commentCounts", commentCounts);

        // Suma do zapłaty
        BigDecimal totalToPay = invoices.stream()
                .map(InvoiceData::getAmountDue)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("pl", "PL"));
        formatter.setGroupingUsed(true);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);

        String totalToPayFormatted = formatter.format(totalToPay) + " zł";

        // Licznik błędów
        int errorCount = (int) invoices.stream()
                .filter(dto -> dto.getInvoiceNumber() == null || dto.getInvoiceNumber().isEmpty()
                        || dto.getContractor() == null || dto.getContractor().isEmpty()
                        || dto.getInvoiceIssueDate() == null || dto.getInvoiceIssueDate().isEmpty()
                        || dto.getPaymentDate() == null || dto.getPaymentDate().isEmpty()
                        || dto.getGrossAmount() == null
                        || dto.getAmountDue() == null)
                .count();

        model.addAttribute("invoices", invoices);
        model.addAttribute("totalToPay", totalToPayFormatted);
        model.addAttribute("errorCount", errorCount);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        addCommonAttributes(model, authentication);
        return "dashboard";
    }

    private void addCommonAttributes(Model model, Authentication authentication) {
        try {
            User user = userService.findByEmail(authentication.getName());
            model.addAttribute("fullName", user.getFullName());
        } catch (Exception e) {
            model.addAttribute("fullName", "Gość");
        }
    }
}