package com.devstack.pos.dao.custom.impl;

import com.devstack.pos.dao.CrudUtil;
import com.devstack.pos.dao.custom.CustomerDao;
import com.devstack.pos.entity.Customer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    @Override
    public List<Customer> searchCustomers(String searchText) throws SQLException, ClassNotFoundException {
        searchText =  "%"+searchText+"%";
        ResultSet resultSet= CrudUtil.execute("select * from customer where  email like ? || name like ?",searchText,searchText);
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

    @Override
    public boolean save(Customer customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into customer values (?,?,?,?)",customer.getEmail(),customer.getName(),customer.getContact(),customer.getSalary());
    }

    @Override
    public boolean update(Customer customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update customer set name=?, contact=?,salary=? where email=?",customer.getName(),customer.getContact(),customer.getSalary(),customer.getEmail());
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
       return CrudUtil.execute("delete from customer where email=?",s);
    }

    @Override
    public Customer find(String s) throws SQLException, ClassNotFoundException {
        ResultSet resultSet= CrudUtil.execute("select * from customer where email=?",s);
        if (resultSet.next()){
            return new Customer(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getDouble(4));
        }
        return null;
    }

    @Override
    public List<Customer> findAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet= CrudUtil.execute("select * from customer");
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
}
