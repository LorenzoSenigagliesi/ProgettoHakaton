package unicam.account;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
@Getter

public class UtenteRegistrato implements UtenteGenerico {
    /**
     * TODO: metodi da implementare
     *      - AccettazioneInvito
     *      - CreaTeam
     *      - Logout
     * */
    private final String userName;
    private final String email;
    private final String password;
    private String team; //vincolo con il Team
    private TipoUtente Tipo;
    private Double Salvadanaio;

    public UtenteRegistrato(@NotNull String userName, @NotNull String email, @NotNull String password) {
        if (userName.isBlank()) {
            throw new IllegalArgumentException("Username non può essere nullo o vuoto");
        }
        if (userName.length() < 3 || userName.length() > 50) {
            throw new IllegalArgumentException("Username deve essere tra 3 e 50 caratteri");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) { throw new IllegalArgumentException("Formato email errato"); }
        this.userName=userName;
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
