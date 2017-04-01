package controllers;

import entities.GeneralMethods;
import entities.Windows;
import entities.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.Main;

public class RegisterController {

	@FXML
	private TextField nameTextField, lastNameTextField, idTextField, passTextField, appPassTextField, emailTextField;
	@FXML
	private Text nameText, lastNameText, idText, passText, appPassText, emailText;
	@FXML
	private CheckBox techCheckBox, dalpakCheckBox, managerCheckBox;
	private int isManager=-1;
	private GeneralMethods GM ;
	public static Worker worker;

	public void initialize(){
		dalpakCheckBox.setSelected(true);
		GM = new GeneralMethods();
	}
/**
 * Closes the current register window.
 * @author orelzman
 */
	public void onBack(){
		GM.closePopup(Main.popup);
	}
	
/**
 * Checks if any of the text fields are empty as well as checks if the input is inviable to run within a query
 *  and if so colors it's text with red and returns.
 *  Also, onContinue checks wether the id exists in the system or not, and returns if it true.
 *  @author orelzman
 */
	public void onContinue(){
		GeneralMethods GM = new GeneralMethods();
		setBlack();
		boolean flag=false;//Empty field? passwords do not match?
		if(nameTextField.getText().equals("")){
			nameText.setFill(Color.RED);flag=true;
			}
		if(lastNameTextField.getText().equals("")){
			lastNameText.setFill(Color.RED);flag=true;
			}
		if(idTextField.getText().equals("")){
			idText.setFill(Color.RED);flag=true;
			}
		if(passTextField.getText().equals("")){
			passText.setFill(Color.RED);flag=true;
			}
		if(appPassTextField.getText().equals("")){
			appPassText.setFill(Color.RED);flag=true;
		}
		if(emailTextField.getText().equals("")){
			emailText.setFill(Color.RED);flag=true;
			}
		if(!appPassTextField.getText().equals(passTextField.getText())){
			Windows.warning("!סיסמאות לא תואמות");flag=true;
		}
		
		if(!checkInputs())//תקינות קלט
			flag=true;
		
		if(flag)//Forget to fill one of the fields
			return;
		
		setWorker();
		GM.sendServer(worker, "AddWorker");
		while(worker.actionNow.equals("AddWorker"))
			GM.Sleep(2);
		if(worker.actionNow.equals("IDExists"))
			return;
		else
			GM.closePopup(Main.popup);
		
	}

	/**
	 * Unticks the other checkboxes and updates isManager, which is -1 for dalpak, 0 for tech, 1 for manager.
	 * @author orelzman
	 */
	public void onTechCheckBox(){
		isManager=0;
		dalpakCheckBox.setSelected(false);
		managerCheckBox.setSelected(false);

	}
	
	/**
	 * Unticks the other checkboxes and updates isManager, which is -1 for dalpak, 0 for tech, 1 for manager.
	 * Also makes sure you want to create a manager user.
	 * @author orelzman
	 */
	public void onManagerCheckBox(){
		if(!Windows.yesNo("?האם אתה בטוח שברצונך ליצור משתמש מנהל", "מנהל משתמש")){
			managerCheckBox.setSelected(false);
			return;
		}
		isManager=1;
		dalpakCheckBox.setSelected(false);
		techCheckBox.setSelected(false);
	}

	/**
	 * Unticks the other checkboxes and updates isManager, which is -1 for dalpak, 0 for tech, 1 for manager.
	 * @author orelzman
	 */
	public void onDalpakCheckBox(){
		isManager=-1;
		managerCheckBox.setSelected(false);
		techCheckBox.setSelected(false);
	}

	/**
	 * Sets all the texts' color to black, for initialization.
	 * @author orelzman
	 */
	public void setBlack(){
		nameText.setFill(Color.BLACK);
		lastNameText.setFill(Color.BLACK); 
		idText.setFill(Color.BLACK);
		passText.setFill(Color.BLACK);
		appPassText.setFill(Color.BLACK);
		emailText.setFill(Color.BLACK);
	}
	
	/**
	 * Sets the current worker in a worker object in this class, for server checks purposes.
	 * @author orelzman
	 */
	public void setWorker(){
		worker = new Worker(idTextField.getText(), nameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), isManager);
		worker.setPassword(passTextField.getText());
	}
	
	/**
	 * Checks wether the inputs from the user are viable for use within an sql query.
	 * @return True if viable, false else
	 * @author orelzman
	 */
	public boolean checkInputs(){
		if((GM.checkText(nameTextField.getText()) && GM.checkText(lastNameTextField.getText()) && GM.checkText(emailTextField.getText()) &&
		 GM.checkText(idTextField.getText()) && GM.checkText(appPassTextField.getText()) && GM.checkText(passTextField.getText())))
			return true;
		return false;
	}
	
}
