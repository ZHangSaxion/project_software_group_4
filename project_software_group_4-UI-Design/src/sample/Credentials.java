package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Credentials {

    /**
     * function to close the credentials window
     *
     * @param e ActionEvent to get the stage of the button that executed the function
     */
    public void yesClose(ActionEvent e) {
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
    }
}
