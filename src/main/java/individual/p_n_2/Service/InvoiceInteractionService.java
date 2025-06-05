package individual.p_n_2.Service;

import individual.p_n_2.Domain.User.InvoiceInteraction;
import individual.p_n_2.Domain.User.User;
import individual.p_n_2.Repository.User.InvoiceInteractionRepository;
import individual.p_n_2.Repository.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceInteractionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvoiceInteractionRepository invoiceInteractionRepository;

    private final UserService userService;

    public InvoiceInteractionService(InvoiceInteractionRepository invoiceInteractionRepository, UserService userService) {
        this.invoiceInteractionRepository = invoiceInteractionRepository;
        this.userService = userService;
    }

    public void addComment(String invoiceNumber, String comment) {
        InvoiceInteraction interaction = new InvoiceInteraction();
        interaction.setInvoiceNumber(invoiceNumber);
        interaction.setType("comment");
        interaction.setValue(comment);
        interaction.setTimestamp(LocalDateTime.now());
        interaction.setCreatedBy(getCurrentUserFullName());
        invoiceInteractionRepository.save(interaction);
    }

    public void addPhoneCall(String invoiceNumber) {
        InvoiceInteraction interaction = new InvoiceInteraction();
        interaction.setInvoiceNumber(invoiceNumber);
        interaction.setType("phone");
        interaction.setTimestamp(LocalDateTime.now());
        interaction.setCreatedBy(getCurrentUserFullName());
        invoiceInteractionRepository.save(interaction);
    }

    public void saveEmailInteraction(String invoiceNumber, String username) {
        // Pobierz obiekt użytkownika
        Optional<User> userOpt = userRepository.findByEmail(username);
        String fullName = userOpt.map(u -> u.getFullName()).orElse(username);  // fallback: username

        InvoiceInteraction interaction = new InvoiceInteraction();
        interaction.setInvoiceNumber(invoiceNumber);
        interaction.setType("email");
        interaction.setTimestamp(LocalDateTime.now());
        interaction.setCreatedBy(fullName);  // zamiast username wstawiamy fullName
        invoiceInteractionRepository.save(interaction);
    }

    public List<InvoiceInteraction> getHistory(String invoiceNumber) {
        return invoiceInteractionRepository.findByInvoiceNumberOrderByTimestampDesc(invoiceNumber);
    }

    public List<InvoiceInteraction> getEmailHistoryForInvoice(String invoiceNumber) {
        return invoiceInteractionRepository.findByInvoiceNumberAndTypeOrderByTimestampDesc(invoiceNumber, "email");
    }

    public int countPhoneCallsForInvoice(String invoiceNumber) {
        return invoiceInteractionRepository.countByInvoiceNumberAndType(invoiceNumber, "phone");
    }

    public int countCommentsForInvoice(String invoiceNumber) {
        return invoiceInteractionRepository.countByInvoiceNumberAndType(invoiceNumber, "comment");
    }

    public int countEmailsSentForInvoice(String invoiceNumber) {
        return invoiceInteractionRepository.countByInvoiceNumberAndType(invoiceNumber, "email");
    }

    public Map<String, Integer> getEmailSentCountsForAllInvoices() {
        List<InvoiceInteraction> interactions = invoiceInteractionRepository.findAll();
        return interactions.stream()
                .filter(i -> "email".equals(i.getType()))
                .collect(Collectors.groupingBy(
                        InvoiceInteraction::getInvoiceNumber,
                        Collectors.summingInt(i -> 1)
                ));
    }

    private String getCurrentUserFullName() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByEmail(userEmail).getFullName();
    }

    public int sumEmailSentCount(String invoiceNumber) {
        List<InvoiceInteraction> interactions = invoiceInteractionRepository.findByInvoiceNumberAndTypeOrderByTimestampDesc(invoiceNumber, "email");
        return interactions.size();
    }
}