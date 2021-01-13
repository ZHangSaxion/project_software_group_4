package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ExitWnd {

    /**
     * function to quit the program
     *
     * @param e unused ActionEvent
     */
    public void yesExit(ActionEvent e) {
        System.exit(0);
    }

    /**
     * function to return to the main window
     *
     * @param e ActionEvent to get the stage of the button that executed the function
     */
    public void noExit(ActionEvent e) {
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
    }

}


