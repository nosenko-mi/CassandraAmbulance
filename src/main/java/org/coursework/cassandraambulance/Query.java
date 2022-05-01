package org.coursework.cassandraambulance;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;

import java.time.LocalDate;
import java.util.UUID;


// клас для підготовки великих запитів
public class Query {

    public static ResultSet GetCallsByDateQuery(LocalDate dateToSearch, String localityToSearch, String thoroughfareToSearch){
        ResultSet rs;
        if (dateToSearch == null && localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty()){

//            StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
//                    .append("booksByTitle").append("(")
//                    .append("id uuid, ")
//                    .append("title text,")
//                    .append("PRIMARY KEY (title, id));");
            final String getCalls = "SELECT * FROM " + StringResources.CALL_BY_DATE + " LIMIT 100";
            rs = DBConnector.getSession().execute(getCalls);

        } else if (dateToSearch == null && thoroughfareToSearch.isEmpty()) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.CALL_BY_DATE + " WHERE a_locality = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(localityToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (dateToSearch == null && localityToSearch.isEmpty()) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.CALL_BY_DATE + " WHERE a_thoroughfare = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(thoroughfareToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (dateToSearch == null) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.CALL_BY_DATE + " WHERE a_locality = ? AND a_thoroughfare = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind( localityToSearch,thoroughfareToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if ( localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty()){
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.CALL_BY_DATE + " WHERE date = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        }  else if (thoroughfareToSearch.isEmpty()) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.CALL_BY_DATE + " WHERE date = ?  AND a_locality = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch, localityToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (localityToSearch.isEmpty()) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.CALL_BY_DATE + " WHERE date = ?  AND a_thoroughfare = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch, thoroughfareToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.CALL_BY_DATE + " WHERE date = ?  AND a_locality = ? AND a_thoroughfare = ? LIMIT 100 ALLOW FILTERING"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch, localityToSearch, thoroughfareToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        }
        return rs;
    }

    public static ResultSet GetCallByAddress(String localityToSearch, String thoroughfareToSearch, String premiseToSearch, String subPremiseToSearch){
        ResultSet rs = null;
        if(!localityToSearch.isEmpty() && !thoroughfareToSearch.isEmpty() && !premiseToSearch.isEmpty() && !subPremiseToSearch.isEmpty()){

            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
                    "SELECT * FROM  " + StringResources.CALL_BY_ADDRESS + "  WHERE a_locality = ? AND a_thoroughfare = ? AND a_premise = ? AND a_sub_premise = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch, thoroughfareToSearch, premiseToSearch, subPremiseToSearch);
            rs = DBConnector.getSession().execute(boundStatement);

        } else if (!localityToSearch.isEmpty() && !thoroughfareToSearch.isEmpty() && !premiseToSearch.isEmpty() && subPremiseToSearch.isEmpty()) {

            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
                    "SELECT * FROM  " + StringResources.CALL_BY_ADDRESS + "  WHERE a_locality = ? AND a_thoroughfare = ? AND a_premise = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch, thoroughfareToSearch, premiseToSearch);
            rs = DBConnector.getSession().execute(boundStatement);

        } else if (!localityToSearch.isEmpty() && !thoroughfareToSearch.isEmpty() && premiseToSearch.isEmpty() && subPremiseToSearch.isEmpty()){

            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
                    "SELECT * FROM  " + StringResources.CALL_BY_ADDRESS + "  WHERE a_locality = ? AND a_thoroughfare = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch, thoroughfareToSearch);
            rs = DBConnector.getSession().execute(boundStatement);

        } else if (!localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty() && premiseToSearch.isEmpty() && subPremiseToSearch.isEmpty()) {

            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
                    "SELECT * FROM  " + StringResources.CALL_BY_ADDRESS + "  WHERE a_locality = ?"
            );
            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch);
            rs = DBConnector.getSession().execute(boundStatement);

        } else if(localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty() && premiseToSearch.isEmpty() && subPremiseToSearch.isEmpty()) {

            final String getCalls = "SELECT * FROM  " + StringResources.CALL_BY_ADDRESS + "  LIMIT 100";
            rs = DBConnector.getSession().execute(getCalls);

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection error");
            alert.setHeaderText("Possible selection options: Locality - > Thoroughfare -> Premise -> Sub premise");
            alert.showAndWait();
        }
        return rs;

//        if(localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty() && premiseToSearch.isEmpty() && subPremiseToSearch.isEmpty()){
//            final String getCalls = "SELECT * FROM  " + StringResources.CALL_BY_ADDRESS + "  LIMIT 100";
//            rs = DBConnector.getSession().execute(getCalls);
//        } else if (thoroughfareToSearch.isEmpty() && premiseToSearch.isEmpty() && subPremiseToSearch.isEmpty()) {
//            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
//                    "SELECT * FROM  " + StringResources.CALL_BY_ADDRESS + "  WHERE a_locality = ?"
//            );
//            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch);
//            rs = DBConnector.getSession().execute(boundStatement);
//        } else if (premiseToSearch.isEmpty() && subPremiseToSearch.isEmpty()){
//            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
//                    "SELECT * FROM  " + StringResources.CALL_BY_ADDRESS + "  WHERE a_locality = ? AND a_thoroughfare = ? LIMIT 100"
//            );
//            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch, thoroughfareToSearch);
//            rs = DBConnector.getSession().execute(boundStatement);
//        } else if (subPremiseToSearch.isEmpty()) {
//            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
//                    "SELECT * FROM  " + StringResources.CALL_BY_ADDRESS + "  WHERE a_locality = ? AND a_thoroughfare = ? AND a_premise = ? LIMIT 100"
//            );
//            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch, thoroughfareToSearch, premiseToSearch);
//            rs = DBConnector.getSession().execute(boundStatement);
//        } else if (!localityToSearch.isEmpty() && !thoroughfareToSearch.isEmpty() && !premiseToSearch.isEmpty() && !subPremiseToSearch.isEmpty()) {
//            PreparedStatement selectAllCallsByAddress = DBConnector.getSession().prepare(
//                    "SELECT * FROM  " + StringResources.CALL_BY_ADDRESS + "  WHERE a_locality = ? AND a_thoroughfare = ? AND a_premise = ? and a_sub_premise = ? LIMIT 100"
//            );
//            BoundStatement boundStatement = selectAllCallsByAddress.bind(localityToSearch, thoroughfareToSearch, premiseToSearch, subPremiseToSearch);
//            rs = DBConnector.getSession().execute(boundStatement);
//        } else {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Selection error");
//            alert.setHeaderText("Possible selection options: Locality - > Thoroughfare -> Premise -> Sub premise");
//            alert.showAndWait();
//        }
    }

    public static ResultSet GetEmployees(CheckBox doctorCheckBox, CheckBox OrderlyCheckBox, CheckBox DriverCheckBox){
        ResultSet rs;
        StringBuilder selectEmployees;
        if (!doctorCheckBox.isSelected() && !OrderlyCheckBox.isSelected() && !DriverCheckBox.isSelected()){
            selectEmployees = new StringBuilder("SELECT * FROM " + StringResources.PERSONS + " WHERE type IN ( 'Лікар', 'Водій', 'Санітар');");
            System.out.println(selectEmployees);
        } else {
            boolean isFirstItem = true;
            selectEmployees
                    = new StringBuilder("SELECT * FROM ")
                    .append(StringResources.PERSONS)
                    .append(" WHERE type IN (");

            if (doctorCheckBox.isSelected()){
                selectEmployees.append(StringResources.DOCTOR);
                isFirstItem = false;
            }
            if (OrderlyCheckBox.isSelected()){
                if (!isFirstItem){
                    selectEmployees.append(", ");
                }
                selectEmployees.append(StringResources.ORDERLY);
                isFirstItem = false;

            }
            if (DriverCheckBox.isSelected()){
                if (!isFirstItem){
                    selectEmployees.append(", ");
                }
                selectEmployees.append(StringResources.DRIVER);
            }
            selectEmployees.append(");");
            System.out.println(selectEmployees);
        }
        rs = DBConnector.getSession().execute(selectEmployees.toString());
        return rs;
    }

    public static ResultSet GetUnitById(){
        ResultSet rs = null;
        return rs;
    }

    public static ResultSet GetPersonByTypeId(String type, UUID id){
        ResultSet rs;

        PreparedStatement getPerson;
        BoundStatement boundStatement;


        if (id == null){
            getPerson = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.PERSONS + " WHERE type = ? ;"
            );
            boundStatement = getPerson.bind(type);
        } else {
            getPerson = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.PERSONS + " WHERE type = ? AND id = ? ;"
            );
            boundStatement = getPerson.bind(type, id);
        }

        rs = DBConnector.getSession().execute(boundStatement);

        return rs;
    }

    public static ResultSet GetPersonByTypeName(String type, String fn, String mn, String ln){
        ResultSet rs;

        PreparedStatement getPerson;
        BoundStatement boundStatement;

        if (type == null && fn.isEmpty() && mn.isEmpty()){
            getPerson = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.PERSONS + " WHERE last_name = ? ;"
            );
            boundStatement = getPerson.bind(ln);
        } else if (type == null && mn.isEmpty()){

            getPerson = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.PERSONS + " WHERE last_name = ? AND first_name = ? ALLOW FILTERING ;"
            );
            boundStatement = getPerson.bind(ln, fn);

        } else if (type == null && fn.isEmpty()){

            getPerson = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.PERSONS + " WHERE last_name = ? AND middle_name = ? ALLOW FILTERING ;"
            );
            boundStatement = getPerson.bind(ln, mn);

        } else if (type == null && ln.isEmpty()){

            getPerson = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.PERSONS + " WHERE first_name = ? AND middle_name = ? ALLOW FILTERING ;"
            );
            boundStatement = getPerson.bind(fn, mn);

        } else if (type != null && fn.isEmpty() && mn.isEmpty() && ln.isEmpty()){

            getPerson = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.PERSONS + " WHERE type = ? ;"
            );
            boundStatement = getPerson.bind(type);

        } else if (type != null && fn.isEmpty() && mn.isEmpty()){

            getPerson = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.PERSONS + " WHERE type = ? AND last_name = ? ALLOW FILTERING ;"
            );
            boundStatement = getPerson.bind(type, ln);

        } else if (type != null && ln.isEmpty() && mn.isEmpty()){

            getPerson = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.PERSONS + " WHERE type = ? AND first_name = ? ALLOW FILTERING ;"
            );
            boundStatement = getPerson.bind(type, fn);

        } else if (type != null && ln.isEmpty() && fn.isEmpty()){

            getPerson = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.PERSONS + " WHERE type = ? AND middle_name = ? ALLOW FILTERING ;"
            );
            boundStatement = getPerson.bind(type, mn);

        } else if (type != null && mn.isEmpty()){

            getPerson = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.PERSONS + " WHERE type = ? AND first_name = ? AND last_name = ? ALLOW FILTERING ;"
            );
            boundStatement = getPerson.bind(type, fn, ln);

        } else if (type != null && fn.isEmpty()){

            getPerson = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.PERSONS + " WHERE type = ? AND middle_name = ? AND last_name = ? ALLOW FILTERING ;"
            );
            boundStatement = getPerson.bind(type, mn, ln);

        } else if (type != null && ln.isEmpty()){

            getPerson = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.PERSONS + " WHERE type = ? AND first_name = ? AND middle_name = ? ALLOW FILTERING ;"
            );
            boundStatement = getPerson.bind(type, fn, mn);

        }  else {

            getPerson = DBConnector.getSession().prepare(
                    "SELECT * FROM " + StringResources.PERSONS + " WHERE type = ? AND middle_name = ? ALLOW FILTERING ;"
            );
            boundStatement = getPerson.bind(type, mn);

        }

//        else if (type != null && fn.isEmpty() && ln.isEmpty()){
//
//            getPerson = DBConnector.getSession().prepare(
//                    "SELECT * FROM " + StringResources.PERSONS + " WHERE type = ? AND middle_name = ? ALLOW FILTERING ;"
//            );
//            boundStatement = getPerson.bind(type, mn);
//
//        }

        rs = DBConnector.getSession().execute(boundStatement);
        return rs;
    }

    public  static ResultSet GetOneCall(){
        ResultSet rs = null;
        return rs;
    }

    public  static ResultSet GetOneReport(UUID callId, UUID reportId){
        ResultSet rs;

        PreparedStatement getOneCall = DBConnector.getSession().prepare(
                "SELECT * FROM " + StringResources.REPORT_BY_CALL + " WHERE call_id = ? AND id = ? ;"
        );
        BoundStatement boundStatement = getOneCall.bind(callId, reportId);
        rs = DBConnector.getSession().execute(boundStatement);
        return rs;
    }

    public static ResultSet GetOnePatient(UUID id){
        ResultSet rs;

        PreparedStatement getOnePatient = DBConnector.getSession().prepare(
                "SELECT * FROM " + StringResources.PATIENTS + " WHERE id = ? ;"
        );
        BoundStatement boundStatement = getOnePatient.bind(id);
        rs = DBConnector.getSession().execute(boundStatement);
        return rs;
    }


}
