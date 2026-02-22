package unicam.hackathon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unicam.SQLService;
import unicam.account.Team;
import unicam.amministrazione.Giudice;
import unicam.amministrazione.Mentore;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GestioneHackathon {
    private final SQLService SQL;

    @Autowired
    public GestioneHackathon(SQLService sqlService) {
        this.SQL = sqlService;
    }

    public boolean createHackthon(Hackathon nuovoHackathon){
        return SQL.HackathonExist(nuovoHackathon.getNome()) && SQL.salvaHackathon(nuovoHackathon);
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

    public List<Iscrizioni> ViewIscrizioni(String nomeHackathon)
    {
        return SQL.getAllIscrizioni(nomeHackathon);
    }

    public boolean IscriviTeam(Team team, Hackathon hackathon){
        if(team.getDim()>hackathon.getDimTeam()){
            return false;
        }
        if (hackathon.getMaxteam()>=SQL.getAllIscrizioni(hackathon.getNome()).size()){
            return false;
        }
        Iscrizioni iscrizione = new Iscrizioni(team.getNome(),hackathon.getNome(),"",0);
        return SQL.salvaIscrizione(iscrizione);
    }

    public List<Hackathon> HackathonDaGiudicare(String emailGiudice){
        return SQL.getAllHackathons().stream().
                                      filter(hackathon -> hackathon.getStato().equals(StatoHackathon.InValutazione)).
                                      filter(hackathon -> hackathon.getGiudice().equals(emailGiudice)).
                                      collect(Collectors.toList());
    }

    public boolean ProclamaVincitore(Hackathon hackathon){
        Optional<Iscrizioni> vincitori = ViewIscrizioni(hackathon.getNome()).stream().max(Comparator.comparingInt(Iscrizioni::getVoto));
        hackathon.setVincitori(vincitori.orElseGet(null).getTeam());
        SQL.salvaHackathon(hackathon);
        return true;
    }


}
