package com.xidian.xienong.model;

/**
 * Created by MMY on 2017/7/12.
 */

public class MallOrderBottom extends MallOrderBean {
    private String transport_money;
    private String comm_number;
    private String total_money;
    private String status;
    private boolean isEvalued;
    private String evaluate_mark;

    public MallOrderBottom(int item_type, String transport_money, String comm_number, String total_money, String status, boolean isEvalued) {
        super(item_type);
        this.transport_money = transport_money;
        this.comm_number = comm_number;
        this.total_money = total_money;
        this.status = status;
        this.isEvalued = isEvalued;
    }

    public MallOrderBottom(int item_type, String transport_money, String comm_number, String total_money, String status, String evaluate_mark) {
        super(item_type);
        this.transport_money = transport_money;
        this.comm_number = comm_number;
        this.total_money = total_money;
        this.status = status;
        this.evaluate_mark = evaluate_mark;
    }

    public String getTransport_money() {
        return transport_money;
    }

    public void setTransport_money(String transport_money) {
        this.transport_money = transport_money;
    }

    public String getComm_number() {
        return comm_number;
    }

    public void setComm_number(String comm_number) {
        this.comm_number = comm_number;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isEvalued() {
        return isEvalued;
    }

    public void setEvalued(boolean evalued) {
        isEvalued = evalued;
    }
}
