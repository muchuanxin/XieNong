package com.xidian.xienong.model;

import java.io.Serializable;
import java.util.List;

public class Machine implements Serializable{
	private String machine_id;
	private String identification_number;
	private String machineCategory;
	private int machineNumber;
	private String uploadTime;
	private String isChecked;
	private String category_name;
	private String category_id;
	private String trademark_id;  //品牌
	private String trademark_name;
	private String batch_number;//批次号
	private int work_price;
	private String machineDescriptionOrMessage;
	private List<MachineImage> machineImage;
	private String reservationTime;
	private String machineIdentification;
	private Boolean isUsed;
	private List<MachineIdentify> machineIdentify;
	
	
	
	public Boolean getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}
	public String getReservationTime() {
		return reservationTime;
	}
	public void setReservationTime(String reservationTime) {
		this.reservationTime = reservationTime;
	}
	public String getMachine_id() {
		return machine_id;
	}
	public void setMachine_id(String machine_id) {
		this.machine_id = machine_id;
	}
	public String getIdentification_number() {
		return identification_number;
	}
	public void setIdentification_number(String identification_number) {
		this.identification_number = identification_number;
	}
	public String getMachineCategory() {
		return machineCategory;
	}
	public void setMachineCategory(String machineCategory) {
		this.machineCategory = machineCategory;
	}
	public int getMachineNumber() {
		return machineNumber;
	}
	public void setMachineNumber(int machineNumber) {
		this.machineNumber = machineNumber;
	}
	public String getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
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
	public String getMachineDescriptionOrMessage() {
		return machineDescriptionOrMessage;
	}
	public void setMachineDescriptionOrMessage(String machineDescriptionOrMessage) {
		this.machineDescriptionOrMessage = machineDescriptionOrMessage;
	}
	public List<MachineImage> getMachineImage() {
		return machineImage;
	}
	public void setMachineImage(List<MachineImage> machineImage) {
		this.machineImage = machineImage;
	}
	public String getMachineIdentification() {
		return machineIdentification;
	}
	public void setMachineIdentification(String machineIdentification) {
		this.machineIdentification = machineIdentification;
	}
	public String getTrademark_id() {
		return trademark_id;
	}
	public void setTrademark_id(String trademark_id) {
		this.trademark_id = trademark_id;
	}
	public String getTrademark_name() {
		return trademark_name;
	}
	public void setTrademark_name(String trademark_name) {
		this.trademark_name = trademark_name;
	}
	public String getBatch_number() {
		return batch_number;
	}
	public void setBatch_number(String batch_number) {
		this.batch_number = batch_number;
	}
	public List<MachineIdentify> getMachineIdentify() {
		return machineIdentify;
	}
	public void setMachineIdentify(List<MachineIdentify> machineIdentify) {
		this.machineIdentify = machineIdentify;
	}
	
	
	

}
