package unicam.SQLInterface;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.amministrazione.Mentore;
import unicam.hackathon.Hackathon;

import java.util.Optional;

@Repository
public interface TabellaHackathon extends JpaRepository<Hackathon, String> {
    boolean existsByNome(String nome);
    Optional<Hackathon> findByNome(String nome);
}
