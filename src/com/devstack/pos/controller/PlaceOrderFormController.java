package com.devstack.pos.controller;

import com.beust.ah.A;
import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.CustomerBo;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.dto.ProductDetailJoinDto;
import com.devstack.pos.entity.Customer;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.view.tm.CartTm;
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

public class PlaceOrderFormController {

    public AnchorPane context;
    public RadioButton rbManualMode;
    public Button btnBackToHome;
    public TextField txtEmail;
    public TextField txtName;
    public TextField txtContact;
    public TextField txtSalary;
    public Button btnNewProduct;
    public Label lblLoyalCustomer;
    public TextField txtDescription;
    public TextField txtQtyOnHand;
    public TextField txtDiscount;
    public TextField txtShowPrice;
    public TextField txtBuyingPrice;
    public TextField txtSellingPrice;
    public TextField txtQty;
    public TableView tblOrder;
    public TableColumn colId;
    public TableColumn colDescription;
    public TableColumn colSellingPrice;
    public TableColumn colDiscount;
    public TableColumn colShowPrice;
    public TableColumn colQty;
    public TableColumn colTotal;
    public TableColumn colOperation;
    public Label lblBill;
    public Button btnCompleteOrder;
    public Button btnNewCustomer;
    public Hyperlink urlNewLoyalty;
    public TextField txtBarcode;

    CustomerBo bo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    private ProductDetailBo productDetailBo = BoFactory.getInstance().getBo(BoType.PRODUCT_DETAIL);

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colSellingPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colShowPrice.setCellValueFactory(new PropertyValueFactory<>("showPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colOperation.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }
    private void setUi(String url,boolean state) throws IOException {
        Stage stage = null;
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/"+url+".fxml")));
        if (state) {
            stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }else {
            stage = (Stage) context.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
        }
    }

    public void CompleteOrderOnAction(ActionEvent actionEvent) {

    }

    public void NewCustomerOnAction(ActionEvent actionEvent) throws IOException {
            setUi("CustomerForm",true);
    }

    public void BackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm",false);
    }

    public void NewProductOnACtion(ActionEvent actionEvent) throws IOException {
        setUi("ProductMainForm",true);
    }

    public void NewLoyaltyOnAction(ActionEvent actionEvent) {
    }

    public void searchCustomerOnAction(ActionEvent actionEvent) {
        try {
            CustomerDto customer= bo.findCustomer(txtEmail.getText());
           if (customer!=null){
               txtName.setText(String.valueOf(customer.getName()));
               txtSalary.setText(String.valueOf(customer.getSalary()));
               txtContact.setText(customer.getContact());
               fetchLoyaltyCardData(txtEmail.getText());
           }
        }catch (SQLException|ClassNotFoundException e){
            new Alert(Alert.AlertType.WARNING,"Can't find the customer!").show();
        }
    }

    private void fetchLoyaltyCardData(String text) {
        urlNewLoyalty.setText("+ New Loyalty");
        urlNewLoyalty.setVisible(true);

    }

    public void LoadProductOnAction(ActionEvent actionEvent)  {
        ProductDetailJoinDto productDetailJoinDto = null;
      try {
          productDetailJoinDto = productDetailBo.findProductJoinDetail(txtBarcode.getText());
          if (productDetailJoinDto!=null){
              txtDescription.setText(productDetailJoinDto.getDescription());
              txtDiscount.setText(String.valueOf(0));
              txtSellingPrice.setText(String.valueOf(productDetailJoinDto.getDto().getSellingPrice()));
              txtShowPrice.setText(String.valueOf(productDetailJoinDto.getDto().getShowPrice()));
              txtQtyOnHand.setText(String.valueOf(productDetailJoinDto.getDto().getQtyOnHand()));
              txtBuyingPrice.setText(String.valueOf(productDetailJoinDto.getDto().getBuyingPrice()));
              txtQty.requestFocus();
          }else {
              new Alert(Alert.AlertType.WARNING,"Can't find the product").show();
          }
      }catch (SQLException|ClassNotFoundException e){
          new Alert(Alert.AlertType.WARNING,"Can't find the product").show();
          throw new RuntimeException(e);
      }
    }

    ObservableList<CartTm> tms = FXCollections.observableArrayList();
    public void AddToCartOnAction(ActionEvent actionEvent) {
        int qty = Integer.parseInt(txtQty.getText());
        double sellingPrice = Double.parseDouble(txtSellingPrice.getText());
        double totalCost = qty*sellingPrice;
        CartTm selectedCartTm = isExist(txtBarcode.getText());
        if (selectedCartTm!=null){
            selectedCartTm.setQty(qty+selectedCartTm.getQty());
            selectedCartTm.setTotalCost(totalCost+selectedCartTm.getTotalCost());
            tblOrder.refresh();
        }else{
            Button btn = new Button("Remove");
            CartTm tm = new CartTm(
                    txtBarcode.getText(),
                    txtDescription.getText(),
                    Double.parseDouble(txtDiscount.getText()),
                    sellingPrice,
                    Double.parseDouble(txtShowPrice.getText()),
                    qty,
                    totalCost,
                    btn);
            btn.setOnAction(
                    (e)->{
                        tms.remove(tm);
                        tblOrder.refresh();
                        setTotal();
                    }
            );
            tms.add(tm);
            clear();
            tblOrder.setItems(tms);
            setTotal();
        }
    }

    private void clear() {
        txtDescription.clear();
        txtSellingPrice.clear();
        txtDiscount.clear();
        txtShowPrice.clear();
        txtQtyOnHand.clear();
        txtBuyingPrice.clear();
        txtQty.clear();
        txtBarcode.clear();
        txtBarcode.requestFocus();
    }

    private CartTm isExist(String code){
        for (CartTm tm:tms){
            if (tm.getCode().equals(code)){
                return tm;
            }
        }
        return null;
    }

    private void setTotal(){
        double total = 0;
        for (CartTm tm:tms){
            total+=tm.getTotalCost();
        }
        lblBill.setText(total+" /=");
    }
}
