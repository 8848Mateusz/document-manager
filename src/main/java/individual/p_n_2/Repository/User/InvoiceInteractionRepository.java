package individual.p_n_2.Repository.User;

import individual.p_n_2.Domain.User.InvoiceInteraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceInteractionRepository extends JpaRepository<InvoiceInteraction, Long> {
    List<InvoiceInteraction> findByInvoiceNumberOrderByTimestampDesc(String invoiceNumber);
    int countByInvoiceNumberAndType(String invoiceNumber, String type);
    void deleteByInvoiceNumber(String invoiceNumber);

}
