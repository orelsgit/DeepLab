package entities;

public class Regulator extends GeneralMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String model,manufacturer,serialNum, deepNum, nextDate;
	float interPressure;
	
	
	public Regulator(String model, String manufacturer, float interPressure, String serialNum, String deepNum, String nextDate) {
		super();
		this.model = model;
		this.manufacturer = manufacturer;
		this.interPressure = interPressure;
		this.serialNum = serialNum;
		this.deepNum = deepNum;
		this.nextDate = nextDate;
		}
	
	
	
	public Regulator(String model){
		this.model = model;
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

	public String getDeepNum() {
		return deepNum;
	}
	public void setDeepNum(String deepNum) {
		this.deepNum = deepNum;
	}
	public float getInterPressure() {
		return interPressure;
	}
	public void setInterPressure(float f) {
		this.interPressure = f;
	}



	public String getNextDate() {
		return nextDate;
	}



	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}
	
}
