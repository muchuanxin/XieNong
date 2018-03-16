package com.xidian.xienong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/7/6.
 */

public class FirstContent implements Serializable{
    private String firstCategoryId;
    private String firstCategoryName;
    private String firstUrl;
    private List<SecondContent> secondContentList;


    public String getFirstCategoryId() {
        return firstCategoryId;
    }

    public void setFirstCategoryId(String firstCategoryId) {
        this.firstCategoryId = firstCategoryId;
    }

    public String getFirstCategoryName() {
        return firstCategoryName;
    }

    public void setFirstCategoryName(String firstCategoryName) {
        this.firstCategoryName = firstCategoryName;
    }

    public List<SecondContent> getSecondContentList() {
        return secondContentList;
    }

    public void setSecondContentList(List<SecondContent> secondContentList) {
        this.secondContentList = secondContentList;
    }

    public String getFirstUrl() {
        return firstUrl;
    }

    public void setFirstUrl(String firstUrl) {
        this.firstUrl = firstUrl;
    }
}
