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

	public void onBack(){
		GM.closePopup(Main.popup);
	}

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

	public void onTechCheckBox(){
		isManager=0;
		dalpakCheckBox.setSelected(false);
		managerCheckBox.setSelected(false);

	}

	public void onManagerCheckBox(){
		if(Windows.yesNo("?האם אתה בטוח שברצונך ליצור משתמש מנהל", "מנהל משתמש")==1){//false
			managerCheckBox.setSelected(false);
			return;
		}
		isManager=1;
		dalpakCheckBox.setSelected(false);
		techCheckBox.setSelected(false);
	}

	public void onDalpakCheckBox(){
		isManager=-1;
		managerCheckBox.setSelected(false);
		techCheckBox.setSelected(false);
	}

	public void setBlack(){
		nameText.setFill(Color.BLACK);
		lastNameText.setFill(Color.BLACK); 
		idText.setFill(Color.BLACK);
		passText.setFill(Color.BLACK);
		appPassText.setFill(Color.BLACK);
		emailText.setFill(Color.BLACK);
	}
	
	public void setWorker(){
		worker = new Worker(idTextField.getText(), nameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), isManager);
		worker.setPassword(passTextField.getText());
	}
	
	public boolean checkInputs(){
		if((GM.checkText(nameTextField.getText()) && GM.checkText(lastNameTextField.getText()) && GM.checkText(emailTextField.getText()) &&
		 GM.checkText(idTextField.getText()) && GM.checkText(appPassTextField.getText()) && GM.checkText(passTextField.getText())))
			return true;
		return false;
	}
	
}
