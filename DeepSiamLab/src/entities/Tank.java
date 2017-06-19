package entities;

public class Tank extends GeneralMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serialNum,manufacturer, model, deepNum, nextDate;
	private int volume, aluminium;
	public Tank(String model,String manufacturer, int volume, String serialNum, String deepNum, int aluminium, String nextDate){
		this.volume = volume;
		this.model = model;
		this.serialNum = serialNum;
		this.manufacturer = manufacturer;
		this.deepNum = deepNum;
		this.aluminium = aluminium;
		this.setNextDate(nextDate);
	}

	public Tank(){
		
	}
	
	public Tank(String model){
		this.model = model;
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
		this.model = model;
	}

	public String getDeepNum() {
		return deepNum;
	}

	public void setDeepNum(String deepNum) {
		this.deepNum = deepNum;
	}
	
	public int getAluminium(){
		return aluminium;
	}
	
	public void setAluminium(int aluminium){
		this.aluminium = aluminium;
	}

	public String getNextDate() {
		return nextDate;
	}

	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}

}
