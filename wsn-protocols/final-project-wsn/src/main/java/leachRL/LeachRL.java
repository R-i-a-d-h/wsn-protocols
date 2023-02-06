package leachRL;

import runMain.LeachRLParameters;
import simulation.RRound;
import simulation.RSimulation;

import java.util.ArrayList;
import java.util.Random;

public class LeachRL {
    public double xm;
    public double ym;
    public double sinkx;
    public double sinky;
    public double eo;
    public double Eelec;
    public double Eamp;
    public double EDA;
    public int k;
    public int r;
    LeachRLParameters parameters;
    ArrayList<NodeRL> chs = new ArrayList<NodeRL>();
    ArrayList<NodeRL> nodes = new ArrayList<NodeRL>();
    ArrayList<Point> points = new ArrayList<>();
    int dead_nodes;
    int operating_nodes;
    int n;

    public LeachRL(LeachRLParameters parameters) {
        this.parameters = parameters;
        System.out.println(parameters.toString());
        dead_nodes = 0;
        operating_nodes = parameters.getN();
        n = parameters.getN();
        xm = parameters.getXm();
        ym = parameters.getYm();
        eo = parameters.getEo();
        Eamp = parameters.getEamp();
        EDA = parameters.getEda();
        Eelec = parameters.getEelec();
        k = parameters.getK();
        r = parameters.getR();
        sinky = parameters.getSinky();
        sinkx = parameters.getSinkx();

    }

    void initNetWork() {
        for (int i = 0; i < n; i++) {
            double xc = Math.round(Math.random() * xm);
            double yc = Math.round(Math.random() * ym);
            double d = Math.sqrt(Math.pow(xc - sinkx, 2) + Math.pow(yc - sinky, 2));
            NodeRL node = new NodeRL(
                    //int id, double x, double y, double e, int role, int cluster, int cond, int rop, double rleft, double dtch, double dts, int tel, int rn, int chid) {
                    i,
                    (xc),
                    (yc),
                    eo,
                    d
            );
            nodes.add(node);
        }
        Point midpoint = new Point(xm / 2, ym / 2);
        AreaDiv areadiv = new AreaDiv();
        points.addAll(areadiv.onDiv(xm, ym, r, midpoint));


        for (int i = 0; i < nodes.size(); i++) {
            double mindCenter = Math.sqrt(Math.pow((points.get(0).getX() - nodes.get(i).getX()), 2)
                    + Math.pow(points.get(0).getY() - nodes.get(i).getY(), 2));
            int index = 0;
            for (int j = 0; j < points.size(); j++) {
                double dCenter = Math.sqrt(Math.pow((points.get(j).getX() - nodes.get(i).getX()), 2)
                        + Math.pow(points.get(j).getY() - nodes.get(i).getY(), 2));

                if (dCenter < mindCenter) {
                    mindCenter = dCenter;
                    index = j;
                    nodes.get(i).setdCenter(mindCenter);
                    nodes.get(i).setRid(index);
                }
            }
        }

    }

    void steadyStatePhase(double energy) {

        for (int i = 0; i < n; i++) {
            NodeRL node = nodes.get(i);
            if (node.getRole() == 0 && node.getCond() == 1 && chs.size() > 0) {
                if (node.getE() > 0) {
                    double ETx = Eelec * k + Eamp * k * Math.pow(node.getDch(), 2);
                    node.setE((node.getE() - ETx));
                    //System.out.println(" E : "+(node.getE()));
                    energy = energy + ETx;
                    NodeRL nodeCH = nodes.get(node.getChid());
                    if (nodeCH.getE() > 0 && nodeCH.getCond() == 1 && nodeCH.getRole() > 0) {
                        double ERx = (Eelec + EDA) * k;
                        energy = energy + ERx;
                        nodeCH.setE(nodeCH.getE() - ERx);
                        if (nodeCH.getE() <= 0) {// if cluster heads energy depletes with reception
                            nodeCH.setCond(0);

                            dead_nodes = dead_nodes + 1;
                            operating_nodes = operating_nodes - 1;
                        }
                    }
                }
                if (node.getE() <= 0) {
                    node.setCond(0);
                    node.setChid(-1);

                    dead_nodes = dead_nodes + 1;
                    operating_nodes = operating_nodes - 1;
                }
            }
        }

        // Energy Dissipation for cluster head nodes //
        for (int i = 0; i < n; i++) {
            NodeRL node = nodes.get(i);
            if (node.getRole() == 1 && node.getCond() == 1) {
                if (node.getE() > 0) {
                    double ETx = (Eelec + EDA) * k + Eamp * k * Math.pow(node.getDbs(), 2);

                    System.out.println(" dist " + node.getDbs() + "  " + k);
                    node.setE(node.getE() - ETx);
                    energy = energy + ETx;
                    //System.out.println("ETX : "+ wsn.Eamp);

                }
                if (node.getE() <= 0) {
                    node.setCond(0);
                    dead_nodes = dead_nodes + 1;
                    operating_nodes = operating_nodes - 1;
                }
            }
        }

    }

    public RSimulation run() {

        initNetWork();
        int rnd = 0;
        int transmissions = 0;
        double energy = 0;
        RSimulation simulation = new RSimulation(points);
        //simulation.onInsertSimulationInDatabase();
        while (operating_nodes > 0) {
            for (int i = 0; i < nodes.size(); i++) {
                nodes.get(i).setQ(3 / 4 * nodes.get(i).getE() + 1 / 4 * 1 / (nodes.get(i).getDbs() + nodes.get(i).getdCenter()));
            }
            ArrayList<Index> indexes = new ArrayList<>();
            for (int i = 0; i < points.size(); i++) {
                double minQ = -1;
                int indexQ = -1;
                for (int j = 0; j < n; j++) {
                    if (nodes.get(j).getRid() == i && nodes.get(j).getCond() == 1 && nodes.get(j).getRwd() == 0) {

                        if (minQ == -1) {
                            minQ = nodes.get(j).getQ();
                            indexQ = j;
                        } else {
                            if (minQ < nodes.get(j).getQ()) {
                                minQ = nodes.get(j).getQ();
                                indexQ = j;
                            }


                        }

                    }

                }
                indexes.add(new Index(indexQ, i));


            }

            System.out.println(indexes.toString());
            for (int j = 0; j < n; j++) {
                nodes.get(j).setRole(0);
            }
            chs.clear();
            for (int j = 0; j < indexes.size(); j++) {
                int index = indexes.get(j).getIndex();
                //System.out.println(index+"<- X ");
                if (index != -1) {
                    nodes.get(index).setRole(1);
                    chs.add(nodes.get(index));
                }
            }
            for (int i = 0; i < chs.size(); i++) {
                for (int j = 0; j < n; j++) {
                    if (chs.get(i).getRid() == nodes.get(j).getRid()) {
                        double d = Math.sqrt(Math.pow((chs.get(i).getX() - nodes.get(j).getX()), 2) + Math.pow((chs.get(i).getY() - nodes.get(j).getY()), 2));
                        nodes.get(j).setDch(d);
                        nodes.get(j).setChid(chs.get(i).getId());

                    }
                }
            }
            for (int i = 0; i < n; i++) {
                if (nodes.get(i).getRwd() != 0) {
                    nodes.get(i).setRwd(nodes.get(i).getRwd() - 1);
                }

            }
            for (int i = 0; i < chs.size(); i++) {
                int r = 0;
                for (int j = 0; j < n; j++) {
                    if (chs.get(i).getRid() == nodes.get(j).getRid() && nodes.get(j).getCond() == 1 && nodes.get(j).getRwd() == 0) {
                        r++;
                    }
                }
                Random rn = new Random();
                System.out.println("bound : " + r);
                int rd = 0;
                if ((r - 1) != 0) {
                    rd = rn.nextInt(r - 1);
                }
                chs.get(i).setRwd(rd);

            }
            steadyStatePhase(energy);
            rnd++;
            if (chs.size() > 0) {
                transmissions = transmissions + 1;
                RRound iround = new RRound();
                iround.setNodes(nodes);
                iround.setNbCH(chs.size());
                iround.setNbRound(transmissions);
                simulation.getiRounds().add(iround);
            }
            System.out.println("Rnd : " + rnd);
            System.out.println("Chs : " + chs.size());
            System.out.println("Transmissions : " + transmissions);
            System.out.println("wsn.dead_nodes : " + dead_nodes);
            if (rnd == 9999) {
                break;
            }

            if (rnd == -1) {
                for (NodeRL a : nodes) {
                    System.out.println(a.getE());
                }
            }

        }
        return simulation;

    }

}