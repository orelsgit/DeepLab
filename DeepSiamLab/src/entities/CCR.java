package entities;

public class CCR extends GeneralMessage{

	private static final long serialVersionUID = 1L;
private String serialNum, manufacturer, owner, model;

	

public CCR(String manufacturer, String model, String serialNum) {
	super();
	this.manufacturer = manufacturer;
	this.model = model;
	this.serialNum = serialNum;
}

public CCR(){
	
}

public CCR(String manufacturer){
	this.manufacturer = manufacturer;
}

public CCR(String manufacturer,String model) {
	super();
	this.manufacturer = manufacturer;
	this.setModel(model);
}

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

public String getModel() {
	return model;
}

public void setModel(String model) {
	this.model = model;
}
}
