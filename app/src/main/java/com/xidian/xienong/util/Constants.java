package com.xidian.xienong.util;

import com.xidian.xienong.application.ConnectUtil;
import com.xidian.xienong.model.CroplandType;
import com.xidian.xienong.model.MachineCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/5.
 */

public class Constants {
    public static final String SAVE_USER = "saveUser";//保存用户信息的xml文件名
    public static List<MachineCategory> machineCategory = new ArrayList<MachineCategory>();
    //croplandType
    public static List<CroplandType> croplandType = new ArrayList<CroplandType>();
    //location
    public static List<String> location = new ArrayList<String>();
    public static boolean haveConfirmed = false;
    public static List<MachineCategory> machineCategoryList = new ArrayList<MachineCategory>();
    public static  boolean isFirstGetAllMachineCategory = true;
    public static final String HAS_EVALUATED_BY_FARMER = "has_evaluated_by_farmer";
    public static final String HAS_AGREE_OR_REFUSE_BY_FARMER = "has_agree_or_refuse_by_farmer";
    public static final String GRAB_ORDER_FAIL_ACTION = "grab_order_fail_action";
    public static final String GRAB_ORDER_SUCCESS_ACTION = "grab_order_success_action";
    public static final String DISPATCH_ORDER_SUCCESS_ACTION = "dispatch_order_success_action";
    public static final String IS_OPERATING_STATE_ORDER_ACTION = "is_operating_state_order_action";
    public static final String OPERATED_ORDER_ACTION = "operated_order_action";
    public static final String CANCLE_GRABBED_ORDER_ACTION = "cancle_grabbed_order_action";
    public static final String CANCLE_ORDER_ACTION = "cancle_order_action";
    public static final String DISPATCH_INFO_CANCLE_ORDER_ACTION = "dispatch_info_cancle_order_action";
    public static final String CANCLE_REQUEST_ACTION = "cancle_request_action";
    //me_uploadHeadPhoto
    public static String uploadHeadImage = ConnectUtil.UploadHeadPhoto + "?";

    public static int sortStyle=0;
    public static List<String> distanceList = new ArrayList<String>(Arrays.asList("附近", "10km", "20km","50km","不限"));
    public static List<String> intelligenceList = new ArrayList<String>(Arrays.asList("智能排序", "离我最近","好评优先", "人气最高"));

    //upload_photo
    public final static int LOCAL_PHOTO = 3;
    public static final int TAKE_PICTURE = 0x000001;
}
