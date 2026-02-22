package unicam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unicam.SQLInterface.*;
import unicam.account.*;
import unicam.amministrazione.*;
import unicam.hackathon.Hackathon;
import unicam.hackathon.Iscrizioni;
import unicam.hackathon.MentoriHackathon;
import unicam.notifiche.Notifiche;
import unicam.notifiche.Segnalazione;
import unicam.supporto.RichiesteSupporto;

import java.util.List;
import java.util.Optional;

@Service
public class SQLService {

    private final TabellaUtenti tabellaUtenti;
    private final TabellaAmministrazione tabellaAmministrazione;
    private final TabellaHackathon tabellaHackathon;
    private final TabellaTeam tabellaTeam;
    private final TabellaMentoriHackathon tabellaMentoriHackathon;
    private final TabellaNotifiche tabellaNotifiche;
    private final TabellaIscrizioni tabellaIscrizioni;
    private final TabellaRichiesteSupporto tabellaRichiesteSupporto;
    private final TabellaCall tabellaCall;
    private final TabellaSegnalazioni tabellaSegnalazioni;
    @Autowired
    public SQLService(TabellaUtenti tabellaUtenti,
                      TabellaAmministrazione tabellaAmministrazione,
                      TabellaHackathon tabellaHackathon,
                      TabellaTeam tabellaTeam,
                      TabellaMentoriHackathon tabellaMentoriHackathon, 
                      TabellaNotifiche tabellaNotifiche, 
                      TabellaIscrizioni tabellaIscrizioni,
                      TabellaRichiesteSupporto tabellaRichiesteSupporto,
                      TabellaCall tabellaCall,
                      TabellaSegnalazioni tabellaSegnalazioni) {
        this.tabellaUtenti = tabellaUtenti;
        this.tabellaAmministrazione = tabellaAmministrazione;
        this.tabellaHackathon = tabellaHackathon;
        this.tabellaTeam = tabellaTeam;
        this.tabellaMentoriHackathon = tabellaMentoriHackathon;
        this.tabellaNotifiche = tabellaNotifiche;
        this.tabellaIscrizioni = tabellaIscrizioni;
        this.tabellaRichiesteSupporto = tabellaRichiesteSupporto;
        this.tabellaCall = tabellaCall;
        this.tabellaSegnalazioni = tabellaSegnalazioni;
    }

    public List<Hackathon> getAllHackathons() {
        return tabellaHackathon.findAll();
    }

    public boolean HackathonExist(String hackatonName) {
        return tabellaHackathon.existsByNome(hackatonName);
    }

    public boolean salvaHackathon(Hackathon newHackaton) {
        try {
            tabellaHackathon.save(newHackaton);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Hackathon dettagliHackathon(String nomeHackathon){
        try {
            return tabellaHackathon.findByNome(nomeHackathon).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean salvaMentori(MentoriHackathon mentore){
        try {
            tabellaMentoriHackathon.save(mentore);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public UtenteRegistrato loginUtenti(String email, String password) {
        Optional<UtenteRegistrato> utente = tabellaUtenti.findByEmailAndPassword(email, password);
        return utente.orElse(null);
    }

    public MembroStaff loginAmministrazione(String email, String password) {
        Optional<MembroStaff> membro = tabellaAmministrazione.findByEmailAndPassword(email, password);
        return membro.orElse(null);
    }

    public boolean salvaUtente(UtenteRegistrato utente) {
        try {
            tabellaUtenti.save(utente);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkAccount(String email) {
        return tabellaUtenti.existsByEmail(email);
    }

    public Team readTeam(String nome) {
        Optional<Team> team = tabellaTeam.findByNome(nome);
        return team.orElse(null);
    }

    public boolean creaTeam(Team team, UtenteRegistrato utente) {
        try {
            tabellaTeam.save(team);
            utente.setTeam(team.getNome());
            tabellaUtenti.save(utente);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean salvaNotifica(Notifiche Notifica) {
        try {
            tabellaNotifiche.save(Notifica);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Iscrizioni> getAllIscrizioni(String NomeHackathon) {
        return tabellaIscrizioni.findByHackathon(NomeHackathon);
    }

    public boolean salvaIscrizione(Iscrizioni Iscrizione) {
        try {
            tabellaIscrizioni.save(Iscrizione);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Notifiche findNotifiche(int id) {
        return tabellaNotifiche.findById(id).orElse(null);
    }

    public List<Notifiche> getNotifichePerUtente(String destinatario) {
        return tabellaNotifiche.findByDestinatario(destinatario);
    }

    public UtenteRegistrato findUtenteByEmail(String email) {
        return tabellaUtenti.findByEmail(email).orElse(null);
    }

    public List<UtenteRegistrato> getMembriTeam(String nomeTeam) {
        return tabellaUtenti.findByTeam(nomeTeam);
    }

    // Metodi per RichiesteSupporto
    public boolean salvaRichiestaSupporto(RichiesteSupporto richiesta) {
        try {
            tabellaRichiesteSupporto.save(richiesta);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<RichiesteSupporto> getRichiesteSupportoByHackathon(String hackathonNome) {
        return tabellaRichiesteSupporto.findByHackathonNome(hackathonNome);
    }

    public List<RichiesteSupporto> getRichiesteSupportoByMentore(String mentoreEmail) {
        return tabellaRichiesteSupporto.findByMentoreEmail(mentoreEmail);
    }

    public List<RichiesteSupporto> getRichiesteSupportoByTeam(String teamNome) {
        return tabellaRichiesteSupporto.findByTeamNome(teamNome);
    }

    public Optional<RichiesteSupporto> getRichiestaSupporto(Long id) {
        return tabellaRichiesteSupporto.findById(id);
    }

    // Metodi per Call
    public boolean salvaCall(unicam.calendario.Call call) {
        try {
            tabellaCall.save(call);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<unicam.calendario.Call> getCallByHackathon(String hackathonNome) {
        return tabellaCall.findByHackathonNomeOrderByDataOraAsc(hackathonNome);
    }

    public List<unicam.calendario.Call> getCallByMentore(String mentoreEmail) {
        return tabellaCall.findByMentoreEmail(mentoreEmail);
    }

    public Optional<unicam.calendario.Call> getCall(Long id) {
        return tabellaCall.findById(id);
    }

    public boolean eliminaCall(Long id) {
        try {
            tabellaCall.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Metodi per Segnalazioni
    public boolean salvaSegnalazione(Segnalazione segnalazione) {
        try {
            tabellaSegnalazioni.save(segnalazione);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Segnalazione> getSegnalazioniByHackathon(String hackathonNome) {
        return tabellaSegnalazioni.findByHackathonNome(hackathonNome);
    }

    public List<Segnalazione> getSegnalazioniNonGestite(String hackathonNome) {
        return tabellaSegnalazioni.findByHackathonNomeAndGestita(hackathonNome, false);
    }

    public Optional<Segnalazione> getSegnalazione(Long id) {
        return tabellaSegnalazioni.findById(id);
    }

    // Metodo per ottenere mentori di un hackathon
    public List<MentoriHackathon> getMentoriHackathon(String hackathonNome) {
        return tabellaMentoriHackathon.findByHackathon(hackathonNome);
    }
}
