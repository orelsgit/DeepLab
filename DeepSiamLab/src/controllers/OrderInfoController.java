package controllers;

import java.io.IOException;

import entities.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import main.Main;

public class OrderInfoController {

	private static Order orderSelected;
	private static Customer customerSelected;
	private static GeneralMethods GM;
	public static boolean isBackFromServer,regCheck, bcdCheck, ccrCheck,tankCheck, isFixOrAnnual;/*Avoid initializing while in phone window*/;
	public static boolean isGotEquipments = false;
	public static Regulator regChosen;


	@FXML
	private Text nameText, phoneNameText;
	@FXML
	private TextArea descriptionTextArea, commentsTextArea;
	@FXML
	private TextField phoneTextField;
	@FXML
	private Button regButton, bcdButton, tankButton, ccrButton;




	/**
	 * Initializes the window's text fields with information from the chosen unreviewed order.
	 *  @author orelzman
	 */
	public void initialize(){
		if(isFixOrAnnual)
			return;
		orderSelected = LabOrdersController.orderSelected;
		getEquipmentInfo();
		falseChecks();
		GM = new GeneralMethods();
		nameText.setText(orderSelected.getName());
		descriptionTextArea.setText(orderSelected.getDescription());
		commentsTextArea.setText(orderSelected.getComments());
		GM.Sleep(10);//Not sure, but if I write checkCheckBox earlier, it throws an exception
		checkCheckBox();
	}

	/**
	 * Gets the information about the unreviewed order that was selected ealier.
	 *  @author orelzman
	 */

	public void getEquipmentInfo(){
		Thread thread = new Thread(){
			public void run(){
				isGotEquipments = false;
				regChosen = new Regulator();
				String order = orderSelected.getDescription();
				int index;

				if(order.contains("BCD")){
					/*BCD bcd = new BCD();
					index = order.indexOf("BCD");
					GM.sendServer(bcd, "GetBCD");*/
				}

				if(orderSelected.getDescription().contains("Regulator")){
					Regulator reg = new Regulator();
					String regModel="";
					index = order.indexOf("Model", order.indexOf("Regulator"));
					if(index==-1)
						return;
					index+=7;//To get to the first index of the actual model
					while(order.charAt(index)!='\n')
						regModel+=order.charAt(index++);
					reg.setModel(regModel);
					GM.sendServer(reg, "FindInterPressure");
					while(!isGotEquipments)
						GM.Sleep(2);
					if(regChosen.actionNow.equals("InterNotFound"))
						Windows.warning("!מודל הוסט לא נמצא");
					else
						regChosen.setModel(reg.getModel());

				}

				if(orderSelected.getDescription().contains("Tank")){

				}

				if(orderSelected.getDescription().contains("CCR")){

				}


				isGotEquipments=true;
			}
		};thread.start();


	}



	/**
	 *  Puts false in the checks boolean attributes, in order to make sure the right window will show up
	 *   @author orelzman
	 */

	public void falseChecks(){
		regCheck=false;
		bcdCheck=false;
		ccrCheck=false;
		tankCheck=false;
		isBackFromServer=false;
		isFixOrAnnual=false;
	}
	/**
	 * Checks which button was pressed and sets the check boolean attributes accordingly
	 *  @author orelzman
	 */

	public void checkCheckBox(){
		bcdButton.setVisible(false);
		tankButton.setVisible(false);
		ccrButton.setVisible(false);
		regButton.setVisible(false);
		if(orderSelected.getDescription().contains("BCD"))
			bcdButton.setVisible(true);
		if(orderSelected.getDescription().contains("Regulator"))
			regButton.setVisible(true);
		if(orderSelected.getDescription().contains("Tank"))
			tankButton.setVisible(true);
		if(orderSelected.getDescription().contains("CCR")){
			ccrButton.setVisible(true);
		}
	}




	/**
	 * Accesses the server and pulls the phone number of the customer's card.
	 * @author orelzman
	 */
	public void onPhone(){
		if((phoneTextField.getText())!=null&&!phoneTextField.getText().equals(""))
			return;
		customerSelected = new Customer();
		customerSelected.setCustID(orderSelected.getCustID());
		GM.sendServerThread(customerSelected, "GetPhone");
		Thread thread = new Thread(){
			public void run(){
				while(!isBackFromServer)
					GM.Sleep(2);
				phoneTextField.setText(customerSelected.getPhone());
			}
		};thread.start();
	}

	/**
	 * Opens the mail window, to send the customer an email.
	 * @author orelzman
	 */
	public void onMail(){
		GM.getPopup(Main.popup, "Email", "שלח אימייל", "popup");
	}


	/**
	 * Opens the annual check window and sets current popup string for the manager's password and more.
	 *  @author orelzman
	 */
	public void onAnnual(){
		while(!isGotEquipments)// DO NOT TRUST THE USER!
			GM.Sleep(2);
		isFixOrAnnual=false;
		showAnnual();
		GM.closePopup(Main.popup);
	}

	/**
	 * Opens the fix window and sets current popup string for the manager's password and more.
	 *  @author orelzman
	 */
	public void onFix(){
		while(!isGotEquipments)// DO NOT TRUST THE USER!
			GM.Sleep(2);
		isFixOrAnnual=false;
		GM.getPopup(Main.popup2, "Fix", "תיקון וסת", "popup2");
		GM.closePopup(Main.popup);

	}	

	/**
	 * Sets the regCheck flag as true, so the system will write the information into the right table.
	 *  @author orelzman
	 */
	public void onReg(){
		falseChecks();
		regCheck = true;
		isFixOrAnnual=true;
		GM.getPopup(Main.popup, "FixOrAnnual", "FixOrAnnual", "popup");
	}

	/**
	 * Sets the bcdCheck flag as true, so the system will write the information into the right table.
	 *  @author orelzman
	 */
	public void onBCD(){
		falseChecks();
		isFixOrAnnual=true;
		bcdCheck = true;
		GM.getPopup(Main.popup, "FixOrAnnual", "FixOrAnnual", "popup");
	}

	/**
	 * Sets the ccrCheck flag as true, so the system will write the information into the right table.
	 *  @author orelzman
	 */
	public void onCCR(){
		falseChecks();
		ccrCheck=true;
		isFixOrAnnual=true;
		GM.getPopup(Main.popup, "FixOrAnnual", "FixOrAnnual", "popup");
	}
	
	
	/**
	 * Sets the tankCheck flag as true, so the system will write the information into the right table.
	 *  @author orelzman
	 */
	public void onTank(){
		falseChecks();
		isFixOrAnnual=true;
		tankCheck = true;
		GM.getPopup(Main.popup, "FixOrAnnual", "FixOrAnnual", "popup");

	}


/**
 * Closes the phone window.
 *  @author orelzman
 */
	public void onBackPhone(){
		GM.closePopup(Main.popup);
	}
	
	/**
	 * Closes the LabOrders window and returns to the unreviewed orders window.
	 *  @author orelzman
	 */
	public void onBack(){
		Main.showMenu("LabOrders");
		GeneralMessage.currentPopup = "";
	}

	/**
	 * Opens up the annual check window.
	 *  @author orelzman
	 */
	public void showAnnual(){
		try {
			TabPane mainLayout = FXMLLoader.load(Main.class.getResource("/GUI/Annual.fxml"));
			Main.popup2.setScene(new Scene(mainLayout));
			Main.popup2.setTitle("Annual");
			Main.popup2.showAndWait();
		} catch (IOException e) {e.printStackTrace();}
		GeneralMessage.currentPopup = "popup2";
	}


	public static Customer getCustomerSelected() {
		return customerSelected;
	}

	public static void setCustomerSelected(Customer customerSelected) {
		OrderInfoController.customerSelected = customerSelected;
	}
	public static Order getOrderSelected() {
		return orderSelected;
	}


	public static void setOrderSelected(Order orderSelected) {
		OrderInfoController.orderSelected = orderSelected;
	}
}

