package org.coursework.cassandraambulance.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.coursework.cassandraambulance.Alerts;
import org.coursework.cassandraambulance.models.EmergencyCall;
import org.coursework.cassandraambulance.tables.EmergencyCallTable;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class GetByDateController extends Controller {

    @FXML
    private TableView<EmergencyCall> dataTable;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField localityTextField, thoroughfareTextField;

    private LocalDate dateToSearch = null;
    private String localityToSearch = null, thoroughfareToSearch = null;

    @FXML
    protected void GetCallsByDate() {

        GetSearchValues();

        EmergencyCallTable.GetByDate(dataTable, dateToSearch, localityToSearch, thoroughfareToSearch);
    }

    @FXML
    protected void GetSearchDate(){
        dateToSearch = datePicker.getValue();

        if (dateToSearch.isAfter(LocalDate.now())){
            System.out.println("incorrect date to search");
        }
        System.out.println(dateToSearch.toString());
    }

    protected void GetSearchValues(){

        dateToSearch = null;
        localityToSearch = null;
        thoroughfareToSearch = null;
        try {
            dateToSearch = datePicker.getValue();
            localityToSearch = localityTextField.getText();
            thoroughfareToSearch = thoroughfareTextField.getText();
        } catch (DateTimeParseException e) {
            System.out.println("[Error] " + e);
            Alerts.ParseError("Time can't be parsed\nTime format: hh:MM:ss");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}