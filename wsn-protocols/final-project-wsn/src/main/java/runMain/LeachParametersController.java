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
import leach.Leach;
import simulation.Simulation;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LeachParametersController implements Initializable {
    public TextField sinkx;
    public TextField sinky;
    public TextField eo;
    public TextField eelec;
    public TextField eamp;
    public TextField eda;
    public TextField k;
    public TextField p;
    public TextField n;
    public TextField ym;
    public TextField xm;
    public AnchorPane pane;
    public TextField sleep;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sinky.setText(50 + "");
        sinkx.setText(50 + "");
        eo.setText(2 + "");
        eelec.setText(50 * Math.pow(10, (-9)) + "");
        eamp.setText(100 * Math.pow(10, (-12)) + "");
        eda.setText(5 * Math.pow(10, (-9)) + "");
        k.setText(4000 + "");
        p.setText(0.1 + "");
        xm.setText(100 + "");
        ym.setText(100 + "");
        n.setText(100 + "");
        sleep.setText(50 + "");


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
        String pText = p.getText();
        String sleepText = sleep.getText();
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
        ) {
            LeachParameters leachParameters = new LeachParameters(
                    Integer.parseInt(kText),
                    Integer.parseInt(nText),
                    Double.parseDouble(sinkxText),
                    Double.parseDouble(sinkyText),
                    Double.parseDouble(xmText),
                    Double.parseDouble(ymText),
                    Double.parseDouble(pText),
                    Double.parseDouble(eoText),
                    Double.parseDouble(edaText),
                    Double.parseDouble(eampText),
                    Double.parseDouble(eelecText),
                    Long.parseLong(sleepText)
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
            Task<Simulation> task = new Task<Simulation>() {

                @Override
                protected Simulation call() throws Exception {
                    /* send a http request */
                    Leach leach = new Leach(leachParameters);
                    return leach.run();
                }
            };

            task.setOnSucceeded(event -> {
                System.out.println("The request has been finished");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("leach.fxml"));
                loader.setControllerFactory(e -> {

                    return new LeachController(task.getValue(), leachParameters);

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
