package org.coursework.cassandraambulance;

import com.datastax.oss.driver.api.core.AllNodesFailedException;
import com.datastax.oss.driver.api.core.CqlSession;
import java.net.InetSocketAddress;


// клас, необхідний для з'єднання з базою даних
public class DBConnector {

    private static CqlSession session;
    // назва бази даних
    private static final String KEYSPACE = "ambulance_ver3";

    public static void connectDB(String seeds, int port){
        try{
            session = CqlSession
                    .builder()
                    .addContactPoint(new InetSocketAddress(seeds, port))
                    .withKeyspace(KEYSPACE)
                    .build();

            System.out.println("[Connected] " + session.getContext().getSessionName());
        } catch (AllNodesFailedException e){
            System.out.println(e.getMessage());

        }
    }

    public static void connectDB(){
        // за замовчуванням seeds: localhost port: 9042
        try{
            session = CqlSession
                    .builder()
                    .withKeyspace(KEYSPACE)
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
