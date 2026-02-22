package unicam.amministrazione;

public class StaffDecorator implements UtenzaAmministrazione{
    private final UtenzaAmministrazione base;

    public StaffDecorator(UtenzaAmministrazione utenteCorrente) {
        this.base = utenteCorrente;
    }

    @Override
    public String getUsername() {
        return base.getUsername();
    }

    @Override
    public String getEmail() {
        return base.getEmail();
    }

    @Override
    public String getPassword() {
        return base.getPassword();
    }

    @Override
    public String getRuolo() {
        return base.getRuolo();
    }
}
