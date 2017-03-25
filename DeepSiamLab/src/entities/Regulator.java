package entities;

public class Regulator extends GeneralMessage{
	private String model,manufacturer,serialNum,KitChangeDate;
	private int deepNum;
	float interPressure;
	
	
	public Regulator(String model, String manufacturer, String serialNum, int deepNum, String kitChangeDate) {
		super();
		this.model = model;
		this.manufacturer = manufacturer;
		this.serialNum = serialNum;
		KitChangeDate = kitChangeDate;
		this.deepNum = deepNum;
	}
	
	
	
	public Regulator() {
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
	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public String getKitChangeDate() {
		return KitChangeDate;
	}
	public void setKitChangeDate(String kitChangeDate) {
		KitChangeDate = kitChangeDate;
	}
	public int getDeepNum() {
		return deepNum;
	}
	public void setDeepNum(int deepNum) {
		this.deepNum = deepNum;
	}
	public float getInterPressure() {
		return interPressure;
	}
	public void setInterPressure(float f) {
		this.interPressure = f;
	}
	
}
