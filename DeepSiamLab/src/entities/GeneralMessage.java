package entities;

import java.io.Serializable;
import java.util.ArrayList;

public class GeneralMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3047749912580360585L;
	
	public String actionNow, query;
	public static String currentWindow, currentPopup;
	public static ArrayList<Regulator> regList;
	public static ArrayList<BCD> bcdList;
	public static ArrayList<CCR> ccrList;
	public static ArrayList<Tank> tankList;

}
