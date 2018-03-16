package com.xidian.xienong.model;

import java.io.Serializable;

/**
 * Created by koumiaojuan on 2017/7/5.
 */

public class ProduceParameter implements Serializable{
    private String warranty;
    private String netWeight;
    private String packagingManner;
    private String producingArea;

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }

    public String getPackagingManner() {
        return packagingManner;
    }

    public void setPackagingManner(String packagingManner) {
        this.packagingManner = packagingManner;
    }

    public String getProducingArea() {
        return producingArea;
    }

    public void setProducingArea(String producingArea) {
        this.producingArea = producingArea;
    }
}
