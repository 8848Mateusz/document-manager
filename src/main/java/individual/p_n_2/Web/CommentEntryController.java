package individual.p_n_2.Web;

import individual.p_n_2.Domain.CommentEntry;
import individual.p_n_2.Service.CommentEntryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentEntryController {

    private final CommentEntryService service;

    public CommentEntryController(CommentEntryService service) {
        this.service = service;
    }

    @PostMapping
    public CommentEntry addEntry(@RequestBody CommentEntry entry) {
        return service.saveComment(entry);
    }

    @GetMapping("/{invoiceNumber}")
    public List<CommentEntry> getEntries(@PathVariable String invoiceNumber) {
        return service.getCommentsForInvoice(invoiceNumber);
    }
}
