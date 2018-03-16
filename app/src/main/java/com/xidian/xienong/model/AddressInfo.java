package com.xidian.xienong.model;

/**
 * 收货地址的信息
 * @author SoBan
 * @create 2016/12/7 11:13.
 */
public class AddressInfo {
    private static AddressInfo mInstance;
    private String sProvince;
    private String sCity;
    private String sDistrict;

    public synchronized static AddressInfo getInstance() {
        if (mInstance == null) {
            mInstance = new AddressInfo();
        }
        return mInstance;
    }

    public String getsProvince() {
        return sProvince;
    }

    public void setsProvince(String sProvince) {
        this.sProvince = sProvince;
    }

    public String getsCity() {
        return sCity;
    }

    public void setsCity(String sCity) {
        this.sCity = sCity;
    }

    public String getsDistrict() {
        return sDistrict;
    }

    public void setsDistrict(String sDistrict) {
        this.sDistrict = sDistrict;
    }

    public String getAddress() {
        return sProvince +" " + sCity +" " + sDistrict;
    }
}
