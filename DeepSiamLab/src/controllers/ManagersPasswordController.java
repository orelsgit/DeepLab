package controllers;

import java.io.IOException;

import entities.GeneralMessage;
import entities.GeneralMethods;
import entities.Windows;
import entities.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import main.Client;
import main.Main;

public class ManagersPasswordController {

	public static Worker worker;
	@FXML
	TextField passTextField;
	private GeneralMethods GM;



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

	public void onContinue(){

		if(passTextField.getText().equals(""))
			return;
		worker.setManagerPassword(passTextField.getText());
		GM.sendServer(worker, "ManagerPassword");
		while(worker.actionNow.equals("ManagerPassword"))
			GM.Sleep(2);	
		if(worker.actionNow.equals("Incorrect")){
			Windows.warning("סיסמא שגויה! נסה שנית");
			return;
		}
		switch(GeneralMessage.currentWindow){
		case "LoginScreen":
			GM.setPopup(Main.popup, "Register", "רישום משתמש");break;
		case "AnnualScreen":
			Windows.message("הבדיקה אושרה עי המנהל ותועבר לארכיון לאחר לחיצת המשך בחלון הבא", "סיום טיפול");AnnualController.isManagerApprove = true;GM.closePopup(Main.popup3);break;
		}


	}

	public void onBack(){
		GM.closePopup(Main.popup);
	}


}
