package com.devstack.pos.dao.custom.impl;

import com.devstack.pos.dao.CrudUtil;
import com.devstack.pos.dao.custom.UserDao;
import com.devstack.pos.db.DbConnection;
import com.devstack.pos.entity.User;
import com.devstack.pos.util.PasswordManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public boolean save(User user) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into user values (?,?)",user.getEmail(),user.getPassword());
    }

    @Override
    public boolean update(User user) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update user set password = ? where email = ?",PasswordManager.encryptPassword(user.getPassword()),user.getEmail());
    }

    @Override
    public boolean delete(String email) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("delete from user where email = ?",email);
    }

    @Override
    public User find(String email) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute( "select * from user where email = ?",email);
        if (resultSet.next()){
            return new User(resultSet.getString(1),resultSet.getString(2));
        }
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("select * from user");
        List<User> userList = new ArrayList<>();
        if (resultSet.next()){
            userList.add(new User(resultSet.getString(1),resultSet.getString(2)));
        }
        return userList;
    }
}
