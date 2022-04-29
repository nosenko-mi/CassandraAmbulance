package org.coursework.cassandraambulance.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.coursework.cassandraambulance.models.Unit;
import org.coursework.cassandraambulance.tables.UnitTable;

import java.util.UUID;

public class GetUnitByIdController extends Controller {

    public TextField unitIdTextField;
    public TableView unitTable;
    private UUID unitId;

    public void GetUnitById(ActionEvent event) {
        try {
            unitId = UUID.fromString(unitIdTextField.getText());
        } catch (IllegalArgumentException e){
            unitId = null;
            System.out.println(e);
        }
        UnitTable.GetById(unitTable, unitId) ;
    }


}
