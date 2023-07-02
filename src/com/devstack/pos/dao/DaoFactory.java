package com.devstack.pos.dao;

import com.devstack.pos.dao.custom.impl.ProductDaoImpl;
import com.devstack.pos.dao.custom.impl.ProductDetailDaoImpl;
import com.devstack.pos.dao.custom.impl.UserDaoImpl;
import com.devstack.pos.enums.DaoType;

public class DaoFactory {
    private static DaoFactory daoFactory;

    public static DaoFactory getInstance(){
        return (daoFactory==null)?daoFactory=new DaoFactory():daoFactory;
    }

    public <T>T getDao(DaoType daoType){
        switch (daoType){
            case USER:
                return (T) new UserDaoImpl();
            case PRODUCT:
                return (T) new ProductDaoImpl();
            case CUSTOMER:
                return (T) new ProductDaoImpl();
            case PRODUCT_DETAIL:
                return (T) new ProductDetailDaoImpl();
            default:
                return null;
        }
    }

}