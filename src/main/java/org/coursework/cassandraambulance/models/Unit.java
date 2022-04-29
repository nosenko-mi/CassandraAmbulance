package org.coursework.cassandraambulance.models;

import java.util.UUID;

// клас, що представляє бригаду

public class Unit {
    private UUID id, doctorId, orderlyId, driverId, carId;
    private String doctorFn, doctorMn, doctorLn, orderlyFn, orderlyMn, orderlyLn, driverFn, driverMn, driverLn, carSerialNumber;

    public Unit(UUID id, UUID doctorId, UUID orderlyId, UUID driverId, UUID carId,
                String doctorFn, String doctorMn, String doctorLn,
                String orderlyFn, String orderlyMn, String orderlyLn,
                String driverFn, String driverMn, String driverLn,
                String carSerialNumber) {
        this.id = id;
        this.doctorId = doctorId;
        this.orderlyId = orderlyId;
        this.driverId = driverId;
        this.carId = carId;
        this.doctorFn = doctorFn;
        this.doctorMn = doctorMn;
        this.doctorLn = doctorLn;
        this.orderlyFn = orderlyFn;
        this.orderlyMn = orderlyMn;
        this.orderlyLn = orderlyLn;
        this.driverFn = driverFn;
        this.driverMn = driverMn;
        this.driverLn = driverLn;
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

    public String getDoctorFn() {
        return doctorFn;
    }

    public void setDoctorFn(String doctorFn) {
        this.doctorFn = doctorFn;
    }

    public String getDoctorMn() {
        return doctorMn;
    }

    public void setDoctorMn(String doctorMn) {
        this.doctorMn = doctorMn;
    }

    public String getDoctorLn() {
        return doctorLn;
    }

    public void setDoctorLn(String doctorLn) {
        this.doctorLn = doctorLn;
    }

    public String getOrderlyFn() {
        return orderlyFn;
    }

    public void setOrderlyFn(String orderlyFn) {
        this.orderlyFn = orderlyFn;
    }

    public String getOrderlyMn() {
        return orderlyMn;
    }

    public void setOrderlyMn(String orderlyMn) {
        this.orderlyMn = orderlyMn;
    }

    public String getOrderlyLn() {
        return orderlyLn;
    }

    public void setOrderlyLn(String orderlyLn) {
        this.orderlyLn = orderlyLn;
    }

    public String getDriverFn() {
        return driverFn;
    }

    public void setDriverFn(String driverFn) {
        this.driverFn = driverFn;
    }

    public String getDriverMn() {
        return driverMn;
    }

    public void setDriverMn(String driverMn) {
        this.driverMn = driverMn;
    }

    public String getDriverLn() {
        return driverLn;
    }

    public void setDriverLn(String driverLn) {
        this.driverLn = driverLn;
    }

    public String getCarSerialNumber() {
        return carSerialNumber;
    }

    public void setCarSerialNumber(String carSerialNumber) {
        this.carSerialNumber = carSerialNumber;
    }
}
