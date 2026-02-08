package unicam.hackathon;
import unicam.SQLService;

public class GestioneHackathon {
    SQLService SQL = new SQLService();

    public boolean CreateHackthon(Hackathon NuovoHackathon){
        return SQL.HackathonExist(NuovoHackathon.getNome()) && SQL.CreateHackathon(NuovoHackathon);
    }
}
