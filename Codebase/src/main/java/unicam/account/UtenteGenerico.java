package unicam.account;
import lombok.Getter;
import org.jetbrains.annotations.*;
@Getter
public interface UtenteGenerico {
    //questi possono essere non presenti poiche sono inizializzati a null
    String username = null;
    String email = null;
    String password = null;

    //vanno implementati in tutte le classi che implementano l'interfaccia
    public String toString();
    public int hashCode();
    public boolean equals(Object o);
}
