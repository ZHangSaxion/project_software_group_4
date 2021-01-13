package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    private ComboBox<String> timeFrom;

    @FXML
    private ComboBox<String> timeTo;

    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("d MMM uuuu HH:mm");
    private LocalDateTime dtFrom;
    private LocalDateTime dtTo;

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

    private String getSensorNameFromId(int id) {
        return User.getSensorBools().stream()
                .map(SensorBool::getSensor)
                .filter(sensor -> sensor.getId() == id)
                .map(Sensor::getLocation)
                .findAny()
                .orElse("");
    }

    private boolean invalidTimeFrame() {
        try {
            dtFrom = dateFrom.getValue().atTime(LocalTime.parse(timeFrom.getValue()));
            dtTo = dateTo.getValue().atTime(LocalTime.parse(timeTo.getValue()));
        } catch(DateTimeParseException e) {
            e.printStackTrace();
            try {
                errorMsg();
                return true;
            } catch (IOException iox) {
                iox.printStackTrace();
            }
        }
        if(dtFrom.isEqual(dtTo) || dtFrom.isAfter(dtTo)) {
            try {
                errorMsg();
                return true;
            } catch(IOException iox) {
                iox.printStackTrace();
            }
        }
        return false;
    }

    public void updateGraphs() {
        if(invalidTimeFrame()) return;
        temperatureGraphs.getData().clear();
        pressureGraphs.getData().clear();
        ambientLightGraphs.getData().clear();
        User.getSensorBools().forEach(sensorBool -> {
                var id = sensorBool.getSensor().getId();
                if(sensorBool.isTemperature()) new Thread(() -> addTemperatureGraph(id)).start();
                if(sensorBool.isPressure()) new Thread(() -> addPressureGraph(id)).start();
                if(sensorBool.isAmbientLight()) new Thread(() -> addAmbientLightGraph(id)).start();
        });
    }

    private void addGraph(LineChart<String, Number> chart, XYChart.Series<String, Number> graph) {
        chart.getData().add(graph);
        var sortedTime = chart.getData().parallelStream()
                .flatMap(stringNumberSeries -> stringNumberSeries.getData().stream().map(XYChart.Data::getXValue))
                .sorted(Comparator.comparing(s -> LocalDateTime.parse(s, format)))
                .distinct()
                .collect(Collectors.toList());
        ((CategoryAxis) chart.getXAxis()).setCategories(FXCollections.observableList(sortedTime));
        var sortedGraphs = chart.getData().stream()
                .sorted(Comparator.comparing(XYChart.Series::getName))
                .collect(Collectors.toList());
        chart.setData(FXCollections.observableList(sortedGraphs));
    }

    public void addTemperatureGraph(int sensor) {
        var graph = new XYChart.Series<String, Number>();
        graph.setName(getSensorNameFromId(sensor));
        Parser.getTemperatures(dtFrom, dtTo, sensor).parallelStream()
                .map(reading -> new XYChart.Data<String, Number>(reading.getDate().format(format), reading.getTemperature()))
                .collect(Collectors.toList())
                .forEach(graph.getData()::add);
        Platform.runLater(() -> addGraph(temperatureGraphs, graph));
    }

    public void addPressureGraph(int sensor) {
        var graph = new XYChart.Series<String, Number>();
        graph.setName(getSensorNameFromId(sensor));
        Parser.getPressures(dtFrom, dtTo, sensor).parallelStream()
                .map(reading -> new XYChart.Data<String, Number>(reading.getDate().format(format), reading.getB_pressure()))
                .collect(Collectors.toList())
                .forEach(graph.getData()::add);
        Platform.runLater(() -> addGraph(pressureGraphs, graph));
    }

    public void addAmbientLightGraph(int sensor) {
        var graph = new XYChart.Series<String, Number>();
        graph.setName(getSensorNameFromId(sensor));
        Parser.getAmbientLights(dtFrom, dtTo, sensor).parallelStream()
                .map(reading -> new XYChart.Data<String, Number>(reading.getDate().format(format), reading.getAmbient_light()))
                .collect(Collectors.toList())
                .forEach(graph.getData()::add);
        Platform.runLater(() -> addGraph(ambientLightGraphs, graph));
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

    public void exitMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader ( getClass ().getResource ( "ExitWnd.fxml" ) );
        Parent root2 = fxmlLoader.load ();
        Stage stage = new Stage ();
        stage.setResizable(false);
        stage.setTitle ( "Good Bye!" );
        stage.setScene ( new Scene ( root2 ) );
        stage.show ();
    }

    public void openAvg() throws IOException {
        if(invalidTimeFrame()) return;
        User.setTimeFrame(dtFrom, dtTo);
        FXMLLoader fxmlLoader = new FXMLLoader (getClass ().getResource ("averageTable.fxml"));
        Parent root1 = fxmlLoader.load ();
        Stage stage = new Stage ();
        stage.setResizable(false);
        stage.setTitle ( "Average Window" );
        stage.setScene ( new Scene ( root1 ) );
        stage.show ();
    }

    public void errorMsg() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader ( getClass ().getResource ( "MsgError.fxml" ) );
        Parent root3 = fxmlLoader.load ();
        Stage stage = new Stage ();
        stage.setTitle ( "Time Interval Error!" );
        stage.setScene ( new Scene ( root3 ) );
        stage.show ();
    }

    public void shCredentials() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader ( getClass ().getResource ( "Credentials.fxml" ) );
        Parent root2 = fxmlLoader.load ();
        Stage stage = new Stage ();
        stage.setTitle ( "CREDENTIALS" );
        stage.setScene ( new Scene ( root2 ) );
        stage.show ();
    }

    public void openConfig() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader (getClass ().getResource ( "config.fxml" ));
        Parent root1 = fxmlLoader.load ();
        Stage stage = new Stage ();
        stage.setTitle ( "Configuration Window" );
        stage.setScene ( new Scene ( root1 ) );
        stage.show ();
    }
}
