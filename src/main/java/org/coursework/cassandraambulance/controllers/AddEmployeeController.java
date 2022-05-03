package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import org.coursework.cassandraambulance.Alerts;
import org.coursework.cassandraambulance.DBConnector;
import org.coursework.cassandraambulance.PreparedStatements;
import org.coursework.cassandraambulance.StringResources;

import java.util.UUID;

public class AddEmployeeController extends Controller{

    public TextField employeeMnTextField;
    public TextField employeeFnTextField;
    public TextField employeeLnTextField;
    public MenuButton typeMenuButton;

    private String  employeeType, employeeFn, employeeMn, employeeLn;

    public void AddEmployee(ActionEvent event) {
        GetEmployeeData();

        UUID employeeId = UUID.randomUUID();
        if (employeeType != null){

            BoundStatement boundStatement = PreparedStatements.AddEmployeeToPersons.bind(
                    employeeType, employeeId, employeeFn, employeeMn, employeeLn
            );
            DBConnector.getSession().execute(boundStatement);

            Alerts.SucceedOperation();
        } else {
            Alerts.MissingPrimaryKey("Primary keys are missing");
        }
    }

    public void initialize(){
        InitMenuButton();
    }

    protected void InitMenuButton(){
        MenuItem typeItemDoctor = new MenuItem("Лікар");
        MenuItem typeItemOrderly = new MenuItem("Санітар");
        MenuItem typeItemDriver = new MenuItem("Водій");

        typeMenuButton.getItems().addAll(typeItemDoctor, typeItemOrderly, typeItemDriver);
        typeItemDoctor.setOnAction(event -> typeMenuButton.setText(typeItemDoctor.getText()));
        typeItemOrderly.setOnAction(event -> typeMenuButton.setText(typeItemOrderly.getText()));
        typeItemDriver.setOnAction(event -> typeMenuButton.setText(typeItemDriver.getText()));
    }

    protected void GetEmployeeData(){
        if (typeMenuButton.getText().equals("Type")){
            employeeType = null;
        } else {
            employeeType = typeMenuButton.getText();
        }
        employeeFn = employeeFnTextField.getText();
        employeeMn = employeeMnTextField.getText();
        employeeLn = employeeLnTextField.getText();
    }
}
