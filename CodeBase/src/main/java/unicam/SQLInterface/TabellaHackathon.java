package unicam.SQLInterface;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackathon.Hackathon;

@Repository
public interface TabellaHackathon extends JpaRepository<Hackathon, String> {
    boolean existsByNome(String nome);
}
