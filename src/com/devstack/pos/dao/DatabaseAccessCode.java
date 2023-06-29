package com.devstack.pos.dao;

import com.devstack.pos.dao.custom.CustomerDao;
import com.devstack.pos.dao.custom.ProductDao;
import com.devstack.pos.dao.custom.UserDao;
import com.devstack.pos.dao.custom.impl.CustomerDaoImpl;
import com.devstack.pos.dao.custom.impl.ProductDaoImpl;
import com.devstack.pos.dao.custom.impl.UserDaoImpl;
import com.devstack.pos.db.DbConnection;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.dto.UserDto;
import com.devstack.pos.entity.Customer;
import com.devstack.pos.entity.Product;
import com.devstack.pos.entity.User;
import com.devstack.pos.util.PasswordManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccessCode {

    CustomerDao customerDao = new CustomerDaoImpl();
    ProductDao productDao = new ProductDaoImpl();
    UserDao userDao = new UserDaoImpl();

    //user management
    public boolean createUser(String email,String password) throws ClassNotFoundException, SQLException {
        return userDao.saveUser(
                new User(email,password)
        );
    }

    public UserDto findUser(String email) throws ClassNotFoundException, SQLException {
        String sql = "select * from user where email = ?";
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1,email);

        User user = userDao.findUser(email);
        if (user!=null){
            return new UserDto(user.getEmail(),user.getPassword());
        }
        return null;
     }

     //customer management
     public boolean createCustomer(String email,String name,String contact,double salary) throws SQLException, ClassNotFoundException {
        return customerDao.saveCustomer(
                new Customer(email,name,contact,salary)
        );
     }

    public boolean updateCustomer(String email,String name,String contact,double salary) throws SQLException, ClassNotFoundException {
        return customerDao.updateCustomer(new Customer(email,name,contact,salary));
    }


    public boolean deleteCustomer(String email) throws SQLException, ClassNotFoundException {
        return customerDao.deleteCustomer(email);
    }

    public CustomerDto findCustomer(String email) throws SQLException, ClassNotFoundException {
       Customer customer = customerDao.findCustomer(email);
       if (customer!=null){
           return new CustomerDto( customer.getEmail(),customer.getName(),customer.getContact(),customer.getSalary());
       }
       return null;
    }

    public List<CustomerDto> findAllCustomers() throws SQLException, ClassNotFoundException {
        List<CustomerDto> dtos = new ArrayList<>();
        for (Customer c: customerDao.findAllCustomer()){
            dtos.add(new CustomerDto(c.getEmail(),c.getName(),c.getContact(),c.getSalary()));
        }
        return dtos;
    }

    public List<CustomerDto> searchCustomers(String searchText) throws SQLException, ClassNotFoundException {
        searchText =  "%"+searchText+"%";
        List<CustomerDto> dtos = new ArrayList<>();
        for (Customer c: customerDao.searchCustomers(searchText)){
            dtos.add(new CustomerDto(c.getEmail(),c.getName(),c.getContact(),c.getSalary()));
        }
        return dtos;
    }

    //product management
    public int getLastProductId() throws SQLException, ClassNotFoundException {
        return productDao.getLastProductId();
    }

    public boolean saveProduct(int code, String description) throws SQLException, ClassNotFoundException {
        return productDao.saveProduct(new Product(code,description));
    }

    public boolean updateProduct(int code, String description) throws SQLException, ClassNotFoundException {
        return productDao.updateProduct(new Product(code,description));
    }
}
