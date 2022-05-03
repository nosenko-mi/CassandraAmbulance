package org.coursework.cassandraambulance.tables;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.coursework.cassandraambulance.Query;
import org.coursework.cassandraambulance.TableUtils;
import org.coursework.cassandraambulance.models.AmbulanceCar;

import java.util.UUID;

public class AmbulanceCarTable {
    public static final TableColumn<AmbulanceCar, UUID> idCol = new TableColumn<>("Id");
    public static final TableColumn<AmbulanceCar, String> serialNumberCol = new TableColumn<>("Serial number");
    private static final ObservableList<AmbulanceCar> carObservableList = FXCollections.observableArrayList();


    public static void SetColumns(TableView<AmbulanceCar> dataTable, ObservableList<AmbulanceCar> callObservableList ){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        serialNumberCol.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));

        dataTable.setItems(callObservableList);
        dataTable.getColumns().addAll(idCol, serialNumberCol);
    }

    public static void GetBySerialNumber(TableView<AmbulanceCar> dataTable, String serialNumber){
        ResultSet rs = Query.GetOneCarBySerialNumber(serialNumber);
        carObservableList.clear();


        HandleRows(rs);

        dataTable.getColumns().clear();

        SetColumns(dataTable, carObservableList);

        dataTable.getSelectionModel().setCellSelectionEnabled(true);
        dataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(dataTable);
    }

    public static void GetAll(TableView<AmbulanceCar> dataTable){
        ResultSet rs = Query.GetAllCars();
        carObservableList.clear();


        HandleRows(rs);

        dataTable.getColumns().clear();

        SetColumns(dataTable, carObservableList);

        dataTable.getSelectionModel().setCellSelectionEnabled(true);
        dataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableUtils.installCopyPasteHandler(dataTable);
    }

    private static void HandleRows(ResultSet rs){
        for (Row row : rs){
            carObservableList
                    .add(new AmbulanceCar(
                            row.getString("serial_number"), row.getUuid("id"))
                    );

        }
    }
}
