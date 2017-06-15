package entities;

import java.util.ArrayList;

public class Order extends GeneralMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int orderNum, handled, cost;//HANDLED :  -1 - Not handled, 0 - Not approved by manager, 1-finished and prepared, -2 - to remove
	public boolean IsClubEquipment, ccrDone, regDone, bcdDone, tankDone, isCCR, isReg, isBCD, isTank; //regDone..-To know if it was annualy/shotef isBCD.. to know if BCD is part of the order.
	private String custID, description, comments,date, name, lastName, clubOrPrivate, summary, kitChangeDate;
	public static Order currentOrder;
	public Customer customer;
	private static ArrayList<Order> unHandledOrderList;
	public String modelsToServer;//The serial numbers we send the server when we want to issue an order.
	
	public Order(int handled, String custID, String description, String comments, String date, int orderNum, String summary,int cost) {
		super();
		this.orderNum = orderNum;
		this.handled = handled;
		this.custID = custID;
		this.description = description;
		this.comments = comments;
		this.date = date;
		this.summary = summary;
		this.cost = cost;
	}
	public Order(int handled, String custID, String description, String comments, String date) {
		super();
		this.handled = handled;
		this.custID = custID;
		this.description = description;
		this.comments = comments;
		this.date = date;
	}
	
	public Order() {
	}

	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public int getHandled() {
		return handled;
	}
	public void setHandled(int handled) {
		this.handled = handled;
	}
	public String getCustID() {
		return custID;
	}
	public void setCustID(String custID) {
		this.custID = custID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		if(lastName != null)
			this.name +=" " + lastName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
		if(name!=null)
			name+=" " + lastName;
	}
	public static ArrayList<Order> getUnhandledOrderList() {
		return unHandledOrderList;
	}
	public static void setUnhandledOrderList(ArrayList<Order> unHandledOrderList) {
		Order.unHandledOrderList = unHandledOrderList;
	}
	public boolean isClubEquipment() {
		return IsClubEquipment;
	}
	
	public void setIsClubEquipment(boolean isClubEquipment, Order order) {
		IsClubEquipment = isClubEquipment;
		if(isClubEquipment)
			clubOrPrivate = "ציוד מועדון";
		else
			clubOrPrivate = "ציוד פרטי";
	}
	
	
	public String getClubOrPrivate() {
		return clubOrPrivate;
	}
	public void setClubOrPrivate(String clubOrPrivate) {
		this.clubOrPrivate = clubOrPrivate;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getKitChangeDate() {
		return kitChangeDate;
	}
	public void setKitChangeDate(String kitChangeDate) {
		this.kitChangeDate = kitChangeDate;
	}


	
	
	
}
