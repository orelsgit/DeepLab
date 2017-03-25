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
	public static boolean isBackFromServer=false,regCheck, bcdCheck, ccrCheck,tankCheck, isFixOrAnnual=false;//is it the phone window
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





	public void initialize(){
		if(isFixOrAnnual)
			return;
		getEquipmentInfo();
		falseChecks();
		GM = new GeneralMethods();
		orderSelected = LabOrdersController.orderSelected;
		nameText.setText(orderSelected.getName());
		descriptionTextArea.setText(orderSelected.getDescription());
		commentsTextArea.setText(orderSelected.getComments());
		GM.Sleep(10);//Not sure, but if I write checkCheckBox earlier, it throws an exception
		checkCheckBox();
	}

	/**
	 * Gets, according to the description, information about the equipments chosen by the dalpak from the server.
	 */

	public void getEquipmentInfo(){
		Thread thread = new Thread(){
			public void run(){
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

				System.out.println("done");

				isGotEquipments=true;
			}
		};thread.start();

	}



	/**
	 *  Puts false in the checks boolean attributes, in order to make sure the right window will show up
	 */

	public void falseChecks(){
		regCheck=false;
		bcdCheck=false;
		ccrCheck=false;
		tankCheck=false;
	}
	/**
	 * Checks which button was pressed and sets the check boolean attributes accordingly
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



	public static Customer getCustomerSelected() {
		return customerSelected;
	}

	public static void setCustomerSelected(Customer customerSelected) {
		OrderInfoController.customerSelected = customerSelected;
	}

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

	public void onMail(){
		GM.getPopup(Main.popup2, "Email", "שלח אימייל");
	}

	public static Order getOrderSelected() {
		return orderSelected;
	}


	public static void setOrderSelected(Order orderSelected) {
		OrderInfoController.orderSelected = orderSelected;
	}

	public void onAnnual(){
		while(!isGotEquipments)// DO NOT TRUST THE USER!
			GM.Sleep(2);
		isFixOrAnnual=false;
		showAnnual();
		GeneralMessage.currentPopup = "popup2";
		GM.closePopup(Main.popup);
	}

	public void onFix(){
		while(!isGotEquipments)// DO NOT TRUST THE USER!
			GM.Sleep(2);
		isFixOrAnnual=false;
		GM.getPopup(Main.popup2, "Fix", "תיקון וסת");
		GeneralMessage.currentPopup = "popup2";
		GM.closePopup(Main.popup);

	}	

	public void onReg(){
		falseChecks();
		regCheck = true;
		isFixOrAnnual=true;
		GeneralMessage.currentPopup = "popup2";
		GM.getPopup(Main.popup, "FixOrAnnual", "FixOrAnnual");


	}

	public void onBCD(){
		falseChecks();
		isFixOrAnnual=true;
		bcdCheck = true;
		GeneralMessage.currentPopup = "popup2";
		GM.getPopup(Main.popup, "FixOrAnnual", "FixOrAnnual");


	}

	public void onCCR(){
		falseChecks();
		ccrCheck=true;
		isFixOrAnnual=true;
		GeneralMessage.currentPopup = "popup2";
		GM.getPopup(Main.popup, "FixOrAnnual", "FixOrAnnual");

	}

	public void onTank(){
		falseChecks();
		isFixOrAnnual=true;
		tankCheck = true;
		GeneralMessage.currentPopup = "popup2";
		GM.getPopup(Main.popup, "FixOrAnnual", "FixOrAnnual");

	}



	public void onBackPhone(){
		GM.closePopup(Main.popup2);
	}
	public void onBack(){
		Main.showMenu("LabOrders");
		GeneralMessage.currentPopup = "";
	}

	public void showAnnual(){
		try {
			TabPane mainLayout = FXMLLoader.load(Main.class.getResource("/GUI/Annual.fxml"));
			Main.popup2.setScene(new Scene(mainLayout));
			Main.popup2.setTitle("Annual");
			Main.popup2.showAndWait();
		} catch (IOException e) {e.printStackTrace();}
		GeneralMessage.currentPopup = "popup2";
	}

}
