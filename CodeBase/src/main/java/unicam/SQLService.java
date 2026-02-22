package unicam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unicam.SQLInterface.TabellaAmministrazione;
import unicam.SQLInterface.TabellaHackathon;
import unicam.SQLInterface.TabellaTeam;
import unicam.SQLInterface.TabellaUtenti;
import unicam.account.*;
import unicam.amministrazione.*;
import unicam.hackathon.Hackathon;

import java.util.List;
import java.util.Optional;

@Service
public class SQLService {

    private final TabellaUtenti tabellaUtenti;
    private final TabellaAmministrazione tabellaAmministrazione;
    private final TabellaHackathon tabellaHackathon;
    private final TabellaTeam tabellaTeam;

    @Autowired
    public SQLService(TabellaUtenti tabellaUtenti,
                      TabellaAmministrazione tabellaAmministrazione,
                      TabellaHackathon tabellaHackathon,
                      TabellaTeam tabellaTeam) {
        this.tabellaUtenti = tabellaUtenti;
        this.tabellaAmministrazione = tabellaAmministrazione;
        this.tabellaHackathon = tabellaHackathon;
        this.tabellaTeam = tabellaTeam;
    }

    public List<Hackathon> getAllHackathons() {
        return tabellaHackathon.findAll();
    }

    public boolean HackathonExist(String hackatonName) {
        return tabellaHackathon.existsByNome(hackatonName);
    }

    public boolean CreateHackathon(Hackathon newHackaton) {
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
            tabellanotifiche.save(Notifica);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
