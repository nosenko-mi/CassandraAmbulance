package org.coursework.cassandraambulance;
import com.datastax.oss.driver.api.core.AllNodesFailedException;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.core.metadata.Metadata;

import java.net.InetSocketAddress;

public class DBConnector {

    static CqlSession session;

    public static void connectDB(String seeds, int port){

        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(seeds, port))
                .build();
    }

    public static void connectDB(){
        try{
            session = CqlSession.builder()
                    .withKeyspace("ambulance_ver3")
                    .build();

            System.out.println("[Connected] " + session.getContext().getSessionName());
        } catch (AllNodesFailedException e){
            System.out.println(e.getMessage());

        }

    }

    public static CqlSession getSession() {
        return session;
    }

    public static void close(){
        System.out.println("[Closed] " + session.getContext().getSessionName());

        session.close();
    }
}
