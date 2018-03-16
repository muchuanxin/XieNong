package com.xidian.xienong.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/29.
 */

public class Brand implements Serializable{
    private String brandId;
    private String brandName;
    private String picUrl;
    private List<SecondContent> contents = new ArrayList<>();

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public List<SecondContent> getContents() {
        return contents;
    }

    public void setContents(List<SecondContent> contents) {
        this.contents = contents;
    }
}
