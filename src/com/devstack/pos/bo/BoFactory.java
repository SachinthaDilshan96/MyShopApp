package com.devstack.pos.bo;

import com.devstack.pos.bo.custom.impl.CustomerBoImpl;
import com.devstack.pos.bo.custom.impl.ProductBoImpl;
import com.devstack.pos.bo.custom.impl.ProductDetailBoImpl;
import com.devstack.pos.bo.custom.impl.UserBoImpl;
import com.devstack.pos.enums.BoType;
public class BoFactory {
    private static BoFactory boFactory;

    public static BoFactory getInstance(){
        return (boFactory==null)?boFactory=new BoFactory():boFactory;
    }

    public <T>T getBo(BoType boType){
        switch (boType){
            case USER:
                return (T) new UserBoImpl();
            case PRODUCT:
                return (T) new ProductBoImpl();
            case CUSTOMER:
                return (T) new CustomerBoImpl();
            case PRODUCT_DETAIL:
                return (T) new ProductDetailBoImpl();
            default:
                return null;
        }
    }

}
