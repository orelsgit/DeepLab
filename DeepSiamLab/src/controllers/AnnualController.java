package controllers;


import java.util.ArrayList;

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
	private Text text1, text2, text3, text4, text5, text6, text7, text8, text9, text10, text11, text12, text13, text14,
	text15, text16, urText, nsText, nnText, iText, oText, legendText, titleText1, titleText2, titleText3, commentsTextBlue1,
	commentsText1, mainTitleText;
	@FXML
	private TabPane annualTabPane;
	@FXML
	private TextField interPressureTextField,serialNumTextField;
	@FXML
	private TextField textField1, textField2, textField3, textField4, textField5, textField6, textField7, textField8, 
	textField9, textField10, textField11, textField12, textField13, textField14, textField15, textField16,
	legendTextField;
	@FXML
	private Button overflowButton, checkInterPressureButton, urButton, nsButton, nnButton, iButton, oButton;
	@FXML
	private CheckBox managerCheckBox, managerCheckBox1, kitCheckBox, checkBox1, checkBox2, checkBox3, checkBox4, visualCheckBox,
	hoesCheckBox, sealCheckBox, leakCheckBox, pressureCheckBox, perfectCheckBox, escapeCheckBox;
	@FXML
	private CheckBox checkBox5, checkBox6, checkBox7, checkBox8, checkBox9, checkBox10, checkBox11, checkBox12, checkBox13,
	checkBox14, checkBox15, checkBox16, checkBox17;
	@FXML
	private CheckBox noCheckBox1, noCheckBox2, noCheckBox3, noCheckBox4, noCheckBox5, noCheckBox6, noCheckBox7, noCheckBox8,
	noCheckBox9, noCheckBox10, noCheckBox11, noCheckBox12, noCheckBox13, noCheckBox14, noCheckBox15, noCheckBox16,
	yesCheckBox1, yesCheckBox2, yesCheckBox3, yesCheckBox4, yesCheckBox5, yesCheckBox6, yesCheckBox7, yesCheckBox8,
	yesCheckBox9, yesCheckBox10, yesCheckBox11, yesCheckBox12, yesCheckBox13, yesCheckBox14, yesCheckBox15, yesCheckBox16;
	private int page = 1;
	@FXML


	private TextArea commentsTextArea, leaksTextArea, commentsTextArea1;


	private ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
	private ArrayList<CheckBox> noCheckBoxes = new ArrayList<CheckBox>();
	private ArrayList<CheckBox> yesCheckBoxes = new ArrayList<CheckBox>();
	private ArrayList<TextField> textFields = new ArrayList<TextField>();
	private ArrayList<Text> texts = new ArrayList<Text>();

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

	public static boolean isManagerApprove = false, isInterPressure = false;



	/**
	 * Initializes the tabs, according to the tech's choice
	 * @author orelzman
	 */
	public void initialize(){
		tabList = annualTabPane.getTabs();
		selectionModel = annualTabPane.getSelectionModel();
		//commentsText.setVisible(false);
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
			setTank();
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


	}//End initialize


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


	public void setTank(){
		setYesNoCheckBoxes();
		setTextFields();
		setTexts();
		setPageTank(1);
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
		page = 1;
		onBack();
	}

	public void onBackCCR(){
		if(page == 1)
			onBack();
		if(page == 2)
			setPageCCR(1);
	}




	public void setTexts(){
		texts.add(text1);texts.add(text2);texts.add(text3);texts.add(text4);texts.add(text5);texts.add(text6);
		texts.add(text7);texts.add(text8);texts.add(text9);texts.add(text10);texts.add(text11);texts.add(text12);
		texts.add(text13);texts.add(text14);texts.add(text15);texts.add(text16);

	}

	public void setYesNoCheckBoxes(){
		noCheckBoxes.add(noCheckBox1);noCheckBoxes.add(noCheckBox2);noCheckBoxes.add(noCheckBox3);noCheckBoxes.add(noCheckBox4);
		noCheckBoxes.add(noCheckBox5);noCheckBoxes.add(noCheckBox6);noCheckBoxes.add(noCheckBox7);noCheckBoxes.add(noCheckBox8);
		noCheckBoxes.add(noCheckBox9);noCheckBoxes.add(noCheckBox10);noCheckBoxes.add(noCheckBox11);noCheckBoxes.add(noCheckBox12);
		noCheckBoxes.add(noCheckBox13);noCheckBoxes.add(noCheckBox14);noCheckBoxes.add(noCheckBox15);noCheckBoxes.add(noCheckBox16);

		yesCheckBoxes.add(yesCheckBox1);yesCheckBoxes.add(yesCheckBox2);yesCheckBoxes.add(yesCheckBox3);yesCheckBoxes.add(yesCheckBox4);
		yesCheckBoxes.add(yesCheckBox5);yesCheckBoxes.add(yesCheckBox6);yesCheckBoxes.add(yesCheckBox7);yesCheckBoxes.add(yesCheckBox8);
		yesCheckBoxes.add(yesCheckBox9);yesCheckBoxes.add(yesCheckBox10);yesCheckBoxes.add(yesCheckBox11);yesCheckBoxes.add(yesCheckBox12);
		yesCheckBoxes.add(yesCheckBox13);yesCheckBoxes.add(yesCheckBox14);yesCheckBoxes.add(yesCheckBox15);yesCheckBoxes.add(yesCheckBox16);
	}

	public void setTextFields(){
		textFields.add(textField1);textFields.add(textField2);textFields.add(textField3);textFields.add(textField4);
		textFields.add(textField6);textFields.add(textField6);textFields.add(textField7);textFields.add(textField8);
		textFields.add(textField9);textFields.add(textField10);textFields.add(textField11);textFields.add(textField12);
		textFields.add(textField13);textFields.add(textField14);textFields.add(textField15);textFields.add(textField16);
	}

	public void setCheckBoxes(){
		checkBoxes.add(checkBox1);checkBoxes.add(checkBox2);checkBoxes.add(checkBox3);checkBoxes.add(checkBox4);
		checkBoxes.add(checkBox5);checkBoxes.add(checkBox6);checkBoxes.add(checkBox7);checkBoxes.add(checkBox8);
		checkBoxes.add(checkBox9);checkBoxes.add(checkBox10);checkBoxes.add(checkBox11);checkBoxes.add(checkBox12);
		checkBoxes.add(checkBox13);checkBoxes.add(checkBox14);checkBoxes.add(checkBox15);checkBoxes.add(checkBox16);
		checkBoxes.add(checkBox17);
	}

	public void setPageCCR(int page){
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
			setPageCCR(2);return;
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



	public void onBack(){
		page = 1;
		Main.showMenu("OrderInfo", "הזמנה פרטית");
	}

	public void onBackTank(){
		if(page == 1)
			onBack();
		if(page == 2)
			setPageTank(1);
	}

	public void setPageTank(int page){
		if(page == 1){
			this.page = 1;
			for(int i=0;i<16;++i)
				if(i<9){
					yesCheckBoxes.get(i).setDisable(false);
					yesCheckBoxes.get(i).setVisible(true);
					noCheckBoxes.get(i).setDisable(false);
					noCheckBoxes.get(i).setVisible(true);
					textFields.get(i).setVisible(true);
					textFields.get(i).setDisable(false);
					texts.get(i).setVisible(true);
				}
				else{
					yesCheckBoxes.get(i).setDisable(true);
					yesCheckBoxes.get(i).setVisible(false);
					noCheckBoxes.get(i).setDisable(true);
					noCheckBoxes.get(i).setVisible(false);
					textFields.get(i).setVisible(false);
					textFields.get(i).setDisable(true);
					texts.get(i).setVisible(false);
				}
			setLegend(false);
			setButtons(false);
			setTitles(true);
			setComments(false);
			return;
		}//end Page1
		if(page == 2){
			System.out.println("Page 2 ~");
			this.page =2;
			for(int i=0;i<16;++i)
				if(i>=9){
					yesCheckBoxes.get(i).setDisable(false);
					yesCheckBoxes.get(i).setVisible(true);
					noCheckBoxes.get(i).setDisable(false);
					noCheckBoxes.get(i).setVisible(true);
					textFields.get(i).setVisible(true);
					textFields.get(i).setDisable(false);
					texts.get(i).setVisible(true);
				}
				else{
					yesCheckBoxes.get(i).setDisable(true);
					yesCheckBoxes.get(i).setVisible(false);
					noCheckBoxes.get(i).setDisable(true);
					noCheckBoxes.get(i).setVisible(false);
					textFields.get(i).setVisible(false);
					textFields.get(i).setDisable(true);
					texts.get(i).setVisible(false);
				}
			setLegend(true);
			setButtons(true);
			setTitles(false);
			setComments(true);
		}//End page2
	}


	public void setComments(boolean isSet){
		commentsTextArea1.setVisible(isSet);
		commentsTextArea1.setEditable(isSet);
		commentsTextBlue1.setVisible(isSet);
		commentsText1.setVisible(isSet);
		managerCheckBox1.setDisable(!isSet);
		managerCheckBox1.setVisible(true);
		if(!isSet)
			mainTitleText.setText("CYLINDER");
		else
			mainTitleText.setText("VAVLE");

	}

	public void setTitles(boolean isSet){
		titleText1.setVisible(isSet);
		titleText2.setVisible(isSet);
		titleText3.setVisible(isSet);
	}

	public void setButtons(boolean isSet){
		urButton.setDisable(!isSet);
		urButton.setVisible(isSet);
		nsButton.setDisable(!isSet);
		nsButton.setVisible(isSet);
		nnButton.setDisable(!isSet);
		nnButton.setVisible(isSet);
		iButton.setDisable(!isSet);
		iButton.setVisible(isSet);
		oButton.setDisable(!isSet);
		oButton.setVisible(isSet);
	}

	public void setLegend(boolean isSet){
		legendText.setVisible(isSet);
		urText.setVisible(isSet);
		nsText.setVisible(isSet);
		nnText.setVisible(isSet);
		iText.setVisible(isSet);
		oText.setVisible(isSet);
		urButton.setVisible(isSet);
		nsButton.setVisible(isSet);
		nnButton.setVisible(isSet);
		iButton.setVisible(isSet);
		oButton.setVisible(isSet);
		urButton.setDisable(!isSet);
		nsButton.setDisable(!isSet);
		nnButton.setDisable(!isSet);
		iButton.setDisable(!isSet);
		oButton.setDisable(!isSet);
		legendTextField.setVisible(isSet);
	}


	public void onContinueTank(){
		if(page == 1){
			setPageTank(2);
			return;
		}
		annualComments = "בדיקה שנתית למיכל:" + "\n";
		for(int i=0;i<16;i++){
			if(yesCheckBoxes.get(i).isSelected())
				annualComments+=texts.get(i).getText() + ": YES" + "\n תיאור\\מיקום:" + textFields.get(i).getText() + "\n";
			else
				annualComments+=texts.get(i).getText() + ": NO" + "\n תיאור\\מיקום:" + textFields.get(i).getText() + "\n";
		}

		annualComments+="\n LEGEND: " + legendTextField.getText() + "\n\n";

		String[] options = new String[] {"עבר", "נכשל", "נדחה", "הוחזר לבעלים ללא הסמכה"};
		switch(Windows.multipleChoice(options)){
		case 0:
			annualComments+="הצילינדר עבר את הבדיקה בהצלחה"  + "\n";break;
		case 1:
			annualComments+="הצילינדר נכשל בבדיקה"  + "\n";break;
		case 2:
			annualComments+="הצילינדר נדחה"  + "\n";break;
		case 3:
			annualComments+="הצילינדר הוחזר לבעליו בלי הסמכה"  + "\n";break;
		}
		annualComments+="הערות:" + commentsTextArea.getText() + "\n";
		Windows.message(annualComments, "annual comments Tank");
		LabOrdersController.orderSelected.tankDone = true;
		doneAnnual(annualComments);


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
		onBack();
	}


	public void onUR(){
		setLegendText("Unremarkable");
	}
	public void onNS(){
		setLegendText("Nothing Seen");
	}
	public void onNN(){
		setLegendText("Nothing Noticed");
	}
	public void onI(){
		setLegendText("Inside");
	}
	public void onO(){
		setLegendText("Outside");
	}
	public void setLegendText(String text){
		legendTextField.setText("");
		legendTextField.setText(text);
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
		GM.getPopup(Main.popup3, "ManagersPasswordScreen", "סיסמת מנהל", "popup2");
		GM.closePopup(Main.popup3);//Will be called after the popup is closed.
		if(!isManagerApprove)
			managerCheckBox.setSelected(false);
		else
			managerCheckBox.setSelected(true);
	}
}
