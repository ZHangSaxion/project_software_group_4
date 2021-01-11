package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WeatherGUI {
    @FXML
    private LineChart<String, Number> temperatureGraphs;
    @FXML
    private LineChart<String, Number> pressureGraphs;
    @FXML
    private LineChart<String, Number> ambientLightGraphs;

    private List<Sensor> sensors;
    private List<Reading> readings;

    private ObservableList<LocalTime> listFrom = FXCollections.observableArrayList ();
    private ObservableList<LocalTime> listTo = FXCollections.observableArrayList ();

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
        dateTo.setValue ( LocalDate.now () );
        dateFrom.setValue ( LocalDate.now () );

        for (int i = 0; i < 24; i++) {
            for (int j = 0; j <= 55; j += 5) {
                String th = String.format ( "%02d", i ) + ":" + String.format ( "%02d", j ) + ":" + String.format ( "%02d", 0 );
                listFrom.add ( LocalTime.parse ( th ) );
                listTo.add ( LocalTime.parse ( th ) );
            }
        }
        timeFrom.setItems ( listFrom );
        timeFrom.setValue ( listFrom.get ( 0 ) );
        timeTo.setItems ( listTo );
        DateTimeFormatter myFormatTime = DateTimeFormatter.ofPattern ( "HH:mm" );
        String th = LocalTime.now ().format ( myFormatTime );
        timeTo.setValue ( LocalTime.parse ( th ) );
    }

    public void updateGraphs() {
        sensors.stream()
                .map(Sensor::getId)
                .forEach(id -> {
                    addTemperatureGraph(id);
                    addPressureGraph(id);
                    addAmbientLightGraph(id);
                });
        User.getSensorBools().forEach(System.out::println);
    }

    private String getSensorNameFromId(int id) {
        return sensors.stream()
                .filter(sensor -> sensor.getId() == id)
                .map(Sensor::getLocation)
                .findAny()
                .orElse("");
    }

    public void addTemperatureGraph(int sensor) {
        var graph = new XYChart.Series<String, Number>();
        var format = DateTimeFormatter.ofPattern("d MMMM uuuu H:mm:ss");
        graph.setName(getSensorNameFromId(sensor));
        readings.stream()
                .filter(reading -> reading.getSensor_id() == sensor)
                .map(reading -> new XYChart.Data<String, Number>(reading.getDate().format(format), reading.getTemperature()))
                .forEach(reading -> graph.getData().add(reading));
        temperatureGraphs.getData().add(graph);
    }

    public void addPressureGraph(int sensor) {
        var graph = new XYChart.Series<String, Number>();
        var format = DateTimeFormatter.ofPattern("d MMMM uuuu H:mm:ss");
        graph.setName(getSensorNameFromId(sensor));
        readings.stream()
                .filter(reading -> reading.getSensor_id() == sensor)
                .map(reading -> new XYChart.Data<String, Number>(reading.getDate().format(format), reading.getA_pressure()))
                .forEach(reading -> graph.getData().add(reading));
        pressureGraphs.getData().add(graph);
    }

    public void addAmbientLightGraph(int sensor) {
        var graph = new XYChart.Series<String, Number>();
        var format = DateTimeFormatter.ofPattern("d MMMM uuuu H:mm:ss");
        graph.setName(getSensorNameFromId(sensor));
        readings.stream()
                .filter(reading -> reading.getSensor_id() == sensor)
                .map(reading -> new XYChart.Data<String, Number>(reading.getDate().format(format), reading.getAmbient_light()))
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
        FXMLLoader fxmlLoader = new FXMLLoader (getClass ().getResource ( "average.fxml" ));
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

    //function lastH
    @FXML//fx:id="lastH"
    public void setLastH() {
        setLastTime ();
        timeFrom.setValue ( (timeTo.getValue ().minusHours ( 1 )) );
        dateFrom.setValue ( LocalDate.now () );
    }

    //function lastD
    @FXML//fx:id="lastD"
    public void setLastD() {
        setLastTime ();
        dateFrom.setValue ( LocalDate.now ().minusDays ( 1 ) );
    }

    //function lastW
    @FXML//fx:id="lastW"
    public void setLastW() {
        setLastTime ();
        dateFrom.setValue ( LocalDate.now ().minusDays ( 7 ) );
    }

    //function lastM
    @FXML//fx:id="lastM"
    public void setLastM() {
        setLastTime ();
        dateFrom.setValue ( LocalDate.now ().minusDays ( 30 ) );

    }

    // function to set the last time requested - only common instructions
    private void setLastTime() {
        LocalTime t = LocalTime.now ();
        DateTimeFormatter myFormatTime = DateTimeFormatter.ofPattern ( "HH:mm" );
        String th = t.format ( myFormatTime );
        timeTo.setValue ( LocalTime.parse ( th ) );
        timeFrom.setValue ( timeTo.getValue () );
        dateTo.setValue ( LocalDate.now () );
    }


    @FXML//fx:id="timeFrom"
    private ComboBox<LocalTime> timeFrom;

    @FXML//fx:id="timeTo"
    private ComboBox<LocalTime> timeTo;

    @FXML//fx:id="dateFrom"
    private DatePicker dateFrom;

    @FXML//fx:id="dateTo"
    private DatePicker dateTo;

    //function to get the time interval for sensor data extraction
    public void setTimeInterval() throws ParseException, IOException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern ( "yyyy-MM-dd HH:mm" );

        LocalDateTime begin, end;
        begin = LocalDateTime.parse ( dateFrom.getValue () + " " + timeFrom.getValue (), formatter );
        end = LocalDateTime.parse ( dateTo.getValue () + " " + timeTo.getValue (), formatter );

        if (end.isBefore (LocalDateTime.now ()) && begin.isBefore ( end.minusMinutes ( 5 ) ) ){
            //print check - to be cut after implementation

            for (String s : Arrays.asList ( "The time interval for graphical representation is:\n",
                    "Start Date:" + begin + "\n",
                    "Finish Date:" + end + "\n" )) {
                System.out.println ( s );
            }
            //todo insert function to get the right time parameters
            //Parser.getReadings(LocalDateTime begin, LocalDateTime end);
        } else {
            //show error message for time/date correction
            errorMsg ();
        }
    }
    //show credentials window
    public void shCredentials() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader ( getClass ().getResource ( "Credentials.fxml" ) );
        Parent root2 = fxmlLoader.load ();
        Stage stage = new Stage ();
        stage.setTitle ( "CREDENTIALS" );
        stage.setScene ( new Scene ( root2 ) );
        stage.show ();
    }
    //function to be used ONLY for automate setting the time if timeTo or timeFrom is above the current time now()
    private String resetTime(LocalDateTime reset) {
        LocalTime t = LocalTime.now ();
        DateTimeFormatter myFormatTime = DateTimeFormatter.ofPattern ( "HH:mm" );
        String th = t.format ( myFormatTime );
        if (String.valueOf ( reset ).equals ( "begin" )) {
            timeFrom.setValue ( LocalTime.parse ( th ).minusMinutes ( 10 ) );
            return dateFrom.getValue () + " " + timeFrom.getValue ();
        } else {
            timeTo.setValue ( LocalTime.parse ( th ) );
            return dateTo.getValue () + " " + timeTo.getValue ();
        }
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
