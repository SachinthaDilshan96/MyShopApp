package com.devstack.pos.controller;

import com.beust.ah.A;
import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.CustomerBo;
import com.devstack.pos.bo.custom.LoyaltyCardBo;
import com.devstack.pos.bo.custom.OrderDetailBo;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dto.*;
import com.devstack.pos.entity.Customer;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.enums.CardType;
import com.devstack.pos.util.QrDataGenerator;
import com.devstack.pos.util.UserSessionData;
import com.devstack.pos.view.tm.CartTm;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

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
    private OrderDetailBo orderDetailBo = BoFactory.getInstance().getBo(BoType.ORDER_DETAIL);
    private LoyaltyCardBo loyaltyCardBo = BoFactory.getInstance().getBo(BoType.LOYALTY_CARD);
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
        ArrayList<ItemDetailDto> dtoList = new ArrayList<>();
        double discount = 0;
        for (CartTm tm:tms){
            dtoList.add(new ItemDetailDto(
                    tm.getCode(),
                    tm.getQty(),
                    tm.getDiscount(),
                    tm.getTotalCost()
                    ));
            discount+=tm.getDiscount();
        }
        OrderDetailDto dto = new OrderDetailDto(
                new Random().nextInt(100001),
                new Date(),
                Double.parseDouble(lblBill.getText().split("/=")[0]),
                txtEmail.getText(),
                0,
                UserSessionData.email,
                dtoList
        );
        try {
            if (orderDetailBo.makeOrder(dto)){
                new Alert(Alert.AlertType.CONFIRMATION,"Order Saved");
                clear();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try Again");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
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
        try {
            double salary = Double.parseDouble(txtSalary.getText());
            CardType type = CardType.SILVER;
            if (salary>=100000){
                type = CardType.PLATINUM;
            } else if (salary>=50000) {
                type =CardType.GOLD;
            }else {
                type = CardType.SILVER;
            }

            String uniqueData = QrDataGenerator.generate(25);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(
                    qrCodeWriter.encode(
                            uniqueData, BarcodeFormat.QR_CODE,250,200
                    )
            );

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            javax.imageio.ImageIO.write(bufferedImage,"jpg",baos);
            byte[] arr = baos.toByteArray();

            if (urlNewLoyalty.getText().equals("+ New Loyalty")){
              boolean isLoyaltyCardAssigned =  loyaltyCardBo.saveLoyaltyData(
                        new LoyaltyCardDto(
                                new Random().nextInt(10002),
                                type,
                                Base64.getEncoder().encodeToString(arr),txtEmail.getText())
                );
              if (isLoyaltyCardAssigned){
                  new Alert(Alert.AlertType.CONFIRMATION,"Saved").show();
                  urlNewLoyalty.setText("Show Loyalty Card Info");
              }else {
                  new Alert(Alert.AlertType.WARNING,"Try again Shortly").show();
              }
            }else {

            }
        }catch (SQLException|ClassNotFoundException e){
            new Alert(Alert.AlertType.WARNING,"Try again").show();
            throw new RuntimeException(e);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        double discount = 250;
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
