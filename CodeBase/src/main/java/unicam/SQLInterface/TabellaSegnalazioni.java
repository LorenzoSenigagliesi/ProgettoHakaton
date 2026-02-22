package unicam.SQLInterface;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.notifiche.Segnalazione;

import java.util.List;

@Repository
public interface TabellaSegnalazioni extends JpaRepository<Segnalazione, Long> {
    List<Segnalazione> findByHackathonNome(String hackathonNome);
    List<Segnalazione> findByTeamNome(String teamNome);
    List<Segnalazione> findByHackathonNomeAndGestita(String hackathonNome, boolean gestita);
}
