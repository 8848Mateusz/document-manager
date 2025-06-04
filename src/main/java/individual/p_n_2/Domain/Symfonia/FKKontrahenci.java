package individual.p_n_2.Domain.Symfonia;

import jakarta.persistence.*;

@Entity
@Table(name = "fk_kontrahenci", schema = "FK")
public class FKKontrahenci {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "nazwa")
    private String nazwaKontrahenta;


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNazwaKontrahenta() { return nazwaKontrahenta; }
    public void setNazwaKontrahenta(String nazwaKontrahenta) { this.nazwaKontrahenta = nazwaKontrahenta; }
}
