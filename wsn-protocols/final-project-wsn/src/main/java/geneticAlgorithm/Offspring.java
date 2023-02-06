package geneticAlgorithm;

import java.util.ArrayList;

public class Offspring {
    private ArrayList<Integer> offspring1 = new ArrayList<>();
    private ArrayList<Integer> offspring2 = new ArrayList<>();

    public Offspring() {
    }

    public ArrayList<Integer> getOffspring1() {
        return offspring1;
    }

    public void setOffspring1(ArrayList<Integer> offspring1) {
        this.offspring1 = offspring1;
    }

    public ArrayList<Integer> getOffspring2() {
        return offspring2;
    }

    public void setOffspring2(ArrayList<Integer> offspring2) {
        this.offspring2 = offspring2;
    }

    public void init(ArrayList<Integer> individual1, ArrayList<Integer> individual2, int size) {
        for (int i = 0; i < size; i++) {
            if (i >= (size / 2)) {
                offspring1.add(individual1.get(i));
                offspring2.add(individual2.get(i));

            } else {
                offspring1.add(individual2.get(i));
                offspring2.add(individual1.get(i));

            }
        }

    }

    @Override
    public String toString() {
        return "Offspring{" +
                "offspring1=" + offspring1 + "\n" +
                ", offspring2=" + offspring2 +
                '}';
    }
}
