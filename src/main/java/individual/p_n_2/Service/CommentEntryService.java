package individual.p_n_2.Service;

import individual.p_n_2.Domain.CommentEntry;
import individual.p_n_2.Repository.CommentEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentEntryService {

    private final CommentEntryRepository repository;

    public CommentEntryService(CommentEntryRepository repository) {
        this.repository = repository;
    }

    public CommentEntry saveComment(CommentEntry entry) {
        return repository.save(entry);
    }

    public List<CommentEntry> getCommentsForInvoice(String invoiceNumber) {
        return repository.findByInvoiceNumberOrderByTimestampDesc(invoiceNumber);
    }
}