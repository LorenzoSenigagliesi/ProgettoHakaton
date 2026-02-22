package unicam.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import unicam.account.GestioneAccount;

@Component
@Scope("prototype")
public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private Label lblErrore;

    @Autowired
    private GestioneAccount gestioneAccount;

    @FXML
    public void initialize() {
        cmbTipo.setItems(FXCollections.observableArrayList("Utente", "Staff"));
        cmbTipo.getSelectionModel().selectFirst();
    }

    @FXML
    private void onLogin() {
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();
        String tipo = cmbTipo.getValue();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Inserisci email e password.");
            return;
        }

        if (gestioneAccount.login(email, password, tipo)) {
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            stage.close();
        } else {
            showError("Credenziali non valide o tipo account errato.");
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
