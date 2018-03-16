package com.xidian.xienong.model;

import com.xidian.xienong.util.Time;

import java.io.Serializable;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/8.
 */

public class OrderBean implements Serializable,Comparable<OrderBean>{
    private String order_id;
    private String orderCode;
    private String farmer_id;
    private String farmer_name;
    private String worker_id;
    private String worker_name;
    private String worker_telephone;
    private String telephone;//farmer
    private String farmerHeadphoto;//farmer
    private String workerHeadphoto;//farmer
    private String crop_address;
    private double crop_longtitude;
    private double crop_lantitude;
    private String category_id;
    private String machine_category;
    private String cropland_type;
    private double cropland_number;
    private String reservation_time;
    private String upload_time;
    private String orderState;
    private boolean isEvaluate;
    private boolean isDeletedByFarmer;
    private List<Machine> machines;
    private List<Driver>  drivers;
    private String cancleTime;
    private String cancleReason;
    private List<MachineImage>  machineImages;
    private boolean isDeleteByWorker;
    private long endTime;
    private List<Resource>  resources;
    private int price;
    private String adviceState;
    private String grabOrderTime;
    private String dispatchTime;
    private String operatingTime;
    private String operatedTime;
    private String evaluateTime;
    private String applyCancleReason;
    private String applyCancleReasonId;
    private double distance;
    private String cancleMethod;



    public String getCancleMethod() {
        return cancleMethod;
    }
    public void setCancleMethod(String cancleMethod) {
        this.cancleMethod = cancleMethod;
    }
    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }
    public String getApplyCancleReasonId() {
        return applyCancleReasonId;
    }
    public void setApplyCancleReasonId(String applyCancleReasonId) {
        this.applyCancleReasonId = applyCancleReasonId;
    }
    public String getApplyCancleReason() {
        return applyCancleReason;
    }
    public void setApplyCancleReason(String applyCancleReason) {
        this.applyCancleReason = applyCancleReason;
    }
    public String getGrabOrderTime() {
        return grabOrderTime;
    }
    public void setGrabOrderTime(String grabOrderTime) {
        this.grabOrderTime = grabOrderTime;
    }
    public String getDispatchTime() {
        return dispatchTime;
    }
    public void setDispatchTime(String dispatchTime) {
        this.dispatchTime = dispatchTime;
    }
    public String getOperatingTime() {
        return operatingTime;
    }
    public void setOperatingTime(String operatingTime) {
        this.operatingTime = operatingTime;
    }
    public String getOperatedTime() {
        return operatedTime;
    }
    public void setOperatedTime(String operatedTime) {
        this.operatedTime = operatedTime;
    }
    public String getEvaluateTime() {
        return evaluateTime;
    }
    public void setEvaluateTime(String evaluateTime) {
        this.evaluateTime = evaluateTime;
    }
    public String getAdviceState() {
        return adviceState;
    }
    public void setAdviceState(String adviceState) {
        this.adviceState = adviceState;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public String getWorker_id() {
        return worker_id;
    }
    public void setWorker_id(String worker_id) {
        this.worker_id = worker_id;
    }
    public List<Resource> getResources() {
        return resources;
    }
    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }
    public long getEndTime() {
        return endTime;
    }
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    public boolean isDeleteByWorker() {
        return isDeleteByWorker;
    }
    public void setDeleteByWorker(boolean isDeleteByWorker) {
        this.isDeleteByWorker = isDeleteByWorker;
    }
    public String getOrder_id() {
        return order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public String getOrderCode() {
        return orderCode;
    }
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
    public String getFarmer_name() {
        return farmer_name;
    }
    public void setFarmer_name(String farmer_name) {
        this.farmer_name = farmer_name;
    }
    public String getCrop_address() {
        return crop_address;
    }
    public void setCrop_address(String crop_address) {
        this.crop_address = crop_address;
    }
    public double getCrop_longtitude() {
        return crop_longtitude;
    }
    public void setCrop_longtitude(double crop_longtitude) {
        this.crop_longtitude = crop_longtitude;
    }
    public double getCrop_lantitude() {
        return crop_lantitude;
    }
    public void setCrop_lantitude(double crop_lantitude) {
        this.crop_lantitude = crop_lantitude;
    }
    public String getMachine_category() {
        return machine_category;
    }
    public void setMachine_category(String machine_category) {
        this.machine_category = machine_category;
    }
    public String getCropland_type() {
        return cropland_type;
    }
    public void setCropland_type(String cropland_type) {
        this.cropland_type = cropland_type;
    }
    public double getCropland_number() {
        return cropland_number;
    }
    public void setCropland_number(double cropland_number) {
        this.cropland_number = cropland_number;
    }
    public String getReservation_time() {
        return reservation_time;
    }
    public void setReservation_time(String reservation_time) {
        this.reservation_time = reservation_time;
    }
    public String getUpload_time() {
        return upload_time;
    }
    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }
    public String getOrderState() {
        return orderState;
    }
    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }
    public String getFarmer_id() {
        return farmer_id;
    }
    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFarmerHeadphoto() {
        return farmerHeadphoto;
    }

    public void setFarmerHeadphoto(String farmerHeadphoto) {
        this.farmerHeadphoto = farmerHeadphoto;
    }

    public String getWorkerHeadphoto() {
        return workerHeadphoto;
    }

    public void setWorkerHeadphoto(String workerHeadphoto) {
        this.workerHeadphoto = workerHeadphoto;
    }

    public boolean isEvaluate() {
        return isEvaluate;
    }
    public void setEvaluate(boolean isEvaluate) {
        this.isEvaluate = isEvaluate;
    }
    public boolean isDeletedByFarmer() {
        return isDeletedByFarmer;
    }
    public void setDeletedByFarmer(boolean isDeletedByFarmer) {
        this.isDeletedByFarmer = isDeletedByFarmer;
    }
    public List<Machine> getMachines() {
        return machines;
    }
    public void setMachines(List<Machine> machines) {
        this.machines = machines;
    }
    public List<Driver> getDrivers() {
        return drivers;
    }
    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }
    public String getWorker_name() {
        return worker_name;
    }
    public void setWorker_name(String worker_name) {
        this.worker_name = worker_name;
    }
    public String getWorker_telephone() {
        return worker_telephone;
    }
    public void setWorker_telephone(String worker_telephone) {
        this.worker_telephone = worker_telephone;
    }
    public String getCancleTime() {
        return cancleTime;
    }
    public void setCancleTime(String cancleTime) {
        this.cancleTime = cancleTime;
    }
    public String getCancleReason() {
        return cancleReason;
    }
    public void setCancleReason(String cancleReason) {
        this.cancleReason = cancleReason;
    }
    public List<MachineImage> getMachineImages() {
        return machineImages;
    }
    public void setMachineImages(List<MachineImage> machineImages) {
        this.machineImages = machineImages;
    }
    public String getCategory_id() {
        return category_id;
    }
    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
    @Override
    public int compareTo(OrderBean another) {
        // TODO Auto-generated method stub
        if(Time.compare_date(this.reservation_time, another.reservation_time)==1){
            return -1;
        }else if(Time.compare_date(this.reservation_time, another.reservation_time)==-1){
            return 1;
        }else{
            return 0;
        }
    }

}
