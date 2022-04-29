package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import eu.hansolo.tilesfx.events.SwitchEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.coursework.cassandraambulance.DBConnector;
import org.coursework.cassandraambulance.TableUtils;
import org.coursework.cassandraambulance.ViewSwitcher;
import org.coursework.cassandraambulance.models.EmergencyCall;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.UUID;

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

        TableColumn<EmergencyCall, UUID> idCol = new TableColumn<EmergencyCall, UUID>("id");
        TableColumn<EmergencyCall, UUID> unitIdCol = new TableColumn<EmergencyCall, UUID>("unitId");
        TableColumn<EmergencyCall, String> dateCol = new TableColumn<EmergencyCall, String>("date");
        TableColumn<EmergencyCall, String> timeCol = new TableColumn<EmergencyCall, String>("time");
        TableColumn<EmergencyCall, String> localityCol = new TableColumn<EmergencyCall, String>("locality");
        TableColumn<EmergencyCall, String> thoroughfareCol = new TableColumn<EmergencyCall, String>("thoroughfareCol");
        TableColumn<EmergencyCall, String> premiseCol = new TableColumn<EmergencyCall, String>("premiseCol");
        TableColumn<EmergencyCall, String> subPremiseCol = new TableColumn<EmergencyCall, String>("subPremiseCol");
        TableColumn<EmergencyCall, String> causeCol = new TableColumn<EmergencyCall, String>("causeCol");
        TableColumn<EmergencyCall, UUID> callerIdCol = new TableColumn<EmergencyCall, UUID>("callerId");

        GetSearchValues();

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

        ObservableList<EmergencyCall> callObservableList = FXCollections.observableArrayList();

        for (Row row : rs){
            callObservableList
                    .add(new EmergencyCall(
                            row.getString("a_locality"), row.getString("a_thoroughfare"), row.getString("a_premise"),
                            row.getString("a_sub_premise"), row.getString("cause"), row.getLocalDate("date"),
                            row.getLocalTime("time"),
                            row.getUuid("caller_id"), row.getUuid("id"), row.getUuid("unit_id")
                    ));

        }

        dataTable.getColumns().clear();

        idCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, UUID>("id"));
        unitIdCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, UUID>("unitId"));
        dateCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, String>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, String>("time"));
        localityCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, String>("locality"));
        thoroughfareCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, String>("thoroughfare"));
        premiseCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, String>("premise"));
        subPremiseCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, String>("subPremise"));
        causeCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, String>("cause"));
        callerIdCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, UUID>("callerId"));

        dataTable.setItems(callObservableList);
        dataTable.getColumns().addAll(idCol, unitIdCol, dateCol, timeCol, localityCol, thoroughfareCol, premiseCol, subPremiseCol, causeCol, callerIdCol);

        dataTable.getSelectionModel().setCellSelectionEnabled(true);
        dataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(dataTable);

    }

    public void GetSearchDate(ActionEvent event) {
    }

    public void InitTableColumns(){

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
