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
import unicam.account.GestioneAccount;
import unicam.account.UtenteGenerico;
import unicam.account.UtenteRegistrato;
import unicam.account.TipoUtente;
import unicam.amministrazione.Giudice;
import unicam.amministrazione.Mentore;
import unicam.amministrazione.Organizzatore;
import unicam.amministrazione.UtenzaAmministrazione;
import unicam.SQLService;
import unicam.hackathon.GestioneHackathon;
import unicam.hackathon.Hackathon;
import unicam.hackathon.Iscrizioni;
import unicam.hackathon.StatoHackathon;
import unicam.supporto.GestioneSupporto;
import unicam.notifiche.GestioneNotifiche;
import unicam.notifiche.Notifiche;
import unicam.notifiche.TipoNotifica;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Component
@Scope("prototype")
public class HomeController {

    @FXML private VBox hackathonList;
    @FXML private HBox headerActions;
    @FXML private HBox userInfoBar;
    @FXML private Label lblUserInfo;

    @Autowired
    private GestioneAccount gestioneAccount;

    @Autowired
    private GestioneHackathon gestioneHackathon;

    @Autowired
    private SQLService sqlService;

    @Autowired
    private GestioneNotifiche gestioneNotifiche;

    @Autowired
    private GestioneSupporto gestioneSupporto;

    @FXML
    public void initialize() {
        refreshUI();
    }

    private void refreshUI() {
        updateHeader();
        loadHackathons();
    }

    private void updateHeader() {
        headerActions.getChildren().clear();
        userInfoBar.getChildren().clear();

        if (!gestioneAccount.isLoggedIn()) {
            // Visitatore: Login + Registrati
            lblUserInfo.setText("Consultazione elenco hackathon");

            Button btnLogin = new Button("Accedi");
            btnLogin.getStyleClass().add("btn-primary");
            btnLogin.setOnAction(e -> openModal("/fxml/Login.fxml", "Accedi", 450, 500));

            Button btnRegister = new Button("Registrati");
            btnRegister.getStyleClass().add("btn-accent");
            btnRegister.setOnAction(e -> openModal("/fxml/Registration.fxml", "Registrati", 450, 550));

            headerActions.getChildren().addAll(btnLogin, btnRegister);
            userInfoBar.setVisible(false);
            userInfoBar.setManaged(false);
        } else {
            // Utente loggato
            UtenteGenerico utente = gestioneAccount.getUtenteCorrente();
            lblUserInfo.setText("Benvenuto, " + utente.getUsername());

            Button btnLogout = new Button("Logout");
            btnLogout.getStyleClass().add("btn-secondary");
            btnLogout.setOnAction(e -> {
                gestioneAccount.logout();
                refreshUI();
            });

            headerActions.getChildren().add(btnLogout);

            // User info bar
            userInfoBar.setVisible(true);
            userInfoBar.setManaged(true);

            Label lblUser = new Label("Utente: " + utente.getUsername());
            lblUser.setStyle("-fx-text-fill: #F8FAFC; -fx-font-size: 14px; -fx-font-weight: bold;");
            userInfoBar.getChildren().add(lblUser);

            if (utente instanceof UtenteRegistrato utenteReg) {
                // Mostra info team
                if (utenteReg.getTeam() != null) {
                    Label lblTeam = new Label("Team: " + utenteReg.getTeam());
                    lblTeam.setStyle("-fx-text-fill: #00F5A0; -fx-font-size: 13px;");
                    userInfoBar.getChildren().add(lblTeam);
                }

                Label lblTipo = new Label("Ruolo: " + utenteReg.getTipo().name());
                lblTipo.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 13px;");
                userInfoBar.getChildren().add(lblTipo);

                // Bottone Crea Team (solo se non ha un team)
                if (utenteReg.getTeam() == null && utenteReg.getTipo() == TipoUtente.Utente) {
                    Button btnCreaTeam = new Button("Crea Team");
                    btnCreaTeam.getStyleClass().add("btn-accent");
                    btnCreaTeam.setStyle("-fx-padding: 6 16; -fx-font-size: 12px;");
                    btnCreaTeam.setOnAction(e -> openModal("/fxml/CreateTeam.fxml", "Crea Team", 500, 500));
                    headerActions.getChildren().addFirst(btnCreaTeam);
                }

                // Bottone Invita nel Team e Visualizza Membri (solo se ha già un team)
                if (utenteReg.getTeam() != null) {
                    Button btnInvita = new Button("Invita nel Team");
                    btnInvita.getStyleClass().add("btn-primary");
                    btnInvita.setStyle("-fx-padding: 6 16; -fx-font-size: 12px;");
                    btnInvita.setOnAction(e -> showInvitaDialog(utenteReg));
                    headerActions.getChildren().addFirst(btnInvita);

                    Button btnMembri = new Button("Membri Team");
                    btnMembri.getStyleClass().add("btn-secondary");
                    btnMembri.setStyle("-fx-padding: 6 16; -fx-font-size: 12px;");
                    btnMembri.setOnAction(e -> showMembriTeam(utenteReg.getTeam()));
                    headerActions.getChildren().addFirst(btnMembri);

                    // Bottone Richiedi Supporto (se iscritto a un hackathon InCorso)
                    if (utenteReg.getTipo() == TipoUtente.MembroTeam) {
                        Button btnSupporto = new Button("Richiedi Supporto");
                        btnSupporto.getStyleClass().add("btn-warning");
                        btnSupporto.setStyle("-fx-padding: 6 16; -fx-font-size: 12px;");
                        btnSupporto.setOnAction(e -> showRichiediSupporto(utenteReg));
                        headerActions.getChildren().addFirst(btnSupporto);
                    }
                }

                // Bottone Notifiche / Accettazione invito
                Button btnNotifiche = new Button("Notifiche");
                btnNotifiche.getStyleClass().add("btn-primary");
                btnNotifiche.setStyle("-fx-padding: 6 16; -fx-font-size: 12px;");
                btnNotifiche.setOnAction(e -> showNotifiche(utenteReg));
                headerActions.getChildren().addFirst(btnNotifiche);
            }

            if (utente instanceof UtenzaAmministrazione staff) {
                Label lblRuolo = new Label("Staff: " + staff.getRuolo());
                lblRuolo.setStyle("-fx-text-fill: #A855F7; -fx-font-size: 13px; -fx-font-weight: bold;");
                userInfoBar.getChildren().add(lblRuolo);

                // Cambia Ruolo
                Button btnCambiaRuolo = new Button("Cambia Ruolo");
                btnCambiaRuolo.getStyleClass().add("btn-primary");
                btnCambiaRuolo.setStyle("-fx-padding: 6 16; -fx-font-size: 12px;");
                btnCambiaRuolo.setOnAction(e -> showCambiaRuolo());
                headerActions.getChildren().addFirst(btnCambiaRuolo);

                // Bottoni specifici per ruolo
                if (utente instanceof Organizzatore) {
                    Button btnCreaHackathon = new Button("Crea Hackathon");
                    btnCreaHackathon.getStyleClass().add("btn-accent");
                    btnCreaHackathon.setStyle("-fx-padding: 6 16; -fx-font-size: 12px;");
                    btnCreaHackathon.setOnAction(e -> openModal("/fxml/CreateHackathon.fxml",
                            "Nuovo Hackathon", 700, 750));
                    headerActions.getChildren().addFirst(btnCreaHackathon);
                }

                if (utente instanceof Giudice giudice) {
                    Button btnValuta = new Button("Hackathon da Giudicare");
                    btnValuta.getStyleClass().add("btn-primary");
                    btnValuta.setStyle("-fx-padding: 6 16; -fx-font-size: 12px;");
                    btnValuta.setOnAction(e -> showHackathonDaGiudicare(giudice));
                    headerActions.getChildren().addFirst(btnValuta);
                }

                if (utente instanceof Mentore mentore) {
                    Button btnDashboard = new Button("Dashboard Mentore");
                    btnDashboard.getStyleClass().add("btn-accent");
                    btnDashboard.setStyle("-fx-padding: 6 16; -fx-font-size: 12px;");
                    btnDashboard.setOnAction(e -> openDashboardMentore());
                    headerActions.getChildren().addFirst(btnDashboard);
                }
            }
        }
    }

    private void loadHackathons() {
        List<Hackathon> hackathons = gestioneHackathon.viewHackathon();
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

        String statoText = h.getStato() != null ? h.getStato().name() : "N/D";
        Label stato = new Label(statoText);
        stato.getStyleClass().add("badge-stato");

        // Colori diversi per stato
        if (h.getStato() != null) {
            switch (h.getStato()) {
                case InIscrizione -> stato.setStyle(stato.getStyle() + "-fx-background-color: #6366F1;");
                case InCorso -> stato.setStyle(stato.getStyle() + "-fx-background-color: #00F5A0; -fx-text-fill: #0F0F1E;");
                case InValutazione -> stato.setStyle(stato.getStyle() + "-fx-background-color: #F59E0B; -fx-text-fill: #0F0F1E;");
                case Concluso -> stato.setStyle(stato.getStyle() + "-fx-background-color: #94A3B8;");
            }
        }

        infoRow.getChildren().addAll(luogo, data, stato);

        // Vincitore se concluso
        if (h.getStato() == StatoHackathon.Concluso && h.getVincitori() != null) {
            Label vincitore = new Label("Vincitore: " + h.getVincitori());
            vincitore.setStyle("-fx-text-fill: #00F5A0; -fx-font-size: 13px; -fx-font-weight: bold;");
            infoRow.getChildren().add(vincitore);
        }

        card.getChildren().addAll(nome, infoRow);
        card.setOnMouseClicked(event -> openDetail(h));

        return card;
    }

    private void showHackathonDaGiudicare(Giudice giudice) {
        List<Hackathon> hackathons = gestioneHackathon.HackathonDaGiudicare(giudice.getEmail());
        if (hackathons.isEmpty()) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Nessun Hackathon");
            alert.setHeaderText(null);
            alert.setContentText("Non hai hackathon da giudicare al momento.");
            alert.showAndWait();
            return;
        }

        // Apri il primo hackathon da giudicare nella schermata valutazione
        for (Hackathon h : hackathons) {
            openValutazione(h.getNome());
        }
    }

    private void openValutazione(String nomeHackathon) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Valutazione.fxml"));
            loader.setControllerFactory(CodeBaseApplication.getSpringContext()::getBean);
            Parent root = loader.load();

            ValutazioneController controller = loader.getController();
            controller.setHackathon(nomeHackathon);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/dark-theme.css").toExternalForm());

            stage.setTitle("Valutazione: " + nomeHackathon);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            detailStage.setOnHidden(e -> refreshUI());

            Scene scene = new Scene(root, 800, 650);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/dark-theme.css").toExternalForm());

            detailStage.setTitle(hackathon.getNome());
            detailStage.setScene(scene);
            detailStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotifiche(UtenteRegistrato utente) {
        List<Notifiche> notifiche = sqlService.getNotifichePerUtente(utente.getEmail());

        if (notifiche.isEmpty()) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Notifiche");
            alert.setHeaderText(null);
            alert.setContentText("Nessuna notifica.");
            alert.showAndWait();
            return;
        }

        // Cerca inviti team pendenti
        Optional<Notifiche> invito = notifiche.stream()
                .filter(n -> n.getTipo() == TipoNotifica.INVITO_TEAM)
                .findFirst();

        if (invito.isPresent()) {
            Notifiche inv = invito.get();
            javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Invito al Team");
            confirm.setHeaderText("Hai un invito!");
            confirm.setContentText(inv.getMessaggio() + "\nDa: " + inv.getMittente()
                    + "\n\nVuoi accettare?");

            Optional<javafx.scene.control.ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
                // Estrai il nome del team dal messaggio
                String msg = inv.getMessaggio();
                int start = msg.indexOf("'") + 1;
                int end = msg.lastIndexOf("'");
                if (start > 0 && end > start) {
                    String nomeTeam = msg.substring(start, end);
                    if (gestioneAccount.accettazioneTeam(nomeTeam)) {
                        javafx.scene.control.Alert ok = new javafx.scene.control.Alert(
                                javafx.scene.control.Alert.AlertType.INFORMATION);
                        ok.setTitle("Successo");
                        ok.setHeaderText(null);
                        ok.setContentText("Sei entrato nel team '" + nomeTeam + "'!");
                        ok.showAndWait();
                        refreshUI();
                    }
                }
            }
        } else {
            // Mostra altre notifiche
            StringBuilder sb = new StringBuilder();
            for (Notifiche n : notifiche) {
                sb.append("[" + n.getTipo() + "] " + n.getMessaggio() + "\n");
            }
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Notifiche");
            alert.setHeaderText("Le tue notifiche");
            alert.setContentText(sb.toString());
            alert.showAndWait();
        }
    }

    private void showCambiaRuolo() {
        javafx.scene.control.ChoiceDialog<String> dialog = new javafx.scene.control.ChoiceDialog<>(
                "Organizzatore", "Organizzatore", "Mentore", "Giudice");
        dialog.setTitle("Cambia Ruolo");
        dialog.setHeaderText("Seleziona il nuovo ruolo");
        dialog.setContentText("Ruolo:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(ruolo -> {
            UtenteGenerico corrente = gestioneAccount.getUtenteCorrente();
            if (corrente instanceof UtenzaAmministrazione admin) {
                unicam.amministrazione.StaffDecorator nuovoRuolo = switch (ruolo) {
                    case "Organizzatore" -> new Organizzatore(admin);
                    case "Mentore" -> new unicam.amministrazione.Mentore(admin);
                    case "Giudice" -> new Giudice(admin);
                    default -> null;
                };
                if (nuovoRuolo != null) {
                    gestioneAccount.cambiaRuolo(nuovoRuolo);
                    refreshUI();
                }
            }
        });
    }

    private void showMembriTeam(String nomeTeam) {
        List<UtenteRegistrato> membri = sqlService.getMembriTeam(nomeTeam);

        if (membri.isEmpty()) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Membri Team");
            alert.setHeaderText(null);
            alert.setContentText("Nessun membro nel team.");
            alert.showAndWait();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (UtenteRegistrato m : membri) {
            sb.append(m.getUsername()).append(" (" ).append(m.getEmail()).append(")\n");
        }

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Membri Team: " + nomeTeam);
        alert.setHeaderText("Membri del team (" + membri.size() + ")");
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }

    private void showInvitaDialog(UtenteRegistrato utenteReg) {
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog();
        dialog.setTitle("Invita nel Team");
        dialog.setHeaderText("Invita utenti nel team '" + utenteReg.getTeam() + "'");
        dialog.setContentText("Email (separati da virgola):");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty() || result.get().trim().isEmpty()) return;

        String[] emails = result.get().split(",");
        int inviati = 0;
        for (String email : emails) {
            String trimmed = email.trim();
            if (!trimmed.isEmpty()) {
                Notifiche invito = new Notifiche(
                        TipoNotifica.INVITO_TEAM,
                        "Sei stato invitato a unirti al team '" + utenteReg.getTeam() + "'",
                        utenteReg.getEmail(),
                        trimmed
                );
                if (gestioneNotifiche.CreaNotifica(invito)) inviati++;
            }
        }

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Inviti Inviati");
        alert.setHeaderText(null);
        alert.setContentText(inviati + " invito/i inviato/i con successo!");
        alert.showAndWait();
    }

    private void showRichiediSupporto(UtenteRegistrato utenteReg) {
        if (utenteReg.getTeam() == null) return;

        // Trova hackathon InCorso a cui il team è iscritto
        List<Hackathon> hackathonsInCorso = gestioneHackathon.viewHackathon().stream()
                .filter(h -> h.getStato() == StatoHackathon.InCorso)
                .filter(h -> {
                    List<Iscrizioni> iscrizioni = gestioneHackathon.ViewIscrizioni(h.getNome());
                    return iscrizioni.stream().anyMatch(i -> i.getTeam().equals(utenteReg.getTeam()));
                })
                .toList();

        if (hackathonsInCorso.isEmpty()) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Richiedi Supporto");
            alert.setHeaderText(null);
            alert.setContentText("Non sei iscritto a nessun hackathon in corso.");
            alert.showAndWait();
            return;
        }

        // Se iscritto a più hackathon, fai scegliere
        String hackathonNome;
        if (hackathonsInCorso.size() == 1) {
            hackathonNome = hackathonsInCorso.get(0).getNome();
        } else {
            List<String> nomi = hackathonsInCorso.stream().map(Hackathon::getNome).toList();
            javafx.scene.control.ChoiceDialog<String> scelta = new javafx.scene.control.ChoiceDialog<>(
                    nomi.get(0), nomi);
            scelta.setTitle("Richiedi Supporto");
            scelta.setHeaderText("Seleziona l'hackathon");
            scelta.setContentText("Hackathon:");
            Optional<String> result = scelta.showAndWait();
            if (result.isEmpty()) return;
            hackathonNome = result.get();
        }

        // Chiedi il messaggio
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog();
        dialog.setTitle("Richiedi Supporto");
        dialog.setHeaderText("Richiesta supporto per: " + hackathonNome);
        dialog.setContentText("Descrivi la tua richiesta:");

        Optional<String> msgResult = dialog.showAndWait();
        if (msgResult.isEmpty() || msgResult.get().trim().isEmpty()) return;

        if (gestioneSupporto.inviaRichiestaSupporto(utenteReg.getTeam(), hackathonNome, msgResult.get().trim())) {
            javafx.scene.control.Alert ok = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.INFORMATION);
            ok.setTitle("Successo");
            ok.setHeaderText(null);
            ok.setContentText("Richiesta di supporto inviata al mentore!");
            ok.showAndWait();
        } else {
            javafx.scene.control.Alert err = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
            err.setTitle("Errore");
            err.setHeaderText(null);
            err.setContentText("Errore nell'invio della richiesta.");
            err.showAndWait();
        }
    }

    private void openDashboardMentore() {
        openModal("/fxml/Mentore.fxml", "Dashboard Mentore", 1000, 700);
    }

    private void openModal(String fxmlPath, String title, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(CodeBaseApplication.getSpringContext()::getBean);
            Parent root = loader.load();

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setOnHidden(e -> refreshUI());

            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/dark-theme.css").toExternalForm());

            modalStage.setTitle(title);
            modalStage.setScene(scene);
            modalStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
