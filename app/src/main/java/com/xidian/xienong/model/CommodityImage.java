package com.xidian.xienong.model;

import java.io.Serializable;

/**
 * Created by koumiaojuan on 2017/7/5.
 */

public class CommodityImage implements Serializable{
    private String commodityImgUrl;

    public String getCommodityImgUrl() {
        return commodityImgUrl;
    }

    public void setCommodityImgUrl(String commodityImgUrl) {
        this.commodityImgUrl = commodityImgUrl;
    }
}
