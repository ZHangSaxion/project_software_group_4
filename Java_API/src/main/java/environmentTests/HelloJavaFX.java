package main.java.environmentTests;
/**
 * This is a test class for who want to test if gradle can work on his computer
 */


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloJavaFX extends Application {

    @Override
    public void start(Stage stage) {
        Label l = new Label("JavaFX ok.");
        Scene scene = new Scene(new StackPane(l), 150, 100);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}