package individual.p_n_2.Domain.Symfonia;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pn", schema = "hm")
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "kod")
    private String numerFaktury;

    @Column(name = "data")
    private LocalDate dataWystawienia;

    @Column(name = "termin")
    private LocalDate terminPlatnosci;

    @Column(name = "khid")
    private Integer kontrahentId;

    @Column(name = "kwota")
    private BigDecimal kwota;

    @Column(name = "ok")
    private Integer rozliczona;  // 1 = rozliczona, 0 = nie rozliczona

    @Column(name = "kwotarozl")
    private BigDecimal kwotaRozliczona;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumerFaktury() { return numerFaktury; }
    public void setNumerFaktury(String numerFaktury) { this.numerFaktury = numerFaktury; }

    public LocalDate getDataWystawienia() { return dataWystawienia; }
    public void setDataWystawienia(LocalDate dataWystawienia) { this.dataWystawienia = dataWystawienia; }

    public LocalDate getTerminPlatnosci() { return terminPlatnosci; }
    public void setTerminPlatnosci(LocalDate terminPlatnosci) { this.terminPlatnosci = terminPlatnosci; }

    public Integer getKontrahentId() { return kontrahentId; }
    public void setKontrahentId(Integer kontrahentId) { this.kontrahentId = kontrahentId; }

    public BigDecimal getKwota() { return kwota != null ? kwota : BigDecimal.ZERO; }
    public void setKwota(BigDecimal kwota) { this.kwota = kwota; }

    public Integer getRozliczona() { return rozliczona; }
    public void setRozliczona(Integer rozliczona) { this.rozliczona = rozliczona; }

    public BigDecimal getKwotaRozliczona() { return kwotaRozliczona != null ? kwotaRozliczona : BigDecimal.ZERO; }
    public void setKwotaRozliczona(BigDecimal kwotaRozliczona) { this.kwotaRozliczona = kwotaRozliczona; }
}