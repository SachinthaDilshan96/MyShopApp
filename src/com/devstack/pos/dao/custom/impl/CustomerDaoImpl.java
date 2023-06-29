package com.devstack.pos.dao.custom.impl;

import com.devstack.pos.dao.custom.CustomerDao;
import com.devstack.pos.db.DbConnection;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.entity.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    @Override
    public boolean saveCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        String sql = "insert into customer values (?,?,?,?)";
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1,customer.getEmail());
        preparedStatement.setString(2, customer.getName());
        preparedStatement.setString(3, customer.getContact());
        preparedStatement.setDouble(4, customer.getSalary());
        return preparedStatement.executeUpdate()>0;
    }

    @Override
    public boolean updateCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        String sql = "update customer set name=?, contact=?,salary=? where email=?";
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, customer.getName());
        preparedStatement.setString(2, customer.getContact());
        preparedStatement.setDouble(3, customer.getSalary());
        preparedStatement.setString(4, customer.getEmail());
        return preparedStatement.executeUpdate()>0;
    }

    @Override
    public boolean deleteCustomer(String email) throws SQLException, ClassNotFoundException {
        String sql = "delete from customer where email=?";
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1,email);
        return preparedStatement.executeUpdate()>0;
    }

    @Override
    public Customer findCustomer(String email) throws SQLException, ClassNotFoundException {
        String sql = "select * from customer where email=?";
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1,email);
        ResultSet resultSet= preparedStatement.executeQuery();
        if (resultSet.next()){
            return new Customer(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getDouble(4));
        }
        return null;
    }

    @Override
    public List<Customer> findAllCustomer() throws SQLException, ClassNotFoundException {
        String sql = "select * from customer";
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet= preparedStatement.executeQuery();
        List<Customer> customerList = new ArrayList<>();
        while (resultSet.next()){
            customerList.add(
                    new Customer(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getDouble(4)));
        }
        return customerList;
    }

    @Override
    public List<Customer> searchCustomers(String searchText) throws SQLException, ClassNotFoundException {
        searchText =  "%"+searchText+"%";
        String sql = "select * from customer where  email like ? || name like ?";
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1,searchText);
        preparedStatement.setString(2,searchText);
        ResultSet resultSet= preparedStatement.executeQuery();
        List<Customer> customerDtos = new ArrayList<>();
        while (resultSet.next()){
            customerDtos.add(
                    new Customer(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getDouble(4)));
        }
        return customerDtos;
    }
}