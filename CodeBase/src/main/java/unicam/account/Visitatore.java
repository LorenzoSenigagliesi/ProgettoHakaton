package unicam.account;

import java.util.Objects;

public class Visitatore implements UtenteGenerico {
    private final String userName;

    public Visitatore(String userName) {
        if (userName.isBlank()) {
            throw new IllegalArgumentException("Username non può essere nullo o vuoto");
        }
        if (userName.length() < 3 || userName.length() > 50) {
            throw new IllegalArgumentException("Username deve essere tra 3 e 50 caratteri");
        }
        this.userName=userName;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getPassword() {
        return "";
    }

    // Metodi Object
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visitatore utente = (Visitatore) o;
        return Objects.equals(userName, utente.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }


    @Override
    public String toString() {
        return "Utente{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
