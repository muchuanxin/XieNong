package com.xidian.xienong.util;
/**
 * 发起HTTP请求中的参数
 * @author Bryan
 * @version 0.2
 * @create 2013-8-1
 */
public class PostParameter {
    public PostParameter(){}
    public PostParameter(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }
    private String name;
    private String value;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    /*private void test()
    {
    	org.androidpn.client.Constants.xmppManager.getConnection().isConnected();
    }*/
}
