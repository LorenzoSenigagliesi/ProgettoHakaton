package unicam.account;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
@Getter

public class MembroTeam extends Utente{
    /**
     * TODO: metodi da implementare
     *       - IscrizioneTeam()
     *       - InviaUtenti()
     *       - InviaSottomissioni()
     *       - RichiestaSupporto()
     */
    private String team; //vincolo con il Team

    public MembroTeam(@NotNull String userName, @NotNull String email, @NotNull String password, @NotNull String team) {
        super(userName, email, password);
        this.team = team;
    }


}
