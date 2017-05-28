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
	private Text commentsText, interPressureText,interPressureInfoText, titleText;//Helping text for interpressure
	@FXML
	private TabPane annualTabPane;
	@FXML
	private TextField interPressureTextField,serialNumTextField;
	@FXML
	private Button overflowButton, checkInterPressureButton;
	@FXML
	private CheckBox managerCheckBox, kitCheckBox, checkBox1, checkBox2, checkBox3, checkBox4;
	@FXML
	private TextArea commentsTextArea;
	
	public static String annualComments;
	public static int fixCost = 0;

//	private ObservableList<Tab> tabList;
	private GeneralMethods GM = new GeneralMethods();
//	private SingleSelectionModel<Tab> selectionModel;

	private String lastInterPressure;

	private String visual = "בדיקה ויזואלית בוצעה";
	private String hoes = "בדיקת קרעים בצינורות בוצעה";
	private String seal =  "בדיקת אטימות בוצעה";
	private String leak =  "בדיקת דליפות בוצעה";
	private String kit = "הוחלף הקיט בתאריך: " + GM.getCurrentDate();
	private String pressure = "בדיקת החזקת לחץ של המאזן בוצעה";
	private String perfectBCD = "בדיקת שלמות המאזן בוצעה";
	private String escape = "בדיקת בריחת אוויר בוצעה";
	private String perfectTank = "בדיקת שלמות המיכל בוצעה";
	private String emptyTank = "המיכל היה ריק בתחילת הבדיקה";
	private String perfectCCR = "בדיקת שלמות המערכת הסגורה בוצעה";
	private String checkListCCR = "הצ'ק ליסט נבדק וסומן";

	public static boolean isManagerApprove = false, isInterPressure = false;



	/**
	 * Initializes the tabs, according to the tech's choice
	 * @author orelzman
	 */
	public void initialize(){
//		tabList = annualTabPane.getTabs();
//		selectionModel = annualTabPane.getSelectionModel();
		System.out.println("init" + AnnualController.annualComments);
		commentsText.setVisible(false);


//		if(!OrderInfoController.regCheck){
//			tabList.get(0).setDisable(true);
//			text.setVisible(false);
//		}
//
//		if(!OrderInfoController.bcdCheck)
//
//		if(!OrderInfoController.tankCheck)
//			tabList.get(2).setDisable(true);
//
//		if(!OrderInfoController.ccrCheck)
//			tabList.get(3).setDisable(true);

		if(OrderInfoController.regCheck)
			setReg();
		
		if(OrderInfoController.bcdCheck)
			setBCD();		

		if(OrderInfoController.tankCheck)
			setTank();
		
		if(OrderInfoController.ccrCheck)
			setCCR();
	}

	
	public void setReg(){
		interPressureText.setVisible(true);
		interPressureInfoText.setVisible(true);
		interPressureTextField.setVisible(true);
		checkInterPressureButton.setVisible(true);
		kitCheckBox.setVisible(true);
		
		checkBox1.setText("בדיקה ויזואלית לפיות");
		checkBox2.setText("הוסת ללא קרעים בצינורות");
		checkBox3.setText("הוסת אטום");
		checkBox4.setText("הוסת ללא דליפות");	
		titleText.setText("וסת");
	}
	
	public void unsetReg(){
		interPressureText.setVisible(false);
		interPressureInfoText.setVisible(false);
		interPressureTextField.setVisible(false);
		checkInterPressureButton.setVisible(false);
		kitCheckBox.setVisible(false);
	}
	
	public void setBCD(){
		unsetReg();
		
		checkBox1.setText("המאזן שלם");
		checkBox2.setText("המאזן אינו מנפח באופן חופשי");
		checkBox3.setText("המאזן מחזיק לחץ");
		checkBox4.setVisible(false);
		titleText.setText("מאזן");
	}
	
	public void setCCR(){
		unsetReg();
		checkBox1.setText("המערכת הסגורה שלמה");
		checkBox2.setText("הצ'ק ליסט בוצע");
		checkBox3.setVisible(false);
		checkBox4.setVisible(false);
		titleText.setText("מאזן");
	}
	
	public void setTank(){
		unsetReg();
		checkBox1.setText("המיכל היה ריק בתחילת הבדיקה");
		checkBox2.setText("המיכל שלם חיצונית");
		checkBox3.setVisible(false);
		checkBox4.setVisible(false);
		titleText.setText("מאזן");
	}

	public void onContinue(){
		if(!isManagerApprove){
			Windows.warning("המנהל לא אישר את הבדיקה ולכן לא תוכל להמשיך עד לקבלת אישור.");
			return;
		}
		
		if(OrderInfoController.regCheck)
			onContinueRegulator();
		
		if(OrderInfoController.bcdCheck)
			onContinueBCD();		

		if(OrderInfoController.tankCheck)
			onContinueTank();
		
		if(OrderInfoController.ccrCheck)
			onContinueCCR();
		
		//After a check was finished, we'll reset the vital attributes
		isManagerApprove = false;
		isInterPressure = false;
		OrderInfoController.isAnnualDone = true;
		
		
		
		Main.showMenu("OrderInfo");
	}
	
	public void onContinueCCR(){
		int isAllChecked = 0;
		annualComments="בדיקה שנתית למערכת סגורה:" + "\n";
		if(checkBox1.isSelected()){
			isAllChecked++;annualComments+=perfectCCR + "\n";
		}
		if(checkBox2.isSelected()){
			isAllChecked++;annualComments+=checkListCCR + "\n";
		}
		if(isAllChecked!=3 && !isManagerApprove){
			Windows.warning(".לא אישרת בדיקה של כל הדברים הנחוצים והמנהל לא אישר זאת,ולכן לא תוכל להמשיך הלאה");
			return;
		}
		annualComments+="הערות:" + commentsTextArea.getText();
		Windows.message(annualComments, "annual comments Tank");
		OrderInfoController.getOrderSelected().setSummary(OrderInfoController.getOrderSelected().getSummary()+annualComments);
		System.out.println("CCR " + OrderInfoController.getOrderSelected().getSummary());
	}
	
	
	public void onContinueTank(){
		int isAllChecked = 0;
		annualComments="בדיקה שנתית למיכל:" + "\n";
		if(checkBox1.isSelected()){
			isAllChecked++;annualComments+=perfectTank + "\n";
		}
		if(checkBox2.isSelected()){
			isAllChecked++;annualComments+=emptyTank + "\n";
		}
		if(isAllChecked!=3 && !isManagerApprove){
			Windows.warning(".לא אישרת בדיקה של כל הדברים הנחוצים והמנהל לא אישר זאת,ולכן לא תוכל להמשיך הלאה");
			return;
		}
		annualComments+="הערות:" + commentsTextArea.getText();
		Windows.message(annualComments, "annual comments Tank");
		OrderInfoController.getOrderSelected().setSummary(OrderInfoController.getOrderSelected().getSummary()+annualComments);
		
	}
	
	/**
	 * Creates a new BCD check if everything was filled correctly
	 * @author orelzman
	 */

	public void onContinueBCD(){
		int isAllChecked = 0;//To check if every checkbox is checked!
		annualComments = "בדיקה שנתית למאזן:" + "\n";
		if(checkBox3.isSelected()){
			isAllChecked++;
			annualComments+=pressure + "\n";
		}
		if(checkBox1.isSelected()){
			isAllChecked++;
			annualComments+=perfectBCD + "\n";
		}
		if(checkBox2.isSelected()){
			isAllChecked++;
			annualComments+=escape + "\n";
		}
		if(isAllChecked!=3 && !isManagerApprove){
			Windows.warning(".לא אישרת בדיקה של כל הדברים הנחוצים והמנהל לא אישר זאת,ולכן לא תוכל להמשיך הלאה");
			return;
		}
		annualComments+="הערות:" + commentsTextArea.getText();
		Windows.message(annualComments, "annual comments bcd");
		OrderInfoController.getOrderSelected().setSummary(OrderInfoController.getOrderSelected().getSummary()+annualComments);
		
		

	}
	
	/**
	 * Creates the annual comments, according to the checkboxes that were ticked by the tech.
	 * In addition, it sets an AnnualCheck object with the proper information to send the server and add it into the database.
	 * @author orelzman
	 */
	public void onContinueRegulator(){

		AnnualCheck annualCheck = new AnnualCheck();
		annualComments="בדיקה שנתית לוסת:" + "\n";

		if(!isInterPressure && !isManagerApprove){
			Windows.message("לחץ הביניים לא אושר. אשר אותו לפניי שתמשיך או שתכניס סיסמת מנהל", "לחץ ביניים לא תקין");
			return;
		}

		int isAllChecked = 0;
		if(checkBox1.isSelected())
			isAllChecked++;
		if(checkBox2.isSelected())
			isAllChecked++;
		if(checkBox3.isSelected())
			isAllChecked++;
		if(checkBox4.isSelected())
			isAllChecked++;

		if(isAllChecked != 4){
			Windows.warning(".לא אישרת בדיקה של כל הדברים הנחוצים והמנהל לא אישר זאת,ולכן לא תוכל להמשיך הלאה");
			return;
		}

		if(checkBox1.isSelected())
			annualComments+=(visual);
		if(checkBox2.isSelected())
			annualComments+=("\n" + hoes + "\n");
		if(checkBox3.isSelected())
			annualComments+=(seal + "\n");
		if(checkBox4.isSelected())
			annualComments+=(leak + "\n");
		if(kitCheckBox.isSelected())
			annualComments+=(kit + "\n");
		annualComments+=("לחץ הביניים שנבדק הינו: " + interPressureTextField.getText() + "\n");
		if(!(commentsTextArea.getText().equals("")))
			annualComments+=("הערות:" + "\n" + commentsTextArea.getText());

		Windows.message(annualComments, "");

//		annualCheck.setAnnualComments(annualComments);
//		annualCheck.setReg(true);
//		if(kitCheckBox.isSelected())
//			(OrderInfoController.getOrderSelected()).setKitChangeDate(GM.getCurrentDate());
//		else
//			(OrderInfoController.getOrderSelected()).setKitChangeDate(null);
//		annualCheck.setFixComments(commentsTextArea.getText());
//		annualCheck.setCustID(OrderInfoController.getOrderSelected().getCustID());
		if(serialNumTextField.getText().equals("")){
			Windows.message("הכנס בבקשה את המספר הסידורי של הוסת.", "מספר סידורי");
			return;
		}
//		annualCheck.setSerialNum(serialNumTextField.getText());
//		annualCheck.setManagerApprove(managerCheckBox.isSelected());
//		annualCheck.setKit(kitCheckBox.isSelected());
//		annualCheck.setOrderNum(OrderInfoController.getOrderSelected().getOrderNum());

		Windows.message(annualComments, "Reg");
		OrderInfoController.getOrderSelected().setSummary(OrderInfoController.getOrderSelected().getSummary()+annualComments);
//		GM.sendServerThread(annualCheck, "AnnualCheck");
//		GM.closePopup(Main.popup2);


	}

	/**
	 * Sets information about the check button.
	 * @author orelzman
	 */
	public void onCheckEntered(){
		interPressureInfoText.setVisible(true);
	}


	/**
	 * Removes the information text about the check button.
	 * @author orelzman
	 */
	public void onCheckExited(){
		interPressureInfoText.setVisible(false);
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
				interPressureTextField.setStyle("-fx-control-inner-background: #33FF33;");
				lastInterPressure = interPressureTextField.getText();
				return;
			}
			else{
				isInterPressure = false;
				interPressureTextField.setStyle("-fx-control-inner-background: white;");
			}
		}
		else{
			isInterPressure=true;
			interPressureTextField.setStyle("-fx-control-inner-background: #33FF33;");
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
