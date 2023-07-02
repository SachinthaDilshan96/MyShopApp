package com.devstack.pos.bo.custom;

import com.devstack.pos.bo.SuperBo;
import com.devstack.pos.dto.ProductDetailDto;

import java.sql.SQLException;
import java.util.List;

public interface ProductDetailBo extends SuperBo {
    public boolean saveProductDetail(ProductDetailDto dto) throws SQLException, ClassNotFoundException;
    public List<ProductDetailDto> findAllProductDetails(int productCode) throws SQLException, ClassNotFoundException;

}
