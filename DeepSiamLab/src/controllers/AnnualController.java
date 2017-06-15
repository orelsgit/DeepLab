package controllers;


import java.util.ArrayList;

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
	private Text commentsText, interPressureText,interPressureInfoText, titleText, commentsTextBlue, leaksText;//Helping text for interpressure
	@FXML
	private TabPane annualTabPane;
	@FXML
	private TextField interPressureTextField,serialNumTextField;
	@FXML
	private Button overflowButton, checkInterPressureButton;
	@FXML
	private CheckBox managerCheckBox, kitCheckBox, checkBox1, checkBox2, checkBox3, checkBox4, visualCheckBox,
	hoesCheckBox, sealCheckBox, leakCheckBox, pressureCheckBox, perfectCheckBox, escapeCheckBox;
	@FXML
	private CheckBox checkBox5, checkBox6, checkBox7, checkBox8, checkBox9, checkBox10, checkBox11, checkBox12, checkBox13,
	checkBox14, checkBox15, checkBox16, checkBox17;
	private int page = 1;
	@FXML
	private TextArea commentsTextArea, leaksTextArea;
	private ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();

	public static String annualComments;
	public static int fixCost = 0;

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
	private String perfectBCD = "בדיקת שלמות המאזן בוצעה";
	private String escape = "בדיקת בריחת אוויר בוצעה";
	private String perfectTank = "בדיקת שלמות המיכל בוצעה";
	private String emptyTank = "המיכל היה ריק בתחילת הבדיקה";

	public static boolean isManagerApprove = false, isInterPressure = false;



	/**
	 * Initializes the tabs, according to the tech's choice
	 * @author orelzman
	 */
	public void initialize(){
		tabList = annualTabPane.getTabs();
		selectionModel = annualTabPane.getSelectionModel();
		commentsText.setVisible(false);
		isManagerApprove = false;
		isInterPressure = false;

		int isSelected = -1;

		if(OrderInfoController.regCheck){
			isSelected = 0;
			//setReg();
		}

		if(OrderInfoController.bcdCheck){
			isSelected = 1;
		}

		if(OrderInfoController.tankCheck){
			isSelected = 2;
		}

		if(OrderInfoController.ccrCheck){
			isSelected = 3;
			setCCR();
		}

		for(int i=0;i<4;++i)
			if(i==isSelected){
				tabList.get(i).setDisable(false);
				selectionModel.select(i);
			}
			else
				tabList.get(i).setDisable(true);



		//		if(OrderInfoController.regCheck)
		//			setReg();
		//
		//		if(OrderInfoController.bcdCheck)
		//			setBCD();		
		//
		//		if(OrderInfoController.tankCheck)
		//			setTank();
		//
		//		if(OrderInfoController.ccrCheck)
		//			setCCR();
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




	public void setCCR(){
		//unsetReg();
		commentsTextArea.setVisible(false);
		commentsTextArea.setEditable(false);
		commentsText.setVisible(false);
		commentsTextBlue.setVisible(false);
		managerCheckBox.setDisable(true);
		setCheckBoxes();
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

	public void onBackCCR(){
		if(page == 1)
			Main.showMenu("OrderInfo");
		if(page == 2)
			setPage(1);
	}

	public void setCheckBoxes(){
		checkBoxes.add(checkBox1);checkBoxes.add(checkBox2);checkBoxes.add(checkBox3);checkBoxes.add(checkBox4);
		checkBoxes.add(checkBox5);checkBoxes.add(checkBox6);checkBoxes.add(checkBox7);checkBoxes.add(checkBox8);
		checkBoxes.add(checkBox9);checkBoxes.add(checkBox10);checkBoxes.add(checkBox11);checkBoxes.add(checkBox12);
		checkBoxes.add(checkBox13);checkBoxes.add(checkBox14);checkBoxes.add(checkBox15);checkBoxes.add(checkBox16);
		checkBoxes.add(checkBox17);
	}

	public void setPage(int page){
		if(page == 2){
			this.page = 2;
			leaksTextArea.setEditable(false);
			leaksTextArea.setVisible(false);
			leaksText.setVisible(false);
			managerCheckBox.setVisible(true);
			managerCheckBox.setDisable(false);
			commentsText.setVisible(true);
			commentsTextBlue.setVisible(true);
			commentsTextArea.setVisible(true);
			commentsTextArea.setEditable(true);
			for(int i=0;i<9;++i){
				checkBoxes.get(i).setVisible(false);
				checkBoxes.get(i).setDisable(true);
			}
			for(int i=9;i<17;++i){
				checkBoxes.get(i).setVisible(true);
				checkBoxes.get(i).setDisable(false);			
			}
		}

		else if(page == 1){
			this.page = 1;
			setCCR();
			leaksTextArea.setEditable(true);
			leaksTextArea.setVisible(true);
			leaksText.setVisible(true);
			managerCheckBox.setVisible(false);
			managerCheckBox.setDisable(true);
			commentsText.setVisible(false);
			commentsTextBlue.setVisible(false);
			commentsTextArea.setVisible(false);
			commentsTextArea.setEditable(false);

			for(int i=9;i<17;++i){
				checkBoxes.get(i).setVisible(false);
				checkBoxes.get(i).setDisable(true);
			}
			for(int i=0;i<9;++i){
				checkBoxes.get(i).setVisible(true);
				checkBoxes.get(i).setDisable(false);			
			}
		}
	}

	public void onContinueCCR(){

		if(page == 1){
			setPage(2);return;
		}
		else{
			if(!isManagerApprove){
				Windows.message("המנהל לא אישר את הבדיקה, ולכן לא תוכל לסיים אותה עד לקבלת האישור.", "אישור מנהל");
				return;
			}
			annualComments="בדיקה שנתית למערכת סגורה:" + "\n";
			for(int i=0;i<17;i++)
				if(checkBoxes.get(i).isSelected())
					annualComments+=((i+1) + "." + checkBoxes.get(i).getText() + "\n");
		}



		annualComments+= "\n" + "הערות:" + commentsTextArea.getText() + "\n";
		Windows.message(annualComments, "annual comments Tank");
		LabOrdersController.orderSelected.ccrDone = true;
		doneAnnual(annualComments);
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
		annualComments+="הערות:" + commentsTextArea.getText() + "\n";
		Windows.message(annualComments, "annual comments Tank");
		LabOrdersController.orderSelected.tankDone = true;
		doneAnnual(annualComments);

		//Consider doing the changes of the buttons here and notify the other class here~

	}

	/**
	 * Creates a new BCD check if everything was filled correctly
	 * @author orelzman
	 */

	public void onContinueBCD(){
		int isAllChecked = 0;//To check if every checkbox is checked!
		annualComments = "בדיקה שנתית למאזן:" + "\n";
		if(pressureCheckBox.isSelected()){
			isAllChecked++;
			annualComments+=pressure + "\n";
		}
		if(perfectCheckBox.isSelected()){
			isAllChecked++;
			annualComments+=perfectBCD + "\n";
		}
		if(escapeCheckBox.isSelected()){
			isAllChecked++;
			annualComments+=escape + "\n";
		}
		if(isAllChecked!=3 && !isManagerApprove){
			Windows.warning(".לא אישרת בדיקה של כל הדברים הנחוצים והמנהל לא אישר זאת,ולכן לא תוכל להמשיך הלאה");
			return;
		}
		annualComments+="הערות:" + commentsTextArea.getText() + "\n";
		Windows.message(annualComments, "annual comments bcd");
		LabOrdersController.orderSelected.bcdDone = true;
		doneAnnual(annualComments);

	}




	/**
	 * Creates the annual comments, according to the checkboxes that were ticked by the tech.
	 * In addition, it sets an AnnualCheck object with the proper information to send the server and add it into the database.
	 * @author orelzman
	 */
	public void onContinueRegulator(){
		

		annualComments="בדיקה שנתית לוסת:" + "\n";

		if(!isInterPressure && !isManagerApprove){
			Windows.message("לחץ הביניים לא אושר. אשר אותו לפניי שתמשיך או שתכניס סיסמת מנהל", "לחץ ביניים לא תקין");
			return;
		}

		if(visualCheckBox.isSelected())
			annualComments+=(visual) + "\n";
		if(hoesCheckBox.isSelected())
			annualComments+=(hoes + "\n");
		if(sealCheckBox.isSelected())
			annualComments+=(seal + "\n");
		if(leakCheckBox.isSelected())
			annualComments+=(leak + "\n");
		if(kitCheckBox.isSelected())
			annualComments+=(kit + "\n");

		annualComments+=("לחץ הביניים שנבדק הינו: " + interPressureTextField.getText() + "\n");
		if(!(commentsTextArea.getText().equals("")))
			annualComments+=("הערות:" + "\n" + commentsTextArea.getText() + "\n");

		Windows.message(annualComments, "OnContinueRegAnnualController");
		
		LabOrdersController.orderSelected.regDone = true;
		doneAnnual(annualComments);

	}

	public void doneAnnual(String annualComments){
			if(AnnualController.fixCost!=0){
				LabOrdersController.orderSelected.setCost( LabOrdersController.orderSelected.getCost() + AnnualController.fixCost);
				AnnualController.fixCost=0;
			}
		
		LabOrdersController.orderSelected.setSummary(LabOrdersController.orderSelected.getSummary()+annualComments);
		OrderInfoController.isAnnualDone = true;
		Main.showMenu("OrderInfo");
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

	public void stop(){
		System.out.println("stopped.");
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
