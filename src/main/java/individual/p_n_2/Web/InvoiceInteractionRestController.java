package individual.p_n_2.Web;

import individual.p_n_2.Domain.InvoiceInteraction;
import individual.p_n_2.Service.InvoiceInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice-interaction")
public class InvoiceInteractionRestController {

    @Autowired
    private InvoiceInteractionService service;

    @PostMapping("/comment")
    public void addComment(@RequestParam String invoiceNumber,
                           @RequestParam String comment) {
        service.addComment(invoiceNumber, comment);
    }

    @PostMapping("/phone")
    public void addPhoneCall(@RequestParam String invoiceNumber) {
        service.addPhoneCall(invoiceNumber);
    }

    @GetMapping("/history")
    public List<InvoiceInteraction> getHistory(@RequestParam String invoiceNumber) {
        return service.getHistory(invoiceNumber);
    }
}