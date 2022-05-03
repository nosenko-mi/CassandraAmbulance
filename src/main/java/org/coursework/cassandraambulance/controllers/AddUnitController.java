package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.coursework.cassandraambulance.*;
import org.coursework.cassandraambulance.models.AmbulanceCar;
import org.coursework.cassandraambulance.models.Person;
import org.coursework.cassandraambulance.tables.AmbulanceCarTable;
import org.coursework.cassandraambulance.tables.PersonTable;

import java.util.UUID;

public class AddUnitController extends  Controller{
    public TableView<Person> personTable;
    public TableView<AmbulanceCar> carTable;

    public TextField personMnTextField;
    public TextField personFnTextField;
    public TextField personLnTextField;
    public TextField personIdTextField;
    public TextField carSerialNumberTextField;

    public TextField orderlyIdTextField;
    public TextField doctorIdTextField;
    public TextField carIdTextField;
    public TextField driverIdTextField;

    public MenuButton typeMenuButton;

    private UUID personId, doctorId, orderlyId, driverId, carId;
    private String personFn, personMn, personLn, personType, carSerialNumber;


    public void GetEmployee(ActionEvent event) {
        GetEmployeeSearchValue();

        // так як пошук можливий лише у порядку type -> id, робиться наступна перевірка:
        // коли вказани type та id немає значення, чи вказано ім'я
        if (personType != null && personId != null){
            PersonTable.GetByTypeId(personTable, personType, personId);
        } else if(personType != null) {
            PersonTable.GetByTypeName(personTable, personType, personFn, personMn, personLn);
        } else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Select information");
            alert.setHeaderText("Selection parameters are incorrect");
            alert.setContentText("Available parameters type -> id or type -> name\nCurrently selected all rows");
            alert.showAndWait();
        }
    }

    public void GetCar(ActionEvent event) {
        GetCarSearchValue();

        if (carSerialNumber.isEmpty()){
            AmbulanceCarTable.GetAll(carTable);
        } else {
            AmbulanceCarTable.GetBySerialNumber(carTable, carSerialNumber);
        }

    }

    public void AddUnit(ActionEvent event) {
        GetNewValues();

        if (doctorId != null){
            Person doctor, orderly, driver;
            AmbulanceCar car;

            // для зручності користувача вводяться лише id членів бригади та автомобіля,
            // тому необхідно дістати потрібні значення з бази даних
            doctor = GetOnePerson(StringResources.DOCTOR_TYPE, doctorId);
            orderly = GetOnePerson(StringResources.ORDERLY_TYPE, orderlyId);
            driver = GetOnePerson(StringResources.DRIVER_TYPE, driverId);
            car = GetOneCar(carId);

            // додати запис у таблицю unit_by_id
            UUID unitId = UUID.randomUUID();
            BoundStatement boundStatement = PreparedStatements.addUnit.bind(
                    unitId,
                    doctor.getId(), doctor.getFirstName(), doctor.getMiddleName(), doctor.getLastName(),
                    orderly.getId(), orderly.getFirstName(), orderly.getMiddleName(), orderly.getLastName(),
                    driver.getId(), driver.getFirstName(), driver.getMiddleName(), driver.getLastName(),
                    car.getId(), car.getSerialNumber()
            );
            DBConnector.getSession().execute(boundStatement);

            // додати записи у таблицю unit_by_emp для кожного співпрацівника
            boundStatement = PreparedStatements.addToUnitByEmp.bind(
                    doctor.getId(), unitId
            );
            DBConnector.getSession().execute(boundStatement);

            boundStatement = PreparedStatements.addToUnitByEmp.bind(
                    orderly.getId(), unitId
            );
            DBConnector.getSession().execute(boundStatement);

            boundStatement = PreparedStatements.addToUnitByEmp.bind(
                    driver.getId(), unitId
            );
            DBConnector.getSession().execute(boundStatement);

            Alerts.SucceedOperation();

        } else {
            Alerts.MissingPrimaryKey("Primary keys are missing");
        }
    }

    private AmbulanceCar GetOneCar(UUID id){
        AmbulanceCar car = null;
        ResultSet rs = Query.GetOneCarById(id);
        if (rs != null){
            for(Row row: rs){
                car = new AmbulanceCar(row.getString("serial_number"), row.getUuid("id"));
            }
        }
        return car;
    }

    private Person GetOnePerson(String type, UUID id){
        Person person = null;
        ResultSet rs = Query.GetPersonByTypeId(type, id);
        if (rs != null){
            for(Row row: rs){
                person = new Person(row.getString("type"), row.getUuid("id"),
                        row.getString("first_name"), row.getString("middle_name"), row.getString("last_name"));
            }
        }
        return person;
    }

    private void GetEmployeeSearchValue(){
        try {
            personFn = personFnTextField.getText();
            personMn = personMnTextField.getText();
            personLn = personLnTextField.getText();
            personType = typeMenuButton.getText();

            personId = UUID.fromString(personIdTextField.getText());
        } catch (IllegalArgumentException e) {
            System.out.println("[Error] " + e);
            Alerts.ParseError("Person id can't be parsed");
            personId = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (personType.equals("Type")){
            personType = null;
            System.out.println("[Type is not chosen]");
        }

        System.out.println("[First name]" + personFn);
        System.out.println("[Middle name]" + personMn);
        System.out.println("[Last name]" + personLn);

    }

    private void GetCarSearchValue(){
        carSerialNumber = carSerialNumberTextField.getText();
    }

    private void GetNewValues(){
        try {
            doctorId = UUID.fromString(doctorIdTextField.getText());
            orderlyId = UUID.fromString(orderlyIdTextField.getText());
            driverId = UUID.fromString(driverIdTextField.getText());
            carId = UUID.fromString(carIdTextField.getText());
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            doctorId = null;
            orderlyId = null;
            driverId = null;
            carId = null;
        }
    }

    public void InitMenuButtons() {

        MenuItem typeItemDoctor = new MenuItem(StringResources.DOCTOR_TYPE);
        MenuItem typeItemOrderly = new MenuItem(StringResources.ORDERLY_TYPE);
        MenuItem typeItemDriver = new MenuItem(StringResources.DRIVER_TYPE);
        typeMenuButton.getItems().addAll(typeItemDoctor, typeItemOrderly, typeItemDriver);
        typeItemDoctor.setOnAction(event -> typeMenuButton.setText(typeItemDoctor.getText()));
        typeItemOrderly.setOnAction(event -> typeMenuButton.setText(typeItemOrderly.getText()));
        typeItemDriver.setOnAction(event -> typeMenuButton.setText(typeItemDriver.getText()));


    }
    public void initialize(){
        InitMenuButtons();
    }


}
