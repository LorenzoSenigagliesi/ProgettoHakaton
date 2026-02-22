package unicam.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import unicam.hackathon.GestioneHackathon;
import unicam.hackathon.Iscrizioni;

import java.util.List;

@Component
@Scope("prototype")
public class ValutazioneController {

    @FXML private VBox sottomissioniList;
    @FXML private Label lblTitolo;

    @Autowired
    private GestioneHackathon gestioneHackathon;

    private String nomeHackathon;

    public void setHackathon(String nomeHackathon) {
        this.nomeHackathon = nomeHackathon;
        lblTitolo.setText("Valutazione: " + nomeHackathon);
        loadSottomissioni();
    }

    private void loadSottomissioni() {
        sottomissioniList.getChildren().clear();
        List<Iscrizioni> iscrizioni = gestioneHackathon.ViewIscrizioni(nomeHackathon);

        if (iscrizioni.isEmpty()) {
            Label empty = new Label("Nessuna sottomissione presente.");
            empty.getStyleClass().add("text-secondary");
            empty.setStyle("-fx-font-size: 16px;");
            sottomissioniList.getChildren().add(empty);
            return;
        }

        for (Iscrizioni iscrizione : iscrizioni) {
            VBox card = createSottomissioneCard(iscrizione);
            sottomissioniList.getChildren().add(card);
        }
    }

    private VBox createSottomissioneCard(Iscrizioni iscrizione) {
        VBox card = new VBox(12);
        card.getStyleClass().add("detail-card");

        Label teamLabel = new Label("Team: " + iscrizione.getTeam());
        teamLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #F8FAFC;");

        Label sottomissione = new Label("Sottomissione: " +
                (iscrizione.getSottomissioni() != null && !iscrizione.getSottomissioni().isEmpty()
                        ? iscrizione.getSottomissioni() : "Nessuna sottomissione"));
        sottomissione.setStyle("-fx-text-fill: #E2E8F0; -fx-font-size: 14px; -fx-wrap-text: true;");
        sottomissione.setWrapText(true);

        Label votoAttuale = new Label("Voto attuale: " + iscrizione.getVoto() + "/10");
        votoAttuale.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 13px;");

        HBox votoRow = new HBox(12);
        votoRow.setAlignment(Pos.CENTER_LEFT);

        Label lblVoto = new Label("Nuovo Voto:");
        lblVoto.getStyleClass().add("text-secondary");

        Spinner<Integer> spinnerVoto = new Spinner<>(0, 10, iscrizione.getVoto());
        spinnerVoto.setPrefWidth(80);

        Button btnValuta = new Button("Salva Voto");
        btnValuta.getStyleClass().add("btn-primary");
        btnValuta.setOnAction(e -> {
            int voto = spinnerVoto.getValue();
            if (gestioneHackathon.valutaSottomissione(iscrizione.getTeam(), nomeHackathon, voto)) {
                votoAttuale.setText("Voto attuale: " + voto + "/10");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Voto Salvato");
                alert.setHeaderText(null);
                alert.setContentText("Voto " + voto + "/10 assegnato al team " + iscrizione.getTeam());
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText(null);
                alert.setContentText("Errore nel salvataggio del voto.");
                alert.showAndWait();
            }
        });

        votoRow.getChildren().addAll(lblVoto, spinnerVoto, btnValuta);
        card.getChildren().addAll(teamLabel, sottomissione, votoAttuale, votoRow);

        return card;
    }

    @FXML
    private void onIndietro() {
        Stage stage = (Stage) sottomissioniList.getScene().getWindow();
        stage.close();
    }
}
