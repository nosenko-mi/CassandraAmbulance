package org.coursework.cassandraambulance.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.coursework.cassandraambulance.models.EmergencyCall;
import org.coursework.cassandraambulance.tables.EmergencyCallTable;

public class GetCallByAddressController extends Controller {
    @FXML
    private TableView<EmergencyCall> dataTable;
    @FXML
    private TextField thoroughfareTextField;
    @FXML
    private TextField localityTextField;
    @FXML
    private TextField subPremiseTextField;
    @FXML
    private TextField premiseTextField;

    private String localityToSearch, thoroughfareToSearch, premiseToSearch, subPremiseToSearch;


    public void GetCallByAddress(ActionEvent event) {

        GetSearchValues();

        EmergencyCallTable.GetByAddress(dataTable, localityToSearch, thoroughfareToSearch, premiseToSearch, subPremiseToSearch);

    }

    protected void GetSearchValues(){
        try {
            localityToSearch = localityTextField.getText();
            thoroughfareToSearch = thoroughfareTextField.getText();
            premiseToSearch = premiseTextField.getText();
            subPremiseToSearch = subPremiseTextField.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Locality: " + localityToSearch);
        System.out.println("Thoroughfare: " + thoroughfareToSearch);
        System.out.println("Premise: " + premiseToSearch);
        System.out.println("Sub premise: " + subPremiseToSearch);

    }

    public void initialize(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Select information");
        alert.setHeaderText("Selecting by Full address may reduce the speed of the program");
        alert.showAndWait();
    }


}
