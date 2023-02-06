package runMain;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

class PannableCanvas extends Pane {

    DoubleProperty myScale = new SimpleDoubleProperty(1.0);


    public PannableCanvas() {
        //  setPrefSize(980, 665);
        //
        //
        // setStyle("-fx-border-color: blue;");

        // add scale transform
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);
    }

    /**
     * Add a grid to the canvas, send it to back
     */
    public void addGrid() {

        double w = 1060;
        double h = 645;

        // add grid
        Canvas grid = new Canvas(w, h);

        // don't catch mouse events
        grid.setMouseTransparent(true);

        GraphicsContext gc = grid.getGraphicsContext2D();

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);

        // draw grid lines
        double offset = 25;
        for (double i = 0; i <= w; i += offset) {
            gc.strokeLine(i, 0, i, h);
            gc.strokeLine(0, i, w, i);
        }

        getChildren().add(grid);

        grid.toBack();
    }

    public double getScale() {
        return myScale.get();
    }

    public void setScale(double scale) {
        myScale.set(scale);
    }

    public void setPivot(double x, double y) {
        setTranslateX(getTranslateX() - x);
        setTranslateY(getTranslateY() - y);
    }


}