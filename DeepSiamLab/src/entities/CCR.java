package entities;

public class CCR extends GeneralMessage{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String serialNum, manufacturer, owner;

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

public String getOwner() {
	return owner;
}

public void setOwner(String owner) {
	this.owner = owner;
}
}
