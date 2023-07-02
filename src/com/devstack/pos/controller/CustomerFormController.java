package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.CustomerBo;
import com.devstack.pos.bo.custom.impl.CustomerBoImpl;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.enums.BoType;
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
import java.util.Optional;

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

    CustomerBo bo = BoFactory.getInstance().getBo(BoType.CUSTOMER);

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
        txtSearch.textProperty().addListener(((observable, oldValue, newValue) -> {
            searchText = newValue;
            try{
                loadAllCustomers(searchText);
            }catch (SQLException|ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }));
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
        for(CustomerDto dto:searchText.length()>0?bo.searchCustomers(searchText):bo.findAllCustomers()){
            Button btn =new Button("Delete");
            CustomerTm tm = new CustomerTm(counter++,dto.getEmail(), dto.getName(), dto.getContact(), dto.getSalary(),btn);
            observableList.add(tm);
            btn.setOnAction((e)->{
                try{
                    Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are you sure?",ButtonType.OK,ButtonType.NO);
                    Optional<ButtonType> selectedButtonType = alert.showAndWait();
                    if (selectedButtonType.get().equals(ButtonType.YES)){
                        if (bo.deleteCustomer(dto.getEmail())){
                            new Alert(Alert.AlertType.CONFIRMATION,"Customer Deleted!").show();
                            loadAllCustomers(searchText);
                        }else {
                            new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                        }
                    }
                }catch (SQLException|ClassNotFoundException er){
                    er.printStackTrace();
                    new Alert(Alert.AlertType.ERROR,er.getMessage()).show();
                }
            });
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
        try{
            if (btnSaveCustomer.getText().equals("Save Customer")){
                if(bo.saveCustomer(new CustomerDto(txtEmail.getText(),txtName.getText(),txtContact.getText(),Double.parseDouble(txtSalary.getText())))){
                    new Alert(Alert.AlertType.CONFIRMATION,"Customer Saved!").show();
                    clearFields();
                    loadAllCustomers(searchText);
                }
            }else {
                if(bo.updateCustomer(new CustomerDto(txtEmail.getText(),txtName.getText(),txtContact.getText(),Double.parseDouble(txtSalary.getText())))){
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
