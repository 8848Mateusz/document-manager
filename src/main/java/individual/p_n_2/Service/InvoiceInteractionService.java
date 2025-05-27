package individual.p_n_2.Service;

import individual.p_n_2.Domain.InvoiceInteraction;
import individual.p_n_2.Repository.InvoiceInteractionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InvoiceInteractionService {

    private final InvoiceInteractionRepository repository;

    public InvoiceInteractionService(InvoiceInteractionRepository repository) {
        this.repository = repository;
    }

    public void addComment(String invoiceNumber, String comment) {
        InvoiceInteraction interaction = new InvoiceInteraction();
        interaction.setInvoiceNumber(invoiceNumber);
        interaction.setType("comment");
        interaction.setValue(comment);
        interaction.setTimestamp(LocalDateTime.now());
        repository.save(interaction);
    }

    public void addPhoneCall(String invoiceNumber) {
        InvoiceInteraction interaction = new InvoiceInteraction();
        interaction.setInvoiceNumber(invoiceNumber);
        interaction.setType("phone");
        interaction.setValue(null);
        interaction.setTimestamp(LocalDateTime.now());
        repository.save(interaction);
    }

    public List<InvoiceInteraction> getHistory(String invoiceNumber) {
        return repository.findByInvoiceNumberOrderByTimestampDesc(invoiceNumber);
    }

    public Map<String, Long> getPhoneCallCounts() {
        List<InvoiceInteraction> all = repository.findAll();
        return all.stream()
                .filter(i -> "phone".equals(i.getType()))
                .collect(Collectors.groupingBy(
                        InvoiceInteraction::getInvoiceNumber,
                        Collectors.counting()
                ));
    }

    // Metoda do liczenia telefonów dla jednej faktury
    public int countPhoneCallsForInvoice(String invoiceNumber) {
        return repository.countByInvoiceNumberAndType(invoiceNumber, "phone");
    }
}