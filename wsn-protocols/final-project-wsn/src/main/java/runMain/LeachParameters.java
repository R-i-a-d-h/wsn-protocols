package runMain;

public class LeachParameters {

    private int k;
    private int n;
    private Double sinkx;
    private Double sinky;
    private Double xm;
    private Double ym;
    private Double p;
    private Double eo;
    private Double eda;
    private Double eamp;
    private Double eelec;
    private long sleep;

    public LeachParameters(int k, int n, Double sinkx, Double sinky, Double xm, Double ym, Double p, Double eo, Double eda, Double eamp, Double eelec, long sleep) {
        this.k = k;
        this.n = n;
        this.sinkx = sinkx;
        this.sinky = sinky;
        this.xm = xm;
        this.ym = ym;
        this.p = p;
        this.eo = eo;
        this.eda = eda;
        this.eamp = eamp;
        this.eelec = eelec;
        this.sleep = sleep;
    }

    public long getSleep() {
        return sleep;
    }

    public void setSleep(long sleep) {
        this.sleep = sleep;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public Double getSinkx() {
        return sinkx;
    }

    public void setSinkx(Double sinkx) {
        this.sinkx = sinkx;
    }

    public Double getSinky() {
        return sinky;
    }

    public void setSinky(Double sinky) {
        this.sinky = sinky;
    }

    public Double getXm() {
        return xm;
    }

    public void setXm(Double xm) {
        this.xm = xm;
    }

    public Double getYm() {
        return ym;
    }

    public void setYm(Double ym) {
        this.ym = ym;
    }

    public Double getP() {
        return p;
    }

    public void setP(Double p) {
        this.p = p;
    }

    public Double getEo() {
        return eo;
    }

    public void setEo(Double eo) {
        this.eo = eo;
    }

    public Double getEda() {
        return eda;
    }

    public void setEda(Double eda) {
        this.eda = eda;
    }

    public Double getEamp() {
        return eamp;
    }

    public void setEamp(Double eamp) {
        this.eamp = eamp;
    }

    public Double getEelec() {
        return eelec;
    }

    public void setEelec(Double eelec) {
        this.eelec = eelec;
    }

}
