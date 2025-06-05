package individual.p_n_2.Repository.User;

import individual.p_n_2.Domain.User.InvoiceInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface InvoiceInteractionRepository extends JpaRepository<InvoiceInteraction, Long> {
    List<InvoiceInteraction> findByInvoiceNumberOrderByTimestampDesc(String invoiceNumber);
    int countByInvoiceNumberAndType(String invoiceNumber, String type);
    void deleteByInvoiceNumber(String invoiceNumber);

    @Query("SELECT COALESCE(SUM(i.emailSentCount), 0) FROM InvoiceInteraction i WHERE i.invoiceNumber = :invoiceNumber")
    int sumEmailSentCount(@Param("invoiceNumber") String invoiceNumber);

    Optional<InvoiceInteraction> findFirstByInvoiceNumberAndType(String invoiceNumber, String type);

    List<InvoiceInteraction> findByInvoiceNumberAndTypeOrderByTimestampDesc(String invoiceNumber, String type);





}
