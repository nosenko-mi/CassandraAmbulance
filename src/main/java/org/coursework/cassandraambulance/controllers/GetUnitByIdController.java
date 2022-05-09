package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.coursework.cassandraambulance.Alerts;
import org.coursework.cassandraambulance.DBConnector;
import org.coursework.cassandraambulance.PreparedStatements;
import org.coursework.cassandraambulance.Query;
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

            ResultSet rs = Query.GetUnitById(unitId);
            Row row = rs.one();

            if (rs != null){

                Unit unit = new Unit(
                        row.getUuid("id"), row.getUuid("doctor_id"), row.getUuid("orderly_id"), row.getUuid("driver_id"), row.getUuid("car_id"),
                        row.getString("doctor_first_name"), row.getString("doctor_middle_name"), row.getString("doctor_last_name"),
                        row.getString("orderly_first_name"), row.getString("orderly_middle_name"), row.getString("orderly_last_name"),
                        row.getString("driver_first_name"), row.getString("driver_middle_name"), row.getString("driver_last_name"),
                        row.getString("car_serial_number")
                );


                BoundStatement boundStatement = PreparedStatements.deleteUnit.bind(unitId);

                DBConnector.getSession().execute(boundStatement);

                boundStatement = PreparedStatements.deleteFromUnitByEmp.bind(unit.getDoctorId(),unit.getId());
                DBConnector.getSession().execute(boundStatement);
                boundStatement = PreparedStatements.deleteFromUnitByEmp.bind(unit.getDriverId(),unit.getId());
                DBConnector.getSession().execute(boundStatement);
                boundStatement = PreparedStatements.deleteFromUnitByEmp.bind(unit.getOrderlyId(),unit.getId());
                DBConnector.getSession().execute(boundStatement);
                boundStatement = PreparedStatements.deleteFromUnitByEmp.bind(unit.getCarId(),unit.getId());
                DBConnector.getSession().execute(boundStatement);
                Alerts.SucceedOperation();

            } else {
                Alerts.FailedOperation();
            }


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
