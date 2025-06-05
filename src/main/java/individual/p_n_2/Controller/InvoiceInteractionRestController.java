package individual.p_n_2.Controller;

import individual.p_n_2.Domain.User.InvoiceInteraction;
import individual.p_n_2.Repository.User.InvoiceInteractionRepository;
import individual.p_n_2.Service.InvoiceInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invoice-interaction")
public class InvoiceInteractionRestController {

    private final InvoiceInteractionService invoiceInteractionService;

    public InvoiceInteractionRestController(InvoiceInteractionService invoiceInteractionService) {
        this.invoiceInteractionService = invoiceInteractionService;
    }

    @Autowired
    private InvoiceInteractionService service;

    @Autowired
    private InvoiceInteractionRepository invoiceInteractionRepository;

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

    @GetMapping("/email-history")
    public List<Map<String, Object>> getEmailHistory(@RequestParam String invoiceNumber) {
        List<InvoiceInteraction> interactions = invoiceInteractionService.getEmailHistoryForInvoice(invoiceNumber);
        List<Map<String, Object>> result = new ArrayList<>();

        for (InvoiceInteraction interaction : interactions) {
            Map<String, Object> row = new HashMap<>();
            row.put("createdBy", interaction.getCreatedBy());
            row.put("timestamp", interaction.getTimestamp());
            result.add(row);
        }

        return result;
    }

    @GetMapping("/api/invoice-interaction/history")
    public ResponseEntity<List<InvoiceInteraction>> getInvoiceHistory(@RequestParam("invoiceNumber") String invoiceNumber) {
        List<InvoiceInteraction> interactions = invoiceInteractionRepository.findByInvoiceNumberOrderByTimestampDesc(invoiceNumber);
        return ResponseEntity.ok(interactions);
    }
}