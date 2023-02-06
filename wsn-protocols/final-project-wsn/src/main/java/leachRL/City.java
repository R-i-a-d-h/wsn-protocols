package leachRL;

public class City {
    private int id;
    private double x;
    private double y;
    private double e;
    private int cType;
    private int eid;

    public City(int id, double x, double y, double e, int eid) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.e = e;
        this.cType = 0;
        this.eid = eid;
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

    public int getcType() {
        return cType;
    }

    public void setcType(int cType) {
        this.cType = cType;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }
}
