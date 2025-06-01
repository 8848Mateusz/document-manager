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
    private BigDecimal grossAmount;
    private BigDecimal amountDue;
    private boolean settled;
    private String filename;
    private int phoneCalls;
    private String invoiceIssueDate;

    // FORMATY DATY
    public String getFormattedInvoiceIssueDate() {
        try {
            LocalDate date = LocalDate.parse(this.invoiceIssueDate);
            return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (Exception e) {
            return this.invoiceIssueDate;
        }
    }

    public String getFormattedPaymentDate() {
        try {
            LocalDate date = LocalDate.parse(this.paymentDate);
            return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (Exception e) {
            return this.paymentDate;
        }
    }

    public String getFormattedGrossAmount() {
        if (grossAmount == null) return "0,00 zł";
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("pl", "PL"));
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(grossAmount) + " zł";
    }

    public String getFormattedAmountDue() {
        if (amountDue == null) return "0,00 zł";
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("pl", "PL"));
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(amountDue) + " zł";
    }

    // Metody do sprawdzania braków danych
    public boolean hasMissingInvoiceNumber() {
        return invoiceNumber == null || invoiceNumber.isEmpty();
    }

    public boolean hasMissingContractor() {
        return contractor == null || contractor.isEmpty();
    }

    public boolean hasMissingPaymentDate() {
        return paymentDate == null || paymentDate.isEmpty();
    }

    public boolean hasMissingAmountDue() {
        return amountDue == null;
    }

    public boolean hasErrors() {
        return hasMissingInvoiceNumber() ||
                hasMissingContractor() ||
                hasMissingPaymentDate() ||
                hasMissingAmountDue();
    }


    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public String getContractor() { return contractor; }
    public void setContractor(String contractor) { this.contractor = contractor; }

    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }

    public BigDecimal getGrossAmount() { return grossAmount; }
    public void setGrossAmount(BigDecimal grossAmount) { this.grossAmount = grossAmount; }

    public BigDecimal getAmountDue() { return amountDue; }
    public void setAmountDue(BigDecimal amountDue) { this.amountDue = amountDue; }

    public boolean isSettled() { return settled; }
    public void setSettled(boolean settled) { this.settled = settled; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public int getPhoneCalls() { return phoneCalls; }
    public void setPhoneCalls(int phoneCalls) { this.phoneCalls = phoneCalls; }

    public String getInvoiceIssueDate() { return invoiceIssueDate; }
    public void setInvoiceIssueDate(String invoiceIssueDate) { this.invoiceIssueDate = invoiceIssueDate; }
}