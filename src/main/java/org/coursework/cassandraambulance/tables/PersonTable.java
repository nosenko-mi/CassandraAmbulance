package org.coursework.cassandraambulance.tables;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.coursework.cassandraambulance.DBConnector;
import org.coursework.cassandraambulance.Query;
import org.coursework.cassandraambulance.StringResources;
import org.coursework.cassandraambulance.TableUtils;
import org.coursework.cassandraambulance.models.Person;

import java.util.UUID;

// клас, що представляє таблицю осіб
// необхідний для створення столбців у існуючій таблиці, а також для отримання та внесення даних у таблицю
public class PersonTable {
    public static final TableColumn<Person, UUID> idCol = new TableColumn<>("Id");
    public static final TableColumn<Person, String> typeCol = new TableColumn<>("Type");
    public static final TableColumn<Person, String> fnCol = new TableColumn<>("First name");
    public static final TableColumn<Person, String> mnCol = new TableColumn<>("Middle name");
    public static final TableColumn<Person, String> lnCol = new TableColumn<>("Last name");
    private static final ObservableList<Person> personObservableList = FXCollections.observableArrayList();

    public static void SetColumns(TableView<Person> dataTable, ObservableList<Person> callObservableList ){

        idCol.setCellValueFactory(new PropertyValueFactory<Person, UUID>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<Person, String>("type"));
        fnCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        mnCol.setCellValueFactory(new PropertyValueFactory<Person, String>("middleName"));
        lnCol.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));

        dataTable.setItems(callObservableList);
        dataTable.getColumns().addAll(idCol, typeCol, fnCol, mnCol, lnCol);


    }

    public static void GetByEmp(TableView<Person> personTable, CheckBox doctorCheckBox, CheckBox orderlyCheckBox, CheckBox driverCheckBox){
        ResultSet rs = Query.GetEmployees(doctorCheckBox, orderlyCheckBox, driverCheckBox);
        personObservableList.clear();
        for (Row row : rs){
            personObservableList
                    .add(new Person(
                            row.getString("type"), row.getUuid("id"),
                            row.getString("first_name"), row.getString("middle_name"), row.getString("last_name")
                    ));

        }

        personTable.getColumns().clear();

        SetColumns(personTable, personObservableList);

        personTable.getSelectionModel().setCellSelectionEnabled(true);
        personTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(personTable);
    }

    public  static void GetByTypeId(TableView<Person> personTable, String type, UUID id){

        ResultSet rs = Query.GetPersonByTypeId(type, id);
        personObservableList.clear();


        HandleRows(rs);

        personTable.getColumns().clear();

        SetColumns(personTable, personObservableList);

        personTable.getSelectionModel().setCellSelectionEnabled(true);
        personTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(personTable);

    }

    public  static void GetByTypeName(TableView<Person> personTable, String type, String fn, String mn, String ln){

        ResultSet rs = Query.GetPersonByTypeName(type, fn, mn, ln);
        personObservableList.clear();


        HandleRows(rs);

        personTable.getColumns().clear();

        SetColumns(personTable, personObservableList);

        personTable.getSelectionModel().setCellSelectionEnabled(true);
        personTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(personTable);

    }

    public  static void GetAll(TableView<Person> personTable){

        String getAllPersons = "SELECT * FROM " + StringResources.PERSONS + " LIMIT 100;";

        ResultSet rs = DBConnector.getSession().execute(getAllPersons);
        personObservableList.clear();


        HandleRows(rs);

        personTable.getColumns().clear();

        SetColumns(personTable, personObservableList);

        personTable.getSelectionModel().setCellSelectionEnabled(true);
        personTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(personTable);
    }



    private static void HandleRows(ResultSet rs){
        for (Row row : rs){
            personObservableList
                    .add(new Person(
                            row.getString("type"), row.getUuid("id"),
                            row.getString("first_name"), row.getString("middle_name"), row.getString("last_name")
                    ));

        }
    }
}
