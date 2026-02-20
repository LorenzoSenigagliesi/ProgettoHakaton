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

    public String getUserName() { return userName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    /**
     * accesso sottomissioni()
     * */

    /**
     * VisualizzazioneHackaton
     * */
}
