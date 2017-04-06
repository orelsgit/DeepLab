package entities;

public class Tank extends GeneralMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String deepNum/*A serial number written by the diving club*/,
	serialNum/*A serial number written by the manufacturer*/,
	volume,manufacturer;
	public Tank(String volume,String manufacturer)
	{
		this.volume = volume;
		this.manufacturer = manufacturer;
	}

	public String getDeepNum() {
		return deepNum;
	}

	public void setDeepNum(String deepNum) {
		this.deepNum = deepNum;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
}
