package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.coursework.cassandraambulance.*;
import org.coursework.cassandraambulance.models.Unit;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class AddCallController {

    @FXML
    public TextField thoroughfareTextField;
    @FXML
    public TextField localityTextField;
    @FXML
    public TextField subPremiseTextField;
    @FXML
    public TextField premiseTextField;
    @FXML
    public TextField unitIdTextField;
    @FXML
    public TextField causeTextField;
    @FXML
    private TextField callerMnTextField;
    @FXML
    private TextField callerFnTextField;
    @FXML
    private TextField callerLnTextField;
    @FXML
    private TableView<Unit> dataTable;

    private final TableColumn<Unit, UUID> idCol = new TableColumn<>("id");
    private final TableColumn<Unit, UUID> doctorIdCol = new TableColumn<>("doctorId");
    private final TableColumn<Unit, UUID> orderlyIdCol = new TableColumn<>("orderlyId");
    private final TableColumn<Unit, UUID> driverIdCol = new TableColumn<>("driverId");
    private final TableColumn<Unit, UUID> carIdCol = new TableColumn<>("carId");
    private final TableColumn<Unit, String> doctorFN = new TableColumn<>("doctorFN");
    private final TableColumn<Unit, String> doctorMN = new TableColumn<>("doctorMN");
    private final TableColumn<Unit, String> doctorLN = new TableColumn<>("doctorLN");
    private final TableColumn<Unit, String> orderlyFN = new TableColumn<>("orderlyFN");
    private final TableColumn<Unit, String> orderlyMN = new TableColumn<>("orderlyMN");
    private final TableColumn<Unit, String> orderlyLN = new TableColumn<>("orderlyLN");
    private final TableColumn<Unit, String> driverFN = new TableColumn<>("driverFN");
    private final TableColumn<Unit, String> driverMN = new TableColumn<>("driverMN");
    private final TableColumn<Unit, String> driverLN = new TableColumn<>("driverLN");
    private final TableColumn<Unit, String> carSerialNumberCol = new TableColumn<>("carSerialNumber");

    public void GetUnits(ActionEvent event) {

        dataTable.getColumns().clear();


        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        doctorIdCol.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        orderlyIdCol.setCellValueFactory(new PropertyValueFactory<>("orderlyId"));
        driverIdCol.setCellValueFactory(new PropertyValueFactory<>("driverId"));
        carIdCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
        doctorFN.setCellValueFactory(new PropertyValueFactory<>("doctorFN"));
        doctorMN.setCellValueFactory(new PropertyValueFactory<>("doctorMN"));
        doctorLN.setCellValueFactory(new PropertyValueFactory<>("doctorLN"));
        orderlyFN.setCellValueFactory(new PropertyValueFactory<>("orderlyFN"));
        orderlyMN.setCellValueFactory(new PropertyValueFactory<>("orderlyMN"));
        orderlyLN.setCellValueFactory(new PropertyValueFactory<>("orderlyLN"));
        driverFN.setCellValueFactory(new PropertyValueFactory<>("driverFN"));
        driverMN.setCellValueFactory(new PropertyValueFactory<>("driverMN"));
        driverLN.setCellValueFactory(new PropertyValueFactory<>("driverLN"));
        carSerialNumberCol.setCellValueFactory(new PropertyValueFactory<>("carSerialNumber"));

        final GetUnitService getUnitsService = new GetUnitService();
        dataTable.itemsProperty().bind(getUnitsService.valueProperty());
        getUnitsService.start();

        dataTable.getColumns().addAll(
                idCol, doctorIdCol, orderlyIdCol, carIdCol,
                doctorFN, doctorMN, doctorLN, orderlyFN, orderlyMN, orderlyLN, driverFN, driverMN, driverLN, carSerialNumberCol);

        dataTable.getSelectionModel().setCellSelectionEnabled(true);
        dataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableUtils.installCopyPasteHandler(dataTable);

    }

    public void AddCall(ActionEvent event) {

        UUID callerUuid = UUID.randomUUID(); // Uuid человека, который вызвал бригаду
        addCaller(callerUuid);

        PreparedStatement addCallStatement =
                DBConnector
                        .getSession()
                        .prepare(
                                "INSERT INTO " + TableName.CALL_BY_DATE +
                                        "(date, time, a_locality, a_thoroughfare, a_premise, a_sub_premise, id, cause, unit_id, caller_id)" +
                                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

        BoundStatement boundStatement = addCallStatement
                .bind(
                        LocalDate.now(), LocalTime.now(), localityTextField.getText(),
                        thoroughfareTextField.getText(), premiseTextField.getText(),
                        subPremiseTextField.getText(), UUID.randomUUID(), causeTextField.getText(),
                        UUID.fromString(unitIdTextField.getText()) , callerUuid);

        DBConnector.getSession().execute(boundStatement);
        System.out.println("[Call added]");
    }

    public void addCaller(UUID callerUuid){
        PreparedStatement addCallerStatement =
                DBConnector
                        .getSession()
                        .prepare(
                                "INSERT INTO " + TableName.PERSONS +
                                        " (id, type, first_name, middle_name, last_name)" +
                                        "VALUES(?, ?, ?, ?, ?);");
        BoundStatement boundStatement = addCallerStatement
                .bind(callerUuid, "Викликач", callerFnTextField.getText(), callerMnTextField.getText(), callerLnTextField.getText());
        DBConnector.getSession().execute(boundStatement);
        System.out.println("[Caller added]");

    }


    public void SwitchToCallByDate(MouseEvent mouseEvent){
        ViewSwitcher.SwitchToCallByDate(mouseEvent);
    }

    public void SwitchToCallByAddress(MouseEvent mouseEvent){
        ViewSwitcher.SwitchToCallByAddress(mouseEvent);
    }

    public void SwitchToAddReport(MouseEvent mouseEvent){
        ViewSwitcher.SwitchToAddReport(mouseEvent);
    }

    public void GetSearchDate(ActionEvent event) {
    }
}
