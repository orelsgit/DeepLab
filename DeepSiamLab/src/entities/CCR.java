package entities;

public class CCR extends GeneralMessage{

	private static final long serialVersionUID = 1L;
private String serialNum, manufacturer, owner, model, nextDate;
private boolean isInStock;
	

public CCR(String manufacturer, String model, String serialNum,String nextDate) {
	this.manufacturer = manufacturer;
	this.model = model;
	this.serialNum = serialNum;
	this.setNextDate(nextDate);
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

public String getNextDate() {
	return nextDate;
}

public void setNextDate(String nextDate) {
	this.nextDate = nextDate;
}

public boolean isInStock() {
	return isInStock;
}

public void setInStock(boolean isInStock) {
	this.isInStock = isInStock;
}
}
