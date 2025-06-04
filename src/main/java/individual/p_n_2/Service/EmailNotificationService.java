package individual.p_n_2.Service;

import individual.p_n_2.Model.InvoiceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class EmailNotificationService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Wysyła e-mail z informacją o przeterminowanej fakturze.
     */
    public void sendOverdueInvoiceEmail(String email, String invoiceNumber, BigDecimal amountDue, String dueDate) {
        String formattedAmount = formatCurrency(amountDue);
        email = "test@yourdomain.com";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Przeterminowana faktura");
        message.setText("Szanowny Kliencie,\n\n"
                + "Twoja faktura nr " + invoiceNumber + " jest przeterminowana.\n"
                + "Kwota do zapłaty: " + formattedAmount + "\n"
                + "Termin płatności: " + dueDate + "\n\n"
                + "Prosimy o uregulowanie należności.\n\n"
                + "Z poważaniem,\nTwoja Firma");

        try {
            System.out.println("Wysyłam maila na adres: " + email);
            mailSender.send(message);
            System.out.println("Mail został wysłany!");
        } catch (Exception e) {
            System.err.println("Błąd przy wysyłaniu maila: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Wysyła powiadomienia e-mail do listy faktur (InvoiceData).
     */
    public int sendNotificationsForInvoices(List<InvoiceData> invoices) {
        int sent = 0;
        for (InvoiceData invoice : invoices) {
            if (invoice.getEmail() != null && !invoice.getEmail().isEmpty()
                    && invoice.getAmountDue() != null && invoice.getPaymentDate() != null) {
                sendOverdueInvoiceEmail(
                        invoice.getEmail(),
                        invoice.getInvoiceNumber(),
                        invoice.getAmountDue(),
                        invoice.getPaymentDate()
                );
                sent++;
            }
        }
        return sent;
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "0,00 zł";
        }
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("pl", "PL"));
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        formatter.setGroupingUsed(true);  //spacje tysięcy
        return formatter.format(amount) + " zł";
    }
}