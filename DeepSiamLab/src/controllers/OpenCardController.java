package controllers;



import java.util.ArrayList;

import entities.BCD;
import entities.Customer;
import entities.GeneralMessage;
import entities.GeneralMethods;
import entities.Order;
import entities.Regulator;
import entities.Windows;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.Main;

public class OpenCardController {


	@FXML
	private CheckBox regulatorCheckBox, bcdCheckBox, ccrCheckBox, tankCheckBox, deepCheckBox, privateCheckBox;
	@FXML
	private TextArea commentsTextArea;
	@FXML
	private TextField /*Change to Model*/regManuTextField, regDeepNumTextField, bcdModelTextField,bcdDeepNumTextField, tankDeepNumTextField, tankManuTextField,
	ccrOwnerTextField, ccrDeepNumTextField, idTextField;
	@FXML
	private ComboBox<String> regModelComboBox, bcdModelComboBox, tankManuComboBox;
	@FXML
	private Button chooseBCDButton;

	private GeneralMethods GM;

	public static boolean isBackFromSearch, isDoneSearching = true/*For the search method*/;

	public static Customer customerChosen;
	private static ObservableList<String> regList, regListSearch, bcdList, bcdListSearch;



	public void initialize(){

		deepCheckBox.setSelected(false);
		privateCheckBox.setSelected(true);
		idTextField.setEditable(false);
		GM = new GeneralMethods();
		GeneralMessage.currentWindow = "OpenCard";
		regModelComboBox.setVisibleRowCount(GeneralMessage.getRegList().size());//Sets the amount of rows according to the maximum amount of regulators in the list.
		bcdModelComboBox.setVisibleRowCount(GeneralMessage.getBcdList().size());//Same for the bcds


		commentsTextArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER)  {
					onIssueOrder();
				}
			}
		});
		while(GeneralMessage.getRegList()==null)
			GM.Sleep(2);


		ArrayList<String> regs = new ArrayList<String>();
		ArrayList<String> bcds = new ArrayList<String>();

		for(Regulator reg : GeneralMessage.getRegList())
			regs.add(reg.getModel().replaceAll("\\s+", ""));
		for(BCD bcd : GeneralMessage.getBcdList())
			bcds.add(bcd.getModel().replaceAll("\\s+", ""));


		bcdList = FXCollections.observableArrayList(bcds);
		regList = FXCollections.observableArrayList(regs);
		regModelComboBox.getItems().addAll(regList);
		bcdModelComboBox.getItems().addAll(bcdList);
	}

	public OpenCardController(){
	}

	/**
	 * Updates the regs combo box according to user's input.
	 * @author orelzman
	 */

	public void onRegModelChange(){
		if(!isDoneSearching)
			return;
		Task<Void> task = new Task<Void>() {
			@Override 
			protected Void call() throws Exception {
				for (int i=0; i<1;i++) {                
					Platform.runLater(new Runnable() {
						@Override 
						public void run(){
							String search = regModelComboBox.getEditor().getText();
							if(search.equals("")){
								regModelComboBox.getItems().setAll(regList);
								return;
							}
							isDoneSearching = false;
							regListSearch = FXCollections.observableArrayList();
							for(String str : regList)
								if(str.contains(search))/*** Will be upgraded into nonsensitive ***/
									regListSearch.add(str);
							regModelComboBox.getItems().setAll(regListSearch);
							isDoneSearching = true;
						}
					});
				}
				return null;
			}
		};
		task.run();
	}

	/**
	 * Updates the bcds combo box according to user's input.
	 * @author orelzman
	 */
	public void onBCDModelChange(){
		if(!isDoneSearching)//It runs a search query from another search
			return;
		System.out.println("onBCDModelChange");

		Task<Void> task = new Task<Void>() {
			@Override protected Void call() throws Exception {
				for (int j=0; j<1;j++) {                
					Platform.runLater(new Runnable() {
						@Override 
						public void run(){
							System.out.println("onBCDModelChange");

							String search = bcdModelComboBox.getEditor().getText();
							if(search.equals("")){
								bcdModelComboBox.getItems().setAll(bcdList);
								return;
							}
							isDoneSearching = false;
							bcdListSearch = FXCollections.observableArrayList();
							for(String str : bcdList)
								if(str.contains(search))/*** Will be upgraded into nonsensitive ***/
									bcdListSearch.add(str);
							bcdModelComboBox.getItems().setAll(bcdListSearch);
							isDoneSearching = true;
						}
					});
				}
				return null;
			}
		};
		task.run();
	}
	/**
	 * Opens a customer search window, for the card report.
	 * @author orelzman
	 */
	public void onCCROwner(){//לשנות לחיפוש לפי CCROWNERS
		isBackFromSearch=false;
		Thread thread = new Thread(){
			public void run(){
				while(!isBackFromSearch)
					GM.Sleep(20);
				isBackFromSearch=false;
				ccrOwnerTextField.setText(customerChosen.getCustID());
			}
		};thread.start();
		GM.getPopup(Main.popup, "CustomerSearch", "סיסמת מנהל", "popup");	
		System.out.println("yo");
		Thread thread1 = new Thread(){
			public void run(){
				if(Windows.yesNo("האם אותו הלקוח מבצע את ההזמנה?", "בדיקת לקוח"))
					idTextField.setText(customerChosen.getCustID());	
			}
		};thread1.start();
	}

	/**
	 * Creates a thread that awaits the return of a result from the about-to-open customer search window.
	 * @author orelzman
	 */
	public void onSearch(){	
		isBackFromSearch=false;
		Thread thread = new Thread(){
			public void run(){
				while(!isBackFromSearch)
					GM.Sleep(20);
				isBackFromSearch=false;
				idTextField.setText(customerChosen.getCustID());			
			}
		};thread.start();
		GM.getPopup(Main.popup, "CustomerSearch", "סיסמת מנהל", "popup");	
	}

	/**
	 * Removes/changes/adds fields to the order window, depending on wether it's private equipment or deep's
	 * @author orelzman
	 */
	public void onDeepSelection(){
		privateCheckBox.setSelected(false);
		regDeepNumTextField.setPromptText("Deep Number");
		tankDeepNumTextField.setPromptText("Deep Number");
		ccrDeepNumTextField.setPromptText("Deep Number");
		chooseBCDButton.setVisible(true);
		bcdDeepNumTextField.setVisible(true);

	}
	/**
	 * Removes/changes/adds fields to the order window, depending on wether it's private equipment or deep's
	 *  @author orelzman
	 */
	public void onPrivateSelection(){
		deepCheckBox.setSelected(false);
		regDeepNumTextField.setPromptText("Serial Number");
		tankDeepNumTextField.setPromptText("Serial Number");
		ccrDeepNumTextField.setPromptText("Serial Number");
		chooseBCDButton.setVisible(false);
		bcdDeepNumTextField.setVisible(false);
	}
	
	
	public void chooseRegNumber(){
		
	}
	

	/**
	 * Checks the order fields and issues an order by writing the information into the description String which will later be shown to the tech
	 *  @author orelzman
	 */
	public void onIssueOrder(){
		String description = "", regNum = "", tankNum="", bcdNum="", ccrNum="",temp="", numsToServer="";
		if(regModelComboBox.getSelectionModel().getSelectedItem()!=null && !regDeepNumTextField.getText().equals(""))
			regNum=regDeepNumTextField.getText();
		if(!bcdDeepNumTextField.getText().equals("") && bcdModelComboBox.getSelectionModel().getSelectedItem()!=null)
			bcdNum = bcdDeepNumTextField.getText();
		if(tankManuComboBox.getSelectionModel().getSelectedItem()!=null && !tankDeepNumTextField.getText().equals(""))
			tankNum = tankDeepNumTextField.getText();
		if(!ccrOwnerTextField.getText().equals("") && !ccrDeepNumTextField.getText().equals(""))
			ccrNum = ccrDeepNumTextField.getText();
		if(!regNum.equals(""))
			temp+="REG" + regNum + ",";
		if(!bcdNum.equals(""))
			temp+="BCD" + bcdNum + ",";
		if(!tankNum.equals(""))
			temp+="TANK" + tankNum + ",";
		if(!ccrNum.equals(""))
			temp+="CCR" + ccrNum + ",";
		if(temp.equals(""))
			if(!Windows.yesNo("לא בחרת שום ציוד לתיקון. בטוח שברצונך להמשיך?", "", "כן", "לא"))
				return;
		for(int i=0;i<temp.length();i++)
			numsToServer+=temp.charAt(i);
		Order order = new Order();
		order.numsToServer = numsToServer;
		
		if(idTextField.getText().equals("")){
			Windows.warning("בחר לקוח לפניי שתמשיך!");
			return;
		}
		
		order.setCustID(idTextField.getText());
		order.setDate(GM.getCurrentDate());
		order.setComments(commentsTextArea.getText());
		order.setHandled(-1);



		GM.sendServer(order, "IssueOrder");
		while(Order.currentOrder.actionNow.equals("IssueOrder"))
			GM.Sleep(2);
		Windows.threadMessage("Order has been issued and will be soon reviewed by the tech.", "ORDER DISPATCHED!");


	}


	/**
	 * Checks the order fields and issues an order by writing the information into the description String which will later be shown to the tech
	 *  @author orelzman
	 */
	/*public void onIssueOrder(){
		String description="";//This String will be shown to the tech when he opens the ticket.
		System.out.println("issue");

		if(privateCheckBox.isSelected()){//Private Equipment
			if(regulatorCheckBox.isSelected()  && regDeepNumTextField.getText()!=null&&regModelComboBox.getSelectionModel().getSelectedIndex()<0){
				System.out.println("aye");
				description+="Regulator:\n -Serial Num: " + regDeepNumTextField.getText() + "\n -Model: " + regModelComboBox.getEditor().getText() + "\n";
			}
			if(bcdCheckBox.isSelected() && bcdModelComboBox.getSelectionModel().getSelectedIndex()<0 && bcdDeepNumTextField.getText()!=null)
				description+="BCD:\n -Model: " + bcdModelComboBox.getEditor().getText() + "\n";

			if(tankCheckBox.isSelected())
				description+="Tank:\n -Serial Num: " + tankDeepNumTextField.getText() + "\n -Manufacturer: " + tankManuTextField.getText() + "\n";

			if(ccrCheckBox.isSelected())
				description+="CCR:\n -Owner: "+ ccrOwnerTextField.getText() + "\n -Serial Number: " + ccrDeepNumTextField.getText() + "\n";

			if(idTextField.getText().equals("")){
				Windows.warning("You forget the customer's id!");
				return;
			}

		}else if(deepCheckBox.isSelected()){//Deep Equipment
			if(regulatorCheckBox.isSelected() && regDeepNumTextField.getText()!=null && regModelComboBox.getSelectionModel().getSelectedItem()!=null)
				description+="Regulator:\n -Deep Number: " + regDeepNumTextField.getText() + "\n -Model: " + regModelComboBox.getSelectionModel().getSelectedItem() + "\n";
			if(bcdCheckBox.isSelected()) 
				description+="BCD:\n -Model: " + bcdModelTextField.getText() + "\n";
			if(tankCheckBox.isSelected())
				description+="Tank:\n -Deep Number: " + tankDeepNumTextField.getText() + "\n -Manufacturer: " + tankManuTextField.getText() + "\n";
			if(ccrCheckBox.isSelected())
				description+="CCR:\n -Owner: "+ ccrOwnerTextField.getText() + "\n -Serial Number: " + ccrDeepNumTextField.getText() + "\n";
		}

		if(!(deepCheckBox.isSelected()||privateCheckBox.isSelected())){
			Windows.threadWarning("Tick the private/deep equipment before you proceed.");return;
		}
		if(!(ccrCheckBox.isSelected()||tankCheckBox.isSelected()||bcdCheckBox.isSelected()||regulatorCheckBox.isSelected()))//Nothing is ticked
			if(!Windows.yesNo("Are you sure you want nothing ticked?", "Sure?"))
				return;
			if(!Windows.yesNo("Are you sure that you've TICKED everything needed?", "Be completely sure!"))
				return;

		Order order = new Order(-1, idTextField.getText(), description,commentsTextArea.getText(), GM.getCurrentDate());
		Order.currentOrder = new Order();
		Order.currentOrder.actionNow="IssueOrder";
		System.out.println("description: " + description);
		GM.sendServer(order, "IssueOrder");
		while(Order.currentOrder.actionNow.equals("IssueOrder"))
			GM.Sleep(2);
		Windows.threadMessage("Order has been issued and will be soon reviewed by the tech.", "ORDER DISPATCHED!");

	}

	 */
	/**
	 * This method returns the user to the main screen
	 * @author orelzman
	 */
	public void onBack(){
		Main.showMenu("LoginWorkerScreen");
	}

	public void onClean(){
		commentsTextArea.setText("");
		regModelComboBox.getEditor().setText("");
		regDeepNumTextField.setText("");
		bcdModelTextField.setText("");
		bcdDeepNumTextField.setText("");
		tankDeepNumTextField.setText("");
		tankManuTextField.setText("");
		ccrOwnerTextField.setText("");
		idTextField.setText("");
		ccrDeepNumTextField.setText("");
	}
}
