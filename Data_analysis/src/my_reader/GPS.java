package my_reader;

import java.util.Date;

public class GPS {
	
private String id;
private String user;
private Double latitude;
private Double longtitude;
private Date date;



GPS(String id,String user,Double latitude,Double longtitude,Date date){
	
	this.setId(id);
	this.setUser(user);
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


public String getUser() {
	return user;
}


public void setUser(String user) {
	this.user = user;
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

public Date getDat() {
	return date;
}


public void setDat(Date date) {
	this.date = date;
}
}