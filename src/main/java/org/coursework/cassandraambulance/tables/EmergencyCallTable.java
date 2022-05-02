package org.coursework.cassandraambulance.tables;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.coursework.cassandraambulance.DBConnector;
import org.coursework.cassandraambulance.Query;
import org.coursework.cassandraambulance.StringResources;
import org.coursework.cassandraambulance.TableUtils;
import org.coursework.cassandraambulance.models.EmergencyCall;
import org.coursework.cassandraambulance.models.Unit;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


// клас, що представляє таблицю викликів
// необхідний для створення столбців у існуючій таблиці, а також для отримання та внесення даних у таблицю
public class EmergencyCallTable {

    public static final TableColumn<EmergencyCall, UUID> idCol = new TableColumn<>("id");
    public static final TableColumn<EmergencyCall, UUID> unitIdCol = new TableColumn<>("unitId");
    public static final TableColumn<EmergencyCall, LocalDate> dateCol = new TableColumn<>("date");
    public static final TableColumn<EmergencyCall, LocalTime> timeCol = new TableColumn<>("time");
    public static final TableColumn<EmergencyCall, String> localityCol = new TableColumn<>("locality");
    public static final TableColumn<EmergencyCall, String> thoroughfareCol = new TableColumn<>("thoroughfareCol");
    public static final TableColumn<EmergencyCall, String> premiseCol = new TableColumn<>("premiseCol");
    public static final TableColumn<EmergencyCall, String> subPremiseCol = new TableColumn<>("subPremiseCol");
    public static final TableColumn<EmergencyCall, String> causeCol = new TableColumn<>("causeCol");
    public static final TableColumn<EmergencyCall, UUID> callerIdCol = new TableColumn<>("callerId");

    private static final ObservableList<EmergencyCall> callObservableList = FXCollections.observableArrayList();


    public static void SetColumns(TableView<EmergencyCall> dataTable, ObservableList<EmergencyCall> callObservableList ){

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        unitIdCol.setCellValueFactory(new PropertyValueFactory<>("unitId"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        localityCol.setCellValueFactory(new PropertyValueFactory<>("locality"));
        thoroughfareCol.setCellValueFactory(new PropertyValueFactory<>("thoroughfare"));
        premiseCol.setCellValueFactory(new PropertyValueFactory<>("premise"));
        subPremiseCol.setCellValueFactory(new PropertyValueFactory<>("subPremise"));
        causeCol.setCellValueFactory(new PropertyValueFactory<>("cause"));
        callerIdCol.setCellValueFactory(new PropertyValueFactory<>("callerId"));

        dataTable.setItems(callObservableList);
        dataTable.getColumns().addAll(idCol, unitIdCol, dateCol, timeCol, localityCol, thoroughfareCol, premiseCol, subPremiseCol, causeCol, callerIdCol);


    }

    public static void GetByDate(TableView<EmergencyCall> emergencyCallTable, LocalDate dateToSearch, String localityToSearch, String thoroughfareToSearch){
        ResultSet rs = Query.GetCallsByDateQuery(dateToSearch, localityToSearch, thoroughfareToSearch);
        callObservableList.clear();


        HandleRows(rs);

        emergencyCallTable.getColumns().clear();

        EmergencyCallTable.SetColumns(emergencyCallTable, callObservableList);

        emergencyCallTable.getSelectionModel().setCellSelectionEnabled(true);
        emergencyCallTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(emergencyCallTable);
    }

    public static void GetByAddress(TableView<EmergencyCall> emergencyCallTable, String localityToSearch, String thoroughfareToSearch, String premiseToSearch, String subPremiseToSearch){
        ResultSet rs = Query.GetCallByAddress(localityToSearch, thoroughfareToSearch, premiseToSearch, subPremiseToSearch);

        if (rs != null){

            callObservableList.clear();

            HandleRows(rs);

            emergencyCallTable.getColumns().clear();

            EmergencyCallTable.SetColumns(emergencyCallTable, callObservableList);

            emergencyCallTable.getSelectionModel().setCellSelectionEnabled(true);
            emergencyCallTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            TableUtils.installCopyPasteHandler(emergencyCallTable);
        } else {
            System.out.println("[Incorrect selection]");
        }


    }

    public static void GetByUnitId(TableView<EmergencyCall> emergencyCallTable, UUID id){
        ResultSet rs;

        if (id != null){
            PreparedStatement getUnitByEmp = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.CALL_BY_DATE + " WHERE unit_id = ?;"
            );
            BoundStatement boundStatement = getUnitByEmp.bind(id);
            rs = DBConnector.getSession().execute(boundStatement);
        } else {
            String getAllUnits = "SELECT * FROM " + StringResources.CALL_BY_DATE + ";";
            rs = DBConnector.getSession().execute(getAllUnits);
        }

        callObservableList.clear();

        HandleRows(rs);

        emergencyCallTable.getColumns().clear();

        EmergencyCallTable.SetColumns(emergencyCallTable, callObservableList);

        emergencyCallTable.getSelectionModel().setCellSelectionEnabled(true);
        emergencyCallTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(emergencyCallTable);
    }

    private static void HandleRows(ResultSet rs){
        for (Row row : rs){
            callObservableList
                    .add(new EmergencyCall(
                            row.getString("a_locality"), row.getString("a_thoroughfare"), row.getString("a_premise"),
                            row.getString("a_sub_premise"), row.getString("cause"), row.getLocalDate("date"),
                            row.getLocalTime("time"),
                            row.getUuid("id"), row.getUuid("unit_id"), row.getUuid("caller_id")
                    ));

        }
    }

}
