package org.coursework.cassandraambulance;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.coursework.cassandraambulance.models.EmergencyCall;

public class GetCallTask extends Task<ObservableList<EmergencyCall>> {
    @Override
    protected ObservableList<EmergencyCall> call() throws Exception {


        final String getUnits = "SELECT * FROM call_by_date";
        ResultSet rs = DBConnector.getSession().execute(getUnits);

        // создаем список объектов
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


        return callObservableList;
    }
}
