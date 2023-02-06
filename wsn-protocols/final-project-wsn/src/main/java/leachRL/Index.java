package leachRL;

public class Index {
    int index;
    int qValue;

    public Index(int index, int qValue) {
        this.index = index;
        this.qValue = qValue;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getqValue() {
        return qValue;
    }

    public void setqValue(int qValue) {
        this.qValue = qValue;
    }

    @Override
    public String toString() {
        return "Index{" +
                "index=" + index +
                ", qValue=" + qValue +
                '}';
    }
}
