package individual.p_n_2.Repository;


import individual.p_n_2.Domain.CommentEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentEntryRepository extends JpaRepository<CommentEntry, Long> {
    List<CommentEntry> findByInvoiceNumberOrderByTimestampDesc(String invoiceNumber);
}