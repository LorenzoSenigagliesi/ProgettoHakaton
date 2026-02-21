package unicam.notifiche;

import org.springframework.beans.factory.annotation.Autowired;
import unicam.SQL;

public class GestioneNotifiche {
    private final SQL SQL;

    @Autowired
    public GestioneNotifiche(SQL sqlService) {
        this.SQL = sqlService;
    }

    public boolean CreaNotifica( Notifiche NewNotifica)
    {
        if(NewNotifica == null){
            return false;
        }
        return SQL.salvaNotifica(NewNotifica);
    }

}
