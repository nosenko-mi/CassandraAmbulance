package org.coursework.cassandraambulance;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.coursework.cassandraambulance.models.Unit;

public class GetUnitService extends Service<ObservableList<Unit>> {
    @Override
    protected Task createTask() {
        return new GetUnitTask();
    }
}
