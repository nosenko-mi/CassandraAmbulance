package org.coursework.cassandraambulance;

import com.datastax.oss.driver.api.core.cql.PreparedStatement;

public class PreparedStatements {
//TODO реалізувати підготовлені запити для додавання даних у таблиці
    public static PreparedStatement addUnit = DBConnector.getSession().prepare(
            "INSERT INTO " + StringResources.UNIT_BY_ID +
                    " (id, doctor_id, doctor_first_name, doctor_middle_name, doctor_last_name, orderly_id, orderly_first_name, orderly_middle_name, orderly_last_name, driver_id, driver_first_name, driver_middle_name, driver_last_name, car_id, car_serial_number)" +
                    " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
    );

    public static PreparedStatement AddToUnitByEmp = DBConnector.getSession().prepare(
            "INSERT INTO " + StringResources.UNIT_BY_EMP +
                    " (emp_id, unit_id)" +
                    " VALUES(?, ?);"
    );

    public static PreparedStatement AddCallToCallByDate = DBConnector.getSession().prepare(
            "INSERT INTO " + StringResources.CALL_BY_DATE +
                    "(date, time, a_locality, a_thoroughfare, a_premise, a_sub_premise, id, cause, unit_id, caller_id)" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
    );

    public static PreparedStatement AddCallToCallByAddress = DBConnector.getSession().prepare(
            "INSERT INTO " + StringResources.CALL_BY_ADDRESS +
                    "(date, time, a_locality, a_thoroughfare, a_premise, a_sub_premise, id, cause, unit_id, caller_id)" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
    );

    public static PreparedStatement AddCallerToPersons = DBConnector.getSession().prepare(
            "INSERT INTO " + StringResources.PERSONS +
                    " (id, type, first_name, middle_name, last_name)" +
                    "VALUES(?, ?, ?, ?, ?);"
    );

}
