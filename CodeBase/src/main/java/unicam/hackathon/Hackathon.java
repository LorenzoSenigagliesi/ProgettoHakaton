package unicam.hackathon;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "hackathon")
public class Hackathon {

    @Id
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Column(name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date Data;

    @Column(name = "luogo")
    private String Luogo;

    @Column(name = "regolamento")
    private String Regolamento;

    @Column(name = "data_fine")
    @Temporal(TemporalType.TIMESTAMP)
    private Date DataFine;

    @Column(name = "data_fine_iscrizioni")
    @Temporal(TemporalType.TIMESTAMP)
    private Date DataFineIscrizioni;

    @Column(name = "dim_team")
    private int DimTeam;

    @Column(name = "stato")
    private String stato;

    //TODO: Da implementare in tabelle a parte
    //private Team teamIscritti[];
    //private Giudice giudice;
    //private Organizzatore organizzatore;
    //private Mentore mentori[];



    //getter & setter

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getData() {
        return Data;
    }

    public void setData(Date data) {
        this.Data = data;
    }

    public Date getDataFine() {
        return DataFine;
    }

    public void setDataFine(Date dataFine) {
        this.DataFine = dataFine;
    }

    public Date getDataFineIscrizioni() {
        return DataFineIscrizioni;
    }

    public void setDataFineIscrizioni(Date dataFineIscrizioni) {
        this.DataFineIscrizioni = dataFineIscrizioni;
    }

    public String getRegolamento() {
        return Regolamento;
    }

    public void setRegolamento(String regolamento) {
        this.Regolamento = regolamento;
    }

    public String getLuogo() {
        return Luogo;
    }

    public void setLuogo(String luogo) {
        this.Luogo = luogo;
    }

    public int getDimTeam() {
        return DimTeam;
    }

    public void setDimTeam(int dimTeam) {
        this.DimTeam = dimTeam;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}
