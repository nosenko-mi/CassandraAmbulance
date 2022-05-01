package org.coursework.cassandraambulance.tables;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.coursework.cassandraambulance.Query;
import org.coursework.cassandraambulance.TableUtils;
import org.coursework.cassandraambulance.models.Patient;

import java.time.LocalDate;
import java.util.UUID;

public class PatientTable {

    public static final TableColumn<Patient, UUID> idCol = new TableColumn<>("Id");
    public static final TableColumn<Patient, LocalDate> dobCol = new TableColumn<>("Date of birth");
    public static final TableColumn<Patient, String> fnCol = new TableColumn<>("First name");
    public static final TableColumn<Patient, String> mnCol = new TableColumn<>("Middle name");
    public static final TableColumn<Patient, String> lnCol = new TableColumn<>("Last name");
    private static final ObservableList<Patient> patientObservableList = FXCollections.observableArrayList();

    public static Patient ToModel(UUID id){
        Patient patient = null;
        ResultSet rs = Query.GetOnePatient(id);
        patientObservableList.clear();
        for (Row row : rs){
            patient = new Patient(
                    row.getLocalDate("dob"), row.getUuid("id"),
                    row.getString("first_name"), row.getString("middle_name"), row.getString("last_name"));
        };

        return patient;
    }

    public static void SetColumns(TableView<Patient> dataTable, ObservableList<Patient> callObservableList ){

        idCol.setCellValueFactory(new PropertyValueFactory<Patient, UUID>("id"));
        dobCol.setCellValueFactory(new PropertyValueFactory<Patient, LocalDate>("dob"));
        fnCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("firstName"));
        mnCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("middleName"));
        lnCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("lastName"));

        dataTable.setItems(callObservableList);
        dataTable.getColumns().addAll(idCol, dobCol, fnCol, mnCol, lnCol);
    }

    public static void GetById(TableView<Patient> dataTable, UUID id){
        ResultSet rs = Query.GetOnePatient(id);
        patientObservableList.clear();
        for (Row row : rs){
            patientObservableList
                    .add(new Patient(
                            row.getLocalDate("dob"), row.getUuid("id"),
                            row.getString("first_name"), row.getString("middle_name"), row.getString("last_name")
                    ));

        }

        SetColumns(dataTable, patientObservableList);

        dataTable.getSelectionModel().setCellSelectionEnabled(true);
        dataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(dataTable);
    }
}
