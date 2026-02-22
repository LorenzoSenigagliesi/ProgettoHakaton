package unicam.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import unicam.account.GestioneAccount;
import unicam.account.UtenteRegistrato;

@Component
@Scope("prototype")
public class RegistrationController {

    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfermaPassword;
    @FXML private Label lblErrore;

    @Autowired
    private GestioneAccount gestioneAccount;

    @FXML
    private void onRegistra() {
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        String conferma = txtConfermaPassword.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Tutti i campi sono obbligatori.");
            return;
        }

        if (username.length() < 3 || username.length() > 50) {
            showError("Username deve essere tra 3 e 50 caratteri.");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showError("Formato email non valido.");
            return;
        }

        if (!password.equals(conferma)) {
            showError("Le password non coincidono.");
            return;
        }

        if (gestioneAccount.checkUtente(email)) {
            showError("Email già registrata.");
            return;
        }

        try {
            UtenteRegistrato utente = new UtenteRegistrato(username, email, password);
            if (gestioneAccount.registrazione(utente)) {
                Stage stage = (Stage) txtEmail.getScene().getWindow();
                stage.close();
            } else {
                showError("Errore durante la registrazione.");
            }
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onAnnulla() {
        Stage stage = (Stage) txtEmail.getScene().getWindow();
        stage.close();
    }

    private void showError(String msg) {
        lblErrore.setText(msg);
        lblErrore.setVisible(true);
        lblErrore.setManaged(true);
    }
}
