package simulation;

import database.Db;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Simulation {
    int simulationID = 0;
    String simulationName = LocalDate.now().toString();
    ArrayList<IRound> iRounds = new ArrayList<IRound>();

    public Simulation() {
    }

    public Simulation(int simulationID, String simulationName) {
        this.simulationID = simulationID;
        this.simulationName = simulationName;
    }

    public ArrayList<IRound> getiRounds() {
        return iRounds;
    }

    public void setiRounds(ArrayList<IRound> iRounds) {
        this.iRounds = iRounds;
    }

    public void onInsertSimulationInDatabase() {
        Db db = new Db();
        Connection con = db.getCon();
        ResultSet rs = db.getRs();
        PreparedStatement pstmt;
        String query = "insert into Simulation(simulationName" +
                ")values (?)";
        try {
            pstmt = (PreparedStatement) con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, this.getSimulationName());

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                System.out.println("Generated Emp Id: " + rs.getInt(1));
                this.setSimulationID(rs.getInt(1));
            }

            for (int i = 0; i < getiRounds().size(); i++) {
                IRound iRound = this.getiRounds().get(i);
                iRound.setSimulationID(simulationID);
                iRound.onInsertiRoundInDatabase();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getSimulationID() {
        return simulationID;
    }

    public void setSimulationID(int simulationID) {
        this.simulationID = simulationID;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public void setSimulationName(String simulationName) {
        this.simulationName = simulationName;
    }
}
