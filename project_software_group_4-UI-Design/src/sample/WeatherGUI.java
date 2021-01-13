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
import java.util.Comparator;
import java.util.stream.Collectors;

public class WeatherGUI {
    /** LineChart where the temperature graphs will be plotted */
    @FXML
    private LineChart<String, Number> temperatureGraphs;

    /** LineChart where the pressure graphs will be plotted */
    @FXML
    private LineChart<String, Number> pressureGraphs;

    /** LineChart where the ambient light graphs will be plotted */
    @FXML
    private LineChart<String, Number> ambientLightGraphs;

    /** DatePicker to configure and retrieve the begin date */
    @FXML
    private DatePicker dateFrom;

    /** DatePicker to configure and retrieve the end date */
    @FXML
    private DatePicker dateTo;

    /** dropdown box to configure and retrieve the begin time */
    @FXML
    private ComboBox<String> timeFrom;

    /** dropdown box to configure and retrieve the end time */
    @FXML
    private ComboBox<String> timeTo;

    /** format used to convert between LocalDateTime and String */
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("d MMM uuuu HH:mm");

    /** variable used to store the begin DateTime */
    private LocalDateTime dtFrom;

    /** variable used to store the end DateTime */
    private LocalDateTime dtTo;

    /**
     * initializes the datepickers to not be able to select days in the future and fills the time dropdown boxes,
     * also configures them with default values, in this case the current date at midnight
     */
    public void initialize() {
        dateTo.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) > 0);
            }
        });
        dateFrom.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) > 0);
            }
        });
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

    /**
     * short function to retrieve the name/location of a sensor with it's id
     *
     * @param id a unique sensor id
     * @return the name/location
     */
    private String getSensorNameFromId(int id) {
        return User.getSensorBools().stream()
                .map(SensorBool::getSensor)
                .filter(sensor -> sensor.getId() == id)
                .map(Sensor::getLocation)
                .findAny()
                .orElse("");
    }

    /**
     * function to check if the currently configured date and time values are valid,
     * if not an error message is shown
     *
     * @return true if the datetime is not valid, false otherwise
     */
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

    /**
     * if the currently configured datetime is valid, will remove previous graphs from the charts
     * and then proceed to make new ones according to the configurations stored in User.getSensorBools().
     * for each sensor in each graph there is one api call which are all managed by an individual thread.
     * this function is executed when the START button is pressed
     */
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

    /**
     * because javaFX does not allow updating FXML elements in other threads this function is added to
     * Platform.runLater(Runnable runnable) after every api call
     *
     * @param chart the LineChart graph has to be added to
     * @param graph the XYChart.Series that has to be added to chart
     */
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

    /**
     * function that calls the api to retrieve temperature readings produced by a specific sensor,
     * it converts these to XYChart.Data objects which in turn are stored in an XYChart.Series object
     * and sent to addGraph() together with the corresponding LineChart
     *
     * @param sensor the unique id of the specific sensor
     */
    public void addTemperatureGraph(int sensor) {
        var graph = new XYChart.Series<String, Number>();
        graph.setName(getSensorNameFromId(sensor));
        Parser.getTemperatures(dtFrom, dtTo, sensor).parallelStream()
                .map(reading -> new XYChart.Data<String, Number>(reading.getDate().format(format), reading.getTemperature()))
                .collect(Collectors.toList())
                .forEach(graph.getData()::add);
        Platform.runLater(() -> addGraph(temperatureGraphs, graph));
    }

    /**
     * function that calls the api to retrieve pressure readings produced by a specific sensor,
     * it converts these to XYChart.Data objects which in turn are stored in an XYChart.Series object
     * and sent to addGraph() together with the corresponding LineChart
     *
     * @param sensor the unique id of the specific sensor
     */
    public void addPressureGraph(int sensor) {
        var graph = new XYChart.Series<String, Number>();
        graph.setName(getSensorNameFromId(sensor));
        Parser.getPressures(dtFrom, dtTo, sensor).parallelStream()
                .map(reading -> new XYChart.Data<String, Number>(reading.getDate().format(format), reading.getB_pressure()))
                .collect(Collectors.toList())
                .forEach(graph.getData()::add);
        Platform.runLater(() -> addGraph(pressureGraphs, graph));
    }

    /**
     * function that calls the api to retrieve ambient light readings produced by a specific sensor,
     * it converts these to XYChart.Data objects which in turn are stored in an XYChart.Series object
     * and sent to addGraph() together with the corresponding LineChart
     *
     * @param sensor the unique id of the specific sensor
     */
    public void addAmbientLightGraph(int sensor) {
        var graph = new XYChart.Series<String, Number>();
        graph.setName(getSensorNameFromId(sensor));
        Parser.getAmbientLights(dtFrom, dtTo, sensor).parallelStream()
                .map(reading -> new XYChart.Data<String, Number>(reading.getDate().format(format), reading.getAmbient_light()))
                .collect(Collectors.toList())
                .forEach(graph.getData()::add);
        Platform.runLater(() -> addGraph(ambientLightGraphs, graph));
    }

    /**
     * function to automatically adjust the date and time values stored in the fxml elements
     *
     * @param amount the offset from LocalDateTime.now()
     */
    private void setLastX(TemporalAmount amount) {
        var now = LocalDateTime.now();
        dateTo.setValue(now.toLocalDate());
        timeTo.setValue(now.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        var nowMinusX = now.minus(amount);
        dateFrom.setValue(nowMinusX.toLocalDate());
        timeFrom.setValue(nowMinusX.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    /**
     * calls setLastX() with an offset of one hour
     */
    public void setLastH() {
        setLastX(Duration.ofHours(1));
    }

    /**
     * calls setLastX() with an offset of one day
     */
    public void setLastD() {
        setLastX(Period.ofDays(1));
    }

    /**
     * calls setLastX() with an offset of one week
     */
    public void setLastW() {
        setLastX(Period.ofWeeks(1));
    }

    /**
     * calls setLastX() with an offset of one month
     */
    public void setLastM() {
        setLastX(Period.ofMonths(1));
    }

    /**
     * shows the exit menu
     *
     * @throws IOException if something goes wrong you will know
     */
    public void exitMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader ( getClass ().getResource ( "ExitWnd.fxml" ) );
        Parent root2 = fxmlLoader.load ();
        Stage stage = new Stage ();
        stage.setResizable(false);
        stage.setTitle ( "Good Bye!" );
        stage.setScene ( new Scene ( root2 ) );
        stage.show ();
    }

    /**
     * shows the average table
     *
     * @throws IOException if something goes wrong you will know
     */
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

    /**
     * shows an error message stating that the time interval is not correct
     *
     * @throws IOException if something goes wrong you will know
     */
    public void errorMsg() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader ( getClass ().getResource ( "MsgError.fxml" ) );
        Parent root3 = fxmlLoader.load ();
        Stage stage = new Stage ();
        stage.setTitle ( "Time Interval Error!" );
        stage.setScene ( new Scene ( root3 ) );
        stage.show ();
    }

    /**
     * shows credentials
     *
     * @throws Exception if something goes wrong you will know
     */
    public void shCredentials() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader ( getClass ().getResource ( "Credentials.fxml" ) );
        Parent root2 = fxmlLoader.load ();
        Stage stage = new Stage ();
        stage.setTitle ( "CREDENTIALS" );
        stage.setScene ( new Scene ( root2 ) );
        stage.show ();
    }

    /**
     * shows the config menu
     *
     * @throws IOException if something goes wrong you will know
     */
    public void openConfig() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader (getClass ().getResource ( "config.fxml" ));
        Parent root1 = fxmlLoader.load ();
        Stage stage = new Stage ();
        stage.setTitle ( "Configuration Window" );
        stage.setScene ( new Scene ( root1 ) );
        stage.show ();
    }
}
