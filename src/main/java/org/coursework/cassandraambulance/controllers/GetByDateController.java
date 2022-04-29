package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.querybuilder.select.SelectFrom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.coursework.cassandraambulance.*;
import org.coursework.cassandraambulance.models.EmergencyCall;
import org.coursework.cassandraambulance.tables.EmergencyCallTable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;

public class GetByDateController extends Controller {

    @FXML
    private TableView<EmergencyCall> dataTable;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField timeTextField, localityTextField, thoroughfareTextField;




    private LocalDate dateToSearch = null;
    private LocalTime timeToSearch = null;
    private String localityToSearch = null, thoroughfareToSearch = null;

//    private final TableColumn<EmergencyCall, UUID> idCol = new TableColumn<EmergencyCall, UUID>("id");
//    private final TableColumn<EmergencyCall, UUID> unitIdCol = new TableColumn<EmergencyCall, UUID>("unitId");
//    private final TableColumn<EmergencyCall, LocalDate> dateCol = new TableColumn<EmergencyCall, LocalDate>("date");
//    private final TableColumn<EmergencyCall, LocalTime> timeCol = new TableColumn<EmergencyCall, LocalTime>("time");
//    private final TableColumn<EmergencyCall, String> localityCol = new TableColumn<EmergencyCall, String>("locality");
//    private final TableColumn<EmergencyCall, String> thoroughfareCol = new TableColumn<EmergencyCall, String>("thoroughfareCol");
//    private final TableColumn<EmergencyCall, String> premiseCol = new TableColumn<EmergencyCall, String>("premiseCol");
//    private final TableColumn<EmergencyCall, String> subPremiseCol = new TableColumn<EmergencyCall, String>("subPremiseCol");
//    private final TableColumn<EmergencyCall, String> causeCol = new TableColumn<EmergencyCall, String>("causeCol");
//    private final TableColumn<EmergencyCall, UUID> callerIdCol = new TableColumn<EmergencyCall, UUID>("callerId");



    @FXML
    protected void GetCallsByDate() {

        GetSearchValues();

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

        dataTable.getColumns().clear();

        EmergencyCallTable.SetColumns(dataTable, callObservableList);

        dataTable.getSelectionModel().setCellSelectionEnabled(true);
        dataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(dataTable);

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




    protected ResultSet PrepareAndExecuteStatement(){
        ResultSet rs;
        if (dateToSearch == null && localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty()){

//            StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
//                    .append("booksByTitle").append("(")
//                    .append("id uuid, ")
//                    .append("title text,")
//                    .append("PRIMARY KEY (title, id));");

            final String getCalls = "SELECT * FROM " + StringResources.CALL_BY_DATE + " LIMIT 100";
            rs = DBConnector.getSession().execute(getCalls);
            SimpleStatement simpleStatement = (SimpleStatement) selectFrom(StringResources.CALL_BY_DATE).all().limit(100);
            SelectFrom selectCall =
                    (SelectFrom)
                    selectFrom(StringResources.CALL_BY_DATE)
                    .all()
                    .limit(100);



        } else if (dateToSearch == null && thoroughfareToSearch.isEmpty()) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.CALL_BY_DATE + " WHERE a_locality = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(localityToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (dateToSearch == null && localityToSearch.isEmpty()) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.CALL_BY_DATE + " WHERE a_thoroughfare = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(thoroughfareToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (dateToSearch == null) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.CALL_BY_DATE + " WHERE a_locality = ? AND a_thoroughfare = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind( localityToSearch,thoroughfareToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if ( localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty()){
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.CALL_BY_DATE + " WHERE date = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        }  else if (thoroughfareToSearch.isEmpty()) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.CALL_BY_DATE + " WHERE date = ?  AND a_locality = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch, localityToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (localityToSearch.isEmpty()) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.CALL_BY_DATE + " WHERE date = ?  AND a_thoroughfare = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch, thoroughfareToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.CALL_BY_DATE + " WHERE date = ?  AND a_locality = ? AND a_thoroughfare = ? LIMIT 100 ALLOW FILTERING"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch, localityToSearch, thoroughfareToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        }
        return rs;
    }


//    @FXML
//    protected void SwitchToCallByAddress(MouseEvent mouseEvent){
//        ViewSwitcher.SwitchToCallByAddress(mouseEvent);
//    }
//
//    @FXML
//    public void SwitchToAddCall(MouseEvent mouseEvent) {
//        ViewSwitcher.Switch(mouseEvent, "add_call_view.fxml", "/style.css");
//
//    }
//
////    @FXML
////    public void SwitchToAddReport(MouseEvent mouseEvent) {
////        ViewSwitcher.Switch(mouseEvent, "add_report_view.fxml", "/style.css");
////
////    }
//
//    public void SwitchToUpdateCall(MouseEvent mouseEvent) {
//        ViewSwitcher.Switch(mouseEvent, "update_call_view.fxml", "/style.css");
//    }
//
//    public void SwitchToUpdateReport(MouseEvent mouseEvent) {
//    }
//
//    public void SwitchToReportByCall(MouseEvent mouseEvent) {
//        ViewSwitcher.Switch(mouseEvent, "report_by_call_view.fxml", "/style.css");
//    }
//
//    public void SwitchToUnitByEmployee(MouseEvent mouseEvent) {
//        ViewSwitcher.Switch(mouseEvent, "unit_by_emp_view.fxml", "/style.css");
//
//    }
//
//    public void SwitchToGetPersons(MouseEvent mouseEvent) {
//    }

}