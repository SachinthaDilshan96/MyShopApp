package com.devstack.pos.controller;

import com.devstack.pos.dao.DatabaseAccessCode;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class CustomerFormController {


    public TextField txtEmail;
    public TextField txtName;
    public TextField txtContact;
    public TextField txtSalary;
    public TextField txtSearch;
    public TableView tblCustomer;
    public TableColumn colNumber;
    public TableColumn colEMail;
    public TableColumn colName;
    public TableColumn colContact;
    public TableColumn colSalary;
    public TableColumn colOperator;

    public void BackToHomeOnAction(ActionEvent actionEvent) {
    }

    public void NewCustomerOnAction(ActionEvent actionEvent) {
        try{
            if(DatabaseAccessCode.createCustomer(txtEmail.getText(),txtName.getText(),txtContact.getText(),Double.parseDouble(txtSalary.getText()))){
                new Alert(Alert.AlertType.CONFIRMATION,"Customer Saved!").show();
                clearFields();
            }

        }catch (ClassNotFoundException| SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private void clearFields() {
        txtEmail.clear();
        txtName.clear();
        txtContact.clear();
        txtSalary.clear();
    }

    public void SaveCustomerOnAction(ActionEvent actionEvent) {
    }

    public void ManageLoyaltyCardsOnAction(ActionEvent actionEvent) {
    }
}
