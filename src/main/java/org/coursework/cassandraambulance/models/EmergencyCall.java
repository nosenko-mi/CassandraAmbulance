package org.coursework.cassandraambulance.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

// клас, що представляє виклик
public class EmergencyCall {

    @Override
    public String toString() {
        return "EmergencyCall{" +
                "locality='" + locality + '\'' +
                ", thoroughfare='" + thoroughfare + '\'' +
                ", premise='" + premise + '\'' +
                ", subPremise='" + subPremise + '\'' +
                ", cause='" + cause + '\'' +
                ", id=" + id +
                ", unitId=" + unitId +
                ", callerId=" + callerId +
                ", time=" + time +
                ", date=" + date +
                '}';
    }

    private String locality, thoroughfare, premise, subPremise, cause;
    private UUID id, unitId, callerId;
    private LocalTime time;
    private LocalDate date;

    public EmergencyCall(String locality, String thoroughfare, String premise, String subPremise, String cause, LocalDate date, LocalTime time, UUID id, UUID unitId, UUID callerId) {
        this.locality = locality;
        this.thoroughfare = thoroughfare;
        this.premise = premise;
        this.subPremise = subPremise;
        this.cause = cause;
        this.date = date;
        this.time = time;
        this.id = id;
        this.unitId = unitId;
        this.callerId = callerId;
    }

    public EmergencyCall(String locality, String thoroughfare, String premise, String subPremise, String cause, LocalDate date, LocalTime time, UUID id, UUID unitId) {
        this.locality = locality;
        this.thoroughfare = thoroughfare;
        this.premise = premise;
        this.subPremise = subPremise;
        this.cause = cause;
        this.date = date;
        this.time = time;
        this.id = id;
        this.unitId = unitId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }



    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getThoroughfare() {
        return thoroughfare;
    }

    public void setThoroughfare(String thoroughfare) {
        this.thoroughfare = thoroughfare;
    }

    public String getPremise() {
        return premise;
    }

    public void setPremise(String premise) {
        this.premise = premise;
    }

    public String getSubPremise() {
        return subPremise;
    }

    public void setSubPremise(String subPremise) {
        this.subPremise = subPremise;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public void setUnitId(UUID unitId) {
        this.unitId = unitId;
    }

    public UUID getCallerId() {
        return callerId;
    }

    public void setCallerId(UUID callerId) {
        this.callerId = callerId;
    }

}
