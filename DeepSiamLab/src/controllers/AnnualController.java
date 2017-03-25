package controllers;


import entities.GeneralMessage;
import entities.GeneralMethods;
import entities.Windows;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import main.Main;

public class AnnualController {

	@FXML
	private Text text, commentsText;//Helping text for interpressure
	@FXML
	private TabPane annualTabPane;
	@FXML
	private TextField interPressureTextField;
	@FXML
	private Button overflowButton;

	private ObservableList<Tab> tabList;
	private GeneralMethods GM = new GeneralMethods();
	private SingleSelectionModel<Tab> selectionModel;

	public static boolean isManagerApprove = false;



	/**
	 * 
	 */
	public void initialize(){
		tabList = annualTabPane.getTabs();
		selectionModel = annualTabPane.getSelectionModel();
		text.setVisible(false);
		commentsText.setVisible(false);
		overflowButton.setVisible(false);

		if(!OrderInfoController.regCheck)
			tabList.get(0).setDisable(true);

		if(!OrderInfoController.bcdCheck)
			tabList.get(1).setDisable(true);

		if(!OrderInfoController.tankCheck)
			tabList.get(2).setDisable(true);

		if(!OrderInfoController.ccrCheck)
			tabList.get(3).setDisable(true);

		if(OrderInfoController.regCheck){
			selectionModel.select(0);
		}
		if(OrderInfoController.bcdCheck)
			selectionModel.select(1);		

		if(OrderInfoController.tankCheck)
			selectionModel.select(2);	

		if(OrderInfoController.ccrCheck)
			selectionModel.select(3);


	}

	public void onCheckEntered(){
		text.setVisible(true);
	}

	public void onCheckExited(){
		text.setVisible(false);
	}

	public void onCommentsEntered(){
		commentsText.setVisible(true);
	}

	public void onCommentsExited(){
		commentsText.setVisible(false);
	}

	public void onCheck(){
		if(interPressureTextField.getText().equals(""))
			return;
		if(OrderInfoController.regChosen.getInterPressure()!=Float.parseFloat(interPressureTextField.getText())){
			Windows.warning("לחץ הביניים הנתון לא תואם את לחץ הביניים שנקבע עפ הוראות היצרן." + "\n" + ":לחץ הביניים שנקבע עי היצרון הוא" + OrderInfoController.regChosen.getInterPressure());
			return;
		}

	}

	public void onContinue(){
		GM.closePopup(Main.popup2);
	}

	public void onOverflow(){

	}

	public void onManager(){//Current popup2 
		GeneralMessage.currentWindow = "AnnualScreen";
		GM.getPopup(Main.popup3, "ManagersPasswordScreen", "הכנס סיסמת מנהל");
		if(!isManagerApprove)
			Windows.message("הטיפול לא אושר עי המנהל ולכן אם תבחר להמשיך הוא יאושר חלקית, עד אשר המנהל יאשר סופית את הטיפול ועבירו לארכיון", "אישור חלקי");
		GM.closePopup(Main.popup3);

	}

	public void stop(){
		System.out.println("onStop");
	}

}
