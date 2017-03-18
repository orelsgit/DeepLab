package controllers;

import entities.Customer;
import entities.GeneralMethods;
import entities.Order;
import entities.Windows;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
	private CheckBox regCheckBox, bcdCheckBox, ccrCheckBox, tankCheckBox;
	public static boolean regCheck, bcdCheck, ccrCheck,tankCheck;//According to these attributes the fixing windows will be made



	public void initialize(){
		regCheck=false;
		bcdCheck=false;
		ccrCheck=false;
		tankCheck=false;
		GM = new GeneralMethods();
		orderSelected = LabOrdersController.orderSelected;
		checkCheckBox();
		nameText.setText(orderSelected.getName());
		descriptionTextArea.setText(orderSelected.getDescription());
		commentsTextArea.setText(orderSelected.getComments());

	}

	public void checkCheckBox(){
		bcdCheckBox.setVisible(false);
		tankCheckBox.setVisible(false);
		ccrCheckBox.setVisible(false);
		regCheckBox.setVisible(false);
		if(orderSelected.getDescription().contains("BCD"))
			bcdCheckBox.setVisible(true);
		if(orderSelected.getDescription().contains("Regulator"))
			regCheckBox.setVisible(true);
		if(orderSelected.getDescription().contains("Tank"))
			tankCheckBox.setVisible(true);
		if(orderSelected.getDescription().contains("CCR"))
			ccrCheckBox.setVisible(true);
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

	public void onFix(){
		if(!((ccrCheckBox.isVisible()&&ccrCheckBox.isSelected())||(tankCheckBox.isVisible()&&tankCheckBox.isSelected())||(regCheckBox.isVisible()
				&&regCheckBox.isSelected())||(bcdCheckBox.isVisible()&&bcdCheckBox.isSelected())))
			Windows.warning("סמן את אחת האפשרויות לפניי שתמשיך");
		if(ccrCheckBox.isVisible()&&ccrCheckBox.isSelected())
			ccrCheck=true;
		if((tankCheckBox.isVisible()&&tankCheckBox.isSelected()))
			tankCheck=true;
		if((regCheckBox.isVisible()&&regCheckBox.isSelected()))
			regCheck=true;
		if((bcdCheckBox.isVisible()&&bcdCheckBox.isSelected()))
			bcdCheck=true;

	}

	public void onBackPhone(){
		GM.closePopup(Main.popup2);
	}
	public void onBack(){
		Main.showMenu("LabOrders");
	}

}
