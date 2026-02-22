package unicam.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unicam.SQLService;
import unicam.amministrazione.*;


import java.util.Objects;
import java.util.Random;

@Service
public class GestioneAccount {
    private final Random random = new Random();
    private UtenteGenerico utenteCorrente = new Visitatore("user" + String.format("%05d", random.nextInt(90000)));
    private final SQLService SQL;

    @Autowired
    public GestioneAccount(SQLService sqlService) {
        this.SQL = sqlService;
    }

    public boolean login(String email, String password, String tipoLogin){
        if(!(utenteCorrente instanceof Visitatore)){
            return false;
        }
        UtenteGenerico utenzaTrovata;
        if(Objects.equals(tipoLogin, "Utente")){
            utenzaTrovata = SQL.loginUtenti(email, password);
        } else {
            utenzaTrovata = SQL.loginAmministrazione(email, password);
        }
        if(utenzaTrovata == null){
            return false;
        }
        utenteCorrente = utenzaTrovata;
        return true;
    }

    public boolean logout(){
        if(utenteCorrente instanceof Visitatore){
            return false;
        }
        utenteCorrente = new Visitatore("user" + String.format("%05d", random.nextInt(90000)));
        return true;
    }

    public boolean registrazione(UtenteRegistrato utente){
        if(!(utenteCorrente instanceof Visitatore) && !SQL.salvaUtente(utente)){
            return false;
        }
        utenteCorrente = utente;
        return true;
    }

    public boolean checkUtente(String email){
        return SQL.checkAccount(email);
    }

    public Team readTeam(String nome){
        return SQL.readTeam(nome);
    }

    public boolean creazioneTeam(String nome){
        if(!(utenteCorrente instanceof UtenteRegistrato) || !(((UtenteRegistrato) utenteCorrente).getTipo().equals(TipoUtente.Utente))){
            return false;
        }
        if(readTeam(nome)!=null){
            return false;
        }
        Team team = new Team(nome);
        if (!SQL.creaTeam(team, (UtenteRegistrato)utenteCorrente)){
            return false;
        }
        //modifica utente
        ((UtenteRegistrato)utenteCorrente).setTeam(nome);
         return SQL.salvaUtente((UtenteRegistrato)utenteCorrente);
    }

    public boolean accettazioneTeam(String nomeTeam) {
        if (!(utenteCorrente instanceof UtenteRegistrato) || !(((UtenteRegistrato) utenteCorrente).getTipo().equals(TipoUtente.Utente))) {
            return false;
        }

        ((UtenteRegistrato)utenteCorrente).setTeam(nomeTeam);
        return SQL.salvaUtente((UtenteRegistrato)utenteCorrente);
    }

    //metodi per amministratori Team

    public boolean cambiaRuolo(StaffDecorator ruolo){
        if (!(utenteCorrente instanceof UtenzaAmministrazione)){
            return false;
        }
        switch (ruolo){
            case Organizzatore organizzatore:
                utenteCorrente = new Organizzatore((UtenzaAmministrazione)utenteCorrente);
                break;
            case Mentore mentore:
                utenteCorrente = new Mentore((UtenzaAmministrazione)utenteCorrente);
                break;
            case Giudice giudice:
                utenteCorrente = new Giudice((UtenzaAmministrazione)utenteCorrente);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + ruolo);
        }
        return true;
    }
}
