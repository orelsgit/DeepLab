package controllers;


import entities.AnnualCheck;
import entities.GeneralMessage;
import entities.GeneralMethods;
import entities.Windows;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
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
	@FXML
	private CheckBox managerCheckBox, kitCheckBox, visualCheckBox, hoesCheckBox, sealCheckBox, leakCheckBox;
	@FXML
	private TextArea commentsTextArea;

	private ObservableList<Tab> tabList;
	private GeneralMethods GM = new GeneralMethods();
	private SingleSelectionModel<Tab> selectionModel;

	private String lastInterPressure;

	private String visual = "בדיקה ויזואלית בוצעה";
	private String hoes = "בדיקת קרעים בצינורות בוצעה";
	private String seal =  "בדיקת אטימות בוצעה";
	private String leak =  "בדיקת דליפות בוצעה";
	private String kit = "הוחלף הקיט בתאריך: " + GM.getCurrentDate();

	public static boolean isManagerApprove = false, isInterPressure = false;



	/**
	 * Initializes the tabs, according to the tech's choice
	 */
	public void initialize(){
		tabList = annualTabPane.getTabs();
		selectionModel = annualTabPane.getSelectionModel();
		text.setVisible(false);
		commentsText.setVisible(false);

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


	/**
	 * Sets information about the check button.
	 * @author orelzman
	 */
	public void onCheckEntered(){
		text.setVisible(true);
	}


	/**
	 * Removes the information text about the check button.
	 * @author orelzman
	 */
	public void onCheckExited(){
		text.setVisible(false);
	}

	/**
	 * Sets information about the comments text area
	 * @author orelzman
	 */
	public void onCommentsEntered(){
		commentsText.setVisible(true);
	}

	/**
	 * Removes the information text about the comments text area
	 * @author orelzman
	 */
	public void onCommentsExited(){
		commentsText.setVisible(false);
	}
	/**
	 * Called upon pressing the check button, to check if regulator's intermediate pressure inserted equals to the one ordered by the manu.
	 * The tech will get the option to aprove any exceptions.
	 * @author orelzman
	 */

	public void onCheck(){
		if(interPressureTextField.getText().equals(""))
			return;
		if(OrderInfoController.regChosen.getInterPressure()!=Float.parseFloat(interPressureTextField.getText())){
			if(Windows.yesNoEdited("לחץ הביניים הנתון לא תואם את לחץ הביניים שנקבע עפ הוראות היצרן." + "\n" + "לחץ הביניים שנקבע עי היצרון הוא: " + OrderInfoController.regChosen.getInterPressure()
			+ "\n" + "\n" + "המשך אם אתה מאשר שהלחץ ביניים שהוקלד תקין, אחרת לחץ בטל", "לחץ ביניים", "המשך", "בטל")){
				isInterPressure = true;
				interPressureTextField.setStyle("-fx-background-color: #33FF33;");
				lastInterPressure = interPressureTextField.getText();
				return;
			}
		}
		else{
			isInterPressure=true;
			interPressureTextField.setStyle("-fx-background-color: #33FF33;");
			lastInterPressure = interPressureTextField.getText();
		}
	}



	/**
	 * Makes sure the Intermediate pressure will not be accepted if it was once accepted and changed afterwards.
	 * @author orelzman
	 */
	public void onInterPressureChange(){
		if(lastInterPressure != interPressureTextField.getText()){
			interPressureTextField.setStyle("-fx-background-color: white;");
			interPressureTextField.setStyle("-fx-border-color: gray;");
			isInterPressure = false;
		}
	}

	/**
	 * Creates the annual comments, according to the checkboxes that were ticked by the tech.
	 * In addition, it sets an AnnualCheck object with the proper information to send the server and add it into the database.
	 * @author orelzman
	 */
	public void onContinue(){
		AnnualCheck annualCheck = new AnnualCheck();
		String annualComments="";

		if(!isInterPressure&&!isManagerApprove){
			Windows.message("לחץ הביניים לא אושר. אשר אותו לפניי שתמשיך או שתכניס סיסמת מנהל", "לחץ ביניים לא תקין");
			return;
		}

		if(visualCheckBox.isSelected())
			annualComments+=(visual);
		if(hoesCheckBox.isSelected())
			annualComments+=("\n" + hoes + "\n");
		if(sealCheckBox.isSelected())
			annualComments+=(seal + "\n");
		if(leakCheckBox.isSelected())
			annualComments+=(leak + "\n");
		if(kitCheckBox.isSelected())
			annualComments+=(kit + "\n");
		annualComments+=("לחץ הביניים שנבדק הינו: " + interPressureTextField.getText() + "\n");
		if(!(commentsTextArea.getText().equals("")))
			annualComments+=("הערות:" + "\n" + commentsTextArea.getText());

		Windows.message(annualComments, "");

		annualCheck.setAnnualComments(annualComments);
		annualCheck.setReg(true);
		annualCheck.setKitChangeDate(GM.getCurrentDate());
		annualCheck.setFixComments("");
		annualCheck.setCustID(OrderInfoController.getOrderSelected().getCustID());
		annualCheck.setSerialNum(OrderInfoController.regChosen.getSerialNum());
		annualCheck.setManagerApprove(managerCheckBox.isSelected());
		annualCheck.setKit(kitCheckBox.isSelected());
		annualCheck.setOrderNum(OrderInfoController.getOrderSelected().getOrderNum());

		if(!isManagerApprove)
			Windows.message("הטיפול לא אושר עי המנהל ולכן הוא יאושר חלקית, עד אשר המנהל יאשר סופית את הטיפול ועבירו לארכיון", "אישור חלקי");

		GM.sendServerThread(annualCheck, "AnnualCheck");

		GM.closePopup(Main.popup2);
	}

	/**
	 * Asks the user for manager's password to accept the annual check, according to the israeli diving union.
	 * @author orelzman
	 */
	public void onManager(){//Current popup2 
		managerCheckBox.setSelected(false);
		if(isManagerApprove){
			Windows.message("הבדיקה אושרה", "אושר");
			managerCheckBox.setSelected(true);
			return;
		}
		GeneralMessage.currentWindow = "AnnualScreen";
		GM.getPopup(Main.popup3, "ManagersPasswordScreen", "הכנס סיסמת מנהל");
		GM.closePopup(Main.popup3);
		if(!isManagerApprove)
			managerCheckBox.setSelected(false);
		else
			managerCheckBox.setSelected(true);
	}
}
