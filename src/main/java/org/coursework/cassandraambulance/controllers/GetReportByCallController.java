package org.coursework.cassandraambulance.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.coursework.cassandraambulance.ViewSwitcher;
import org.coursework.cassandraambulance.models.EmergencyCall;
import org.coursework.cassandraambulance.models.Report;
import org.coursework.cassandraambulance.tables.EmergencyCallTable;
import org.coursework.cassandraambulance.tables.ReportTable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class GetReportByCallController extends Controller {
    public DatePicker datePicker;
    public TextField thoroughfareTextField;
    public TextField localityTextField;
    public TableView<EmergencyCall> emergencyCallTable;
    public TextField timeTextField;
    public TextField callIdTextField;
    public TableView<Report> reportTable;


    private LocalDate dateToSearch = null;
    private LocalTime timeToSearch = null;
    private String localityToSearch = null, thoroughfareToSearch = null;

    private UUID callId;

    public void GetCallsByDate(ActionEvent event) {

        GetSearchValues();
        EmergencyCallTable.GetByDate(emergencyCallTable, dateToSearch, localityToSearch, thoroughfareToSearch);

    }

    private void GetSearchValues() {
        dateToSearch = null;
        timeToSearch = null;
        localityToSearch = null;
        thoroughfareToSearch = null;
        try {
            dateToSearch = datePicker.getValue();
            localityToSearch = localityTextField.getText();
            thoroughfareToSearch = thoroughfareTextField.getText();
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
    }

    public void GetReportByCall(ActionEvent event) {

        try {
            callId = UUID.fromString(callIdTextField.getText());
        } catch (IllegalArgumentException e) {
            callId = null;
            System.out.println(e);
        }
        ReportTable.GetByCall(reportTable, callId);


//        PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
//                "SELECT * FROM " + TableName.CALL_BY_DATE + " WHERE a_locality = ? LIMIT 100"
//        );
//        BoundStatement boundStatement = selectAllCallsByDate.bind(localityToSearch);
//        rs = DBConnector.getSession().execute(boundStatement);

//        PreparedStatement getReportByCall = DBConnector.getSession().prepare(
//                "SELECT * FROM " + TableName.REPORT_BY_CALL + " WHERE call_id = ? LIMIT 100;"
//        );
//        BoundStatement boundStatement = getReportByCall.bind(callId);
//        ResultSet rs = DBConnector.getSession().execute(boundStatement);
//
//        ObservableList<Report> reportObservableList = FXCollections.observableArrayList();
//
//        for (Row row : rs){
//            reportObservableList
//                    .add(new Report(
//                            row.getUuid("id"), row.getUuid("call_id"), row.getUuid("patient_id"),
//                            row.getUuid("unit_id"), row.getString("preliminary_diagnosis"),
//                            row.getString("diagnosis_code"), row.getString("result"), row.getString("hospitalization_status"),
//                            row.getString("trauma"), row.getString("onset"), row.getString("applied_before"),
//                            row.getString("fruitless"), row.getString("a_locality"), row.getString("a_thoroughfare"),
//                            row.getString("a_premise"), row.getString("a_sub_premise"),
//                            row.getLocalTime("departure_time"), row.getLocalTime("arrival_time"),
//                            row.getLocalTime("completion_time"), row.getLocalTime("return_time")
//
//                    ));
//        }
//        reportTable.getColumns().clear();
//
//        ReportTable.SetColumns(reportTable, reportObservableList);
//
//        reportTable.getSelectionModel().setCellSelectionEnabled(true);
//        reportTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        TableUtils.installCopyPasteHandler(reportTable);
    }
}
