package unicam.hackathon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unicam.SQL;

@Service
public class GestioneHackathon {
    private final SQL SQL;

    @Autowired
    public GestioneHackathon(SQL sqlService) {
        this.SQL = sqlService;
    }

    public boolean CreateHackthon(Hackathon NuovoHackathon){
        return SQL.HackathonExist(NuovoHackathon.getNome()) && SQL.CreateHackathon(NuovoHackathon);
    }
}
