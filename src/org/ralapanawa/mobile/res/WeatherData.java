package org.ralapanawa.mobile.res;

public class WeatherData {
	private String EmpI;
	private String Datetime;
	private String Depth;
	private String SluiceopLB;
	private String SluiceopRB;
	private String Remarks;
	
	
	
	
	public WeatherData(String empI, String datetime, String depth,
			String sluiceopLB, String sluiceopRB, String remarks) {
		super();
		EmpI = empI;
		Datetime = datetime;
		Depth = depth;
		SluiceopLB = sluiceopLB;
		SluiceopRB = sluiceopRB;
		Remarks = remarks;
	}
	public String getEmpI() {
		return EmpI;
	}
	public void setEmpI(String empI) {
		EmpI = empI;
	}
	public String getDatetime() {
		return Datetime;
	}
	public void setDatetime(String datetime) {
		Datetime = datetime;
	}
	public String getDepth() {
		return Depth;
	}
	public void setDepth(String depth) {
		Depth = depth;
	}
	public String getSluiceopLB() {
		return SluiceopLB;
	}
	public void setSluiceopLB(String sluiceopLB) {
		SluiceopLB = sluiceopLB;
	}
	public String getSluiceopRB() {
		return SluiceopRB;
	}
	public void setSluiceopRB(String sluiceopRB) {
		SluiceopRB = sluiceopRB;
	}
	public String getRemarks() {
		return Remarks;
	}
	public void setRemarks(String remarks) {
		Remarks = remarks;
	}
}
