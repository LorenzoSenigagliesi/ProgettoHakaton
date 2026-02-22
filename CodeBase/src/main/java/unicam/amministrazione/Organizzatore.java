package unicam.amministrazione;

import unicam.account.UtenteGenerico;

public class Organizzatore extends StaffDecorator {
    public Organizzatore(UtenzaAmministrazione utenteCorrente) {
        super(utenteCorrente);
    }

    @Override
    public String getRuolo() {
        return "Organizzatore " + super.getRuolo();
    }
}
