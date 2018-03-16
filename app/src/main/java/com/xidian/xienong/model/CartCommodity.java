package com.xidian.xienong.model;

import java.io.Serializable;

/**
 * Created by xinye on 2017/7/12.
 */

public class CartCommodity implements Serializable{
    private String commodity_id;
    private String commodity_name;
    private String unit;
    private String weight;
    private String commodity_variety;//品种
    private String specification;//规格
    private String producing_area;
    private int current_quantities;//库存
    private double origin_price;
    private double discount;
    private String shopping_cart_id;
    private int added_total_quantities;//服务器传过来的数据库中的商品数量
    private int update_quantities;//修改中的商品数量，可能与added_total_quantities相等或不等
    private String commodity_pic_url;
    private double trans_expense;

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }
    public int getUpdate_quantities() {
        return update_quantities;
    }

    public void setUpdate_quantities(int update_quantities) {
        this.update_quantities = update_quantities;
    }

    public String getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(String commodity_id) {
        this.commodity_id = commodity_id;
    }

    public String getCommodity_name() {
        return commodity_name;
    }

    public void setCommodity_name(String commodity_name) {
        this.commodity_name = commodity_name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCommodity_variety() {
        return commodity_variety;
    }

    public void setCommodity_variety(String commodity_variety) {
        this.commodity_variety = commodity_variety;
    }

    public String getProducing_area() {
        return producing_area;
    }

    public void setProducing_area(String producing_area) {
        this.producing_area = producing_area;
    }

    public String getShopping_cart_id() {
        return shopping_cart_id;
    }

    public void setShopping_cart_id(String shopping_cart_id) {
        this.shopping_cart_id = shopping_cart_id;
    }

    public String getCommodity_pic_url() {
        return commodity_pic_url;
    }

    public void setCommodity_pic_url(String commodity_pic_url) {
        this.commodity_pic_url = commodity_pic_url;
    }

    public int getCurrent_quantities() {
        return current_quantities;
    }

    public void setCurrent_quantities(int current_quantities) {
        this.current_quantities = current_quantities;
    }

    public double getOrigin_price() {
        return origin_price;
    }

    public void setOrigin_price(double origin_price) {
        this.origin_price = origin_price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getAdded_total_quantities() {
        return added_total_quantities;
    }

    public void setAdded_total_quantities(int added_total_quantities) {
        this.added_total_quantities = added_total_quantities;
    }

    public double getTrans_expense() {
        return trans_expense;
    }

    public void setTrans_expense(double trans_expense) {
        this.trans_expense = trans_expense;
    }
}
