package unicam.hackathon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unicam.SQLService;
import unicam.account.Team;
import unicam.amministrazione.Mentore;
import unicam.notifiche.Segnalazione;

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
        return !SQL.HackathonExist(nuovoHackathon.getNome()) && SQL.salvaHackathon(nuovoHackathon);
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
        if (hackathon.getMaxteam()==SQL.getAllIscrizioni(hackathon.getNome()).size()){
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

    public boolean invioSottomissione(String teamName, String hackathonName, String sottomissione) {
        List<Iscrizioni> iscrizioni = SQL.getAllIscrizioni(hackathonName);
        for (Iscrizioni i : iscrizioni) {
            if (i.getTeam().equals(teamName)) {
                i.setSottomissioni(sottomissione);
                return SQL.salvaIscrizione(i);
            }
        }
        return false;
    }

    public boolean valutaSottomissione(String teamName, String hackathonName, int voto) {
        List<Iscrizioni> iscrizioni = SQL.getAllIscrizioni(hackathonName);
        for (Iscrizioni i : iscrizioni) {
            if (i.getTeam().equals(teamName)) {
                i.setVoto(voto);
                return SQL.salvaIscrizione(i);
            }
        }
        return false;
    }

    public boolean ProclamaVincitore(Hackathon hackathon){
        Optional<Iscrizioni> vincitori = ViewIscrizioni(hackathon.getNome()).stream().max(Comparator.comparingInt(Iscrizioni::getVoto));
        hackathon.setVincitori(vincitori.orElseGet(null).getTeam());
        return SQL.salvaHackathon(hackathon);
    }

    public boolean validaSottomissione(String teamNome, String hackathonNome, boolean isValida) {
        List<Iscrizioni> iscrizioni = SQL.getAllIscrizioni(hackathonNome);
        for (Iscrizioni i : iscrizioni) {
            if (i.getTeam().equals(teamNome)) {
                i.setValidata(isValida);
                return SQL.salvaIscrizione(i);
            }
        }
        return false;
    }

    public boolean segnalaTeam(String teamNome, String hackathonNome, String motivo, String segnalante) {
        if (teamNome == null || hackathonNome == null || motivo == null || segnalante == null) {
            return false;
        }
        
        Segnalazione segnalazione = new Segnalazione(teamNome, hackathonNome, segnalante, motivo);
        return SQL.salvaSegnalazione(segnalazione);
    }

    public List<Segnalazione> visualizzaSegnalazioni(String hackathonNome) {
        return SQL.getSegnalazioniByHackathon(hackathonNome);
    }

    public List<Segnalazione> visualizzaSegnalazioniNonGestite(String hackathonNome) {
        return SQL.getSegnalazioniNonGestite(hackathonNome);
    }

    public boolean gestisciSegnalazione(Long segnalazioneId, String azione) {
        Optional<Segnalazione> optSegnalazione = SQL.getSegnalazione(segnalazioneId);
        if (optSegnalazione.isEmpty()) {
            return false;
        }

        Segnalazione segnalazione = optSegnalazione.get();
        segnalazione.setGestita(true);
        segnalazione.setAzioneIntrapresa(azione);
        return SQL.salvaSegnalazione(segnalazione);
    }
}
