package com.devstack.pos.controller;

import com.devstack.pos.dao.DatabaseAccessCode;
import com.devstack.pos.dto.UserDto;
import com.devstack.pos.util.PasswordManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginFormController {
    public AnchorPane context;
    public TextField txtEmail;
    public PasswordField txtPassword;

    public void SigninOnAction(ActionEvent actionEvent) throws IOException {
          try {
              UserDto userDto = DatabaseAccessCode.findUser(txtEmail.getText());
              if (userDto!=null){
                if (PasswordManager.checkPassword(txtPassword.getText(),userDto.getPassword())){
                    setUi("DashboardForm");
                }else {
                    new Alert(Alert.AlertType.WARNING,"Check tha password and try again").show();
                }
            }else {
                new Alert(Alert.AlertType.WARNING,"Email not found").show();
            }
        }catch (ClassNotFoundException | SQLException|IOException  e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
        }
    }

    public void SignupOnAction(ActionEvent actionEvent) throws IOException {
        setUi("SignupForm");
    }
    private void setUi(String url) throws IOException {
        Stage stage = (Stage)context.getScene().getWindow();
        stage.centerOnScreen();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/"+url+".fxml")))
        );
    }
}
