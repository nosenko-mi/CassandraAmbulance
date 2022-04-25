package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.update.Assignment;
import com.datastax.oss.driver.api.querybuilder.update.UpdateStart;
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
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class UpdateCallController {

    public TextField newThoroughfareTextField;
    public TextField newLocalityTextField;
    public TextField newSubPremiseTextField;
    public TextField newPremiseTextField;
    public TextField newUnitIdTextField;
    public TextField newCauseTextField;
    public TextField newCallerMnTextField;
    public TextField newCallerFnTextField;
    public TextField newCallerLnTextField;

    private String newLocality, newThoroughfare, newPremise, newSubPremise, newCause, newCallerFn, newCallerMn, newCallerLn;
    private UUID newUnitId;

    public TextField oldCallIdTextField;
    public DatePicker oldDatePicker;
    public TextField oldTimeTextField;

    public TableView dataTable;
    public DatePicker searchDatePicker;
    public TextField searchTimeTextField;
    public TextField searchThoroughfareTextField;
    public TextField searchLocalityTextField;



    private LocalDate dateToSearch = null, oldDate = null;
    private LocalTime timeToSearch = null, oldTime = null;
    private String localityToSearch = null, thoroughfareToSearch = null;
    private UUID oldCallId = null;

    private TableColumn<EmergencyCall, UUID> idCol = new TableColumn<EmergencyCall, UUID>("id");
    private TableColumn<EmergencyCall, UUID> unitIdCol = new TableColumn<EmergencyCall, UUID>("unitId");
    private TableColumn<EmergencyCall, LocalDate> dateCol = new TableColumn<EmergencyCall, LocalDate>("date");
    private TableColumn<EmergencyCall, LocalTime> timeCol = new TableColumn<EmergencyCall, LocalTime>("time");
    private TableColumn<EmergencyCall, String> localityCol = new TableColumn<EmergencyCall, String>("locality");
    private TableColumn<EmergencyCall, String> thoroughfareCol = new TableColumn<EmergencyCall, String>("thoroughfareCol");
    private TableColumn<EmergencyCall, String> premiseCol = new TableColumn<EmergencyCall, String>("premiseCol");
    private TableColumn<EmergencyCall, String> subPremiseCol = new TableColumn<EmergencyCall, String>("subPremiseCol");
    private TableColumn<EmergencyCall, String> causeCol = new TableColumn<EmergencyCall, String>("causeCol");
    private TableColumn<EmergencyCall, UUID> callerIdCol = new TableColumn<EmergencyCall, UUID>("callerId");


    private final String tableName = "call_by_date";

    public void GetUnits(ActionEvent event) {
    }

    @FXML
    protected void GetCallsByDate() {

        GetSearchValues();

        ResultSet rs;
        if (dateToSearch == null && timeToSearch == null && localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty()){
            final String getCalls = "SELECT * FROM " + tableName + " LIMIT 100";
            rs = DBConnector.getSession().execute(getCalls);
        } else if (timeToSearch == null && localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty()) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + tableName + " WHERE date = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty()){
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + tableName + " WHERE date = ? AND time = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch, timeToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (thoroughfareToSearch.isEmpty()) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + tableName + " WHERE date = ? AND time = ? AND a_locality = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch, timeToSearch, localityToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        }  else {
            final String getCalls = "SELECT * FROM " + tableName + " LIMIT 100";
            rs = DBConnector.getSession().execute(getCalls);
        }

        ObservableList<EmergencyCall> callObservableList = FXCollections.observableArrayList();
        for (Row row : rs){
            callObservableList
                    .add(new EmergencyCall(
                            row.getString("a_locality"), row.getString("a_thoroughfare"), row.getString("a_premise"),
                            row.getString("a_sub_premise"), row.getString("cause"), row.getLocalDate("date"),
                            row.getLocalTime("time"),
                            row.getUuid("id"), row.getUuid("unit_id"), row.getUuid("caller_id")
                    ));

        }

        dataTable.getColumns().clear();

        dataTable.setEditable(true);

        idCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, UUID>("id"));
        unitIdCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, UUID>("unitId"));
        dateCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, LocalDate>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, LocalTime>("time"));
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

    public void UpdateCall(ActionEvent event) {
        GetOldData();
        PreparedStatement selectOneCall = DBConnector.getSession().prepare(
                "SELECT * FROM " + tableName + " WHERE date = ? AND time = ? and id = ? LIMIT 1"
        );
        BoundStatement boundStatement = selectOneCall.bind(oldDate, oldTime, oldCallId);
        ResultSet rs = DBConnector.getSession().execute(boundStatement);
        Row row = rs.one();
        EmergencyCall emergencyCall = new EmergencyCall(
                row.getString("a_locality"), row.getString("a_thoroughfare"), row.getString("a_premise"),
                row.getString("a_sub_premise"), row.getString("cause"), row.getLocalDate("date"),
                row.getLocalTime("time"),
                row.getUuid("id"), row.getUuid("unit_id"), row.getUuid("caller_id")
        );


        System.out.println(emergencyCall.toString());

//        newCause = emergencyCall.getCause();
//        newLocality = emergencyCall.getCause();
//        newPremise = emergencyCall.getCause();
//        newSubPremise = emergencyCall.getCause();
//        newThoroughfare = emergencyCall.getCause();
//        newCause = emergencyCall.getCause();

        GetNewData(emergencyCall);

        PreparedStatement updateCall = DBConnector.getSession().prepare(
                "UPDATE " + tableName +
                        " SET a_locality = ? , a_thoroughfare = ? , a_premise = ?, a_sub_premise = ?, cause = ?, unit_id = ?" +
                        " WHERE date = ? AND time = ? AND id = ?;"
        );
        boundStatement = updateCall.bind(newLocality, newThoroughfare, newPremise, newSubPremise, newCause, newUnitId, oldDate, oldTime, oldCallId);
        System.out.println(boundStatement);

        DBConnector.getSession().execute(boundStatement);


    }

    protected void GetSearchValues(){

        dateToSearch = null;
        timeToSearch = null;
        localityToSearch = null;
        thoroughfareToSearch = null;
        try {
            dateToSearch = searchDatePicker.getValue();
            localityToSearch = searchLocalityTextField.getText();
            thoroughfareToSearch = searchThoroughfareTextField.getText();
            timeToSearch = LocalTime.parse(searchTimeTextField.getText());
        } catch (DateTimeParseException e) {
            System.out.println("[Error] " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Date: " + dateToSearch);
        System.out.println("Time: " + timeToSearch);
        System.out.println("Locality: " + localityToSearch);
        System.out.println("Thoroughfare: " + thoroughfareToSearch);


//        UpdateStart update = (UpdateStart) update(tableName).set(
//                Assignment.setColumn("a_locality", bindMarker()),
//                Assignment.setColumn("a_thoroughfare", bindMarker()),
//                Assignment.setColumn("a_premise", bindMarker()),
//                Assignment.setColumn("a_sub_premise", bindMarker()),
//                Assignment.setColumn("cause", bindMarker()),
//                Assignment.setColumn("unit_id", bindMarker())
//        );


    }

    public void RemoveCall(ActionEvent event) {
        GetOldData();
        PreparedStatement deleteCall = DBConnector.getSession().prepare(
                "DELETE FROM " + tableName + " WHERE date = ? AND time = ? AND id = ?;"
        );
        BoundStatement boundStatement = deleteCall.bind(oldDate, oldTime, oldCallId);
        DBConnector.getSession().execute(boundStatement);


        //doesn't work
//        Delete delete = deleteFrom(tableName)
//                .where(Relation.column("date").isEqualTo(bindMarker()))
//                .where(Relation.column("time").isEqualTo(bindMarker()))
//                .where(Relation.column("id").isEqualTo(bindMarker()));
////        delete.build(oldDate, oldTime, oldCallId);
//        DBConnector.getSession().execute(delete.build(oldDate, oldTime, oldCallId));

//        BoundStatement boundStatement = delete.bind(oldDate, oldTime, oldCallId);

    }

    protected void GetOldData(){
        try {
            oldDate = oldDatePicker.getValue();
            oldCallId = UUID.fromString(oldCallIdTextField.getText());
            oldTime = LocalTime.parse(oldTimeTextField.getText());
        } catch (DateTimeParseException e) {
            System.out.println("[Error] " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Old Date: " + oldDate);
        System.out.println("Old Time: " + oldTime);
        System.out.println("Old Id: " + oldCallId);

    }

    protected void GetNewData(EmergencyCall emergencyCall){
        try {
            newLocality = newLocalityTextField.getText();
            newThoroughfare = newThoroughfareTextField.getText();
            newPremise = newPremiseTextField.getText();
            newSubPremise = newSubPremiseTextField.getText();
            newCause = newCauseTextField.getText();
            newUnitId = UUID.fromString(newUnitIdTextField.getText());

        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }



        if (newLocality.isEmpty()){
            newLocality = emergencyCall.getLocality();
        }
        if (newThoroughfare.isEmpty()){
            newThoroughfare = emergencyCall.getThoroughfare();
        }
        if (newPremise.isEmpty()){
            newPremise = emergencyCall.getPremise();
        }
        if (newSubPremise.isEmpty()){
            newSubPremise = emergencyCall.getSubPremise();
        }
        if (newCause.isEmpty()){
            newCause = emergencyCall.getCause();
        }
        if (newUnitId == null){
            newUnitId = emergencyCall.getUnitId();
        }

        System.out.println("newLocality:  " + newLocality);
        System.out.println("newThoroughfare:  " + newThoroughfare);
        System.out.println("newPremise:  " + newPremise);
        System.out.println("newSubPremise:  " + newSubPremise);
        System.out.println("newCause:  " + newCause);
        System.out.println("newUnitId:  " + newUnitId);
    }

    public void SwitchToCallByDate(MouseEvent mouseEvent) {
        ViewSwitcher.Switch(mouseEvent, "call_by_date_view.fxml", "/style.css");
    }

    public void SwitchToCallByAddress(MouseEvent mouseEvent) {
    }

    public void SwitchToAddReport(MouseEvent mouseEvent) {
    }

    public void SwitchToUpdateReport(MouseEvent mouseEvent) {
    }


    public void SwitchToAddCall(MouseEvent mouseEvent) {
        ViewSwitcher.Switch(mouseEvent, "add_call_view.fxml", "/style.css");
    }
}
