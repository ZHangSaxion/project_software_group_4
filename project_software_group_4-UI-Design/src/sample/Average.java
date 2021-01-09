package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
public class Average {
    @FXML
    public TableView<Readings> tableView; // The table.
    @FXML
    public TableColumn<Readings, String> sensor; // The sensor column.
    @FXML
    public TableColumn<Readings, Double> temp; // The temperature column.
    @FXML
    public TableColumn<Readings, Integer> light; // The ambient light column.
    @FXML
    public TableColumn<Readings, Double> pressure; // The pressure column.

//    // The data that will be added to the tableView.
//    private final ObservableList<Readings> data = FXCollections.observableArrayList(
//            new Readings("Sensor 1", 12.35 + " c°", 34 + " %", 1016 + " hPa"),
//            new Readings("Sensor 2", 15.70 + " c°", 39 + " %", 1016 + " hPa"),
//            new Readings("Sensor 3", 11.23 + " c°", 70 + " %", 1017 + " hPa"),
//            new Readings("Sensor 4", 13.45 + " c°", 68 + " %", 1016 + " hPa")
//    );
//
//    public void initialize() {
//        // Associate the data with the table columns.
//        sensor.setCellValueFactory(new PropertyValueFactory<>("Sensor"));
//        temp.setCellValueFactory(new PropertyValueFactory<>("Temp"));
//        light.setCellValueFactory(new PropertyValueFactory<>("Light"));
//        pressure.setCellValueFactory(new PropertyValueFactory<>("Pressure"));
//        // Adds the data to the tableView.
//        tableView.setItems(data);
//    }
}