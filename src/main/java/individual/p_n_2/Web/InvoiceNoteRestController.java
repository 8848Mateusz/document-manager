package individual.p_n_2.Web;

import individual.p_n_2.Domain.InvoiceNote;
import individual.p_n_2.Service.InvoiceNoteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class InvoiceNoteRestController {

    private final InvoiceNoteService service;

    public InvoiceNoteRestController(InvoiceNoteService service) {
        this.service = service;
    }

    @PostMapping
    public InvoiceNote addNote(@RequestBody InvoiceNote note) {
        return service.saveNote(note);
    }

    @GetMapping("/{invoiceNumber}")
    public List<InvoiceNote> getNotes(@PathVariable String invoiceNumber) {
        return service.getNotesForInvoice(invoiceNumber);
    }
}