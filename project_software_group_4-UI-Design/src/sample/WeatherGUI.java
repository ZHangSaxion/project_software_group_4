package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
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
import java.util.Comparator;
import java.util.stream.Collectors;

public class WeatherGUI {
    @FXML
    private volatile LineChart<String, Number> temperatureGraphs;

    @FXML
    private volatile LineChart<String, Number> pressureGraphs;

    @FXML
    private volatile LineChart<String, Number> ambientLightGraphs;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    @FXML
    private ComboBox<String> timeFrom;

    @FXML
    private ComboBox<String> timeTo;

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
                timeFrom.getItems().add(LocalTime.of(i, j).toString());
                timeTo.getItems().add(LocalTime.of(i, j).toString());
            }
        }
        timeFrom.setValue("00:00");
        timeTo.setValue("00:00");
    }

    public void updateGraphs(ActionEvent e) {
        var dtFrom = dateFrom.getValue().atTime(LocalTime.parse(timeFrom.getValue()));
        var dtTo = dateTo.getValue().atTime(LocalTime.parse(timeTo.getValue()));
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
            if(sensorBool.isTemperature()) new Thread(() -> addTemperatureGraph(id)).start();
            else new Thread(() -> removeTemperatureGraph(id)).start();
            if(sensorBool.isPressure()) new Thread(() -> addPressureGraph(id)).start();
            else new Thread(() -> removePressureGraph(id)).start();
            if(sensorBool.isAmbientLight()) new Thread(() -> addAmbientLightGraph(id)).start();
            else new Thread(() -> removeAmbientLightGraph(id)).start();
        });
//        Platform.runLater(this::sortAxis);
    }

//    private void sortAxis() { //TODO sort axis correctly
//        var format = DateTimeFormatter.ofPattern("d MMM uuuu HH:mm");
//        var temperatureAxis = (CategoryAxis) temperatureGraphs.getXAxis();
//        var orderedTemperatureDates = temperatureGraphs.getData().stream()
//                .flatMap(stringNumberSeries -> stringNumberSeries.getData().stream()
//                        .map(XYChart.Data::getXValue))
//                .distinct()
//                .sorted(Comparator.comparing(datetime -> LocalDateTime.parse(datetime, format)))
//                .collect(Collectors.toList());
//
//        var pressureAxis = (CategoryAxis) pressureGraphs.getXAxis();
//        var ambientLightAxis = (CategoryAxis) ambientLightGraphs.getXAxis();
//
//    }

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

    private Reading roundTime(Reading reading) {
        LocalDateTime rounded;
        if(reading.getDate().getMinute() < 3) {
            rounded = reading.getDate().minusMinutes(reading.getDate().getMinute() % 5);
        } else {
            rounded = reading.getDate().plusMinutes(5 - (reading.getDate().getMinute() % 5));
        }
        reading.setDate(rounded);
        return reading;
    }

    public void addTemperatureGraph(int sensor) {
        var format = DateTimeFormatter.ofPattern("d MMM uuuu HH:mm");
        var dtFrom = dateFrom.getValue().atTime(LocalTime.parse(timeFrom.getValue()));
        var dtTo = dateTo.getValue().atTime(LocalTime.parse(timeTo.getValue()));
        var graph = new XYChart.Series<String, Number>();
        graph.setName(getSensorNameFromId(sensor));
        Parser.getTemperatures(dtFrom, dtTo, sensor).parallelStream()
                .map(this::roundTime)
                .distinct()
                .map(reading -> new XYChart.Data<String, Number>(reading.getDate().format(format), reading.getTemperature()))
                .sorted(Comparator.comparing(XYChart.Data::getXValue))
                .collect(Collectors.toList())
                .forEach(graph.getData()::add);
        Platform.runLater(() -> temperatureGraphs.getData().add(graph));
    }

    public void addPressureGraph(int sensor) {
        var format = DateTimeFormatter.ofPattern("d MMM uuuu HH:mm");
        var dtFrom = dateFrom.getValue().atTime(LocalTime.parse(timeFrom.getValue()));
        var dtTo = dateTo.getValue().atTime(LocalTime.parse(timeTo.getValue()));
        var graph = new XYChart.Series<String, Number>();
        graph.setName(getSensorNameFromId(sensor));
        Parser.getPressures(dtFrom, dtTo, sensor).parallelStream()
                .map(this::roundTime)
                .distinct()
                .map(reading -> new XYChart.Data<String, Number>(reading.getDate().format(format), reading.getB_pressure()))
                .sorted(Comparator.comparing(XYChart.Data::getXValue))
                .collect(Collectors.toList())
                .forEach(graph.getData()::add);
        Platform.runLater(() -> pressureGraphs.getData().add(graph));
    }

    public void addAmbientLightGraph(int sensor) {
        var format = DateTimeFormatter.ofPattern("d MMM uuuu HH:mm");
        var dtFrom = dateFrom.getValue().atTime(LocalTime.parse(timeFrom.getValue()));
        var dtTo = dateTo.getValue().atTime(LocalTime.parse(timeTo.getValue()));
        var graph = new XYChart.Series<String, Number>();
        graph.setName(getSensorNameFromId(sensor));
        Parser.getAmbientLights(dtFrom, dtTo, sensor).parallelStream()
                .map(this::roundTime)
                .distinct()
                .map(reading -> new XYChart.Data<String, Number>(reading.getDate().format(format), reading.getAmbient_light()))
                .sorted(Comparator.comparing(XYChart.Data::getXValue))
                .collect(Collectors.toList())
                .forEach(graph.getData()::add);
        Platform.runLater(() -> ambientLightGraphs.getData().add(graph));
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
        var dtFrom = dateFrom.getValue().atTime(LocalTime.parse(timeFrom.getValue()));
        var dtTo = dateTo.getValue().atTime(LocalTime.parse(timeTo.getValue()));
        if(dtFrom.isEqual(dtTo) || dtFrom.isAfter(dtTo)) {
            try {
                errorMsg();
                return;
            } catch(IOException iox) {
                iox.printStackTrace();
            }
        }
        User.setTimeFrame(dtFrom, dtTo);
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
        timeTo.setValue(now.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        var nowMinusX = now.minus(amount);
        dateFrom.setValue(nowMinusX.toLocalDate());
        timeFrom.setValue(nowMinusX.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
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
