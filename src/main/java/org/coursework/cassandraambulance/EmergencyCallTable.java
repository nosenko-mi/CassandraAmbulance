package org.coursework.cassandraambulance;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.coursework.cassandraambulance.models.EmergencyCall;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class EmergencyCallTable {

    public static final TableColumn<EmergencyCall, UUID> idCol = new TableColumn<EmergencyCall, UUID>("id");
    public static final TableColumn<EmergencyCall, UUID> unitIdCol = new TableColumn<EmergencyCall, UUID>("unitId");
    public static final TableColumn<EmergencyCall, LocalDate> dateCol = new TableColumn<EmergencyCall, LocalDate>("date");
    public static final TableColumn<EmergencyCall, LocalTime> timeCol = new TableColumn<EmergencyCall, LocalTime>("time");
    public static final TableColumn<EmergencyCall, String> localityCol = new TableColumn<EmergencyCall, String>("locality");
    public static final TableColumn<EmergencyCall, String> thoroughfareCol = new TableColumn<EmergencyCall, String>("thoroughfareCol");
    public static final TableColumn<EmergencyCall, String> premiseCol = new TableColumn<EmergencyCall, String>("premiseCol");
    public static final TableColumn<EmergencyCall, String> subPremiseCol = new TableColumn<EmergencyCall, String>("subPremiseCol");
    public static final TableColumn<EmergencyCall, String> causeCol = new TableColumn<EmergencyCall, String>("causeCol");
    public static final TableColumn<EmergencyCall, UUID> callerIdCol = new TableColumn<EmergencyCall, UUID>("callerId");

    public static void SetColumns(TableView<EmergencyCall> dataTable, ObservableList<EmergencyCall> callObservableList ){

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


    }

    public static void GetByDate(TableView<EmergencyCall> emergencyCallTable,LocalDate dateToSearch, String localityToSearch, String thoroughfareToSearch){
        ResultSet rs = Query.GetCallsByDateQuery(dateToSearch, localityToSearch, thoroughfareToSearch);
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

        emergencyCallTable.getColumns().clear();

        EmergencyCallTable.SetColumns(emergencyCallTable, callObservableList);

        emergencyCallTable.getSelectionModel().setCellSelectionEnabled(true);
        emergencyCallTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(emergencyCallTable);
    }

}
