package individual.p_n_2.Web;

import individual.p_n_2.Model.InvoiceData;
import individual.p_n_2.Service.*;
import individual.p_n_2.Domain.User.User;
import individual.p_n_2.Domain.Symfonia.TransactionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private InvoiceInteractionService interactionService;

    @Autowired
    private SymfoniaInvoiceService symfoniaInvoiceService;

    @Autowired
    private EmailNotificationService notificationService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        addCommonAttributes(model, authentication);
        return "dashboard";
    }

    @GetMapping("/dashboard/load")
    public String loadDashboardData(@RequestParam(required = false) String from,
                                    @RequestParam(required = false) String to,
                                    @RequestParam(required = false, defaultValue = "none") String sortOrder,
                                    Model model,
                                    Authentication authentication) {

        // konwersja dat
        DateTimeFormatter htmlFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fromDate = (from != null && !from.isEmpty()) ? LocalDate.parse(from, htmlFormatter) : null;
        LocalDate toDate = (to != null && !to.isEmpty()) ? LocalDate.parse(to, htmlFormatter) : null;

       // pobierz fakturę
        List<TransactionRecord> records = symfoniaInvoiceService.getFilteredInvoicesFrom2021AndFVS();

        // Sortujemy TYLKO jeśli sortOrder=asc
        if ("asc".equalsIgnoreCase(sortOrder)) {
            records.sort(Comparator
                    .comparing((TransactionRecord tr) -> {
                        String contractorName = symfoniaInvoiceService.getContractorName(tr.getNumerFaktury());
                        return contractorName != null ? contractorName : "";
                    }, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(TransactionRecord::getNumerFaktury, String.CASE_INSENSITIVE_ORDER));
        }

        List<InvoiceData> invoices = new ArrayList<>();
        Map<String, Integer> commentCounts = new HashMap<>();

        for (TransactionRecord record : records) {
            try {
                InvoiceData data = new InvoiceData();
                data.setInvoiceNumber(record.getNumerFaktury());
                data.setGrossAmount(record.getKwota() != null ? record.getKwota() : BigDecimal.ZERO);

                BigDecimal rozliczone = record.getKwotaRozliczona() != null ? record.getKwotaRozliczona() : BigDecimal.ZERO;
                BigDecimal kwotaDoZaplaty = data.getGrossAmount().subtract(rozliczone).max(BigDecimal.ZERO);
                data.setAmountDue(kwotaDoZaplaty);

                data.setContractor(symfoniaInvoiceService.getContractorName(record.getNumerFaktury()));

                int phoneCount = interactionService.countPhoneCallsForInvoice(record.getNumerFaktury());
                int commentCount = interactionService.countCommentsForInvoice(record.getNumerFaktury());
                data.setPhoneCalls(phoneCount);
                commentCounts.put(record.getNumerFaktury(), commentCount);

                int emailSentCount = interactionService.sumEmailSentCount(record.getNumerFaktury());
                data.setEmailSentCount(emailSentCount);

                if (record.getDataWystawienia() != null) {
                    data.setInvoiceIssueDate(record.getDataWystawienia().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                }

                if (record.getTerminPlatnosci() != null) {
                    data.setPaymentDate(record.getTerminPlatnosci().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                }

                // Filtr daty płatności
                if (fromDate != null || toDate != null) {
                    if (data.getPaymentDate() != null && !data.getPaymentDate().isEmpty()) {
                        LocalDate issueDateParsed = LocalDate.parse(data.getInvoiceIssueDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                        if ((fromDate == null || !issueDateParsed.isBefore(fromDate)) &&
                                (toDate == null || !issueDateParsed.isAfter(toDate))) {
                            invoices.add(data);
                        }
                    } else {
                        invoices.add(data);
                    }
                } else {
                    invoices.add(data);
                }

            } catch (Exception e) {
                System.err.println("Błąd przetwarzania faktury: " + record.getNumerFaktury() + ": " + e.getMessage());
            }
        }

        LocalDateTime aktualizacja = LocalDateTime.now();
        DateTimeFormatter aktualizacjaFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", new Locale("pl"));
        model.addAttribute("aktualizacja", aktualizacja.format(aktualizacjaFormatter));

        model.addAttribute("invoiceCount", invoices.size());
        model.addAttribute("commentCounts", commentCounts);

        BigDecimal totalToPay = invoices.stream()
                .map(InvoiceData::getAmountDue)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("pl", "PL"));
        formatter.setGroupingUsed(true);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        model.addAttribute("totalToPay", formatter.format(totalToPay) + " zł");

        int errorCount = (int) invoices.stream()
                .filter(dto -> dto.getInvoiceNumber() == null || dto.getInvoiceNumber().isEmpty()
                        || dto.getContractor() == null || dto.getContractor().isEmpty()
                        || dto.getInvoiceIssueDate() == null || dto.getInvoiceIssueDate().isEmpty()
                        || dto.getPaymentDate() == null || dto.getPaymentDate().isEmpty()
                        || dto.getGrossAmount() == null
                        || dto.getAmountDue() == null)
                .count();

        model.addAttribute("errorCount", errorCount);
        model.addAttribute("invoices", invoices);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("sortOrder", sortOrder);

        addCommonAttributes(model, authentication);
        return "dashboard";
    }

    @PostMapping("/dashboard/sendNotifications")
    @ResponseBody
    public Map<String, Object> sendNotifications(@RequestParam("invoiceNumbers") List<String> invoiceNumbers) {
        int successCount = 0;
        int noEmailCount = 0;
        List<String> contractorsWithoutEmail = new ArrayList<>();

        for (String invoiceNumber : invoiceNumbers) {
            String email = symfoniaInvoiceService.getContractorEmail(invoiceNumber);
            BigDecimal amountDue = symfoniaInvoiceService.getRemainingAmountForInvoice(invoiceNumber);
            String contractorName = symfoniaInvoiceService.getContractorName(invoiceNumber);
            String dueDate = symfoniaInvoiceService.getDueDate(invoiceNumber);

            if (email != null && !email.isEmpty() && amountDue != null && dueDate != null && !dueDate.isEmpty()) {
                try {
                    notificationService.sendOverdueInvoiceEmail(email, invoiceNumber, amountDue, dueDate);
                    interactionService.incrementEmailSentCount(invoiceNumber);
                    successCount++;
                } catch (Exception e) {
                    contractorsWithoutEmail.add(contractorName + " (błąd wysyłki)");
                    noEmailCount++;
                }
            } else {
                contractorsWithoutEmail.add(contractorName + " (brak emaila lub danych)");
                noEmailCount++;
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("successCount", successCount);
        response.put("totalCount", invoiceNumbers.size());
        response.put("noEmailCount", noEmailCount);
        response.put("missingEmails", contractorsWithoutEmail);
        return response;
    }


    private void addCommonAttributes(Model model, Authentication authentication) {
        try {
            User user = userService.findByEmail(authentication.getName());
            model.addAttribute("fullName", user.getFullName());
        } catch (Exception e) {
            model.addAttribute("fullName", "Gość");
        }
    }

    @GetMapping("/dashboard/emailSentCount")
    @ResponseBody
    public int getEmailSentCount(@RequestParam("invoiceNumber") String invoiceNumber) {
        return interactionService.sumEmailSentCount(invoiceNumber);
    }
}