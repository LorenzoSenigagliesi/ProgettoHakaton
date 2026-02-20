package unicam.SQLInterface;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.account.UtenteRegistrato;

import java.util.Optional;

@Repository
public interface TabellaUtenti extends JpaRepository<UtenteRegistrato, String> {
    Optional<UtenteRegistrato> findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
}
