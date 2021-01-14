package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MsgError {

    /**
     * function to close the window of the error message
     *
     * @param e ActionEvent to get the stage of the button that executed the function
     */
    public void closeMsgError(ActionEvent e) {
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
    }
}
