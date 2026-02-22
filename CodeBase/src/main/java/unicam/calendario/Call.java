package unicam.calendario;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "call")
public class Call {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mentore_email", nullable = false)
    private String mentoreEmail;

    @Column(name = "hackathon_nome", nullable = false)
    private String hackathonNome;

    @Column(name = "titolo", nullable = false, length = 200)
    private String titolo;

    @Column(name = "descrizione", length = 1000)
    private String descrizione;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_ora", nullable = false)
    private Date dataOra;

    @Column(name = "luogo", length = 500)
    private String luogo;

    @Column(name = "team_nome")
    private String teamNome;

    // Costruttore vuoto richiesto da JPA
    protected Call() {}

    public Call(String mentoreEmail, String hackathonNome, String titolo, 
                String descrizione, Date dataOra, String luogo, String teamNome) {
        this.mentoreEmail = mentoreEmail;
        this.hackathonNome = hackathonNome;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataOra = dataOra;
        this.luogo = luogo;
        this.teamNome = teamNome;
    }

    public Long getId() {
        return id;
    }

    public String getMentoreEmail() {
        return mentoreEmail;
    }

    public String getHackathonNome() {
        return hackathonNome;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getDataOra() {
        return dataOra;
    }

    public void setDataOra(Date dataOra) {
        this.dataOra = dataOra;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public String getTeamNome() {
        return teamNome;
    }

    public void setTeamNome(String teamNome) {
        this.teamNome = teamNome;
    }
}
