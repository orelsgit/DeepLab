package controllers;



import java.util.ArrayList;

import entities.BCD;
import entities.CCR;
import entities.Customer;
import entities.GeneralMessage;
import entities.GeneralMethods;
import entities.Order;
import entities.Regulator;
import entities.Tank;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.Main;

public class OpenCardController {


	@FXML
	private CheckBox regulatorCheckBox, bcdCheckBox, ccrCheckBox, tankCheckBox, deepCheckBox, privateCheckBox;
	@FXML
	private TextArea commentsTextArea;
	@FXML
	private TextField /*Change to Model*/regManuTextField,  bcdModelTextField,bcdManuTextField, tankManuTextField, 
	ccrOwnerTextField, ccrManuTextField, idTextField;
	@FXML
	private ComboBox<String> regModelComboBox, bcdModelComboBox, tankModelComboBox, bcdManuComboBox, regManuComboBox, tankManuComboBox;
	@FXML
	private Button chooseBCDButton;
	@FXML
	Text bcdText, regText, tankText, ccrText;

	private GeneralMethods GM;

	public static boolean isBackFromSearch, isDoneSearching = true/*For the search method*/;

	public static Customer customerChosen;
	public static BCD bcdChosen;
	public static Tank tankChosen;
	public static Regulator regChosen;
	public static CCR ccrChosen;
	
	private static ObservableList<String> regModelList, regListSearch, bcdModelList, bcdListSearch, tankModelList, tankListSearch,
	bcdManuList, regManuList, tankManuList;



	public void initialize(){

		deepCheckBox.setSelected(false);
		privateCheckBox.setSelected(true);
		idTextField.setEditable(false);
		GM = new GeneralMethods();
		GeneralMessage.currentWindow = "OpenCard";
		regModelComboBox.setVisibleRowCount(GeneralMessage.getRegList().size());//Sets the amount of rows according to the maximum amount of regulators in the list.
		bcdModelComboBox.setVisibleRowCount(GeneralMessage.getBcdList().size());//Same for the bcds
		tankModelComboBox.setVisibleRowCount(GeneralMessage.getTankList().size());


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


		ArrayList<String> regsModel = new ArrayList<String>();
		ArrayList<String> bcdsModel = new ArrayList<String>();
		ArrayList<String> tanksModel = new ArrayList<String>();
		ArrayList<String> bcdsManu = new ArrayList<String>();
		ArrayList<String> regsManu = new ArrayList<String>();
		ArrayList<String> tanksManu = new ArrayList<String>();		
		
// Models ~
		for(Regulator reg : GeneralMessage.getRegList())
			regsModel.add(reg.getModel().replaceAll("\\s+", ""));
		for(BCD bcd : GeneralMessage.getBcdList())
			bcdsModel.add(bcd.getModel().replaceAll("\\s+", ""));
		for(Tank tank : GeneralMessage.getTankList())
			tanksModel.add(tank.getModel().replaceAll("\\s+", ""));	


		bcdModelList = FXCollections.observableArrayList(regsModel);
		regModelList = FXCollections.observableArrayList(bcdsModel);
		tankModelList = FXCollections.observableArrayList(tanksModel);
		regModelComboBox.getItems().addAll(regModelList);
		bcdModelComboBox.getItems().addAll(bcdModelList);
		tankModelComboBox.getItems().addAll(tankModelList);
		
// Manufacturer ~
		for(Regulator reg : GeneralMessage.getRegList())
			regsManu.add(reg.getManufacturer().replaceAll("\\s+", ""));
		for(BCD bcd : GeneralMessage.getBcdList())
			bcdsManu.add(bcd.getManufacturer().replaceAll("\\s+", ""));
		for(Tank tank : GeneralMessage.getTankList())
			tanksManu.add(tank.getManufacturer().replaceAll("\\s+", ""));	
		
		bcdManuList = FXCollections.observableArrayList(bcdsManu);
		regManuList = FXCollections.observableArrayList(regsManu);
		tankManuList = FXCollections.observableArrayList(tanksManu);
		bcdManuComboBox.getItems().addAll(bcdManuList);
		regManuComboBox.getItems().addAll(regManuList);
		tankManuComboBox.getItems().addAll(tankManuList);
		
	}
	
	
	public void onBCDEntered(){
		bcdText.setFill(Color.BLUE);
	}
	
	public void onBCDExited(){
		bcdText.setFill(Color.BLACK);
	}
	
	
	public void onRegEntered(){
		regText.setFill(Color.BLUE);
	}
	
	public void onRegExited(){
		regText.setFill(Color.BLACK);
	}
	
	public void onTankEntered(){
		tankText.setFill(Color.BLUE);
	}
	
	public void onTankExited(){
		tankText.setFill(Color.BLACK);
	}
	
	public void onCCREntered(){
		ccrText.setFill(Color.BLUE);
	}
	
	public void onCCRExited(){
		ccrText.setFill(Color.BLACK);
	}
	
	
	public void onMenuChange(ComboBox<String> comboBox, ObservableList<String> itemList, ObservableList<String> itemSearchList ){
		if(!isDoneSearching)
			return;
		Task<Void> task = new Task<Void>() {
			@Override 
			protected Void call() throws Exception {
				for (int i=0; i<1;i++) {                
					Platform.runLater(new Runnable() {
						@Override 
						public void run(){
							String search = comboBox.getEditor().getText();
							if(search.equals("") || !search.matches(".*[a-z].*")){
								comboBox.getEditor().setText("");
								comboBox.getItems().setAll(itemList);
								return;
							}
							isDoneSearching = false;
							for(String str : itemList)
								if(str.toLowerCase().contains(search.toLowerCase()))
									itemSearchList.add(str);
							comboBox.getItems().setAll(itemSearchList);
							isDoneSearching = true;
						}
					});
				}
				return null;
			}
		};
		task.run();
	}
	
	
	public void onTankManuChange(){
		tankListSearch = FXCollections.observableArrayList();
		onMenuChange(tankManuComboBox, tankManuList, tankListSearch);
	}
	
	public void onRegManuChange(){
		regListSearch = FXCollections.observableArrayList();
		onMenuChange(regManuComboBox, regManuList, regListSearch);
	}
	
	public void onBCDManuChange(){
		bcdListSearch = FXCollections.observableArrayList();
		onMenuChange(bcdManuComboBox, bcdManuList, bcdListSearch);
	}
	
	public void onTankModelChange(){
		tankListSearch = FXCollections.observableArrayList();
		onMenuChange(tankModelComboBox, tankModelList, tankListSearch);
	}
	
	
	public void findTank(){
		GM.getPopup(Main.popup, "SearchTank", "SearchTank", "popup");
		if(tankChosen == null)
			return;
	
		tankModelComboBox.getEditor().setText(tankChosen.getModel());
		tankManuComboBox.getEditor().setText(tankChosen.getManufacturer());
	}
	
	public void findBCD(){
		GM.getPopup(Main.popup, "SearchBCD", "SearchBCD", "popup");
		if(bcdChosen == null)
			return;
	
		bcdModelComboBox.getEditor().setText(bcdChosen.getModel());
		bcdManuComboBox.getEditor().setText(bcdChosen.getManufacturer());
	}
	
	public void findReg(){
		GM.getPopup(Main.popup, "SearchReg", "SearchReg", "popup");
		if(regChosen == null)
			return;
		
		regModelComboBox.getEditor().setText(regChosen.getModel());
		regManuComboBox.getEditor().setText(regChosen.getManufacturer());
	}

	/**
	 * Updates the regs combo box according to user's input.
	 * @author orelzman
	 */

	public void onRegModelChange(){
		System.out.println("Reg");
		regListSearch = FXCollections.observableArrayList();
		onMenuChange(regModelComboBox, regModelList, regListSearch);
	}

	/**
	 * Updates the bcds combo box according to user's input.
	 * @author orelzman
	 */
	public void onBCDModelChange(){
		System.out.println("BCD");
		bcdListSearch = FXCollections.observableArrayList();
		onMenuChange(bcdModelComboBox, bcdModelList, bcdListSearch);

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
		GM.getPopup(Main.popup, "CCROwnerSearch", "סיסמת מנהל", "popup");	
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


	}
	/**
	 * Removes/changes/adds fields to the order window, depending on wether it's private equipment or deep's
	 *  @author orelzman
	 */
	public void onPrivateSelection(){
		deepCheckBox.setSelected(false);

	}
	

	
	

	/**
	 * Checks the order fields and issues an order by writing the information into the description String which will later be shown to the tech
	 *  @author orelzman
	 */
	public void onIssueOrder(){
		String  regModel = "", tankModel="", bcdModel="", ccrModel="",temp="";//, modelsToServer="";
		
		if(regModelComboBox.getSelectionModel().getSelectedItem()!=null && !regManuTextField.getText().equals(""))
			regModel=regManuTextField.getText();
		System.out.println("  as" + bcdModelComboBox.getSelectionModel().getSelectedIndex());
		if(!(bcdManuComboBox.getEditor().getText().equals("")) || bcdModelComboBox.getSelectionModel().getSelectedItem()!=null)
			bcdModel = "מודל:" +  bcdModelComboBox.getSelectionModel().getSelectedItem() + "\n" + "יצרן:" + bcdManuComboBox.getEditor().getText();
		System.out.println("yo" + bcdModel);
		
		if(tankModelComboBox.getSelectionModel().getSelectedItem()!=null && !tankManuTextField.getText().equals(""))
			tankModel = "מודל:" + tankManuTextField.getText()  + "\n" + "יצרן:" + tankModelComboBox.getSelectionModel().getSelectedItem();
		System.out.println(tankModel);
		
		if(!ccrOwnerTextField.getText().equals("") && !ccrManuTextField.getText().equals(""))
			ccrModel = ccrManuTextField.getText();
		
		if(!regModel.equals("")||!regManuTextField.getText().equals(""))
			temp+="וסת:" + "\n" +  "מודל:" + regModel + "\n" + "יצרן:" + ccrManuTextField.getText();
		
		if(!bcdModel.equals("")||!bcdManuComboBox.getEditor().getText().equals(""))
			temp+="מאזן:" + "\n";
		
		if(!tankModel.equals("")||!tankManuTextField.getText().equals(""))
			temp+="מיכל:" + "\n";
		
		if(!ccrModel.equals("")||!ccrManuTextField.getText().equals(""))
			temp+="מערכת סגורה:" + "\n" +  "   מודל:" + ccrModel + "\n" + "יצרן:" + ccrManuTextField.getText();
		
		if(temp.equals(""))
			if(!Windows.yesNo("לא בחרת שום ציוד לתיקון. בטוח שברצונך להמשיך?", "", "כן", "לא"))
				return;
	//	for(int i=0;i<temp.length();i++)
		//	modelsToServer+=temp.charAt(i);
		Order order = new Order();
		order.modelsToServer = temp;
		Windows.message(temp, "Order");
		
		if(idTextField.getText().equals("")){
			Windows.warning("בחר לקוח לפניי שתמשיך!");
			return;
		}
		
		order.setCustID(idTextField.getText());
		order.setDate(GM.getCurrentDate());
		order.setComments(commentsTextArea.getText());
		order.setHandled(-1);



	//	GM.sendServer(order, "IssueOrder");
		//while(Order.currentOrder.actionNow.equals("IssueOrder"))
		//	GM.Sleep(2);
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
			if(regulatorCheckBox.isSelected()  && regManuTextField.getText()!=null&&regModelComboBox.getSelectionModel().getSelectedIndex()<0){
				System.out.println("aye");
				description+="Regulator:\n -Serial Num: " + regManuTextField.getText() + "\n -Model: " + regModelComboBox.getEditor().getText() + "\n";
			}
			if(bcdCheckBox.isSelected() && bcdModelComboBox.getSelectionModel().getSelectedIndex()<0 && bcdManuTextField.getText()!=null)
				description+="BCD:\n -Model: " + bcdModelComboBox.getEditor().getText() + "\n";

			if(tankCheckBox.isSelected())
				description+="Tank:\n -Serial Num: " + tankManuTextField.getText() + "\n -Manufacturer: " + tankManuTextField.getText() + "\n";

			if(ccrCheckBox.isSelected())
				description+="CCR:\n -Owner: "+ ccrOwnerTextField.getText() + "\n -Serial Number: " + ccrManuTextField.getText() + "\n";

			if(idTextField.getText().equals("")){
				Windows.warning("You forget the customer's id!");
				return;
			}

		}else if(deepCheckBox.isSelected()){//Deep Equipment
			if(regulatorCheckBox.isSelected() && regManuTextField.getText()!=null && regModelComboBox.getSelectionModel().getSelectedItem()!=null)
				description+="Regulator:\n -Deep Number: " + regManuTextField.getText() + "\n -Model: " + regModelComboBox.getSelectionModel().getSelectedItem() + "\n";
			if(bcdCheckBox.isSelected()) 
				description+="BCD:\n -Model: " + bcdModelTextField.getText() + "\n";
			if(tankCheckBox.isSelected())
				description+="Tank:\n -Deep Number: " + tankManuTextField.getText() + "\n -Manufacturer: " + tankManuTextField.getText() + "\n";
			if(ccrCheckBox.isSelected())
				description+="CCR:\n -Owner: "+ ccrOwnerTextField.getText() + "\n -Serial Number: " + ccrManuTextField.getText() + "\n";
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
		regManuTextField.setText("");
		bcdModelTextField.setText("");
		bcdManuComboBox.getEditor().setText("");
		tankManuTextField.setText("");
		tankManuTextField.setText("");
		ccrOwnerTextField.setText("");
		idTextField.setText("");
		ccrManuTextField.setText("");
	}
}
