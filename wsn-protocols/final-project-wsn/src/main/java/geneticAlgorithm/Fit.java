package geneticAlgorithm;

public class Fit implements Comparable<Fit> {
    private int index;
    private double value;


    public Fit(int index, double value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Fit{" +
                "index=" + index +
                ", value=" + value +
                '}';
    }


    @Override
    public int compareTo(Fit o1) {
        return (this.getValue() > o1.getValue() ? -1 :
                (this.getValue() == o1.getValue() ? 0 : 1));
    }
}
