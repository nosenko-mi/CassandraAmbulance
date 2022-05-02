package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.coursework.cassandraambulance.DBConnector;
import org.coursework.cassandraambulance.StringResources;
import org.coursework.cassandraambulance.models.Person;
import org.coursework.cassandraambulance.tables.PersonTable;

import java.time.format.DateTimeParseException;
import java.util.UUID;

public class UpdatePersonController extends Controller{

    public TableView<Person> personTable;


    public TextField personLnTextField;
    public TextField personFnTextField;
    public TextField personMnTextField;
    public TextField personIdTextField;

    public TextField oldPersonIdTextField;

    public TextField newPersonMnTextField;
    public TextField newPersonFnTextField;
    public TextField newPersonLnTextField;

    public MenuButton oldTypeMenuButton;
    public MenuButton typeMenuButton;

    private UUID personId, oldPersonId;
    private String personFn, personMn, personLn, personType, newPersonFn, newPersonMn, newPersonLn, oldPersonType;


    public void UpdatePerson(ActionEvent event) {
        GetOldValues();
        GetNewValues();

        if (oldPersonId != null && oldPersonType != null){
            PreparedStatement updatePerson = DBConnector.getSession().prepare(
                    "UPDATE " + StringResources.PERSONS +
                            " SET  first_name = ?, middle_name = ?, last_name = ?" +
                            "WHERE type = ? AND id = ?"
            );
            BoundStatement boundStatement = updatePerson.bind(newPersonFn, newPersonMn, newPersonLn, oldPersonType, oldPersonId);
            DBConnector.getSession().execute(boundStatement);
            System.out.println("[Person updated]");
        } else {
            System.out.println("[Person is not updated]");
            AddAlert("Update error");
        }


    }

    public void RemovePerson(ActionEvent event) {
        GetOldValues();
        if (oldPersonId != null && oldPersonType != null){
            PreparedStatement deletePerson = DBConnector.getSession().prepare(
                    "DELETE FROM " + StringResources.PERSONS +
                            "WHERE type = ? AND id = ?"
            );
            BoundStatement boundStatement = deletePerson.bind(oldPersonType, oldPersonId);
            DBConnector.getSession().execute(boundStatement);
            System.out.println("[Person deleted]");
        } else {
            System.out.println("[Person is not updated]");
            AddAlert("Delete error");
        }
    }

    public void GetPerson(ActionEvent event) {
        GetSearchValues();

        // так як пошук можливий лише у порядку type -> id, робиться наступна перевірка:
        // коли вказани type та id немає значення, чи вказано ім'я
        if (personType != null && personId != null){
            PersonTable.GetByTypeId(personTable, personType, personId);
        } else if(personType != null) {
            PersonTable.GetByTypeName(personTable, personType, personFn, personMn, personLn);
        } else {
            PersonTable.GetAll(personTable);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Select information");
            alert.setHeaderText("Selection parameters is incorrect");
            alert.setContentText("Available parameters type -> id or type -> name\nCurrently selected all rows");
            alert.showAndWait();
        }

    }

    protected void GetSearchValues(){
        try {
            personFn = personFnTextField.getText();
            personMn = personMnTextField.getText();
            personLn = personLnTextField.getText();
            personType = typeMenuButton.getText();

            personId = UUID.fromString(personIdTextField.getText());
        } catch (DateTimeParseException e) {
            System.out.println("[Error] " + e);
        } catch (IllegalArgumentException e) {
            System.out.println("[Error] " + e);
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

    protected void GetNewValues(){
        newPersonFn = newPersonFnTextField.getText();
        newPersonMn = newPersonMnTextField.getText();
        newPersonLn = newPersonLnTextField.getText();
    }

    protected void GetOldValues(){
        try {
            if (!oldTypeMenuButton.getText().equals("Type")){
                oldPersonType = oldTypeMenuButton.getText();

            } else {
                oldPersonType = null;
            }
            oldPersonId = UUID.fromString(oldPersonIdTextField.getText());

        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            oldPersonId = null;
        }
    }

    private void AddAlert(String title){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Primary keys are missing");
        alert.setContentText("Add Type and Id in 'Old information' section");
        alert.showAndWait();
    }

    public void InitMenuButtons() {

        // кожне MenuButton повинно мати власні MenuItems

        MenuItem typeItemDoctor = new MenuItem("Лікар");
        MenuItem typeItemOrderly = new MenuItem("Санітар");
        MenuItem typeItemDriver = new MenuItem("Водій");
        MenuItem typeItemCaller = new MenuItem("Викликач");
        MenuItem typeItemNone = new MenuItem("-");

        typeMenuButton.getItems().addAll(typeItemDoctor, typeItemOrderly, typeItemDriver, typeItemCaller, typeItemNone);
        typeItemDoctor.setOnAction(event -> typeMenuButton.setText(typeItemDoctor.getText()));
        typeItemOrderly.setOnAction(event -> typeMenuButton.setText(typeItemOrderly.getText()));
        typeItemDriver.setOnAction(event -> typeMenuButton.setText(typeItemDriver.getText()));
        typeItemCaller.setOnAction(event -> typeMenuButton.setText(typeItemCaller.getText()));
        typeItemNone.setOnAction(event -> typeMenuButton.setText("Type"));


        MenuItem oldTypeItemDoctor = new MenuItem("Лікар");
        MenuItem oldTypeItemOrderly = new MenuItem("Санітар");
        MenuItem oldTypeItemDriver = new MenuItem("Водій");
        MenuItem oldTypeItemCaller = new MenuItem("Викликач");

        oldTypeMenuButton.getItems().addAll(oldTypeItemDoctor, oldTypeItemOrderly, oldTypeItemDriver, oldTypeItemCaller);
        oldTypeItemDoctor.setOnAction(event -> oldTypeMenuButton.setText(oldTypeItemDoctor.getText()));
        oldTypeItemOrderly.setOnAction(event -> oldTypeMenuButton.setText(oldTypeItemOrderly.getText()));
        oldTypeItemDriver.setOnAction(event -> oldTypeMenuButton.setText(oldTypeItemDriver.getText()));
        oldTypeItemCaller.setOnAction(event -> oldTypeMenuButton.setText(oldTypeItemCaller.getText()));


    }
    public void initialize(){
        InitMenuButtons();
    }
}
