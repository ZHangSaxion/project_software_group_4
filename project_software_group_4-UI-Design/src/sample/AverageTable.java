package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
        var averages = Parser.getAverages(User.getBegin(), User.getEnd());
        // Associate the data with the table columns.
        sensor.setCellValueFactory(new PropertyValueFactory<>("sensor_id"));
        temp.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        light.setCellValueFactory(new PropertyValueFactory<>("b_pressure"));
        pressure.setCellValueFactory(new PropertyValueFactory<>("ambient_light"));
        // Adds the data to the tableView.
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