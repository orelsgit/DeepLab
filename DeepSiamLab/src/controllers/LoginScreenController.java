package controllers;

import entities.Constants;
import entities.Error;
import entities.GeneralMessage;
import entities.GeneralMethods;
import entities.Status;
import entities.Windows;
import entities.Worker;
import entities.Write;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
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
	@FXML
	ProgressIndicator loginProgressIndicator;
	private GeneralMethods GM;
	public static boolean isBackFromServer;




	
	/**
	 * Initializes key(ENTER) handlers for the id and password text fields and creates a thread that will get information from the server
	 * into arraylists to diminish the server accesses.
	 * @author orelzman
	 */
	public void initialize(){

		GM = new GeneralMethods();
		GeneralMessage.currentWindow = "LoginScreen";
		GeneralMessage.currentPopup = "";

		Write.getInstance().writeVersion(Constants.Version);
		
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

		GM.refresh(null);

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
		GM.sendServer(worker, "Login");

		Task<Void> task = new Task<Void>() {
			@Override protected Void call() throws Exception {
				for (int i=0; i<1; i++) {                
					Platform.runLater(new Runnable() {
						@Override 		
						public void run(){
							Error error = new Error("LoginScreenController", "onLogin", 0);
							int timesCalled = 1;
							isBackFromServer = false;
							while(Worker.getCurrentWorker()==null&&!isBackFromServer)
								if(!GM.Sleep(250, error, timesCalled++)){
									return;
								}
							if(Worker.getCurrentWorker() == null)
								return;
							if(Worker.getCurrentWorker().actionNow==null)
								return;//No such user
							Windows.message("ברוך שובך " + Worker.getCurrentWorker().getfName(), "Deepsiam Lab");
							if(Worker.getCurrentWorker().getIsManager().equals(Status.Dalpak))
								Main.showMenu("OpenCardScreen", "כרטיס חדש");
							else
								Main.showMenu("LoginWorkerScreen", "מסך ניווט");
						}
					});
				}
				return null;
			}
		};
		task.run();
	}



	/**
	 * This method is called in order to make sure the connection is closed, so the user can reuse the program safetly without problems.
	 * @author orelzman
	 */
	public void onClose(){
		System.exit(0);
	}




}
