package my_reader;

import java.util.Date;

public class wifi {
	private String id;
	private String user;
	private String ssid;
	private String mac;
	private int rssi;
	private int rssi_min = 0;
	private int frenqury;
	private Double latitude;
	private Double longtitude;
	private Date date;
	
	wifi(String id,String user,String ssid,String mac,int rssi,int frenqury,Double latitude,Double longtitude,Date date){
		
	this.setId(id);
	this.setUser(user);
	this.setSsid(ssid);
	this.setMac(mac);
	this.setRssi(rssi);
	this.setFrenqury(frenqury);
	this.setLatitude(latitude);
	this.setLongtitude(longtitude);
	this.setDat(date);
	}
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getSsid() {
		return ssid;
	}
	
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	
	public String getMac() {
		return mac;
	}
	
	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public int getRssi() {
		return rssi;
	}
	
	public void setRssi(int rssi) {
		this.rssi = rssi;
	}
	
	public int getFrenqury() {
		return frenqury;
	}
	
	public void setFrenqury(int frenqury) {
		this.frenqury = frenqury;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public Double getLongtitude() {
		return longtitude;
	}
	
	public void setLongtitude(Double longtitude) {
		this.longtitude = longtitude;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	
	public Date getDat() {
		return date;
	}


	public void setDat(Date date) {
		this.date = date;
	}


	public int getRssi_min() {
		return rssi_min;
	}


	public void setRssi_min(int rssi_min) {
		this.rssi_min = rssi_min;
	}

}
