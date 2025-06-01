package individual.p_n_2.Controller;

import individual.p_n_2.Domain.Symfonia.TransactionRecord;
import individual.p_n_2.Service.SymfoniaInvoiceService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/symfonia-invoices")
public class InvoiceSymfoniaRestController {

    private final SymfoniaInvoiceService symfoniaInvoiceService;

    public InvoiceSymfoniaRestController(SymfoniaInvoiceService symfoniaInvoiceService) {
        this.symfoniaInvoiceService = symfoniaInvoiceService;
    }

    /**
     * Zwraca listę wszystkich przeterminowanych i nieopłaconych faktur.
     */
    @GetMapping("/overdue")
    public List<TransactionRecord> getOverdueInvoices() {
        return symfoniaInvoiceService.getOverdueUnpaidInvoices();
    }

    /**
     * Sprawdza, czy dana faktura (numer faktury = kolumna 'kod' w bazie TR) jest jednocześnie nieopłacona i przeterminowana.
     */
    @GetMapping("/unpaid-overdue/{invoiceNumber}")
    public boolean isUnpaidAndOverdue(@PathVariable String invoiceNumber) {
        return symfoniaInvoiceService.isInvoiceUnpaidAndOverdue(invoiceNumber);
    }

    /**
     * Zwraca pozostałą do zapłaty kwotę (remaining amount) dla wskazanej faktury
     * (numer faktury = kolumna 'kod' w bazie TR).
     */
    @GetMapping("/amount-due/{invoiceNumber}")
    public BigDecimal getRemainingAmount(@PathVariable String invoiceNumber) {
        return symfoniaInvoiceService.getRemainingAmountForInvoice(invoiceNumber);
    }
}