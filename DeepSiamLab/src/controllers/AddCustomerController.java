package controllers;

import entities.Customer;
import entities.Error;
import entities.GeneralMessage;
import entities.GeneralMethods;
import entities.Windows;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.Main;

public class AddCustomerController {

	@FXML
	private TextField nameTextField, lastNameTextField, phoneTextField, emailTextField, idTextField, dobTextField;
	@FXML
	private Text nameText, lastNameText, phoneText, emailText, idText, dobText;

	GeneralMethods GM = new GeneralMethods();
	public static boolean isBackFromServer = false;

	public AddCustomerController(){

	}

	public void initialize(){
	}

	/**
	 * Checks if is there any empty field. If there is, it's text will be colored red and will halt the registeration.
	 * If nothing is empty, a new customer will be added to the database.
	 * @author orelzman
	 */
	public void onRegister(){
		textToBlack();
		boolean isAllFilled = true;
		if(nameTextField.getText().equals("")||!(GM.checkText(nameTextField.getText()))){
			nameText.setFill(Color.RED);isAllFilled=false;
		}
		if(lastNameTextField.getText().equals("")||!GM.checkText(lastNameTextField.getText())){
			lastNameText.setFill(Color.RED);isAllFilled=false;
		}
		if(phoneTextField.getText().equals("")||!GM.checkText(phoneTextField.getText())){
			phoneText.setFill(Color.RED);isAllFilled=false;
		}
		if(emailTextField.getText().equals("")||!GM.checkText(emailTextField.getText())){
			emailText.setFill(Color.RED);isAllFilled=false;
		}
		if(idTextField.getText().equals("")||!GM.checkText(nameTextField.getText())){
			idText.setFill(Color.RED);isAllFilled=false;
		}
		if(dobTextField.getText().equals("")){
			dobText.setFill(Color.RED);isAllFilled=false;
		}
		if(!isAllFilled)
			return;
		Customer customer = new Customer(nameTextField.getText(), lastNameTextField.getText(), phoneTextField.getText(),
				emailTextField.getText(), idTextField.getText(), dobTextField.getText());
		isBackFromServer=false;
		GM.sendServer(customer, "NewCustomer");
		
		Error error = new Error("AddCustomerController", "onRegister", 0);
		int timesCalled = 0;
		while(!isBackFromServer)
			if(!GM.Sleep(70, error, timesCalled++))
				return;
		
			
		
		Windows.message("הלקוח הוסף בהצלחה", "לקוח חדש");
	}
/**
 * Returns to the window that had this new customer registeration opened.
 * @author orelzman
 */
	public void onBack(){
		if(!GeneralMessage.currentPopup.equals("")){
			switch(GeneralMessage.currentPopup){
			case "popup":
				GM.closePopup(Main.popup);break;
			case "popup2":
				GM.closePopup(Main.popup2);break;
			case "popup3":
				GM.closePopup(Main.popup3);break;
			}
		}
		else if(!GeneralMessage.currentWindow.equals(""))
			Main.showMenu(GeneralMessage.currentWindow);
	}
	
	/**
	 * Sets the texts to black, for initialization.
	 * @author orelzman
	 */

	public void textToBlack(){
		nameText.setFill(Color.BLACK);
		lastNameText.setFill(Color.BLACK);
		phoneText.setFill(Color.BLACK);
		emailText.setFill(Color.BLACK);
		idText.setFill(Color.BLACK);
		dobText.setFill(Color.BLACK);
	}
}
