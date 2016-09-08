package my_reader;

import java.util.Date;

public class baseStation {
	private String id;
	private String user;
	private String operator;
	private int mcc;
	private int mnc;
	private int cid;
	private int lac;
	private Double latitude;
	private Double longtitude;
	private Date date;
	
	baseStation(String id,String user,String operator ,int mcc,int mnc , int cid,int lac, Double latitude, Double longtitude ,Date date ){
		this.setId(id);
		this.setUser(user);
		this.setOperator(operator);
		this.setMcc(mcc);
		this.setMnc(mnc);
		this.setLac(lac);
		this.setLatitude(latitude);
		this.setLongtitude(longtitude);
		this.setCid(cid);
		this.setDat(date);		
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public int getMcc() {
		return mcc;
	}
	public void setMcc(int mcc) {
		this.mcc = mcc;
	}
	public int getMnc() {
		return mnc;
	}
	public void setMnc(int mnc) {
		this.mnc = mnc;
	}
	public int getLac() {
		return lac;
	}
	public void setLac(int lac) {
		this.lac = lac;
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

	public int getCid() {
		return cid;
	}


	public void setCid(int cid) {
		this.cid = cid;
	}

	public Date getDat() {
		return date;
	}


	public void setDat(Date date) {
		this.date = date;
	}
}

