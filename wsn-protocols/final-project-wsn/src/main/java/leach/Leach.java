package leach;

import network.Node;
import runMain.LeachParameters;
import simulation.IRound;
import simulation.Simulation;

import java.util.ArrayList;

public class Leach {
    public double xm;
    public double ym;
    public double sinkx;
    public double sinky;
    public double eo;
    public double Eelec;
    public double Eamp;
    public double EDA;
    public int k;
    public double p;
    ArrayList<Node> nodes = new ArrayList<Node>();
    ArrayList<Node> chs = new ArrayList<Node>();
    int dead_nodes;
    int operating_nodes;
    int n;
    LeachParameters leachParameters;
    double energy = 0;
    int rnd = 0;
    int transmissions = 0;

    public Leach(LeachParameters leachParameters) {
        this.leachParameters = leachParameters;
        dead_nodes = 0;
        operating_nodes = leachParameters.getN();
        n = leachParameters.getN();
        xm = leachParameters.getXm();
        ym = leachParameters.getYm();
        eo = leachParameters.getEo();
        Eamp = leachParameters.getEamp();
        EDA = leachParameters.getEda();
        Eelec = leachParameters.getEelec();
        k = leachParameters.getK();
        p = leachParameters.getP();
        sinky = leachParameters.getSinky();
        sinkx = leachParameters.getSinkx();

    }

    // Creation of the Wireless Sensor Network
    void initNetWork() {
        for (int i = 0; i < n; i++) {
            double xc = Math.round(Math.random() * xm);
            double yc = Math.round(Math.random() * ym);
            double d = Math.sqrt(Math.pow(xc - sinkx, 2) + Math.pow(yc - sinky, 2));
            Node node = new Node(
                    //int id, double x, double y, double e, int role, int cluster, int cond, int rop, double rleft, double dtch, double dts, int tel, int rn, int chid) {
                    i,
                    (xc),
                    (yc),
                    eo,
                    0,
                    0,
                    1,
                    0,
                    0,
                    0,
                    d,
                    0,
                    0,
                    -1);
            nodes.add(node);
        }
    }

    void setUpPhase(double t, double tleft) {
        for (int i = 0; i < n; i++) {
            nodes.get(i).setCluster(0);  //% reseting cluster in which the node belongs to
            nodes.get(i).setRole(0);      //% reseting node role
            nodes.get(i).setChid(-1);       //% reseting cluster head id
            if (nodes.get(i).getRleft() > 0) {
                nodes.get(i).setRleft(nodes.get(i).getRleft() - 1);
            }
            if (nodes.get(i).getE() > 0 && nodes.get(i).getRleft() == 0) {
                double generate = ((Math.random() * (1)) + 0);

                if (generate < t) {
                    nodes.get(i).setRole(1);// assigns the node role of acluster head
                    nodes.get(i).setRn(rnd);// Assigns the round that the cluster head was elected to the data table
                    nodes.get(i).setTel(nodes.get(i).getTel() + 1);
                    nodes.get(i).setRleft(1 / p - tleft);//rounds for which the node will be unable to become a CH
                    nodes.get(i).setDts(Math.sqrt(Math.pow((sinkx - nodes.get(i).getX()), 2) + (Math.pow((sinky - nodes.get(i).getY()), 2))));//calculates the distance between the sink and the cluster hea
                    chs.add(nodes.get(i));

                }
            }
        }
        for (int i = 0; i < n; i++) {
            Node node = nodes.get(i);
            if (node.getRole() == 0 && node.getE() > 0 && chs.size() > 0) {
                double[] mindist = {Math.sqrt(Math.pow((node.getX() - chs.get(0).getX()), 2) + Math.pow((node.getY() - chs.get(0).getY()), 2)), 0};
                for (int j = 1; j < chs.size(); j++) {
                    double[] dist = {Math.sqrt(Math.pow((node.getX() - chs.get(j).getX()), 2) + Math.pow((node.getY() - chs.get(j).getY()), 2)), j};
                    if (dist[0] < mindist[0]) {
                        mindist = dist;
                    }
                }

                node.setChid(chs.get((int) mindist[1]).getId());
                node.setDtch(mindist[1]);

            }

        }
    }

    void steadyStatePhase() {
        for (int i = 0; i < n; i++) {
            Node node = nodes.get(i);
            if (node.getRole() == 0 && node.getCond() == 1 && chs.size() > 0) {
                if (node.getE() > 0) {
                    double ETx = Eelec * k + Eamp * k * Math.pow(node.getDtch(), 2);
                    node.setE((node.getE() - ETx));
                    energy = energy + ETx;
                    Node nodeCH = nodes.get(node.getChid());
                    if (nodeCH.getE() > 0 && nodeCH.getCond() == 1 && nodeCH.getRole() > 0) {
                        double ERx = (Eelec + EDA) * k;
                        energy = energy + ERx;
                        nodeCH.setE(nodeCH.getE() - ERx);
                        if (nodeCH.getE() <= 0) {// if cluster heads energy depletes with reception
                            nodeCH.setCond(0);
                            nodeCH.setRop(rnd);
                            dead_nodes = dead_nodes + 1;
                            operating_nodes = operating_nodes - 1;
                        }
                    }
                }
                if (node.getE() <= 0) {
                    node.setCond(0);
                    node.setChid(-1);
                    node.setRop(rnd);
                    dead_nodes = dead_nodes + 1;
                    operating_nodes = operating_nodes - 1;
                }
            }
        }
        // Energy Dissipation for cluster head nodes //
        for (int i = 0; i < n; i++) {
            Node node = nodes.get(i);
            if (node.getRole() == 1 && node.getCond() == 1) {
                if (node.getE() > 0) {
                    double ETx = (Eelec + EDA) * k + Eamp * k * Math.pow(node.getDts(), 2);
                    node.setE(node.getE() - ETx);
                    energy = energy + ETx;
                }
                if (node.getE() <= 0) {
                    node.setCond(0);
                    node.setRop(rnd);
                    dead_nodes = dead_nodes + 1;
                    operating_nodes = operating_nodes - 1;
                }
            }
        }


    }

    public Simulation run() {
        initNetWork();
        Simulation simulation = new Simulation();
        // simulation.onInsertSimulationInDatabase();
        while (operating_nodes > 0) {
            double t = (p / (1 - p * (rnd % Math.round(1 / p))));
            double tleft = rnd % Math.round(1 / p);
            chs.clear();
            System.out.println("-------------------------------------------------------------");
            setUpPhase(t, tleft);
            steadyStatePhase();
            rnd++;
            if (chs.size() > 0) {
                transmissions = transmissions + 1;
                IRound iround = new IRound();
                iround.setNodes(nodes);
                iround.setNbCH(chs.size());
                iround.setNbRound(transmissions);
                simulation.getiRounds().add(iround);
            }
            System.out.println("rnd : " + rnd);
            System.out.println("chs : " + chs.size());
            System.out.println("transmissions : " + transmissions);
            System.out.println("wsn.dead_nodes : " + dead_nodes);
            if (rnd == -10) {
                break;
            }
        }

        System.out.println(energy + "<_ energy");
        return simulation;
    }


}
