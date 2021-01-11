package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.*;

public class WeatherGUI {
    @FXML
    private LineChart<String, Number> temperatureGraphs;

    @FXML
    private LineChart<String, Number> pressureGraphs;

    @FXML
    private LineChart<String, Number> ambientLightGraphs;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    @FXML
    private ComboBox<LocalTime> timeFrom;

    @FXML
    private ComboBox<LocalTime> timeTo;

    public void initialize() {
        //initialize the start and finish date - future date inhibited
        dateTo.setDayCellFactory ( picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem ( date, empty );
                LocalDate today = LocalDate.now ();

                setDisable ( empty || date.compareTo ( today ) > 0 );
            }
        } );
        dateFrom.setDayCellFactory ( picker -> new DateCell () {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem ( date, empty );
                LocalDate today = LocalDate.now ();

                setDisable ( empty || date.compareTo ( today ) > 0 );
            }
        } );
        dateTo.setValue(LocalDate.now());
        dateFrom.setValue(LocalDate.now());

        for (var i = 0; i < 24; ++i) {
            for (var j = 0; j < 60; j += 5) {
                timeFrom.getItems().add(LocalTime.of(i, j));
                timeTo.getItems().add(LocalTime.of(i, j));
            }
        }
        timeFrom.setValue(LocalTime.MIDNIGHT);
        timeTo.setValue(LocalTime.MIDNIGHT);
    }

    public void updateGraphs(ActionEvent e) {
        var dtFrom = dateFrom.getValue().atTime(timeFrom.getValue());
        var dtTo = dateTo.getValue().atTime(timeTo.getValue());
        if(dtFrom.isEqual(dtTo) || dtFrom.isAfter(dtTo)) {
            try {
                errorMsg();
                return;
            } catch(IOException iox) {
                iox.printStackTrace();
            }
        }
        temperatureGraphs.getData().clear();
        pressureGraphs.getData().clear();
        ambientLightGraphs.getData().clear();
        User.getSensorBools().forEach(sensorBool -> {
            var id = sensorBool.getSensor().getId();
            if(sensorBool.isTemperature()) addTemperatureGraph(id);
            else removeTemperatureGraph(id);
            if(sensorBool.isPressure()) addPressureGraph(id);
            else removePressureGraph(id);
            if(sensorBool.isAmbientLight()) addAmbientLightGraph(id);
            else removeAmbientLightGraph(id);
        });
    }

    private String getSensorNameFromId(int id) {
        return User.getSensorBools().stream()
                .map(SensorBool::getSensor)
                .filter(sensor -> sensor.getId() == id)
                .map(Sensor::getLocation)
                .findAny()
                .orElse("");
    }

    private int getSensorIdFromName(String location) {
        return User.getSensorBools().stream()
                .map(SensorBool::getSensor)
                .filter(sensor -> sensor.getLocation().equals(location))
                .map(Sensor::getId)
                .findAny()
                .orElse(0);
    }

    public void addTemperatureGraph(int sensor) {
        ObservableList<XYChart.Data<String, Number>> oList = FXCollections.observableArrayList();
        var format = DateTimeFormatter.ofPattern("d MMM uuuu HH:mm");
        var dtFrom = dateFrom.getValue().atTime(timeFrom.getValue());
        var dtTo = dateTo.getValue().atTime(timeTo.getValue());
        Parser.getTemperatures(dtFrom, dtTo, sensor).stream()
                .map(reading -> new XYChart.Data<String, Number>(reading.getDate().format(format), reading.getTemperature()))
                .forEach(oList::add);
        var sList = new SortedList<>(oList, Comparator.comparing(o -> LocalDateTime.parse(o.getXValue(), format)));
        temperatureGraphs.getData().add(new XYChart.Series<>(getSensorNameFromId(sensor), sList));
    }

    public void addPressureGraph(int sensor) {
        ObservableList<XYChart.Data<String, Number>> oList = FXCollections.observableArrayList();
        var format = DateTimeFormatter.ofPattern("d MMM uuuu HH:mm");
        var dtFrom = dateFrom.getValue().atTime(timeFrom.getValue());
        var dtTo = dateTo.getValue().atTime(timeTo.getValue());
        Parser.getPressures(dtFrom, dtTo, sensor).stream()
                .map(reading -> new XYChart.Data<String, Number>(reading.getDate().format(format), reading.getB_pressure()))
                .forEach(oList::add);
        var sList = new SortedList<>(oList, Comparator.comparing(o -> LocalDateTime.parse(o.getXValue(), format)));
        pressureGraphs.getData().add(new XYChart.Series<>(getSensorNameFromId(sensor), sList));
    }

    public void addAmbientLightGraph(int sensor) {
        ObservableList<XYChart.Data<String, Number>> oList = FXCollections.observableArrayList();
        var format = DateTimeFormatter.ofPattern("d MMM uuuu HH:mm");
        var dtFrom = dateFrom.getValue().atTime(timeFrom.getValue());
        var dtTo = dateTo.getValue().atTime(timeTo.getValue());
        Parser.getAmbientLights(dtFrom, dtTo, sensor).stream()
                .map(reading -> new XYChart.Data<String, Number>(reading.getDate().format(format), reading.getAmbient_light()))
                .forEach(oList::add);
        var sList = new SortedList<>(oList, Comparator.comparing(o -> LocalDateTime.parse(o.getXValue(), format)));
        ambientLightGraphs.getData().add(new XYChart.Series<>(getSensorNameFromId(sensor), sList));
    }

    public void removeTemperatureGraph(int sensor) {
        temperatureGraphs.getData().removeIf(graph -> getSensorIdFromName(graph.getName()) == sensor);
    }

    public void removePressureGraph(int sensor) {
        pressureGraphs.getData().removeIf(graph -> getSensorIdFromName(graph.getName()) == sensor);
    }

    public void removeAmbientLightGraph(int sensor) {
        ambientLightGraphs.getData().removeIf(graph -> getSensorIdFromName(graph.getName()) == sensor);
    }

    //show exit window
    public void exitMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader ( getClass ().getResource ( "ExitWnd.fxml" ) );
        Parent root2 = fxmlLoader.load ();
        Stage stage = new Stage ();
        stage.setResizable(false);
        stage.setTitle ( "Good Bye!" );
        stage.setScene ( new Scene ( root2 ) );
        stage.show ();
    }
    //show average window
    public void openAvg() throws IOException {
        var from = dateFrom.getValue().atTime(timeFrom.getValue());
        var to = dateTo.getValue().atTime(timeTo.getValue());
        User.setTimeFrame(from, to);
        FXMLLoader fxmlLoader = new FXMLLoader (getClass ().getResource ("averageTable.fxml"));
        Parent root1 = fxmlLoader.load ();
        Stage stage = new Stage ();
        stage.setResizable(false);
        stage.setTitle ( "Average Window" );
        stage.setScene ( new Scene ( root1 ) );
        stage.show ();
    }

    //show error message
    public void errorMsg() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader ( getClass ().getResource ( "MsgError.fxml" ) );
        Parent root3 = fxmlLoader.load ();
        Stage stage = new Stage ();
        stage.setTitle ( "Time Interval Error!" );
        stage.setScene ( new Scene ( root3 ) );
        stage.show ();
    }

    private void setLastX(TemporalAmount amount) {
        var now = LocalDateTime.now();
        dateTo.setValue(now.toLocalDate());
        timeTo.setValue(LocalTime.of(now.getHour(), now.getMinute()));
        var nowMinusX = now.minus(amount);
        dateFrom.setValue(nowMinusX.toLocalDate());
        timeFrom.setValue(LocalTime.of(nowMinusX.getHour(), nowMinusX.getMinute()));
    }

    public void setLastH() {
        setLastX(Duration.ofHours(1));
    }

    public void setLastD() {
        setLastX(Period.ofDays(1));
    }

    public void setLastW() {
        setLastX(Period.ofWeeks(1));
    }

    public void setLastM() {
        setLastX(Period.ofMonths(1));
    }

    public void shCredentials() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader ( getClass ().getResource ( "Credentials.fxml" ) );
        Parent root2 = fxmlLoader.load ();
        Stage stage = new Stage ();
        stage.setTitle ( "CREDENTIALS" );
        stage.setScene ( new Scene ( root2 ) );
        stage.show ();
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
