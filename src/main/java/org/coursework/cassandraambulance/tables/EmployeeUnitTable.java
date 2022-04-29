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
import org.coursework.cassandraambulance.Query;
import org.coursework.cassandraambulance.StringResources;
import org.coursework.cassandraambulance.TableUtils;
import org.coursework.cassandraambulance.models.EmployeeUnit;
import org.coursework.cassandraambulance.models.Person;
import org.coursework.cassandraambulance.models.Unit;

import java.util.UUID;

// клас, що представляє таблицю співпрацівник-бригада
// необхідний для створення столбців у існуючій таблиці, а також для отримання та внесення даних у таблицю
public class EmployeeUnitTable {
    public static final TableColumn<EmployeeUnit, UUID> empIdCol = new TableColumn<>("Employee id");
    public static final TableColumn<EmployeeUnit, UUID> unitIdCol = new TableColumn<>("Unit id");

    public static void SetColumns(TableView<EmployeeUnit> dataTable, ObservableList<EmployeeUnit> callObservableList ){

        empIdCol.setCellValueFactory(new PropertyValueFactory<EmployeeUnit, UUID>("empId"));
        unitIdCol.setCellValueFactory(new PropertyValueFactory<EmployeeUnit, UUID>("unitId"));

        dataTable.setItems(callObservableList);
        dataTable.getColumns().addAll(empIdCol, unitIdCol);
    }

    public static void GetByEmp(TableView<EmployeeUnit> employeeUnitTable, UUID empId){

        PreparedStatement getEmpUnit = DBConnector.getSession().prepare(
                "SELECT * FROM " + StringResources.UNIT_BY_EMP + " WHERE emp_id = ?;"
        );
        BoundStatement boundStatement = getEmpUnit.bind(empId);
        ResultSet rs = DBConnector.getSession().execute(boundStatement);
        ObservableList<EmployeeUnit> personObservableList = FXCollections.observableArrayList();

        for (Row row : rs){
            personObservableList
                    .add(new EmployeeUnit(
                            row.getUuid("emp_id"), row.getUuid("unit_id")
                    ));

        }

        employeeUnitTable.getColumns().clear();

        SetColumns(employeeUnitTable, personObservableList);

        employeeUnitTable.getSelectionModel().setCellSelectionEnabled(true);
        employeeUnitTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(employeeUnitTable);
    }
}
