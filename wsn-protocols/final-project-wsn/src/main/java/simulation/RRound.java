package simulation;

import database.Db;
import leachRL.NodeRL;

import java.sql.*;
import java.util.ArrayList;

public class RRound {
    int iRoundID;
    int simulationID;
    int nbRound;
    int nbCH;
    ArrayList<RNode> nodes = new ArrayList<RNode>();

    public RRound() {
    }

    public RRound(int iRoundID, int simulationID, int nbRound, int nbCH, ArrayList<RNode> nodes) {
        this.iRoundID = iRoundID;
        this.simulationID = simulationID;
        this.nbRound = nbRound;
        this.nbCH = nbCH;
        this.nodes = nodes;
    }

    public int getNbCH() {
        return nbCH;
    }

    public void setNbCH(int nbCH) {
        this.nbCH = nbCH;
    }


    public void onInsertiRoundInDatabase() {
        Db db = new Db();
        Connection con = db.getCon();
        ResultSet rs = db.getRs();
        PreparedStatement pstmt;
        String query = "insert into IRound(simulationID," +
                "nbround" +
                ")values (?,?)";
        try {
            pstmt = (PreparedStatement) con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, this.getSimulationID());
            pstmt.setInt(2, this.getNbRound());


            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                System.out.println("Generated Emp Id: " + rs.getInt(1));
                this.setiRoundID(rs.getInt(1));
            }
            for (int i = 0; i < nodes.size(); i++) {
                RNode node = nodes.get(i);
                //node.onInsertNodeInDatabase(this.getiRoundID());

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


    @Override
    public String toString() {
        return "IRound{" +
                "iRoundID=" + iRoundID +
                ", simulationID=" + simulationID +
                ", nbRound=" + nbRound +
                ", nodes=" + nodes +
                '}';
    }

    public int getiRoundID() {
        return iRoundID;
    }

    public void setiRoundID(int iRoundID) {
        this.iRoundID = iRoundID;
    }

    public int getSimulationID() {
        return simulationID;
    }

    public void setSimulationID(int simulationID) {
        this.simulationID = simulationID;
    }

    public int getNbRound() {
        return nbRound;
    }

    public void setNbRound(int nbRound) {
        this.nbRound = nbRound;
    }

    public ArrayList<RNode> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<NodeRL> nodes) {
        for (NodeRL node : nodes) {
            this.nodes.add(new RNode(node));
        }

    }
}
