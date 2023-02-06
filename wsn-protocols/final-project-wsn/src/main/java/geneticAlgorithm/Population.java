package geneticAlgorithm;

import java.util.ArrayList;

public class Population {
    ArrayList<Integer> pop = new ArrayList<>();

    public Population() {
    }

    public Population(ArrayList<Integer> pop) {
        this.pop = pop;
    }

    public void init(int size, double t) {
        boolean b = false;
        for (int i = 0; i < size; i++) {
            double ge = Math.random();
            if (ge <= t && b == false) {
                pop.add(1);
                b = true;
            } else {
                pop.add(0);
            }
        }

    }

    public ArrayList<Integer> getPop() {
        return pop;
    }

    public void setPop(ArrayList<Integer> pop) {
        this.pop = pop;
    }

    @Override
    public String toString() {
        return "Population{" +
                "pop=" + pop +
                '}';
    }
}
