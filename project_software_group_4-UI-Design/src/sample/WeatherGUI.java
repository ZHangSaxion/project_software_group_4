package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import java.io.IOException;

public class WeatherGUI {
    @FXML
    public LineChart<Number, Number> tempChart;
    @FXML
    public MenuItem config;


    public void initialize() {
        System.out.println("initialize WeatherGUI");
    }

    public void openConfig(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader (getClass ().getResource ( "config.fxml" ));
        Parent root1 = fxmlLoader.load ();
        Stage stage = new Stage ();
        stage.setTitle ( "Configuration Window" );
        stage.setScene ( new Scene ( root1 ) );
        stage.show ();
    }

    public void exitMenu(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader (getClass ().getResource ( "ExitWnd.fxml" ));
        Parent root2 = fxmlLoader.load ();
        Stage stage = new Stage ();
        stage.setTitle ( "Good Bye!" );
        stage.setScene ( new Scene ( root2 ) );
        stage.show ();
    }



}
