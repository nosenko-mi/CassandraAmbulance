package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.coursework.cassandraambulance.Alerts;
import org.coursework.cassandraambulance.DBConnector;
import org.coursework.cassandraambulance.PreparedStatements;
import org.coursework.cassandraambulance.models.Unit;
import org.coursework.cassandraambulance.tables.UnitTable;

import java.util.UUID;

public class GetUnitByIdController extends Controller {

    public TextField unitIdTextField;
    public TableView<Unit> unitTable;
    private UUID unitId;

    public void GetUnitById(ActionEvent event) {
        try {
            unitId = UUID.fromString(unitIdTextField.getText());
            UnitTable.GetById(unitTable, unitId) ;

        } catch (IllegalArgumentException e){
            unitId = null;
            Alerts.ParseError("Unit id can't be parsed");
            UnitTable.GetAll(unitTable);
            System.out.println(e.getMessage());

        }
    }


    public void DeleteUnit(ActionEvent event) {
        try {
            unitId = UUID.fromString(unitIdTextField.getText());
            BoundStatement boundStatement = PreparedStatements.deleteUnit.bind(unitId);

            DBConnector.getSession().execute(boundStatement);
            Alerts.SucceedOperation();

        } catch (IllegalArgumentException e){
            unitId = null;
            Alerts.ParseError("Unit id can't be parsed");
            System.out.println(e.getMessage());

        } catch (Exception e){
            unitId = null;
            Alerts.FailedOperation();
            System.out.println(e.getMessage());

        }
    }
}
