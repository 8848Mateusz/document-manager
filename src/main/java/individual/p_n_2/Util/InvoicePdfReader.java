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
        data.setSettled(false); // domyślnie false

        String text = "";

        try (PDDocument doc = PDDocument.load(file)) {
            text = new PDFTextStripper().getText(doc);

            // Jeśli zawiera informację o zapłacie — oznacz jako rozliczoną
            if (Pattern.compile("Z\\s*A\\s*P\\s*Ł\\s*A\\s*C\\s*O\\s*N\\s*O\\s*:?", Pattern.CASE_INSENSITIVE).matcher(text).find()) {
                data.setSettled(true);
            }

            // Numer faktury
            Matcher nr = Pattern.compile("Nr:\\s*(\\S+)").matcher(text);
            if (nr.find()) data.setInvoiceNumber(nr.group(1));

            // Kontrahent
            Matcher contractor = Pattern.compile("Nabywca:\\s*(.*?)\\s+Adres:", Pattern.DOTALL).matcher(text);
            if (contractor.find()) data.setContractor(contractor.group(1).trim());

            // Termin płatności
            Matcher date = Pattern.compile("Termin płatności:\\s*(\\d{4}-\\d{2}-\\d{2})").matcher(text);
            if (date.find()) data.setPaymentDate(date.group(1));

            // Kwota brutto
            Matcher kwota = Pattern.compile("Razem do zapłaty:\\s*([\\d\\s\\u00A0.,\\-]+)").matcher(text);
            if (kwota.find()) {
                String raw = kwota.group(1);
                String cleaned = raw.replaceAll("[\\s\\u00A0]", "");
                if (cleaned.contains(",") && !cleaned.contains(".")) {
                    cleaned = cleaned.replace(",", ".");
                } else if (cleaned.contains(",") && cleaned.contains(".")) {
                    cleaned = cleaned.replace(",", "");
                }
                data.setGrossAmount(cleaned);
            }

        } catch (IOException e) {
            System.err.println("Błąd parsowania pliku " + file.getName() + ": " + e.getMessage());
            return null;
        }

        return data;
    }
}