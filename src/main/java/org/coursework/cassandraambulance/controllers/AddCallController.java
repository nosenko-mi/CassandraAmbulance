package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.coursework.cassandraambulance.Alerts;
import org.coursework.cassandraambulance.DBConnector;
import org.coursework.cassandraambulance.PreparedStatements;
import org.coursework.cassandraambulance.StringResources;
import org.coursework.cassandraambulance.models.Unit;
import org.coursework.cassandraambulance.tables.UnitTable;

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

    private UUID unitId;


    public void GetUnits(ActionEvent event) {

//        final GetUnitService getUnitsService = new GetUnitService();
//        dataTable.itemsProperty().bind(getUnitsService.valueProperty());
//        getUnitsService.start();
        UnitTable.GetAll(dataTable);

    }

    public void AddCall(ActionEvent event) {

        GetCallValues();

        // Uuid людини, що викликала бригаду швидкої
        UUID callerUuid = UUID.randomUUID();

        // додати викликача, якщо додано - додати виклик
        if (unitId != null && addCaller(callerUuid)){
            // додати звіт до таблиці call_by_date

            BoundStatement boundStatement = PreparedStatements.AddCallToCallByDate.bind(
                    LocalDate.now(), LocalTime.now(), localityTextField.getText(),
                    thoroughfareTextField.getText(), premiseTextField.getText(),
                    subPremiseTextField.getText(), UUID.randomUUID(), causeTextField.getText(),
                    unitId , callerUuid
            );

            DBConnector.getSession().execute(boundStatement);

            // додати звіт до таблиці call_by_address
            boundStatement = PreparedStatements.AddCallToCallByAddress.bind(
                    LocalDate.now(), LocalTime.now(), localityTextField.getText(),
                    thoroughfareTextField.getText(), premiseTextField.getText(),
                    subPremiseTextField.getText(), UUID.randomUUID(), causeTextField.getText(),
                    UUID.fromString(unitIdTextField.getText()) , callerUuid
            );

            DBConnector.getSession().execute(boundStatement);

            Alerts.SucceedOperation();
        }
        Alerts.MissingPrimaryKey("Unit id is missing");
    }

    public boolean addCaller(UUID callerUuid){

        try {
            BoundStatement boundStatement = PreparedStatements.AddCallerToPersons.bind(
                    callerUuid, StringResources.CALLER_TYPE, callerFnTextField.getText(), callerMnTextField.getText(), callerLnTextField.getText()
            );
            DBConnector.getSession().execute(boundStatement);
            return true;
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return false;
    }

    private void GetCallValues(){
        try {
            unitId = UUID.fromString(unitIdTextField.getText());
        } catch (IllegalArgumentException e){
            unitId = null;
            System.out.println(e.getMessage());
        }
    }
}
