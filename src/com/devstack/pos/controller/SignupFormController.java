package com.devstack.pos.controller;

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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignupFormController {
    public AnchorPane context;
    public TextField txtEmail;
    public PasswordField txtPassword;

    public void SignupOnAction(ActionEvent actionEvent) {

       try {
           Class.forName("com.mysql.cj.jdbc.Driver");
           Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myshop_db","root","Liyanage36@");
           String sql = "insert into user values (?,?)";
           PreparedStatement preparedStatement = connection.prepareStatement(sql);
           preparedStatement.setString(1,txtEmail.getText());
           preparedStatement.setString(2, PasswordManager.encryptPassword(txtPassword.getText()));

           if (preparedStatement.executeUpdate()>0){
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
