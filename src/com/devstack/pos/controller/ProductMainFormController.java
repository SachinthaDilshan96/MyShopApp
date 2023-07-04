package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.ProductBo;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.bo.custom.impl.ProductBoImpl;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.dto.ProductDetailDto;
import com.devstack.pos.dto.ProductDto;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.view.tm.ProductDetailTm;
import com.devstack.pos.view.tm.ProductTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    public TableView tblProducts;
    public TableColumn colProductID;
    public TableColumn colProductDescription;
    public TableColumn colProductShowMore;
    public TableColumn colProductDelete;
    public TextArea txtSelectedItemDesc;
    public TableView<ProductDetailTm> tblDetail;
    public TableColumn colProdDetailId;
    public TableColumn colProdDetailQty;
    public TableColumn colProdDetailSeelingPrice;
    public TableColumn colProdDetailBuyingPrice;
    public TableColumn colProdDetailDiscAvailability;
    public TableColumn colProdDetailShowPrice;
    public TableColumn colProdDetailDelete;
    private String searchText= "";

    ProductBo bo = BoFactory.getInstance().getBo(BoType.PRODUCT);
    ProductDetailBo detailBo = BoFactory.getInstance().getBo(BoType.PRODUCT_DETAIL);

    public void initialize() throws SQLException, ClassNotFoundException {
        colProductID.setCellValueFactory(new PropertyValueFactory<>("code"));
        colProductDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colProductShowMore.setCellValueFactory(new PropertyValueFactory<>("showMore"));
        colProductDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));

        colProdDetailId.setCellValueFactory(new PropertyValueFactory<>("code"));
        colProdDetailQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colProdDetailSeelingPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colProdDetailBuyingPrice.setCellValueFactory(new PropertyValueFactory<>("buyingPrice"));
        colProdDetailDiscAvailability.setCellValueFactory(new PropertyValueFactory<>("discountAvailability"));
        colProdDetailShowPrice.setCellValueFactory(new PropertyValueFactory<>("showPrice"));
        colProdDetailDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));


        loadProductId();
        loadAllProducts(searchText);

        tblProducts.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->{
                    setData((ProductTm) newValue);
                }
        );
        tblDetail.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->{
                    try {
                        loadExternalUI(true,newValue);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    private void setData(ProductTm newValue) {
        txtSelectedProductCode.setText(String.valueOf(newValue.getCode()));
        txtSelectedItemDesc.setText(newValue.getDescription());
        btnNewBatch.setDisable(false);
        try {
            loadBatchData(newValue.getCode());
        }catch (SQLException|ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    private void loadProductId()  {
       try {
           txtProductCode.setText(String.valueOf(bo.getLastProductId()));
       }catch (SQLException | ClassNotFoundException e){
           throw new RuntimeException(e);
       }
    }

    public void NewBatchOnAction(ActionEvent actionEvent) throws IOException {
       loadExternalUI( false,null);
        /* if (!txtSelectedProductCode.getText().isEmpty()){
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/NewBatchForm.fxml"));
            Parent parent = fxmlLoader.load();
            NewBatchFormController controller = fxmlLoader.getController();
            controller.setDetail(Integer.parseInt(txtSelectedProductCode.getText()),txtSelectedItemDesc.getText(),stage);
            stage.setScene(new Scene(parent));
            stage.setTitle("New Batch");
            stage.centerOnScreen();
            stage.show();
        }else {
            new Alert(Alert.AlertType.WARNING,"Please select a valid one");
        }*/
    }
    private void loadExternalUI(boolean state,ProductDetailTm tm) throws IOException {
        if (!txtSelectedProductCode.getText().isEmpty()){
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/NewBatchForm.fxml"));
            Parent parent = fxmlLoader.load();
            NewBatchFormController controller = fxmlLoader.getController();
            controller.setDetail(Integer.parseInt(txtSelectedProductCode.getText()),txtSelectedItemDesc.getText(),stage,state,tm);
            stage.setScene(new Scene(parent));
            stage.setTitle("New Batch");
            stage.centerOnScreen();
            stage.show();
        }else {
            new Alert(Alert.AlertType.WARNING,"Please select a valid one");
        }
    }

    public void BackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void NewProductOnAction(ActionEvent actionEvent) {

    }

    private void loadAllProducts(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<ProductTm> tms = FXCollections.observableArrayList();
        for (ProductDto dto:bo.findAllProducts()){
            Button showMore  = new Button("Show More");
            Button delete  = new Button("Delete");
            ProductTm tm = new ProductTm(dto.getCode(),dto.getDescription(),showMore,delete);
            tms.add(tm);
        }
        tblProducts.setItems(tms);
    }

    public void SaveProductOnAction(ActionEvent actionEvent) {
        try{
            if (btnSaveProduct.getText().equals("Save Product")){
                if(bo.saveProduct(new ProductDto(Integer.parseInt(txtProductCode.getText()),txtDescription.getText()))){
                    new Alert(Alert.AlertType.CONFIRMATION,"Product Saved!").show();
                    clearFields();
                    loadAllProducts(searchText);
                }
            }else {
                if(bo.updateProduct(new ProductDto(Integer.parseInt(txtProductCode.getText()),txtDescription.getText()))){
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

    private void loadBatchData(int code) throws SQLException, ClassNotFoundException {
        ObservableList<ProductDetailTm> obList = FXCollections.observableArrayList();
        for (ProductDetailDto p:detailBo.findAllProductDetails(code)){
            Button btn =  new Button("Delete");
            ProductDetailTm tm = new ProductDetailTm(
              p.getCode(),
              p.getQtyOnHand(),
              p.getSellingPrice(),
              p.getBuyingPrice(),
              p.getdiscountAvailabilty(),
              p.getShowPrice(),
              btn
            );
            obList.add(tm);
        }
        tblDetail.setItems(obList);
    }
}
