package entities;

import java.sql.Date;

public class RegCheck extends GeneralMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String FixDescription/*isProblemFix*/,deepNum,customerID;
	private Date date;
	private int isFinished;//After treatment is done,set 1
	
	public RegCheck(String fixDescription, String deepNum, String customerID, Date date, int isFinished) {
		super();
		FixDescription = fixDescription;
		this.deepNum = deepNum;
		this.customerID = customerID;
		this.date = date;
		this.isFinished = isFinished;
	}
	public String getFixDescription() {
		return FixDescription;
	}
	public void setFixDescription(String fixDescription) {
		FixDescription = fixDescription;
	}
	public String getDeepNum() {
		return deepNum;
	}
	public void setDeepNum(String deepNum) {
		this.deepNum = deepNum;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getIsFinished() {
		return isFinished;
	}
	public void setIsFinished(int isFinished) {
		this.isFinished = isFinished;
	}
}
