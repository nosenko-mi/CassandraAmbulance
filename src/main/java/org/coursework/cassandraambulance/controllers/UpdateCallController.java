package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.coursework.cassandraambulance.Alerts;
import org.coursework.cassandraambulance.DBConnector;
import org.coursework.cassandraambulance.PreparedStatements;
import org.coursework.cassandraambulance.StringResources;
import org.coursework.cassandraambulance.models.EmergencyCall;
import org.coursework.cassandraambulance.models.Person;
import org.coursework.cassandraambulance.tables.EmergencyCallTable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class UpdateCallController extends Controller {

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

    public TableView<EmergencyCall> dataTable;
    public DatePicker searchDatePicker;
    public TextField searchTimeTextField;
    public TextField searchThoroughfareTextField;
    public TextField searchLocalityTextField;



    private LocalDate dateToSearch = null, oldDate = null;
    private LocalTime timeToSearch = null, oldTime = null;
    private String localityToSearch = null, thoroughfareToSearch = null;
    private UUID oldCallId = null;

    @FXML
    protected void GetCallsByDate() {

        GetSearchValues();

        EmergencyCallTable.GetByDate(dataTable, dateToSearch, localityToSearch, thoroughfareToSearch);

    }

    public void UpdateCall(ActionEvent event) {
        GetOldData();
        // ???????????? ????????????, ???? ???????? ??????????????????????
        BoundStatement boundStatement = PreparedStatements.selectOneCallFromCallByDate.bind(oldDate, oldTime, oldCallId);
        ResultSet rs = DBConnector.getSession().execute(boundStatement);

        Row row = rs.one();
        EmergencyCall emergencyCall = new EmergencyCall(
                row.getString("a_locality"), row.getString("a_thoroughfare"), row.getString("a_premise"),
                row.getString("a_sub_premise"), row.getString("cause"), row.getLocalDate("date"),
                row.getLocalTime("time"),
                row.getUuid("id"), row.getUuid("unit_id"), row.getUuid("caller_id")
        );

        // ???????????? ??????????????????, ???? ???????? ??????????????????????
        boundStatement = PreparedStatements.selectOneCallerFromPersons.bind(emergencyCall.getCallerId());
        rs = DBConnector.getSession().execute(boundStatement);

        row = rs.one();
        Person caller = new Person(
                row.getString("type"), row.getUuid("id"),
                row.getString("first_name"), row.getString("middle_name"), row.getString("last_name"));

        // ???????????????????? ???????? ????????
        GetNewData(emergencyCall, caller);

        // ?????????????? ???????? ??????????????????
        UpdateCaller(caller);

        // ?????????????? ???????? ??????????????
        boundStatement = PreparedStatements.updateCallInCallByDate.bind(newLocality, newThoroughfare, newPremise, newSubPremise, newCause, newUnitId, oldDate, oldTime, oldCallId);
        DBConnector.getSession().execute(boundStatement);

        // ???????? ???????????????????? ?? ?????????????? call_by_address, ?????? ?? ?????? ???????????? - ?????????????????? ????????, ???????? ?????? ?????????????????? ?????????????????????? ?????????? ??????????, ?? ???????????? ??????????????????????
        UpdateCallByAddress(emergencyCall);

        Alerts.SucceedOperation();
    }

    private void UpdateCallByAddress(EmergencyCall emergencyCall){
        BoundStatement boundStatement = PreparedStatements.deleteCallFromCallByAddress.bind(emergencyCall.getLocality(), emergencyCall.getThoroughfare(), emergencyCall.getPremise(), emergencyCall.getSubPremise(), emergencyCall.getId());
        DBConnector.getSession().execute(boundStatement);

        boundStatement = PreparedStatements.addCallToCallByAddress.bind(emergencyCall.getDate(), emergencyCall.getTime(), newLocality, newThoroughfare, newPremise, newSubPremise, emergencyCall.getId(), newCause, newUnitId, emergencyCall.getCallerId());
        DBConnector.getSession().execute(boundStatement);

    }

    private void UpdateCaller(Person caller){
        if (newCallerFn.equals(caller.getFirstName()) && newCallerMn.equals(caller.getMiddleName()) && newCallerLn.equals(caller.getLastName())){
            System.out.println("Caller is not changed");

        } else {
            System.out.println("Caller changed");

            BoundStatement boundStatement = PreparedStatements.updateCallerInPersons.bind(newCallerFn, newCallerMn, newCallerLn, "????????????????", caller.getId());
            DBConnector.getSession().execute(boundStatement);
        }
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

    }

    public void RemoveCall(ActionEvent event) {
        GetOldData();

        BoundStatement boundStatement = PreparedStatements.deleteCall.bind(oldDate, oldTime, oldCallId);
        DBConnector.getSession().execute(boundStatement);

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

    protected void GetNewData(EmergencyCall emergencyCall, Person caller){
        try {
            newLocality = newLocalityTextField.getText();
            newThoroughfare = newThoroughfareTextField.getText();
            newPremise = newPremiseTextField.getText();
            newSubPremise = newSubPremiseTextField.getText();
            newCause = newCauseTextField.getText();

            newCallerFn = newCallerFnTextField.getText();
            newCallerMn = newCallerMnTextField.getText();
            newCallerLn = newCallerLnTextField.getText();


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
        if (newCallerFn.isEmpty()){
            newCallerFn = caller.getFirstName();
        }
        if (newCallerMn.isEmpty()){
            newCallerMn = caller.getMiddleName();
        }
        if (newCallerLn.isEmpty()){
            newCallerLn = caller.getLastName();
        }

        System.out.println("newLocality:  " + newLocality);
        System.out.println("newThoroughfare:  " + newThoroughfare);
        System.out.println("newPremise:  " + newPremise);
        System.out.println("newSubPremise:  " + newSubPremise);
        System.out.println("newCause:  " + newCause);
        System.out.println("newUnitId:  " + newUnitId);
    }


}
