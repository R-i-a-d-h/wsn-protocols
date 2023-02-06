package runMain;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {


    public AnchorPane pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onOpenLeachProtocol(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("leach-parameters.fxml"));

        Stage stage = new Stage();

        Parent rot = null;
        try {
            rot = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Scene scene = new Scene(rot);
        stage.setScene(scene);
        stage.setTitle("Simulator");
        stage.getIcons().add(new Image("runMain/main.png"));
        stage.show();

    }

    public void onOpenLeachRLProtocol(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("leachRL-parameters.fxml"));

        Stage stage = new Stage();

        Parent rot = null;
        try {
            rot = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Scene scene = new Scene(rot);
        stage.setScene(scene);
        stage.setTitle("Simulator");
        stage.getIcons().add(new Image("runMain/main.png"));
        stage.show();


//        Light.Distant light = new Light.Distant();
//        light.setAzimuth(0);
//        Lighting lighting = new Lighting(light);
//        lighting.setSurfaceScale(0);
//
//
//        System.out.println("The request has been finished");
//        FXMLLoader lr = new FXMLLoader(getClass().getResource("loading.fxml"));
//
//        Stage st = new Stage();
//        Parent root = null;
//        try {
//            root = lr.load();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Scene se = new Scene(root);
//        st.setScene(se);
//        st.initStyle(StageStyle.TRANSPARENT);
//        st.setTitle("Simulator");
//        st.getIcons().add(new Image("runMain/main.png"));
//        st.show();
//        pane.setEffect(lighting);
//        Task<RSimulation> task = new Task<RSimulation>() {
//
//            @Override
//            protected RSimulation call() throws Exception {
//                /* send a http request */
//                LeachRL leach = new LeachRL();
//                RSimulation simulation = leach.run();
//                return simulation;
//            }
//        };
//
//        task.setOnSucceeded(event -> {
//            System.out.println("The request has been finished");
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("LeachRL.fxml"));
//            loader.setControllerFactory(e -> {
//
//                return new LeachRLController(task.getValue());
//
//            });
//            Stage stage = new Stage();
//            Parent rot = null;
//            try {
//                rot = loader.load();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            Scene scene = new Scene(rot);
//            stage.setScene(scene);
//            st.close();
//            pane.setEffect(null);
//            stage.show();
//        });
//
//        Thread thread = new Thread(task);
//        thread.setDaemon(true);
//        thread.start();
    }

    public void onOpenHistory(ActionEvent actionEvent) {
        //TODO history
    }

    public void onOpenLeachRLAntProtocol(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("leachRLAnt-parameters.fxml"));

        Stage stage = new Stage();

        Parent rot = null;
        try {
            rot = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Scene scene = new Scene(rot);
        stage.setScene(scene);
        stage.setTitle("Simulator");
        stage.getIcons().add(new Image("runMain/main.png"));
        stage.show();


    }
}
