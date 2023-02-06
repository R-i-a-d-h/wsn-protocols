package leachRL;

import java.util.ArrayList;

public class Ant {
    private int id;
    private ArrayList<Integer> trail;
    private int lastCity;
    private int closeTrail;

    public Ant(int lastCity, int id) {
        this.lastCity = lastCity;
        this.id = id;
        this.trail = new ArrayList<>();
        this.closeTrail = 0;

    }

    public boolean visited(int i) {
        for (Integer city : this.trail) {
            if (city == i) {
                return true;
            }
        }

        return false;
    }

    protected double trailLength(double graph[][]) {
        double length = 0;
        for (int i = 0; i < trail.size() - 1; i++)
            length += graph[trail.get(i)][trail.get(i + 1)];
        return length;
    }

    public Boolean checkCity(int city, double[][] graph) {
        int currentCity = this.trail.get(this.trail.size() - 1);
        if (graph[currentCity][city] == 0) {
            return false;
        }

        return true;
    }


    public Boolean trailHealthy() {
        int currentCity = this.trail.get(this.trail.size() - 1);
        if (currentCity == this.lastCity) {
            return true;
        }
        return false;
    }


    protected void clear() {
        trail.clear();
        closeTrail = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getTrail() {
        return trail;
    }

    public void setTrail(ArrayList<Integer> trail) {
        this.trail = trail;
    }

    public int getLastCity() {
        return lastCity;
    }

    public void setLastCity(int lastCity) {
        this.lastCity = lastCity;
    }

    public int getCloseTrail() {
        return closeTrail;
    }

    public void setCloseTrail(int closeTrail) {
        this.closeTrail = closeTrail;
    }

    public void visitCity(int selectNextCity) {
        if (selectNextCity == this.lastCity) {
            this.closeTrail = 1;
        }

        this.trail.add(selectNextCity);
    }
}
