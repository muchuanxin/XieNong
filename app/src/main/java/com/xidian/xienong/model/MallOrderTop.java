package com.xidian.xienong.model;

/**
 * Created by MMY on 2017/7/12.
 */

public class MallOrderTop  extends MallOrderBean {

    private String mall_order_id;
    private String mall_order_number;//订单编号
    private String mall_commodity_money;
    private String mall_interal_money;
    private String transportation_expanse;
    private String total_money;
    private String order_generated_time;
    private String order_status;
    private String order_receiver;
    private String order_receiver_addr;
    private String order_receiver_tel;


    public MallOrderTop(int item_type, String mall_order_id, String mall_order_number, String mall_commodity_money, String mall_interal_money, String transportation_expanse, String total_money, String order_generated_time, String order_status, String order_receiver, String order_receiver_addr, String order_receiver_tel) {
        super(item_type);
        this.mall_order_id = mall_order_id;
        this.mall_order_number = mall_order_number;
        this.mall_commodity_money = mall_commodity_money;
        this.mall_interal_money = mall_interal_money;
        this.transportation_expanse = transportation_expanse;
        this.total_money = total_money;
        this.order_generated_time = order_generated_time;
        this.order_status = order_status;
        this.order_receiver = order_receiver;
        this.order_receiver_addr = order_receiver_addr;
        this.order_receiver_tel = order_receiver_tel;

    }
    public MallOrderTop(int item_type,String order_generated_time,String order_status){
        super(item_type);
        this.order_generated_time = order_generated_time;
        this.order_status = order_status;
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

    public String getOrder_receiver() {
        return order_receiver;
    }

    public void setOrder_receiver(String order_receiver) {
        this.order_receiver = order_receiver;
    }

    public String getOrder_receiver_addr() {
        return order_receiver_addr;
    }

    public void setOrder_receiver_addr(String order_receiver_addr) {
        this.order_receiver_addr = order_receiver_addr;
    }

    public String getOrder_receiver_tel() {
        return order_receiver_tel;
    }

    public void setOrder_receiver_tel(String order_receiver_tel) {
        this.order_receiver_tel = order_receiver_tel;
    }
}
