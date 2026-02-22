package unicam.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import unicam.SQLService;
import unicam.account.GestioneAccount;
import unicam.hackathon.GestioneHackathon;
import unicam.hackathon.Hackathon;
import unicam.hackathon.MentoriHackathon;
import unicam.hackathon.StatoHackathon;

import java.time.ZoneId;
import java.util.Date;

@Component
@Scope("prototype")
public class CreateHackathonController {

    @FXML private TextField txtNome;
    @FXML private TextField txtLuogo;
    @FXML private TextArea txtRegolamento;
    @FXML private DatePicker dpDataInizio;
    @FXML private DatePicker dpDataFine;
    @FXML private DatePicker dpScadenzaIscrizioni;
    @FXML private Spinner<Integer> spnDimTeam;
    @FXML private Spinner<Integer> spnMaxTeam;
    @FXML private TextField txtGiudice;
    @FXML private TextField txtMentori;
    @FXML private Label lblErrore;

    @Autowired
    private GestioneHackathon gestioneHackathon;

    @Autowired
    private GestioneAccount gestioneAccount;

    @Autowired
    private SQLService sqlService;

    @FXML
    public void initialize() {
        spnDimTeam.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 4));
        spnMaxTeam.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200, 10));
    }

    @FXML
    private void onCrea() {
        String nome = txtNome.getText().trim();
        String luogo = txtLuogo.getText().trim();
        String regolamento = txtRegolamento.getText().trim();

        if (nome.isEmpty() || luogo.isEmpty()) {
            showError("Nome e luogo sono obbligatori.");
            return;
        }

        if (dpDataInizio.getValue() == null || dpDataFine.getValue() == null
                || dpScadenzaIscrizioni.getValue() == null) {
            showError("Tutte le date sono obbligatorie.");
            return;
        }

        if (txtGiudice.getText().trim().isEmpty()) {
            showError("L'email del giudice è obbligatoria.");
            return;
        }

        Hackathon hackathon = new Hackathon();
        hackathon.setNome(nome);
        hackathon.setLuogo(luogo);
        hackathon.setRegolamento(regolamento);
        hackathon.setData(Date.from(dpDataInizio.getValue()
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        hackathon.setDataFine(Date.from(dpDataFine.getValue()
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        hackathon.setDataFineIscrizioni(Date.from(dpScadenzaIscrizioni.getValue()
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        hackathon.setDimTeam(spnDimTeam.getValue());
        hackathon.setMaxteam(spnMaxTeam.getValue());
        hackathon.setGiudice(txtGiudice.getText().trim());
        hackathon.setOrganizzatore(gestioneAccount.getUtenteCorrente().getEmail());
        hackathon.setStato(StatoHackathon.InIscrizione);

        if (!gestioneHackathon.createHackthon(hackathon)) {
            showError("Errore nella creazione. Il nome potrebbe essere già in uso.");
            return;
        }

        // Salva mentori
        String mentoriText = txtMentori.getText().trim();
        if (!mentoriText.isEmpty()) {
            String[] emails = mentoriText.split(",");
            for (String email : emails) {
                String trimmed = email.trim();
                if (!trimmed.isEmpty()) {
                    MentoriHackathon mh = new MentoriHackathon(trimmed, nome);
                    sqlService.salvaMentori(mh);
                }
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successo");
        alert.setHeaderText(null);
        alert.setContentText("Hackathon \"" + nome + "\" creato con successo!");
        alert.showAndWait();

        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onIndietro() {
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }

    private void showError(String msg) {
        lblErrore.setText(msg);
        lblErrore.setVisible(true);
        lblErrore.setManaged(true);
    }
}
