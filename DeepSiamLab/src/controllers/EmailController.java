package controllers;

import entities.Error;
import entities.GeneralMethods;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import main.Main;

public class EmailController {

	@FXML
	private TextField toTextField, emailTextField, passwordTextField, subjectTextField;
	@FXML
	private TextArea msgTextArea;
	@FXML
	private CheckBox fromCheckBox;
	@FXML
	private Text passwordText;
	@FXML
	private ProgressBar emailProgressBar;
	@FXML
	private ProgressIndicator emailProgressIndicator;
	
	private GeneralMethods GM;
	public static String emailTo;
	
	private final String password = "00220022", emailFrom = "garbagedeepsiam@gmail.com";
	
	private DoubleProperty number;
	
	
	/**
	 * Sets the email send window components.
	 * @author orelzman
	 */
	public void initialize(){
		
		emailProgressBar.setVisible(false);
		emailProgressIndicator.setVisible(false);
		number = new SimpleDoubleProperty(0.0);//Number for the progress bars
		emailTextField.setStyle("-fx-background: #9A9A9A;");
		passwordTextField.setVisible(false);
		toTextField.setStyle("-fx-background #9A9A9A;");
		emailTextField.setEditable(false);
		passwordTextField.setEditable(false);
		passwordText.setVisible(false);
		
		GM = new GeneralMethods();
		emailTo = ".";
		
		GM.sendServerJoin(LabOrdersController.orderSelected, "GetMail");
		
		Error error = new Error("EmailController", "initialize", 0);
		int timesCalled = 0;
		while(emailTo.equals("."))
			if(!GM.Sleep(70, error, timesCalled++))
				return;
			

		toTextField.setText(emailTo);
		emailTextField.setText(emailFrom);
		passwordTextField.setText(password);
	}
	
	public void onFrom(){
		if(fromCheckBox.isSelected()){
			emailTextField.setStyle("-fx-background-color: white;");
			passwordTextField.setVisible(true);
			emailTextField.setEditable(true);
			passwordTextField.setEditable(true);
			emailTextField.setText("");
			passwordTextField.setText("");
			passwordText.setVisible(true);
		}else{
			emailTextField.setStyle("-fx-background-color: #9A9A9A;");
			passwordTextField.setVisible(false);
			emailTextField.setEditable(false);
			passwordTextField.setEditable(false);
			emailTextField.setText(emailFrom);
			passwordTextField.setText(password);
			passwordText.setVisible(false);
		}
	}
	/**
	 * Checks the validation of the written text and sends the message.
	 * @author orelzman
	 */
	public void onSend(){
		if(!GM.checkText(emailTextField.getText())&&GM.checkText(toTextField.getText().replaceAll("\\s+", ""))&&GM.checkText(passwordTextField.getText()))
			return;
		emailProgressBar.setVisible(true);
		emailProgressIndicator.setVisible(true);
		GM.sendMail(toTextField.getText().replaceAll("\\s+", ""), subjectTextField.getText(), msgTextArea.getText(), emailTextField.getText(), passwordTextField.getText());
		activateProgressBar();
	}
	
	/**
	 * Cleans the window, preparing it to send another email or the edit the last one and resend it.
	 * @author orelzman
	 */
	public void afterSend(){
		if(number.get()<1)
			return;
		emailProgressBar.setVisible(false);
		emailProgressIndicator.setVisible(false);
		number.set(0.0);
	}
	

	/**
	 * Activates a thread to run the progress bar/indicator.
	 * @author orelzman
	 */
	public void activateProgressBar(){
		Thread thread = new Thread(){
			public void run(){
				while(number.get()<1){
				try {Thread.sleep(60);} catch (InterruptedException e) {e.printStackTrace();}
				number.set(number.get()+0.03);
				emailProgressIndicator.progressProperty().bind(number);
				emailProgressBar.progressProperty().bind(number);
				}
			}
		};thread.start();

	}
	
	/**
	 * Closes the email window.
	 * @author orelzman
	 */
	public void onBack(){
		GM.closePopup(Main.popup);
	}
	
}
