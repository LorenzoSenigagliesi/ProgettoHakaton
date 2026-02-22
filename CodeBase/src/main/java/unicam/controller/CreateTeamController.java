package unicam.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import unicam.account.GestioneAccount;
import unicam.account.UtenteGenerico;
import unicam.notifiche.GestioneNotifiche;
import unicam.notifiche.Notifiche;
import unicam.notifiche.TipoNotifica;

@Component
@Scope("prototype")
public class CreateTeamController {

    @FXML private TextField txtNomeTeam;
    @FXML private TextArea txtInviti;
    @FXML private Label lblErrore;
    @FXML private Label lblSuccesso;

    @Autowired
    private GestioneAccount gestioneAccount;

    @Autowired
    private GestioneNotifiche gestioneNotifiche;

    @FXML
    private void onCreaTeam() {
        hideMessages();
        String nome = txtNomeTeam.getText().trim();

        if (nome.isEmpty()) {
            showError("Il nome del team è obbligatorio.");
            return;
        }

        if (!gestioneAccount.creazioneTeam(nome)) {
            showError("Impossibile creare il team. Nome già esistente o non sei un utente valido.");
            return;
        }

        showSuccess("Team \"" + nome + "\" creato con successo!");

        // Invia inviti se presenti
        String inviti = txtInviti.getText().trim();
        if (!inviti.isEmpty()) {
            UtenteGenerico corrente = gestioneAccount.getUtenteCorrente();
            String[] emails = inviti.split(",");
            for (String email : emails) {
                String emailTrimmed = email.trim();
                if (!emailTrimmed.isEmpty()) {
                    Notifiche invito = new Notifiche(
                            TipoNotifica.INVITO_TEAM,
                            "Sei stato invitato a unirti al team '" + nome + "'",
                            corrente.getEmail(),
                            emailTrimmed
                    );
                    gestioneNotifiche.CreaNotifica(invito);
                }
            }
            showSuccess("Team creato e inviti inviati!");
        }
    }

    @FXML
    private void onChiudi() {
        Stage stage = (Stage) txtNomeTeam.getScene().getWindow();
        stage.close();
    }

    private void showError(String msg) {
        lblSuccesso.setVisible(false);
        lblSuccesso.setManaged(false);
        lblErrore.setText(msg);
        lblErrore.setVisible(true);
        lblErrore.setManaged(true);
    }

    private void showSuccess(String msg) {
        lblErrore.setVisible(false);
        lblErrore.setManaged(false);
        lblSuccesso.setText(msg);
        lblSuccesso.setVisible(true);
        lblSuccesso.setManaged(true);
    }

    private void hideMessages() {
        lblErrore.setVisible(false);
        lblErrore.setManaged(false);
        lblSuccesso.setVisible(false);
        lblSuccesso.setManaged(false);
    }
}
