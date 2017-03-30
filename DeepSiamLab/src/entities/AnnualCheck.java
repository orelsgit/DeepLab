package entities;

/**
 * Contains information about annual checks for any customer's equipment
 * @author orelzman
 *
 */
public class AnnualCheck extends GeneralMessage{


	private static final long serialVersionUID = 1L;
	
	private String size, model, manufacturer, custID, annualComments, fixComments, serialNum, kitChangeDate;
	private int orderNum;
	private boolean bcd, reg, ccr, isKit, isManagerApprove;//For server check needs
	
	
	
	public AnnualCheck(){
		super();
	}


	public AnnualCheck(String custID, String annualComments, String fixComments, String serialNum) {//Tank constructor
		super();
		this.custID = custID;
		this.annualComments = annualComments;
		this.fixComments = fixComments;
		this.serialNum = serialNum;
	}
	
	

	public AnnualCheck(String custID, String annualComments, String fixComments, String serialNum,
			String kitChangeDate) {//Regulator constructor
		super();
		this.custID = custID;
		this.annualComments = annualComments;
		this.fixComments = fixComments;
		this.serialNum = serialNum;
		this.kitChangeDate = kitChangeDate;
	}


	
	

	public AnnualCheck(String size, String model, String manufacturer, String custID, String annualComments,
			String fixComments) {//BCD constructor
		super();
		this.size = size;
		this.model = model;
		this.manufacturer = manufacturer;
		this.custID = custID;
		this.annualComments = annualComments;
		this.fixComments = fixComments;
	}



	public boolean isBcd() {
		return bcd;
	}

	public void setBcd(boolean bcd) {
		this.bcd = bcd;
	}

	public boolean isReg() {
		return reg;
	}

	public void setReg(boolean reg) {
		this.reg = reg;
	}

	public boolean isCcr() {
		return ccr;
	}

	public void setCcr(boolean ccr) {
		this.ccr = ccr;
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

	public String getCustID() {
		return custID;
	}

	public void setCustID(String custID) {
		this.custID = custID;
	}

	public String getAnnualComments() {
		return annualComments;
	}

	public void setAnnualComments(String annualComments) {
		this.annualComments = annualComments;
	}

	public String getFixComments() {
		return fixComments;
	}

	public void setFixComments(String fixComments) {
		this.fixComments = fixComments;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getKitChangeDate() {
		return kitChangeDate;
	}

	public void setKitChangeDate(String kitChangeDate) {
		this.kitChangeDate = kitChangeDate;
	}


	public boolean isManagerApprove() {
		return isManagerApprove;
	}


	public void setManagerApprove(boolean isManagerApproved) {
		this.isManagerApprove = isManagerApproved;
	}


	public boolean isKit() {
		return isKit;
	}


	public void setKit(boolean isKit) {
		this.isKit = isKit;
	}


	public int getOrderNum() {
		return orderNum;
	}


	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	
}
