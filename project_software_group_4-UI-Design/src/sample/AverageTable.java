package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AverageTable {
    /** the table to store the average data in */
    @FXML
    public TableView<Average> tableView;

    /** column containing the sensor id's */
    @FXML
    public TableColumn<Average, String> sensor;

    /** column containing the average temperatures */
    @FXML
    public TableColumn<Average, Double> temp;

    /** column containing the average pressures */
    @FXML
    public TableColumn<Average, Double> pressure;

    /** column containing the average ambient light percentages */
    @FXML
    public TableColumn<Average, Double> light;

    /**
     * initialization, a placeholder gets set for the table and the averages get
     * calculated in a separate thread so the user does not have to wait for the window to open
     */
    public void initialize() {
        tableView.setPlaceholder(new Text("retrieving averages..."));
        new Thread(this::initializeAverages).start();
    }

    /**
     * calls the api to get all the readings in the timeframe, then calculates averages
     * and passes those to initializeFX() when it is finished
     */
    private void initializeAverages() {
        var averages = new ArrayList<Average>();
        var readings = Parser.getReadings(User.getBegin(), User.getEnd());
        readings.stream()
                .map(Reading::getSensor_id)
                .distinct()
                .sorted(Comparator.comparing(Integer::intValue))
                .forEach(sensorId -> {
                    var temperature = readings.parallelStream()
                            .filter(reading -> reading.getSensor_id() == sensorId)
                            .mapToDouble(Reading::getTemperature)
                            .average()
                            .orElse(0);
                    var pressure = readings.parallelStream()
                            .filter(reading -> reading.getSensor_id() == sensorId)
                            .mapToDouble(Reading::getB_pressure)
                            .average()
                            .orElse(0);
                    var ambientLight = readings.parallelStream()
                            .filter(reading -> reading.getSensor_id() == sensorId)
                            .mapToDouble(Reading::getAmbient_light)
                            .average()
                            .orElse(0);
                    averages.add(new Average(sensorId, temperature, pressure, ambientLight));
                });
        Platform.runLater(() -> initializeFX(averages));
    }

    /**
     * javafx does not allow updating fxml objects in a different thread so the table has to be updated
     * separately. if the list of averages is empty, the table will say so
     *
     * @param averages the list of just calculated averages
     */
    private void initializeFX(List<Average> averages) {
        if(averages.isEmpty()) tableView.setPlaceholder(new Text("no readings found in this timeframe"));
        sensor.setCellValueFactory(new PropertyValueFactory<>("sensor_id"));
        temp.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        pressure.setCellValueFactory(new PropertyValueFactory<>("b_pressure"));
        light.setCellValueFactory(new PropertyValueFactory<>("ambient_light"));
        tableView.setItems(FXCollections.observableList(averages));
    }
}