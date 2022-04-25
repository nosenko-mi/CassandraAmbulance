package org.coursework.cassandraambulance.models;

import java.util.UUID;

public class Unit {
    private UUID id, doctorId, orderlyId, driverId, carId;
    private String doctorFN, doctorMN, doctorLN, orderlyFN, orderlyMN, orderlyLN, driverFN, driverMN, driverLN, carSerialNumber;

    public Unit(UUID id, UUID doctorId, UUID orderlyId, UUID driverId, UUID carId, String doctorFN, String doctorMN, String doctorLN, String orderlyFN, String orderlyMN, String orderlyLN, String driverFN, String driverMN, String driverLN, String carSerialNumber) {
        this.id = id;
        this.doctorId = doctorId;
        this.orderlyId = orderlyId;
        this.driverId = driverId;
        this.carId = carId;
        this.doctorFN = doctorFN;
        this.doctorMN = doctorMN;
        this.doctorLN = doctorLN;
        this.orderlyFN = orderlyFN;
        this.orderlyMN = orderlyMN;
        this.orderlyLN = orderlyLN;
        this.driverFN = driverFN;
        this.driverMN = driverMN;
        this.driverLN = driverLN;
        this.carSerialNumber = carSerialNumber;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
    }

    public UUID getOrderlyId() {
        return orderlyId;
    }

    public void setOrderlyId(UUID orderlyId) {
        this.orderlyId = orderlyId;
    }

    public UUID getDriverId() {
        return driverId;
    }

    public void setDriverId(UUID driverId) {
        this.driverId = driverId;
    }

    public UUID getCarId() {
        return carId;
    }

    public void setCarId(UUID carId) {
        this.carId = carId;
    }

    public String getDoctorFN() {
        return doctorFN;
    }

    public void setDoctorFN(String doctorFN) {
        this.doctorFN = doctorFN;
    }

    public String getDoctorMN() {
        return doctorMN;
    }

    public void setDoctorMN(String doctorMN) {
        this.doctorMN = doctorMN;
    }

    public String getDoctorLN() {
        return doctorLN;
    }

    public void setDoctorLN(String doctorLN) {
        this.doctorLN = doctorLN;
    }

    public String getOrderlyFN() {
        return orderlyFN;
    }

    public void setOrderlyFN(String orderlyFN) {
        this.orderlyFN = orderlyFN;
    }

    public String getOrderlyMN() {
        return orderlyMN;
    }

    public void setOrderlyMN(String orderlyMN) {
        this.orderlyMN = orderlyMN;
    }

    public String getOrderlyLN() {
        return orderlyLN;
    }

    public void setOrderlyLN(String orderlyLN) {
        this.orderlyLN = orderlyLN;
    }

    public String getDriverFN() {
        return driverFN;
    }

    public void setDriverFN(String driverFN) {
        this.driverFN = driverFN;
    }

    public String getDriverMN() {
        return driverMN;
    }

    public void setDriverMN(String driverMN) {
        this.driverMN = driverMN;
    }

    public String getDriverLN() {
        return driverLN;
    }

    public void setDriverLN(String driverLN) {
        this.driverLN = driverLN;
    }

    public String getCarSerialNumber() {
        return carSerialNumber;
    }

    public void setCarSerialNumber(String carSerialNumber) {
        this.carSerialNumber = carSerialNumber;
    }
}
