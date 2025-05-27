package individual.p_n_2.Service;

import individual.p_n_2.Model.InvoiceData;
import individual.p_n_2.Util.InvoicePdfReader;
import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class PdfInvoiceParserService {
    public InvoiceData parse(File file) {
        return InvoicePdfReader.read(file);
    }
}
