package org.coursework.cassandraambulance;

// коллекція строкових ресурсів, що використовуються в програмі
public class StringResources {

//    назви таблиць
    public static final String CALL_BY_DATE = "call_by_date";
    public static final String CALL_BY_ADDRESS = "call_by_address";
    public static final String REPORT_BY_CALL = "report_by_call";
    public static final String UNIT_BY_EMP = "unit_by_emp";
    public static final String UNIT_BY_ID = "unit_by_id";
    public static final String PERSONS = "persons";
    public static final String PATIENTS = "patients";
    public static final String AMBULANCE_CARS = "ambulance_cars";

//   використовюються у запитах, тому значення у ''
    public static final String DRIVER = "'Водій'";
    public static final String ORDERLY = "'Санітар'";
    public static final String DOCTOR = "'Лікар'";

    public static final String DRIVER_TYPE = "Водій";
    public static final String ORDERLY_TYPE = "Санітар";
    public static final String DOCTOR_TYPE = "Лікар";
    public static final String CALLER_TYPE = "Викликач";

    public static final String HOSP_TRUE = "Госпіталізовано";
    public static final String HOSP_FALSE = "Не госпіталізовано";
    public static final String HOSP_DEFAULT = "Hospitalization";

}
