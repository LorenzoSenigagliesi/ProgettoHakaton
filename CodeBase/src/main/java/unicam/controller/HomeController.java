package unicam.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import unicam.CodeBaseApplication;
import unicam.SQLService;
import unicam.account.UtenteRegistrato;
import unicam.hackathon.Hackathon;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
@Scope("prototype")
public class HomeController {

    @FXML
    private VBox hackathonList;

    @FXML
    private Button btnLogin;

    @Autowired
    private SQLService sqlService;

    @FXML
    public void initialize() {
        loadHackathons();
    }

    private void loadHackathons() {
        List<Hackathon> hackathons = sqlService.getAllHackathons();
        hackathonList.getChildren().clear();

        if (hackathons.isEmpty()) {
            Label empty = new Label("Nessun hackathon disponibile al momento.");
            empty.getStyleClass().add("text-secondary");
            empty.setStyle("-fx-font-size: 16px;");
            hackathonList.getChildren().add(empty);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Hackathon h : hackathons) {
            VBox card = createCard(h, sdf);
            hackathonList.getChildren().add(card);
        }
    }

    private VBox createCard(Hackathon h, SimpleDateFormat sdf) {
        VBox card = new VBox(8);
        card.getStyleClass().add("hackathon-card");

        Label nome = new Label(h.getNome());
        nome.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #F8FAFC;");

        HBox infoRow = new HBox(20);
        infoRow.setAlignment(Pos.CENTER_LEFT);

        Label luogo = new Label("Luogo: " + (h.getLuogo() != null ? h.getLuogo() : "N/D"));
        luogo.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 13px;");

        Label data = new Label("Data: " + (h.getData() != null ? sdf.format(h.getData()) : "N/D"));
        data.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 13px;");

        Label stato = new Label(h.getStato() != null ? h.getStato() : "N/D");
        stato.getStyleClass().add("badge-stato");

        infoRow.getChildren().addAll(luogo, data, stato);
        card.getChildren().addAll(nome, infoRow);

        card.setOnMouseClicked(event -> openDetail(h));

        return card;
    }

    @FXML
    private void onLogin() {
        // TODO: implementare finestra di login
        sqlService.salvaUtente(new UtenteRegistrato("Pippo","PippoDroga@gmail.com","Droga"));
    }

    private void openDetail(Hackathon hackathon) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HackathonDetail.fxml"));
            loader.setControllerFactory(CodeBaseApplication.getSpringContext()::getBean);
            Parent root = loader.load();

            HackathonDetailController controller = loader.getController();
            controller.setHackathon(hackathon);

            Stage detailStage = new Stage();
            detailStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/dark-theme.css").toExternalForm());

            detailStage.setTitle(hackathon.getNome());
            detailStage.setScene(scene);
            detailStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
