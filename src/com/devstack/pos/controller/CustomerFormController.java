package com.devstack.pos.controller;

import com.devstack.pos.dao.DatabaseAccessCode;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.view.tm.CustomerTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class CustomerFormController {


    public TextField txtEmail;
    public TextField txtName;
    public TextField txtContact;
    public TextField txtSalary;
    public TextField txtSearch;
    public TableView<CustomerTm> tblCustomer;
    public TableColumn colNumber;
    public TableColumn colEMail;
    public TableColumn colName;
    public TableColumn colContact;
    public TableColumn colSalary;
    public TableColumn colOperator;
    public AnchorPane context;
    public Button btnBackToHome;
    public Button btnNewCustomer;
    public Button btnSaveCustomer;
    public Button btnManageLoyaltyCards;

    private String searchText = "";
    public void initialize() throws SQLException, ClassNotFoundException {
        colNumber.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEMail.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOperator.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));

        loadAllCustomers(searchText);
        tblCustomer.getSelectionModel().selectedItemProperty()
                .addListener((observable,oldValue,newValue)->{
                    if (newValue!=null){
                        setData(newValue);
                    }
                });
    }

    private void setData(CustomerTm newValue) {
        txtEmail.setEditable(false);
        btnSaveCustomer.setText("Update Customer");
        txtEmail.setText(newValue.getEmail());
        txtName.setText(newValue.getName());
        txtSalary.setText(String.valueOf(newValue.getSalary()));
        txtContact.setText(newValue.getContact());

    }

    private void loadAllCustomers(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<CustomerTm> observableList = FXCollections.observableArrayList();
        int counter = 1;
        for(CustomerDto dto:DatabaseAccessCode.searchCustomers(searchText)){
            CustomerTm tm = new CustomerTm(counter++,dto.getEmail(), dto.getName(), dto.getContact(), dto.getSalary(),new Button("Delete"));
            observableList.add(tm);
        }
        tblCustomer.setItems(observableList);
    }

    public void BackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void NewCustomerOnAction(ActionEvent actionEvent) {
        txtEmail.setEditable(true);
        btnSaveCustomer.setText("Save Customer");
        clearFields();
    }

    private void clearFields() {
        txtEmail.clear();
        txtName.clear();
        txtContact.clear();
        txtSalary.clear();
    }

    public void SaveCustomerOnAction(ActionEvent actionEvent) {
     /*   try{
            if(DatabaseAccessCode.updateCustomer(txtEmail.getText(),txtName.getText(),txtContact.getText(),Double.parseDouble(txtSalary.getText()))){
                new Alert(Alert.AlertType.CONFIRMATION,"Customer Updated!").show();
                clearFields();
                loadAllCustomers(searchText);
            }

        }catch (ClassNotFoundException| SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }*/
        try{
            if (btnSaveCustomer.getText().equals("Save Customer")){
                if(DatabaseAccessCode.createCustomer(txtEmail.getText(),txtName.getText(),txtContact.getText(),Double.parseDouble(txtSalary.getText()))){
                    new Alert(Alert.AlertType.CONFIRMATION,"Customer Saved!").show();
                    clearFields();
                    loadAllCustomers(searchText);
                }
            }else {
                if(DatabaseAccessCode.updateCustomer(txtEmail.getText(),txtName.getText(),txtContact.getText(),Double.parseDouble(txtSalary.getText()))){
                    new Alert(Alert.AlertType.CONFIRMATION,"Customer Updated!").show();
                    clearFields();
                    loadAllCustomers(searchText);
                    txtEmail.setEditable(false);
                    btnSaveCustomer.setText("Save Customer");
                }
            }
        }catch (ClassNotFoundException| SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    public void ManageLoyaltyCardsOnAction(ActionEvent actionEvent) {
    }

    private void setUi(String url) throws IOException {
        Stage stage = (Stage)context.getScene().getWindow();
        stage.centerOnScreen();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/"+url+".fxml")))
        );
    }

}
