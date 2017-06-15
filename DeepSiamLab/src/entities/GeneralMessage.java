package entities;

import java.io.Serializable;
import java.util.ArrayList;

public class GeneralMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3047749912580360585L;
	
	private Files file=new Files();
	public String actionNow, query;
	private String message;
	public static String currentWindow, currentPopup;
	private static ArrayList<Regulator> regList = new ArrayList<Regulator>();
	private static ArrayList<BCD> bcdList = new ArrayList<BCD>();
	private static ArrayList<CCR> ccrList = new ArrayList<CCR>();
	private static ArrayList<Tank> tankList = new ArrayList<Tank>();
	private static ArrayList<Customer> custList = new ArrayList<Customer>();
	private static ArrayList<Order> unhandledOrders = new ArrayList<Order>();
	private static boolean gotLists = false;
	public static boolean regDone, ccrDone, tankDone, bcdDone;
	
	public GeneralMessage(String actionNow){
		this.actionNow = actionNow;
	}
	
	public GeneralMessage(){}
	
	public static ArrayList<Regulator> getRegList() {
		return regList;
	}
	public static void setRegList(ArrayList<Regulator> regList) {
		GeneralMessage.regList = regList;
	}
	public static ArrayList<BCD> getBcdList() {
		return bcdList;
	}
	public static void setBcdList(ArrayList<BCD> bcdList) {
		GeneralMessage.bcdList = bcdList;
	}
	public static ArrayList<CCR> getCcrList() {
		return ccrList;
	}
	public static void setCcrList(ArrayList<CCR> ccrList) {
		GeneralMessage.ccrList = ccrList;
	}
	public static ArrayList<Tank> getTankList() {
		return tankList;
	}
	public static void setTankList(ArrayList<Tank> tankList) {
		GeneralMessage.tankList = tankList;
	}
	public static ArrayList<Customer> getCustList() {
		return custList;
	}
	public static void setCustList(ArrayList<Customer> custList) {
		GeneralMessage.custList = custList;
	}
	public static boolean isGotLists() {
		return gotLists;
	}
	public static void setGotLists(boolean gotLists) {
		GeneralMessage.gotLists = gotLists;
	}
	public static boolean getGotLists(){
		return gotLists;
	}
	public  String getMessage() {
		return message;
	}
	public  void setMessage(String message) {
		this.message = message;
	}
	public  void addMessage(String message){
		if(message == null)
			message = "";
		this.message+=message;
	}

	public static ArrayList<Order> getUnhandledOrders() {
		return unhandledOrders;
	}

	public static void setUnhandledOrders(ArrayList<Order> unhandledOrders) {
		GeneralMessage.unhandledOrders = unhandledOrders;
	}

	public Files getFiles() {
		return file;
	}

	public void setFiles(Files file) {
		this.file = file;
	}

}
