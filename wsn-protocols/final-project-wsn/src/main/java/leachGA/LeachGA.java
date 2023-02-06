package leachGA;

import geneticAlgorithm.*;
import network.Node;
import network.WSN;
import simulation.IRound;
import simulation.Simulation;

import java.util.ArrayList;

public class LeachGA {

    WSN wsn = new WSN();
    ArrayList<Node> nodes = new ArrayList<Node>();
    ArrayList<Node> chs = new ArrayList<Node>();


    // Creation of the Wireless Sensor Network
    void initNetWork() {
        for (int i = 0; i < wsn.n; i++) {
            double xc = Math.round(Math.random() * wsn.xm);
            double yc = Math.round(Math.random() * wsn.ym);
            double d = Math.sqrt(Math.pow(xc - wsn.sinkx, 2) + Math.pow(yc - wsn.sinky, 2));
            System.out.println(xc);
            Node node = new Node(
                    //int id, double x, double y, double e, int role, int cluster, int cond, int rop, double rleft, double dtch, double dts, int tel, int rn, int chid) {
                    i,
                    xc,
                    yc,
                    wsn.eo,
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

    void setUpPhase(Genetic genetic, ArrayList<Population> populations) {
        ArrayList<Fit> fits = genetic.fitness(6, wsn.n, populations, nodes, wsn.sinkx, wsn.sinky);
        ArrayList<Individual> individuals = genetic.selection(3, populations, fits);
        ArrayList<Offspring> offsprings = genetic.crossover(wsn.n, individuals);
        offsprings = genetic.mutation(offsprings);
        genetic.updatePopulation(offsprings, populations);
        Population pop = populations.get(fits.get(0).getIndex());
        System.out.println(fits.get(0));
        System.out.println(pop.getPop());

        for (int i = 0; i < wsn.n; i++) {
            int role = pop.getPop().get(i);
            nodes.get(i).setRole(role);
            if (role == 1 && nodes.get(i).getCond() == 1) {
                chs.add(nodes.get(i));
            }
            //System.out.print(nodes.get(i).getRole() +" , ");

        }


        for (int i = 0; i < wsn.n; i++) {
            Node node = nodes.get(i);
            if (node.getRole() == 0 && node.getE() > 0 && chs.size() > 0) {
                double[] mindist = {Math.sqrt(Math.pow((node.getX() - chs.get(0).getX()), 2) + Math.pow((node.getY() - chs.get(0).getY()), 2)), 0};
                for (int j = 0; j < chs.size(); j++) {
                    double[] dist = {Math.sqrt(Math.pow((node.getX() - chs.get(j).getX()), 2) + Math.pow((node.getY() - chs.get(j).getY()), 2)), j};
                    // System.out.println("min "+mindist[0]+"dist"+dist[0]);
                    if (dist[0] < mindist[0]) {
                        mindist = dist;
                    }
                }
                // System.out.println("min "+mindist[0]);

                node.setChid(chs.get((int) mindist[1]).getId());
                node.setDtch(mindist[1]);

            }

        }


    }

    void steadyStatePhase(double energy, int rnd) {

        for (int i = 0; i < wsn.n; i++) {
            Node node = nodes.get(i);
            if (node.getRole() == 0 && node.getCond() == 1 && chs.size() > 0) {
                if (node.getE() > 0) {
                    double ETx = wsn.Eelec * wsn.k + wsn.Eamp * wsn.k * Math.pow(node.getDtch(), 2);

                    //   System.out.println("ETX : "+ETx+"    E :"+node.getE());
                    //  System.out.println("ETX - E : "+(node.getE() - ETx));
                    node.setE((node.getE() - ETx));
                    // System.out.println(" E : "+(node.getE()));
                    energy = energy + ETx;
                    Node nodeCH = nodes.get(node.getChid());
                    if (nodeCH.getE() > 0 && nodeCH.getCond() == 1 && nodeCH.getRole() > 0) {
                        double ERx = (wsn.Eelec + wsn.EDA) * wsn.k;
                        energy = energy + ERx;
                        nodeCH.setE(nodeCH.getE() - ERx);
                        if (nodeCH.getE() <= 0) {// if cluster heads energy depletes with reception
                            nodeCH.setCond(0);
                            nodeCH.setRop(rnd);
                            wsn.dead_nodes = wsn.dead_nodes + 1;
                            wsn.operating_nodes = wsn.operating_nodes - 1;
                        }
                    }
                }
                if (node.getE() <= 0) {
                    node.setCond(0);
                    node.setChid(-1);
                    node.setRop(rnd);
                    wsn.dead_nodes = wsn.dead_nodes + 1;
                    wsn.operating_nodes = wsn.operating_nodes - 1;
                }
            }
        }

        // Energy Dissipation for cluster head nodes //
        for (int i = 0; i < wsn.n; i++) {
            Node node = nodes.get(i);
            if (node.getRole() == 1 && node.getCond() == 1) {
                if (node.getE() > 0) {
                    double ETx = (wsn.Eelec + wsn.EDA) * wsn.k + wsn.Eamp * wsn.k * Math.pow(node.getDts(), 2);
                    node.setE(node.getE() - ETx);
                    energy = energy + ETx;
                }
                if (node.getE() <= 0) {
                    node.setCond(0);
                    node.setRop(rnd);
                    wsn.dead_nodes = wsn.dead_nodes + 1;
                    wsn.operating_nodes = wsn.operating_nodes - 1;
                }
            }
        }


    }

    public Simulation run() {

        initNetWork();
        int rnd = 0;
        int transmissions = 0;
        double t = 0.1;
        Genetic genetic = new Genetic();
        ArrayList<Population> populations = genetic.createPopulation(6, wsn.n, t);
        double energy = 0;
        Simulation simulation = new Simulation();
        while (wsn.operating_nodes > 0) {
            chs.clear();
            System.out.println("-------------------------------------------------------------");
            double ee = 0;
            for (Node node : nodes) {
                ee = ee + node.getE();
            }
            System.out.println("Emoy : " + (ee / wsn.n));


            setUpPhase(genetic, populations);
            steadyStatePhase(energy, rnd);

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
            System.out.println("wsn.dead_nodes : " + wsn.dead_nodes);
            if (rnd == -3) {
                break;
            }


        }

//            for (Node node :nodes){
//                System.out.println(node.toString());
//
//            }
        return simulation;
    }


}
