package unicam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unicam.SQLInterface.*;
import unicam.account.*;
import unicam.amministrazione.*;
import unicam.hackathon.Hackathon;
import unicam.hackathon.MentoriHackathon;

import java.util.List;
import java.util.Optional;

@Service
public class SQLService {

    private final TabellaUtenti tabellaUtenti;
    private final TabellaAmministrazione tabellaAmministrazione;
    private final TabellaHackathon tabellaHackathon;
    private final TabellaTeam tabellaTeam;
    private final TabellaMentoriHackathon tabellaMentoriHackathon;

    @Autowired
    public SQLService(TabellaUtenti tabellaUtenti,
                      TabellaAmministrazione tabellaAmministrazione,
                      TabellaHackathon tabellaHackathon,
                      TabellaTeam tabellaTeam,
                      TabellaMentoriHackathon tabellaMentoriHackathon) {
        this.tabellaUtenti = tabellaUtenti;
        this.tabellaAmministrazione = tabellaAmministrazione;
        this.tabellaHackathon = tabellaHackathon;
        this.tabellaTeam = tabellaTeam;
        this.tabellaMentoriHackathon = tabellaMentoriHackathon;
    }

    public List<Hackathon> getAllHackathons() {
        return tabellaHackathon.findAll();
    }

    public boolean hackathonExist(String hackatonName) {
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
}
