package leachRL;

public class NodeRL implements Comparable<NodeRL> {
    private int id;//sensor's ID number
    private double x;//X-axis coordinates of sensor node
    private double y;//Y-axis coordinates of sensor node
    private double e;//nodes energy levels (initially set to be equal to "Eo")
    private double q;
    private int role;
    private int rid;//node acts as normal if the value is '0', if elected as a cluster head it  gets the value '1' (initially all nodes are normal)
    private double dbs;//the cluster which a node belongs to
    private int cond;//States the current condition of the node. when the node is operational its value is =1 and when dead =0
    private int chid;//number of rounds node was operational
    private double dch;//nodes distance from the sink
    private double dCenter;
    private int rwd;//round node got elected as cluster head


    public NodeRL(int id, double x, double y, double e, double dist) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.e = e;
        this.cond = 1;
        this.dbs = dist;
        this.rwd = 0;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public double getdCenter() {
        return dCenter;
    }

    public void setdCenter(double dCenter) {
        this.dCenter = dCenter;
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

    public double getQ() {
        return q;
    }

    public void setQ(double q) {
        this.q = q;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public double getDbs() {
        return dbs;
    }

    public void setDbs(double dbs) {
        this.dbs = dbs;
    }

    public int getCond() {
        return cond;
    }

    public void setCond(int cond) {
        this.cond = cond;
    }

    public int getChid() {
        return chid;
    }

    public void setChid(int chid) {
        this.chid = chid;
    }

    public double getDch() {
        return dch;
    }

    public void setDch(double dch) {
        this.dch = dch;
    }

    public int getRwd() {
        return rwd;
    }

    public void setRwd(int rwd) {
        this.rwd = rwd;
    }

    @Override
    public int compareTo(NodeRL nodeRL) {

        return (int) (nodeRL.getDbs() - this.getDbs());
    }

    @Override
    public String toString() {
        return "NodeRL{" +

                ", dbs=" + dbs +

                '}';
    }
}
