package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ExitWnd {

    public void yesExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void noExit(ActionEvent e) throws IOException {
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();

    }

}


