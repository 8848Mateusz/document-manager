package individual.p_n_2.Domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class InvoiceNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNumber;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private boolean phoneContact;

    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean resolved;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public boolean isPhoneContact() {
        return phoneContact;
    }

    public void setPhoneContact(boolean phoneContact) {
        this.phoneContact = phoneContact;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

}
