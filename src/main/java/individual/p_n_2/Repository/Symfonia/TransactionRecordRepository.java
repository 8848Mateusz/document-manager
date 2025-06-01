package individual.p_n_2.Repository.Symfonia;

import individual.p_n_2.Domain.Symfonia.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long> {

    Optional<TransactionRecord> findFirstByNumerFakturyOrderByIdDesc(String numerFaktury);

    boolean existsByNumerFakturyAndDatarozlIsNull(String numerFaktury);

    List<TransactionRecord> findByDatarozlIsNullAndDataplatBefore(LocalDate date);

    List<TransactionRecord> findAllByNumerFakturyIn(List<String> numerFakturyList);

    @Query("SELECT tr FROM TransactionRecord tr WHERE tr.numerFaktury = :numerFaktury AND tr.datarozl IS NULL AND tr.dataplat < CURRENT_DATE")
    List<TransactionRecord> findUnpaidAndOverdueByNumerFaktury(@Param("numerFaktury") String numerFaktury);
}