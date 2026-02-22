package unicam.SQLInterface;

import org.springframework.data.jpa.repository.JpaRepository;
import unicam.hackathon.MentoriHackathon;

public interface TabellaMentoriHackathon extends JpaRepository<MentoriHackathon, String> {

}
