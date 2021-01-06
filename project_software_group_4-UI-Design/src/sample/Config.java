package sample;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class Config {

    @FXML
    public CheckBox Temperature;
    @FXML
    public ChoiceBox<String> choiceBox;
    @FXML
    public CheckBox sensor1;
    @FXML
    public CheckBox sensor2;
    @FXML
    public CheckBox sensor3;
    @FXML
    public CheckBox sensor4;
    @FXML
    public CheckBox sensor5;

    private final ObservableSet<CheckBox> selectedCheckBoxes = FXCollections.observableSet();
    private final ObservableSet<CheckBox> unselectedCheckBoxes = FXCollections.observableSet();

    private final int maxNumSelected = 3;
    private final IntegerBinding numCheckBoxesSelected = Bindings.size(selectedCheckBoxes);

    public void initialize() {
        configureCheckBox(sensor1);
        configureCheckBox(sensor2);
        configureCheckBox(sensor3);
        configureCheckBox(sensor4);
        configureCheckBox(sensor5);

        numCheckBoxesSelected.addListener((obs, oldSelectedCount, newSelectedCount) -> {
            if (newSelectedCount.intValue() >= maxNumSelected) {
                unselectedCheckBoxes.forEach(cb -> cb.setDisable(true));
            }
            else {
                unselectedCheckBoxes.forEach(cb -> cb.setDisable(false));
            }
        });
    }

    public void setSens(ActionEvent e) {
        if (e.getSource() instanceof CheckBox) {
            CheckBox chk = (CheckBox) e.getSource();
            System.out.println("Action performed on checkbox " + chk.getText());
            manageChoiceBox(chk.getText());
        }
    }

    public void showTemp(ActionEvent e) {
        if (e.getSource() instanceof CheckBox) {
            CheckBox chk = (CheckBox) e.getSource();
            System.out.println("Action performed on checkbox: " + chk.getText());
            if (choiceBox.getValue().matches(choiceBox.getItems().get(0))) {
                System.out.println("Worked for: " + choiceBox.getItems().get(0));
            }
            else if (choiceBox.getValue().matches(choiceBox.getItems().get(1))) {
                System.out.println("Worked for: " + choiceBox.getItems().get(1));

            }
            else if (choiceBox.getValue().matches(choiceBox.getItems().get(2))) {
                System.out.println("Worked for: " + choiceBox.getItems().get(2));

            }
        }
    }

    public void manageChoiceBox(String s) {
        System.out.println("manageChoiceBox");
        if (!choiceBox.getItems().contains(s)) {
            choiceBox.getItems().add(s);
        }
        else {
            choiceBox.getItems().remove(s);
        }
    }

    public void configureCheckBox(CheckBox checkBox) {
        if (checkBox.isSelected()) {
            selectedCheckBoxes.add(checkBox);
        }
        else {
            unselectedCheckBoxes.add(checkBox);
        }

        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                unselectedCheckBoxes.remove(checkBox);
                selectedCheckBoxes.add(checkBox);
            }
            else {
                selectedCheckBoxes.remove(checkBox);
                unselectedCheckBoxes.add(checkBox);
            }
        });
    }

    public void returnBack(ActionEvent e ){
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
    }



}
