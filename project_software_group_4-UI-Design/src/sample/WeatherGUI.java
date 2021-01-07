package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class WeatherGUI {
    @FXML
    private LineChart<Number, Number> temperatureGraphs;
    @FXML
    private LineChart<Number, Number> pressureGraphs;
    @FXML
    private LineChart<Number, Number> ambientLightGraphs;

    private List<Sensor> sensors;
    private List<Reading> readings;

    public void initialize() {
        updateReadings();
    }

    public void updateReadings() {
        var parser = new Parser();
        sensors = parser.getSensors();
        readings = parser.getReadings();
    }

    public void updateGraphs() {
        sensors.stream()
                .map(Sensor::getId)
                .forEach(id -> {
                    addTemperatureGraph(id);
                    addPressureGraph(id);
                    addAmbientLightGraph(id);
                });
    }

    public void addTemperatureGraph(int sensor) {
        var graph = new XYChart.Series<Number, Number>();
        var graphName = sensors.stream()
                .filter(sensor1 -> sensor1.getId() == sensor)
                .collect(Collectors.toList())
                .get(0)
                .getLocation();
        graph.setName(graphName);
        readings.stream()
                .filter(reading -> reading.getSensor_id() == sensor)
                .map(reading -> new XYChart.Data<Number, Number>(reading.getDate().toEpochSecond(ZoneOffset.UTC), reading.getTemperature()))
                .forEach(reading -> graph.getData().add(reading));
        temperatureGraphs.getData().add(graph);
    }

    public void addPressureGraph(int sensor) {
        var graph = new XYChart.Series<Number, Number>();
        var graphName = sensors.stream()
                .filter(sensor1 -> sensor1.getId() == sensor)
                .collect(Collectors.toList())
                .get(0)
                .getLocation();
        graph.setName(graphName);
        readings.stream()
                .filter(reading -> reading.getSensor_id() == sensor)
                .map(reading -> new XYChart.Data<Number, Number>(reading.getDate().toEpochSecond(ZoneOffset.UTC), reading.getA_pressure()))
                .forEach(reading -> graph.getData().add(reading));
        pressureGraphs.getData().add(graph);
    }

    public void addAmbientLightGraph(int sensor) {
        var graph = new XYChart.Series<Number, Number>();
        var graphName = sensors.stream()
                .filter(sensor1 -> sensor1.getId() == sensor)
                .collect(Collectors.toList())
                .get(0)
                .getLocation();
        graph.setName(graphName);
        readings.stream()
                .filter(reading -> reading.getSensor_id() == sensor)
                .map(reading -> new XYChart.Data<Number, Number>(reading.getDate().toEpochSecond(ZoneOffset.UTC), reading.getAmbient_light()))
                .forEach(reading -> graph.getData().add(reading));
        ambientLightGraphs.getData().add(graph);
    }

//    public void removeTemperatureGraph(int sensor) {
//        var graph = temperatureGraphs.getData().stream()
//                .filter(graph -> graph.getName().equals(
//                        sensors.stream()
//                ))
//        temperatureGraphs.getData().remove(graph);
//    }

    public void removePressureGraph(int sensor) {
        pressureGraphs.getData().removeIf(graph -> graph.getName().equals(String.valueOf(sensor)));
    }

    public void removeAmbientLightGraph(int sensor) {
        ambientLightGraphs.getData().removeIf(graph -> graph.getName().equals(String.valueOf(sensor)));
    }

    public void lastHour(ActionEvent e) {

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
