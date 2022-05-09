package org.coursework.cassandraambulance;

import com.datastax.oss.driver.api.core.cql.PreparedStatement;

public class PreparedStatements {
//TODO реалізувати підготовлені запити для додавання даних у таблиці
    public static PreparedStatement addUnit = DBConnector.getSession().prepare(
            "INSERT INTO " + StringResources.UNIT_BY_ID +
                    " (id, doctor_id, doctor_first_name, doctor_middle_name, doctor_last_name, orderly_id, orderly_first_name, orderly_middle_name, orderly_last_name, driver_id, driver_first_name, driver_middle_name, driver_last_name, car_id, car_serial_number)" +
                    " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
    );

    public static PreparedStatement deleteUnit = DBConnector.getSession().prepare(
            "DELETE FROM " + StringResources.UNIT_BY_ID +
                    " WHERE id = ?"
    );

    public static PreparedStatement addToUnitByEmp = DBConnector.getSession().prepare(
            "INSERT INTO " + StringResources.UNIT_BY_EMP +
                    " (emp_id, unit_id)" +
                    " VALUES(?, ?);"
    );

    public static PreparedStatement deleteFromUnitByEmp = DBConnector.getSession().prepare(
            "DELETE FROM " + StringResources.UNIT_BY_EMP +
                    " WHERE emp_id = ? AND unit_id = ?;"
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


    public static PreparedStatement addPatientToPatients = DBConnector.getSession().prepare(
            "INSERT INTO " + StringResources.PATIENTS +
                    " (dob, id, first_name, middle_name, last_name) " +
                    "VALUES(?, ?, ?, ?, ?);"
    );

    public static PreparedStatement deleteOnePatientById = DBConnector.getSession().prepare(
            "DELETE FROM " + StringResources.PATIENTS + " WHERE id = ? ;"
    );

    public static PreparedStatement selectOneCallFromCallByDate = DBConnector.getSession().prepare(
            "SELECT * FROM " + StringResources.CALL_BY_DATE + " WHERE date = ? AND time = ? and id = ?"
    );

    public static PreparedStatement selectOneCallerFromPersons = DBConnector.getSession().prepare(
            "SELECT * FROM " + StringResources.PERSONS + " WHERE type = 'Викликач' AND id = ?"
    );

    public static PreparedStatement updateCallInCallByDate = DBConnector.getSession().prepare(
            "UPDATE " + StringResources.CALL_BY_DATE +
                    " SET a_locality = ? , a_thoroughfare = ? , a_premise = ?, a_sub_premise = ?, cause = ?, unit_id = ?" +
                    " WHERE date = ? AND time = ? AND id = ?;"
    );

    public static PreparedStatement deleteCall = DBConnector.getSession().prepare(
            "DELETE FROM " + StringResources.CALL_BY_DATE + " WHERE date = ? AND time = ? AND id = ?;"
    );

    public static PreparedStatement deleteCallFromCallByAddress = DBConnector.getSession().prepare(
            "DELETE FROM " + StringResources.CALL_BY_ADDRESS +
                    " WHERE a_locality = ? AND a_thoroughfare = ? AND a_premise = ? AND a_sub_premise = ? AND id = ?;"
    );

    public static PreparedStatement updateCallerInPersons = DBConnector.getSession().prepare(
            "UPDATE " + StringResources.PERSONS +
                    " SET first_name = ? , middle_name = ? , last_name = ?" +
                    " WHERE type = ? AND id = ? ;"
    );

    public static PreparedStatement updatePersonInPersons = DBConnector.getSession().prepare(
            "UPDATE " + StringResources.PERSONS +
                    " SET  first_name = ?, middle_name = ?, last_name = ?" +
                    "WHERE type = ? AND id = ?"
    );

    public static PreparedStatement deletePersonFromPersons = DBConnector.getSession().prepare(
            "DELETE FROM " + StringResources.PERSONS +
                    " WHERE type = ? AND id = ?"
    );

    public static PreparedStatement updateReportInReportByCall = DBConnector.getSession().prepare(
            "UPDATE " + StringResources.REPORT_BY_CALL +
                    " SET a_locality = ? , a_thoroughfare = ? , a_premise = ?, a_sub_premise = ?, " +
                    "departure_time = ?, arrival_time = ?, completion_time = ?, return_time = ?, " +
                    "preliminary_diagnosis = ?, diagnosis_code = ?, result = ?, hospitalization_status = ?, " +
                    "applied_before = ?, trauma = ?, onset = ?, fruitless = ? , unit_id = ?" +
                    "WHERE call_id = ? AND id = ?;"
    );

    public static PreparedStatement deleteReportFromReportByCall = DBConnector.getSession().prepare(
            "DELETE FROM " + StringResources.REPORT_BY_CALL + " WHERE call_id = ? and id = ?;"
    );

    public static PreparedStatement updatePatientInReportByCall = DBConnector.getSession().prepare(
            "UPDATE " + StringResources.REPORT_BY_CALL +
                    " SET patient_id = ? " +
                    "WHERE call_id = ? AND id = ?;"
    );

    public static PreparedStatement deletePatientFromPatient = DBConnector.getSession().prepare(
            "DELETE FROM " + StringResources.PATIENTS + " WHERE id = ? AND dob = ?;"
    );

    public static PreparedStatement updatePatientNameInPatients = DBConnector.getSession().prepare(
            "UPDATE " + StringResources.PATIENTS +
                    " SET first_name = ? , middle_name = ? , last_name = ?" +
                    " WHERE id = ? AND dob = ? ;"
    );


}
