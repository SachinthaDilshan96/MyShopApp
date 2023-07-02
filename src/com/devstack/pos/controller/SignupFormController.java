package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.UserBo;
import com.devstack.pos.bo.custom.impl.UserBoImpl;
import com.devstack.pos.dto.UserDto;
import com.devstack.pos.enums.BoType;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SignupFormController {
    public AnchorPane context;
    public TextField txtEmail;
    public PasswordField txtPassword;
    UserBo bo = BoFactory.getInstance().getBo(BoType.USER);
    public void SignupOnAction(ActionEvent actionEvent) {

       try {
           if (bo.saveUser(new UserDto(txtEmail.getText(),txtPassword.getText()))){
               new Alert(Alert.AlertType.CONFIRMATION,"User Saved").show();
               clearFields();
           }
           else {
               new Alert(Alert.AlertType.WARNING,"Try Again").show();
           }
       }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
       }

    }

    public void LoginOnAction(ActionEvent actionEvent) throws IOException {
        setUi("LoginForm");
    }

    private void setUi(String url) throws IOException {
        Stage stage = (Stage)context.getScene().getWindow();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/"+url+".fxml")))
        );
        stage.centerOnScreen();
    }
    public void clearFields(){
        txtEmail.clear();
        txtPassword.clear();
    }
}
