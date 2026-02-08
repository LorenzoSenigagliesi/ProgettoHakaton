package unicam;
import unicam.hackathon.Hackathon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLService {
    private String url;
    private String user;
    private String password;

    public boolean HackathonExist(String HackatonName) {
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM Hackathon where Name = " + HackatonName;
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

}
