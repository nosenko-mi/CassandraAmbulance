package org.coursework.cassandraambulance.controllers;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.coursework.cassandraambulance.DBConnector;
import org.coursework.cassandraambulance.TableUtils;
import org.coursework.cassandraambulance.ViewSwitcher;
import org.coursework.cassandraambulance.models.EmergencyCall;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class AddReportController {

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
    public TextField patientMnTextField;
    public TextField patientFnTextField;
    public TextField callIdTextField;
    public TextField patientLnTextField;
    public TableView dataTable;
    public TextField patientDobTextField;

    public DatePicker datePicker;
    public TextField timeTextField;
    public TextField thoroughfareTextField1;
    public TextField localityTextField1;

    private LocalDate dateToSearch = null;
    private LocalTime timeToSearch = null;
    private String localityToSearch = null, thoroughfareToSearch = null;


    public void AddReport(ActionEvent event) {
    }

    public void AddPatient(UUID patientUuid){
        LocalTime patientDob = LocalTime.now();

        try {
            patientDob = LocalTime.parse(patientDobTextField.getText());
        } catch (DateTimeParseException e) {
            System.out.println("[Error] " + e);
        }

        PreparedStatement addCallerStatement =
                DBConnector
                        .getSession()
                        .prepare(
                                "INSERT INTO ambulance_ver3.patients" +
                                        "(id, dob, first_name, middle_name, last_name)" +
                                        "VALUES(?, ?, ?, ?, ?);");
        BoundStatement boundStatement = addCallerStatement
                .bind(patientUuid, patientDob, patientFnTextField.getText(), patientMnTextField.getText(), patientLnTextField.getText());
        DBConnector.getSession().execute(boundStatement);
        System.out.println("[Patient added]");
    }

    public void SwitchToCallByDate(MouseEvent mouseEvent) {
        ViewSwitcher.Switch(mouseEvent, "call_by_date_view.fxml", "/style.css");
    }

    public void SwitchToCallByAddress(MouseEvent mouseEvent) {
        ViewSwitcher.SwitchToCallByAddress(mouseEvent);
    }

    public void SwitchToAddReport(MouseEvent mouseEvent) {
    }

    public void SwitchToAddCall(MouseEvent mouseEvent) {
        ViewSwitcher.SwitchToAddCall(mouseEvent);
    }

    public void GetUnits(ActionEvent event) {
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


        MenuItem appliedItem1 = new MenuItem("Не звертався");
        MenuItem appliedItem2 = new MenuItem("звертався на СШМД");
        MenuItem appliedItem3 = new MenuItem("Звертався в інший ЛПЗ");
        appliedMenuButton.getItems().addAll(appliedItem1, appliedItem2, appliedItem3);

        MenuItem onsetItem1 = new MenuItem("До 1 год.");
        MenuItem onsetItem2 = new MenuItem("Від 1 до 3 год.");
        MenuItem onsetItem3 = new MenuItem("Від 3 до 6 год");
        MenuItem onsetItem4 = new MenuItem("Від 6 до 12 год");
        MenuItem onsetItem5 = new MenuItem("Від 12 до 24 год");
        MenuItem onsetItem6 = new MenuItem("Понад 24 год.");
        onsetMenuButton.getItems().addAll(onsetItem1, onsetItem2, onsetItem3, onsetItem4, onsetItem5, onsetItem6);

        MenuItem fruitlessItem1 = new MenuItem("Не застали");
        MenuItem fruitlessItem2 = new MenuItem("Адреса не знайдена");
        MenuItem fruitlessItem3 = new MenuItem("Не доїхали");
        MenuItem fruitlessItem4 = new MenuItem("Не викликали");
        MenuItem fruitlessItem5 = new MenuItem("Обслужений до приїзду");
        MenuItem fruitlessItem6 = new MenuItem("Відмова від медичної допомоги");
        fruitlessMenuButton.getItems().addAll(fruitlessItem1, fruitlessItem2, fruitlessItem3, fruitlessItem4, fruitlessItem5, fruitlessItem6);



    }

    public void initialize(){
        InitMenuButtons();
    }


    public void GetSearchDate(ActionEvent event) {
    }

    @FXML
    protected void GetCalls(ActionEvent event) {
        TableColumn<EmergencyCall, UUID> idCol = new TableColumn<EmergencyCall, UUID>("id");
        TableColumn<EmergencyCall, UUID> unitIdCol = new TableColumn<EmergencyCall, UUID>("unitId");
        TableColumn<EmergencyCall, String> dateCol = new TableColumn<EmergencyCall, String>("date");
        TableColumn<EmergencyCall, String> timeCol = new TableColumn<EmergencyCall, String>("time");
        TableColumn<EmergencyCall, String> localityCol = new TableColumn<EmergencyCall, String>("locality");
        TableColumn<EmergencyCall, String> thoroughfareCol = new TableColumn<EmergencyCall, String>("thoroughfareCol");
        TableColumn<EmergencyCall, String> premiseCol = new TableColumn<EmergencyCall, String>("premiseCol");
        TableColumn<EmergencyCall, String> subPremiseCol = new TableColumn<EmergencyCall, String>("subPremiseCol");
        TableColumn<EmergencyCall, String> causeCol = new TableColumn<EmergencyCall, String>("causeCol");
        TableColumn<EmergencyCall, UUID> callerIdCol = new TableColumn<EmergencyCall, UUID>("callerId");

        GetSearchValues();


        ResultSet rs;
        if (dateToSearch == null && timeToSearch == null && localityToSearch == null && thoroughfareToSearch == null){
            final String getCalls = "SELECT * FROM ambulance_ver3.call_by_date LIMIT 100";
            rs = DBConnector.getSession().execute(getCalls);
        } else if (timeToSearch == null && localityToSearch == null && thoroughfareToSearch == null) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM ambulance_ver3.call_by_date WHERE date = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (localityToSearch == null && thoroughfareToSearch == null){
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM ambulance_ver3.call_by_date WHERE date = ? AND time = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch, timeToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (thoroughfareToSearch == null) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM ambulance_ver3.call_by_date WHERE date = ? AND time = ? AND a_locality = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch, timeToSearch, localityToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        }  else {
            final String getCalls = "SELECT * FROM ambulance_ver3.call_by_date LIMIT 100";
            rs = DBConnector.getSession().execute(getCalls);
        }

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

        idCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, UUID>("id"));
        unitIdCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, UUID>("unitId"));
        dateCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, String>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, String>("time"));
        localityCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, String>("locality"));
        thoroughfareCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, String>("thoroughfare"));
        premiseCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, String>("premise"));
        subPremiseCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, String>("subPremise"));
        causeCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, String>("cause"));
        callerIdCol.setCellValueFactory(new PropertyValueFactory<EmergencyCall, UUID>("callerId"));

        dataTable.setItems(callObservableList);
        dataTable.getColumns().addAll(idCol, unitIdCol, dateCol, timeCol, localityCol, thoroughfareCol, premiseCol, subPremiseCol, causeCol, callerIdCol);

        dataTable.getSelectionModel().setCellSelectionEnabled(true);
        dataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(dataTable);

    }
    protected void GetSearchValues(){
        try {
            dateToSearch = datePicker.getValue();
            timeToSearch = LocalTime.parse(timeTextField.getText());
            localityToSearch = localityTextField.getText();
            thoroughfareToSearch = thoroughfareTextField.getText();
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
}
