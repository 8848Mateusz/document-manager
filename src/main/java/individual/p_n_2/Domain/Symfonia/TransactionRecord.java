package individual.p_n_2.Domain.Symfonia;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "TR", schema = "HM")
public class TransactionRecord {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "kod")
    private String numerFaktury;  // Numer faktury w kolumnie 'kod'

    @Column(name = "wartosc_rozl")
    private BigDecimal wartoscRozl;

    @Column(name = "kwota")
    private BigDecimal kwota;

    @Column(name = "dataplat")
    private LocalDate dataplat;

    @Column(name = "datarozl")
    private LocalDate datarozl;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumerFaktury() { return numerFaktury; }
    public void setNumerFaktury(String numerFaktury) { this.numerFaktury = numerFaktury; }

    public BigDecimal getWartoscRozl() {
        return wartoscRozl != null ? wartoscRozl : BigDecimal.ZERO;
    }

    public void setWartoscRozl(BigDecimal wartoscRozl) {
        this.wartoscRozl = wartoscRozl;
    }

    public BigDecimal getKwota() { return kwota; }
    public void setKwota(BigDecimal kwota) { this.kwota = kwota; }

    public LocalDate getDataplat() { return dataplat; }
    public void setDataplat(LocalDate dataplat) { this.dataplat = dataplat; }

    public LocalDate getDatarozl() { return datarozl; }
    public void setDatarozl(LocalDate datarozl) { this.datarozl = datarozl; }
}