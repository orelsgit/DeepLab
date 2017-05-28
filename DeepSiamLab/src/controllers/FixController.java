package controllers;

import entities.GeneralMethods;
import entities.Windows;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import main.Main;

public class FixController {
	
	private GeneralMethods GM =new GeneralMethods();
	@FXML
	private Text titleText;
	@FXML
	private TextArea fixTextArea;
	@FXML
	private TextField costTextField;

public void onBack(){
	Main.showMenu("OrderInfo");

}

public void onContinue(){
	String fixComments="";
	if(OrderInfoController.regCheck)
		 fixComments = "טיפול שוטף לוסת: " + "\n";
	
	if(OrderInfoController.bcdCheck)
		 fixComments = "טיפול שוטף למאזן: " + "\n";
	
	if(OrderInfoController.tankCheck)
		 fixComments = "טיפול שוטף למיכל: " + "\n";
	
	if(OrderInfoController.ccrCheck)
		 fixComments = "טיפול שוטף למערכת סגורה: " + "\n";
	
	fixComments+="הערות הטיפול: " + "\n" + fixTextArea.getText() + "\n";
	fixComments+="עלות הטיפול: " + costTextField.getText();
	Windows.message(fixComments, "בדיקה");
	//OrderInfoController.getOrderSelected().setSummary(OrderInfoController.getOrderSelected().getSummary()+fixComments);
	//OrderInfoController.getOrderSelected().setCost(OrderInfoController.getOrderSelected().getCost()+Integer.parseInt(costTextField.getText()));
	AnnualController.annualComments = fixComments;
	if(!costTextField.getText().equals("") && costTextField.getText().matches("[-+]?\\d*\\.?\\d+"))
		AnnualController.fixCost = Integer.parseInt(costTextField.getText());
	OrderInfoController.isAnnualDone = true;
	onBack();
}

}
