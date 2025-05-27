package individual.p_n_2.Service;

import individual.p_n_2.Domain.InvoiceNote;
import individual.p_n_2.Repository.InvoiceNoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceNoteService {

    private final InvoiceNoteRepository repository;

    public InvoiceNoteService(InvoiceNoteRepository repository) {
        this.repository = repository;
    }

    public InvoiceNote saveNote(InvoiceNote note) {
        return repository.save(note);
    }

    public List<InvoiceNote> getNotesForInvoice(String invoiceNumber) {
        return repository.findByInvoiceNumberOrderByCreatedAtDesc(invoiceNumber);
    }
}