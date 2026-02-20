package unicam.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import unicam.hackathon.Hackathon;

import java.text.SimpleDateFormat;

@Component
@Scope("prototype")
public class HackathonDetailController {

    @FXML private Label lblNome;
    @FXML private Label lblData;
    @FXML private Label lblDataFine;
    @FXML private Label lblDataFineIscrizioni;
    @FXML private Label lblLuogo;
    @FXML private Label lblDimTeam;
    @FXML private Label lblStato;
    @FXML private TextArea txtRegolamento;
    @FXML private Button btnIndietro;
    @FXML private Button btnIscriviti;
    @FXML private Button btnRegolamento;

    private Hackathon hackathon;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public void setHackathon(Hackathon hackathon) {
        this.hackathon = hackathon;
        populateFields();
    }

    private void populateFields() {
        if (hackathon == null) return;

        lblNome.setText(hackathon.getNome());
        lblData.setText(hackathon.getData() != null ? sdf.format(hackathon.getData()) : "N/D");
        lblDataFine.setText(hackathon.getDataFine() != null ? sdf.format(hackathon.getDataFine()) : "N/D");
        lblDataFineIscrizioni.setText(hackathon.getDataFineIscrizioni() != null
                ? sdf.format(hackathon.getDataFineIscrizioni()) : "N/D");
        lblLuogo.setText(hackathon.getLuogo() != null ? hackathon.getLuogo() : "N/D");
        lblDimTeam.setText(String.valueOf(hackathon.getDimTeam()));
        lblStato.setText(hackathon.getStato() != null ? hackathon.getStato() : "N/D");
        txtRegolamento.setText(hackathon.getRegolamento() != null
                ? hackathon.getRegolamento() : "Nessun regolamento disponibile.");
    }

    @FXML
    private void onIndietro() {
        Stage stage = (Stage) btnIndietro.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onIscriviti() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Iscrizione");
        alert.setHeaderText(null);
        alert.setContentText("Funzionalità di iscrizione in fase di sviluppo.");
        alert.showAndWait();
    }

    @FXML
    private void onRegolamento() {
        if (hackathon != null && hackathon.getRegolamento() != null) {
            txtRegolamento.requestFocus();
        }
    }
}
