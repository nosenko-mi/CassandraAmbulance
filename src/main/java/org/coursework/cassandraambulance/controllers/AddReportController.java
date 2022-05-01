package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.coursework.cassandraambulance.*;
import org.coursework.cassandraambulance.models.EmergencyCall;
import org.coursework.cassandraambulance.tables.EmergencyCallTable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class AddReportController extends Controller{

    public MenuButton resultMenuButton;
    public MenuButton hospitalizationMenuButton;
    public MenuButton traumaMenuButton;
    public MenuButton onsetMenuButton;
    public MenuButton fruitlessMenuButton;
    public MenuButton appliedMenuButton;

    public TextField completionTimeTextField;
    public TextField arrivalTimeTextField;
    public TextField returnTimeTextField;
    public TextField departureTimeTextField;

    public TextField preliminaryDiagnosisTextField;
    public TextField diagnosisCodeTextField;

    public TextField thoroughfareTextField;
    public TextField localityTextField;
    public TextField premiseTextField;
    public TextField subPremiseTextField;

    public TextField unitIdTextField;
    public TextField callIdTextField;

    public TextField patientMnTextField;
    public TextField patientFnTextField;
    public TextField patientLnTextField;
    public TableView dataTable;
    public TextField patientDobTextField;

    public DatePicker datePicker;
    public TextField timeTextField;
    public TextField thoroughfareToSearchTextField;
    public TextField localityToSearchTextField;

    private LocalDate dateToSearch = null;
    private LocalTime timeToSearch = null, departureTime, arrivalTime, completionTime, returnTime;
    private String localityToSearch = null, thoroughfareToSearch = null;


    public void AddReport(ActionEvent event) {

        // додати пацієнта
        UUID patientUuid = UUID.randomUUID();
        AddPatient(patientUuid);

        // зібрати введені дані
        GetReportData();

        // додати звіт
        PreparedStatement addReportStatement =
                DBConnector
                        .getSession()
                        .prepare(
                                "INSERT INTO " + StringResources.REPORT_BY_CALL +
                                        " (call_id, id, unit_id, patient_id," +
                                        " a_locality, a_thoroughfare, a_premise, a_sub_premise," +
                                        " departure_time, arrival_time, completion_time, return_time," +
                                        " hospitalization_status, preliminary_diagnosis, diagnosis_code," +
                                        " result, trauma, applied_before, onset, fruitless)" +
                                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

        BoundStatement boundStatement = addReportStatement
                .bind(
                        UUID.fromString(callIdTextField.getText()), UUID.randomUUID(), UUID.fromString(unitIdTextField.getText()), patientUuid,
                        localityTextField.getText(), thoroughfareTextField.getText(), premiseTextField.getText(), subPremiseTextField.getText(),
                        departureTime, arrivalTime, completionTime, returnTime,
                        hospitalizationMenuButton.getText(), preliminaryDiagnosisTextField.getText(), diagnosisCodeTextField.getText(),
                        resultMenuButton.getText() ,traumaMenuButton.getText(), appliedMenuButton.getText(), onsetMenuButton.getText(), fruitlessMenuButton.getText()
                );
        DBConnector.getSession().execute(boundStatement);
        System.out.println("[Report added]");

    }

    private void GetReportData() {

        departureTime = LocalTime.now();
        arrivalTime = LocalTime.now();
        completionTime = LocalTime.now();
        returnTime = LocalTime.now();

        try {
            departureTime = LocalTime.parse(departureTimeTextField.getText());
            arrivalTime = LocalTime.parse(arrivalTimeTextField.getText());
            completionTime = LocalTime.parse(completionTimeTextField.getText());
            returnTime = LocalTime.parse(returnTimeTextField.getText());
        } catch (DateTimeParseException e) {
            System.out.println("[Error] " + e);
        }
    }

    public void AddPatient(UUID patientUuid){
        LocalDate patientDob;

        try {
            patientDob = LocalDate.parse(patientDobTextField.getText());
        } catch (DateTimeParseException e) {
            patientDob = LocalDate.MIN;
            System.out.println("[Error] " + e);
        }

        PreparedStatement addPatientStatement =
                DBConnector
                        .getSession()
                        .prepare(
                                "INSERT INTO ambulance_ver3.patients" +
                                        "(id, dob, first_name, middle_name, last_name)" +
                                        "VALUES(?, ?, ?, ?, ?);");
        BoundStatement boundStatement = addPatientStatement
                .bind(patientUuid, patientDob, patientFnTextField.getText(), patientMnTextField.getText(), patientLnTextField.getText());
        DBConnector.getSession().execute(boundStatement);
        System.out.println("[Patient added]");


    }


    public void GetSearchDate(ActionEvent event) {
    }

    @FXML
    protected void GetCallsByDate(ActionEvent event) {
        GetSearchValues();

        ResultSet rs = Query.GetCallsByDateQuery(dateToSearch, localityToSearch, thoroughfareToSearch);
        ObservableList<EmergencyCall> callObservableList = FXCollections.observableArrayList();

        for (Row row : rs){
            callObservableList
                    .add(new EmergencyCall(
                            row.getString("a_locality"), row.getString("a_thoroughfare"), row.getString("a_premise"),
                            row.getString("a_sub_premise"), row.getString("cause"), row.getLocalDate("date"),
                            row.getLocalTime("time"),
                            row.getUuid("caller_id"), row.getUuid("id"), row.getUuid("unit_id")
                    ));

        }
        dataTable.getColumns().clear();


        EmergencyCallTable.SetColumns(dataTable, callObservableList);

        dataTable.getSelectionModel().setCellSelectionEnabled(true);
        dataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(dataTable);

    }

    protected void GetSearchValues(){

        dateToSearch = null;
        timeToSearch = null;
        localityToSearch = null;
        thoroughfareToSearch = null;
        try {
            dateToSearch = datePicker.getValue();
            localityToSearch = localityTextField.getText();
            thoroughfareToSearch = thoroughfareTextField.getText();
            timeToSearch = LocalTime.parse(timeTextField.getText());
        } catch (DateTimeParseException e) {
            System.out.println("[Error] " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Date: " + dateToSearch);
        System.out.println("Time: " + timeToSearch);
        System.out.println("Locality: " + localityToSearch);
        System.out.println("Thoroughfare: " + thoroughfareToSearch);

    }

    private void ResultItemsSetOnAction(MenuItem resultItem1, MenuItem resultItem2, MenuItem resultItem3, MenuItem resultItem4, MenuItem resultItem5) {
        resultItem1.setOnAction(event -> {
            resultMenuButton.setText((resultItem1.getText()));
        });
        resultItem2.setOnAction(event -> {
            resultMenuButton.setText((resultItem2.getText()));
        });
        resultItem3.setOnAction(event -> {
            resultMenuButton.setText((resultItem3.getText()));
        });
        resultItem4.setOnAction(event -> {
            resultMenuButton.setText((resultItem4.getText()));
        });
        resultItem5.setOnAction(event -> {
            resultMenuButton.setText((resultItem5.getText()));
        });
    }


    public void InitMenuButtons(){

        MenuItem hospItemY = new MenuItem("Hospitalized");
        MenuItem hospItemN = new MenuItem("Not hospitalized");
        hospitalizationMenuButton.getItems().addAll(hospItemY, hospItemN);
        hospItemN.setOnAction(event -> {
            hospitalizationMenuButton.setText(hospItemN.getText());
        });
        hospItemY.setOnAction(event -> {
            hospitalizationMenuButton.setText(hospItemY.getText());
        });


        MenuItem resultItem1 = new MenuItem("Покращення");
        MenuItem resultItem2 = new MenuItem("Без ефекту");
        MenuItem resultItem3 = new MenuItem("Погіршення");
        MenuItem resultItem4 = new MenuItem("Смерть в присотності");
        MenuItem resultItem5 = new MenuItem("Смерть до приїзду");
        MenuItem resultItem6 = new MenuItem("Здоровий");
        MenuItem resultItem7 = new MenuItem("Чергування");
        MenuItem resultItem8 = new MenuItem("Інші перевезення");
        MenuItem resultItem9 = new MenuItem("Заправка Автомобіля");
        MenuItem resultItem10 = new MenuItem("Інше");
        resultMenuButton.getItems().addAll(resultItem1, resultItem2, resultItem3, resultItem4, resultItem5, resultItem6, resultItem7, resultItem8, resultItem9, resultItem10);
        ResultItemsSetOnAction(resultItem1, resultItem2, resultItem3, resultItem4, resultItem5);
        ResultItemsSetOnAction(resultItem6, resultItem7, resultItem8, resultItem9, resultItem10);


        MenuItem traumaItem1 = new MenuItem("Немає");
        MenuItem traumaItem2 = new MenuItem("Побутова");
        MenuItem traumaItem3 = new MenuItem("Вулична");
        MenuItem traumaItem4 = new MenuItem("Кримінальна");
        MenuItem traumaItem5 = new MenuItem("ДТП");
        MenuItem traumaItem6 = new MenuItem("Виробнича");
        MenuItem traumaItem7 = new MenuItem("Спортивна");
        MenuItem traumaItem8 = new MenuItem("Самогубство");
        MenuItem traumaItem9 = new MenuItem("Вулична, внаслідок ожеледиці");
        traumaMenuButton.getItems().addAll(traumaItem1, traumaItem2, traumaItem3, traumaItem4, traumaItem5, traumaItem6, traumaItem7, traumaItem8, traumaItem9);
        traumaItem1.setOnAction(event -> {
            traumaMenuButton.setText(traumaItem1.getText());
        });
        traumaItem2.setOnAction(event -> {
            traumaMenuButton.setText(traumaItem2.getText());
        });
        traumaItem3.setOnAction(event -> {
            traumaMenuButton.setText(traumaItem3.getText());
        });
        traumaItem4.setOnAction(event -> {
            traumaMenuButton.setText(traumaItem4.getText());
        });
        traumaItem5.setOnAction(event -> {
            traumaMenuButton.setText(traumaItem5.getText());
        });
        traumaItem6.setOnAction(event -> {
            traumaMenuButton.setText(traumaItem6.getText());
        });
        traumaItem7.setOnAction(event -> {
            traumaMenuButton.setText(traumaItem7.getText());
        });
        traumaItem8.setOnAction(event -> {
            traumaMenuButton.setText(traumaItem8.getText());
        });
        traumaItem9.setOnAction(event -> {
            traumaMenuButton.setText(traumaItem9.getText());
        });

        MenuItem appliedItem1 = new MenuItem("Не звертався");
        MenuItem appliedItem2 = new MenuItem("звертався на СШМД");
        MenuItem appliedItem3 = new MenuItem("Звертався в інший ЛПЗ");
        appliedMenuButton.getItems().addAll(appliedItem1, appliedItem2, appliedItem3);
        appliedItem1.setOnAction(event -> {
            appliedMenuButton.setText(appliedItem1.getText());
        });
        appliedItem2.setOnAction(event -> {
            appliedMenuButton.setText(appliedItem2.getText());
        });
        appliedItem3.setOnAction(event -> {
            appliedMenuButton.setText(appliedItem3.getText());
        });

        MenuItem onsetItem1 = new MenuItem("До 1 год.");
        MenuItem onsetItem2 = new MenuItem("Від 1 до 3 год.");
        MenuItem onsetItem3 = new MenuItem("Від 3 до 6 год");
        MenuItem onsetItem4 = new MenuItem("Від 6 до 12 год");
        MenuItem onsetItem5 = new MenuItem("Від 12 до 24 год");
        MenuItem onsetItem6 = new MenuItem("Понад 24 год.");
        onsetMenuButton.getItems().addAll(onsetItem1, onsetItem2, onsetItem3, onsetItem4, onsetItem5, onsetItem6);
        onsetItem1.setOnAction(event -> {
            onsetMenuButton.setText(onsetItem1.getText());
        });
        onsetItem2.setOnAction(event -> {
            onsetMenuButton.setText(onsetItem2.getText());
        });
        onsetItem3.setOnAction(event -> {
            onsetMenuButton.setText(onsetItem3.getText());
        });
        onsetItem4.setOnAction(event -> {
            onsetMenuButton.setText(onsetItem4.getText());
        });
        onsetItem5.setOnAction(event -> {
            onsetMenuButton.setText(onsetItem5.getText());
        });
        onsetItem6.setOnAction(event -> {
            onsetMenuButton.setText(onsetItem6.getText());
        });


        MenuItem fruitlessItem1 = new MenuItem("Не застали");
        MenuItem fruitlessItem2 = new MenuItem("Адреса не знайдена");
        MenuItem fruitlessItem3 = new MenuItem("Не доїхали");
        MenuItem fruitlessItem4 = new MenuItem("Не викликали");
        MenuItem fruitlessItem5 = new MenuItem("Обслужений до приїзду");
        MenuItem fruitlessItem6 = new MenuItem("Відмова від медичної допомоги");
        fruitlessMenuButton.getItems().addAll(fruitlessItem1, fruitlessItem2, fruitlessItem3, fruitlessItem4, fruitlessItem5, fruitlessItem6);
        fruitlessItem1.setOnAction(event -> {
            fruitlessMenuButton.setText(fruitlessItem1.getText());
        });
        fruitlessItem1.setOnAction(event -> {
            fruitlessMenuButton.setText(fruitlessItem1.getText());
        });
        fruitlessItem2.setOnAction(event -> {
            fruitlessMenuButton.setText(fruitlessItem2.getText());
        });
        fruitlessItem3.setOnAction(event -> {
            fruitlessMenuButton.setText(fruitlessItem3.getText());
        });
        fruitlessItem4.setOnAction(event -> {
            fruitlessMenuButton.setText(fruitlessItem4.getText());
        });
        fruitlessItem5.setOnAction(event -> {
            fruitlessMenuButton.setText(fruitlessItem5.getText());
        });
        fruitlessItem6.setOnAction(event -> {
            fruitlessMenuButton.setText(fruitlessItem6.getText());
        });


    }


    public void initialize(){
        InitMenuButtons();
    }

}
