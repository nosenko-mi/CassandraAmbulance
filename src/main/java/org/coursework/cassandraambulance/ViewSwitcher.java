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

    public static void SwitchToCallByDate(MouseEvent mouseEvent){
        try {
            root = FXMLLoader.load(ViewSwitcher.class.getResource("call_by_date_view.fxml"));
            stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(ViewSwitcher.class.getResource("/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();

        }

    }

    public static void SwitchToCallByAddress(MouseEvent mouseEvent){
        try {
            root = FXMLLoader.load(ViewSwitcher.class.getResource("call_by_address_view.fxml"));
            stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(ViewSwitcher.class.getResource("/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();

        }
    }

    public static void SwitchToCallByUnit(MouseEvent mouseEvent){
        try {
            root = FXMLLoader.load(ViewSwitcher.class.getResource("call_by_unit_view.fxml"));
            stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(ViewSwitcher.class.getResource("/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();

        }


    }

    public static void SwitchToReportByCall(MouseEvent mouseEvent){
        try {
            root = FXMLLoader.load(ViewSwitcher.class.getResource("report_by_call_view.fxml"));
            stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(ViewSwitcher.class.getResource("/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();

        }
    }

    public static void SwitchToReportByHosp(MouseEvent mouseEvent){
        try {
            root = FXMLLoader.load(ViewSwitcher.class.getResource("report_by_hosp_view.fxml"));
            stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(ViewSwitcher.class.getResource("/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();

        }
    }

    public static void SwitchToUnitById(MouseEvent mouseEvent){
        try {
            root = FXMLLoader.load(ViewSwitcher.class.getResource("unit_by_id_view.fxml"));
            stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(ViewSwitcher.class.getResource("/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();

        }
    }

    public static void SwitchToUnitByEmp(MouseEvent mouseEvent){
        try {
            root = FXMLLoader.load(ViewSwitcher.class.getResource("unit_by_emp_view.fxml"));
            stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(ViewSwitcher.class.getResource("/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();

        }
    }

    public static void SwitchToPersons(MouseEvent mouseEvent){
        try {
            root = FXMLLoader.load(ViewSwitcher.class.getResource("persons_view.fxml"));
            stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(ViewSwitcher.class.getResource("/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();

        }
    }

    public static void SwitchToAddCall(MouseEvent mouseEvent){
        try {
            root = FXMLLoader.load(ViewSwitcher.class.getResource("add_call_view.fxml"));
            stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(ViewSwitcher.class.getResource("/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    public static void SwitchToAddReport(MouseEvent mouseEvent){
        try {
            root = FXMLLoader.load(ViewSwitcher.class.getResource("add_report_view.fxml"));
            stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(ViewSwitcher.class.getResource("/style.css").toExternalForm());
            stage.setScene(scene);

            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }

    }


}
