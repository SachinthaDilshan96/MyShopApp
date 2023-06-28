package com.devstack.pos.dao;

import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.dto.UserDto;
import com.devstack.pos.util.PasswordManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccessCode {
    // user management
    public static boolean createUser(String email, String password) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myshop_db","root","Liyanage36@");
        String sql = "insert into user values (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,email);
        preparedStatement.setString(2, PasswordManager.encryptPassword(password));
        return preparedStatement.executeUpdate()>0;
    }

    public static UserDto findUser(String email) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myshop_db","root","Liyanage36@");
        String sql = "select * from user where email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,email);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return new UserDto(resultSet.getString(1),resultSet.getString(2));
        }
        return null;
    }
    //customer management
    public static boolean createCustomer(String email, String name, String contact, double salary) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myshop_db","root","Liyanage36@");
        String sql = "insert into customer values (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,email);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, contact);
        preparedStatement.setDouble(4, salary);
        return preparedStatement.executeUpdate()>0;

    }

    public static boolean updateCustomer(String email, String name, String contact, double salary) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myshop_db","root","Liyanage36@");
        String sql = "update customer set name=?, contact=?,salary=? where email=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,name);
        preparedStatement.setString(2, contact);
        preparedStatement.setDouble(3, salary);
        preparedStatement.setString(4, email);
        return preparedStatement.executeUpdate()>0;

    }
    public static CustomerDto findCustomer(String email) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myshop_db","root","Liyanage36@");
        String sql = "select * from customer where email=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,email);
        ResultSet resultSet= preparedStatement.executeQuery();
        if (resultSet.next()){
            return new CustomerDto(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getDouble(4));
        }
        return null;
    }
    public static boolean deleteCustomer(String email) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myshop_db","root","Liyanage36@");
        String sql = "delete from customer where email=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,email);
        return preparedStatement.executeUpdate()>0;
    }
    public static List<CustomerDto> findAllCustomers() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myshop_db","root","Liyanage36@");
        String sql = "select * from customer";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet= preparedStatement.executeQuery();
        List<CustomerDto> customerDtos = new ArrayList<>();
        while (resultSet.next()){
            customerDtos.add(
                    new CustomerDto(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getDouble(4)));
        }
        return customerDtos;
    }

    public static List<CustomerDto> searchCustomers(String searchText) throws ClassNotFoundException, SQLException {
        searchText =  "%"+searchText+"%";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myshop_db","root","Liyanage36@");
        String sql = "select * from customer where  email like ? || name like ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,searchText);
        preparedStatement.setString(2,searchText);
        ResultSet resultSet= preparedStatement.executeQuery();
        List<CustomerDto> customerDtos = new ArrayList<>();
        while (resultSet.next()){
            customerDtos.add(
                    new CustomerDto(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getDouble(4)));
        }
        return customerDtos;
    }


}
