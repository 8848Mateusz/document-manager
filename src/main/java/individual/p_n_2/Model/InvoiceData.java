package individual.p_n_2.Model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class InvoiceData {
    private String invoiceNumber;
    private String contractor;
    private String paymentDate;
    private String grossAmount;
    private boolean settled;
    private String filename;
    private int phoneCalls;

    //  Getter do wyświetlania sformatowanej kwoty
    public String getFormattedGrossAmount() {
        try {
            double value = Double.parseDouble(grossAmount.replace(",", "."));
            return NumberFormat.getCurrencyInstance(new Locale("pl", "PL")).format(value);
        } catch (Exception e) {
            return grossAmount;
        }
    }

    //  Getter do sformatowanej daty (np. 2025-05-25 → 25 maja 2025)
    public String getFormattedPaymentDate() {
        try {
            LocalDate date = LocalDate.parse(paymentDate);
            return date.format(DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("pl")));
        } catch (Exception e) {
            return paymentDate;
        }
    }

    //  Getter do wartości liczbowej – przydatne do sumowania
    public BigDecimal getAmountDue() {
        try {
            return new BigDecimal(grossAmount.replace(",", "."));
        } catch (Exception e) {
            return null;
        }
    }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public String getContractor() { return contractor; }
    public void setContractor(String contractor) { this.contractor = contractor; }

    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }

    public String getGrossAmount() { return grossAmount; }
    public void setGrossAmount(String grossAmount) { this.grossAmount = grossAmount; }

    public boolean isSettled() { return settled; }
    public void setSettled(boolean settled) { this.settled = settled; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public int getPhoneCalls() { return phoneCalls; }
    public void setPhoneCalls(int phoneCalls) { this.phoneCalls = phoneCalls; }
}