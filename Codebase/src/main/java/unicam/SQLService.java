package unicam;
import unicam.account.Team;
import unicam.account.UtenteGenerico;
import unicam.account.UtenteRegistrato;
import unicam.amministrazione.MembroStaff;
import unicam.hackathon.Hackathon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLService {
    private static final String url = "";
    private static final String user = "";
    private static final String password = "";

    public boolean HackathonExist(String HackatonName) {
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM Hackathon where Name = '" + HackatonName + "'";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, "admin");
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean CreateHackathon(Hackathon NewHackaton) {
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = "INSERT INTO table_name (Nome, Data, Luogo, Regolamento, DataFine, DataFineIscrizioni,DimTeam,Stato) " +
                                           "VALUES ('"+ NewHackaton.getNome() + "'," +
                                                   "'" + NewHackaton.getData()+ "'," +
                                                   "'" + NewHackaton.getLuogo()+ "'," +
                                                   "'" + NewHackaton.getRegolamento()+ "'," +
                                                   "'" + NewHackaton.getDataFine()+ "'," +
                                                   "'" + NewHackaton.getDataFineIscrizioni()+ "'," +
                                                   "'" + NewHackaton.getDimTeam()+ "'," +
                                                   "'" + NewHackaton.getDataFine()+"'," +
                                                   "'" + NewHackaton.getStato()+ "')";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, "admin");
            ResultSet rs = pstmt.executeQuery();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public UtenteRegistrato loginUtenti(String email, String password){
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM Utenti where email = '" + email + "' and password = '" + password + "'";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, "admin");
            ResultSet rs = pstmt.executeQuery();
            String tipoUtente = rs.getString("tipo_utente"); // o il nome della colonna che distingue il tipo

            UtenteRegistrato utente = new UtenteRegistrato(
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
            );
            utente.setTeam(rs.getString("team"));
            return utente;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MembroStaff loginAmministrazione(String email, String password){
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM Amministrazione where email = '" + email + "' and password = '" + password + "'";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, "admin");
            ResultSet rs = pstmt.executeQuery();
            String tipoUtente = rs.getString("tipo_utente"); // o il nome della colonna che distingue il tipo

            return new MembroStaff(
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean registrazione(UtenteRegistrato utente){
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = "INSERT INTO Utenti (username, email, password, team) VALUES ('" + utente.getUserName() + "','" +
                                                                                              utente.getEmail() + "','" +
                                                                                              utente.getPassword() + "','" +
                                                                                              utente.getTeam() + "')";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, "admin");
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkAccount(String email){
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM Utenti where email = '" + email + "'";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, "admin");
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Team readTeam(String nome){
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM Team where email = '" + nome + "'";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, "admin");
            ResultSet rs = pstmt.executeQuery();
            String tipoUtente = rs.getString("tipo_utente"); // o il nome della colonna che distingue il tipo

            Team team = new Team(
                    rs.getString("nome")
            );
            team.setDim(rs.getInt("dim"));
            return team;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
