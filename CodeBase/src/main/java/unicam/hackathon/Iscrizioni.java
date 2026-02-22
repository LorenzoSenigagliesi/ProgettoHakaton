package unicam.hackathon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "iscrizioni")
public class Iscrizioni {
    @Id
    @Column(name = "team", nullable = false)
    private String team;

    @Column(name = "hackathon", nullable = false)
    private String hackathon;

    @Column(name = "sottomissioni")
    private String sottomissioni;

    @Column(name = "voto", nullable = false)
    private int voto;

    public Iscrizioni(String team, String hackathon, String sottomissioni, int voto) {
        this.team = team;
        this.hackathon = hackathon;
        this.sottomissioni = sottomissioni;
        this.voto = voto;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTeam() {
        return team;
    }

    public String getHackathon() {
        return hackathon;
    }

    public String getSottomissioni() {
        return sottomissioni;
    }

    public int getVoto() {
        return voto;
    }

    public void setHackathon(String hackathon) {
        this.hackathon = hackathon;
    }

    public void setSottomissioni(String sottomissioni) {
        this.sottomissioni = sottomissioni;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }
}
