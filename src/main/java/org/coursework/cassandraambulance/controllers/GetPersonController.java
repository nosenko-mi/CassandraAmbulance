package org.coursework.cassandraambulance.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.coursework.cassandraambulance.models.Person;
import org.coursework.cassandraambulance.tables.PersonTable;

import java.time.format.DateTimeParseException;
import java.util.UUID;

public class GetPersonController extends Controller {
    public TableView<Person> personTable;
    public TextField personMnTextField;
    public TextField personFnTextField;
    public TextField personLnTextField;
    public MenuButton typeMenuButton;
    public TextField personIdTextField;

    private UUID personId;
    private String personFn, personMn, personLn, personType;

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
            alert.setContentText("Available parameters type -> id or type -> name or name parameters\nCurrently selected all rows");
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

    public void InitMenuButtons() {

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


    }
    public void initialize(){
        InitMenuButtons();
    }
}