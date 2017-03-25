package controllers;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import entities.Customer;
import entities.GeneralMethods;
import entities.Order;
import entities.Windows;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.Main;

public class OpenCardController {


	@FXML
	private CheckBox regulatorCheckBox, bcdCheckBox, ccrCheckBox, tankCheckBox, deepCheckBox, privateCheckBox;
	@FXML
	private TextArea commentsTextArea;
	@FXML
	private TextField /*Change to Model*/regManuTextField, regDeepNumTextField, bcdModelTextField,bcdDeepNumTextField, tankDeepNumTextField, tankManuTextField,
	ccrOwnerTextField, ccrDeepNumTextField, idTextField;

	private GeneralMethods GM;

	private final  DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	private final Calendar calobj = Calendar.getInstance();
	public static boolean isBackFromSearch;
	
	public static Customer customerChosen;


	public void initialize(){
		idTextField.setEditable(false);
		GM = new GeneralMethods();
		commentsTextArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER)  {
					onIssueOrder();
				}
			}
		});
	}

	public OpenCardController(){
	}

	public void onSearch(){	
		isBackFromSearch=false;
		Thread thread = new Thread(){
			public void run(){
				while(!isBackFromSearch)
					GM.Sleep(500);
				isBackFromSearch=false;
				idTextField.setText(customerChosen.getCustID());			
			}
		};thread.start();
		GM.getPopup(Main.popup, "CustomerSearch", "סיסמת מנהל");	
	}

	/**
	 * This method removes/changes/adds fields to the order window, depending on wether it's private equipment or deep's
	 */
	public void onDeepSelection(){
		privateCheckBox.setSelected(false);
		regDeepNumTextField.setPromptText("Deep Number");
		tankDeepNumTextField.setPromptText("Deep Number");
		ccrDeepNumTextField.setPromptText("Deep Number");
		bcdDeepNumTextField.setVisible(true);

	}
	/**
	 * This method removes/changes/adds fields to the order window, depending on wether it's private equipment or deep's
	 */
	public void onPrivateSelection(){
		deepCheckBox.setSelected(false);
		regDeepNumTextField.setPromptText("Serial Number");
		tankDeepNumTextField.setPromptText("Serial Number");
		ccrDeepNumTextField.setPromptText("Serial Number");
		bcdDeepNumTextField.setVisible(false);
	}

	/**
	 * This method checks the order fields and issues an order by writing the information into the description String
	 * which will later be shown to the tech
	 */
	public void onIssueOrder(){
		String description="";//This String will be shown to the tech when he opens the ticket.
		if(privateCheckBox.isSelected()){//Private Equipment
			if(regulatorCheckBox.isSelected())
				description+="Regulator:\n -Serial Num: " + regDeepNumTextField.getText() + "\n -Model: " + regManuTextField.getText() + "\n";
		
			if(bcdCheckBox.isSelected())
				description+="BCD:\n -Model: " + bcdModelTextField.getText() + "\n";
			
			if(tankCheckBox.isSelected())
				description+="Tank:\n -Serial Num: " + tankDeepNumTextField.getText() + "\n -Manufacturer: " + tankManuTextField.getText() + "\n";
			
			if(ccrCheckBox.isSelected())
			description+="CCR:\n -Owner: "+ ccrOwnerTextField.getText() + "\n -Serial Number: " + ccrDeepNumTextField.getText() + "\n";
			
			if(idTextField.getText().equals("")){
				Windows.warning("You forget the customer's id!");
				return;
			}

		}else if(deepCheckBox.isSelected()){//Deep Equipment
			if(regulatorCheckBox.isSelected())
				description+="Regulator:\n -Deep Number: " + regDeepNumTextField.getText() + "\n -Model: " + regManuTextField.getText() + "\n";
			if(bcdCheckBox.isSelected())
				description+="BCD:\n -Model: " + bcdModelTextField.getText() + "\n";
			if(tankCheckBox.isSelected())
				description+="Tank:\n -Deep Number: " + tankDeepNumTextField.getText() + "\n -Manufacturer: " + tankManuTextField.getText() + "\n";
			if(ccrCheckBox.isSelected())
				description+="CCR:\n -Owner: "+ ccrOwnerTextField.getText() + "\n -Serial Number: " + ccrDeepNumTextField.getText() + "\n";
		}

		if(!(deepCheckBox.isSelected()||privateCheckBox.isSelected())){
			Windows.threadWarning("Tick the private/deep equipment before you proceed.");return;
		}
		if(!(ccrCheckBox.isSelected()&&tankCheckBox.isSelected()&&bcdCheckBox.isSelected()&&regulatorCheckBox.isSelected()))//Nothing is ticked
			if(Windows.yesNo("Are you sure you want nothing ticked?", "Sure?")==1)
				return;
			else if(Windows.yesNo("Are you sure that you've TICKED everything needed?", "Be completely sure!")==1)
				return;
		
		Order order = new Order(-1, idTextField.getText(), description,commentsTextArea.getText(), df.format(calobj.getTime()));
		Order.currentOrder = new Order();
		Order.currentOrder.actionNow="IssueOrder";
		
		GM.sendServer(order, "IssueOrder");
		while(Order.currentOrder.actionNow.equals("IssueOrder"))
			GM.Sleep(2);
		Windows.threadMessage("Order has been issued and will be soon reviewed by the tech.", "ORDER DISPATCHED!");

	}


	/**
	 * This method returns the user to the main screen
	 * @author orelzman
	 */
	public void onBack(){
		Main.showMenu("LoginWorkerScreen");
	}



}
