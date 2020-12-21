package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class ExitWnd {

    public void yesExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void noExit(ActionEvent e) throws IOException {
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();

    }

}


