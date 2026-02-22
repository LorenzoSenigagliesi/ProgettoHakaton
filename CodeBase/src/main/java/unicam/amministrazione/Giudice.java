package unicam.amministrazione;

import unicam.account.UtenteGenerico;

public class Giudice extends StaffDecorator {
    public Giudice(UtenzaAmministrazione utenteCorrente) {
        super(utenteCorrente);
    }

    @Override
    public String getRuolo() {
        return "Giudice " + super.getRuolo();
    }
}
