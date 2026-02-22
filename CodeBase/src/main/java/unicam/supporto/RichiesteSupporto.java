package unicam.supporto;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "richieste_supporto")
public class RichiesteSupporto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_nome", nullable = false)
    private String teamNome;

    @Column(name = "mentore_email")
    private String mentoreEmail;

    @Column(name = "hackathon_nome", nullable = false)
    private String hackathonNome;

    @Column(name = "messaggio", length = 1000)
    private String messaggio;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato", nullable = false)
    private StatoRichiesta stato;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_creazione", nullable = false)
    private Date dataCreazione;

    @Column(name = "risposta", length = 1000)
    private String risposta;

    // Costruttore vuoto richiesto da JPA
    protected RichiesteSupporto() {}

    public RichiesteSupporto(String teamNome, String hackathonNome, String messaggio) {
        this.teamNome = teamNome;
        this.hackathonNome = hackathonNome;
        this.messaggio = messaggio;
        this.stato = StatoRichiesta.APERTA;
        this.dataCreazione = new Date();
    }

    public Long getId() {
        return id;
    }

    public String getTeamNome() {
        return teamNome;
    }

    public String getMentoreEmail() {
        return mentoreEmail;
    }

    public void setMentoreEmail(String mentoreEmail) {
        this.mentoreEmail = mentoreEmail;
    }

    public String getHackathonNome() {
        return hackathonNome;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public StatoRichiesta getStato() {
        return stato;
    }

    public void setStato(StatoRichiesta stato) {
        this.stato = stato;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public String getRisposta() {
        return risposta;
    }

    public void setRisposta(String risposta) {
        this.risposta = risposta;
    }
}
