package unicam.calendario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unicam.SQLService;
import unicam.hackathon.Hackathon;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GestioneCall {
    private final SQLService SQL;

    @Autowired
    public GestioneCall(SQLService sqlService) {
        this.SQL = sqlService;
    }

    public boolean proponiCall(String mentoreEmail, String hackathonNome, 
                               String titolo, String descrizione, Date dataOra, String luogo,
                               String teamNome) {
        if (mentoreEmail == null || hackathonNome == null || 
            titolo == null || titolo.trim().isEmpty() || dataOra == null ||
            teamNome == null || teamNome.trim().isEmpty()) {
            return false;
        }

        // Verifica che l'hackathon esista
        Hackathon hackathon = SQL.dettagliHackathon(hackathonNome);
        if (hackathon == null) {
            return false;
        }

        Call call = new Call(mentoreEmail, hackathonNome, titolo, descrizione, dataOra, luogo, teamNome);
        return SQL.salvaCall(call);
    }

    public List<Call> visualizzaCall(String hackathonNome) {
        return SQL.getCallByHackathon(hackathonNome);
    }

    public List<Call> visualizzaCallMentore(String mentoreEmail) {
        return SQL.getCallByMentore(mentoreEmail);
    }

    public boolean eliminaCall(Long callId, String mentoreEmail) {
        Optional<Call> optCall = SQL.getCall(callId);
        if (optCall.isEmpty()) {
            return false;
        }

        Call call = optCall.get();
        // Verifica che il mentore sia il proprietario della call
        if (!call.getMentoreEmail().equals(mentoreEmail)) {
            return false;
        }

        return SQL.eliminaCall(callId);
    }

    public boolean modificaCall(Long callId, String mentoreEmail, 
                                String nuovoTitolo, String nuovaDescrizione, 
                                Date nuovaDataOra, String nuovoLuogo) {
        Optional<Call> optCall = SQL.getCall(callId);
        if (optCall.isEmpty()) {
            return false;
        }

        Call call = optCall.get();
        // Verifica che il mentore sia il proprietario della call
        if (!call.getMentoreEmail().equals(mentoreEmail)) {
            return false;
        }

        if (nuovoTitolo != null && !nuovoTitolo.trim().isEmpty()) {
            call.setTitolo(nuovoTitolo);
        }
        if (nuovaDescrizione != null) {
            call.setDescrizione(nuovaDescrizione);
        }
        if (nuovaDataOra != null) {
            call.setDataOra(nuovaDataOra);
        }
        if (nuovoLuogo != null) {
            call.setLuogo(nuovoLuogo);
        }

        return SQL.salvaCall(call);
    }
}
