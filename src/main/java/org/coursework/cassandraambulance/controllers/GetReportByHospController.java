package org.coursework.cassandraambulance.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import org.coursework.cassandraambulance.StringResources;
import org.coursework.cassandraambulance.models.Report;
import org.coursework.cassandraambulance.tables.ReportTable;

public class GetReportByHospController extends Controller{

    public MenuButton hospitalizationMenuButton;
    public TableView<Report> reportTable;

    private String hospitalizationStatus;

    public void GetReportByHosp(ActionEvent event) {
        GetSearchData();

        ReportTable.GetByHosp(reportTable, hospitalizationStatus);

    }

    private void GetSearchData(){

        hospitalizationStatus = hospitalizationMenuButton.getText();

    }

    private void InitMenuButtons(){
        MenuItem hospItemY = new MenuItem(StringResources.HOSP_TRUE);
        MenuItem hospItemN = new MenuItem(StringResources.HOSP_FALSE);
        hospitalizationMenuButton.getItems().addAll(hospItemY, hospItemN);
        hospItemN.setOnAction(event -> hospitalizationMenuButton.setText(hospItemN.getText()));
        hospItemY.setOnAction(event -> hospitalizationMenuButton.setText(hospItemY.getText()));
    }

    public void initialize(){
        InitMenuButtons();
    }


}
