package unicam.SQLInterface;

import org.springframework.data.jpa.repository.JpaRepository;
import unicam.hackathon.Iscrizioni;
import unicam.hackathon.MentoriHackathon;

import java.util.List;

public interface TabellaIscrizioni extends JpaRepository<Iscrizioni, String> {
    List<Iscrizioni> findByHackathon(String hackathon);
}
