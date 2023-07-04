package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dao.custom.ProductDetailDao;
import com.devstack.pos.dto.ProductDetailDto;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.util.QrDataGenerator;
import com.devstack.pos.view.tm.ProductDetailTm;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;

public class NewBatchFormController {
    public ImageView imgBarcodeView;
    public TextField txtQty;
    public TextField txtSellingPrice;
    public TextField txtShowPrice;
    public TextField txtBuyingPrice;
    public RadioButton rbYes;
    public TextField txtProductCode;
    public TextArea txtProductDescription;
    public Button btnSaveBatch;
    String uniqueData = null;
    BufferedImage bufferedImage = null;
    Stage stage = null;

    private ProductDetailBo productDetailBo = BoFactory.getInstance().getBo(BoType.PRODUCT_DETAIL);

    public void initialize() throws WriterException {
        setQRCode();
    }

    private void setQRCode() throws WriterException {
        uniqueData = QrDataGenerator.generate(25);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        bufferedImage = MatrixToImageWriter.toBufferedImage(
                (qrCodeWriter.encode(
                        uniqueData, BarcodeFormat.QR_CODE,250,200
                ))
        );
        Image image= SwingFXUtils.toFXImage(bufferedImage,null);
        imgBarcodeView.setImage(image);
    }

    public void setDetail(int code, String description, Stage stage, boolean state, ProductDetailTm tm){
        if (state){
            try {
                ProductDetailDto productDetail =productDetailBo.findProductDetails(tm.getCode());
                if (productDetail!=null){
                    txtQty.setText(String.valueOf(productDetail.getQtyOnHand()));
                    txtBuyingPrice.setText(String.valueOf(productDetail.getBuyingPrice()));
                    txtSellingPrice.setText(String.valueOf(productDetail.getSellingPrice()));
                    txtShowPrice.setText(String.valueOf(productDetail.getShowPrice()));
                    rbYes.setSelected(productDetail.getdiscountAvailabilty());

                    byte[] data = Base64.getDecoder().decode(productDetail.getBarcode());
                    imgBarcodeView.setImage(Image.impl_fromPlatformImage(new ByteArrayInputStream(data)));

                }else {
                    stage.close();
                }
            }catch (SQLException|ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }else {
            try {
                setQRCode();
            }catch (WriterException e){
                throw new RuntimeException(e);
            }
        }
        txtProductCode.setText(String.valueOf(code));
        txtProductDescription.setText(description);
    }
    public void SaveBatchOnACtion(ActionEvent actionEvent) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        javax.imageio.ImageIO.write(bufferedImage,"jpg",baos);
        byte[] arr = baos.toByteArray();
        ProductDetailDto dto = new ProductDetailDto(
                uniqueData,
                Base64.getEncoder().encodeToString(arr),
                Integer.parseInt(txtQty.getText()),
                Double.parseDouble(txtSellingPrice.getText()),
                Double.parseDouble(txtShowPrice.getText()),
                Double.parseDouble(txtBuyingPrice.getText()),
                Integer.parseInt(txtProductCode.getText()),
                rbYes.isSelected()?true:false
        );
        try {
            if (productDetailBo.saveProductDetail(dto)){
                new Alert(Alert.AlertType.CONFIRMATION,"Batch Saved!").show();
                Thread.sleep(3000);
                this.stage.close();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try again!").show();
            }
        }catch (SQLException|ClassNotFoundException|InterruptedException e){
            throw new RuntimeException(e);
        }
    }
    private void loadBatchData(){

    }
}
