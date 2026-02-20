package unicam.SQLInterface;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.account.Team;

import java.util.Optional;

@Repository
public interface TabellaTeam extends JpaRepository<Team, String> {
    Optional<Team> findByNome(String nome);
}
