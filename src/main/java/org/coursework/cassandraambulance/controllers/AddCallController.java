package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.coursework.cassandraambulance.DBConnector;
import org.coursework.cassandraambulance.GetUnitService;
import org.coursework.cassandraambulance.TableUtils;
import org.coursework.cassandraambulance.ViewSwitcher;
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
    private TableView dataTable;

    private TableColumn<Unit, UUID> idCol = new TableColumn<Unit, UUID>("id");
    private TableColumn<Unit, UUID> doctorIdCol = new TableColumn<Unit, UUID>("doctorId");
    private TableColumn<Unit, UUID> orderlyIdCol = new TableColumn<Unit, UUID>("orderlyId");
    private TableColumn<Unit, UUID> driverIdCol = new TableColumn<Unit, UUID>("driverId");
    private TableColumn<Unit, UUID> carIdCol = new TableColumn<Unit, UUID>("carId");
    private TableColumn<Unit, String> doctorFN = new TableColumn<Unit, String>("doctorFN");
    private TableColumn<Unit, String> doctorMN = new TableColumn<Unit, String>("doctorMN");
    private TableColumn<Unit, String> doctorLN = new TableColumn<Unit, String>("doctorLN");
    private TableColumn<Unit, String> orderlyFN = new TableColumn<Unit, String>("orderlyFN");
    private TableColumn<Unit, String> orderlyMN = new TableColumn<Unit, String>("orderlyMN");
    private TableColumn<Unit, String> orderlyLN = new TableColumn<Unit, String>("orderlyLN");
    private TableColumn<Unit, String> driverFN = new TableColumn<Unit, String>("driverFN");
    private TableColumn<Unit, String> driverMN = new TableColumn<Unit, String>("driverMN");
    private TableColumn<Unit, String> driverLN = new TableColumn<Unit, String>("driverLN");
    private TableColumn<Unit, String> carSerialNumberCol = new TableColumn<Unit, String>("carSerialNumber");

    public void GetUnits(ActionEvent event) {

        dataTable.getColumns().clear();


        idCol.setCellValueFactory(new PropertyValueFactory<Unit, UUID>("id"));
        doctorIdCol.setCellValueFactory(new PropertyValueFactory<Unit, UUID>("doctorId"));
        orderlyIdCol.setCellValueFactory(new PropertyValueFactory<Unit, UUID>("orderlyId"));
        driverIdCol.setCellValueFactory(new PropertyValueFactory<Unit, UUID>("driverId"));
        carIdCol.setCellValueFactory(new PropertyValueFactory<Unit, UUID>("carId"));
        doctorFN.setCellValueFactory(new PropertyValueFactory<Unit, String>("doctorFN"));
        doctorMN.setCellValueFactory(new PropertyValueFactory<Unit, String>("doctorMN"));
        doctorLN.setCellValueFactory(new PropertyValueFactory<Unit, String>("doctorLN"));
        orderlyFN.setCellValueFactory(new PropertyValueFactory<Unit, String>("orderlyFN"));
        orderlyMN.setCellValueFactory(new PropertyValueFactory<Unit, String>("orderlyMN"));
        orderlyLN.setCellValueFactory(new PropertyValueFactory<Unit, String>("orderlyLN"));
        driverFN.setCellValueFactory(new PropertyValueFactory<Unit, String>("driverFN"));
        driverMN.setCellValueFactory(new PropertyValueFactory<Unit, String>("driverMN"));
        driverLN.setCellValueFactory(new PropertyValueFactory<Unit, String>("driverLN"));
        carSerialNumberCol.setCellValueFactory(new PropertyValueFactory<Unit, String>("carSerialNumber"));

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
                                "INSERT INTO ambulance_ver3.call_by_date" +
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
                                "INSERT INTO persons" +
                                        "(id, type, first_name, middle_name, last_name)" +
                                        "VALUES(?, ?, ?, ?, ?);");
        BoundStatement boundStatement = addCallerStatement
                .bind(callerUuid, "Викликач", callerFnTextField.getText(), callerMnTextField.getText(), callerLnTextField.getText());
        DBConnector.getSession().execute(boundStatement);
        System.out.println("[Caller added]");

    }


    public void SwitchToCallByDate(MouseEvent mouseEvent) throws IOException {
        ViewSwitcher.SwitchToCallByDate(mouseEvent);
    }

    public void SwitchToCallByAddress(MouseEvent mouseEvent) throws IOException {
        ViewSwitcher.SwitchToCallByAddress(mouseEvent);
    }

    public void SwitchToAddReport(MouseEvent mouseEvent) throws IOException {
        ViewSwitcher.SwitchToAddReport(mouseEvent);
    }

    public void InitMenuButtons(){

    }


    public void GetSearchDate(ActionEvent event) {
    }
}
