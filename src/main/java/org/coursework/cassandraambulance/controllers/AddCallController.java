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
import org.coursework.cassandraambulance.DBConnector;
import org.coursework.cassandraambulance.GetUnitService;
import org.coursework.cassandraambulance.StringResources;
import org.coursework.cassandraambulance.TableUtils;
import org.coursework.cassandraambulance.models.Unit;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class AddCallController extends Controller {

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
    private final TableColumn<Unit, String> doctorFn = new TableColumn<>("doctorFn");
    private final TableColumn<Unit, String> doctorMn = new TableColumn<>("doctorMn");
    private final TableColumn<Unit, String> doctorLn = new TableColumn<>("doctorLn");
    private final TableColumn<Unit, String> orderlyFn = new TableColumn<>("orderlyFn");
    private final TableColumn<Unit, String> orderlyMn = new TableColumn<>("orderlyMn");
    private final TableColumn<Unit, String> orderlyLn = new TableColumn<>("orderlyLn");
    private final TableColumn<Unit, String> driverFn = new TableColumn<>("driverFn");
    private final TableColumn<Unit, String> driverMn = new TableColumn<>("driverMn");
    private final TableColumn<Unit, String> driverLn = new TableColumn<>("driverLn");
    private final TableColumn<Unit, String> carSerialNumberCol = new TableColumn<>("carSerialNumber");

    public void GetUnits(ActionEvent event) {

        dataTable.getColumns().clear();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        doctorIdCol.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        orderlyIdCol.setCellValueFactory(new PropertyValueFactory<>("orderlyId"));
        driverIdCol.setCellValueFactory(new PropertyValueFactory<>("driverId"));
        carIdCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
        doctorFn.setCellValueFactory(new PropertyValueFactory<>("doctorFn"));
        doctorMn.setCellValueFactory(new PropertyValueFactory<>("doctorMn"));
        doctorLn.setCellValueFactory(new PropertyValueFactory<>("doctorLn"));
        orderlyFn.setCellValueFactory(new PropertyValueFactory<>("orderlyFn"));
        orderlyMn.setCellValueFactory(new PropertyValueFactory<>("orderlyMn"));
        orderlyLn.setCellValueFactory(new PropertyValueFactory<>("orderlyLn"));
        driverFn.setCellValueFactory(new PropertyValueFactory<>("driverFn"));
        driverMn.setCellValueFactory(new PropertyValueFactory<>("driverMn"));
        driverLn.setCellValueFactory(new PropertyValueFactory<>("driverLn"));
        carSerialNumberCol.setCellValueFactory(new PropertyValueFactory<>("carSerialNumber"));

        final GetUnitService getUnitsService = new GetUnitService();
        dataTable.itemsProperty().bind(getUnitsService.valueProperty());
        getUnitsService.start();
        

        dataTable.getColumns().addAll(
                idCol, doctorIdCol, orderlyIdCol, carIdCol,
                doctorFn, doctorMn, doctorLn, orderlyFn, orderlyMn, orderlyLn, driverFn, driverMn, driverLn, carSerialNumberCol);

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
                                "INSERT INTO " + StringResources.CALL_BY_DATE +
                                        "(date, time, a_locality, a_thoroughfare, a_premise, a_sub_premise, id, cause, unit_id, caller_id)" +
                                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

        BoundStatement boundStatement = addCallStatement
                .bind(
                        LocalDate.now(), LocalTime.now(), localityTextField.getText(),
                        thoroughfareTextField.getText(), premiseTextField.getText(),
                        subPremiseTextField.getText(), UUID.randomUUID(), causeTextField.getText(),
                        UUID.fromString(unitIdTextField.getText()) , callerUuid);

        DBConnector.getSession().execute(boundStatement);
        // додати звіт до таблиці call_by_address
        addCallStatement =
                DBConnector
                        .getSession()
                        .prepare(
                                "INSERT INTO " + StringResources.CALL_BY_ADDRESS +
                                        "(date, time, a_locality, a_thoroughfare, a_premise, a_sub_premise, id, cause, unit_id, caller_id)" +
                                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

        boundStatement = addCallStatement
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
                                "INSERT INTO " + StringResources.PERSONS +
                                        " (id, type, first_name, middle_name, last_name)" +
                                        "VALUES(?, ?, ?, ?, ?);");
        BoundStatement boundStatement = addCallerStatement
                .bind(callerUuid, "Викликач", callerFnTextField.getText(), callerMnTextField.getText(), callerLnTextField.getText());
        DBConnector.getSession().execute(boundStatement);
        System.out.println("[Caller added]");

    }

}
