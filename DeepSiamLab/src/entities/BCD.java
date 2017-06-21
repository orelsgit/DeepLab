package entities;

public class BCD extends GeneralMessage {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String deepNum, size, model, manufacturer, nextDate;
private boolean isInStock;



public BCD(String size, String model, String manufacturer,String deepNum, String nextDate) {
	super();
	this.size = size;
	this.model = model;
	this.manufacturer = manufacturer;
	this.deepNum=deepNum;
	this.nextDate = nextDate;
}


public BCD(String size, String model, String manufacturer) {
	super();
	this.size = size;
	this.model = model;
	this.manufacturer = manufacturer;
}

public BCD(){
	
}
 

public BCD(String size){
	this.size = size;
}



public String getDeepNum() {
	return deepNum;
}

public void setDeepNum(String deepNum) {
	this.deepNum = deepNum;
}

public String getSize() {
	return size;
}

public void setSize(String size) {
	this.size = size;
}

public String getModel() {
	return model;
}

public void setModel(String model) {
	this.model = model;
}

public String getManufacturer() {
	return manufacturer;
}

public void setManufacturer(String manufacturer) {
	this.manufacturer = manufacturer;
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
