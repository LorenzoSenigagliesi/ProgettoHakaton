package unicam.SQLInterface;

import org.springframework.data.jpa.repository.JpaRepository;
import unicam.hackathon.Hackathon;
import unicam.notifiche.Notifiche;

public interface TabellaNotifiche extends JpaRepository<Notifiche, String> {
}
