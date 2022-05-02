package org.coursework.cassandraambulance.controllers;

import javafx.scene.input.MouseEvent;
import org.coursework.cassandraambulance.ViewSwitcher;

public class Controller {

    public void SwitchToCallByDate(MouseEvent mouseEvent) {
        ViewSwitcher.Switch(mouseEvent, "call_by_date_view.fxml", "/style.css");

    }

    public void SwitchToCallByAddress(MouseEvent mouseEvent) {
        ViewSwitcher.Switch(mouseEvent, "call_by_address_view.fxml", "/style.css");

    }

    public void SwitchToCallByUnit(MouseEvent mouseEvent) {
        ViewSwitcher.Switch(mouseEvent, "call_by_unit_view.fxml", "/style.css");

    }

    public void SwitchToReportByCall(MouseEvent mouseEvent) {
        ViewSwitcher.Switch(mouseEvent, "report_by_call_view.fxml", "/style.css");

    }

    public void SwitchToUnitByEmployee(MouseEvent mouseEvent) {
        ViewSwitcher.Switch(mouseEvent, "unit_by_emp_view.fxml", "/style.css");
    }

    public void SwitchToUnitById(MouseEvent mouseEvent) {
        ViewSwitcher.Switch(mouseEvent, "unit_by_id_view.fxml", "/style.css");
    }

    public void SwitchToGetPersons(MouseEvent mouseEvent) {
        ViewSwitcher.Switch(mouseEvent, "get_persons_view.fxml", "/style.css");

    }

    public void SwitchToAddReport(MouseEvent mouseEvent) {
        ViewSwitcher.Switch(mouseEvent, "add_report_view.fxml", "/style.css");
    }

    public void SwitchToUpdateReport(MouseEvent mouseEvent) {
        ViewSwitcher.Switch(mouseEvent, "update_report_view.fxml", "/style.css");
    }

    public void SwitchToAddCall(MouseEvent mouseEvent) {
        ViewSwitcher.Switch(mouseEvent, "add_call_view.fxml", "/style.css");
    }

    public void SwitchToUpdateCall(MouseEvent mouseEvent) {
        ViewSwitcher.Switch(mouseEvent, "update_call_view.fxml", "/style.css");
    }

    public void SwitchToUpdatePerson(MouseEvent mouseEvent) {
        ViewSwitcher.Switch(mouseEvent, "update_persons_view.fxml", "/style.css");
    }

    public void SwitchToGetPatients(MouseEvent mouseEvent){
        ViewSwitcher.Switch(mouseEvent, "get_patients_view.fxml", "/style.css");
    }

    public void SwitchToReportByHosp(MouseEvent mouseEvent){
        ViewSwitcher.Switch(mouseEvent, "report_by_hosp_view.fxml", "/style.css");

    }

}
