package org.coursework.cassandraambulance.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.coursework.cassandraambulance.Alerts;
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
            System.out.println(e.getMessage());

        }
    }


}
