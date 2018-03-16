package com.xidian.xienong.model;

import android.support.annotation.NonNull;

import com.xidian.xienong.util.Time;

import java.io.Serializable;

/**
 * Created by MMY on 2017/7/12.
 */

public class MallOrderBean implements Serializable,Comparable<MallOrderBean> {

    /*private String mall_order_id;
    private String mall_order_number;
    private String mall_commodity_money;
    private String mall_interal_money;
    private String transportation_expanse;
    private String total_money;
    private String order_generated_time;
    private String order_status;
    private boolean isEvalued;
    private List<CommodityInOrder> commodities;*/
    private int itemType;
    private String order_generated_time;
    private String order_status;
    private String commodityId;
    private String  commodityName;//名称
    private String  price;//价格
    private String  specification;//规格
    private String  image_url;
    private String  buy_number;
    private String transport_money;
    private boolean isEvalauted;

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public MallOrderBean(int item_type) {
        this.itemType = item_type;
    }

   /* public String getMall_order_id() {
        return mall_order_id;
    }

    public void setMall_order_id(String mall_order_id) {
        this.mall_order_id = mall_order_id;
    }

    public String getMall_order_number() {
        return mall_order_number;
    }

    public void setMall_order_number(String mall_order_number) {
        this.mall_order_number = mall_order_number;
    }

    public String getMall_commodity_money() {
        return mall_commodity_money;
    }

    public void setMall_commodity_money(String mall_commodity_money) {
        this.mall_commodity_money = mall_commodity_money;
    }

    public String getMall_interal_money() {
        return mall_interal_money;
    }

    public void setMall_interal_money(String mall_interal_money) {
        this.mall_interal_money = mall_interal_money;
    }

    public String getTransportation_expanse() {
        return transportation_expanse;
    }

    public void setTransportation_expanse(String transportation_expanse) {
        this.transportation_expanse = transportation_expanse;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }*/

    public String getOrder_generated_time() {
       return order_generated_time;
    }

   public void setOrder_generated_time(String order_generated_time) {
       this.order_generated_time = order_generated_time;
   }

   /* public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public boolean isEvalued() {
        return isEvalued;
    }

    public void setEvalued(boolean evalued) {
        isEvalued = evalued;
    }

    public List<CommodityInOrder> getCommodities() {
        return commodities;
    }

    public void setCommodities(List<CommodityInOrder> commodities) {
        this.commodities = commodities;
    }*/

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int compareTo(MallOrderBean o) {
        if(Time.compare_date(this.getOrder_generated_time(),o.getOrder_generated_time()) > 0){
            return -1;
        }else if(Time.compare_date(this.getOrder_generated_time(),o.getOrder_generated_time()) < 0){
            return 1;
        }else{
            return 0;
        }

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

    public String getTransport_money() {
        return transport_money;
    }

    public void setTransport_money(String transport_money) {
        this.transport_money = transport_money;
    }
}
