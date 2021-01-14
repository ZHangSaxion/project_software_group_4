package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Config {
    /** the check box to select/deselect the temperature for the currently selected sensor(s) */
    @FXML
    public CheckBox temperature;

    /** the check box to select/deselect the pressure for the currently selected sensor(s) */
    @FXML
    public CheckBox pressure;

    /** the check box to select/deselect the ambient light for the currently selected sensor(s) */
    @FXML
    public CheckBox ambientLight;

    /** the choice box to select which sensor(s) to configure */
    @FXML
    public ChoiceBox<String> choiceBox;

    /** after the sensors have been retrieved, a button will appear for each sensor inside this list */
    @FXML
    public VBox sensorList;

    /** all sensors retrieved from the api will be stored here */
    private List<Sensor> sensors;

    /**
     * the current configuration of each sensor and changes to it will be stored in this list,
     * if the OK button is pressed it will be stored in the User class
     */
    private List<SensorBool> sensorBools;

    /**
     * initialization, default values are configured and buttons are added corresponding to sensors.
     * if the config menu has been opened before, retrieves already configured settings
     */
    public void initialize() {
        choiceBox.getItems().add("all sensors");
        choiceBox.setValue("all sensors");
        choiceBox.setOnAction(this::updateButtonsFromChoiceBox);
        sensorList.getChildren().add(new Text("retrieving sensors..."));
        new Thread(this::initializeSensorList).start();
    }

    /**
     * retrieves sensors from api and assigns each to it's own check box
     */
    private void initializeSensorList() {

        sensors = Parser.getLocations();
        sensorBools = new ArrayList<>(User.getSensorBools());
        var sensorCheckBoxes = sensors.stream()
                .map(sensor -> {
                    var chk = new CheckBox(sensor.getLocation());
                    chk.setOnAction(this::updateChoiceBox);
                    return chk;
                })
                .collect(Collectors.toList());
        Platform.runLater(() -> initializeFX(sensorCheckBoxes));
    }

    /**
     * javafx does not allow altering fxml elements outside of the main thread,
     * when the sensors are retrieved they will appear in the scroll list
     *
     * @param sensorCheckBoxes the newly created sensor buttons
     */
    private void initializeFX(List<CheckBox> sensorCheckBoxes) {
        sensorList.getChildren().addAll(sensorCheckBoxes);
        sensorList.getChildren().stream()
                .filter(node -> node instanceof CheckBox)
                .map(chk -> (CheckBox) chk)
                .forEach(checkBox -> {
                    checkBox.setSelected(sensorBools.stream()
                            .anyMatch(sensorBool -> sensorBool.getSensor().getLocation().equals(checkBox.getText())));
                    if(checkBox.isSelected()) choiceBox.getItems().add(checkBox.getText());
                });
        updateButtonsFromChoiceBox(new ActionEvent());
        sensorList.getChildren().removeIf(node -> node instanceof Text);
    }

    /**
     * short function to get a SensorBool of a sensor, this is useful in combination with the text on the checkboxes
     *
     * @param location the location/name of the sensor
     * @return the corresponding SensorBool
     */
    private SensorBool getSensorBoolWithLocation(String location) {
        return sensorBools.stream()
                .filter(sensorBool -> sensorBool.getSensor().getLocation().equals(location))
                .findAny()
                .orElse(new SensorBool(new Sensor()));
    }

    /**
     * when a sensor is selected or deselected it has to appear/disappear in the choice box,
     * it also has to be added to an external static list so that the main window will know what to display
     *
     * @param e ActionEvent to retrieve which button executed the function
     */
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

    /**
     * each time a different option is selected in the choice box
     * the already enabled options should be displayed properly according to the option selected
     *
     * @param e unused ActionEvent
     */
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

    /**
     * when the temperature box is checked/unchecked the SensorBool(s) of the currently selected sensor(s)
     * has/have to be altered to represent the desired configuration
     *
     * @param e ActionEvent to see if the button is checked or unchecked
     */
    public void updateTemperatureGraphs(ActionEvent e) {
        var chk = (CheckBox) e.getSource();
        if(choiceBox.getValue().equals("all sensors")) {
            sensorBools.forEach(sensorBool -> sensorBool.setTemperature(chk.isSelected()));
        } else {
            getSensorBoolWithLocation(choiceBox.getValue()).setTemperature(chk.isSelected());
        }
    }

    /**
     * when the pressure box is checked/unchecked the SensorBool(s) of the currently selected sensor(s)
     * has/have to be altered to represent the desired configuration
     *
     * @param e ActionEvent to see if the button is checked or unchecked
     */
    public void updatePressureGraphs(ActionEvent e) {
        var chk = (CheckBox) e.getSource();
        if(choiceBox.getValue().equals("all sensors")) {
            sensorBools.forEach(sensorBool -> sensorBool.setPressure(chk.isSelected()));
        } else {
            getSensorBoolWithLocation(choiceBox.getValue()).setPressure(chk.isSelected());
        }
    }

    /**
     * when the ambient light box is checked/unchecked the SensorBool(s) of the currently selected sensor(s)
     * has/have to be altered to represent the desired configuration
     *
     * @param e ActionEvent to see if the button is checked or unchecked
     */
    public void updateAmbientLightGraphs(ActionEvent e) {
        var chk = (CheckBox) e.getSource();
        if(choiceBox.getValue().equals("all sensors")) {
            sensorBools.forEach(sensorBool -> sensorBool.setAmbientLight(chk.isSelected()));
        } else {
            getSensorBoolWithLocation(choiceBox.getValue()).setAmbientLight(chk.isSelected());
        }
    }

    /**
     * when the OK button is pressed, the just configured configuration has to be saved and the window closed
     *
     * @param e ActionEvent to get and close the Stage of the button that executed the function
     */
    public void updateSensorBools(ActionEvent e) {
        User.setSensorBools(sensorBools);
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
    }

    /**
     * when Back is pressed, the just configured configuration has to be discarded and the window closed
     *
     * @param e ActionEvent to get and close the Stage of the button that executed the function
     */
    public void returnBack(ActionEvent e) {
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
    }
}
