package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import entities.Customer;
import entities.Error;
import entities.GeneralMessage;
import entities.GeneralMethods;
import entities.Regulator;
import entities.Windows;
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

	private static Customer customerSelected;
	private static GeneralMethods GM;
	public static boolean isBackFromServer,regCheck, bcdCheck, ccrCheck,tankCheck, isFixOrAnnual, regDone, bcdDone, ccrDone, tankDone;/*Avoid initializing while in phone window*/;
	public static boolean isGotEquipments = false, isAnnualDone = false, doOnce=false;
	public static Regulator regChosen;
	protected static int equipmentCnt = 0; // Counts how many equipments are needed to be taken care of, so we can close the order.
	private ArrayList<Integer> indexes = new ArrayList<>(); 
	private int ccr, bcd, reg, tank;


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
		
		System.out.println("equipmentCnt: " + equipmentCnt);

		
		if(!doOnce){
			checkCheckBox();
			getEquipmentInfo();
			LabOrdersController.orderSelected.setSummary("");
			LabOrdersController.orderSelected.setCost(0);
			GM = new GeneralMethods();
			doOnce=true;
		}

		if(isFixOrAnnual){
			isFixOrAnnual=false;
			return;
		}

		//Set fields
		nameText.setText(LabOrdersController.orderSelected.getName());
		descriptionTextArea.setText(LabOrdersController.orderSelected.getDescription());
		commentsTextArea.setText(LabOrdersController.orderSelected.getComments());
		//Set fields

		
		//Get indexes
		ccr = descriptionTextArea.getText().indexOf("CCR");
		bcd = descriptionTextArea.getText().indexOf("BCD");
		reg = descriptionTextArea.getText().indexOf("Regulator");
		tank = descriptionTextArea.getText().indexOf("Tank");
		indexes.add(reg);
		indexes.add(tank);
		indexes.add(bcd);
		indexes.add(ccr);
		Collections.sort(indexes);
		System.out.println("ccr: " + ccr + "bcd: " + bcd +  "reg: " + reg + "tank: " + tank);
		//Get indexes



		if(LabOrdersController.orderSelected.regDone&&reg!=-1){//reg will be -1 after we finish here.
			System.out.println("inside reg");
			--equipmentCnt;
			LabOrdersController.orderSelected.setSummary(LabOrdersController.orderSelected.getSummary()+AnnualController.annualComments);
			if(AnnualController.fixCost!=0){
				LabOrdersController.orderSelected.setCost(LabOrdersController.orderSelected.getCost()+AnnualController.fixCost);
				AnnualController.fixCost=0;
			}
			updateDescription(reg);
		}

		else if(LabOrdersController.orderSelected.tankDone&&tank!=-1){
			--equipmentCnt;
			LabOrdersController.orderSelected.setSummary(LabOrdersController.orderSelected.getSummary()+AnnualController.annualComments);
			if(AnnualController.fixCost!=0){
				LabOrdersController.orderSelected.setCost(LabOrdersController.orderSelected.getCost()+AnnualController.fixCost);
				AnnualController.fixCost=0;
			}
			updateDescription(tank);
		}

		else if(LabOrdersController.orderSelected.bcdDone&&bcd!=-1){
			--equipmentCnt;
			LabOrdersController.orderSelected.setSummary(LabOrdersController.orderSelected.getSummary()+AnnualController.annualComments);
			if(AnnualController.fixCost!=0){
				LabOrdersController.orderSelected.setCost(LabOrdersController.orderSelected.getCost()+AnnualController.fixCost);
				AnnualController.fixCost=0;
			}
			updateDescription(bcd);
		}


		else if(LabOrdersController.orderSelected.ccrDone&&ccr!=-1){
			--equipmentCnt;
			LabOrdersController.orderSelected.setSummary(LabOrdersController.orderSelected.getSummary()+AnnualController.annualComments);
			if(AnnualController.fixCost!=0){
				LabOrdersController.orderSelected.setCost(LabOrdersController.orderSelected.getCost() + AnnualController.fixCost);
				AnnualController.fixCost=0;
			}
			updateDescription(ccr);
		}



		if(equipmentCnt == 0){
			GM.sendServer(LabOrdersController.orderSelected, "OrderHandled");	
			Windows.message("הטיפול הושלם והכרטיס נסגר.", "טיפול הושלם");
			onBack();
			return;
		}


		falseChecks();
	}

	public void updateIndexes(){
		ccr = descriptionTextArea.getText().indexOf("CCR");
		bcd = descriptionTextArea.getText().indexOf("BCD");
		reg = descriptionTextArea.getText().indexOf("Regulator");
		tank = descriptionTextArea.getText().indexOf("Tank");
		indexes.add(reg);
		indexes.add(tank);
		indexes.add(bcd);
		indexes.add(ccr);
		Collections.sort(indexes);
		System.out.println("ccr: " + ccr + "bcd: " + bcd +  "reg: " + reg + "tank: " + tank);
	}
	
	public void updateDescription(int x){
		int i=0;

		for(;i<4;i++)
			if(indexes.get(i) == x)
				break;

		String newDes = "", oldDes = descriptionTextArea.getText();
		for(int j=0;j<x;++j)
			newDes+=oldDes.charAt(j);

		if(i!=3 && indexes.get(i++)!=-1)
			for(int j=indexes.get(i);j<oldDes.length();++j)
				newDes+=oldDes.charAt(j);
		System.out.println("NEW DES");
		descriptionTextArea.setText(newDes);
		LabOrdersController.orderSelected.setDescription(newDes);
		updateIndexes();
	}

	/**
	 * Gets the information about the unreviewed order that was selected earlier.
	 *  @author orelzman
	 */

	public void getEquipmentInfo(){
		Thread thread = new Thread(){
			public void run(){
				isGotEquipments = false;
				regChosen = new Regulator();
				String order = LabOrdersController.orderSelected.getDescription();
				int index;

				if(LabOrdersController.orderSelected.getDescription().contains("Regulator")){
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

					Error error = new Error("OrderInfoController", "getEquipmentInfo", 0);
					int timesCalled = 0;
					while(!isGotEquipments)
						if(!GM.Sleep(70, error, timesCalled++))
							return;
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


		if(LabOrdersController.orderSelected.getDescription().contains("BCD") && !bcdDone ){
			bcdButton.setVisible(true);
			LabOrdersController.orderSelected.isBCD = true;
			LabOrdersController.orderSelected.bcdDone=false;
		}
		if(LabOrdersController.orderSelected.getDescription().contains("Regulator") && !regDone){
			regButton.setVisible(true);
			LabOrdersController.orderSelected.isReg = true;
			LabOrdersController.orderSelected.regDone=false;
		}
		if(LabOrdersController.orderSelected.getDescription().contains("Tank") && !tankDone){
			tankButton.setVisible(true);
			LabOrdersController.orderSelected.isTank = true;
			LabOrdersController.orderSelected.tankDone=false;
		}
		if(LabOrdersController.orderSelected.getDescription().contains("CCR") && !ccrDone){
			ccrButton.setVisible(true);
			LabOrdersController.orderSelected.isCCR = true;
			LabOrdersController.orderSelected.ccrDone=false;
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
		customerSelected.setCustID(LabOrdersController.orderSelected.getCustID());
		GM.sendServerThread(customerSelected, "GetPhone");
		Thread thread = new Thread(){
			public void run(){

				Error error = new Error("OrderInfoController", "onPhone", 1);
				int timesCalled = 0;
				while(!isBackFromServer)
					if(!GM.Sleep(70, error, timesCalled++))
						return;

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

		Error error = new Error("OrderInfoController", "onAnnual", 2);
		int timesCalled = 0;
		while(!isGotEquipments)
			if(!GM.Sleep(70, error, timesCalled++))
				return;// DO NOT TRUST THE USER!

		isFixOrAnnual=false;
		GM.closePopup(Main.popup);
		Main.showMenu("Annual", "טיפול שנתי");
		//	GM.getPopup(Main.popup2, "Test", "בדיקה שוטפת", "popup2");




	}

	/**
	 * Opens the fix window and sets current popup string for the manager's password and more.
	 *  @author orelzman
	 */
	public void onFix(){

		Error error = new Error("OrderInfoController", "onFix", 3);
		int timesCalled = 0;
		while(!isGotEquipments)
			if(!GM.Sleep(70, error, timesCalled++))
				return;// DO NOT TRUST THE USER!

		isFixOrAnnual=false;
		GM.closePopup(Main.popup);
		Main.showMenu("Fix", "טיפול שוטף");


	}	

	/**
	 * Sets the regCheck flag as true, so the system will write the information into the right table.
	 *  @author orelzman
	 */
	public void onReg(){
		if(reg == -1)
			return;
		falseChecks();
		regCheck = true;
		isFixOrAnnual=true;
		GM.getPopup(Main.popup, "FixOrAnnual", "טיפול שוטף או טיפול שנתי", "popup");

	}

	/**
	 * Sets the bcdCheck flag as true, so the system will write the information into the right table.
	 *  @author orelzman
	 */
	public void onBCD(){
		if(bcd == -1)
			return;
		falseChecks();
		isFixOrAnnual=true;
		bcdCheck = true;
		GM.getPopup(Main.popup, "FixOrAnnual", "טיפול שוטף או טיפול שנתי", "popup");

	}

	/**
	 * Sets the ccrCheck flag as true, so the system will write the information into the right table.
	 *  @author orelzman
	 */
	public void onCCR(){
		if(ccr == -1)
			return;
		falseChecks();
		ccrCheck=true;
		isFixOrAnnual=true;
		GM.getPopup(Main.popup, "FixOrAnnual", "טיפול שוטף או טיפול שנתי", "popup");
	}


	/**
	 * Sets the tankCheck flag as true, so the system will write the information into the right table.
	 *  @author orelzman
	 */
	public void onTank(){
		if(tank == -1)
			return;
		falseChecks();
		isFixOrAnnual=true;
		tankCheck = true;
		GM.getPopup(Main.popup, "FixOrAnnual", "טיפול שוטף או טיפול שנתי", "popup");

	}


	///**
	// * Closes the phone window.
	// *  @author orelzman
	// */
	//	public void onBackPhone(){
	//		
	//		GM.closePopup(Main.popup);
	//	}

	/**
	 * Closes the LabOrders window and returns to the unreviewed orders window.
	 *  @author orelzman
	 */
	public void onBack(){
		if(equipmentCnt != 0)//Update w.e we did so far.			
			GM.sendServer(LabOrdersController.orderSelected, "UpdateOrder");

		Main.showMenu("LabOrders", "הזמנות");

	}

	/**
	 * Opens up the annual check window.
	 *  @author orelzman
	 */
	public void showAnnual(){
		try {
			TabPane mainLayout = FXMLLoader.load(Main.class.getResource("/GUI/Test.fxml"));
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
}

