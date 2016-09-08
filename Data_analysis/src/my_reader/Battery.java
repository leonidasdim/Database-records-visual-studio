package my_reader;

import java.util.Date;

public class Battery {
	private String id;
	private String user;
	private int level;
	private int plugged;
	private int temperature;
	private int voltage;
	private Date date;

Battery(String id,String user,int level ,int plugged,int temperature ,int voltage ,Date date){
	this.setId(id);
	this.setUser(user);
	this.setPlugged(plugged);
	this.setLevel(level);
	this.setTemperature(temperature);
	this.setVoltage(voltage);
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

public int getPlugged() {
	return plugged;
}

public void setPlugged(int plugged) {
	this.plugged = plugged;
}

public int getLevel() {
	return level;
}

public void setLevel(int level) {
	this.level = level;
}

public int getTemperature() {
	return temperature;
}

public void setTemperature(int temperature) {
	this.temperature = temperature;
}

public int getVoltage() {
	return voltage;
}

public void setVoltage(int voltage) {
	this.voltage = voltage;
}

public Date getDat() {
	return date;
}

public void setDat(Date date) {
	this.date = date;
}

}
