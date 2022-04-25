package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.coursework.cassandraambulance.*;
import org.coursework.cassandraambulance.models.EmergencyCall;
import org.w3c.dom.views.DocumentView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.UUID;

public class GetByDateController{

    @FXML
    private TableView<EmergencyCall> dataTable;
    @FXML
    private Button getDataButton;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField timeTextField, localityTextField, thoroughfareTextField;
    @FXML
    private Label getCallByAddress;




    private LocalDate dateToSearch = null;
    private LocalTime timeToSearch = null;
    private String localityToSearch = null, thoroughfareToSearch = null;

    private TableColumn<EmergencyCall, UUID> idCol = new TableColumn<EmergencyCall, UUID>("id");
    private TableColumn<EmergencyCall, UUID> unitIdCol = new TableColumn<EmergencyCall, UUID>("unitId");
    private TableColumn<EmergencyCall, LocalDate> dateCol = new TableColumn<EmergencyCall, LocalDate>("date");
    private TableColumn<EmergencyCall, LocalTime> timeCol = new TableColumn<EmergencyCall, LocalTime>("time");
    private TableColumn<EmergencyCall, String> localityCol = new TableColumn<EmergencyCall, String>("locality");
    private TableColumn<EmergencyCall, String> thoroughfareCol = new TableColumn<EmergencyCall, String>("thoroughfareCol");
    private TableColumn<EmergencyCall, String> premiseCol = new TableColumn<EmergencyCall, String>("premiseCol");
    private TableColumn<EmergencyCall, String> subPremiseCol = new TableColumn<EmergencyCall, String>("subPremiseCol");
    private TableColumn<EmergencyCall, String> causeCol = new TableColumn<EmergencyCall, String>("causeCol");
    private TableColumn<EmergencyCall, UUID> callerIdCol = new TableColumn<EmergencyCall, UUID>("callerId");


    private final String tableName = "call_by_date";
    
    @FXML
    protected void GetCallsByDate() {

        GetSearchValues();

//        if (dateToSearch == null && timeToSearch == null && localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty()){
//            final String getCalls = "SELECT * FROM " + tableName + " LIMIT 100";
//            rs = DBConnector.getSession().execute(getCalls);
//        } else if (timeToSearch == null && localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty()) {
//            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
//                    "SELECT * FROM " + tableName + " WHERE date = ? LIMIT 100"
//            );
//            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch);
//            rs = DBConnector.getSession().execute(boundStatement);
//        } else if (localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty()){
//            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
//                    "SELECT * FROM " + tableName + " WHERE date = ? AND time = ? LIMIT 100"
//            );
//            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch, timeToSearch);
//            rs = DBConnector.getSession().execute(boundStatement);
//        } else if (thoroughfareToSearch.isEmpty()) {
//            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
//                    "SELECT * FROM " + tableName + " WHERE date = ? AND time = ? AND a_locality = ? LIMIT 100"
//            );
//            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch, timeToSearch, localityToSearch);
//            rs = DBConnector.getSession().execute(boundStatement);
//        }  else {
//            final String getCalls = "SELECT * FROM " + tableName + " LIMIT 100";
//            rs = DBConnector.getSession().execute(getCalls);
//        }
        ResultSet rs = PrepareAndExecuteStatement();
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

        dataTable.setEditable(true);

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

//
//        final GetCallService getCallsService = new GetCallService();
//        dataTable.itemsProperty().bind(getCallsService.valueProperty());
//        getCallsService.start();

        dataTable.setItems(callObservableList);
        dataTable.getColumns().addAll(idCol, unitIdCol, dateCol, timeCol, localityCol, thoroughfareCol, premiseCol, subPremiseCol, causeCol, callerIdCol);

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


    @FXML
    protected void SwitchToCallByAddress(MouseEvent mouseEvent){
        ViewSwitcher.SwitchToCallByAddress(mouseEvent);
    }

    @FXML
    public void SwitchToAddCall(MouseEvent mouseEvent) {
        ViewSwitcher.SwitchToAddCall(mouseEvent);
    }

    @FXML
    public void SwitchToAddReport(MouseEvent mouseEvent) {
        ViewSwitcher.SwitchToAddReport(mouseEvent);
    }

    public void initialize(){





        dateCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<EmergencyCall, LocalDate>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<EmergencyCall, LocalDate> t) {
                        ((EmergencyCall) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setDate(t.getNewValue());
                    }
                }
        );
        timeCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<EmergencyCall, LocalTime>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<EmergencyCall, LocalTime> t) {
                        ((EmergencyCall) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setTime(t.getNewValue());
                    }
                }
        );
        unitIdCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<EmergencyCall, UUID>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<EmergencyCall, UUID> t) {
                        ((EmergencyCall) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setUnitId(t.getNewValue());
                    }
                }
        );
        localityCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<EmergencyCall, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<EmergencyCall, String> t) {
                        ((EmergencyCall) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setLocality(t.getNewValue());
                    }
                }
        );

        thoroughfareCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<EmergencyCall, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<EmergencyCall, String> t) {
                        ((EmergencyCall) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setThoroughfare(t.getNewValue());
                    }
                }
        );
        premiseCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<EmergencyCall, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<EmergencyCall, String> t) {
                        ((EmergencyCall) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setPremise(t.getNewValue());
                    }
                }
        );
        subPremiseCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<EmergencyCall, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<EmergencyCall, String> t) {
                        ((EmergencyCall) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setSubPremise(t.getNewValue());
                    }
                }
        );
        causeCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<EmergencyCall, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<EmergencyCall, String> t) {
                        ((EmergencyCall) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setCause(t.getNewValue());
                    }
                }
        );
        callerIdCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<EmergencyCall, UUID>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<EmergencyCall, UUID> t) {
                        ((EmergencyCall) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setCallerId(t.getNewValue());
                    }
                }
        );

    }

    protected ResultSet PrepareAndExecuteStatement(){
        ResultSet rs;
        if (dateToSearch == null && timeToSearch == null && localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty()){
            final String getCalls = "SELECT * FROM " + tableName + " LIMIT 100";
            rs = DBConnector.getSession().execute(getCalls);
        } else if (timeToSearch == null && localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty()) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + tableName + " WHERE date = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty()){
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + tableName + " WHERE date = ? AND time = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch, timeToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (thoroughfareToSearch.isEmpty()) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + tableName + " WHERE date = ? AND time = ? AND a_locality = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch, timeToSearch, localityToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        }  else {
            final String getCalls = "SELECT * FROM " + tableName + " LIMIT 100";
            rs = DBConnector.getSession().execute(getCalls);
        }
        return rs;
    }


    public void SwitchToUpdateCall(MouseEvent mouseEvent) {
        ViewSwitcher.Switch(mouseEvent, "update_call_view.fxml", "/style.css");
    }

    public void SwitchToUpdateReport(MouseEvent mouseEvent) {
    }
}