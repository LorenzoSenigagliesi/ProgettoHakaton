package unicam.hackathon;

import java.util.Date;
import jakarta.persistence.*;
import unicam.account.Team;
import unicam.amministrazione.Giudice;
import unicam.amministrazione.Mentore;
import unicam.amministrazione.Organizzatore;

@Entity
@Table(name = "hackathon")
public class Hackathon {

    @Id
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Column(name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    @Column(name = "luogo")
    private String luogo;

    @Column(name = "regolamento")
    private String regolamento;

    @Column(name = "data_fine")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFine;

    @Column(name = "data_fine_iscrizioni")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFineIscrizioni;

    @Column(name = "dim_team")
    private int dimTeam;

    @Column(name = "stato")
    private StatoHackathon stato;

    @Column (name = "giudice")
    private String giudice;

    @Column (name = "organizzatore")
    private String organizzatore;


    //getter & setter

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getData() {
        return this.data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getDataFine() {
        return this.dataFine;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    public Date getDataFineIscrizioni() {
        return this.dataFineIscrizioni;
    }

    public void setDataFineIscrizioni(Date dataFineIscrizioni) {
        this.dataFineIscrizioni = dataFineIscrizioni;
    }

    public String getRegolamento() {
        return this.regolamento;
    }

    public void setRegolamento(String regolamento) {
        this.regolamento = regolamento;
    }

    public String getLuogo() {
        return this.luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public int getDimTeam() {
        return this.dimTeam;
    }

    public void setDimTeam(int dimTeam) {
        this.dimTeam = dimTeam;
    }

    public StatoHackathon getStato() {
        return stato;
    }

    public void setStato(StatoHackathon stato) {
        this.stato = stato;
    }

    public String getGiudice() {
        return giudice;
    }

    public void setGiudice(String giudice) {
        this.giudice = giudice;
    }

    public String getOrganizzatore() {
        return organizzatore;
    }

    public void setOrganizzatore(String organizzatore) {
        this.organizzatore = organizzatore;
    }

    public void cambiaStato(){
        switch (stato){
            case StatoHackathon.InIscrizione -> stato = StatoHackathon.InCorso;
            case StatoHackathon.InCorso -> stato = StatoHackathon.InValutazione;
            case StatoHackathon.InValutazione -> stato = StatoHackathon.Concluso;
        }
    }
}
