package controllers;


import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
	private TextField interPressureTextField,serialNumTextField;
	@FXML
	private Button overflowButton;
	@FXML
	private CheckBox managerCheckBox, kitCheckBox, visualCheckBox, hoesCheckBox, sealCheckBox, leakCheckBox,
	pressureCheckBox, perfectCheckBox, escapeCheckBox;
	@FXML
	private TextArea regCommentsTextArea, bcdCommentsTextArea;

	private ObservableList<Tab> tabList;
	private GeneralMethods GM = new GeneralMethods();
	private SingleSelectionModel<Tab> selectionModel;

	private String lastInterPressure;

	private String visual = "בדיקה ויזואלית בוצעה";
	private String hoes = "בדיקת קרעים בצינורות בוצעה";
	private String seal =  "בדיקת אטימות בוצעה";
	private String leak =  "בדיקת דליפות בוצעה";
	private String kit = "הוחלף הקיט בתאריך: " + GM.getCurrentDate();
	private String pressure = "בדיקת החזקת לחץ של המאזן בוצעה";
	private String perfect = "בדיקת שלמות המאזן בוצעה";
	private String escape = "בדיקת בריחת אוויר בוצעה";

	public static boolean isManagerApprove = false, isInterPressure = false;



	/**
	 * Initializes the tabs, according to the tech's choice
	 * @author orelzman
	 */
	public void initialize(){
		tabList = annualTabPane.getTabs();
		selectionModel = annualTabPane.getSelectionModel();
		System.out.println("init");
		commentsText.setVisible(false);


		if(!OrderInfoController.regCheck){
			tabList.get(0).setDisable(true);
			text.setVisible(false);
		}

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
	 * Creates a new BCD check if everything was filled correctly
	 * @author orelzman
	 */

	public void onContinueBCD(){
		int isAllChecked = 0;//To check if every checkbox is checked!
		String annualComments = "";
		if(pressureCheckBox.isSelected()){
			isAllChecked++;
			annualComments+=pressure + "\n";
		}
		if(perfectCheckBox.isSelected()){
			isAllChecked++;
			annualComments+=perfect + "\n";
		}
		if(escapeCheckBox.isSelected()){
			isAllChecked++;
			annualComments+=escape + "\n";
		}
		if(isAllChecked!=3 && !isManagerApprove){
			Windows.warning(".לא אישרת בדיקה של כל הדברים הנחוצים והמנהל לא אישר זאת,ולכן לא תוכל להמשיך הלאה");
			return;
		}
		annualComments+="הערות:" + bcdCommentsTextArea.getText();
		Windows.message(annualComments, "annual comments bcd");



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
			if(Windows.yesNo("לחץ הביניים הנתון לא תואם את לחץ הביניים שנקבע עפ הוראות היצרן." + "\n" + "לחץ הביניים שנקבע עי היצרון הוא: " + OrderInfoController.regChosen.getInterPressure()
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
	public void onContinueRegulator(){
		if(!isManagerApprove){
			Windows.warning("המנהל לא אישר את הבדיקה ולכן לא תוכל להמשיך עד לקבלת אישור.");
			return;
		}
		AnnualCheck annualCheck = new AnnualCheck();
		String annualComments="";

		if(!isInterPressure && !isManagerApprove){
			Windows.message("לחץ הביניים לא אושר. אשר אותו לפניי שתמשיך או שתכניס סיסמת מנהל", "לחץ ביניים לא תקין");
			return;
		}

		int isAllChecked = 0;
		if(visualCheckBox.isSelected())
			isAllChecked++;
		if(hoesCheckBox.isSelected())
			isAllChecked++;
		if(sealCheckBox.isSelected())
			isAllChecked++;
		if(leakCheckBox.isSelected())
			isAllChecked++;


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
		if(!(regCommentsTextArea.getText().equals("")))
			annualComments+=("הערות:" + "\n" + regCommentsTextArea.getText());

		Windows.message(annualComments, "");

		annualCheck.setAnnualComments(annualComments);
		annualCheck.setReg(true);
		if(kitCheckBox.isSelected())
			annualCheck.setKitChangeDate(GM.getCurrentDate());
		else
			annualCheck.setKitChangeDate(null);
		annualCheck.setFixComments(regCommentsTextArea.getText());
		annualCheck.setCustID(OrderInfoController.getOrderSelected().getCustID());
		if(serialNumTextField.getText().equals("")){
			Windows.message("הכנס בבקשה את המספר הסידורי של הוסת.", "מספר סידורי");
			return;
		}
		annualCheck.setSerialNum(serialNumTextField.getText());
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
	public void onManager(){
		managerCheckBox.setSelected(false);
		if(isManagerApprove){
			Windows.message("הבדיקה אושרה", "אושר");
			managerCheckBox.setSelected(true);
			return;
		}
		GeneralMessage.currentWindow = "AnnualScreen";
		GM.getPopup(Main.popup3, "ManagersPasswordScreen", "הכנס סיסמת מנהל", "popup2");
		GM.closePopup(Main.popup3);//Will be called after the popup is closed.
		if(!isManagerApprove)
			managerCheckBox.setSelected(false);
		else
			managerCheckBox.setSelected(true);
	}
}
