package controllers;

import entities.*;
import entities.Error;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.Main;
/**
 * The class is used to assure some only-manager allows/approvals will be allowed/approved by the manager.(Replaces a paper signature)
 * @author orelzman
 *
 */
public class ManagersPasswordController {

	public static Worker worker;
	@FXML
	TextField passTextField;
	private GeneralMethods GM;


/**
 * Sets a key(ENTER) listener for the password.
 * @author orelzman
 */
	public void initialize(){
		GM = new GeneralMethods();
		worker = new Worker();
		passTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER)  {
					onContinue();
				}
			}
		});
	}

	/**
	 * Accesses the server and checks if the password that was inserted actually fits any of the manager's password and reacts according
	 * to the current window.
	 */
	public void onContinue(){

		if(passTextField.getText().equals(""))
			return;
		if(!(GM.checkText(passTextField.getText())))
			return;

		worker.setManagerPassword(passTextField.getText());
		GM.sendServer(worker, "ManagerPassword");
		
		Error error = new Error("ManagerPasswordController", "onContinue", 0);
		int timesCalled = 0;
		while(worker.actionNow.equals("ManagerPassword")&&GM.Sleep(70, error, timesCalled++));
			
		
		
		if(worker.actionNow.equals("Incorrect")){
			Windows.threadMessage("סיסמא שגויה! נסה שנית", "wrong pass");
			return;
		}
		switch(GeneralMessage.currentWindow){
		case "LoginScreen":
			GM.getPopup(Main.popup2, "Register", "רישום עובד", "popup2");break;
		case "AnnualScreen":
			Windows.message("הבדיקה אושרה עי המנהל", "סיום טיפול");AnnualController.isManagerApprove = true;GM.closePopup(Main.popup3);break;
		}


	}
/**
 * Closes the current window.
 * @author orelzman
 */
	public void onBack(){
		switch(GeneralMessage.currentWindow){
		case "LoginScreen":
		GM.closePopup(Main.popup);break;
		case "AnnualScreen":
			GM.closePopup(Main.popup3);break;
			
		}
	}


}
