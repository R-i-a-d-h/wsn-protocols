package leachRL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AntColonyOptimization {
    public String s = "";
    public ArrayList<Integer> bestTourOrder = new ArrayList<>();
    public double bestTourLength;
    int startIndex;
    int lastIndex;
    private double c;             //number of trails
    private double alpha;           //pheromone importance
    private double beta;            //distance priority
    private double evaporation;
    private double Q;             //pheromone left on trail per ant
    private double antFactor;     //no of ants per Node
    private double randomFactor; //introducing randomness
    private int maxIterations;
    private int numberOfCities;
    private int numberOfAnts;
    private double graph[][];
    private double trails[][];
    ;
    private List<Ant> ants = new ArrayList<>();
    private Random random = new Random();
    private double probabilities[];

    public AntColonyOptimization(double tr, double al, double be, double ev,
                                 double q, double af, double rf, int iter, int noOfCities, double[][] graph, int st, int la) {
        c = tr;
        alpha = al;
        beta = be;
        evaporation = ev;
        Q = q;
        antFactor = af;
        randomFactor = rf;
        maxIterations = iter;
        startIndex = st;
        lastIndex = la;

        this.graph = graph;
        numberOfCities = noOfCities;
        numberOfAnts = (int) (numberOfCities * antFactor);

        trails = new double[numberOfCities][numberOfCities];
        probabilities = new double[numberOfCities];


        for (int i = 0; i < numberOfAnts; i++)
            ants.add(new Ant(lastIndex, i));
        this.s += "INIT :";
        this.s += "\nGraph :" + Arrays.toString(this.graph);
        this.s += "\nNumber Of Cities :" + this.numberOfCities;
        this.s += "\nTrails :" + Arrays.toString(this.trails);
        this.s += "\nAnts : ";
        for (int i = 0; i < this.numberOfAnts; i++) {
            Ant ant = this.ants.get(i);
            this.s += "\nAnt : " + ant.getId() + " Trail : " + ant.getTrail().toString();
        }


        this.s += "\nNumber Of Ants :" + this.numberOfAnts;
        this.s += "\nProbabilities :" + Arrays.toString(this.probabilities);
        this.s += "\nEvaporation :" + this.evaporation;
        this.s += "\nStart Index :" + this.startIndex;
        this.s += "\nLast Index : " + this.lastIndex;
        this.s += "\nQ :" + this.Q;
        this.s += "\nAlpha :" + this.alpha;
        this.s += "\nBeta : " + this.beta;
        this.s += "\nNumber Of Iterations : " + this.maxIterations;
    }

    /**
     * Generate initial solution
     */


    public ArrayList<Integer> getBestTourOrder() {
        System.out.println("path" + bestTourOrder.toString());
        return bestTourOrder;
    }

    /**
     * Perform ant optimization
     *
     * @return
     */
    public void startAntOptimization() {

        for (int i = 1; i <= 1; i++) {
            s += ("\nAttempt #" + i);
            solve();
            s += "\n";
        }

    }

    public ArrayList<Integer> solve() {
        this.setupAnts();
        this.clearTrails();


        for (int i = 0; i < maxIterations; i++) {
            this.moveAnts();
            updateTrails();
            updateBest();
            s += ("\n---------------------------------------------------------------------------------------------------");
            s += ("\nIT : " + i + "\nBest tour length: " + bestTourLength);
            s += ("\nBest tour order: ");
            for (int t : bestTourOrder) {
                s += "[" + t + "]";

            }


            for (Ant ant : ants) {

                //System.out.println("Ant : "+ant.getId()+" Trail length : "+ant.trailLength(graph));
                ant.clear();

                ant.visitCity(startIndex);

            }

        }


        return bestTourOrder;
    }

    public void setupAnts() {
        this.s += "\nSetupAnts : ";
        for (Ant ant : ants) {
            ant.clear();
            ant.visitCity(startIndex);
            this.s += "\nAnt : " + ant.getId() + " Trail" + ant.getTrail().toString();
        }

    }

    private boolean antTest() {
        boolean t = false;

        for (Ant ant : ants) {

            if (ant.getCloseTrail() == 0) {
                return t;
            }


        }
        return true;

    }

    private void moveAnts() {

        while (!antTest()) {
            for (Ant ant : ants) {
                this.s += "\nAnt : " + ant.getId() + " Trail : " + ant.getTrail().toString();
                if (ant.getCloseTrail() == 0) {
                    int city = this.selectNextCity(ant);
                    if (city == -1) {
                        ant.setCloseTrail(1);
                    } else {
                        ant.visitCity(city);
                    }
                    this.s += "\nAnt : " + ant.getId() + " Trail : " + ant.getTrail().toString();
                    this.s += "\n.................................................................";
                }


            }

        }


    }

    private void clearTrails() {
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++)
                trails[i][j] = c;
        }
    }

    private int selectNextCity(Ant ant) {

        int t = random.nextInt(this.numberOfCities + 1);
        if (random.nextDouble() < this.randomFactor) {
            int cityIndex = -999;
            for (int i = 0; i < this.numberOfCities; i++) {
                if (i == t && ant.checkCity(i, this.graph) && !ant.visited(i)) {
                    cityIndex = i;
                    break;
                }
            }
            if (cityIndex != -999) {
                this.s += " use : The random way !";
                return cityIndex;
            }
        }


        int currentCity = ant.getTrail().get(ant.getTrail().size() - 1);
        double pheromone = 0.0;
        for (int l = 0; l < numberOfCities; l++) {
            if (graph[currentCity][l] != 0 && !ant.visited(l)) {
                pheromone += Math.pow(trails[currentCity][l], alpha) * Math.pow(1.0 / graph[currentCity][l], beta);
            }
        }
        for (int j = 0; j < numberOfCities; j++) {
            if (graph[currentCity][j] == 0 || ant.visited(j)) {
                probabilities[j] = 0.0;
            } else {
                double numerator = Math.pow(trails[currentCity][j], alpha) * Math.pow(1.0 / graph[currentCity][j], beta);
                probabilities[j] = numerator / pheromone;
            }
        }

        double r = random.nextDouble();
        double total = 0;
        for (int i = 0; i < numberOfCities; i++) {
            total += probabilities[i];
            if (total >= r) {
                return i;
            }
        }
        return -1;

    }

    private void updateTrails() {
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++)
                trails[i][j] *= evaporation;
        }
        for (Ant a : ants) {
            if (a.trailHealthy()) {
                double contribution = Q / a.trailLength(graph);
                for (int i = 0; i < a.getTrail().size() - 1; i++)
                    trails[a.getTrail().get(i)][a.getTrail().get(i + 1)] += contribution;
            }


        }
    }

    private void updateBest() {

        if (bestTourOrder.isEmpty()) {
            for (Ant ant : ants) {
                if (ant.trailHealthy()) {
                    bestTourOrder.clear();
                    bestTourOrder.addAll(ant.getTrail());
                    bestTourLength = ant.trailLength(graph);
                }

            }

        }

        for (Ant a : ants) {
            if (a.trailLength(graph) < bestTourLength) {
                bestTourLength = a.trailLength(graph);
                bestTourOrder.clear();
                bestTourOrder.addAll(a.getTrail());

            }
        }

        this.s += "\nBest Trail : " + this.bestTourOrder.toString();
        this.s += "\nBest Length : " + this.bestTourLength;
        this.s += "\nTrails : " + this.trails.toString();

    }
}
