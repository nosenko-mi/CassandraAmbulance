package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.coursework.cassandraambulance.Alerts;
import org.coursework.cassandraambulance.DBConnector;
import org.coursework.cassandraambulance.PreparedStatements;
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
    public TableView<EmergencyCall> dataTable;
    public TextField patientDobTextField;

    public DatePicker datePicker;
    public TextField timeTextField;
    public TextField thoroughfareToSearchTextField;
    public TextField localityToSearchTextField;

    private LocalDate dateToSearch = null;
    private LocalTime departureTime, arrivalTime, completionTime, returnTime;
    private String localityToSearch = null, thoroughfareToSearch = null;


    public void AddReport(ActionEvent event) {

        UUID patientUuid = UUID.randomUUID();

        if (GetReportData() && AddPatient(patientUuid)){
            // додати звіт
            BoundStatement boundStatement = PreparedStatements.addReportToReportByCall
                    .bind(
                            UUID.fromString(callIdTextField.getText()), UUID.randomUUID(), UUID.fromString(unitIdTextField.getText()), patientUuid,
                            localityTextField.getText(), thoroughfareTextField.getText(), premiseTextField.getText(), subPremiseTextField.getText(),
                            departureTime, arrivalTime, completionTime, returnTime,
                            hospitalizationMenuButton.getText(), preliminaryDiagnosisTextField.getText(), diagnosisCodeTextField.getText(),
                            resultMenuButton.getText() ,traumaMenuButton.getText(), appliedMenuButton.getText(), onsetMenuButton.getText(), fruitlessMenuButton.getText()
                    );
            DBConnector.getSession().execute(boundStatement);

            Alerts.SucceedOperation();
        }




    }

    private boolean GetReportData() {

//        departureTime = LocalTime.now();
//        arrivalTime = LocalTime.now();
//        completionTime = LocalTime.now();
//        returnTime = LocalTime.now();

        try {
            departureTime = LocalTime.parse(departureTimeTextField.getText());
            arrivalTime = LocalTime.parse(arrivalTimeTextField.getText());
            completionTime = LocalTime.parse(completionTimeTextField.getText());
            returnTime = LocalTime.parse(returnTimeTextField.getText());
            return true;
        } catch (DateTimeParseException e) {
            System.out.println("[Error] " + e);
            Alerts.ParseError("Time can't be parsed\nTime format: hh:MM:ss");
        }

        return false;
    }

    public boolean AddPatient(UUID patientUuid){
        LocalDate patientDob;

        try {
            patientDob = LocalDate.parse(patientDobTextField.getText());

            BoundStatement boundStatement = PreparedStatements.addPatientToPatients.bind(
                    patientUuid, patientDob,
                    patientFnTextField.getText(), patientMnTextField.getText(), patientLnTextField.getText());
            DBConnector.getSession().execute(boundStatement);
            System.out.println("[Patient added]");

            return true;
        } catch (DateTimeParseException e) {
            System.out.println("[Error] " + e);
            Alerts.ParseError("Date can't be parsed\nDate format: yyyy-mm-dd");

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return false;
    }


    public void GetSearchDate(ActionEvent event) {
    }

    @FXML
    protected void GetCallsByDate(ActionEvent event) {
        GetSearchValues();
        EmergencyCallTable.GetByDate(dataTable, dateToSearch, localityToSearch, thoroughfareToSearch);

    }

    protected void GetSearchValues(){

        dateToSearch = null;
        localityToSearch = null;
        thoroughfareToSearch = null;
        try {
            dateToSearch = datePicker.getValue();
            localityToSearch = localityTextField.getText();
            thoroughfareToSearch = thoroughfareTextField.getText();
        } catch (DateTimeParseException e) {
            System.out.println("[Error] " + e);
            Alerts.ParseError("Date can't be parsed\nDate format: yyyy-mm-dd");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void ResultItemsSetOnAction(MenuItem resultItem1, MenuItem resultItem2, MenuItem resultItem3, MenuItem resultItem4, MenuItem resultItem5) {
        resultItem1.setOnAction(event -> resultMenuButton.setText((resultItem1.getText())));
        resultItem2.setOnAction(event -> resultMenuButton.setText((resultItem2.getText())));
        resultItem3.setOnAction(event -> resultMenuButton.setText((resultItem3.getText())));
        resultItem4.setOnAction(event -> resultMenuButton.setText((resultItem4.getText())));
        resultItem5.setOnAction(event -> resultMenuButton.setText((resultItem5.getText())));
    }


    public void InitMenuButtons(){

        MenuItem hospItemY = new MenuItem("Госпіталізовано");
        MenuItem hospItemN = new MenuItem("Не госпіталізовано");
        hospitalizationMenuButton.getItems().addAll(hospItemY, hospItemN);
        hospItemN.setOnAction(event -> hospitalizationMenuButton.setText(hospItemN.getText()));
        hospItemY.setOnAction(event -> hospitalizationMenuButton.setText(hospItemY.getText()));


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
        traumaItem1.setOnAction(event -> traumaMenuButton.setText(traumaItem1.getText()));
        traumaItem2.setOnAction(event -> traumaMenuButton.setText(traumaItem2.getText()));
        traumaItem3.setOnAction(event -> traumaMenuButton.setText(traumaItem3.getText()));
        traumaItem4.setOnAction(event -> traumaMenuButton.setText(traumaItem4.getText()));
        traumaItem5.setOnAction(event -> traumaMenuButton.setText(traumaItem5.getText()));
        traumaItem6.setOnAction(event -> traumaMenuButton.setText(traumaItem6.getText()));
        traumaItem7.setOnAction(event -> traumaMenuButton.setText(traumaItem7.getText()));
        traumaItem8.setOnAction(event -> traumaMenuButton.setText(traumaItem8.getText()));
        traumaItem9.setOnAction(event -> traumaMenuButton.setText(traumaItem9.getText()));

        MenuItem appliedItem1 = new MenuItem("Не звертався");
        MenuItem appliedItem2 = new MenuItem("звертався на СШМД");
        MenuItem appliedItem3 = new MenuItem("Звертався в інший ЛПЗ");
        appliedMenuButton.getItems().addAll(appliedItem1, appliedItem2, appliedItem3);
        appliedItem1.setOnAction(event -> appliedMenuButton.setText(appliedItem1.getText()));
        appliedItem2.setOnAction(event -> appliedMenuButton.setText(appliedItem2.getText()));
        appliedItem3.setOnAction(event -> appliedMenuButton.setText(appliedItem3.getText()));

        MenuItem onsetItem1 = new MenuItem("До 1 год.");
        MenuItem onsetItem2 = new MenuItem("Від 1 до 3 год.");
        MenuItem onsetItem3 = new MenuItem("Від 3 до 6 год");
        MenuItem onsetItem4 = new MenuItem("Від 6 до 12 год");
        MenuItem onsetItem5 = new MenuItem("Від 12 до 24 год");
        MenuItem onsetItem6 = new MenuItem("Понад 24 год.");
        onsetMenuButton.getItems().addAll(onsetItem1, onsetItem2, onsetItem3, onsetItem4, onsetItem5, onsetItem6);
        onsetItem1.setOnAction(event -> onsetMenuButton.setText(onsetItem1.getText()));
        onsetItem2.setOnAction(event -> onsetMenuButton.setText(onsetItem2.getText()));
        onsetItem3.setOnAction(event -> onsetMenuButton.setText(onsetItem3.getText()));
        onsetItem4.setOnAction(event -> onsetMenuButton.setText(onsetItem4.getText()));
        onsetItem5.setOnAction(event -> onsetMenuButton.setText(onsetItem5.getText()));
        onsetItem6.setOnAction(event -> onsetMenuButton.setText(onsetItem6.getText()));


        MenuItem fruitlessItem1 = new MenuItem("Не застали");
        MenuItem fruitlessItem2 = new MenuItem("Адреса не знайдена");
        MenuItem fruitlessItem3 = new MenuItem("Не доїхали");
        MenuItem fruitlessItem4 = new MenuItem("Не викликали");
        MenuItem fruitlessItem5 = new MenuItem("Обслужений до приїзду");
        MenuItem fruitlessItem6 = new MenuItem("Відмова від медичної допомоги");
        MenuItem fruitlessItem7 = new MenuItem("Ні");
        fruitlessMenuButton.getItems().addAll(fruitlessItem1, fruitlessItem2, fruitlessItem3, fruitlessItem4, fruitlessItem5, fruitlessItem6, fruitlessItem7);
        fruitlessItem1.setOnAction(event -> fruitlessMenuButton.setText(fruitlessItem1.getText()));
        fruitlessItem1.setOnAction(event -> fruitlessMenuButton.setText(fruitlessItem1.getText()));
        fruitlessItem2.setOnAction(event -> fruitlessMenuButton.setText(fruitlessItem2.getText()));
        fruitlessItem3.setOnAction(event -> fruitlessMenuButton.setText(fruitlessItem3.getText()));
        fruitlessItem4.setOnAction(event -> fruitlessMenuButton.setText(fruitlessItem4.getText()));
        fruitlessItem5.setOnAction(event -> fruitlessMenuButton.setText(fruitlessItem5.getText()));
        fruitlessItem6.setOnAction(event -> fruitlessMenuButton.setText(fruitlessItem6.getText()));
        fruitlessItem7.setOnAction(event -> fruitlessMenuButton.setText(fruitlessItem7.getText()));


    }


    public void initialize(){
        InitMenuButtons();
    }

}
