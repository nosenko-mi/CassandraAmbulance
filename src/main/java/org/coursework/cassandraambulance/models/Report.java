package org.coursework.cassandraambulance.models;

import java.time.LocalTime;
import java.util.UUID;

public class Report {
    private UUID id, callId, patientId, unitId;
    private String preliminary_diagnosis, diagnosis_code, result, hospStatus, traumaStatus, onsetStatus, appliedBeforeStatus, fruitlessStatus, locality, thoroughfare, premise, subPremise;
    private LocalTime departureTime, arrivalTime, completionTime, returnTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCallId() {
        return callId;
    }

    public void setCallId(UUID callId) {
        this.callId = callId;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public void setUnitId(UUID unitId) {
        this.unitId = unitId;
    }

    public String getPreliminary_diagnosis() {
        return preliminary_diagnosis;
    }

    public void setPreliminary_diagnosis(String preliminary_diagnosis) {
        this.preliminary_diagnosis = preliminary_diagnosis;
    }

    public String getDiagnosis_code() {
        return diagnosis_code;
    }

    public void setDiagnosis_code(String diagnosis_code) {
        this.diagnosis_code = diagnosis_code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getHospStatus() {
        return hospStatus;
    }

    public void setHospStatus(String hospStatus) {
        this.hospStatus = hospStatus;
    }

    public String getTraumaStatus() {
        return traumaStatus;
    }

    public void setTraumaStatus(String traumaStatus) {
        this.traumaStatus = traumaStatus;
    }

    public String getOnsetStatus() {
        return onsetStatus;
    }

    public void setOnsetStatus(String onsetStatus) {
        this.onsetStatus = onsetStatus;
    }

    public String getAppliedBeforeStatus() {
        return appliedBeforeStatus;
    }

    public void setAppliedBeforeStatus(String appliedBeforeStatus) {
        this.appliedBeforeStatus = appliedBeforeStatus;
    }

    public String getFruitlessStatus() {
        return fruitlessStatus;
    }

    public void setFruitlessStatus(String fruitlessStatus) {
        this.fruitlessStatus = fruitlessStatus;
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

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalTime getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(LocalTime completionTime) {
        this.completionTime = completionTime;
    }

    public LocalTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalTime returnTime) {
        this.returnTime = returnTime;
    }

    public Report(UUID id, UUID callId, UUID patientId, UUID unitId, String preliminary_diagnosis, String diagnosis_code, String result, String hospStatus, String traumaStatus, String onsetStatus, String appliedBeforeStatus, String fruitlessStatus, String locality, String thoroughfare, String premise, String subPremise, LocalTime departureTime, LocalTime arrivalTime, LocalTime completionTime, LocalTime returnTime) {
        this.id = id;
        this.callId = callId;
        this.patientId = patientId;
        this.unitId = unitId;
        this.preliminary_diagnosis = preliminary_diagnosis;
        this.diagnosis_code = diagnosis_code;
        this.result = result;
        this.hospStatus = hospStatus;
        this.traumaStatus = traumaStatus;
        this.onsetStatus = onsetStatus;
        this.appliedBeforeStatus = appliedBeforeStatus;
        this.fruitlessStatus = fruitlessStatus;
        this.locality = locality;
        this.thoroughfare = thoroughfare;
        this.premise = premise;
        this.subPremise = subPremise;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.completionTime = completionTime;
        this.returnTime = returnTime;
    }
}
