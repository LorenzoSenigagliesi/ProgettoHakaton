package unicam.SQLInterface;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.amministrazione.MembroStaff;

import java.util.Optional;

@Repository
public interface TabellaAmministrazione extends JpaRepository<MembroStaff, String> {
    Optional<MembroStaff> findByEmailAndPassword(String email, String password);
}
