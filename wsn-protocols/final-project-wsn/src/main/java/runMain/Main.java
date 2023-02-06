package runMain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("start.fxml"));

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
