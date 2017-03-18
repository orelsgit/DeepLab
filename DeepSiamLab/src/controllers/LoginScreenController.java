package controllers;

import java.io.IOException;

import entities.GeneralMethods;
import entities.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import main.Main;

public class LoginScreenController {
	@FXML
	private TextField idTextField, passTextField;
	@FXML
	private Pane mainScreenLayout;
	private GeneralMethods GM;



	public void initialize(){
		GM = new GeneralMethods();
		idTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER)  {
					onLogin();
				}
			}
		});
		passTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER)  {
					onLogin();
				}
			}
		});
	}





	/**
	 * This method creates a popup to require a manager password, in order to add a new worker.
	 */
	public void onRegister(){
		GM.getPopup(Main.popup, "ManagersPasswordScreen", "סיסמת מנהל");
	}


	/**
	 * This method is called upon pressing the login screen.
	 */
	public void  onLogin(){
		idTextField.setStyle("-fx-background-color: white;");
		passTextField.setStyle("-fx-background-color: white;");
		GeneralMethods GM = new GeneralMethods();

		if(!(GM.checkText(idTextField.getText()) && GM.checkText(passTextField.getText())))
			return;

		if(idTextField.getText().isEmpty())
			idTextField.setStyle("-fx-background-color: red;");
		if(passTextField.getText().isEmpty())
			passTextField.setStyle("-fx-background-color: red;");

		Worker worker = new Worker();
		worker.query = "SELECT * FROM orelDeepdivers.Workers WHERE ID = '" + idTextField.getText() + "' AND "
				+ "Password = '" + passTextField.getText() + "';";
		Worker.setCurrentWorker(null);

		Worker.setCurrentWorker(null);
		GM.sendServer(worker, "Login");
		while(Worker.getCurrentWorker()==null) GM.Sleep(2);
		if(Worker.getCurrentWorker().actionNow==null)
			return;
		Main.showMenu("LoginWorkerScreen");
	}



	/**
	 * This method is called in order to make sure the connection is closed, so the user can reuse the program safetly without problems.
	 * @author orelzman
	 */
	public void onClose(){
		System.exit(0);
	}




}
