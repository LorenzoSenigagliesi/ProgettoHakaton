package unicam.SQLInterface;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.supporto.RichiesteSupporto;
import unicam.supporto.StatoRichiesta;

import java.util.List;

@Repository
public interface TabellaRichiesteSupporto extends JpaRepository<RichiesteSupporto, Long> {
    List<RichiesteSupporto> findByHackathonNome(String hackathonNome);
    List<RichiesteSupporto> findByMentoreEmail(String mentoreEmail);
    List<RichiesteSupporto> findByTeamNome(String teamNome);
    List<RichiesteSupporto> findByHackathonNomeAndStato(String hackathonNome, StatoRichiesta stato);
}
