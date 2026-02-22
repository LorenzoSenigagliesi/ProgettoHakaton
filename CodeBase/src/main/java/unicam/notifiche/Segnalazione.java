package unicam.notifiche;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "segnalazioni")
public class Segnalazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_nome", nullable = false)
    private String teamNome;

    @Column(name = "hackathon_nome", nullable = false)
    private String hackathonNome;

    @Column(name = "segnalante", nullable = false)
    private String segnalante;

    @Column(name = "motivo", length = 1000, nullable = false)
    private String motivo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_creazione", nullable = false)
    private Date dataCreazione;

    @Column(name = "gestita", nullable = false)
    private boolean gestita;

    @Column(name = "azione_intrapresa", length = 500)
    private String azioneIntrapresa;

    // Costruttore vuoto richiesto da JPA
    protected Segnalazione() {}

    public Segnalazione(String teamNome, String hackathonNome, String segnalante, String motivo) {
        this.teamNome = teamNome;
        this.hackathonNome = hackathonNome;
        this.segnalante = segnalante;
        this.motivo = motivo;
        this.dataCreazione = new Date();
        this.gestita = false;
    }

    public Long getId() {
        return id;
    }

    public String getTeamNome() {
        return teamNome;
    }

    public String getHackathonNome() {
        return hackathonNome;
    }

    public String getSegnalante() {
        return segnalante;
    }

    public String getMotivo() {
        return motivo;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public boolean isGestita() {
        return gestita;
    }

    public void setGestita(boolean gestita) {
        this.gestita = gestita;
    }

    public String getAzioneIntrapresa() {
        return azioneIntrapresa;
    }

    public void setAzioneIntrapresa(String azioneIntrapresa) {
        this.azioneIntrapresa = azioneIntrapresa;
    }
}
