package controllers;

import entities.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
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


/**
 * Initializes key(ENTER) handlers for the id and password text fields and creates a thread that will get information from the server
 * into arraylists to diminish the server accesses.
 * @author orelzman
 */
	public void initialize(){

		GM = new GeneralMethods();
		GeneralMessage.currentWindow = "LoginScreen";
		GeneralMessage.currentPopup = "";
		
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

		Thread thread = new Thread(){
			public void run(){
				Order order = new Order();
				GM.sendServer(order, "GetNewOrders");
				GM.sendServer(order, "GetReg");
				GM.sendServer(order, "GetBCD");
				GM.sendServer(order, "GetTank");
				GM.sendServer(order, "GetCCR");
				GM.sendServer(order, "GetCustomers");
				while(GeneralMessage.getBcdList()!=null
						&&GeneralMessage.getCcrList()!=null
						&&GeneralMessage.getCustList()!=null
						&&GeneralMessage.getRegList()!=null
						&&GeneralMessage.getTankList()!=null)
					GM.Sleep(2);
				GeneralMessage.setGotLists(true);

			}
		};thread.start();

	}





	/**
	 * Creates a popup to require a manager password, in order to add a new worker.
	 * @author orelzman
	 */
	public void onRegister(){
		GM.getPopup(Main.popup, "ManagersPasswordScreen", "סיסמת מנהל", "popup");
	}


	/**
	 * Called upon pressing the login screen.
	 * @author orelzman
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

		Task<Void> task = new Task<Void>() {
			@Override protected Void call() throws Exception {
				for (int i=0; i<1; i++) {                
					Platform.runLater(new Runnable() {
						@Override 		
						public void run(){
							while(Worker.getCurrentWorker()==null)
								GM.Sleep(2);
							if(Worker.getCurrentWorker().actionNow==null)
								return;
							Windows.message("Welcome back " + Worker.getCurrentWorker().getfName(), "Deepsiam Lab");
							Main.showMenu("LoginWorkerScreen");
						}
					});
				}
				return null;
			}
		};
		task.run();
		while(!GeneralMessage.getGotLists())
			GM.Sleep(100);//לעשות פה בר טעינה או משהו
	}



	/**
	 * This method is called in order to make sure the connection is closed, so the user can reuse the program safetly without problems.
	 * @author orelzman
	 */
	public void onClose(){
		System.exit(0);
	}




}
