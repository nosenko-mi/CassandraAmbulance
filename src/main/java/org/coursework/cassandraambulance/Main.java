package org.coursework.cassandraambulance;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("call_by_date_view.fxml"));
        Parent root  = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.show();
    }

    public static void main(String[] args) {
        System.out.println("java version: "+System.getProperty("java.version"));
        System.out.println("javafx.version: " + System.getProperty("javafx.version"));
        DBConnector.connectDB();
        launch();

        DBConnector.close();
    }
}