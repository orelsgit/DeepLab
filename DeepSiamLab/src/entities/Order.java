package entities;

import java.util.ArrayList;

public class Order extends GeneralMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int orderNum, handled;//HANDLED :  -1 - Not handled, 0 - Not approved by manager, 1-finished and prepared, -2 - to remove
	private boolean IsClubEquipment;
	private String custID, description, Comments,date, name, lastName;
	public static Order currentOrder;
	private static ArrayList<Order> unHandledOrderList;
	public String modelsToServer;//The serial numbers we send the server when we want to issue an order.
	
	public Order(int handled, String custID, String description, String comments, String date, int orderNum) {
		super();
		this.orderNum = orderNum;
		this.handled = handled;
		this.custID = custID;
		this.description = description;
		Comments = comments;
		this.date = date;
	}
	public Order(int handled, String custID, String description, String comments, String date) {
		super();
		this.handled = handled;
		this.custID = custID;
		this.description = description;
		Comments = comments;
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
		return Comments;
	}
	public void setComments(String comments) {
		Comments = comments;
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
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public static ArrayList<Order> getUnhandledOrderList() {
		return unHandledOrderList;
	}
	public static void setUnhandledOrderList(ArrayList<Order> unHandledOrderList) {
		Order.unHandledOrderList = unHandledOrderList;
	}
	public boolean isIsClubEquipment() {
		return IsClubEquipment;
	}
	public void setIsClubEquipment(boolean isClubEquipment) {
		IsClubEquipment = isClubEquipment;
	}


	
	
	
}
