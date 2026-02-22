package unicam.SQLInterface;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.calendario.Call;

import java.util.List;

@Repository
public interface TabellaCall extends JpaRepository<Call, Long> {
    List<Call> findByHackathonNome(String hackathonNome);
    List<Call> findByMentoreEmail(String mentoreEmail);
    List<Call> findByHackathonNomeOrderByDataOraAsc(String hackathonNome);
    List<Call> findByTeamNome(String teamNome);
}
