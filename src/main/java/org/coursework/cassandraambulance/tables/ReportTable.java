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
import org.coursework.cassandraambulance.StringResources;
import org.coursework.cassandraambulance.TableUtils;
import org.coursework.cassandraambulance.models.Report;

import java.time.LocalTime;
import java.util.UUID;

// клас, що представляє таблицю звітів
// необхідний для створення столбців у існуючій таблиці, а також для отримання та внесення даних у таблицю
public class ReportTable {

    public static final TableColumn<Report, UUID> idCol = new TableColumn<>("Id");
    public static final TableColumn<Report, UUID> callIdCol = new TableColumn<>("Call id ");
    public static final TableColumn<Report, UUID> patientIdCol = new TableColumn<>("Patient id ");
    public static final TableColumn<Report, UUID> unitIdCol = new TableColumn<>("Unit id ");

    public static final TableColumn<Report, String> localityCol = new TableColumn<>("Locality");
    public static final TableColumn<Report, String> thoroughfareCol = new TableColumn<>("Thoroughfare");
    public static final TableColumn<Report, String> premiseCol = new TableColumn<>("Premise");
    public static final TableColumn<Report, String> subPremiseCol = new TableColumn<>("Sub premise");
    public static final TableColumn<Report, LocalTime> departureTimeCol = new TableColumn<>("Departure");
    public static final TableColumn<Report, LocalTime> arrivalTimeCol = new TableColumn<>("Arrival");
    public static final TableColumn<Report, LocalTime> completionTimeCol = new TableColumn<>("Completion");
    public static final TableColumn<Report, LocalTime> returnTimeCol = new TableColumn<>("Return");
    public static final TableColumn<Report, String> resultCol = new TableColumn<>("Result");
    public static final TableColumn<Report, String> hospitalizationCol = new TableColumn<>("Hospitalization");
    public static final TableColumn<Report, String> diagnosisCol = new TableColumn<>("Diagnosis");
    public static final TableColumn<Report, String> diagnosisCodeCol = new TableColumn<>("Diagnosis code");
    public static final TableColumn<Report, String> traumaCol = new TableColumn<>("Trauma");
    public static final TableColumn<Report, String> onsetCol = new TableColumn<>("Onset");
    public static final TableColumn<Report, String> appliedBeforeCol = new TableColumn<>("Applied before");
    public static final TableColumn<Report, String> fruitlessCol = new TableColumn<>("Fruitless");

    private static ObservableList<Report> reportObservableList = FXCollections.observableArrayList();


    public static void SetColumns(TableView<Report> dataTable, ObservableList<Report> callObservableList ){

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        callIdCol.setCellValueFactory(new PropertyValueFactory<>("callId"));
        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        unitIdCol.setCellValueFactory(new PropertyValueFactory<>("unitId"));
        localityCol.setCellValueFactory(new PropertyValueFactory<>("locality"));
        thoroughfareCol.setCellValueFactory(new PropertyValueFactory<>("thoroughfare"));
        premiseCol.setCellValueFactory(new PropertyValueFactory<>("premise"));
        subPremiseCol.setCellValueFactory(new PropertyValueFactory<>("subPremise"));
        departureTimeCol.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        arrivalTimeCol.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        completionTimeCol.setCellValueFactory(new PropertyValueFactory<>("completionTime"));
        returnTimeCol.setCellValueFactory(new PropertyValueFactory<>("returnTime"));
        resultCol.setCellValueFactory(new PropertyValueFactory<>("resultStatus"));
        hospitalizationCol.setCellValueFactory(new PropertyValueFactory<>("hospitalizationStatus"));
        diagnosisCol.setCellValueFactory(new PropertyValueFactory<>("preliminaryDiagnosis"));
        diagnosisCodeCol.setCellValueFactory(new PropertyValueFactory<>("diagnosisCode"));
        traumaCol.setCellValueFactory(new PropertyValueFactory<>("traumaStatus"));
        onsetCol.setCellValueFactory(new PropertyValueFactory<>("onsetStatus"));
        appliedBeforeCol.setCellValueFactory(new PropertyValueFactory<>("appliedBeforeStatus"));
        fruitlessCol.setCellValueFactory(new PropertyValueFactory<>("fruitlessStatus"));

        dataTable.setItems(callObservableList);
        dataTable.getColumns().addAll(
                idCol, callIdCol, patientIdCol, unitIdCol,
                localityCol, thoroughfareCol, premiseCol, subPremiseCol,
                departureTimeCol, resultCol, hospitalizationCol, diagnosisCol, diagnosisCodeCol,
                traumaCol, onsetCol, appliedBeforeCol, fruitlessCol);
    }


    public static void GetByCall(TableView<Report> reportTable, UUID callId){
        ResultSet rs;

        if (callId != null){
            PreparedStatement getReportByCall = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.REPORT_BY_CALL + " WHERE call_id = ? LIMIT 100;");
            BoundStatement boundStatement = getReportByCall.bind(callId);
            rs = DBConnector.getSession().execute(boundStatement);

        } else {
            String getAllReports = "SELECT * FROM " + StringResources.REPORT_BY_CALL + " LIMIT 100;";
            rs = DBConnector.getSession().execute(getAllReports);
        }

        reportObservableList.clear();

        HandleRows(rs);

        reportTable.getColumns().clear();

        ReportTable.SetColumns(reportTable, reportObservableList);

        reportTable.getSelectionModel().setCellSelectionEnabled(true);
        reportTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(reportTable);

    }

    public static void  GetByHosp(TableView<Report> reportTable, String hospitalizationStatus){
        ResultSet rs;
        if (!hospitalizationStatus.equals("Hospitalization")){
            PreparedStatement getReports = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.REPORT_BY_CALL + " WHERE hospitalization_status = ? ;"
            );
            BoundStatement boundStatement = getReports.bind(hospitalizationStatus);
            rs = DBConnector.getSession().execute(boundStatement);

        } else {
            String getAllReports = "SELECT * FROM " + StringResources.REPORT_BY_CALL + " LIMIT 100;";
            rs = DBConnector.getSession().execute(getAllReports);
        }

        reportObservableList.clear();

        HandleRows(rs);

        reportTable.getColumns().clear();

        ReportTable.SetColumns(reportTable, reportObservableList);

        reportTable.getSelectionModel().setCellSelectionEnabled(true);
        reportTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(reportTable);
    }

    private static void HandleRows(ResultSet rs){
        for (Row row : rs){
            reportObservableList
                    .add(new Report(
                            row.getUuid("id"), row.getUuid("call_id"), row.getUuid("patient_id"),
                            row.getUuid("unit_id"), row.getString("preliminary_diagnosis"),
                            row.getString("diagnosis_code"), row.getString("result"), row.getString("hospitalization_status"),
                            row.getString("trauma"), row.getString("onset"), row.getString("applied_before"),
                            row.getString("fruitless"), row.getString("a_locality"), row.getString("a_thoroughfare"),
                            row.getString("a_premise"), row.getString("a_sub_premise"),
                            row.getLocalTime("departure_time"), row.getLocalTime("arrival_time"),
                            row.getLocalTime("completion_time"), row.getLocalTime("return_time")

                    ));
        }
    }

}
