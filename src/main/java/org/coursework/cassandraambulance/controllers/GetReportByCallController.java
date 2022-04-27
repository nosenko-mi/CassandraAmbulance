package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.coursework.cassandraambulance.EmergencyCallTable;
import org.coursework.cassandraambulance.Query;
import org.coursework.cassandraambulance.TableUtils;
import org.coursework.cassandraambulance.models.EmergencyCall;
import org.coursework.cassandraambulance.models.Report;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class GetReportByCallController {
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

    public void GetSearchDate(ActionEvent event) {
    }

    public void SwitchToCallByAddress(MouseEvent mouseEvent) {
    }

    public void SwitchToAddCall(MouseEvent mouseEvent) {
    }

    public void SwitchToAddReport(MouseEvent mouseEvent) {
    }

    public void SwitchToUpdateCall(MouseEvent mouseEvent) {
    }

    public void SwitchToUpdateReport(MouseEvent mouseEvent) {
    }

    public void GetReportsByCall(ActionEvent event) {
    }

    public void SwitchToCallByDate(MouseEvent mouseEvent) {
    }

    public void SwitchToUnitByEmployee(MouseEvent mouseEvent) {
    }

    public void SwitchToGetPersons(MouseEvent mouseEvent) {
    }
}
