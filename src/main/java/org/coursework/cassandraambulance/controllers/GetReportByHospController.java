package org.coursework.cassandraambulance.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import org.coursework.cassandraambulance.tables.ReportTable;

public class GetReportByHospController extends Controller{

    public MenuButton hospitalizationMenuButton;
    public TableView reportTable;

    private String hospitalizationStatus;

    public void GetReportByHosp(ActionEvent event) {
        GetSearchData();

        ReportTable.GetByHosp(reportTable, hospitalizationStatus);

    }

    private void GetSearchData(){

        hospitalizationStatus = hospitalizationMenuButton.getText();

    }

    private void InitMenyButtons(){
        MenuItem hospItemY = new MenuItem("Госпіталізовано");
        MenuItem hospItemN = new MenuItem("Не госпіталізовано");
        hospitalizationMenuButton.getItems().addAll(hospItemY, hospItemN);
        hospItemN.setOnAction(event -> hospitalizationMenuButton.setText(hospItemN.getText()));
        hospItemY.setOnAction(event -> hospitalizationMenuButton.setText(hospItemY.getText()));
    }

    public void initialize(){
        InitMenyButtons();
    }


}
