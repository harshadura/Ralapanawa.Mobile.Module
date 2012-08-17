package org.ralapanawa.mobile.entity;

import java.util.Date;

public class Water_Level {
	public static final String TABLE_NAME = "water_level";
	private String tankID;
	public static final String TANK_ID = "tank_id";
	private String water_level_id;
	public static final String WATER_LEVEL_ID = "water_level_id";
	private String empid;
	public static final String EMP_ID = "empid";
	private Date date;
	public static final String DATE = "datetime";
	private String depth_ft;
	public static final String DEPTH_FT = "depth_ft";
	private String capacity;
	public static final String CAPACITY = "capacity";
	private String sluiplb;
	public static final String SLUIPLB = "sluiplb";
	private String sluiprb;
	public static final String SLUIPRB = "sluiprb";
	private String remarks;
	public static final String REMARKS = "remarks";
	private String gps;
	public static final String GPS = "gps";

	public Water_Level() {

	}

	public Water_Level(String tankID, String water_level_id, String empid,
			Date date, String depth_ft, String capacity, String sluiplb,
			String sluiprb, String remarks, String gps) {
		super();
		this.tankID = tankID;
		this.water_level_id = water_level_id;
		this.empid = empid;
		this.date = date;
		this.depth_ft = depth_ft;
		this.capacity = capacity;
		this.sluiplb = sluiplb;
		this.sluiprb = sluiprb;
		this.remarks = remarks;
		this.gps = gps;
		
		
	}

	public String getTankID() {
		return tankID;
	}

	public void setTankID(String tankID) {
		this.tankID = tankID;
	}

	public String getWater_level_id() {
		return water_level_id;
	}

	public void setWater_level_id(String water_level_id) {
		this.water_level_id = water_level_id;
	}

	public String getEmpid() {
		return empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDepth_ft() {
		return depth_ft;
	}

	public void setDepth_ft(String depth_ft) {
		this.depth_ft = depth_ft;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getSluiplb() {
		return sluiplb;
	}

	public void setSluiplb(String sluiplb) {
		this.sluiplb = sluiplb;
	}

	public String getSluiprb() {
		return sluiprb;
	}

	public void setSluiprb(String sluiprb) {
		this.sluiprb = sluiprb;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

}
