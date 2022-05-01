package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.coursework.cassandraambulance.DBConnector;
import org.coursework.cassandraambulance.models.EmergencyCall;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class GetCallByAddressController extends Controller {
    @FXML
    private TableView<EmergencyCall> dataTable;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField timeTextField;
    @FXML
    private TextField thoroughfareTextField;
    @FXML
    private TextField localityTextField;
    @FXML
    private TextField subPremiseTextField;
    @FXML
    private TextField premiseTextField;

    private String localityToSearch, thoroughfareToSearch, premiseToSearch, subPremiseToSearch;
    private LocalDate dateToSearch;
    private LocalTime timeToSearch;

    public void GetCallsByAddress(ActionEvent event) {

        GetSearchValues();

//        select * from call_by_date WHERE a_locality = 'Запоріжжя' AND a_thoroughfare = 'Нова' ALLOW FILTERING ;
        ResultSet rs;
        if(localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty() && premiseToSearch.isEmpty() && subPremiseToSearch.isEmpty() && dateToSearch == null && timeToSearch == null){
            final String getCalls = "SELECT * FROM ambulance_ver3.call_by_address LIMIT 100";
            rs = DBConnector.getSession().execute(getCalls);
        } else if (thoroughfareToSearch.isEmpty() && premiseToSearch.isEmpty() && subPremiseToSearch.isEmpty() && dateToSearch == null && timeToSearch == null) {
            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
                    "SELECT * FROM ambulance_ver3.call_by_address WHERE a_locality = ?"
            );
            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (premiseToSearch.isEmpty() && subPremiseToSearch.isEmpty() && dateToSearch == null && timeToSearch == null){
            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
                    "SELECT * FROM ambulance_ver3.call_by_address WHERE a_locality = ? AND a_thoroughfare = '' LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch, thoroughfareToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (subPremiseToSearch.isEmpty() && dateToSearch == null && timeToSearch == null) {
            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
                    "SELECT * FROM ambulance_ver3.call_by_address WHERE a_locality = ? AND a_thoroughfare = '' AND a_premise = '' LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch, thoroughfareToSearch, premiseToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (dateToSearch == null && timeToSearch == null) {
            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
                    "SELECT * FROM ambulance_ver3.call_by_address WHERE a_locality = ? AND a_thoroughfare = '' AND a_premise = '' and a_sub_premise = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch, thoroughfareToSearch, premiseToSearch, subPremiseToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (timeToSearch == null) {
            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
                    "SELECT * FROM ambulance_ver3.call_by_address WHERE a_locality = ? AND a_thoroughfare = '' AND a_premise = '' AND a_sub_premise = ? AND date = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch, thoroughfareToSearch, premiseToSearch, subPremiseToSearch, dateToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        }  else {
            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
                    "SELECT * FROM ambulance_ver3.call_by_address WHERE a_locality = ? AND a_thoroughfare = '' AND a_premise = '' AND a_sub_premise = ? AND date = ? AND time = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch, thoroughfareToSearch, premiseToSearch, subPremiseToSearch, dateToSearch, timeToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        }




    }

    public void GetSearchDate(ActionEvent event) {
    }

    protected void GetSearchValues(){
        try {
            localityToSearch = localityTextField.getText();
            thoroughfareToSearch = thoroughfareTextField.getText();
            premiseToSearch = premiseTextField.getText();
            subPremiseToSearch = subPremiseTextField.getText();
            dateToSearch = datePicker.getValue();
            timeToSearch = LocalTime.parse(timeTextField.getText());
        } catch (DateTimeParseException e) {
            System.out.println("[Error] " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Date: " + dateToSearch);
        System.out.println("Time: " + timeToSearch);
        System.out.println("Locality: " + localityToSearch);
        System.out.println("Thoroughfare: " + thoroughfareToSearch);
        System.out.println("Premise: " + premiseToSearch);
        System.out.println("Sub premise: " + subPremiseToSearch);

    }


}
