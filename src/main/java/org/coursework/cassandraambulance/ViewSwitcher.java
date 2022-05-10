package org.coursework.cassandraambulance;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewSwitcher {

    private static Parent root;
    private static Stage stage;
    private static Scene scene;



    public static void Switch(MouseEvent mouseEvent, String viewName, String styleName){
        try {
            root = FXMLLoader.load(ViewSwitcher.class.getResource(viewName));
            stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(ViewSwitcher.class.getResource(styleName).toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();

        }

    }

}
