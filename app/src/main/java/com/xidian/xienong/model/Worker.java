package com.xidian.xienong.model;

import com.xidian.xienong.util.Constants;

import java.io.Serializable;
import java.util.List;

public class Worker implements Serializable,Comparable<Worker>{
	private String workerId;
	private String workerName;
	private String password;
	private String telephone;
	private String headPhoto;
	private String brief;
	private String address;
	private double longtitude;
	private double lantitude;
	private String category_id;
	private String category_name;
	private int work_price;
	private String machine_number;
	private boolean isChecked;
	private String machineDescription;
	private List<MachineImage> machineImages;
	private double distance;
	private float evaluateVaule;
	private int evaluateCount;
	private String simpleAddress;
	
	
	
	public String getSimpleAddress() {
		return simpleAddress;
	}
	public void setSimpleAddress(String simpleAddress) {
		this.simpleAddress = simpleAddress;
	}
	public float getEvaluateVaule() {
		return evaluateVaule;
	}
	public void setEvaluateVaule(float evaluateVaule) {
		this.evaluateVaule = evaluateVaule;
	}
	public int getEvaluateCount() {
		return evaluateCount;
	}
	public void setEvaluateCount(int evaluateCount) {
		this.evaluateCount = evaluateCount;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getWorkerId() {
		return workerId;
	}
	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}
	public String getWorkerName() {
		return workerName;
	}
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getHeadPhoto() {
		return headPhoto;
	}
	public void setHeadPhoto(String headPhoto) {
		this.headPhoto = headPhoto;
	}
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public double getLantitude() {
		return lantitude;
	}
	public void setLantitude(double lantitude) {
		this.lantitude = lantitude;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public int getWork_price() {
		return work_price;
	}
	public void setWork_price(int work_price) {
		this.work_price = work_price;
	}
	public String getMachine_number() {
		return machine_number;
	}
	public void setMachine_number(String machine_number) {
		this.machine_number = machine_number;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public String getMachineDescription() {
		return machineDescription;
	}
	public void setMachineDescription(String machineDescription) {
		this.machineDescription = machineDescription;
	}
	public List<MachineImage> getMachineImages() {
		return machineImages;
	}
	public void setMachineImages(List<MachineImage> machineImages) {
		this.machineImages = machineImages;
	}
	
	@Override
	public int compareTo(Worker another) {
		// TODO Auto-generated method stub
		if(Constants.sortStyle == 0){
			if(this.distance < another.getDistance() && this.evaluateVaule > another.getEvaluateVaule()){
				return -1;
			}else if(this.distance > another.getDistance() || this.evaluateVaule < another.getEvaluateVaule() ){
				return 1;
			}else{
				return 0;
			}
		}else if (Constants.sortStyle == 1){
			if(this.distance < another.getDistance()){
				return -1;
			}else if(this.distance > another.getDistance()){
				return 1;
			}else{
				return 0;
			}
		}else if (Constants.sortStyle == 2){
			if(this.evaluateVaule < another.getEvaluateVaule()){ 
				return 1;
			}else if(this.evaluateVaule > another.getEvaluateVaule()){
				return -1;
			}else{
				return 0;
			}
		}else{
			if(this.evaluateCount < another.getEvaluateCount()){
				return 1;
			}else if(this.evaluateCount > another.getEvaluateCount()){
				return -1;
			}else{
				return 0;
			}
		}
	}
	
	
	
	

}
