package com.devstack.pos.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardFormController {

    public AnchorPane context;

    public void customerManagementOnAction(ActionEvent actionEvent) throws IOException {
        setUi("CustomerForm");
    }

    public void productManagementOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ProductMainForm");
    }

    public void placeOrderOnAction(ActionEvent actionEvent) throws IOException {
        setUi("PlaceOrderForm");
    }

    public void OrderDetailsOnAction(ActionEvent actionEvent) {

    }

    public void incomeReportOnAction(ActionEvent actionEvent) {

    }

    private void setUi(String url) throws IOException {
        Stage stage = (Stage)context.getScene().getWindow();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/"+url+".fxml")))
        );
        stage.centerOnScreen();
    }
}
