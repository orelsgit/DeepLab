package entities;

public class Regulator extends GeneralMessage{
	private String model,manufacturer,serialNum,KitChangeDate;
	private int deepNum,interPressure;
	
	
	public Regulator(String model, String manufacturer, String serialNum, String kitChangeDate, int deepNum,
			int interPressure) {
		super();
		this.model = model;
		this.manufacturer = manufacturer;
		this.serialNum = serialNum;
		KitChangeDate = kitChangeDate;
		this.deepNum = deepNum;
		this.interPressure = interPressure;
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
	public int getInterPressure() {
		return interPressure;
	}
	public void setInterPressure(int interPressure) {
		this.interPressure = interPressure;
	}
	
}
