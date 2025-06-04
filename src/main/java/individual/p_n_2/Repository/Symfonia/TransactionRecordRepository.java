package individual.p_n_2.Repository.Symfonia;

import individual.p_n_2.Domain.Symfonia.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long> {

    Optional<TransactionRecord> findFirstByNumerFakturyOrderByIdDesc(String numerFaktury);

    @Query("SELECT tr FROM TransactionRecord tr WHERE tr.numerFaktury = :numerFaktury AND tr.rozliczona = 0 AND tr.terminPlatnosci < CURRENT_DATE")
    List<TransactionRecord> findUnpaidAndOverdueByNumerFaktury(@Param("numerFaktury") String numerFaktury);

    @Query("SELECT t.terminPlatnosci FROM TransactionRecord t WHERE t.numerFaktury = :invoiceNumber ORDER BY t.id DESC")
    Optional<LocalDate> findDueDateForInvoice(@Param("invoiceNumber") String invoiceNumber);

    @Query("SELECT tr FROM TransactionRecord tr WHERE tr.rozliczona = 0 AND tr.terminPlatnosci < CURRENT_DATE")
    List<TransactionRecord> findOverdueUnpaidInvoices();

    List<TransactionRecord> findAllByNumerFakturyIn(List<String> numerFakturyList);

    @Query("SELECT tr FROM TransactionRecord tr " +
            "WHERE tr.rozliczona = 0 " +
            "AND tr.terminPlatnosci < CURRENT_DATE " +
            "AND tr.dataWystawienia >= :startDate " +
            "AND tr.numerFaktury LIKE %:pattern%")
    List<TransactionRecord> findFilteredFromDateAndFVS(@Param("startDate") LocalDate startDate,
                                                       @Param("pattern") String pattern);

    default List<TransactionRecord> findFilteredFromDateAndFVS(LocalDate startDate) {
        return findFilteredFromDateAndFVS(startDate, "FVS");
    }

}