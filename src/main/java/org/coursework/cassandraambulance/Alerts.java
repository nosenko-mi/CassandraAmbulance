package org.coursework.cassandraambulance;

import javafx.scene.control.Alert;

public class Alerts {
    private static Alert alert;
    private static final String ERROR_TITLE = "Error";
    private static final String SUCCESS_TITLE = "Success";
    private static final String WARNING_TITLE = "Warning";

    public static void MissingPrimaryKey(String content){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ERROR_TITLE);
        alert.setHeaderText("Primary keys are missing");
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void ParseError(String content){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ERROR_TITLE);
        alert.setHeaderText("Parse error");
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void SucceedOperation(){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(SUCCESS_TITLE);
        alert.setHeaderText("Operation succeed");
        alert.showAndWait();
    }

    public static void WrongSearchParameters(String content){
        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(WARNING_TITLE);
        alert.setHeaderText("Search parameters is incorrect");
        alert.setContentText(content);
        alert.showAndWait();
    }



}
