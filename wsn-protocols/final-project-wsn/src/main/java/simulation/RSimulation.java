package simulation;

import leachRL.Point;

import java.time.LocalDate;
import java.util.ArrayList;

public class RSimulation {
    int simulationID = 0;
    String simulationName = LocalDate.now().toString();
    ArrayList<RRound> iRounds = new ArrayList<RRound>();
    ArrayList<Point> points = new ArrayList<>();

    public RSimulation(ArrayList<Point> points) {
        this.points.addAll(points);
    }

    public RSimulation(int simulationID, String simulationName) {
        this.simulationID = simulationID;
        this.simulationName = simulationName;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public ArrayList<RRound> getiRounds() {
        return iRounds;
    }

    public void setiRounds(ArrayList<RRound> iRounds) {
        this.iRounds = iRounds;
    }

    //    public void  onInsertSimulationInDatabase(){
//        Db db = new Db();
//        Connection con =db.getCon();
//        ResultSet rs =db.getRs();
//        PreparedStatement pstmt;
//        String query = "insert into Simulation(simulationName"+
//                ")values (?)";
//        try {
//            pstmt = (PreparedStatement) con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
//            pstmt.setString(1, this.getSimulationName());
//
//            pstmt.executeUpdate();
//
//            rs = pstmt.getGeneratedKeys();
//            if (rs != null && rs.next()) {
//                System.out.println("Generated Emp Id: " + rs.getInt(1));
//                this.setSimulationID(rs.getInt(1));
//            }
//
//            for (int i=0;i<getiRounds().size();i++){
//                IRound iRound = this.getiRounds().get(i);
//                iRound.setSimulationID(simulationID);
//                iRound.onInsertiRoundInDatabase();
//
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        try {
//            db.closeConnection();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
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
