package com.devstack.pos.controller;

import com.devstack.pos.dao.DatabaseAccessCode;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ProductMainFormController {
    public TextField txtSelectedProductCode;
    public Button btnNewBatch;
    public TextField txtProductCode;
    public Button btnBackToHome;
    public Button btnNewProduct;
    public TextArea txtDescription;
    public Button btnSaveProduct;
    public TextField txtSearch;
    public AnchorPane context;
    private String searchText= "";
    public void initialize(){
        loadProductId();
    }

    private void loadProductId()  {
       try {
           txtProductCode.setText(String.valueOf(new DatabaseAccessCode().getLastProductId()));
       }catch (SQLException | ClassNotFoundException e){
           throw new RuntimeException(e);
       }
    }

    public void NewBatchOnAction(ActionEvent actionEvent) {
    }

    public void BackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void NewProductOnAction(ActionEvent actionEvent) {

    }

    private void loadAllProducts(String searchText) {
    }

    public void SaveProductOnAction(ActionEvent actionEvent) {
        try{
            if (btnSaveProduct.getText().equals("Save Product")){
                if(new DatabaseAccessCode().saveProduct(Integer.parseInt(txtProductCode.getText()),txtDescription.getText())){
                    new Alert(Alert.AlertType.CONFIRMATION,"Product Saved!").show();
                    clearFields();
                    loadAllProducts(searchText);
                }
            }else {
                if(new DatabaseAccessCode().updateProduct(Integer.parseInt(txtProductCode.getText()),txtDescription.getText())){
                    new Alert(Alert.AlertType.CONFIRMATION,"Product Updated!").show();
                    clearFields();
                    loadAllProducts(searchText);
                    txtProductCode.setEditable(false);
                    btnSaveProduct.setText("Save Product");
                }
            }
        }catch (ClassNotFoundException| SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private void setUi(String url) throws IOException {
        Stage stage = (Stage)context.getScene().getWindow();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/"+url+".fxml")))
        );
        stage.centerOnScreen();
    }

    private void clearFields() {
        txtProductCode.clear();
        txtDescription.clear();
        loadProductId();
    }
}
