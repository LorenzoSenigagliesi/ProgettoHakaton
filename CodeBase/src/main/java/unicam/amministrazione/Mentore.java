package unicam.amministrazione;

import unicam.account.UtenteGenerico;

public class Mentore extends StaffDecorator {
    public Mentore(UtenzaAmministrazione utenteCorrente) {
        super(utenteCorrente);
    }

    @Override
    public String getRuolo() {
        return "Mentore " + super.getRuolo();
    }
}
