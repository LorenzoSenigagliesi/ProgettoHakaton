package unicam.hackathon;

import java.util.Date;

public class Hackathon {
    private String nome;
    private int id;
    private Date data;
    private Date dataFine;
    private Date dataFineIscrizioni;
    private String regolamento;
    private String luogo;
    //private Team teamIscritti[];
    private int dimTeam;
    //private Giudice giudice;
    //private Organizzatore organizzatore;
    //private Mentore mentori[];
    //private stato;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getDataFine() {
        return dataFine;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    public Date getDataFineIscrizioni() {
        return dataFineIscrizioni;
    }

    public void setDataFineIscrizioni(Date dataFineIscrizioni) {
        this.dataFineIscrizioni = dataFineIscrizioni;
    }

    public String getRegolamento() {
        return regolamento;
    }

    public void setRegolamento(String regolamento) {
        this.regolamento = regolamento;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public int getDimTeam() {
        return dimTeam;
    }

    public void setDimTeam(int dimTeam) {
        this.dimTeam = dimTeam;
    }
}
