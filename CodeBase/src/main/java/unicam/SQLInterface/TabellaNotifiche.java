package unicam.SQLInterface;

import org.springframework.data.jpa.repository.JpaRepository;
import unicam.hackathon.Hackathon;
import unicam.notifiche.Notifiche;

import java.util.List;
import java.util.Optional;

public interface TabellaNotifiche extends JpaRepository<Notifiche, String> {
    Optional<Notifiche> findById(int s);
    List<Notifiche> findByDestinatario(String destinatario);
}
