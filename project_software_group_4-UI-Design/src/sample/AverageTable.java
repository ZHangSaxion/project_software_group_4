package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class AverageTable {
    @FXML
    public TableView<Average> tableView;

    @FXML
    public TableColumn<Average, String> sensor; // The sensor column.

    @FXML
    public TableColumn<Average, Double> temp; // The temperature column.

    @FXML
    public TableColumn<Average, Double> pressure; // The pressure column.

    @FXML
    public TableColumn<Average, Double> light; // The ambient light column.

    public void initialize() {
        var averages = new ArrayList<Average>();
        var readings = Parser.getReadings(User.getBegin(), User.getEnd());
        readings.stream()
                .map(Reading::getSensor_id)
                .distinct()
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

    private String getSensorNameFromId(int id) {
        return User.getSensorBools().stream()
                .map(SensorBool::getSensor)
                .filter(sensor1 -> sensor1.getId() == id)
                .map(Sensor::getLocation)
                .findAny()
                .orElse("");
    }
}