package com.xidian.xienong.model;

import java.io.Serializable;

/**
 * Created by koumiaojuan on 2017/6/16.
 */

public class Consult implements Serializable {
    private String imageUrl;
    private String title;
    private String subTitle;
    private String contentUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}
