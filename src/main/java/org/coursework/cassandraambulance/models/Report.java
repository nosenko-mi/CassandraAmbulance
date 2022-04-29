package org.coursework.cassandraambulance.models;

import java.time.LocalTime;
import java.util.UUID;


// клас, що представляє звіт
public class Report {
    private UUID id, callId, patientId, unitId;
    private String preliminaryDiagnosis, diagnosisCode, resultStatus, hospitalizationStatus, traumaStatus, onsetStatus, appliedBeforeStatus, fruitlessStatus, locality, thoroughfare, premise, subPremise;
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

    public String getPreliminaryDiagnosis() {
        return preliminaryDiagnosis;
    }

    public void setPreliminaryDiagnosis(String preliminaryDiagnosis) {
        this.preliminaryDiagnosis = preliminaryDiagnosis;
    }

    public String getDiagnosisCode() {
        return diagnosisCode;
    }

    public void setDiagnosisCode(String diagnosisCode) {
        this.diagnosisCode = diagnosisCode;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getHospitalizationStatus() {
        return hospitalizationStatus;
    }

    public void setHospitalizationStatus(String hospitalizationStatus) {
        this.hospitalizationStatus = hospitalizationStatus;
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

    public Report(UUID id, UUID callId, UUID patientId, UUID unitId, String preliminaryDiagnosis, String diagnosisDode, String resultStatus, String hospitalizatonStatus, String traumaStatus, String onsetStatus, String appliedBeforeStatus, String fruitlessStatus, String locality, String thoroughfare, String premise, String subPremise, LocalTime departureTime, LocalTime arrivalTime, LocalTime completionTime, LocalTime returnTime) {
        this.id = id;
        this.callId = callId;
        this.patientId = patientId;
        this.unitId = unitId;
        this.preliminaryDiagnosis = preliminaryDiagnosis;
        this.diagnosisCode = diagnosisDode;
        this.resultStatus = resultStatus;
        this.hospitalizationStatus = hospitalizatonStatus;
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
