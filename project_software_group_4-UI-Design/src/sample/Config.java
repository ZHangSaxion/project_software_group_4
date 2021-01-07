package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class Config {

    @FXML
    public CheckBox temperature;
    @FXML
    public CheckBox pressure;
    @FXML
    public CheckBox ambientLight;
    @FXML
    public ChoiceBox<String> choiceBox;
    @FXML
    public VBox sensorList;

    private List<Integer> temperatureSensors;
    private List<Integer> pressureSensors;
    private List<Integer> ambientLightSensors;

    private List<Sensor> sensors;
//    private final ObservableSet<CheckBox> selectedCheckBoxes = FXCollections.observableSet();
//    private final ObservableSet<CheckBox> unselectedCheckBoxes = FXCollections.observableSet();
//
//    private final int maxNumSelected = 3;
//    private final IntegerBinding numCheckBoxesSelected = Bindings.size(selectedCheckBoxes);

    public void initialize() {
        var parser = new Parser();
        sensors = parser.getSensors();
        temperature.setDisable(true);
        pressure.setDisable(true);
        ambientLight.setDisable(true);
//        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
//            if(observableValue == null) {
//                temperature.setDisable(true);
//                pressure.setDisable(true);
//                ambientLight.setDisable(true);
//            } else {
//                temperature.setDisable(false);
//                pressure.setDisable(false);
//                ambientLight.setDisable(false);
//                temperature.setSelected(
//                        WeatherGUI.temperatureGraphs.getData().stream()
//                                .anyMatch(graph ->
//                                        sensors.get(Integer.parseInt(graph.getName()))
//                                                .getLocation().equals(choiceBox.getValue())
//                                )
//                );
//                pressure.setSelected(
//                        WeatherGUI.pressureGraphs.getData().stream()
//                                .anyMatch(graph ->
//                                        sensors.get(Integer.parseInt(graph.getName()))
//                                                .getLocation().equals(choiceBox.getValue()))
//                );
//                ambientLight.setSelected(
//                        WeatherGUI.ambientLightGraphs.getData().stream()
//                                .anyMatch(graph ->
//                                        sensors.get(Integer.parseInt(graph.getName()))
//                                                .getLocation().equals(choiceBox.getValue()))
//                );
//            }
//        });
        sensorList.getChildren().addAll(
                sensors.stream()
                .map(sensor -> {
                    var chk = new CheckBox(sensor.getLocation());
                    chk.resize(120, 45);
                    chk.setPadding(new Insets(10));
                    chk.setOnAction(this::updateChoiceBox);
                    return chk;
                })
                .collect(Collectors.toList())
        );
//        sensorList.getChildren().forEach(checkBox -> configureCheckBox((CheckBox) checkBox));

//        numCheckBoxesSelected.addListener((obs, oldSelectedCount, newSelectedCount) -> {
//            if (newSelectedCount.intValue() >= maxNumSelected) {
//                unselectedCheckBoxes.forEach(cb -> cb.setDisable(true));
//            }
//            else {
//                unselectedCheckBoxes.forEach(cb -> cb.setDisable(false));
//            }
//        });
    }

    public void updateChoiceBox(ActionEvent e) {
        var chk = (CheckBox) e.getSource();
        System.out.println("Action performed on checkbox " + chk.getText());
        System.out.println("manageChoiceBox");
        if (!choiceBox.getItems().contains(chk.getText())) {
            choiceBox.getItems().add(chk.getText());
        } else {
            choiceBox.getItems().remove(chk.getText());
        }
    }

//    public void updateTemperatureGraphs(ActionEvent e) {
//        var chk = (CheckBox) e.getSource();
//        var sensor = sensors.stream()
//                .filter(sensor1 -> sensor1.getLocation().equals(choiceBox.getValue()))
//                .collect(Collectors.toList())
//                .get(0)
//                .getId();
//        if(chk.isSelected()) {
//            WeatherGUI.addTemperatureGraph(sensor);
//        } else {
//            WeatherGUI.removeTemperatureGraph(sensor);
//        }
//        System.out.println("Action performed on checkbox: " + chk.getText());
//        if(choiceBox.getValue() != null) {
//            choiceBox.getItems().forEach(item -> {
//                if(choiceBox.getValue().matches(item)) {
//                    System.out.println("Worked for: " + item);
//                }
//            });
//        }
//        if (choiceBox.getValue().matches(choiceBox.getItems().get(0))) {
//            System.out.println("Worked for: " + choiceBox.getItems().get(0));
//        }
//        else if (choiceBox.getValue().matches(choiceBox.getItems().get(1))) {
//            System.out.println("Worked for: " + choiceBox.getItems().get(1));
//        }
//        else if (choiceBox.getValue().matches(choiceBox.getItems().get(2))) {
//            System.out.println("Worked for: " + choiceBox.getItems().get(2));
//        }
//    }

//    public void updatePressureGraphs(ActionEvent e) {
//        var chk = (CheckBox) e.getSource();
//        var sensor = sensors.stream()
//                .filter(sensor1 -> sensor1.getLocation().equals(choiceBox.getValue()))
//                .collect(Collectors.toList())
//                .get(0)
//                .getId();
//        if(chk.isSelected()) {
//            WeatherGUI.addPressureGraph(sensor);
//        } else {
//            WeatherGUI.removePressureGraph(sensor);
//        }
//    }
//
//    public void updateAmbientLightGraphs(ActionEvent e) {
//        var chk = (CheckBox) e.getSource();
//        var sensor = sensors.stream()
//                .filter(sensor1 -> sensor1.getLocation().equals(choiceBox.getValue()))
//                .collect(Collectors.toList())
//                .get(0)
//                .getId();
//        if(chk.isSelected()) {
//            WeatherGUI.addAmbientLightGraph(sensor);
//        } else {
//            WeatherGUI.removeAmbientLightGraph(sensor);
//        }
//    }

//    public void configureCheckBox(CheckBox checkBox) {
//        if (checkBox.isSelected()) {
//            selectedCheckBoxes.add(checkBox);
//        }
//        else {
//            unselectedCheckBoxes.add(checkBox);
//        }
//
//        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
//            if (isNowSelected) {
//                unselectedCheckBoxes.remove(checkBox);
//                selectedCheckBoxes.add(checkBox);
//            }
//            else {
//                selectedCheckBoxes.remove(checkBox);
//                unselectedCheckBoxes.add(checkBox);
//            }
//        });
//    }

//    public void updateSensorLists(ActionEvent e) {
//        temperatureSensors = WeatherGUI.temperatureGraphs.getData().stream()
//                .map(graph ->
//                        sensors.stream()
//                                .filter(sensor -> sensor.getLocation().equals(graph.getName()))
//                                .collect(Collectors.toList())
//                                .get(0)
//                                .getId()
//                )
//                .collect(Collectors.toList());
//        pressureSensors = WeatherGUI.pressureGraphs.getData().stream()
//                .map(graph ->
//                        sensors.stream()
//                                .filter(sensor -> sensor.getLocation().equals(graph.getName()))
//                                .collect(Collectors.toList())
//                                .get(0)
//                                .getId()
//                )
//                .collect(Collectors.toList());
//        ambientLightSensors = WeatherGUI.ambientLightGraphs.getData().stream()
//                .map(graph ->
//                        sensors.stream()
//                                .filter(sensor -> sensor.getLocation().equals(graph.getName()))
//                                .collect(Collectors.toList())
//                                .get(0)
//                                .getId()
//                )
//                .collect(Collectors.toList());
//        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
//    }
//
//    public void returnBack(ActionEvent e) {
//        WeatherGUI.temperatureGraphs.getData().removeIf(graph ->
//            !temperatureSensors.contains(Integer.valueOf(graph.getName()))
//        );
//        WeatherGUI.pressureGraphs.getData().removeIf(graph ->
//            !pressureSensors.contains(Integer.valueOf(graph.getName()))
//        );
//        WeatherGUI.ambientLightGraphs.getData().removeIf(graph ->
//            !ambientLightSensors.contains(Integer.valueOf(graph.getName()))
//        );
//        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
//    }



}
