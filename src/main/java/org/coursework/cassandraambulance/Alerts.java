package org.coursework.cassandraambulance;

import javafx.scene.control.Alert;

public class Alerts {
    private static Alert alert;
    private static String ERROR_TITLE = "Error";
    private static String SUCCESS_TITLE = "Success";

    public static void MissingPrimaryKey(String content){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ERROR_TITLE);
        alert.setHeaderText("Primary keys are missing");
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void SucceedOperation(){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(SUCCESS_TITLE);
        alert.setHeaderText("Operation succeed");
        alert.showAndWait();
    }

}
