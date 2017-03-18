package entities;

public class Compressor extends GeneralMessage{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serialNum, manufacturer, capacity;

public String getSerialNum() {
	return serialNum;
}
public void setSerialNum(String serialNum) {
	this.serialNum = serialNum;
}
public String getManufacturer() {
	return manufacturer;
}
public void setManufacturer(String manufacturer) {
	this.manufacturer = manufacturer;
}
public String getCapacity() {
	return capacity;
}
public void setCapacity(String capacity) {
	this.capacity = capacity;
}
}
