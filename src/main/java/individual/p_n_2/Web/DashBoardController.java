package individual.p_n_2.Web;

import individual.p_n_2.Domain.User;
import individual.p_n_2.Model.InvoiceData;
import individual.p_n_2.Service.InvoiceInteractionService;
import individual.p_n_2.Service.PdfInvoiceParserService;
import individual.p_n_2.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashBoardController {

    @Autowired
    private UserService userService;

    @Autowired
    private PdfInvoiceParserService pdfParser;

    @Autowired
    private InvoiceInteractionService interactionService;

    private static final String FOLDER = System.getProperty("user.home") + "/Desktop/faktury-testowe";

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String from,
                            @RequestParam(required = false) String to,
                            Model model,
                            Authentication authentication) {

        DateTimeFormatter htmlFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter pdfFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
                    if (data != null) {
                        if (data.isSettled()) {
                            // Usuń historię tej faktury z bazy danych
                            interactionService.deleteHistoryForInvoice(data.getInvoiceNumber());
                            continue; // Pomijamy tę fakturę w dashboardzie
                        }

                        int phoneCount = interactionService.countPhoneCallsForInvoice(data.getInvoiceNumber());
                        int commentCount = interactionService.countCommentsForInvoice(data.getInvoiceNumber());
                        data.setPhoneCalls(phoneCount);
                        commentCounts.put(data.getInvoiceNumber(), commentCount);

                        if (fromDate != null || toDate != null) {
                            if (data.getPaymentDate() != null) {
                                LocalDate paymentDate = LocalDate.parse(data.getPaymentDate(), pdfFormatter);

                                if ((fromDate == null || !paymentDate.isBefore(fromDate)) &&
                                        (toDate == null || !paymentDate.isAfter(toDate))) {
                                    invoices.add(data);
                                }
                            }
                        } else {
                            invoices.add(data);
                        }
                    }

                } catch (Exception e) {
                    System.err.println("Błąd parsowania pliku " + file.getName() + ": " + e.getMessage());
                }
            }
        }

        BigDecimal totalToPay = BigDecimal.ZERO;
        int errorCount = 0;

        for (InvoiceData dto : invoices) {
            if (dto.getAmountDue() != null) {
                totalToPay = totalToPay.add(dto.getAmountDue());
            }
            if (dto.getInvoiceNumber() == null ||
                    dto.getContractor() == null ||
                    dto.getPaymentDate() == null ||
                    dto.getAmountDue() == null) {
                errorCount++;
            }
        }

        model.addAttribute("invoices", invoices);
        model.addAttribute("totalToPay", totalToPay.setScale(2, RoundingMode.HALF_UP));
        model.addAttribute("errorCount", errorCount);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("commentCounts", commentCounts);

        User user = userService.findByEmail(authentication.getName());
        model.addAttribute("fullName", user.getFullName());

        return "dashboard";
    }
}