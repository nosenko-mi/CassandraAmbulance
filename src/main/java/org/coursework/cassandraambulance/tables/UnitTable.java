package org.coursework.cassandraambulance.tables;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
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
import org.coursework.cassandraambulance.StringResources;
import org.coursework.cassandraambulance.TableUtils;
import org.coursework.cassandraambulance.models.Unit;

import java.util.UUID;

// клас, що представляє таблицю бригад
// необхідний для створення столбців у існуючій таблиці, а також для отримання та внесення даних у таблицю
public class UnitTable {

    public static final TableColumn<Unit, UUID> idCol = new TableColumn<>("Id");
    public static final TableColumn<Unit, UUID> doctorIdCol = new TableColumn<>("Doctor id");
    public static final TableColumn<Unit, UUID> orderlyIdCol = new TableColumn<>("Orderly id");
    public static final TableColumn<Unit, UUID> driverIdCol = new TableColumn<>("Driver id");
    public static final TableColumn<Unit, UUID> carIdCol = new TableColumn<>("Car id");

    public static final TableColumn<Unit, String> doctorFnCol = new TableColumn<>("Doctor's first name");
    public static final TableColumn<Unit, String> doctorMnCol = new TableColumn<>("Doctor's middle name");
    public static final TableColumn<Unit, String> doctorLnCol = new TableColumn<>("Doctor's last name");

    public static final TableColumn<Unit, String> orderlyFnCol = new TableColumn<>("Orderly's first name");
    public static final TableColumn<Unit, String> orderlyMnCol = new TableColumn<>("Orderly's middle name");
    public static final TableColumn<Unit, String> orderlyLnCol = new TableColumn<>("Orderly's last name");

    public static final TableColumn<Unit, String> driverFnCol = new TableColumn<>("Driver's first name");
    public static final TableColumn<Unit, String> driverMnCol = new TableColumn<>("Driver's middle name");
    public static final TableColumn<Unit, String> driverLnCol = new TableColumn<>("Driver's last name");

    public static final TableColumn<Unit, String> carSerialNumberCol = new TableColumn<>("Car serial number");

    private static final ObservableList<Unit> personObservableList = FXCollections.observableArrayList();


    public static void SetColumns(TableView<Unit> dataTable, ObservableList<Unit> callObservableList ){

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        doctorIdCol.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        orderlyIdCol.setCellValueFactory(new PropertyValueFactory<>("orderlyId"));
        driverIdCol.setCellValueFactory(new PropertyValueFactory<>("driverId"));
        carIdCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
        doctorFnCol.setCellValueFactory(new PropertyValueFactory<>("doctorFn"));
        doctorMnCol.setCellValueFactory(new PropertyValueFactory<>("doctorMn"));
        doctorLnCol.setCellValueFactory(new PropertyValueFactory<>("doctorLn"));
        orderlyFnCol.setCellValueFactory(new PropertyValueFactory<>("orderlyFn"));
        orderlyMnCol.setCellValueFactory(new PropertyValueFactory<>("orderlyMn"));
        orderlyLnCol.setCellValueFactory(new PropertyValueFactory<>("orderlyLn"));
        driverFnCol.setCellValueFactory(new PropertyValueFactory<>("driverFn"));
        driverMnCol.setCellValueFactory(new PropertyValueFactory<>("driverMn"));
        driverLnCol.setCellValueFactory(new PropertyValueFactory<>("driverLn"));
        carSerialNumberCol.setCellValueFactory(new PropertyValueFactory<>("carSerialNumber"));



        dataTable.setItems(callObservableList);
        dataTable.getColumns().addAll(
                idCol,
                doctorIdCol, doctorFnCol, doctorMnCol, doctorLnCol,
                orderlyIdCol, orderlyFnCol, orderlyMnCol, orderlyLnCol,
                driverIdCol, driverFnCol, driverMnCol, driverLnCol,
                carIdCol, carSerialNumberCol);


    }

    public static void GetById(TableView<Unit> unitTable, UUID id){

        ResultSet rs;
        // обрати записи з таблиці unit_by_emp
        if (id != null){
            PreparedStatement getUnitByEmp = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.UNIT_BY_ID + " WHERE id = ?;"
            );
            BoundStatement boundStatement = getUnitByEmp.bind(id);
            rs = DBConnector.getSession().execute(boundStatement);
        } else {
            String getAllUnits = "SELECT * FROM " + StringResources.UNIT_BY_ID + ";";
            rs = DBConnector.getSession().execute(getAllUnits);
        }

        personObservableList.clear();

        HandleRows(rs);

        unitTable.getColumns().clear();

        SetColumns(unitTable, personObservableList);

        unitTable.getSelectionModel().setCellSelectionEnabled(true);
        unitTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(unitTable);
    }



    private static void HandleRows(ResultSet rs){
        for (Row row : rs){
            personObservableList
                    .add(new Unit(
                            row.getUuid("id"), row.getUuid("doctor_id"), row.getUuid("orderly_id"), row.getUuid("driver_id"), row.getUuid("car_id"),
                            row.getString("doctor_first_name"), row.getString("doctor_middle_name"), row.getString("doctor_last_name"),
                            row.getString("orderly_first_name"), row.getString("orderly_middle_name"), row.getString("orderly_last_name"),
                            row.getString("driver_first_name"), row.getString("driver_middle_name"), row.getString("driver_last_name"),
                            row.getString("car_serial_number")
                    ));

        }
    }

}
