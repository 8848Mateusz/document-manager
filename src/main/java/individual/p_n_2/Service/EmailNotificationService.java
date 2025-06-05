package individual.p_n_2.Service;

import individual.p_n_2.Domain.User.InvoiceInteraction;
import individual.p_n_2.Model.InvoiceData;
import individual.p_n_2.Repository.User.InvoiceInteractionRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class EmailNotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private InvoiceInteractionRepository invoiceInteractionRepository;

    /**
     * Wysyła e-mail z informacją o przeterminowanych fakturach (wersja dla listy faktur).
     */
    public void sendOverdueInvoiceEmail(String contractorEmail, List<InvoiceData> invoices) {
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("Szanowni Państwo,<br><br>");
        emailBody.append("Poniżej przedstawiamy zestawienie przeterminowanych faktur:<br><br>");
        emailBody.append("<table border='1' cellpadding='5' cellspacing='0'>");
        emailBody.append("<tr><th>Numer faktury</th><th>Termin płatności</th><th>Kwota do zapłaty</th></tr>");

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (InvoiceData invoice : invoices) {
            emailBody.append("<tr>")
                    .append("<td>").append(invoice.getInvoiceNumber()).append("</td>")
                    .append("<td>").append(invoice.getPaymentDate()).append("</td>")
                    .append("<td>").append(invoice.getFormattedAmountDue()).append("</td>")
                    .append("</tr>");
            totalAmount = totalAmount.add(invoice.getAmountDue());
        }
        emailBody.append("</table><br>");
        emailBody.append("<strong>Łączna kwota do zapłaty:</strong> ")
                .append(formatCurrency(totalAmount)).append("<br><br>");
        emailBody.append("Prosimy o pilne uregulowanie zaległości.<br><br>Pozdrawiamy,<br>Zespół");

        // TESTOWY adres (tu można wprowadzić docelowy adres)
        contractorEmail = "test@yourdomain.com";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(contractorEmail);
            helper.setSubject("Przeterminowane faktury");
            helper.setText(emailBody.toString(), true);

            mailSender.send(message);
            System.out.println("E-mail wysłany pomyślnie.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Błąd podczas wysyłania e-maila: " + e.getMessage());
        }
    }

    /**
     * Wysyła e-mail z informacją o jednej przeterminowanej fakturze (do wykorzystania w kontrolerze).
     */
    public void sendOverdueInvoiceEmail(String contractorEmail, String invoiceNumber, BigDecimal amountDue, String dueDate) {
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("Szanowni Państwo,<br><br>");
        emailBody.append("Poniżej przesyłamy informację o przeterminowanej fakturze:<br><br>");
        emailBody.append("<table border='1' cellpadding='5' cellspacing='0'>");
        emailBody.append("<tr><th>Numer faktury</th><th>Termin płatności</th><th>Kwota do zapłaty</th></tr>");
        emailBody.append("<tr>")
                .append("<td>").append(invoiceNumber).append("</td>")
                .append("<td>").append(dueDate).append("</td>")
                .append("<td>").append(formatCurrency(amountDue)).append("</td>")
                .append("</tr>");
        emailBody.append("</table><br>");
        emailBody.append("Prosimy o pilne uregulowanie zaległości.<br><br>Pozdrawiamy,<br>Zespół");

        // TESTOWY adres (tu można wprowadzić docelowy adres)
        contractorEmail = "test@yourdomain.com";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(contractorEmail);
            helper.setSubject("Przeterminowana faktura");
            helper.setText(emailBody.toString(), true);

            mailSender.send(message);
            System.out.println("E-mail wysłany pomyślnie.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Błąd podczas wysyłania e-maila: " + e.getMessage());
        }
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "0,00 zł";
        }
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("pl", "PL"));
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        formatter.setGroupingUsed(true);  // spacje tysięcy
        return formatter.format(amount) + " zł";
    }

    public void saveEmailInteraction(String invoiceNumber, String createdBy) {
        InvoiceInteraction interaction = new InvoiceInteraction();
        interaction.setInvoiceNumber(invoiceNumber);
        interaction.setType("email");
        interaction.setTimestamp(LocalDateTime.now());
        interaction.setCreatedBy(createdBy);
        invoiceInteractionRepository.save(interaction);
    }
}