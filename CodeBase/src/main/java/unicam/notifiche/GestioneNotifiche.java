package unicam.notifiche;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unicam.SQLService;

@Service
public class GestioneNotifiche {
    private final SQLService SQL;

    @Autowired
    public GestioneNotifiche(SQLService sqlService) {
        this.SQL = sqlService;
    }

    public boolean CreaNotifica( Notifiche NewNotifica) {
        if(NewNotifica == null){
            return false;
        }
        return SQL.salvaNotifica(NewNotifica);
    }

    //chiudi
    //ricerca notifica

    public Notifiche readNotifiche(int Id){
        return SQL.findNotifiche(Id);
    }

}
