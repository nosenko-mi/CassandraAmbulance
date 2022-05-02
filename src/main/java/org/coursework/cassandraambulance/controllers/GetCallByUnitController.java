package org.coursework.cassandraambulance.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.coursework.cassandraambulance.tables.EmergencyCallTable;

import java.util.UUID;

public class GetCallByUnitController extends Controller{
    public TextField unitIdTextField;
    public TableView dataTable;


    public void GetCallsByUnitId(ActionEvent event) {
        try {
            UUID unitId = UUID.fromString(unitIdTextField.getText());
            EmergencyCallTable.GetByUnitId(dataTable, unitId);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }


}
