package geneticAlgorithm;

import network.Node;

import java.util.ArrayList;
import java.util.Collections;

public class Genetic {
    public ArrayList<Population> createPopulation(int size, int nbPop, double t) {
        ArrayList<Population> populations = new ArrayList<Population>();
        for (int i = 0; i < size; i++) {
            Population population = new Population();
            population.init(nbPop, t);
            populations.add(population);
        }
        return populations;
    }

    public ArrayList<Fit> fitness(int size, int nbPop, ArrayList<Population> populations, ArrayList<Node> nodes, double sinkx, double sinky) {
        ArrayList<Fit> fits = new ArrayList<Fit>();
        for (int i = 0; i < size; i++) {
            double energy = 0;
            double dIC = 0;
            double nbCHs = 0;
            double dBC = 0;
            for (int j = 0; j < nbPop; j++) {
                // if (populations.get(i).pop.get(j) == 1){
                energy = energy + nodes.get(j).getE();
                nbCHs = nbCHs + 1;
                dBC = dBC + Math.sqrt(Math.pow((nodes.get(j).getX() - sinkx), 2) + Math.pow((nodes.get(j).getY() - sinky), 2));
                //}

            }

            fits.add(new Fit(i, energy / 100));
            //+(nbPop - nbCHs) +dBC/nbPop)
        }

        Collections.sort(fits);
        return fits;
    }

    public ArrayList<Individual> selection(int size, ArrayList<Population> populations, ArrayList<Fit> fits) {
        ArrayList<Individual> individuals = new ArrayList<Individual>();
        for (int i = 0; i < size; i++) {
            Population population = populations.get(fits.get(i).getIndex());
            individuals.add(new Individual(population.getPop()));
        }
        return individuals;
    }

    public ArrayList<Offspring> crossover(int n, ArrayList<Individual> individuals) {
        ArrayList<Offspring> offsprings = new ArrayList<Offspring>();

        for (int i = 0; i < individuals.size(); i++) {
            for (int j = i + 1; j < individuals.size(); j++) {
                Offspring offspring = new Offspring();
                offspring.init(individuals.get(i).getIndividual(), individuals.get(j).getIndividual(), n);
                offsprings.add(offspring);
            }
        }
        return offsprings;
    }

    public ArrayList<Offspring> mutation(ArrayList<Offspring> offsprings) {
        int upper = offsprings.get(0).getOffspring1().size();
        int lower = 0;
        int ge = (int) (Math.random() * (upper - lower) + lower);
        System.out.println("ge" + ge);
        for (int i = 0; i < offsprings.size(); i++) {
            Offspring offspring = offsprings.get(i);


            if (offspring.getOffspring1().get(ge) == 1) {
                offspring.getOffspring1().remove(ge);
                offspring.getOffspring1().add(ge, 0);
            } else {
                offspring.getOffspring1().remove(ge);
                offspring.getOffspring1().add(ge, 1);
            }
            if (offspring.getOffspring2().get(ge) == 1) {
                offspring.getOffspring2().remove(ge);
                offspring.getOffspring2().add(ge, 0);
            } else {
                offspring.getOffspring2().remove(ge);
                offspring.getOffspring2().add(ge, 1);
            }
        }
        return offsprings;
    }

    public void updatePopulation(ArrayList<Offspring> offsprings, ArrayList<Population> populations) {
        populations.clear();
        for (int i = 0; i < offsprings.size(); i++) {
            Offspring offspring = offsprings.get(i);
            Population pop1 = new Population(offspring.getOffspring1());
            Population pop2 = new Population(offspring.getOffspring2());
            populations.add(pop1);
            populations.add(pop2);
        }

    }
}
