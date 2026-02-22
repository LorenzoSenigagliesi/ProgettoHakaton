package unicam.SQLInterface;

import org.springframework.data.jpa.repository.JpaRepository;
import unicam.hackathon.MentoriHackathon;

import java.util.List;

public interface TabellaMentoriHackathon extends JpaRepository<MentoriHackathon, String> {
    List<MentoriHackathon> findByHackathon(String hackathon);
}
