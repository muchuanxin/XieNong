package com.xidian.xienong.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by koumiaojuan on 2017/6/5.
 */

public class SharePreferenceUtil {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SharePreferenceUtil(Context context, String file) {
        sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void setToken(String token){
        editor.putString("token",token);
        editor.commit();
    }
    public String getToken() {
        return sp.getString("token", "");
    }

    public void setUserId(String userId){
        editor.putString("userId",userId);
        editor.commit();
    }
    public String getUserId() {
        return sp.getString("userId", "");
    }

    public void setUserName(String userName){
        editor.putString("userName",userName);
        editor.commit();
    }
    public String getUserName() {
        return sp.getString("userName", "");
    }

    public void setphoneNumber(String phoneNumber){
        editor.putString("telephone", phoneNumber);
        editor.commit();
    }
    public String getPhoneNumber(){
        return sp.getString("telephone", "");
    }

    public void setPassword(String password) {
        editor.putString("password", password);
        editor.commit();
    }
    public String getPassword() {
        return sp.getString("password", "");
    }

    public void setHeadPhoto(String headPhoto) {
        editor.putString("head_photo", headPhoto);
        editor.commit();
    }
    public String getHeadPhoto() {
        return sp.getString("head_photo", "");
    }

    public void setFarmer(Boolean isFarmer) {
        editor.putBoolean("isFarmer", isFarmer);
        editor.commit();
    }
    public Boolean getFarmer() {
        return sp.getBoolean("isFarmer", false);
    }

    public void setWorker(Boolean isWorker) {
        editor.putBoolean("isWorker", isWorker);
        editor.commit();
    }
    public Boolean getWorker() {
        return sp.getBoolean("isWorker", false);
    }

    public void setBrief(String brief) {
        editor.putString("brief", brief);
        editor.commit();
    }
    public String getBrief(){
        return sp.getString("brief","");
    }

    public String getSimpleAddress() {
        return sp.getString("SimpleAddress", "");
    }
    public void setSimpleAddress(String name) {
        editor.putString("SimpleAddress", name);
        editor.commit();
    }

    public String getAddress() {
        return sp.getString("Address", "");
    }
    public void setAddress(String name) {
        editor.putString("Address", name);
        editor.commit();
    }

    public String getLongtitude() {
        return sp.getString("longtitude", "");
    }
    public void setLongtitude(String longtitude) {
        editor.putString("longtitude", longtitude);
        editor.commit();
    }

    public String getLantitude() {
        return sp.getString("lantitude", "");
    }
    public void setLantitude(String lantitude) {
        editor.putString("lantitude", lantitude);
        editor.commit();
    }

    public String getTotalIntegral() {
        return sp.getString("totalIntegral", "");
    }
    public void setTotalIntegral(String totalIntegral) {
        editor.putString("totalIntegral", totalIntegral);
        editor.commit();
    }


    public void setIsWorker(String isWorker) {
        editor.putString("isAWorker", isWorker);
        editor.commit();
    }
    public String getisWorker() {
        return sp.getString("isAWorker", "");
    }


    public void setFarmerId(String id) {
        editor.putString("farmerid", id);
        editor.commit();
    }

    public String getFarmerId() {
        return sp.getString("farmerid", "");
    }

    public void setLoginRole(String loginRole) {
        editor.putString("loginRole", loginRole);
        editor.commit();
    }

    public String getLoginRole() {
        return sp.getString("loginRole", "");
    }












    public String getCookie() {
        return sp.getString("cookie", "");
    }

    public void setCookie(String cookie) {
        editor.putString("cookie", cookie);
        editor.commit();
    }

    public String getFarmerName() {
        return sp.getString("farmername", "");
    }

    public void setFarmerName(String name) {
        editor.putString("farmername", name);
        editor.commit();
    }



    //第一次启动
    public void setIsFirst(boolean isFirst) {
        editor.putBoolean("isFirst", isFirst);
        editor.commit();
    }

    public boolean getisFirst() {
        return sp.getBoolean("isFirst", true);
    }

    public void setIsFirstLogin(boolean isFirst) {
        editor.putBoolean("isFirstLogin", isFirst);
        editor.commit();
    }

    public boolean getisFirstLogin() {
        return sp.getBoolean("isFirstLogin", true);
    }

    public void setIsFirstGoFarmerMainActivity(boolean isFirst) {
        editor.putBoolean("isFirstGoFarmerMainActivity", isFirst);
        editor.commit();
    }

    public boolean getisFirstGoFarmerMainActivity() {
        return sp.getBoolean("isFirstGoFarmerMainActivity", true);
    }

    public void setIsFirstGoWorkerMainActivity(boolean isFirst) {
        editor.putBoolean("isFirstGoWorkerMainActivity", isFirst);
        editor.commit();
    }

    public boolean getisFirstGoWorkerMainActivity() {
        return sp.getBoolean("isFirstGoWorkerMainActivity", true);
    }

    public void setIsFirstChooseRole(boolean isFirst) {
        editor.putBoolean("isFirstChooseRole", isFirst);
        editor.commit();
    }

    public boolean getisFirstChooseRole() {
        return sp.getBoolean("isFirstChooseRole", true);
    }

    public void setCurrentCity(String currentCity) {
        editor.putString("currentCity", currentCity);
        editor.commit();
    }

    public String getCurrentCity() {
        return sp.getString("currentCity", "");
    }

    public String getStatus() {
        return sp.getString("status", "");
    }

    public void setStatus(String status) {
        editor.putString("status", status);
        editor.commit();
    }


    //---worker
    public void setWorkerId(String id) {
        editor.putString("workerid", id);
        editor.commit();
    }

    public String getWorkerId() {
        return sp.getString("workerid", "");
    }






    public void setCountyId(String countyid) {
        editor.putString("countyid", countyid);
        editor.commit();
    }

    public String getCountyId(){
        return sp.getString("countyid", "");
    }

    public void setWorkPrice(String workprice) {
        editor.putString("workprice", workprice);
        editor.commit();
    }

    public String getWorkPrice(){
        return sp.getString("workprice", "");
    }

    public String getWorkerName() {
        return sp.getString("workername", "");
    }

    public void setWorkerName(String name) {
        editor.putString("workername", name);
        editor.commit();
    }


}
