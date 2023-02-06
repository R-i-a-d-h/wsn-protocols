package runMain;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import leachRL.Point;
import simulation.RNode;
import simulation.RSimulation;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LeachRLAntController implements Initializable {
    public AnchorPane pane;
    public Label lab_1;
    public Label lab_2;
    public Label lab_3;
    public Label lab_4;
    public AnchorPane areapane;
    public ProgressBar pr;
    public ImageView playImage;
    PannableCanvas canvas;
    RSimulation simulation;
    double kx;
    double ky;
    LeachRLParameters parameters;
    private Boolean pause = false;
    private SimpleIntegerProperty count = new SimpleIntegerProperty(-1);

    public LeachRLAntController(RSimulation simulation, LeachRLParameters parameters) {
        this.parameters = parameters;
        this.simulation = simulation;
        this.kx = 1360 / parameters.getXm();
        this.ky = 550 / parameters.getYm();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onProgress(count.get());


    }

    private void onProgress(int i) {
        count.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pr.setProgress((double) count.get() / simulation.getiRounds().size());
            }
        });
        count.set(i);


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        canvas = new PannableCanvas();
                        SceneGestures sceneGestures = new SceneGestures(canvas);
                        //MouseMoved mouseMoved = new MouseMoved();

                        //pane.addEventFilter( MouseEvent.MOUSE_MOVED, mouseMoved.getOnMovedEventHandler());
                        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
                        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
                        canvas.addEventFilter(ScrollEvent.SCROLL, sceneGestures.getOnScrollEventHandler());
                        //canvas.addGrid();

                        areapane.getChildren().clear();
                        areapane.getChildren().add(canvas);

                        Rectangle rectangle = new Rectangle(parameters.getSinkx() * kx, parameters.getSinky() * ky, 15, 15);
                        rectangle.setFill(Color.GREEN);
                        areapane.getChildren().add(rectangle);


                        count.set(count.get() + 1);
                        ArrayList<RNode> nodes = simulation.getiRounds().get(count.get()).getNodes();
                        lab_1.setText("NB rnd : " + count.get());
                        lab_2.setText("NB CH : " + simulation.getiRounds().get(count.get()).getNbCH());
                        lab_3.setText("NB node : " + (nodes.size() - simulation.getiRounds().get(count.get()).getNbCH()));
                        int dead = 0;
                        for (int i = 0; i < nodes.size(); i++) {
                            RNode node = nodes.get(i);
                            if (node.getCond() == 0) {
                                dead++;
                            }
                        }
                        lab_4.setText("NB dead : " + dead);
                        //pane.getChildren().addAll(lab_1,lab_2,lab_3,lab_4);
                        for (int i = 0; i < nodes.size(); i++) {
                            RNode node = nodes.get(i);
                            // System.out.println(node.getE());
                            //  System.out.println(simulation.getiRounds().get(0).getNodes().get(i).getE());
//

                            if (node.getRole() == 0 && node.getCond() == 1) {
                                Line line = new Line();
                                line.setStartX(node.getX() * kx);
                                line.setStyle("-fx-stroke: red;");
                                line.setStartY(node.getY() * ky);
                                line.setEndX(nodes.get(node.getChid()).getX() * kx);
                                line.setEndY(nodes.get(node.getChid()).getY() * ky);
                                areapane.getChildren().add(line);

                            }


                        }


                        for (int i = 0; i < nodes.size(); i++) {
                            RNode node = nodes.get(i);
                            if (node.getRole() == 1 && node.getCond() == 1) {
                                Line line = new Line();
                                line.setStyle("-fx-stroke: green;");
                                line.setStrokeWidth(5);
                                line.setStartX(node.getX() * kx);
                                line.setStartY(node.getY() * ky);
                                if (nodes.get(i).getChid() == -2) {
                                    line.setEndX(parameters.getSinkx() * kx);
                                    line.setEndY(parameters.getSinky() * ky);

                                } else {
                                    line.setEndX(nodes.get(node.getChid()).getX() * kx);
                                    line.setEndY(nodes.get(node.getChid()).getY() * ky);
                                }

                                areapane.getChildren().add(line);
                            }
                        }
                        ArrayList<Point> points = simulation.getPoints();

                        for (int i = 0; i < points.size(); i++) {
                            Point point = points.get(i);
                            Circle circle = new Circle(point.getX() * kx, point.getY() * ky, 7);
                            circle.setFill(Color.AZURE);
                            areapane.getChildren().add(circle);
                        }
                        for (int i = 0; i < nodes.size(); i++) {
                            RNode node = nodes.get(i);

                            Circle circle = new Circle(node.getX() * kx, node.getY() * ky, 5);
                            if (node.getRole() == 0) {
                                circle.setFill(Color.BLACK);
                            } else {
                                circle.setFill(Color.RED);
                            }
                            if (node.getCond() == 0) {
                                circle.setFill(Color.SILVER);
                            }
                            areapane.getChildren().add(circle);
                        }

                    }

                };

                while (count.get() < simulation.getiRounds().size() - 1 && pause) {
                    try {
                        Thread.sleep(parameters.getSleep());
                    } catch (InterruptedException ex) {
                    }

                    // UI update is run on the Application thread
                    Platform.runLater(updater);
                }
            }

        });
        // don't let thread prevent JVM shutdown
        thread.setDaemon(true);
        thread.start();
    }

    public void onStartAndPause(ActionEvent actionEvent) {
        if (pause) {
            pause = false;
            playImage.setImage(new Image("runMain/play.png"));
        } else {
            pause = true;
            int i = count.get();
            playImage.setImage(new Image("runMain/pause.png"));
            onProgress(i);
        }

    }

    public void onReplay(ActionEvent actionEvent) {
        count.set(-1);
        updateNextUI();
    }

    private void updateNextUI() {
        canvas = new PannableCanvas();
        SceneGestures sceneGestures = new SceneGestures(canvas);
        //MouseMoved mouseMoved = new MouseMoved();

        //pane.addEventFilter( MouseEvent.MOUSE_MOVED, mouseMoved.getOnMovedEventHandler());
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        canvas.addEventFilter(ScrollEvent.SCROLL, sceneGestures.getOnScrollEventHandler());
        //canvas.addGrid();

        areapane.getChildren().clear();
        areapane.getChildren().add(canvas);

        Rectangle rectangle = new Rectangle(parameters.getSinkx() * kx, parameters.getSinky() * ky, 15, 15);
        rectangle.setFill(Color.GREEN);
        areapane.getChildren().add(rectangle);


        count.set(count.get() + 1);
        ArrayList<RNode> nodes = simulation.getiRounds().get(count.get()).getNodes();
        lab_1.setText("NB rnd : " + count.get());
        lab_2.setText("NB CH : " + simulation.getiRounds().get(count.get()).getNbCH());
        lab_3.setText("NB node : " + (nodes.size() - simulation.getiRounds().get(count.get()).getNbCH()));
        int dead = 0;
        for (int i = 0; i < nodes.size(); i++) {
            RNode node = nodes.get(i);
            if (node.getCond() == 0) {
                dead++;
            }
        }
        lab_4.setText("NB dead : " + dead);
        //pane.getChildren().addAll(lab_1,lab_2,lab_3,lab_4);
        for (int i = 0; i < nodes.size(); i++) {
            RNode node = nodes.get(i);
            // System.out.println(node.getE());
            //  System.out.println(simulation.getiRounds().get(0).getNodes().get(i).getE());
//

            if (node.getRole() == 0 && node.getCond() == 1) {
                Line line = new Line();
                line.setStartX(node.getX() * kx);
                line.setStyle("-fx-stroke: red;");
                line.setStartY(node.getY() * ky);
                line.setEndX(nodes.get(node.getChid()).getX() * kx);
                line.setEndY(nodes.get(node.getChid()).getY() * ky);
                areapane.getChildren().add(line);

            }


        }
        for (int i = 0; i < nodes.size(); i++) {
            RNode node = nodes.get(i);
            if (node.getRole() == 1 && node.getCond() == 1) {
                Line line = new Line();
                line.setStyle("-fx-stroke: green;");
                line.setStrokeWidth(5);
                line.setStartX(node.getX() * kx);
                line.setStartY(node.getY() * ky);
                if (nodes.get(i).getChid() == -2) {
                    line.setEndX(parameters.getSinkx() * kx);
                    line.setEndY(parameters.getSinky() * ky);

                } else {
                    line.setEndX(nodes.get(node.getChid()).getX() * kx);
                    line.setEndY(nodes.get(node.getChid()).getY() * ky);
                }

                areapane.getChildren().add(line);
            }
        }
        ArrayList<Point> points = simulation.getPoints();

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            Circle circle = new Circle(point.getX() * kx, point.getY() * ky, 7);
            circle.setFill(Color.AZURE);
            areapane.getChildren().add(circle);
        }
        for (int i = 0; i < nodes.size(); i++) {
            RNode node = nodes.get(i);

            Circle circle = new Circle(node.getX() * kx, node.getY() * ky, 5);
            if (node.getRole() == 0) {
                circle.setFill(Color.BLACK);
            } else {
                circle.setFill(Color.RED);
            }
            if (node.getCond() == 0) {
                circle.setFill(Color.SILVER);
            }
            areapane.getChildren().add(circle);
        }

    }

    private void updateBackUI() {
        canvas = new PannableCanvas();
        SceneGestures sceneGestures = new SceneGestures(canvas);
        //MouseMoved mouseMoved = new MouseMoved();

        //pane.addEventFilter( MouseEvent.MOUSE_MOVED, mouseMoved.getOnMovedEventHandler());
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        canvas.addEventFilter(ScrollEvent.SCROLL, sceneGestures.getOnScrollEventHandler());
        //canvas.addGrid();

        areapane.getChildren().clear();
        areapane.getChildren().add(canvas);
        Rectangle rectangle = new Rectangle(parameters.getSinkx() * kx, parameters.getSinky() * ky, 15, 15);
        rectangle.setFill(Color.GREEN);
        areapane.getChildren().add(rectangle);


        count.set(count.get() - 1);
        ArrayList<RNode> nodes = simulation.getiRounds().get(count.get()).getNodes();
        lab_1.setText("NB rnd : " + count.get());
        lab_2.setText("NB CH : " + simulation.getiRounds().get(count.get()).getNbCH());
        lab_3.setText("NB node : " + (nodes.size() - simulation.getiRounds().get(count.get()).getNbCH()));
        int dead = 0;
        for (int i = 0; i < nodes.size(); i++) {
            RNode node = nodes.get(i);
            if (node.getCond() == 0) {
                dead++;
            }
        }
        lab_4.setText("NB dead : " + dead);
        //pane.getChildren().addAll(lab_1,lab_2,lab_3,lab_4);
        for (int i = 0; i < nodes.size(); i++) {
            RNode node = nodes.get(i);
            // System.out.println(node.getE());
            //  System.out.println(simulation.getiRounds().get(0).getNodes().get(i).getE());
//

            if (node.getRole() == 0 && node.getCond() == 1) {
                Line line = new Line();
                line.setStartX(node.getX() * kx);
                line.setStyle("-fx-stroke: red;");
                line.setStartY(node.getY() * ky);
                line.setEndX(nodes.get(node.getChid()).getX() * kx);
                line.setEndY(nodes.get(node.getChid()).getY() * ky);
                areapane.getChildren().add(line);

            }


        }
        for (int i = 0; i < nodes.size(); i++) {
            RNode node = nodes.get(i);
            if (node.getRole() == 1 && node.getCond() == 1) {
                Line line = new Line();
                line.setStyle("-fx-stroke: green;");
                line.setStrokeWidth(5);
                line.setStartX(node.getX() * kx);
                line.setStartY(node.getY() * ky);
                if (nodes.get(i).getChid() == -2) {
                    line.setEndX(parameters.getSinkx() * kx);
                    line.setEndY(parameters.getSinky() * ky);

                } else {
                    line.setEndX(nodes.get(node.getChid()).getX() * kx);
                    line.setEndY(nodes.get(node.getChid()).getY() * ky);
                }

                areapane.getChildren().add(line);
            }
        }
        ArrayList<Point> points = simulation.getPoints();

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            Circle circle = new Circle(point.getX() * kx, point.getY() * ky, 7);
            circle.setFill(Color.AZURE);
            areapane.getChildren().add(circle);
        }
        for (int i = 0; i < nodes.size(); i++) {
            RNode node = nodes.get(i);

            Circle circle = new Circle(node.getX() * kx, node.getY() * ky, 5);
            if (node.getRole() == 0) {
                circle.setFill(Color.BLACK);
            } else {
                circle.setFill(Color.RED);
            }
            if (node.getCond() == 0) {
                circle.setFill(Color.SILVER);
            }
            areapane.getChildren().add(circle);
        }

    }

    public void onNext(ActionEvent actionEvent) {
        int i = count.get();
        count.set(i);
        updateNextUI();
    }


    public void onBack(ActionEvent actionEvent) {
        int i = count.get();
        if (i > 0) {
            count.set(i);
            updateBackUI();
        }

    }


}

