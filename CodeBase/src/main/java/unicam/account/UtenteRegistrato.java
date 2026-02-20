package unicam.account;

import java.util.Objects;
import jakarta.persistence.*;

@Entity
@Table(name = "utenti")
public class UtenteRegistrato implements UtenteGenerico {

    @Id
    @Column(name = "username", nullable = false, unique = true)
    private String userName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "team")
    private String team;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoUtente Tipo;

    @Column(name = "salvadanaio")
    private Double Salvadanaio;

    // Costruttore vuoto richiesto da JPA
    protected UtenteRegistrato() {}

    public UtenteRegistrato(String userName, String email, String password) {
        if (userName.isBlank()) {
            throw new IllegalArgumentException("Username non può essere nullo o vuoto");
        }
        if (userName.length() < 3 || userName.length() > 50) {
            throw new IllegalArgumentException("Username deve essere tra 3 e 50 caratteri");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) { throw new IllegalArgumentException("Formato email errato"); }
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.team = null;
        Tipo = TipoUtente.Utente;
        Salvadanaio = 0.0;
    }

    public void setTeam(String team) {
        this.team = team;
        if (this.team == null){
            Tipo = TipoUtente.Utente;
        }else{
            Tipo = TipoUtente.MembroTeam;
        }
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getTeam() {
        return team;
    }

    public TipoUtente getTipo() {
        return Tipo;
    }

    // Metodi Object
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UtenteRegistrato utente = (UtenteRegistrato) o;
        return Objects.equals(userName, utente.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }

    @Override
    public String toString() {
        return "Utente{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
