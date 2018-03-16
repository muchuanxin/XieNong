package com.xidian.xienong.model;

import java.io.Serializable;

/**
 * Created by koumiaojuan on 2017/7/6.
 */

public class FirstInfo implements Serializable{
    private String firstId;
    private String firstName;
    private String firstPic;

    public String getFirstId() {
        return firstId;
    }

    public void setFirstId(String firstId) {
        this.firstId = firstId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }
}
