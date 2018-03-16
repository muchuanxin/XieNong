package com.xidian.xienong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/7/28.
 */

public class MallsOrderBean  implements Serializable{

    private String mall_order_id;
    private String mall_order_number;
    private String mall_commodity_money;
    private String mall_interal_money;
    private String transportation_expanse;
    private String total_money;
    private String order_generated_time;
    private String order_status;
    private String evaluated_remark;
    private List<CommodityInOrder> commodities;
    private int itemType;
    private List<MallOrderBean> mallTypeOrders;

    public List<MallOrderBean> getMallTypeOrders() {
        return mallTypeOrders;
    }

    public void setMallTypeOrders(List<MallOrderBean> mallTypeOrders) {
        this.mallTypeOrders = mallTypeOrders;
    }



    public String getMall_order_id() {
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
    }

    public String getOrder_generated_time() {
        return order_generated_time;
    }

    public void setOrder_generated_time(String order_generated_time) {
        this.order_generated_time = order_generated_time;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getEvaluated_remark() {
        return evaluated_remark;
    }

    public void setEvaluated_remark(String evaluated_remark) {
        this.evaluated_remark = evaluated_remark;
    }

    public List<CommodityInOrder> getCommodities() {
        return commodities;
    }

    public void setCommodities(List<CommodityInOrder> commodities) {
        this.commodities = commodities;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
