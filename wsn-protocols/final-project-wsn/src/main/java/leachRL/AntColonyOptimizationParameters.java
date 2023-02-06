package leachRL;

public class AntColonyOptimizationParameters {
    private double c;             //number of trails
    private double alpha;           //pheromone importance
    private double beta;            //distance priority
    private double evaporation;
    private double Q;             //pheromone left on trail per ant
    private double antFactor;     //no of ants per Node
    private double randomFactor; //introducing randomness
    private int maxIterations;

    public AntColonyOptimizationParameters(double c, double alpha, double beta, double evaporation, double q, double antFactor, double randomFactor, int maxIterations) {
        this.c = c;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporation = evaporation;
        Q = q;
        this.antFactor = antFactor;
        this.randomFactor = randomFactor;
        this.maxIterations = maxIterations;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getEvaporation() {
        return evaporation;
    }

    public void setEvaporation(double evaporation) {
        this.evaporation = evaporation;
    }

    public double getQ() {
        return Q;
    }

    public void setQ(double q) {
        Q = q;
    }

    public double getAntFactor() {
        return antFactor;
    }

    public void setAntFactor(double antFactor) {
        this.antFactor = antFactor;
    }

    public double getRandomFactor() {
        return randomFactor;
    }

    public void setRandomFactor(double randomFactor) {
        this.randomFactor = randomFactor;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }
}
