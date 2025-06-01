package individual.p_n_2.Controller;

import individual.p_n_2.Domain.User.InvoiceInteraction;
import individual.p_n_2.Service.InvoiceInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/counts")
    public Map<String, Integer> getCounts(@RequestParam String invoiceNumber) {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("phoneCalls", service.countPhoneCallsForInvoice(invoiceNumber));
        counts.put("comments", service.countCommentsForInvoice(invoiceNumber));
        return counts;
    }
}