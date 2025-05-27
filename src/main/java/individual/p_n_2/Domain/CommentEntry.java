package individual.p_n_2.Domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CommentEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNumber;
    private String comment;
    private boolean phoneCall;
    private LocalDateTime timestamp;

    public CommentEntry() {
        this.timestamp = LocalDateTime.now();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public boolean isPhoneCall() {
        return phoneCall;
    }

    public void setPhoneCall(boolean phoneCall) {
        this.phoneCall = phoneCall;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

}