package unicam.hackathon;

import java.util.Date;

public class Hackathon {
    private String nome;
    private Date Data;
    private String Luogo;
    private String Regolamento;
    private Date DataFine;
    private Date DataFineIscrizioni;
    private int DimTeam;
    private StatiHackathon Stato;
    //TODO: Da implementare in tabelle a parte
    //private Team teamIscritti[];
    //private Giudice giudice;
    //private Organizzatore organizzatore;
    //private Mentore mentori[];


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
        return Stato.getName();
    }
}
