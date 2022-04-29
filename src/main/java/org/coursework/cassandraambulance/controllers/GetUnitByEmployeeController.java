package org.coursework.cassandraambulance.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.coursework.cassandraambulance.models.EmployeeUnit;
import org.coursework.cassandraambulance.models.Person;
import org.coursework.cassandraambulance.tables.EmployeeUnitTable;
import org.coursework.cassandraambulance.tables.PersonTable;

import java.util.UUID;

public class GetUnitByEmployeeController extends Controller  {

    public CheckBox doctorCheckBox;
    public CheckBox orderlyCheckBox;
    public CheckBox driverCheckBox;

    public TextField employeeIdTextField;

    private UUID employeeId;

    public TableView<EmployeeUnit> employeeUnitTable;

    public TableView<Person> employeeTable;

    public void GetEmployees(ActionEvent event) {
        PersonTable.GetByEmp(employeeTable, doctorCheckBox, orderlyCheckBox, driverCheckBox);
    }

    public void GetUnitsByEmployee(ActionEvent event) {
        try {
            employeeId = UUID.fromString(employeeIdTextField.getText());
        } catch (IllegalArgumentException e){
            employeeId = null;
            System.out.println(e);
        }
        EmployeeUnitTable.GetByEmp(employeeUnitTable, employeeId);

    }


    public void DoctorChecked(ActionEvent event) {
    }

    public void OrderlyChecked(ActionEvent event) {
    }

    public void DriverChecked(ActionEvent event) {
    }


}
