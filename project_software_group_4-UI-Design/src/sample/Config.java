package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
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

    private List<Sensor> sensors;
    private List<SensorBool> sensorBools;

    public void initialize() {
        choiceBox.getItems().add("all sensors");
        choiceBox.setValue("all sensors");
        choiceBox.setOnAction(this::updateButtonsFromChoiceBox);
        sensors = Parser.getLocations();
        sensorBools = new ArrayList<>(User.getSensorBools());
        var sensorCheckBoxes = sensors.stream()
                .map(sensor -> {
                    var chk = new CheckBox(sensor.getLocation());
                    chk.resize(temperature.getWidth(), temperature.getHeight());
                    chk.setPadding(temperature.getInsets());
                    chk.setOnAction(this::updateChoiceBox);
                    return chk;
                })
                .collect(Collectors.toList());
        sensorList.getChildren().addAll(sensorCheckBoxes);
        sensorList.getChildren().stream()
                .map(chk -> (CheckBox) chk)
                .forEach(checkBox -> {
                    checkBox.setSelected(sensorBools.stream()
                            .anyMatch(sensorBool -> sensorBool.getSensor().getLocation().equals(checkBox.getText())));
                    if(checkBox.isSelected()) choiceBox.getItems().add(checkBox.getText());
                });
        updateButtonsFromChoiceBox(new ActionEvent());
    }

    private SensorBool getSensorBoolWithLocation(String location) {
        return sensorBools.stream()
                .filter(sensorBool -> sensorBool.getSensor().getLocation().equals(location))
                .findAny()
                .orElse(new SensorBool(new Sensor()));
    }

    public void updateChoiceBox(ActionEvent e) {
        var chk = (CheckBox) e.getSource();
        if (!choiceBox.getItems().contains(chk.getText()) && chk.isSelected()) {
            choiceBox.getItems().add(chk.getText());
            var sensor = sensors.stream()
                    .filter(sensor1 -> sensor1.getLocation().equals(chk.getText()))
                    .findAny()
                    .orElse(new Sensor());
            sensorBools.add(new SensorBool(sensor));
        } else {
            if(choiceBox.getValue().equals(chk.getText())) choiceBox.setValue("all sensors");
            choiceBox.getItems().remove(chk.getText());
            sensorBools.removeIf(sensorBool -> sensorBool.getSensor().getLocation().equals(chk.getText()));
        }
        updateButtonsFromChoiceBox(new ActionEvent());
    }

    public void updateButtonsFromChoiceBox(ActionEvent e) {
        if(sensorBools.isEmpty()) {
            temperature.setSelected(false);
            pressure.setSelected(false);
            ambientLight.setSelected(false);
        } else if(choiceBox.getValue().equals("all sensors")) {
            temperature.setSelected(sensorBools.stream().allMatch(SensorBool::isTemperature));
            pressure.setSelected(sensorBools.stream().allMatch(SensorBool::isPressure));
            ambientLight.setSelected(sensorBools.stream().allMatch(SensorBool::isAmbientLight));
        } else {
            temperature.setSelected(getSensorBoolWithLocation(choiceBox.getValue()).isTemperature());
            pressure.setSelected(getSensorBoolWithLocation(choiceBox.getValue()).isPressure());
            ambientLight.setSelected(getSensorBoolWithLocation(choiceBox.getValue()).isAmbientLight());
        }
    }

    public void updateTemperatureGraphs(ActionEvent e) {
        var chk = (CheckBox) e.getSource();
        if(choiceBox.getValue().equals("all sensors")) {
            sensorBools.forEach(sensorBool -> sensorBool.setTemperature(chk.isSelected()));
        } else {
            getSensorBoolWithLocation(choiceBox.getValue()).setTemperature(chk.isSelected());
        }
    }

    public void updatePressureGraphs(ActionEvent e) {
        var chk = (CheckBox) e.getSource();
        if(choiceBox.getValue().equals("all sensors")) {
            sensorBools.forEach(sensorBool -> sensorBool.setPressure(chk.isSelected()));
        } else {
            getSensorBoolWithLocation(choiceBox.getValue()).setPressure(chk.isSelected());
        }
    }

    public void updateAmbientLightGraphs(ActionEvent e) {
        var chk = (CheckBox) e.getSource();
        if(choiceBox.getValue().equals("all sensors")) {
            sensorBools.forEach(sensorBool -> sensorBool.setAmbientLight(chk.isSelected()));
        } else {
            getSensorBoolWithLocation(choiceBox.getValue()).setAmbientLight(chk.isSelected());
        }
    }

    public void updateSensorBools(ActionEvent e) {
        User.setSensorBools(sensorBools);
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
    }

    public void returnBack(ActionEvent e) {
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
    }
}
