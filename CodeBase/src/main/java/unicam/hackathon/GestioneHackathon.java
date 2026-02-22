package unicam.hackathon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unicam.SQLService;
import unicam.amministrazione.Mentore;

import java.util.List;

@Service
public class GestioneHackathon {
    private final SQLService SQL;

    @Autowired
    public GestioneHackathon(SQLService sqlService) {
        this.SQL = sqlService;
    }

    public boolean createHackthon(Hackathon nuovoHackathon){
        return SQL.hackathonExist(nuovoHackathon.getNome()) && SQL.salvaHackathon(nuovoHackathon);
    }

    public List<Hackathon> viewHackathon(){
        return SQL.getAllHackathons();
    }

    public boolean addMentori(List<Mentore> mentori, String nomeHackathon){
        MentoriHackathon mentoriHackathon;
        for(Mentore m : mentori){
            mentoriHackathon = new MentoriHackathon(m.getEmail(), nomeHackathon);
            SQL.salvaMentori(mentoriHackathon);
        }
        return true;
    }

    public boolean cambiaStato(Hackathon hackathon){
        hackathon.cambiaStato();
        return SQL.salvaHackathon(hackathon);
    }

    public Hackathon dettagliHackathon(String nomeHackathon){
        return SQL.dettagliHackathon(nomeHackathon);
    }
}
