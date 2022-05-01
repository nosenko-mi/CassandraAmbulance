package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.coursework.cassandraambulance.DBConnector;
import org.coursework.cassandraambulance.StringResources;
import org.coursework.cassandraambulance.models.EmergencyCall;
import org.coursework.cassandraambulance.tables.EmergencyCallTable;

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

    public void GetCallByAddress(ActionEvent event) {

        GetSearchValues();
        
//        select * from call_by_date WHERE a_locality = 'Запоріжжя' AND a_thoroughfare = 'Нова' ALLOW FILTERING ;
//        ResultSet rs;
//        if(localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty() && premiseToSearch.isEmpty() && subPremiseToSearch.isEmpty()){
//            final String getCalls = "SELECT * FROM  " + StringResources.CALL_BY_ADDRESS + "  LIMIT 100";
//            rs = DBConnector.getSession().execute(getCalls);
//        } else if (thoroughfareToSearch.isEmpty() && premiseToSearch.isEmpty() && subPremiseToSearch.isEmpty()) {
//            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
//                    "SELECT * FROM  " + StringResources.CALL_BY_ADDRESS + "  WHERE a_locality = ?"
//            );
//            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch);
//            rs = DBConnector.getSession().execute(boundStatement);
//        } else if (premiseToSearch.isEmpty() && subPremiseToSearch.isEmpty()){
//            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
//                    "SELECT * FROM  " + StringResources.CALL_BY_ADDRESS + "  WHERE a_locality = ? AND a_thoroughfare = '' LIMIT 100"
//            );
//            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch, thoroughfareToSearch);
//            rs = DBConnector.getSession().execute(boundStatement);
//        } else if (subPremiseToSearch.isEmpty()) {
//            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
//                    "SELECT * FROM  " + StringResources.CALL_BY_ADDRESS + "  WHERE a_locality = ? AND a_thoroughfare = '' AND a_premise = '' LIMIT 100"
//            );
//            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch, thoroughfareToSearch, premiseToSearch);
//            rs = DBConnector.getSession().execute(boundStatement);
//        } else if (!localityToSearch.isEmpty() && !thoroughfareToSearch.isEmpty() && !premiseToSearch.isEmpty() && !subPremiseToSearch.isEmpty()) {
//            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
//                    "SELECT * FROM  " + StringResources.CALL_BY_ADDRESS + "  WHERE a_locality = ? AND a_thoroughfare = '' AND a_premise = '' and a_sub_premise = ? LIMIT 100"
//            );
//            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch, thoroughfareToSearch, premiseToSearch, subPremiseToSearch);
//            rs = DBConnector.getSession().execute(boundStatement);
//        } else {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Selection error");
//            alert.setHeaderText("Possible selection options: Locality - > Thoroughfare -> Premise -> Sub premise");
//            alert.showAndWait();
//        }

        EmergencyCallTable.GetByAddress(dataTable, localityToSearch, thoroughfareToSearch, premiseToSearch, subPremiseToSearch);


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

    public void initialize(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Select information");
        alert.setHeaderText("Selecting by Address may reduce the speed of the program");
        alert.showAndWait();
    }


}
