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
import unicam.account.GestioneAccount;
import unicam.amministrazione.Mentore;
import unicam.calendario.Call;
import unicam.calendario.GestioneCall;
import unicam.hackathon.MentoriHackathon;
import unicam.SQLService;
import unicam.hackathon.Iscrizioni;
import unicam.notifiche.GestioneNotifiche;
import unicam.notifiche.Notifiche;
import unicam.notifiche.TipoNotifica;
import unicam.supporto.GestioneSupporto;
import unicam.supporto.StatoRichiesta;
import unicam.supporto.RichiesteSupporto;
import unicam.account.UtenteRegistrato;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
@Scope("prototype")
public class MentoreController {

    @FXML private VBox richiesteList;
    @FXML private VBox callList;
    @FXML private Label lblTitolo;
    @FXML private Label lblFeedback;

    // Form proponi call
    @FXML private TextField txtCallTitolo;
    @FXML private TextArea txtCallDescrizione;
    @FXML private DatePicker dpCallData;
    @FXML private TextField txtCallLuogo;
    @FXML private ComboBox<String> cmbHackathon;
    @FXML private ComboBox<String> cmbTeam;

    @Autowired
    private GestioneSupporto gestioneSupporto;

    @Autowired
    private GestioneCall gestioneCall;

    @Autowired
    private GestioneAccount gestioneAccount;

    @Autowired
    private SQLService sqlService;

    @Autowired
    private GestioneNotifiche gestioneNotifiche;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        if (!(gestioneAccount.getUtenteCorrente() instanceof Mentore)) {
            lblTitolo.setText("Accesso non autorizzato");
            return;
        }

        Mentore mentore = (Mentore) gestioneAccount.getUtenteCorrente();
        lblTitolo.setText("Dashboard Mentore: " + mentore.getUsername());

        // Cerca tutti gli hackathon dove questo mentore è assegnato
        List<String> hackathonNames = sqlService.getAllHackathons().stream()
                .filter(h -> {
                    List<MentoriHackathon> mentori = sqlService.getMentoriHackathon(h.getNome());
                    return mentori.stream().anyMatch(m -> m.getEmail().equals(mentore.getEmail()));
                })
                .map(h -> h.getNome())
                .toList();

        cmbHackathon.getItems().addAll(hackathonNames);
        if (!hackathonNames.isEmpty()) {
            cmbHackathon.getSelectionModel().selectFirst();
        }

        // Aggiorna la lista team quando si cambia hackathon
        cmbHackathon.setOnAction(e -> loadTeamPerHackathon(cmbHackathon.getValue()));
        // Carica i team per l'hackathon iniziale
        if (cmbHackathon.getValue() != null) {
            loadTeamPerHackathon(cmbHackathon.getValue());
        }

        loadRichieste(mentore);
        loadCall(mentore);
    }

    private void loadRichieste(Mentore mentore) {
        richiesteList.getChildren().clear();
        List<RichiesteSupporto> richieste = gestioneSupporto.visualizzaRichiesteMentore(mentore.getEmail());

        if (richieste.isEmpty()) {
            Label empty = new Label("Nessuna richiesta di supporto ricevuta.");
            empty.getStyleClass().add("text-secondary");
            empty.setStyle("-fx-font-size: 14px;");
            richiesteList.getChildren().add(empty);
            return;
        }

        for (RichiesteSupporto r : richieste) {
            VBox card = createRichiestaCard(r);
            richiesteList.getChildren().add(card);
        }
    }

    private VBox createRichiestaCard(RichiesteSupporto richiesta) {
        VBox card = new VBox(8);
        card.getStyleClass().add("detail-card");
        card.setStyle("-fx-padding: 16;");

        Label teamLabel = new Label("Team: " + richiesta.getTeamNome()
                + " | Hackathon: " + richiesta.getHackathonNome());
        teamLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #F8FAFC;");

        Label msgLabel = new Label(richiesta.getMessaggio());
        msgLabel.setStyle("-fx-text-fill: #E2E8F0; -fx-font-size: 13px;");
        msgLabel.setWrapText(true);

        Label statoLabel = new Label("Stato: " + richiesta.getStato().name());
        statoLabel.getStyleClass().add("badge-stato");

        card.getChildren().addAll(teamLabel, msgLabel, statoLabel);

        // Se aperta, mostra opzione di risposta
        if (richiesta.getStato() == StatoRichiesta.APERTA) {
            HBox rispostaRow = new HBox(10);
            rispostaRow.setAlignment(Pos.CENTER_LEFT);

            TextField txtRisposta = new TextField();
            txtRisposta.setPromptText("Scrivi una risposta...");
            txtRisposta.getStyleClass().add("text-field-dark");
            txtRisposta.setPrefWidth(400);

            Button btnRispondi = new Button("Rispondi");
            btnRispondi.getStyleClass().add("btn-primary");
            btnRispondi.setStyle("-fx-padding: 6 16; -fx-font-size: 12px;");
            btnRispondi.setOnAction(e -> {
                String risposta = txtRisposta.getText().trim();
                if (!risposta.isEmpty()) {
                    Mentore mentore = (Mentore) gestioneAccount.getUtenteCorrente();
                    if (gestioneSupporto.rispondiRichiesta(richiesta.getId(), risposta, mentore.getEmail())) {
                        showFeedback("Risposta inviata!", false);
                        loadRichieste(mentore);
                    } else {
                        showFeedback("Errore nell'invio della risposta.", true);
                    }
                }
            });

            Button btnChiudi = new Button("Chiudi Richiesta");
            btnChiudi.getStyleClass().add("btn-secondary");
            btnChiudi.setStyle("-fx-padding: 6 16; -fx-font-size: 12px;");
            btnChiudi.setOnAction(e -> {
                if (gestioneSupporto.chiudiRichiesta(richiesta.getId())) {
                    showFeedback("Richiesta chiusa.", false);
                    Mentore mentore = (Mentore) gestioneAccount.getUtenteCorrente();
                    loadRichieste(mentore);
                }
            });

            rispostaRow.getChildren().addAll(txtRisposta, btnRispondi, btnChiudi);
            card.getChildren().add(rispostaRow);
        }

        // Se ha risposta, mostrala
        if (richiesta.getRisposta() != null && !richiesta.getRisposta().isEmpty()) {
            Label rispostaLabel = new Label("Risposta: " + richiesta.getRisposta());
            rispostaLabel.setStyle("-fx-text-fill: #00F5A0; -fx-font-size: 13px;");
            rispostaLabel.setWrapText(true);
            card.getChildren().add(rispostaLabel);
        }

        return card;
    }

    private void loadCall(Mentore mentore) {
        callList.getChildren().clear();
        List<Call> calls = gestioneCall.visualizzaCallMentore(mentore.getEmail());

        if (calls.isEmpty()) {
            Label empty = new Label("Nessuna call programmata.");
            empty.getStyleClass().add("text-secondary");
            empty.setStyle("-fx-font-size: 14px;");
            callList.getChildren().add(empty);
            return;
        }

        for (Call c : calls) {
            HBox row = new HBox(16);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("detail-card");
            row.setStyle("-fx-padding: 12;");

            String teamInfo = c.getTeamNome() != null ? " | Team: " + c.getTeamNome() : "";
            Label info = new Label(c.getTitolo() + " | " + sdf.format(c.getDataOra())
                    + " | " + c.getHackathonNome() + teamInfo);
            info.setStyle("-fx-text-fill: #F8FAFC; -fx-font-size: 13px;");

            if (c.getLuogo() != null && !c.getLuogo().isEmpty()) {
                Label luogo = new Label("Luogo: " + c.getLuogo());
                luogo.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 12px;");
                row.getChildren().addAll(info, luogo);
            } else {
                row.getChildren().add(info);
            }

            Button btnElimina = new Button("Elimina");
            btnElimina.getStyleClass().add("btn-danger");
            btnElimina.setStyle("-fx-padding: 4 12; -fx-font-size: 11px;");
            btnElimina.setOnAction(e -> {
                if (gestioneCall.eliminaCall(c.getId(), mentore.getEmail())) {
                    showFeedback("Call eliminata.", false);
                    loadCall(mentore);
                }
            });
            row.getChildren().add(btnElimina);

            callList.getChildren().add(row);
        }
    }

    @FXML
    private void onProponiCall() {
        if (!(gestioneAccount.getUtenteCorrente() instanceof Mentore mentore)) return;

        String hackathonNome = cmbHackathon.getValue();
        String titolo = txtCallTitolo.getText().trim();
        String descrizione = txtCallDescrizione.getText().trim();
        String luogo = txtCallLuogo.getText().trim();
        String teamNome = cmbTeam.getValue();

        if (hackathonNome == null || hackathonNome.isEmpty()) {
            showFeedback("Seleziona un hackathon.", true);
            return;
        }
        if (teamNome == null || teamNome.isEmpty()) {
            showFeedback("Seleziona un team.", true);
            return;
        }
        if (titolo.isEmpty()) {
            showFeedback("Il titolo della call è obbligatorio.", true);
            return;
        }
        if (dpCallData.getValue() == null) {
            showFeedback("Seleziona una data per la call.", true);
            return;
        }

        Date dataOra = Date.from(dpCallData.getValue()
                .atStartOfDay(ZoneId.systemDefault()).toInstant());

        if (gestioneCall.proponiCall(mentore.getEmail(), hackathonNome,
                titolo, descrizione, dataOra, luogo, teamNome)) {
            showFeedback("Call proposta con successo!", false);
            txtCallTitolo.clear();
            txtCallDescrizione.clear();
            txtCallLuogo.clear();
            dpCallData.setValue(null);
            loadCall(mentore);

            // Notifica i membri del team
            notificaMembriTeam(teamNome,
                    "Il mentore ti ha proposto una call: " + titolo,
                    mentore.getEmail(), TipoNotifica.PROPOSTA_CALL);
        } else {
            showFeedback("Errore nella creazione della call.", true);
        }
    }

    @FXML
    private void onIndietro() {
        Stage stage = (Stage) lblTitolo.getScene().getWindow();
        stage.close();
    }

    private void loadTeamPerHackathon(String hackathonNome) {
        cmbTeam.getItems().clear();
        if (hackathonNome == null || hackathonNome.isEmpty()) return;

        List<Iscrizioni> iscrizioni = sqlService.getAllIscrizioni(hackathonNome);
        List<String> teamNames = iscrizioni.stream()
                .map(Iscrizioni::getTeam)
                .toList();
        cmbTeam.getItems().addAll(teamNames);
        if (!teamNames.isEmpty()) {
            cmbTeam.getSelectionModel().selectFirst();
        }
    }

    private void notificaMembriTeam(String teamNome, String messaggio, String mittente, TipoNotifica tipo) {
        List<UtenteRegistrato> membri = sqlService.getMembriTeam(teamNome);
        for (UtenteRegistrato membro : membri) {
            Notifiche notifica = new Notifiche(tipo, messaggio, mittente, membro.getEmail());
            gestioneNotifiche.CreaNotifica(notifica);
        }
    }

    private void showFeedback(String msg, boolean isError) {
        lblFeedback.setText(msg);
        lblFeedback.setStyle("-fx-font-size: 14px; -fx-text-fill: " + (isError ? "#EF4444;" : "#00F5A0;"));
        lblFeedback.setVisible(true);
        lblFeedback.setManaged(true);
    }
}
