package org.coursework.cassandraambulance.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.coursework.cassandraambulance.Alerts;
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
    private String localityToSearch = null, thoroughfareToSearch = null;

    private UUID callId;

    public void GetCallsByDate(ActionEvent event) {

        GetSearchValues();
        EmergencyCallTable.GetByDate(emergencyCallTable, dateToSearch, localityToSearch, thoroughfareToSearch);

    }

    public void GetReportByCall(ActionEvent event) {

        try {
            callId = UUID.fromString(callIdTextField.getText());

            ReportTable.GetByCall(reportTable, callId);

        } catch (IllegalArgumentException e) {
            Alerts.ParseError("Call id can't be parsed");
            System.out.println(e.getMessage());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    private void GetSearchValues() {
        dateToSearch = null;
        localityToSearch = null;
        thoroughfareToSearch = null;
        try {
            localityToSearch = localityTextField.getText();
            thoroughfareToSearch = thoroughfareTextField.getText();
            dateToSearch = datePicker.getValue();
        } catch (DateTimeParseException e) {
            System.out.println("[Error] " + e);
            Alerts.ParseError("Date can't be parsed\nTime format: yyyy-MM-dd");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
