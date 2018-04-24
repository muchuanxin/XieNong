package com.xidian.xienong.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.xidian.xienong.util.PostParameter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by koumiaojuan on 2017/6/5.
 */

public class Url {

    public static final String UTF_8 = "UTF-8";
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static String HTTP_RE_ERROE_CODE = "";
    private final static int READ_TIMEOUT = 20000;
    private final static int CONNECT_TIMEOUT = 20000;

    //public final static String API_HOST = "http://113.200.60.160:8080/Agriculture/";
    //public final static String API_HOST = "http://192.168.0.38:8080/Agriculture/";
    //public final static String API_HOST = "http://192.168.0.71:8080/Agriculture/";//范孙IP
    //public final static String API_HOST = "http://47.93.31.35/XieNong/";
    public final static String API_HOST = "http://47.93.31.35/XieNong/";
    /*public final static String API_HOST = "http://192.168.0.38:8080/XieNong/";*/
    public static String FarmerLogin = API_HOST + "FarmerLogin";
    public static String WorkerLogin = API_HOST + "WorkerLogin";
    public static String VerificationCode = API_HOST + "VerificationCode";
    public static String Register = API_HOST + "Register";
    public static String ForgetPassword = API_HOST + "ForgetPassword";
    public static String ChangeName = API_HOST + "ChangeName";
    public static String UploadHeadPhoto = API_HOST + "UploadHeadPhoto";
    public static String GetAllComment = API_HOST + "GetAllComment";
    public static String CheckNewVersion = API_HOST + "CheckNewVersion";
    public static String ResetPassword = API_HOST + "ResetPassword";
    public static String GetWaitingToReceiveAnnouncement = API_HOST + "GetWaitingToReceiveAnnouncement";
    public static String GetAdvertisement = API_HOST + "GetAdvertisement";
    public static String GetAllMachineCategory = API_HOST + "GetAllMachineCategory";
    public static String GetAllCroplandType = API_HOST + "GetAllCroplandType";
    public static String PublishAnnouncement = API_HOST + "PublishAnnouncement";
    public static String GetHaveReceivedAnnouncement = API_HOST + "GetHaveReceivedAnnouncement";
    public static String GetIsOperatingAnnouncement = API_HOST + "GetIsOperatingAnnouncement";
    public static String CancleAnnouncement = API_HOST + "CancleAnnouncement";
    public static String GetAllCancleReason = API_HOST + "GetAllCancleReason";
    public static String DeteleOrder = API_HOST + "DeteleOrder";
    public static String GetAllAnnouncement = API_HOST + "GetAllAnnouncement";
    public static String GetOperatedAnnouncement = API_HOST + "GetOperatedAnnouncement";
    public static String GetMachineByDistance = API_HOST + "GetMachineByDistance";
    public static String GetAllRegisteredMachines = API_HOST + "GetAllRegisteredMachines";
    public static String GetHavedPublishedMachineByWorkerId = API_HOST + "GetHavedPublishedMachineByWorkerId";
    public static String RegisterNewCategoryMachine = API_HOST + "RegisterNewCategoryMachine";
    public static String WorkerUploadMachineImage = API_HOST + "WorkerUploadMachineImage";
    public static String SaveModifiedMachine = API_HOST + "SaveModifiedMachine";
    public static String RecommendOptimalOrder = API_HOST + "RecommendOptimalOrder";
    public static String GrabOrder = API_HOST + "GrabOrder";
    public static String GetIdleMachine = API_HOST + "GetIdleMachine";
    public static String GetAddedDriver = API_HOST + "GetAddedDriver";
    public static String AddNewDriver = API_HOST + "AddNewDriver";
    public static String DispatchOrder = API_HOST + "DispatchOrder";
    public static String CancleHaveGrabbedOrder = API_HOST + "CancleHaveGrabbedOrder";
    public static String StartOperating = API_HOST + "StartOperating";
    public static String EndOperated = API_HOST + "EndOperated";
    public static String GetHaveGrabbedOrder = API_HOST + "GetHaveGrabbedOrder";
    public static String GetIsOperatingOrder = API_HOST + "GetIsOperatingOrder";
    public static String GetOperatedOrder = API_HOST + "GetOperatedOrder";
    public static String AutoCancleOrder = API_HOST + "AutoCancleOrder";
    public static String GetMachineByCategory = API_HOST + "GetMachineByCategory";
    public static String GetAllMachines = API_HOST + "GetAllMachines";
    public static String GetAllMachineByDistance = API_HOST + "GetAllMachineByDistance";
    public static String UserFeedback = API_HOST + "UserFeedback";
    public static String GetMyMachineNumberAndDriverNumber = API_HOST + "GetMyMachineNumberAndDriverNumber";
    public static String GetMachineNumber = API_HOST + "GetMachineNumber";
    public static String DeteleMachine = API_HOST + "DeteleMachine";
    public static String ModifyDriver = API_HOST + "ModifyDriver";
    public static String DeleteSomeDriver = API_HOST + "DeleteSomeDriver";
    public static String GetMachineReservationState = API_HOST + "GetMachineReservationState";
    public static String GetDriverReservationState = API_HOST + "GetDriverReservationState";
    public static String GetAvaliableResource = API_HOST + "GetAvaliableResource";
    public static String GetReservationTimeByDriverId = API_HOST + "GetReservationTimeByDriverId";
    public static String GetReservationTimeByMachineId = API_HOST + "GetReservationTimeByMachineId";
    public static String GetCommentContent = API_HOST + "GetCommentContent";
    public static String FarmerEvaluateWorker = API_HOST + "FarmerEvaluateWorker";
    public static String DeleteHaveOperatedOrder = API_HOST + "DeleteHaveOperatedOrder";
    public static String IsRegister = API_HOST + "IsRegister";
    public static String PublishMachines=API_HOST+"PublishMachines";
    public static String GetAllMachineTrademark=API_HOST+"GetAllMachineTrademark";
    public static String UploadSomeMachineImage=API_HOST+"UploadSomeMachineImage";
    public static String DeleteSomeMachine=API_HOST+"DeleteSomeMachine"; //删除某个农机
    public static String GetMyMachines = API_HOST+"GetMyMachines"; //获取农机列表
    public static String RequestCancleOrder = API_HOST+"RequestCancleOrder";
    public static String AgreeCancleOrder = API_HOST+"AgreeCancleOrder";//农户同意取消订单
    public static String RefuseCancleOrder = API_HOST+"RefuseCancleOrder";//农户拒绝取消订单
    public static String ApplyCancleOrder = API_HOST+"ApplyCancleOrder";//农户拒绝取消订单

    public static String GetHomepageAd = API_HOST+"GetHomepageAd";
    public static String AgricultureHeadline = API_HOST+"AgricultureHeadline";
    public static String AppImages = API_HOST+"AppImages";
    public static String IsLogin = API_HOST+"IsLogin";
    public static String UserIsRegister= API_HOST+"UserIsRegister";
    public static String UserRegister= API_HOST+"UserRegister";
    public static String UserLogin= API_HOST+"UserLogin";
    public static String ResetUserPassword= API_HOST+"ResetUserPassword";
    public static String GetConsults= API_HOST+"GetConsults";

    public static String GetHotCommodity= API_HOST+"GetHotCommodity";
    public static String GetHotSecondCategory= API_HOST+"GetHotSecondCategory";
    public static String GetCommodityByVariety= API_HOST+"GetCommodityByVariety";
    public static String GetCommodityCategory= API_HOST+"GetCommodityCategory";

    public static String GetRankInformation= API_HOST+"GetRankInformation";
    public static String GetRecentMonthHotCommodityByVariety= API_HOST+"GetRecentMonthHotCommodityByVariety";

    /*****www******/
    public static String GetAllMallOrder=API_HOST+"GetAllMallOrder";
    public static String GetWaitingToPayOrder=API_HOST+"GetWaitingToPayOrder";//待付款
    public static String GetWaitingToDispatchGoods=API_HOST+"GetWaitingToDispatchGoods";//待发货
    public static String GetWaitingToReceivingGoods=API_HOST+"GetWaitingToReceivingGoods";//待收货
    public static String GetHaveSignedGoods=API_HOST+"GetHaveSignedGoods";//已签收
    public static String GetWaitingToEvaluateMallOrders=API_HOST+"GetWaitingToEvaluateMallOrders";//待评价

    //xinye
    public static String AddCommodityToCart = API_HOST+"AddCommodityToCart";
    public static String GetAllCommodityFromCart = API_HOST+"GetAllCommodityFromCart";
    public static String DeleteCommodityFromCart = API_HOST+"DeleteCommodityFromCart";
    public static String FinishModifyCart = API_HOST+"FinishModifyCart";
    public static String CreateMallOrder = API_HOST+"CreateMallOrder";
    public static String GetAllMyAddress = API_HOST+"GetAllMyAddress";
    public static String AddMyAddress = API_HOST+"AddMyAddress";
    public static String DeleteMyAddress = API_HOST+"DeleteMyAddress";
    public static String SetDefaultAddress = API_HOST+"SetDefaultAddress";
    public static String ModifyMyAddress = API_HOST+"ModifyMyAddress";

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connect == null) {
            return false;
        } else// get all network info
        {
            NetworkInfo[] info = connect.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String encodeParameters(PostParameter[] postParams) {
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < postParams.length; j++) {
            if (j != 0) {
                buf.append("&");
            }
            try {
                if (null != postParams[j]) {
                    if (null != postParams[j].getName()) {
                        buf.append(URLEncoder.encode(postParams[j].getName(), "utf-8")).append("=");
                    }
                    if (null != postParams[j].getValue()) {
                        buf.append(URLEncoder.encode(postParams[j].getValue(), "utf-8"));
                    }
                }
            } catch (java.io.UnsupportedEncodingException neverHappen) {
            }
        }
        return buf.toString();
    }

    public static String httpRequest(String url, PostParameter[] postParams, String httpMethod) {
        InputStream is = null;
        String jsonSource = "";
        try {
            HttpURLConnection con = null;
            OutputStream osw = null;
            try {
                con = (HttpURLConnection) new URL(url).openConnection();
                con.setDoInput(true);
                if (null != postParams || POST.equals(httpMethod)) {
                    con.setRequestMethod(POST);
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setDoOutput(true);
                    con.setDoInput(true);

                    con.setReadTimeout(READ_TIMEOUT);
                    con.setConnectTimeout(CONNECT_TIMEOUT);
                    String postParam = "";
                    if (postParams != null) {
                        // 莴訋私薷褠覡毛貈止讗蟿脪毛蠆廷
                        postParam = encodeParameters(postParams);
                    }
                    byte[] bytes = postParam.getBytes("UTF-8");
                    con.setRequestProperty("Content-Length", Integer.toString(bytes.length));

                    Log.i("mmm", "url----" + con.getURL() + "?" + postParam);
                    osw = con.getOutputStream();
                    osw.write(bytes);
                    osw.flush();
                    osw.close();
                }
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    is = con.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(is, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String temp = "";

                    //

                    while ((temp = bufferedReader.readLine()) != null) {
                        jsonSource += temp;
                    }
                    // 跇視
                    bufferedReader.close();
                    is.close();
                    Log.i("mmm", "jsonString=" + jsonSource);
                    return jsonSource;

                } else {
                    HTTP_RE_ERROE_CODE = String.valueOf(con.getResponseCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
                jsonSource = null;
            } finally {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonSource = null;
        } finally {

            return jsonSource;
        }

    }


}
