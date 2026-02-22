package unicam.supporto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unicam.SQLService;
import unicam.hackathon.Hackathon;
import unicam.hackathon.MentoriHackathon;

import java.util.List;
import java.util.Optional;

@Service
public class GestioneSupporto {
    private final SQLService SQL;

    @Autowired
    public GestioneSupporto(SQLService sqlService) {
        this.SQL = sqlService;
    }

    public boolean inviaRichiestaSupporto(String teamNome, String hackathonNome, String messaggio) {
        if (teamNome == null || hackathonNome == null || messaggio == null || messaggio.trim().isEmpty()) {
            return false;
        }

        Hackathon hackathon = SQL.dettagliHackathon(hackathonNome);
        if (hackathon == null) {
            return false;
        }

        RichiesteSupporto richiesta = new RichiesteSupporto(teamNome, hackathonNome, messaggio);
        
        // Assegna un mentore se disponibile per questo hackathon
        List<MentoriHackathon> mentori = SQL.getMentoriHackathon(hackathonNome);
        if (!mentori.isEmpty()) {
            // Assegna il primo mentore disponibile (si può implementare logica più sofisticata)
            richiesta.setMentoreEmail(mentori.get(0).getEmail());
        }

        return SQL.salvaRichiestaSupporto(richiesta);
    }

    public List<RichiesteSupporto> visualizzaRichieste(String hackathonNome) {
        return SQL.getRichiesteSupportoByHackathon(hackathonNome);
    }

    public List<RichiesteSupporto> visualizzaRichiesteMentore(String mentoreEmail) {
        return SQL.getRichiesteSupportoByMentore(mentoreEmail);
    }

    public List<RichiesteSupporto> visualizzaRichiesteTeam(String teamNome) {
        return SQL.getRichiesteSupportoByTeam(teamNome);
    }

    public boolean rispondiRichiesta(Long richiestaId, String risposta, String mentoreEmail) {
        Optional<RichiesteSupporto> optRichiesta = SQL.getRichiestaSupporto(richiestaId);
        if (optRichiesta.isEmpty()) {
            return false;
        }

        RichiesteSupporto richiesta = optRichiesta.get();
        
        // Assegna il mentore se non ancora assegnato
        if (richiesta.getMentoreEmail() == null) {
            richiesta.setMentoreEmail(mentoreEmail);
        }
        
        richiesta.setRisposta(risposta);
        richiesta.setStato(StatoRichiesta.IN_CORSO);
        
        return SQL.salvaRichiestaSupporto(richiesta);
    }

    public boolean chiudiRichiesta(Long richiestaId) {
        Optional<RichiesteSupporto> optRichiesta = SQL.getRichiestaSupporto(richiestaId);
        if (optRichiesta.isEmpty()) {
            return false;
        }

        RichiesteSupporto richiesta = optRichiesta.get();
        richiesta.setStato(StatoRichiesta.RISOLTA);
        
        return SQL.salvaRichiestaSupporto(richiesta);
    }
}
