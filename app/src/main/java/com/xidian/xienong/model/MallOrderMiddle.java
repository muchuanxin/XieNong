package com.xidian.xienong.model;

/**
 * Created by MMY on 2017/7/12.
 */

public class MallOrderMiddle extends MallOrderBean {

    private String commodityId;
    private String  commodityName;//名称
    private String  price;//价格
    private String  specification;//规格
    private String  image_url;
    private String  buy_number;
    private boolean isEvalauted;

    public MallOrderMiddle(int item_type, String commodityId, String commodityName, String price, String specification, String image_url, String buy_number, boolean isEvalauted) {
        super(item_type);
        this.commodityId = commodityId;
        this.commodityName = commodityName;
        this.price = price;
        this.specification = specification;
        this.image_url = image_url;
        this.buy_number = buy_number;
        this.isEvalauted = isEvalauted;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getBuy_number() {
        return buy_number;
    }

    public void setBuy_number(String buy_number) {
        this.buy_number = buy_number;
    }

    public boolean isEvalauted() {
        return isEvalauted;
    }

    public void setEvalauted(boolean evalauted) {
        isEvalauted = evalauted;
    }
}
