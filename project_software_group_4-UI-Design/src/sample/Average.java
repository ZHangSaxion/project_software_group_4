package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Average {
    @FXML
    public TableView<Reading> tableView; // The table.
    @FXML
    public TableColumn<Reading, String> sensor; // The sensor column.
    @FXML
    public TableColumn<Reading, Double> temp; // The temperature column.
    @FXML
    public TableColumn<Reading, Integer> light; // The ambient light column.
    @FXML
    public TableColumn<Reading, Double> pressure; // The pressure column.

    public void initialize() {
        // Associate the data with the table columns.
        sensor.setCellValueFactory(new PropertyValueFactory<>("Sensor"));
        temp.setCellValueFactory(new PropertyValueFactory<>("Temp"));
        light.setCellValueFactory(new PropertyValueFactory<>("Light"));
        pressure.setCellValueFactory(new PropertyValueFactory<>("Pressure"));
        // Adds the data to the tableView.
        //tableView.setItems(); //TODO parser stuff
    }
}