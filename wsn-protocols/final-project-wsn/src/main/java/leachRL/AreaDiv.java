package leachRL;

import java.util.ArrayList;

public class AreaDiv {
    public ArrayList<Point> onDiv(double xm, double ym, double r, Point midpoint) {
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<Point> ypoints = new ArrayList<>();
        ArrayList<Point> xpoints = new ArrayList<>();
        points.add(midpoint);

        int i = 1;
        while (i * r < (xm - midpoint.getX())) {
            Point p1 = new Point((i * r) + midpoint.getX(), midpoint.getY());
            xpoints.add(p1);
            points.add(p1);
            i = i + 1;
        }

        i = 1;
        while (((-r) * i) + midpoint.getX() > 0) {
            Point p2 = new Point(((-r) * i) + midpoint.getX(), midpoint.getY());
            xpoints.add(p2);
            points.add(p2);
            i = i + 1;
        }

        i = 1;
        while (i * r < (ym - midpoint.getY())) {
            Point p1 = new Point(midpoint.getX(), (i * r) + midpoint.getY());
            ypoints.add(p1);
            points.add(p1);
            i = i + 1;
        }
        i = 1;
        while (((-r) * i) + midpoint.getY() > 0) {
            Point p2 = new Point(midpoint.getX(), (i * (-r)) + midpoint.getY());
            ypoints.add(p2);
            points.add(p2);
            i = i + 1;
        }
        for (int j = 0; j < xpoints.size(); j++) {
            Point px = xpoints.get(j);
            for (int g = 0; g < ypoints.size(); g++) {
                Point py = ypoints.get(g);
                Point point = new Point(px.getX(), py.getY());
                points.add(point);

            }

        }

        return points;
    }
}
