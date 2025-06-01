package individual.p_n_2.Service;

import individual.p_n_2.Domain.Symfonia.TransactionRecord;
import individual.p_n_2.Repository.Symfonia.TransactionRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SymfoniaInvoiceService {

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    public boolean isInvoiceUnpaidAndOverdue(String invoiceNumber) {
        Optional<TransactionRecord> recordOpt = transactionRecordRepository.findFirstByNumerFakturyOrderByIdDesc(invoiceNumber);
        if (recordOpt.isPresent()) {
            TransactionRecord record = recordOpt.get();
            if (record.getDatarozl() == null) {
                if (record.getDataplat() == null) {
                    return true;
                }
                if (record.getDataplat().isBefore(LocalDate.now())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<TransactionRecord> getOverdueUnpaidInvoices() {
        return transactionRecordRepository.findByDatarozlIsNullAndDataplatBefore(LocalDate.now());
    }

    /**
     * Pobiera wartość rozliczoną (wartość_rozl) z Symfonii dla podanej faktury.
     */
    public BigDecimal getRemainingAmountForInvoice(String invoiceNumber) {
        Optional<TransactionRecord> recordOpt = transactionRecordRepository.findFirstByNumerFakturyOrderByIdDesc(invoiceNumber);

        if (recordOpt.isPresent()) {
            TransactionRecord record = recordOpt.get();
            BigDecimal grossAmount = record.getKwota() != null ? record.getKwota() : BigDecimal.ZERO;
            BigDecimal paidAmount = record.getWartoscRozl() != null ? record.getWartoscRozl() : BigDecimal.ZERO;
            return grossAmount.subtract(paidAmount).max(BigDecimal.ZERO);
        }

        return BigDecimal.ZERO;
    }

    public BigDecimal getRozliczoneForInvoice(String invoiceNumber) {
        Optional<TransactionRecord> recordOpt = transactionRecordRepository.findFirstByNumerFakturyOrderByIdDesc(invoiceNumber);

        if (recordOpt.isPresent()) {
            TransactionRecord record = recordOpt.get();
            return record.getWartoscRozl() != null ? record.getWartoscRozl() : BigDecimal.ZERO;
        }

        return BigDecimal.ZERO;
    }
}