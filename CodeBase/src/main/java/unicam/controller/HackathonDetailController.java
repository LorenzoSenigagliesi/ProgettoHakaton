package unicam.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import unicam.CodeBaseApplication;
import unicam.SQLService;
import unicam.account.GestioneAccount;
import unicam.account.UtenteGenerico;
import unicam.account.UtenteRegistrato;
import unicam.account.TipoUtente;
import unicam.amministrazione.Giudice;
import unicam.amministrazione.Mentore;
import unicam.amministrazione.Organizzatore;
import unicam.amministrazione.UtenzaAmministrazione;
import unicam.calendario.GestioneCall;
import unicam.hackathon.GestioneHackathon;
import unicam.hackathon.Hackathon;
import unicam.hackathon.Iscrizioni;
import unicam.hackathon.MentoriHackathon;
import unicam.hackathon.StatoHackathon;
import unicam.notifiche.GestioneNotifiche;
import unicam.notifiche.Notifiche;
import unicam.notifiche.Segnalazione;
import unicam.notifiche.TipoNotifica;
import unicam.supporto.GestioneSupporto;
import unicam.supporto.RichiesteSupporto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    @FXML private Label lblVincitore;
    @FXML private Label lblFeedback;
    @FXML private HBox hboxVincitore;
    @FXML private HBox topActions;
    @FXML private TextArea txtRegolamento;
    @FXML private TextArea txtSottomissione;
    @FXML private Button btnIndietro;
    @FXML private Button btnInviaSottomissione;
    @FXML private Button btnCambiaStato;
    @FXML private Button btnProclamaVincitore;
    @FXML private VBox sezioneSottomissione;
    @FXML private VBox sezioneOrganizzatore;

    // Sezione Richiesta Supporto (MembroTeam)
    @FXML private VBox sezioneRichiestaSupp;
    @FXML private TextArea txtRichiestaSupp;

    // Sezione Mentore
    @FXML private VBox sezioneMentore;
    @FXML private VBox listaRichiesteSupp;

    // Sezione Staff
    @FXML private VBox sezioneStaff;
    @FXML private VBox listaTeamIscritti;

    @Autowired
    private GestioneAccount gestioneAccount;

    @Autowired
    private GestioneHackathon gestioneHackathon;

    @Autowired
    private GestioneSupporto gestioneSupporto;

    @Autowired
    private GestioneCall gestioneCall;

    @Autowired
    private SQLService sqlService;

    @Autowired
    private GestioneNotifiche gestioneNotifiche;

    private Hackathon hackathon;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public void setHackathon(Hackathon hackathon) {
        this.hackathon = hackathon;
        populateFields();
        setupContextualActions();
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
        lblStato.setText(hackathon.getStato() != null ? hackathon.getStato().name() : "N/D");
        txtRegolamento.setText(hackathon.getRegolamento() != null
                ? hackathon.getRegolamento() : "Nessun regolamento disponibile.");

        // Vincitore
        if (hackathon.getVincitori() != null && !hackathon.getVincitori().isEmpty()) {
            hboxVincitore.setVisible(true);
            hboxVincitore.setManaged(true);
            lblVincitore.setText(hackathon.getVincitori());
        }
    }

    private void setupContextualActions() {
        if (hackathon == null) return;
        topActions.getChildren().clear();

        UtenteGenerico utente = gestioneAccount.getUtenteCorrente();
        StatoHackathon stato = hackathon.getStato();

        // -- Membro Team: bottone Iscriviti (se InIscrizione e ha un team) --
        if (utente instanceof UtenteRegistrato utenteReg
                && utenteReg.getTipo() == TipoUtente.MembroTeam
                && stato == StatoHackathon.InIscrizione) {
            Button btnIscriviti = new Button("Iscriviti");
            btnIscriviti.getStyleClass().add("btn-accent");
            btnIscriviti.setOnAction(e -> onIscriviti(utenteReg));
            topActions.getChildren().add(btnIscriviti);
        }

        // -- Membro Team: sezione sottomissione (se InCorso e team iscritto) --
        if (utente instanceof UtenteRegistrato utenteReg
                && utenteReg.getTipo() == TipoUtente.MembroTeam
                && stato == StatoHackathon.InCorso
                && utenteReg.getTeam() != null) {

            // Verifica se il team è iscritto a questo hackathon
            List<Iscrizioni> iscrizioni = gestioneHackathon.ViewIscrizioni(hackathon.getNome());
            for (Iscrizioni isc : iscrizioni) {
                if (isc.getTeam().equals(utenteReg.getTeam())) {
                    sezioneSottomissione.setVisible(true);
                    sezioneSottomissione.setManaged(true);
                    if (isc.getSottomissioni() != null && !isc.getSottomissioni().isEmpty()) {
                        txtSottomissione.setText(isc.getSottomissioni());
                    }

                    // Mostra sezione richiesta supporto
                    sezioneRichiestaSupp.setVisible(true);
                    sezioneRichiestaSupp.setManaged(true);
                    break;
                }
            }
        }

        // -- Giudice: bottone Valuta (se InValutazione e assegnato) --
        if (utente instanceof Giudice giudice
                && stato == StatoHackathon.InValutazione
                && hackathon.getGiudice() != null
                && hackathon.getGiudice().equals(giudice.getEmail())) {
            Button btnValuta = new Button("Valuta Sottomissioni");
            btnValuta.getStyleClass().add("btn-primary");
            btnValuta.setOnAction(e -> openValutazione());
            topActions.getChildren().add(btnValuta);
        }

        // -- Staff: tutti i membri dello staff possono vedere i team iscritti --
        if (utente instanceof UtenzaAmministrazione) {
            sezioneStaff.setVisible(true);
            sezioneStaff.setManaged(true);
            loadTeamIscritti();
        }

        // -- Mentore: sezione azioni mentore (se assegnato a questo hackathon e InCorso) --
        if (utente instanceof Mentore mentore && stato == StatoHackathon.InCorso) {
            List<MentoriHackathon> mentori = sqlService.getMentoriHackathon(hackathon.getNome());
            boolean isMentoreAssegnato = mentori.stream()
                    .anyMatch(mh -> mh.getEmail().equals(mentore.getEmail()));

            if (isMentoreAssegnato) {
                sezioneMentore.setVisible(true);
                sezioneMentore.setManaged(true);
                loadRichiesteSupporto(mentore);
            }
        }

        // -- Organizzatore: sezione gestione (se è organizzatore di questo hackathon) --
        if (utente instanceof Organizzatore organizzatore
                && hackathon.getOrganizzatore() != null
                && hackathon.getOrganizzatore().equals(organizzatore.getEmail())) {
            sezioneOrganizzatore.setVisible(true);
            sezioneOrganizzatore.setManaged(true);

            // Disabilita proclama vincitore se non in valutazione
            btnProclamaVincitore.setDisable(stato != StatoHackathon.InValutazione);
            // Disabilita cambia stato se concluso
            btnCambiaStato.setDisable(stato == StatoHackathon.Concluso);
        }
    }

    private void onIscriviti(UtenteRegistrato utenteReg) {
        if (utenteReg.getTeam() == null) {
            showFeedback("Devi avere un team per iscriverti.", true);
            return;
        }

        unicam.account.Team team = gestioneAccount.readTeam(utenteReg.getTeam());
        if (team == null) {
            showFeedback("Team non trovato.", true);
            return;
        }

        if (gestioneHackathon.IscriviTeam(team, hackathon)) {
            showFeedback("Team iscritto con successo!", false);
        } else {
            showFeedback("Impossibile iscriversi. Il team potrebbe essere troppo grande o l'hackathon pieno.", true);
        }
    }

    @FXML
    private void onInviaSottomissione() {
        UtenteGenerico utente = gestioneAccount.getUtenteCorrente();
        if (!(utente instanceof UtenteRegistrato utenteReg) || utenteReg.getTeam() == null) {
            showFeedback("Errore: utente non valido.", true);
            return;
        }

        String sottomissione = txtSottomissione.getText().trim();
        if (sottomissione.isEmpty()) {
            showFeedback("La sottomissione non può essere vuota.", true);
            return;
        }

        if (gestioneHackathon.invioSottomissione(utenteReg.getTeam(), hackathon.getNome(), sottomissione)) {
            showFeedback("Sottomissione inviata con successo!", false);
        } else {
            showFeedback("Errore nell'invio della sottomissione.", true);
        }
    }

    @FXML
    private void onCambiaStato() {
        if (gestioneHackathon.cambiaStato(hackathon)) {
            lblStato.setText(hackathon.getStato().name());
            showFeedback("Stato aggiornato a: " + hackathon.getStato().name(), false);
            setupContextualActions();
        } else {
            showFeedback("Errore nel cambio di stato.", true);
        }
    }

    @FXML
    private void onProclamaVincitore() {
        try {
            if (gestioneHackathon.ProclamaVincitore(hackathon)) {
                showFeedback("Vincitore proclamato: " + hackathon.getVincitori(), false);
                hboxVincitore.setVisible(true);
                hboxVincitore.setManaged(true);
                lblVincitore.setText(hackathon.getVincitori());
            } else {
                showFeedback("Errore nella proclamazione del vincitore.", true);
            }
        } catch (Exception e) {
            showFeedback("Nessuna sottomissione presente per proclamare un vincitore.", true);
        }
    }

    private void openValutazione() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Valutazione.fxml"));
            loader.setControllerFactory(CodeBaseApplication.getSpringContext()::getBean);
            Parent root = loader.load();

            ValutazioneController controller = loader.getController();
            controller.setHackathon(hackathon.getNome());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/dark-theme.css").toExternalForm());

            stage.setTitle("Valutazione: " + hackathon.getNome());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== Richiesta Supporto (Membro Team) ==========

    @FXML
    private void onInviaRichiestaSupp() {
        UtenteGenerico utente = gestioneAccount.getUtenteCorrente();
        if (!(utente instanceof UtenteRegistrato utenteReg) || utenteReg.getTeam() == null) {
            showFeedback("Errore: utente non valido.", true);
            return;
        }

        String messaggio = txtRichiestaSupp.getText().trim();
        if (messaggio.isEmpty()) {
            showFeedback("Inserisci una descrizione per la richiesta.", true);
            return;
        }

        if (gestioneSupporto.inviaRichiestaSupporto(utenteReg.getTeam(), hackathon.getNome(), messaggio)) {
            showFeedback("Richiesta di supporto inviata!", false);
            txtRichiestaSupp.clear();
        } else {
            showFeedback("Errore nell'invio della richiesta.", true);
        }
    }

    // ========== Azioni Mentore ==========

    @FXML
    private void onSegnalaTeam() {
        if (!(gestioneAccount.getUtenteCorrente() instanceof Mentore mentore)) return;

        // Chiedi il nome del team e il motivo
        TextInputDialog dialogTeam = new TextInputDialog();
        dialogTeam.setTitle("Segnala Team");
        dialogTeam.setHeaderText("Segnalazione violazione regolamento");
        dialogTeam.setContentText("Nome del team da segnalare:");

        Optional<String> teamResult = dialogTeam.showAndWait();
        if (teamResult.isEmpty() || teamResult.get().trim().isEmpty()) return;

        TextInputDialog dialogMotivo = new TextInputDialog();
        dialogMotivo.setTitle("Segnala Team");
        dialogMotivo.setHeaderText("Motivo della segnalazione");
        dialogMotivo.setContentText("Descrivi la violazione:");

        Optional<String> motivoResult = dialogMotivo.showAndWait();
        if (motivoResult.isEmpty() || motivoResult.get().trim().isEmpty()) return;

        String teamNome = teamResult.get().trim();
        if (gestioneHackathon.segnalaTeam(teamNome, hackathon.getNome(),
                motivoResult.get().trim(), mentore.getEmail())) {
            showFeedback("Segnalazione inviata all'organizzatore.", false);

            // Notifica ai membri del team segnalato
            notificaMembriTeam(teamNome,
                    "Il tuo team è stato segnalato per: " + motivoResult.get().trim(),
                    mentore.getEmail(), TipoNotifica.SEGNALAZIONE);
        } else {
            showFeedback("Errore nell'invio della segnalazione.", true);
        }
    }

    @FXML
    private void onProponiCall() {
        if (!(gestioneAccount.getUtenteCorrente() instanceof Mentore mentore)) return;

        // Selezione del team destinatario
        List<Iscrizioni> iscrizioni = gestioneHackathon.ViewIscrizioni(hackathon.getNome());
        List<String> teamNames = iscrizioni.stream().map(Iscrizioni::getTeam).toList();

        if (teamNames.isEmpty()) {
            showFeedback("Nessun team iscritto a questo hackathon.", true);
            return;
        }

        ChoiceDialog<String> dialogTeam = new ChoiceDialog<>(teamNames.get(0), teamNames);
        dialogTeam.setTitle("Proponi Call");
        dialogTeam.setHeaderText("Seleziona il team destinatario");
        dialogTeam.setContentText("Team:");

        Optional<String> teamResult = dialogTeam.showAndWait();
        if (teamResult.isEmpty()) return;

        TextInputDialog dialogTitolo = new TextInputDialog();
        dialogTitolo.setTitle("Proponi Call");
        dialogTitolo.setHeaderText("Nuova call per hackathon: " + hackathon.getNome());
        dialogTitolo.setContentText("Titolo della call:");

        Optional<String> titoloResult = dialogTitolo.showAndWait();
        if (titoloResult.isEmpty() || titoloResult.get().trim().isEmpty()) return;

        TextInputDialog dialogDesc = new TextInputDialog();
        dialogDesc.setTitle("Proponi Call");
        dialogDesc.setHeaderText("Descrizione (opzionale)");
        dialogDesc.setContentText("Descrizione:");

        Optional<String> descResult = dialogDesc.showAndWait();
        String descrizione = descResult.orElse("");

        // Data: usa la data corrente + 1 giorno come default
        Date dataOra = new Date(System.currentTimeMillis() + 86400000L);

        String teamNome = teamResult.get();
        if (gestioneCall.proponiCall(mentore.getEmail(), hackathon.getNome(),
                titoloResult.get().trim(), descrizione, dataOra, "", teamNome)) {
            showFeedback("Call proposta al team " + teamNome + "!", false);

            // Notifica i membri del team
            notificaMembriTeam(teamNome,
                    "Il mentore ti ha proposto una call: " + titoloResult.get().trim(),
                    mentore.getEmail(), TipoNotifica.PROPOSTA_CALL);
        } else {
            showFeedback("Errore nella creazione della call.", true);
        }
    }

    private void loadRichiesteSupporto(Mentore mentore) {
        listaRichiesteSupp.getChildren().clear();
        List<RichiesteSupporto> richieste = gestioneSupporto.visualizzaRichieste(hackathon.getNome());

        if (richieste.isEmpty()) {
            Label empty = new Label("Nessuna richiesta di supporto.");
            empty.getStyleClass().add("text-secondary");
            listaRichiesteSupp.getChildren().add(empty);
            return;
        }

        for (RichiesteSupporto r : richieste) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-padding: 8; -fx-background-color: #2A2A40; -fx-background-radius: 8;");

            Label info = new Label("Team: " + r.getTeamNome() + " | " + r.getMessaggio()
                    + " [" + r.getStato().name() + "]");
            info.setStyle("-fx-text-fill: #E2E8F0; -fx-font-size: 12px;");
            info.setWrapText(true);
            row.getChildren().add(info);

            listaRichiesteSupp.getChildren().add(row);
        }
    }

    // ========== Azioni Organizzatore ==========

    @FXML
    private void onAmmonisciTeam() {
        if (!(gestioneAccount.getUtenteCorrente() instanceof Organizzatore)) return;

        // Mostra segnalazioni non gestite
        List<Segnalazione> segnalazioni =
                gestioneHackathon.visualizzaSegnalazioniNonGestite(hackathon.getNome());

        if (segnalazioni.isEmpty()) {
            showFeedback("Nessuna segnalazione da gestire.", false);
            return;
        }

        // Per semplicità gestisci la prima segnalazione
        Segnalazione seg = segnalazioni.get(0);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Gestisci Segnalazione");
        alert.setHeaderText("Segnalazione per team: " + seg.getTeamNome());
        alert.setContentText("Motivo: " + seg.getMotivo()
                + "\nSegnalato da: " + seg.getSegnalante()
                + "\n\nVuoi ammonire il team?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (gestioneHackathon.gestisciSegnalazione(seg.getId(), "Team ammonito")) {
                showFeedback("Team " + seg.getTeamNome() + " ammonito.", false);

                // Notifica ai membri del team ammonito
                Organizzatore org = (Organizzatore) gestioneAccount.getUtenteCorrente();
                notificaMembriTeam(seg.getTeamNome(),
                        "Il tuo team è stato ammonito dall'organizzatore per: " + seg.getMotivo(),
                        org.getEmail(), TipoNotifica.AMMONIZIONE);
            } else {
                showFeedback("Errore nella gestione della segnalazione.", true);
            }
        }
    }

    @FXML
    private void onAddMentori() {
        if (!(gestioneAccount.getUtenteCorrente() instanceof Organizzatore)) return;

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Aggiungi Mentori");
        dialog.setHeaderText("Aggiungi mentori a: " + hackathon.getNome());
        dialog.setContentText("Email mentori (separati da virgola):");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty() || result.get().trim().isEmpty()) return;

        String[] emails = result.get().split(",");
        int aggiunti = 0;
        for (String email : emails) {
            String trimmed = email.trim();
            if (!trimmed.isEmpty()) {
                MentoriHackathon mh = new MentoriHackathon(trimmed, hackathon.getNome());
                if (sqlService.salvaMentori(mh)) aggiunti++;
            }
        }
        showFeedback(aggiunti + " mentore/i aggiunto/i.", false);
    }

    private void notificaMembriTeam(String teamNome, String messaggio, String mittente, TipoNotifica tipo) {
        List<UtenteRegistrato> membri = sqlService.getMembriTeam(teamNome);
        for (UtenteRegistrato membro : membri) {
            Notifiche notifica = new Notifiche(tipo, messaggio, mittente, membro.getEmail());
            gestioneNotifiche.CreaNotifica(notifica);
        }
    }

    // ========== Staff: Lista Team Iscritti ==========

    private void loadTeamIscritti() {
        listaTeamIscritti.getChildren().clear();
        List<Iscrizioni> iscrizioni = gestioneHackathon.ViewIscrizioni(hackathon.getNome());

        if (iscrizioni.isEmpty()) {
            Label empty = new Label("Nessun team iscritto.");
            empty.getStyleClass().add("text-secondary");
            listaTeamIscritti.getChildren().add(empty);
            return;
        }

        for (Iscrizioni isc : iscrizioni) {
            VBox card = new VBox(6);
            card.setStyle("-fx-padding: 10; -fx-background-color: #2A2A40; -fx-background-radius: 8;");

            Label teamName = new Label("Team: " + isc.getTeam());
            teamName.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #F8FAFC;");

            String sottText = (isc.getSottomissioni() != null && !isc.getSottomissioni().isEmpty())
                    ? isc.getSottomissioni() : "Nessuna sottomissione";
            Label sott = new Label("Sottomissione: " + sottText);
            sott.setStyle("-fx-text-fill: #E2E8F0; -fx-font-size: 12px;");
            sott.setWrapText(true);

            Label voto = new Label("Voto: " + isc.getVoto() + "/10");
            voto.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 12px;");

            card.getChildren().addAll(teamName, sott, voto);
            listaTeamIscritti.getChildren().add(card);
        }
    }

    @FXML
    private void onIndietro() {
        Stage stage = (Stage) btnIndietro.getScene().getWindow();
        stage.close();
    }

    private void showFeedback(String msg, boolean isError) {
        lblFeedback.setText(msg);
        lblFeedback.setStyle("-fx-font-size: 14px; -fx-text-fill: " + (isError ? "#EF4444;" : "#00F5A0;"));
        lblFeedback.setVisible(true);
        lblFeedback.setManaged(true);
    }
}
