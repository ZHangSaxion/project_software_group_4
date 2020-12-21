package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("weatherGUI.fxml"));
        primaryStage.setTitle("Weather Application Group 4");
        Scene weatherScene = new Scene(root, 1020, 700);
        primaryStage.setScene(weatherScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

