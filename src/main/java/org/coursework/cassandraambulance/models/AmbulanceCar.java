package org.coursework.cassandraambulance.models;

import java.util.UUID;

public class AmbulanceCar {

    private String serialNumber;
    private UUID id;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AmbulanceCar(String serialNumber, UUID id) {
        this.serialNumber = serialNumber;
        this.id = id;
    }
}
