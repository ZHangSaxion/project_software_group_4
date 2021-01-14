package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * javafx function to start the GUI
     *
     * @param primaryStage argument passed by the javafx application
     * @throws Exception if something goes wrong, you will know
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("weatherGUI.fxml"));
        primaryStage.setTitle("Weather Application Group 4");
        Scene weatherScene = new Scene(root, 1020, 700);
        primaryStage.setScene(weatherScene);
        primaryStage.show();
    }

    /**
     * main function, program arguments are passed here
     *
     * @param args program arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

