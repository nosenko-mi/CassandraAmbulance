package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.coursework.cassandraambulance.*;
import org.coursework.cassandraambulance.models.EmergencyCall;
import org.coursework.cassandraambulance.models.Patient;
import org.coursework.cassandraambulance.models.Report;
import org.coursework.cassandraambulance.tables.EmergencyCallTable;
import org.coursework.cassandraambulance.tables.ReportTable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class UpdateReportController extends Controller {

    public MenuButton resultMenuButton;
    public MenuButton hospitalizationMenuButton;
    public MenuButton traumaMenuButton;
    public MenuButton onsetMenuButton;
    public MenuButton fruitlessMenuButton;
    public MenuButton appliedMenuButton;
    public DatePicker datePicker;

    public TextField thoroughfareToSearchTextField;
    public TextField localityToSearchTextField;
    public TextField callIdToSearchTextField;

    public TableView<EmergencyCall> emergencyCallTable;
    public TableView<Report> reportTable;

    public TextField oldCallIdTextField;
    public TextField oldReportIdTextField1;

    public TextField newPatientDobTextField;
    public TextField newCompletionTimeTextField;
    public TextField newArrivalTimeTextField;
    public TextField newReturnTimeTextField;
    public TextField newDepartureTimeTextField;
    public TextField newThoroughfareTextField;
    public TextField newLocalityTextField;
    public TextField newSubPremiseTextField;
    public TextField newPremiseTextField;
    public TextField newUnitIdTextField;
    public TextField newPatientMnTextField;
    public TextField newPatientFnTextField;
    public TextField newPatientLnTextField;
    public TextField newDiagnosisTextField;
    public TextField newDiagnosisCodeTextField;

    private LocalDate dateToSearch = null, newPatientDob;
    private final LocalTime timeToSearch = null;
    private LocalTime newDepartureTime;
    private LocalTime newArrivalTime;
    private LocalTime newCompletionTime;
    private LocalTime newReturnTime;
    private String localityToSearch = null, thoroughfareToSearch = null;
    private UUID callId, reportId, newUnitId;

    private String newLocality, newThoroughfare, newPremise, newSubPremise,
            newPatientFn, newPatientMn, newPatientLn, newDiagnosis, newDiagnosisCode,
            newResult, newHospitalization, newApplied, newOnset, newFruitless, newTrauma;


    @FXML
    protected void GetCallsByDate(ActionEvent event) {
        GetSearchData();

        ResultSet rs = Query.GetCallsByDateQuery(dateToSearch, localityToSearch, thoroughfareToSearch);
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
        emergencyCallTable.getColumns().clear();


        EmergencyCallTable.SetColumns(emergencyCallTable, callObservableList);

        emergencyCallTable.getSelectionModel().setCellSelectionEnabled(true);
        emergencyCallTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(emergencyCallTable);

    }

    public void GetReportByCall(ActionEvent event) {

        try {
            callId = UUID.fromString(callIdToSearchTextField.getText());
        } catch (IllegalArgumentException e) {
            callId = null;
            System.out.println(e.getMessage());
        }
        ReportTable.GetByCall(reportTable, callId);

    }

    public void UpdateReport(ActionEvent event) {
        GetOldData();
        try {
            //        ???????????? ????????, ???? ???????? ??????????????
            ResultSet rs = Query.GetOneReport(callId, reportId);
            Row row = rs.one();
            Report oldReport = new Report(
                    row.getUuid("id"), row.getUuid("call_id"), row.getUuid("patient_id"),
                    row.getUuid("unit_id"), row.getString("preliminary_diagnosis"),
                    row.getString("diagnosis_code"), row.getString("result"), row.getString("hospitalization_status"),
                    row.getString("trauma"), row.getString("onset"), row.getString("applied_before"),
                    row.getString("fruitless"), row.getString("a_locality"), row.getString("a_thoroughfare"),
                    row.getString("a_premise"), row.getString("a_sub_premise"),
                    row.getLocalTime("departure_time"), row.getLocalTime("arrival_time"),
                    row.getLocalTime("completion_time"), row.getLocalTime("return_time")

            );
            // ???????????? ????????????????, ???? ???????? ??????????????
            rs = Query.GetOnePatient(oldReport.getPatientId());
            row = rs.one();
            Patient oldPatient = new Patient(
                    row.getLocalDate("dob"), row.getUuid("id"),
                    row.getString("first_name"), row.getString("middle_name"), row.getString("last_name")
            );
            // ???????????????????? ???????? ????????
            GetNewData(oldReport, oldPatient);
            // ?????????????? ???????? ??????????
            BoundStatement boundStatement = PreparedStatements.updateReportInReportByCall.bind(
                    newLocality, newThoroughfare, newPremise, newSubPremise,
                    newDepartureTime, newArrivalTime, newCompletionTime, newReturnTime,
                    newDiagnosis, newDiagnosisCode, newResult, newHospitalization,
                    newApplied, newTrauma, newOnset, newFruitless, newUnitId,
                    callId, reportId
            );

            DBConnector.getSession().execute(boundStatement);
//        ?????????????? ???????? ????????????????
            UpdatePatient(oldPatient);

            Alerts.SucceedOperation();
        } catch (NullPointerException e){
            System.out.println(e.getMessage());
            Alerts.FailedOperation();
        }

    }

    public void RemoveReport(ActionEvent event) {
        GetOldData();

        try {
            BoundStatement boundStatement = PreparedStatements.deleteReportFromReportByCall.bind(callId, reportId);
            DBConnector.getSession().execute(boundStatement);
            System.out.println("[Report deleted]");

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }



    public void initialize(){
        InitMenuButtons();
    }

    private void UpdatePatient(Patient oldPatient){
        BoundStatement boundStatement;
        if (newPatientFn.equals(oldPatient.getFirstName()) && newPatientMn.equals(oldPatient.getMiddleName()) && newPatientLn.equals(oldPatient.getLastName()) && newPatientDob == oldPatient.getDob()){
            System.out.println("[Patient is not changed]");
        } else if(newPatientDob != oldPatient.getDob() && oldPatient.getDob() != null){

            // dob - clustering key ?? ?????????????? patients, ???????? ???????? ???????????????? ?????????????????? ??????????????.
            // ???????????????? ?????????????? - ?????????????????? ???????????? ????????????, ???? ?????????????????? ????????????????????????
            UUID patientId = UUID.randomUUID();

            boundStatement = PreparedStatements.addPatientToPatients.bind(newPatientDob, patientId, newPatientFn, newPatientMn, newPatientLn);
            DBConnector.getSession().execute(boundStatement);
            System.out.println("[Patient added]");

            boundStatement = PreparedStatements.updatePatientInReportByCall.bind(patientId, callId, reportId);
            DBConnector.getSession().execute(boundStatement);
            System.out.println("[Report changed]");

            boundStatement = PreparedStatements.deletePatientFromPatient.bind(oldPatient.getId(), oldPatient.getDob());
            DBConnector.getSession().execute(boundStatement);
            System.out.println("[Patient deleted]");


        } else {

            boundStatement = PreparedStatements.updatePatientNameInPatients.bind(newPatientFn, newPatientMn, newPatientLn, oldPatient.getId(), oldPatient.getDob());

            DBConnector.getSession().execute(boundStatement);
            System.out.println("[Patient changed]");
        }

    }

    protected void GetSearchData(){

        dateToSearch = null;
        localityToSearch = null;
        thoroughfareToSearch = null;
        try {
            dateToSearch = datePicker.getValue();
            localityToSearch = localityToSearchTextField.getText();
            thoroughfareToSearch = thoroughfareToSearchTextField.getText();
        } catch (DateTimeParseException e) {
            System.out.println("[Error] " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void GetOldData(){
        try {
            callId = UUID.fromString(oldCallIdTextField.getText());
            reportId = UUID.fromString(oldReportIdTextField1.getText());
        } catch (IllegalArgumentException e) {
            callId = null;
            reportId = null;
            System.out.println(e.getMessage());
        }
        ReportTable.GetByCall(reportTable, callId);
    }

    protected void GetNewData(Report oldReport, Patient oldPatient){

        newLocality = newLocalityTextField.getText();
        newThoroughfare = newThoroughfareTextField.getText();
        newPremise = newPremiseTextField.getText();
        newSubPremise = newSubPremiseTextField.getText();

        newDiagnosis = newDiagnosisTextField.getText();
        newDiagnosisCode = newDiagnosisCodeTextField.getText();

        newPatientFn = newPatientFnTextField.getText();
        newPatientMn = newPatientMnTextField.getText();
        newPatientLn = newPatientLnTextField.getText();

        newHospitalization = hospitalizationMenuButton.getText();
        newResult = resultMenuButton.getText();
        newTrauma = traumaMenuButton.getText();
        newOnset = onsetMenuButton.getText();
        newFruitless = onsetMenuButton.getText();
        newApplied = appliedMenuButton.getText();

        if (newHospitalization.equals("Hospitalization")){
            newHospitalization = oldReport.getHospitalizationStatus();
        }
        if (newResult.equals("Result")){
            newResult = oldReport.getResultStatus();
        }
        if (newTrauma.equals("Trauma")){
            newTrauma = oldReport.getTraumaStatus();
        }
        if (newOnset.equals("Onset")){
            newOnset = oldReport.getOnsetStatus();
        }
        if (newFruitless.equals("Fruitless")){
            newFruitless = oldReport.getFruitlessStatus();
        }
        if (newApplied.equals("Applied before")){
            newApplied = oldReport.getAppliedBeforeStatus();
        }


        if (newLocality.isEmpty()){
            newLocality = oldReport.getLocality();
        }
        if (newThoroughfare.isEmpty()){
            newThoroughfare = oldReport.getThoroughfare();
        }
        if (newPremise.isEmpty()){
            newPremise = oldReport.getPremise();
        }
        if (newSubPremise.isEmpty()){
            newSubPremise = oldReport.getSubPremise();
        }
        if (newDiagnosis.isEmpty()){
            newDiagnosis = oldReport.getPreliminaryDiagnosis();
        }
        if (newDiagnosisCode.isEmpty()){
            newDiagnosisCode = oldReport.getDiagnosisCode();
        }

        if (newPatientFn.isEmpty()){
            newPatientFn = oldPatient.getFirstName();
        }
        if (newPatientMn.isEmpty()){
            newPatientMn = oldPatient.getMiddleName();
        }
        if (newPatientLn.isEmpty()){
            newPatientLn = oldPatient.getLastName();
        }

        try {
            newUnitId = UUID.fromString(newUnitIdTextField.getText());
        } catch (IllegalArgumentException e){
            newUnitId = oldReport.getUnitId();
            System.out.println(e.getMessage());
        }

        try {
            newPatientDob = LocalDate.parse(newPatientDobTextField.getText());
        } catch (DateTimeParseException e){
            newPatientDob = oldPatient.getDob();
            System.out.println(e.getMessage());
        }

        try {
            newDepartureTime = LocalTime.parse(newDepartureTimeTextField.getText());
        } catch (DateTimeParseException e){
            newDepartureTime = oldReport.getDepartureTime();
            System.out.println(e.getMessage());
        }
        try {
            newArrivalTime = LocalTime.parse(newArrivalTimeTextField.getText());
        } catch (DateTimeParseException e){
            newArrivalTime = oldReport.getArrivalTime();
            System.out.println(e.getMessage());
        }
        try {
            newCompletionTime = LocalTime.parse(newCompletionTimeTextField.getText());
        } catch (DateTimeParseException e){
            newCompletionTime = oldReport.getCompletionTime();
            System.out.println(e.getMessage());
        }
        try {
            newReturnTime = LocalTime.parse(newReturnTimeTextField.getText());
        } catch (DateTimeParseException e){
            newReturnTime = oldReport.getReturnTime();
            System.out.println(e.getMessage());
        }


    }

    public void InitMenuButtons(){

        MenuItem hospItemY = new MenuItem(StringResources.HOSP_TRUE);
        MenuItem hospItemN = new MenuItem(StringResources.HOSP_FALSE);
        hospitalizationMenuButton.getItems().addAll(hospItemY, hospItemN);
        hospItemN.setOnAction(event -> hospitalizationMenuButton.setText(hospItemN.getText()));
        hospItemY.setOnAction(event -> hospitalizationMenuButton.setText(hospItemY.getText()));


        MenuItem resultItem1 = new MenuItem("????????????????????");
        MenuItem resultItem2 = new MenuItem("?????? ????????????");
        MenuItem resultItem3 = new MenuItem("????????????????????");
        MenuItem resultItem4 = new MenuItem("???????????? ?? ??????????????????????");
        MenuItem resultItem5 = new MenuItem("???????????? ???? ??????????????");
        MenuItem resultItem6 = new MenuItem("????????????????");
        MenuItem resultItem7 = new MenuItem("????????????????????");
        MenuItem resultItem8 = new MenuItem("???????? ??????????????????????");
        MenuItem resultItem9 = new MenuItem("???????????????? ????????????????????");
        MenuItem resultItem10 = new MenuItem("????????");
        resultMenuButton.getItems().addAll(resultItem1, resultItem2, resultItem3, resultItem4, resultItem5, resultItem6, resultItem7, resultItem8, resultItem9, resultItem10);
        ResultItemsSetOnAction(resultItem1, resultItem2, resultItem3, resultItem4, resultItem5);
        ResultItemsSetOnAction(resultItem6, resultItem7, resultItem8, resultItem9, resultItem10);


        MenuItem traumaItem1 = new MenuItem("??????????");
        MenuItem traumaItem2 = new MenuItem("????????????????");
        MenuItem traumaItem3 = new MenuItem("??????????????");
        MenuItem traumaItem4 = new MenuItem("??????????????????????");
        MenuItem traumaItem5 = new MenuItem("??????");
        MenuItem traumaItem6 = new MenuItem("??????????????????");
        MenuItem traumaItem7 = new MenuItem("??????????????????");
        MenuItem traumaItem8 = new MenuItem("??????????????????????");
        MenuItem traumaItem9 = new MenuItem("??????????????, ?????????????????? ??????????????????");
        traumaMenuButton.getItems().addAll(traumaItem1, traumaItem2, traumaItem3, traumaItem4, traumaItem5, traumaItem6, traumaItem7, traumaItem8, traumaItem9);
        traumaItem1.setOnAction(event -> traumaMenuButton.setText(traumaItem1.getText()));
        traumaItem2.setOnAction(event -> traumaMenuButton.setText(traumaItem2.getText()));
        traumaItem3.setOnAction(event -> traumaMenuButton.setText(traumaItem3.getText()));
        traumaItem4.setOnAction(event -> traumaMenuButton.setText(traumaItem4.getText()));
        traumaItem5.setOnAction(event -> traumaMenuButton.setText(traumaItem5.getText()));
        traumaItem6.setOnAction(event -> traumaMenuButton.setText(traumaItem6.getText()));
        traumaItem7.setOnAction(event -> traumaMenuButton.setText(traumaItem7.getText()));
        traumaItem8.setOnAction(event -> traumaMenuButton.setText(traumaItem8.getText()));
        traumaItem9.setOnAction(event -> traumaMenuButton.setText(traumaItem9.getText()));

        MenuItem appliedItem1 = new MenuItem("???? ??????????????????");
        MenuItem appliedItem2 = new MenuItem("?????????????????? ???? ????????");
        MenuItem appliedItem3 = new MenuItem("?????????????????? ?? ?????????? ??????");
        appliedMenuButton.getItems().addAll(appliedItem1, appliedItem2, appliedItem3);
        appliedItem1.setOnAction(event -> appliedMenuButton.setText(appliedItem1.getText()));
        appliedItem2.setOnAction(event -> appliedMenuButton.setText(appliedItem2.getText()));
        appliedItem3.setOnAction(event -> appliedMenuButton.setText(appliedItem3.getText()));

        MenuItem onsetItem1 = new MenuItem("???? 1 ??????.");
        MenuItem onsetItem2 = new MenuItem("?????? 1 ???? 3 ??????.");
        MenuItem onsetItem3 = new MenuItem("?????? 3 ???? 6 ??????");
        MenuItem onsetItem4 = new MenuItem("?????? 6 ???? 12 ??????");
        MenuItem onsetItem5 = new MenuItem("?????? 12 ???? 24 ??????");
        MenuItem onsetItem6 = new MenuItem("?????????? 24 ??????.");
        onsetMenuButton.getItems().addAll(onsetItem1, onsetItem2, onsetItem3, onsetItem4, onsetItem5, onsetItem6);
        onsetItem1.setOnAction(event -> onsetMenuButton.setText(onsetItem1.getText()));
        onsetItem2.setOnAction(event -> onsetMenuButton.setText(onsetItem2.getText()));
        onsetItem3.setOnAction(event -> onsetMenuButton.setText(onsetItem3.getText()));
        onsetItem4.setOnAction(event -> onsetMenuButton.setText(onsetItem4.getText()));
        onsetItem5.setOnAction(event -> onsetMenuButton.setText(onsetItem5.getText()));
        onsetItem6.setOnAction(event -> onsetMenuButton.setText(onsetItem6.getText()));


        MenuItem fruitlessItem1 = new MenuItem("???? ??????????????");
        MenuItem fruitlessItem2 = new MenuItem("???????????? ???? ????????????????");
        MenuItem fruitlessItem3 = new MenuItem("???? ??????????????");
        MenuItem fruitlessItem4 = new MenuItem("???? ??????????????????");
        MenuItem fruitlessItem5 = new MenuItem("???????????????????? ???? ??????????????");
        MenuItem fruitlessItem6 = new MenuItem("?????????????? ?????? ???????????????? ????????????????");
        fruitlessMenuButton.getItems().addAll(fruitlessItem1, fruitlessItem2, fruitlessItem3, fruitlessItem4, fruitlessItem5, fruitlessItem6);
        fruitlessItem1.setOnAction(event -> fruitlessMenuButton.setText(fruitlessItem1.getText()));
        fruitlessItem1.setOnAction(event -> fruitlessMenuButton.setText(fruitlessItem1.getText()));
        fruitlessItem2.setOnAction(event -> fruitlessMenuButton.setText(fruitlessItem2.getText()));
        fruitlessItem3.setOnAction(event -> fruitlessMenuButton.setText(fruitlessItem3.getText()));
        fruitlessItem4.setOnAction(event -> fruitlessMenuButton.setText(fruitlessItem4.getText()));
        fruitlessItem5.setOnAction(event -> fruitlessMenuButton.setText(fruitlessItem5.getText()));
        fruitlessItem6.setOnAction(event -> fruitlessMenuButton.setText(fruitlessItem6.getText()));


    }

    private void ResultItemsSetOnAction(MenuItem resultItem1, MenuItem resultItem2, MenuItem resultItem3, MenuItem resultItem4, MenuItem resultItem5) {
        resultItem1.setOnAction(event -> resultMenuButton.setText((resultItem1.getText())));
        resultItem2.setOnAction(event -> resultMenuButton.setText((resultItem2.getText())));
        resultItem3.setOnAction(event -> resultMenuButton.setText((resultItem3.getText())));
        resultItem4.setOnAction(event -> resultMenuButton.setText((resultItem4.getText())));
        resultItem5.setOnAction(event -> resultMenuButton.setText((resultItem5.getText())));
    }


}
