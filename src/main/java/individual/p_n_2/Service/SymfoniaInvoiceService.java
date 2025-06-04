package individual.p_n_2.Service;

import individual.p_n_2.Domain.Symfonia.FKKontrahenci;
import individual.p_n_2.Domain.Symfonia.TransactionRecord;
import individual.p_n_2.Repository.Symfonia.FKKontrahenciRepository;
import individual.p_n_2.Repository.Symfonia.TransactionRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SymfoniaInvoiceService {

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @Autowired
    private FKKontrahenciRepository fkKontrahenciRepository;

    public boolean isInvoiceUnpaidAndOverdue(String invoiceNumber) {
        Optional<TransactionRecord> recordOpt = transactionRecordRepository.findFirstByNumerFakturyOrderByIdDesc(invoiceNumber);
        if (recordOpt.isPresent()) {
            TransactionRecord record = recordOpt.get();
            if (record.getRozliczona() == null || record.getRozliczona() == 0) {
                if (record.getTerminPlatnosci() == null) {
                    return true;
                }
                if (record.getTerminPlatnosci().isBefore(LocalDate.now())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<TransactionRecord> getOverdueUnpaidInvoices() {
        return transactionRecordRepository.findOverdueUnpaidInvoices();
    }

    public BigDecimal getRemainingAmountForInvoice(String invoiceNumber) {
        Optional<TransactionRecord> recordOpt = transactionRecordRepository.findFirstByNumerFakturyOrderByIdDesc(invoiceNumber);

        if (recordOpt.isPresent()) {
            TransactionRecord record = recordOpt.get();
            BigDecimal grossAmount = record.getKwota() != null ? record.getKwota() : BigDecimal.ZERO;
            BigDecimal paidAmount = record.getKwotaRozliczona() != null ? record.getKwotaRozliczona() : BigDecimal.ZERO;

            BigDecimal remainingAmount = grossAmount.subtract(paidAmount);
            if (remainingAmount.compareTo(BigDecimal.ZERO) < 0) {
                remainingAmount = BigDecimal.ZERO;
            }
            return remainingAmount.setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getRozliczoneForInvoice(String invoiceNumber) {
        Optional<TransactionRecord> recordOpt = transactionRecordRepository.findFirstByNumerFakturyOrderByIdDesc(invoiceNumber);

        if (recordOpt.isPresent()) {
            TransactionRecord record = recordOpt.get();
            return record.getKwotaRozliczona() != null ? record.getKwotaRozliczona() : BigDecimal.ZERO;
        }

        return BigDecimal.ZERO;
    }

    public String getInvoiceIssueDate(String invoiceNumber) {
        Optional<TransactionRecord> recordOpt = transactionRecordRepository.findFirstByNumerFakturyOrderByIdDesc(invoiceNumber);

        if (recordOpt.isPresent() && recordOpt.get().getDataWystawienia() != null) {
            LocalDate issueDate = recordOpt.get().getDataWystawienia();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return issueDate.format(formatter);
        }

        return "";
    }

    public String getContractorEmail(String invoiceNumber) {
        Optional<TransactionRecord> recordOpt = transactionRecordRepository.findFirstByNumerFakturyOrderByIdDesc(invoiceNumber);

        if (recordOpt.isPresent()) {
            TransactionRecord record = recordOpt.get();
            Integer contractorId = record.getKontrahentId();
            if (contractorId != null) {
                Optional<FKKontrahenci> contractorOpt = fkKontrahenciRepository.findById(contractorId);
                if (contractorOpt.isPresent() && contractorOpt.get().getEmail() != null) {
                    return contractorOpt.get().getEmail();
                }
            }
        }
        return "";
    }

    public String getContractorName(String invoiceNumber) {
        Optional<TransactionRecord> recordOpt = transactionRecordRepository.findFirstByNumerFakturyOrderByIdDesc(invoiceNumber);

        if (recordOpt.isPresent()) {
            TransactionRecord record = recordOpt.get();
            Integer contractorId = record.getKontrahentId();
            if (contractorId != null) {
                Optional<FKKontrahenci> contractor = fkKontrahenciRepository.findById(contractorId);
                return contractor.map(FKKontrahenci::getNazwaKontrahenta).orElse("Brak kontrahenta");
            }
        }
        return "Brak kontrahenta";
    }

    public BigDecimal getGrossAmountForInvoice(String invoiceNumber) {
        Optional<TransactionRecord> recordOpt = transactionRecordRepository.findFirstByNumerFakturyOrderByIdDesc(invoiceNumber);
        if (recordOpt.isPresent()) {
            TransactionRecord record = recordOpt.get();
            return record.getKwota() != null ? record.getKwota() : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    public String getDueDate(String invoiceNumber) {
        Optional<TransactionRecord> recordOpt = transactionRecordRepository.findFirstByNumerFakturyOrderByIdDesc(invoiceNumber);
        if (recordOpt.isPresent() && recordOpt.get().getTerminPlatnosci() != null) {
            LocalDate dueDate = recordOpt.get().getTerminPlatnosci();
            return dueDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }
        return "";
    }

    public List<TransactionRecord> getOverdueUnpaidInvoices(LocalDate fromDate, LocalDate toDate) {
        List<TransactionRecord> records = transactionRecordRepository.findOverdueUnpaidInvoices();

        // Filtr FVS i od 2021
        return records.stream()
                .filter(r -> r.getNumerFaktury() != null && r.getNumerFaktury().contains("FVS"))
                .filter(r -> r.getDataWystawienia() != null && r.getDataWystawienia().getYear() >= 2021)
                .collect(Collectors.toList());
    }

    public List<TransactionRecord> getFilteredInvoicesFrom2021AndFVS() {
        List<TransactionRecord> records = transactionRecordRepository.findOverdueUnpaidInvoices();
        return records.stream()
                .filter(r -> r.getNumerFaktury() != null && r.getNumerFaktury().contains("FVS"))
                .filter(r -> r.getDataWystawienia() == null || !r.getDataWystawienia().isBefore(LocalDate.of(2021, 1, 1)))
                .collect(Collectors.toCollection(ArrayList::new));  // mutowalna lista
    }
}