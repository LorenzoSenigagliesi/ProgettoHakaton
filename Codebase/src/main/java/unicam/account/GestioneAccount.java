package unicam.account;

import unicam.SQLService;

import java.util.Objects;
import java.util.Random;

public class GestioneAccount {
    private final Random random = new Random();
    private UtenteGenerico utenteCorrente = new Visitatore("user" + String.format("%05d", random.nextInt(90000)));
    private static final SQLService SQL = new SQLService();

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
        if(!(utenteCorrente instanceof Visitatore) && !SQL.registrazione(utente)){
            return false;
        }
        utenteCorrente = utente;
        return true;
    }

    public boolean checkUtente(String email){
        return SQL.checkAccount(email);
    }

    public Team readTeam(String nome){
        if(!(utenteCorrente instanceof UtenteRegistrato) || !(((UtenteRegistrato) utenteCorrente).getTipo().equals(TipoUtente.MembroTeam))){
            return null;
        }
        return SQL.readTeam(nome);
    }
}
