import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class loadingController implements Initializable {
    public AnchorPane pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        pane.getChildren().add(progressIndicator);
        progressIndicator.setStyle("-fx-accent: #58B691;");
    }
}
