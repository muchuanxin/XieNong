package com.xidian.xienong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/30.
 */

public class Commodity implements Serializable{
    private String  commodityName;
    private String  price;
    private String  specification;
    private String  commodityUrl;
    private String  producePlace;
    private int imgHeight;
    private String commodityId;
    private String dealVolume;
    private String sale_quantity;
    private String currentCount;
    private String discount;
    private String transportExpense;
    private String commodityDescription;
    private ProduceParameter produceParameter;
    private List<CommodityImage> commodityImageList;
    private List<CommodityComment> commodityCommentsList;
    private String variety;

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public List<CommodityComment> getCommodityCommentsList() {
        return commodityCommentsList;
    }

    public void setCommodityCommentsList(List<CommodityComment> commodityCommentsList) {
        this.commodityCommentsList = commodityCommentsList;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getDealVolume() {
        return dealVolume;
    }

    public void setDealVolume(String dealVolume) {
        this.dealVolume = dealVolume;
    }

    public String getSale_quantity() {
        return sale_quantity;
    }

    public void setSale_quantity(String sale_quantity) {
        this.sale_quantity = sale_quantity;
    }

    public String getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(String currentCount) {
        this.currentCount = currentCount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTransportExpense() {
        return transportExpense;
    }

    public void setTransportExpense(String transportExpense) {
        this.transportExpense = transportExpense;
    }

    public String getCommodityDescription() {
        return commodityDescription;
    }

    public void setCommodityDescription(String commodityDescription) {
        this.commodityDescription = commodityDescription;
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

    public String getCommodityUrl() {
        return commodityUrl;
    }

    public void setCommodityUrl(String commodityUrl) {
        this.commodityUrl = commodityUrl;
    }

    public String getProducePlace() {
        return producePlace;
    }

    public void setProducePlace(String producePlace) {
        this.producePlace = producePlace;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }

    public ProduceParameter getProduceParameter() {
        return produceParameter;
    }

    public void setProduceParameter(ProduceParameter produceParameter) {
        this.produceParameter = produceParameter;
    }

    public List<CommodityImage> getCommodityImageList() {
        return commodityImageList;
    }

    public void setCommodityImageList(List<CommodityImage> commodityImageList) {
        this.commodityImageList = commodityImageList;
    }
}
