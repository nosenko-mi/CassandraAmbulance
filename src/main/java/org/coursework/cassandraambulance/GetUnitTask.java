package org.coursework.cassandraambulance;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.coursework.cassandraambulance.models.Unit;

public class GetUnitTask extends Task<ObservableList<Unit>> {
    @Override
    protected ObservableList<Unit> call() throws Exception {


        final String getUnits = "SELECT * FROM unit_by_id";
        ResultSet rs = DBConnector.getSession().execute(getUnits);

        // создаем список объектов
        ObservableList<Unit> unitObservableList = FXCollections.observableArrayList();

        for (Row row : rs){
            unitObservableList
                    .add(new Unit(
                            row.getUuid("id"), row.getUuid("doctor_id"), row.getUuid("orderly_id"), row.getUuid("driver_id"), row.getUuid("car_id"),
                            row.getString("doctor_first_name"), row.getString("doctor_middle_name"), row.getString("doctor_last_name"),
                            row.getString("orderly_first_name"), row.getString("orderly_middle_name"), row.getString("orderly_last_name"),
                            row.getString("driver_first_name"), row.getString("driver_middle_name"), row.getString("driver_last_name"),
                            row.getString("car_serial_number")
                    ));
        }


        return unitObservableList;
    }
}
