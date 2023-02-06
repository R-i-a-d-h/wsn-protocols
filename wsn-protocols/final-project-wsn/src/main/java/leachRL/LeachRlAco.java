package leachRL;

import runMain.LeachRLParameters;
import simulation.RRound;
import simulation.RSimulation;

import java.util.ArrayList;
import java.util.Random;

import static java.util.Collections.sort;

public class LeachRlAco {
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
    AntColonyOptimizationParameters antColonyOptimizationParameters;

    public LeachRlAco(LeachRLParameters parameters, AntColonyOptimizationParameters antColonyOptimizationParameters) {
        this.parameters = parameters;
        this.antColonyOptimizationParameters = antColonyOptimizationParameters;
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
        for (int i = 0; i < n; i++) {
            NodeRL node = nodes.get(i);
            if (node.getRole() == 1 && node.getCond() == 1 && node.getChid() != -2) {
                if (node.getE() > 0) {
                    double ETx = (Eelec + EDA) * k + Eamp * k * Math.pow(node.getDbs(), 2);
                    node.setE(node.getE() - ETx);
                    energy = energy + ETx;
                    if (nodes.get(nodes.get(i).getChid()).getE() > 0 && nodes.get(nodes.get(i).getChid()).getCond() == 1 && nodes.get(nodes.get(i).getChid()).getRole() == 1) {
                        double ERx = (Eelec + EDA) * k;
                        energy = energy + ERx;
                        nodes.get(nodes.get(i).getChid()).setE(nodes.get(nodes.get(i).getChid()).getE() - ERx);
                        if (nodes.get(nodes.get(i).getChid()).getE() <= 0) {//if cluster heads energy depletes with reception
                            nodes.get(nodes.get(i).getChid()).setCond(0);
                            //nodes[nodes[i].child].rop=rnd;
                            dead_nodes = dead_nodes + 1;
                            operating_nodes = operating_nodes - 1;

                        }

                    }
                }
                if (node.getE() <= 0) {
                    node.setCond(0);
                    dead_nodes = dead_nodes + 1;
                    operating_nodes = operating_nodes - 1;
                }
            } else if (node.getRole() == 1 && node.getCond() == 1 && node.getChid() == -2) {
                if (node.getE() > 0) {
                    double ETx = (Eelec + EDA) * k + Eamp * k * Math.pow(node.getDbs(), 2);
                    node.setE(node.getE() - ETx);
                    energy = energy + ETx;
                }
                if (node.getE() <= 0) {
                    dead_nodes = dead_nodes + 1;
                    operating_nodes = operating_nodes - 1;
                    node.setCond(0);
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

            sort(chs);

            ArrayList<City> cities = new ArrayList<>();
            for (int i = 0; i < chs.size(); i++) {
                City city = new City(chs.get(i).getId(), chs.get(i).getX(), chs.get(i).getY(), chs.get(i).getE(), i);
                cities.add(city);
            }
            City city = new City(-1, sinkx, sinky, 0, cities.size());
            cities.add(city);
            ArrayList<ArrayList<Integer>> paths = new ArrayList();
            for (int kk = 0; kk < cities.size() - 1; kk++) {
                if (cities.get(kk).getcType() == 0) {
                    int numberOfCities = chs.size() + 1;
                    double[][] graph = new double[numberOfCities][numberOfCities];
                    for (int i = 0; i < numberOfCities; i++) {
                        for (int j = 0; j < numberOfCities; j++) {
                            double x1 = cities.get(i).getX();
                            double x2 = cities.get(j).getX();
                            double y1 = cities.get(i).getY();
                            double y2 = cities.get(j).getY();
                            double d = Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
                            double en = cities.get(i).getE();
                            if (d < 30) {
                                graph[i][j] = d + en;
                            }

                            if (i == numberOfCities - 1) {
                                graph[i][j] = 0;
                            }

                        }
                    }
                    for (int i = 0; i < numberOfCities; i++) {
                        graph[i][kk] = 0;
                    }
                    AntColonyOptimization a = new AntColonyOptimization(antColonyOptimizationParameters.getC()
                            , antColonyOptimizationParameters.getAlpha()
                            , antColonyOptimizationParameters.getBeta()
                            , antColonyOptimizationParameters.getEvaporation()
                            , antColonyOptimizationParameters.getQ()
                            , antColonyOptimizationParameters.getAntFactor()
                            , antColonyOptimizationParameters.getRandomFactor()
                            , antColonyOptimizationParameters.getMaxIterations()
                            , numberOfCities
                            , graph
                            , kk
                            , numberOfCities - 1
                    );

                    a.startAntOptimization();
                    ArrayList<Integer> best = a.bestTourOrder;
                    if (best.size() == 0) {
                        ArrayList<Integer> way = new ArrayList<Integer>();
                        way.add(kk);
                        way.add(numberOfCities - 1);
                        paths.add(way);
                    } else {
                        for (int kkk = 0; kkk < best.size() - 1; kkk++) {
                            cities.get(best.get(kkk)).setcType(1);
                        }


                        paths.add(best);

                    }

                    //System.out.println(" Best Tour Order : "+a.bestTourOrder.toString()+ " Best Tour Length : "+ a.bestTourLength);
                }
            }
            System.out.println(paths.toString());
            for (int i = 0; i < paths.size(); i++) {
                int st = paths.get(i).size();
                for (int j = 0; j < paths.get(i).size() - 1; j++) {
                    if (j + 1 == (st - 1)) {
                        chs.get(paths.get(i).get(j)).setChid(-2);
                    } else {
                        System.out.println(paths.get(i).get(j + 1));
                        chs.get(paths.get(i).get(j)).setChid(chs.get(paths.get(i).get(j + 1)).getId());
                    }


                }
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

        }
        return simulation;

    }


}
