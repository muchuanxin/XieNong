package com.xidian.xienong.model;

import java.util.List;

/**
 * @author SoBan
 * @create 2016/12/7 09:34.
 */
public class ResultInfo {

    private String message;
    private int status;
    private List<List<AreaInfo>> result; //获取省份列表，上面两个可以不用理

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<List<AreaInfo>> getResult() {
        return result;
    }

    public void setResult(List<List<AreaInfo>> result) {
        this.result = result;
    }
}
