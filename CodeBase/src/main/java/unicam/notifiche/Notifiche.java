package unicam.notifiche;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "notifiche")
public class Notifiche {
    @Id
    @Column(name = "type", nullable = false)
    private TipoNotifica tipo;
    @Column(name = "destina", nullable = false)
    private String destinatario;
    @Column(name = "mittente", nullable = false)
    private String mittente;
    @Column(name = "messaggio", nullable = false)
    private String messaggio;

    public Notifiche(TipoNotifica tipo, String messaggio, String mittente, String destinatario) {
        this.tipo = tipo;
        this.messaggio = messaggio;
        this.mittente = mittente;
        this.destinatario = destinatario;
    }
}
