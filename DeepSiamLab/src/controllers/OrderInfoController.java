package controllers;

import entities.Customer;
import entities.GeneralMethods;
import entities.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;
import main.Main;

public class OrderInfoController {

	private static Order orderSelected;
	private static Customer customerSelected;
	private static GeneralMethods GM;
	public static boolean isBackFromServer=false;;//is it the phone window

	@FXML
	private Text nameText, phoneNameText;
	@FXML
	private TextArea descriptionTextArea, commentsTextArea;
	@FXML
	private TextField phoneTextField;
	@FXML
	private ToggleButton fixToggleButton, annualToggleButton;
	@FXML
	private Button regButton, bcdButton, tankButton, ccrButton;

	public static boolean regCheck, bcdCheck, ccrCheck,tankCheck;//According to these attributes the fixing windows will be made




	public void initialize(){
		regCheck=false;
		bcdCheck=false;
		ccrCheck=false;
		tankCheck=false;
		GM = new GeneralMethods();
		orderSelected = LabOrdersController.orderSelected;
		nameText.setText(orderSelected.getName());
		descriptionTextArea.setText(orderSelected.getDescription());
		commentsTextArea.setText(orderSelected.getComments());

	}

	public void checkCheckBox(){
		bcdButton.setVisible(false);
		tankButton.setVisible(false);
		ccrButton.setVisible(false);
		regButton.setVisible(false);
		if(orderSelected.getDescription().contains("BCD"))
			bcdButton.setVisible(true);
		if(orderSelected.getDescription().contains("Regulator"))
			regButton.setVisible(true);
		if(orderSelected.getDescription().contains("Tank"))
			tankButton.setVisible(true);
		if(orderSelected.getDescription().contains("CCR"))
			ccrButton.setVisible(true);
	}



	public static Customer getCustomerSelected() {
		return customerSelected;
	}

	public static void setCustomerSelected(Customer customerSelected) {
		OrderInfoController.customerSelected = customerSelected;
	}

	public void onPhone(){
		customerSelected = new Customer();
		customerSelected.setCustID(orderSelected.getCustID());
		GM.sendServerThread(customerSelected, "GetPhone");
		Thread thread = new Thread(){
			public void run(){
				while(!isBackFromServer)
					GM.Sleep(2);
				phoneTextField.setText(customerSelected.getPhone());
			}
		};thread.start();
	}
	public void onMail(){
		GM.getPopup(Main.popup2, "Email", "שלח אימייל");
	}

	public static Order getOrderSelected() {
		return orderSelected;
	}


	public static void setOrderSelected(Order orderSelected) {
		OrderInfoController.orderSelected = orderSelected;
	}

	public void onAnnual(){
		if(fixToggleButton.isSelected())
			fixToggleButton.setSelected(false);
	}

	public void onFix(){
		if(annualToggleButton.isSelected())
			annualToggleButton.setSelected(false);
	}	

	public void onReg(){
		if(fixToggleButton.isSelected()){
			GM.getPopup(Main.popup2, "Fix", "תיקון וסת");
			return;
		}
		else if(annualToggleButton.isSelected()){
			return;
		}

	}

	public void onBCD(){
		if(fixToggleButton.isSelected()){
			GM.getPopup(Main.popup2, "Fix", "תיקון כמאזן");
			return;
		}
		else if(annualToggleButton.isSelected()){
			return;
		}
	}

	public void onCCR(){
		if(fixToggleButton.isSelected()){
			GM.getPopup(Main.popup2, "Fix", "תיקון מערכת סגורה");
			return;
		}
		else if(annualToggleButton.isSelected()){
			return;
		}
	}

	public void onTank(){
		if(fixToggleButton.isSelected()){
			GM.getPopup(Main.popup2, "Fix", "תיקון מיכל");
			return;
		}
		else if(annualToggleButton.isSelected()){
			return;
		}
	}



	public void onBackPhone(){
		GM.closePopup(Main.popup2);
	}
	public void onBack(){
		Main.showMenu("LabOrders");
	}

}
