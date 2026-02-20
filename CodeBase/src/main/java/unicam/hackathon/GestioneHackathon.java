package unicam.hackathon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unicam.SQLService;

@Service
public class GestioneHackathon {
    private final SQLService SQL;

    @Autowired
    public GestioneHackathon(SQLService sqlService) {
        this.SQL = sqlService;
    }

    public boolean CreateHackthon(Hackathon NuovoHackathon){
        return SQL.HackathonExist(NuovoHackathon.getNome()) && SQL.CreateHackathon(NuovoHackathon);
    }
}
