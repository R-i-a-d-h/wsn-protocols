package geneticAlgorithm;

import java.util.ArrayList;

public class Individual {
    ArrayList<Integer> individual = new ArrayList<>();

    public Individual(ArrayList<Integer> individual) {
        this.individual = individual;
    }

    public ArrayList<Integer> getIndividual() {
        return individual;
    }

    public void setIndividual(ArrayList<Integer> individual) {
        this.individual = individual;
    }

    @Override
    public String toString() {
        return "Individual{" +
                "individual=" + individual +
                '}';
    }
}
