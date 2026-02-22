package unicam.account;

public interface UtenteGenerico {
    //questi possono essere non presenti poiche sono inizializzati a null
    String username = null;
    String email = null;
    String password = null;

    public String getUsername();
    public String getEmail();
    public String getPassword();

    //vanno implementati in tutte le classi che implementano l'interfaccia
    public String toString();
    public int hashCode();
    public boolean equals(Object o);
}
