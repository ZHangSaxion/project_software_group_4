package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Comparator;

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
     * initialization, the average table doesn't actually need to do more than storing the averages of
     * that specific moment so this is the only function
     */
    public void initialize() {
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
        sensor.setCellValueFactory(new PropertyValueFactory<>("sensor_id"));
        temp.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        pressure.setCellValueFactory(new PropertyValueFactory<>("b_pressure"));
        light.setCellValueFactory(new PropertyValueFactory<>("ambient_light"));
        tableView.setItems(FXCollections.observableList(averages));
    }
}