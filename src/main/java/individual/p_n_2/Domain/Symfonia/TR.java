package individual.p_n_2.Domain.Symfonia;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "TR_EXTENDED", schema = "HM")
public class TR {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "typ")
    private String typ;

    @Column(name = "subtyp")
    private String subtyp;

    @Column(name = "numer")  // numer faktury w TR_EXTENDED
    private String numer;

    @Column(name = "dokument")
    private String dokument;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "dataplat")
    private LocalDate dataPlat;

    @Column(name = "datarozl")
    private LocalDate dataRozliczenia;

    @Column(name = "netto")
    private BigDecimal netto;

    @Column(name = "brutto")
    private BigDecimal brutto;

    @Column(name = "status")
    private Integer status;

    @Column(name = "idkh")
    private Integer idKontrahenta;

    @Column(name = "owner_id")
    private Integer ownerId;

    @Column(name = "owner_base")
    private Integer ownerBase;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTyp() { return typ; }
    public void setTyp(String typ) { this.typ = typ; }
    public String getSubtyp() { return subtyp; }
    public void setSubtyp(String subtyp) { this.subtyp = subtyp; }
    public String getNumer() { return numer; }
    public void setNumer(String numer) { this.numer = numer; }
    public String getDokument() { return dokument; }
    public void setDokument(String dokument) { this.dokument = dokument; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public LocalDate getDataPlat() { return dataPlat; }
    public void setDataPlat(LocalDate dataPlat) { this.dataPlat = dataPlat; }
    public LocalDate getDataRozliczenia() { return dataRozliczenia; }
    public void setDataRozliczenia(LocalDate dataRozliczenia) { this.dataRozliczenia = dataRozliczenia; }
    public BigDecimal getNetto() { return netto; }
    public void setNetto(BigDecimal netto) { this.netto = netto; }
    public BigDecimal getBrutto() { return brutto; }
    public void setBrutto(BigDecimal brutto) { this.brutto = brutto; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getIdKontrahenta() { return idKontrahenta; }
    public void setIdKontrahenta(Integer idKontrahenta) { this.idKontrahenta = idKontrahenta; }
    public Integer getOwnerId() { return ownerId; }
    public void setOwnerId(Integer ownerId) { this.ownerId = ownerId; }
    public Integer getOwnerBase() { return ownerBase; }
    public void setOwnerBase(Integer ownerBase) { this.ownerBase = ownerBase; }
}