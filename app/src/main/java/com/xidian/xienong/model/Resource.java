package com.xidian.xienong.model;

import java.io.Serializable;

public class Resource implements Serializable{
	private String machineId;
	private String machineName;
	private String driverId;
	private String driverName;
	private String driverTele;
	private Machine machine;
	private Driver driver;
	
	public Machine getMachine() {
		return machine;
	}
	public void setMachine(Machine machine) {
		this.machine = machine;
	}
	public Driver getDriver() {
		return driver;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getMachineName() {
		return machineName;
	}
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	public String getDriverId() {
		return driverId;
	}
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverTele() {
		return driverTele;
	}
	public void setDriverTele(String driverTele) {
		this.driverTele = driverTele;
	}
	
	

}
