package org.coursework.cassandraambulance;

import com.datastax.oss.driver.api.core.cql.PreparedStatement;

public class PreparedStatements {
//TODO реалізувати підготовлені запити для додавання даних у таблиці
    public static PreparedStatement addUnit = DBConnector.getSession().prepare(
            "INSERT INTO " + StringResources.UNIT_BY_ID +
                    " (id, doctor_id, doctor_first_name, doctor_middle_name, doctor_last_name, orderly_id, orderly_first_name, orderly_middle_name, orderly_last_name, driver_id, driver_first_name, driver_middle_name, driver_last_name, car_id, car_serial_number)" +
                    " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
    );

    public static PreparedStatement addToUnitByEmp = DBConnector.getSession().prepare(
            "INSERT INTO " + StringResources.UNIT_BY_EMP +
                    " (emp_id, unit_id)" +
                    " VALUES(?, ?);"
    );

    public static PreparedStatement addCallToCallByDate = DBConnector.getSession().prepare(
            "INSERT INTO " + StringResources.CALL_BY_DATE +
                    "(date, time, a_locality, a_thoroughfare, a_premise, a_sub_premise, id, cause, unit_id, caller_id)" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
    );

    public static PreparedStatement addCallToCallByAddress = DBConnector.getSession().prepare(
            "INSERT INTO " + StringResources.CALL_BY_ADDRESS +
                    "(date, time, a_locality, a_thoroughfare, a_premise, a_sub_premise, id, cause, unit_id, caller_id)" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
    );

    public static PreparedStatement addCallerToPersons = DBConnector.getSession().prepare(
            "INSERT INTO " + StringResources.PERSONS +
                    " (id, type, first_name, middle_name, last_name)" +
                    "VALUES(?, ?, ?, ?, ?);"
    );

    public static PreparedStatement AddEmployeeToPersons = DBConnector.getSession().prepare(
            "INSERT INTO " + StringResources.PERSONS +
                    " (type, id, first_name, middle_name, last_name) " +
                    " VALUES(?, ?, ?, ?, ?);"
    );

    public static PreparedStatement addReportToReportByCall = DBConnector.getSession().prepare(
            "INSERT INTO " + StringResources.REPORT_BY_CALL +
                    " (call_id, id, unit_id, patient_id," +
                    " a_locality, a_thoroughfare, a_premise, a_sub_premise," +
                    " departure_time, arrival_time, completion_time, return_time," +
                    " hospitalization_status, preliminary_diagnosis, diagnosis_code," +
                    " result, trauma, applied_before, onset, fruitless)" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

    public static PreparedStatement addPatientToPatients =DBConnector.getSession().prepare(
            "INSERT INTO ambulance_ver3.patients" +
                    "(id, dob, first_name, middle_name, last_name)" +
                    "VALUES(?, ?, ?, ?, ?);"
    );
}
