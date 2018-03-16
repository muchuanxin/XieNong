package com.xidian.xienong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xinye on 2017/7/13.
 */

public class ListCartCommodity implements Serializable {
    private List<CartCommodity> cartCommodityList;

    public List<CartCommodity> getCartCommodityList() {
        return cartCommodityList;
    }

    public void setCartCommodityList(List<CartCommodity> cartCommodityList) {
        this.cartCommodityList = cartCommodityList;
    }
}
