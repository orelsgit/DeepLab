package entities;

public class Tank extends GeneralMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serialNum,manufacturer, model;
	private int volume;
	public Tank(String model,String manufacturer, int volume)
	{
		this.volume = volume;
		this.model = model;
		this.serialNum = serialNum;
		this.manufacturer = manufacturer;
	}



	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		model = model;
	}
}
