package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.coursework.cassandraambulance.*;
import org.coursework.cassandraambulance.models.Patient;
import org.coursework.cassandraambulance.tables.PatientTable;

import java.time.format.DateTimeParseException;
import java.util.UUID;

public class GetPatientController extends Controller{
    public TextField patientIdTextField;
    public TableView<Patient> patientTable;


    private UUID patientId;

    public void GetPatient(ActionEvent event) {
        GetSearchValues();

        PatientTable.GetById(patientTable, patientId);


    }

    public void DeletePatient(ActionEvent event) {
        GetSearchValues();
        Patient patient = PatientTable.ToModel(patientId);
        if (patient != null){
            BoundStatement boundStatement = PreparedStatements.deleteOnePatientById.bind(patient.getId());
            DBConnector.getSession().execute(boundStatement);
            System.out.println("[Patient Deleted]");
        }

    }

    protected void GetSearchValues(){
        try {
            patientId = UUID.fromString(patientIdTextField.getText());
        } catch (IllegalArgumentException e) {
            System.out.println("[Error] " + e);
            Alerts.ParseError("Patient id can't be parsed");
            patientId = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
