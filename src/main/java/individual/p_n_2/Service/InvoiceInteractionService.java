package individual.p_n_2.Service;

import individual.p_n_2.Domain.User.InvoiceInteraction;
import individual.p_n_2.Repository.User.InvoiceInteractionRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceInteractionService {

    private final InvoiceInteractionRepository invoiceInteractionRepository;
    private final UserService userService;

    public InvoiceInteractionService(
            InvoiceInteractionRepository invoiceInteractionRepository,
            UserService userService
    ) {
        this.invoiceInteractionRepository = invoiceInteractionRepository;
        this.userService = userService;
    }

    public void addComment(String invoiceNumber, String comment) {
        InvoiceInteraction interaction = new InvoiceInteraction();
        interaction.setInvoiceNumber(invoiceNumber);
        interaction.setType("comment");
        interaction.setValue(comment);
        interaction.setTimestamp(LocalDateTime.now());

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String fullName = userService.findByEmail(userEmail).getFullName();
        interaction.setCreatedBy(fullName);

        invoiceInteractionRepository.save(interaction);
    }

    public void addPhoneCall(String invoiceNumber) {
        InvoiceInteraction interaction = new InvoiceInteraction();
        interaction.setInvoiceNumber(invoiceNumber);
        interaction.setType("phone");
        interaction.setValue(null);
        interaction.setTimestamp(LocalDateTime.now());

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String fullName = userService.findByEmail(userEmail).getFullName();
        interaction.setCreatedBy(fullName);

        invoiceInteractionRepository.save(interaction);
    }

    public List<InvoiceInteraction> getHistory(String invoiceNumber) {
        return invoiceInteractionRepository.findByInvoiceNumberOrderByTimestampDesc(invoiceNumber);
    }

    public Map<String, Long> getPhoneCallCounts() {
        List<InvoiceInteraction> all = invoiceInteractionRepository.findAll();
        return all.stream()
                .filter(i -> "phone".equals(i.getType()))
                .collect(Collectors.groupingBy(
                        InvoiceInteraction::getInvoiceNumber,
                        Collectors.counting()
                ));
    }

    public int countPhoneCallsForInvoice(String invoiceNumber) {
        return invoiceInteractionRepository.countByInvoiceNumberAndType(invoiceNumber, "phone");
    }

    public int countCommentsForInvoice(String invoiceNumber) {
        return invoiceInteractionRepository.countByInvoiceNumberAndType(invoiceNumber, "comment");
    }

    @Transactional
    public void deleteHistoryForInvoice(String invoiceNumber) {
        invoiceInteractionRepository.deleteByInvoiceNumber(invoiceNumber);
    }

    @Transactional
    public void incrementEmailSentCount(String invoiceNumber) {
        Optional<InvoiceInteraction> emailInteractionOpt = invoiceInteractionRepository
                .findFirstByInvoiceNumberAndType(invoiceNumber, "email");

        if (emailInteractionOpt.isPresent()) {
            InvoiceInteraction emailInteraction = emailInteractionOpt.get();
            emailInteraction.setEmailSentCount(emailInteraction.getEmailSentCount() + 1);
            invoiceInteractionRepository.save(emailInteraction);
        } else {
            InvoiceInteraction emailInteraction = new InvoiceInteraction();
            emailInteraction.setInvoiceNumber(invoiceNumber);
            emailInteraction.setType("email");
            emailInteraction.setEmailSentCount(1);
            emailInteraction.setTimestamp(LocalDateTime.now());

            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            String fullName = userService.findByEmail(userEmail).getFullName();
            emailInteraction.setCreatedBy(fullName);

            invoiceInteractionRepository.save(emailInteraction);
        }
    }

    public int getEmailSentCountForInvoice(String invoiceNumber) {
        return invoiceInteractionRepository.sumEmailSentCount(invoiceNumber);
    }

    public int sumEmailSentCount(String invoiceNumber) {
        List<InvoiceInteraction> interactions = invoiceInteractionRepository.findByInvoiceNumberOrderByTimestampDesc(invoiceNumber);
        return interactions.stream()
                .filter(i -> "email".equals(i.getType()))
                .filter(i -> i.getEmailSentCount() != null && i.getEmailSentCount() > 0)
                .mapToInt(InvoiceInteraction::getEmailSentCount)
                .sum();
    }

    /**
     * NOWA METODA:
     * Pobiera mapę sumowanych liczników wysłanych emaili dla wszystkich faktur.
     */
    public Map<String, Integer> getEmailSentCountsForAllInvoices() {
        List<InvoiceInteraction> interactions = invoiceInteractionRepository.findAll();
        return interactions.stream()
                .filter(i -> i.getEmailSentCount() != null && i.getEmailSentCount() > 0)
                .collect(Collectors.groupingBy(
                        InvoiceInteraction::getInvoiceNumber,
                        Collectors.summingInt(InvoiceInteraction::getEmailSentCount)
                ));
    }

}