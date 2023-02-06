package network;

import database.Db;

import java.sql.*;

public class Node {
    private int id;//sensor's ID number
    private double x;//X-axis coordinates of sensor node
    private double y;//Y-axis coordinates of sensor node
    private double e;//nodes energy levels (initially set to be equal to "Eo")
    private int role;//node acts as normal if the value is '0', if elected as a cluster head it  gets the value '1' (initially all nodes are normal)
    private int cluster;//the cluster which a node belongs to
    private int cond;//States the current condition of the node. when the node is operational its value is =1 and when dead =0
    private int rop;//number of rounds node was operational
    private double rleft;//rounds left for node to become available for Cluster Head election
    private double dtch;//nodes distance from the cluster head of the cluster in which he belongs
    private double dts;//nodes distance from the sink
    private int tel;//states how many times the node was elected as a Cluster Head
    private int rn;//round node got elected as cluster head
    private int chid;//node ID of the cluster head which the "i" normal node belongs to

    public Node(int id, double x, double y, double e, int role, int cluster, int cond, int rop, double rleft, double dtch, double dts, int tel, int rn, int chid) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.e = e;
        this.role = role;
        this.cluster = cluster;
        this.cond = cond;
        this.rop = rop;
        this.rleft = rleft;
        this.dtch = dtch;
        this.dts = dts;
        this.tel = tel;
        this.rn = rn;
        this.chid = chid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getE() {
        return e;
    }

    public void setE(double e) {
        this.e = e;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getCluster() {
        return cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    public int getCond() {
        return cond;
    }

    public void setCond(int cond) {
        this.cond = cond;
    }

    public int getRop() {
        return rop;
    }

    public void setRop(int rop) {
        this.rop = rop;
    }

    public double getRleft() {
        return rleft;
    }

    public void setRleft(double rleft) {
        this.rleft = rleft;
    }

    public double getDtch() {
        return dtch;
    }

    public void setDtch(double dtch) {
        this.dtch = dtch;
    }

    public double getDts() {
        return dts;
    }

    public void setDts(double dts) {
        this.dts = dts;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public int getRn() {
        return rn;
    }

    public void setRn(int rn) {
        this.rn = rn;
    }

    public int getChid() {
        return chid;
    }

    public void setChid(int chid) {
        this.chid = chid;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", e=" + e +
                ", role=" + role +
                ", cluster=" + cluster +
                ", cond=" + cond +
                ", rop=" + rop +
                ", rleft=" + rleft +
                ", dtch=" + dtch +
                ", dts=" + dts +
                ", tel=" + tel +
                ", rn=" + rn +
                ", chid=" + chid +
                '}';
    }

    public void onInsertNodeInDatabase(int iRoundID) {
        Db db = new Db();
        Connection con = db.getCon();
        ResultSet rs = db.getRs();
        PreparedStatement pstmt;
        String query = "insert into iNodes(nodeID," +
                "iRoundID," +
                "xc," +
                "yc," +
                "e," +
                "noderole," +
                "cluster," +
                "cond," +
                "rop," +
                "rleft," +
                "dtch," +
                "dts," +
                "tel," +
                "rn," +
                "chid" +
                ")values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            pstmt = (PreparedStatement) con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, this.getId());
            pstmt.setInt(2, iRoundID);
            pstmt.setDouble(3, this.getX());
            pstmt.setDouble(4, this.getY());
            pstmt.setDouble(5, this.getE());
            pstmt.setInt(6, this.getRole());
            pstmt.setInt(7, this.getCluster());
            pstmt.setInt(8, this.getCond());
            pstmt.setInt(9, this.getRop());
            pstmt.setDouble(10, this.getRleft());
            pstmt.setDouble(11, this.getDtch());
            pstmt.setDouble(12, this.getDts());
            pstmt.setInt(13, this.getTel());
            pstmt.setInt(14, this.getRn());
            pstmt.setInt(15, this.getChid());
            pstmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
