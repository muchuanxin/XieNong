package com.xidian.xienong.util;

import com.xidian.xienong.model.CroplandType;
import com.xidian.xienong.model.MachineCategory;

import java.util.ArrayList;
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
}
