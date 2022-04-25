package org.coursework.cassandraambulance;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.coursework.cassandraambulance.models.EmergencyCall;

public class GetCallService extends Service<ObservableList<EmergencyCall>> {
    @Override
    protected Task createTask() {
        return new GetCallTask();
    }
}