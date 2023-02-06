package runMain;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class MouseMoved {

    public Label lab;
    private EventHandler<MouseEvent> onMouseMovedEventHandler = new EventHandler<MouseEvent>() {

        public void handle(MouseEvent event) {
            lab.setText("X : " + (event.getSceneX() - 120.0) + " Y : " + event.getSceneY() + "");


            event.consume();

        }

    };

    public MouseMoved(Label lab) {
        this.lab = lab;
    }

    public EventHandler<MouseEvent> getOnMovedEventHandler() {
        return onMouseMovedEventHandler;
    }
}
