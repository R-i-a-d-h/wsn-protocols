package runMain;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import leachRL.AntColonyOptimizationParameters;
import leachRL.LeachRlAco;
import simulation.RSimulation;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LeachRLAntParametersController implements Initializable {
    public TextField rf;
    public TextField maxIt;
    public TextField af;
    public TextField q;
    public TextField ev;
    public TextField beta;
    public TextField alpha;
    public TextField c;
    public TextField sleep;
    public TextField r;
    public TextField eda;
    public TextField eamp;
    public TextField eelec;
    public TextField eo;
    public TextField n;
    public TextField sinkx;
    public TextField sinky;
    public TextField ym;
    public TextField xm;
    public AnchorPane pane;
    public TextField k;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sinky.setText(50 + "");
        sinkx.setText(50 + "");
        eo.setText(2 + "");
        eelec.setText(50 * Math.pow(10, (-9)) + "");
        eamp.setText(100 * Math.pow(10, (-12)) + "");
        eda.setText(5 * Math.pow(10, (-9)) + "");
        k.setText(4000 + "");
        r.setText(40 + "");
        xm.setText(100 + "");
        ym.setText(100 + "");
        n.setText(100 + "");
        sleep.setText(50 + "");

        c.setText(1 + "");
        alpha.setText(1 + "");
        q.setText(500 + "");
        beta.setText(5 + "");
        af.setText(2 + "");
        maxIt.setText(20 + "");
        rf.setText(0.5 + "");
        ev.setText(1 + "");


    }


    public void onRun(ActionEvent actionEvent) {
        String sinkxText = sinkx.getText();
        String sinkyText = sinky.getText();
        String kText = k.getText();
        String eoText = eo.getText();
        String eampText = eamp.getText();
        String eelecText = eelec.getText();
        String edaText = eda.getText();
        String nText = n.getText();
        String ymText = ym.getText();
        String xmText = xm.getText();
        String pText = r.getText();
        String sleepText = sleep.getText();

        String cText = c.getText();
        String alphaText = alpha.getText();
        String betaText = beta.getText();
        String qText = q.getText();
        String antFactorText = af.getText();
        String maxItText = maxIt.getText();
        String evText = ev.getText();
        String rfText = rf.getText();


        if (!sinkxText.isEmpty()
                && !sinkyText.isEmpty()
                && !kText.isEmpty() && !eoText.isEmpty()
                && !eampText.isEmpty()
                && !eelecText.isEmpty()
                && !edaText.isEmpty()
                && !nText.isEmpty()
                && !ymText.isEmpty()
                && !xmText.isEmpty()
                && !pText.isEmpty()
                && !cText.isEmpty()
                && !alphaText.isEmpty()
                && !betaText.isEmpty()
                && !qText.isEmpty()
                && !antFactorText.isEmpty()
                && !maxItText.isEmpty()
                && !evText.isEmpty()
                && !rfText.isEmpty()
        ) {
            LeachRLParameters leachRLParameters = new LeachRLParameters(
                    Integer.parseInt(kText),
                    Integer.parseInt(nText),
                    Double.parseDouble(sinkxText),
                    Double.parseDouble(sinkyText),
                    Double.parseDouble(xmText),
                    Double.parseDouble(ymText),
                    Integer.parseInt(pText),
                    Double.parseDouble(eoText),
                    Double.parseDouble(edaText),
                    Double.parseDouble(eampText),
                    Double.parseDouble(eelecText),
                    Long.parseLong(sleepText)
            );
            AntColonyOptimizationParameters antColonyOptimizationParameters = new AntColonyOptimizationParameters(
                    Double.parseDouble(cText),
                    Double.parseDouble(alphaText),
                    Double.parseDouble(betaText),
                    Double.parseDouble(evText),
                    Double.parseDouble(qText),
                    Double.parseDouble(antFactorText),
                    Double.parseDouble(rfText),
                    Integer.parseInt(maxItText)
            );


            Light.Distant light = new Light.Distant();
            light.setAzimuth(0);
            Lighting lighting = new Lighting(light);
            lighting.setSurfaceScale(0);
            FXMLLoader lr = new FXMLLoader(getClass().getResource("loading.fxml"));
            Stage st = new Stage();
            Parent root = null;
            try {
                root = lr.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Scene se = new Scene(root);
            st.setScene(se);
            st.initStyle(StageStyle.TRANSPARENT);
            st.setTitle("Simulator");
            st.getIcons().add(new Image("runMain/main.png"));
            st.show();
            pane.setEffect(lighting);
            Task<RSimulation> task = new Task<RSimulation>() {

                @Override
                protected RSimulation call() throws Exception {
                    /* send a http request */
                    System.out.println("LEach AMt");
                    LeachRlAco leachRlAco = new LeachRlAco(leachRLParameters, antColonyOptimizationParameters);
                    return leachRlAco.run();
                }
            };

            task.setOnSucceeded(event -> {
                System.out.println("The request has been finished");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("leach.fxml"));
                loader.setControllerFactory(e -> {

                    return new LeachRLAntController(task.getValue(), leachRLParameters);

                });
                Stage stage = new Stage();
                Parent rot = null;
                try {
                    rot = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Scene scene = new Scene(rot);
                stage.setScene(scene);
                st.close();
                pane.setEffect(null);
                stage.show();
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
            //TODO open start controller
        } else {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setContentText("Fields must be empty");
            alert2.setTitle("INFORMATION");
            alert2.setHeaderText("");
            alert2.showAndWait();
            return;
        }


    }
}
