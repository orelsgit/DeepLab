package controllers;

import java.io.IOException;

import entities.GeneralMessage;
import entities.GeneralMethods;
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
			GM.closePopup(Main.popup);
			return;
		}
		
		GM.setPopup(Main.popup, "Register", "רישום משתמש");
		
		
	}
	
	public void onBack(){
		GM.closePopup(Main.popup);
	}
	
	
}
