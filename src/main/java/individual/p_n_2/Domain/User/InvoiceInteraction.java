package individual.p_n_2.Domain.User;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class InvoiceInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNumber;

    private String type; // "comment" lub "phone"

    @Column(length = 1000)
    private String value; // treść komentarza lub null

    @Column(name = "created_by")
    private String createdBy;

    private LocalDateTime timestamp;

    public InvoiceInteraction() {
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}