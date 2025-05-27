package individual.p_n_2.Repository;


import individual.p_n_2.Domain.InvoiceNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceNoteRepository extends JpaRepository<InvoiceNote, Long> {
    List<InvoiceNote> findByInvoiceNumberOrderByCreatedAtDesc(String invoiceNumber);
}