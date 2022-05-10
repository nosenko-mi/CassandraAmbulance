package org.coursework.cassandraambulance.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.coursework.cassandraambulance.Alerts;
import org.coursework.cassandraambulance.StringResources;
import org.coursework.cassandraambulance.models.Person;
import org.coursework.cassandraambulance.tables.PersonTable;

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

        // пошук можливий лише у порядку type -> id, робиться наступна перевірка:
        // коли вказані type та id немає значення, чи вказано ім'я
        if (personType != null && personId != null){
            PersonTable.GetByTypeId(personTable, personType, personId);
        } else if(personType != null) {
            PersonTable.GetByTypeName(personTable, personType, personFn, personMn, personLn);
        } else {
            PersonTable.GetAll(personTable);
            Alerts.WrongSearchParameters("Available parameters type -> id or type -> name\nCurrently selected all rows");
        }

    }

    protected void GetSearchValues(){
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

    }

    public void InitMenuButtons() {

        // створення опцій для випадаючого меню
        MenuItem typeItemDoctor = new MenuItem(StringResources.DOCTOR_TYPE);
        MenuItem typeItemOrderly = new MenuItem(StringResources.ORDERLY_TYPE);
        MenuItem typeItemDriver = new MenuItem(StringResources.DRIVER_TYPE);
        MenuItem typeItemCaller = new MenuItem(StringResources.CALLER_TYPE);
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