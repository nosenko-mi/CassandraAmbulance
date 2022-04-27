package org.coursework.cassandraambulance;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;

import java.time.LocalDate;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;

public class Query {

    public static ResultSet GetCallsByDateQuery(LocalDate dateToSearch, String localityToSearch, String thoroughfareToSearch){
        ResultSet rs;
        if (dateToSearch == null && localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty()){

//            StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
//                    .append("booksByTitle").append("(")
//                    .append("id uuid, ")
//                    .append("title text,")
//                    .append("PRIMARY KEY (title, id));");
            final String getCalls = "SELECT * FROM " + TableName.CALL_BY_DATE + " LIMIT 100";
            rs = DBConnector.getSession().execute(getCalls);

        } else if (dateToSearch == null && thoroughfareToSearch.isEmpty()) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + TableName.CALL_BY_DATE + " WHERE a_locality = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(localityToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (dateToSearch == null && localityToSearch.isEmpty()) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + TableName.CALL_BY_DATE + " WHERE a_thoroughfare = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(thoroughfareToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (dateToSearch == null) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + TableName.CALL_BY_DATE + " WHERE a_locality = ? AND a_thoroughfare = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind( localityToSearch,thoroughfareToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if ( localityToSearch.isEmpty() && thoroughfareToSearch.isEmpty()){
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + TableName.CALL_BY_DATE + " WHERE date = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        }  else if (thoroughfareToSearch.isEmpty()) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + TableName.CALL_BY_DATE + " WHERE date = ?  AND a_locality = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch, localityToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else if (localityToSearch.isEmpty()) {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + TableName.CALL_BY_DATE + " WHERE date = ?  AND a_thoroughfare = ? LIMIT 100"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch, thoroughfareToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        } else {
            PreparedStatement selectAllCallsByDate = DBConnector.getSession().prepare(
                    "SELECT * FROM " + TableName.CALL_BY_DATE + " WHERE date = ?  AND a_locality = ? AND a_thoroughfare = ? LIMIT 100 ALLOW FILTERING"
            );
            BoundStatement boundStatement = selectAllCallsByDate.bind(dateToSearch, localityToSearch, thoroughfareToSearch);
            rs = DBConnector.getSession().execute(boundStatement);
        }
        return rs;
    }
}
