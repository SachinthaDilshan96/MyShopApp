package com.devstack.pos.dao.custom.impl;

import com.devstack.pos.dao.CrudUtil;
import com.devstack.pos.dao.custom.ProductDetailDao;
import com.devstack.pos.entity.ProductDetail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailDaoImpl implements ProductDetailDao {
    @Override
    public boolean save(ProductDetail productDetail) throws SQLException, ClassNotFoundException {
      return   CrudUtil.execute(
                "insert into product_detail values(?,?,?,?,?,?,?,?)",
                productDetail.getProductCode(),
                productDetail.getBarcode(),
                productDetail.getQtyOnHand(),
                productDetail.getSellingPrice(),
                productDetail.getdiscountAvailabilty(),
                productDetail.getShowPrice(),
                productDetail.getProductCode(),
                productDetail.getBuyingPrice()
        );
    }

    @Override
    public boolean update(ProductDetail productDetail) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public ProductDetail find(String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<ProductDetail> findAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<ProductDetail> findAllProductDetails(int productCode) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("select * from product_detail where product_code=?",productCode);
        List<ProductDetail> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(new ProductDetail(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4),
                    resultSet.getDouble(6),
                    resultSet.getDouble(8),
                    resultSet.getInt(7),
                    resultSet.getBoolean(5)
            ));
        }
        return list;
    }
}
