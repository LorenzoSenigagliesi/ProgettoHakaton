package unicam.amministrazione;
import jakarta.persistence.*;

@Entity
@Table(name = "amministrazione")
public class MembroStaff implements UtenzaAmministrazione {

    @Id
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    // Costruttore vuoto richiesto da JPA
    protected MembroStaff() {}

    public MembroStaff(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUsername() { return this.userName; }

    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }

    @Override
    public String getRuolo() {
        return "";
    }

    /**
     * accesso sottomissioni()
     * */

    /**
     * VisualizzazioneHackaton
     * */
}
