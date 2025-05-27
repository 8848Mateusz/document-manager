package individual.p_n_2.Util;

import individual.p_n_2.Model.InvoiceData;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvoicePdfReader {

    public static InvoiceData read(File file) {
        InvoiceData data = new InvoiceData();

        try (PDDocument doc = PDDocument.load(file)) {
            String text = new PDFTextStripper().getText(doc);

            // Regex: numer faktury
            Matcher nr = Pattern.compile("Nr:\\s*(\\S+)").matcher(text);
            if (nr.find()) data.setInvoiceNumber(nr.group(1));

            // Regex: kontrahent (między "Nabywca:" a "Adres:")
            Matcher contractor = Pattern.compile("Nabywca:\\s*(.*?)\\s+Adres:", Pattern.DOTALL).matcher(text);
            if (contractor.find()) data.setContractor(contractor.group(1).trim());

            // Regex: termin płatności (format RRRR-MM-DD)
            Matcher date = Pattern.compile("Termin płatności:\\s*(\\d{4}-\\d{2}-\\d{2})").matcher(text);
            if (date.find()) data.setPaymentDate(date.group(1));

            // Regex: kwota brutto (z przecinkiem)
            Matcher kwota = Pattern.compile("Razem do zapłaty:\\s*(\\d+,\\d{2}) PLN").matcher(text);
            if (kwota.find()) data.setGrossAmount(kwota.group(1));

            // Domyślnie rozliczenie: false
            data.setSettled(false);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}