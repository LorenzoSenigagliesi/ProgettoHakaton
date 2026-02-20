package unicam.amministrazione;

import unicam.account.UtenteGenerico;

public class Mentore implements UtenteGenerico {
    private UtenzaAmministrazione base;
    public Mentore(UtenzaAmministrazione utenteCorrente) {
        this.base = utenteCorrente;
    }
}
