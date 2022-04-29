package org.coursework.cassandraambulance.models;

import java.util.UUID;


// клас, що представляє сутність співпрацівник-бригада
public class EmployeeUnit {
    private UUID empId, unitId;

    public UUID getEmpId() {
        return empId;
    }

    public void setEmpId(UUID empId) {
        this.empId = empId;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public void setUnitId(UUID unitId) {
        this.unitId = unitId;
    }

    public EmployeeUnit(UUID empId, UUID unitId) {
        this.empId = empId;
        this.unitId = unitId;
    }
}
