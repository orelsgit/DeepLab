package controllers;



import java.util.ArrayList;

import entities.BCD;
import entities.CCR;
import entities.Customer;
import entities.GeneralMessage;
import entities.GeneralMethods;
import entities.Order;
import entities.Regulator;
import entities.Status;
import entities.Tank;
import entities.Windows;
import entities.Worker;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.Main;

public class OpenCardController {


	@FXML
	private CheckBox regulatorCheckBox, bcdCheckBox, ccrCheckBox, tankCheckBox, deepCheckBox, privateCheckBox;
	@FXML
	private TextArea commentsTextArea;
	@FXML
	private TextField nameTextField, lastNameTextField, emailTextField, phoneTextField, idTextField;
	@FXML
	private Text nameText, lastNameText, emailText, phoneText, idText,bcdText, regText, tankText, ccrText;
	@FXML
	private ComboBox<String> regModelComboBox, bcdModelComboBox, tankModelComboBox, bcdManuComboBox, regManuComboBox, tankManuComboBox, ccrModelComboBox
	,ccrManuComboBox, ccrSNumComboBox, regSNumComboBox, tankSNumComboBox, bcdSNumComboBox, bcdDNumComboBox, regDNumComboBox, tankDNumComboBox;
	@FXML
	private Button chooseBCDButton, backButton;
	@FXML
	AnchorPane anchorPane;

	private GeneralMethods GM;

	public static boolean isBackFromSearch, isDoneSearching = true/*For the search method*/;

	public static Customer customerChosen;
	public static BCD bcdChosen;
	public static Tank tankChosen;
	public static Regulator regChosen;
	public static CCR ccrChosen;

	private static ObservableList<String> regModelList, regListSearch, bcdModelList, bcdListSearch, tankModelList, tankListSearch,
	bcdManuList, regManuList, tankManuList, ccrManuList, ccrModelList, ccrListSearch;



	public void initialize(){		
		
		deepCheckBox.setSelected(false);
		privateCheckBox.setSelected(true);
		GM = new GeneralMethods();
		GeneralMessage.currentWindow = "OpenCard";
		regModelComboBox.setVisibleRowCount(GeneralMessage.getRegList().size());//Sets the amount of rows according to the maximum amount of regulators in the list.
		bcdModelComboBox.setVisibleRowCount(GeneralMessage.getBcdList().size());//Same for the bcds
		tankModelComboBox.setVisibleRowCount(GeneralMessage.getTankList().size());

		if(Worker.getCurrentWorker().getIsManager()==Status.Dalpak)
			backButton.setVisible(false);

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
		ArrayList<String> ccrsModel = new ArrayList<String>();

		ArrayList<String> bcdsManu = new ArrayList<String>();
		ArrayList<String> regsManu = new ArrayList<String>();
		ArrayList<String> tanksManu = new ArrayList<String>();		
		ArrayList<String> ccrsManu = new ArrayList<String>();

		// Models ~


		for(Regulator reg : GeneralMessage.getRegList())
			regsModel.add(reg.getModel());//.replaceAll("\\s+", ""));
		for(BCD bcd : GeneralMessage.getBcdList())
			bcdsModel.add(bcd.getModel());//.replaceAll("\\s+", ""));
		for(Tank tank : GeneralMessage.getTankList())
			tanksModel.add(tank.getModel());//.replaceAll("\\s+", ""));	
		for(CCR ccr : GeneralMessage.getCcrList())
			ccrsModel.add(ccr.getModel());//.replaceAll("\\s+", ""));	



		bcdModelList = FXCollections.observableArrayList(bcdsModel);
		regModelList = FXCollections.observableArrayList(regsModel);
		tankModelList = FXCollections.observableArrayList(tanksModel);
		ccrModelList = FXCollections.observableArrayList(ccrsModel);
		regModelComboBox.getItems().addAll(regModelList);
		bcdModelComboBox.getItems().addAll(bcdModelList);
		tankModelComboBox.getItems().addAll(tankModelList);
		ccrModelComboBox.getItems().addAll(ccrModelList);

		// Manufacturer ~
		for(Regulator reg : GeneralMessage.getRegList())
			regsManu.add(reg.getManufacturer());//.replaceAll("\\s+", ""));
		for(BCD bcd : GeneralMessage.getBcdList())
			bcdsManu.add(bcd.getManufacturer());//.replaceAll("\\s+", ""));
		for(Tank tank : GeneralMessage.getTankList())
			tanksManu.add(tank.getManufacturer());//.replaceAll("\\s+", ""));	
		for(CCR ccr : GeneralMessage.getCcrList())
			ccrsManu.add(ccr.getManufacturer());//.replaceAll("\\s+", ""));	

		bcdManuList = FXCollections.observableArrayList(bcdsManu);
		regManuList = FXCollections.observableArrayList(regsManu);
		tankManuList = FXCollections.observableArrayList(tanksManu);
		ccrManuList = FXCollections.observableArrayList(ccrsManu);
		bcdManuComboBox.getItems().addAll(bcdManuList);
		regManuComboBox.getItems().addAll(regManuList);
		tankManuComboBox.getItems().addAll(tankManuList);
		ccrManuComboBox.getItems().addAll(ccrManuList);
		

	}//END INITIALIZE


	public void onIssueOrder(){
		nameText.setFill(Color.BLACK);
		lastNameText.setFill(Color.BLACK);
		emailText.setFill(Color.BLACK);
		phoneText.setFill(Color.BLACK);
		idText.setFill(Color.BLACK);


		boolean isFull = true;
		if(nameTextField.getText().equals("")){
			nameText.setFill(Color.RED);
			isFull = false;
		}
		if(lastNameTextField.getText().equals("")){
			lastNameText.setFill(Color.RED);
			isFull = false;
		}

		if(phoneTextField.getText().equals("")){
			phoneText.setFill(Color.RED);
			isFull = false;
		}
		if(idTextField.getText().equals("")){
			idText.setFill(Color.RED);
			isFull = false;
		}
		if(!isFull)
			return;
		

		String description = "", regManu, regModel, tankManu, tankModel, bcdManu, bcdModel, ccrManu, ccrModel;
		Order.currentOrder = new Order();
		if(customerChosen != null)
			Order.currentOrder.setCustID(customerChosen.getCustID());
		Order.currentOrder.setDate(GM.getCurrentDate());
		Order.currentOrder.setHandled(-1);

		if(!(regManu = regManuComboBox.getEditor().getText()).equals("") && !(regModel = regModelComboBox.getEditor().getText()).equals("")){
			description += "Regulator: " + "\n" + "Model: " + regModel + "\n" + "Manufacturer: " + regManu + "\n";
		}
		if(!(tankManu = tankManuComboBox.getEditor().getText()).equals("") && !(tankModel = tankModelComboBox.getEditor().getText()).equals("")){
			description+="Tank: " + "\n" + "Model: " + tankModel + "\n" + "Manufacturer: " + tankManu + "\n"; 
		}
		if(!(bcdManu = bcdManuComboBox.getEditor().getText()).equals("") && !(bcdModel = bcdModelComboBox.getEditor().getText()).equals("")){
			description += "BCD: " + "\n" + "Model: " + bcdModel + "\n" + "Manufacturer: " + bcdManu + "\n";
		}
		if(!(ccrManu = ccrManuComboBox.getEditor().getText()).equals("") && !(ccrModel = ccrModelComboBox.getEditor().getText()).equals("")){
			description += "CCR: " + "\n" + "Model: " + ccrManu + "\n" + "Manufacturer: " + ccrModel + "\n";
		}
		Order.currentOrder.setDescription(description);


		if(!commentsTextArea.getText().equals(""))
			Order.currentOrder.setComments(commentsTextArea.getText());

		if(privateCheckBox.isSelected())
			Order.currentOrder.setIsClubEquipment(false);
		else
			Order.currentOrder.setIsClubEquipment(true);
		
		Order.currentOrder.customer = new Customer(nameTextField.getText(), lastNameTextField.getText(),idTextField.getText(), emailTextField.getText(), phoneTextField.getText(), 
				idTextField.getText());
		
		Order.currentOrder.setName(nameTextField.getText());
		Order.currentOrder.setLastName(lastNameTextField.getText());
		
		GM.sendServer(Order.currentOrder, "IssueOrder");
		while(Order.currentOrder.actionNow.equals("IssueOrder"))
			GM.Sleep(2);
		
		System.out.println("The order was issued");
		
		if(Order.currentOrder.actionNow.equals("NewClientOrder"))//A new client was added to the db.
			Windows.message("לקוח חדש נוסף למערכת", "לקוח חדש");
		
		
		Windows.threadMessage("הכרטיס נפתח והועבר לרשימת הטכנאי.", "כרטיס חדש");
		Windows.message(description, "תיאור הזמנה");

	}

	public void onBCDEntered(){
		bcdText.setUnderline(true);
	}

	public void onBCDExited(){
		bcdText.setUnderline(false);
	}


	public void onRegEntered(){
		regText.setUnderline(true);
	}

	public void onRegExited(){
		regText.setUnderline(false);
	}

	public void onTankEntered(){
		tankText.setUnderline(true);
	}

	public void onTankExited(){
		tankText.setUnderline(false);
	}

	public void onCCREntered(){
		ccrText.setUnderline(true);
	}

	public void onCCRExited(){
		ccrText.setUnderline(false);
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


	public void onCCRModelChange(){
		ccrListSearch = FXCollections.observableArrayList();
		onMenuChange(ccrModelComboBox, ccrModelList, ccrListSearch);
	}

	public void onCCRManuChange(){
		ccrListSearch = FXCollections.observableArrayList();
		onMenuChange(ccrManuComboBox, ccrManuList, ccrListSearch);
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


	public void findCCR(){
		GM.getPopup(Main.popup, "SearchCCR","SearchCCR", "popup");
		if(ccrChosen == null)
			return;

		ccrModelComboBox.getEditor().setText(ccrChosen.getModel());
		ccrManuComboBox.getEditor().setText(ccrChosen.getManufacturer());
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

		bcdModelComboBox.setEditable(true);
		bcdManuComboBox.setEditable(true);
		bcdDNumComboBox.setEditable(true);
		
		System.out.println(bcdChosen.getModel() + bcdChosen.getDeepNum());
		
		bcdModelComboBox.getEditor().setText(bcdChosen.getModel());
		bcdManuComboBox.getEditor().setText(bcdChosen.getManufacturer());
		bcdDNumComboBox.getEditor().setText(bcdChosen.getDeepNum());
		
		bcdModelComboBox.setEditable(false);
		bcdManuComboBox.setEditable(false);
		bcdDNumComboBox.setEditable(false);
		
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
		System.out.println("onRegModelChange");
		regListSearch = FXCollections.observableArrayList();
		onMenuChange(regModelComboBox, regModelList, regListSearch);
	}

	/**
	 * Updates the bcds combo box according to user's input.
	 * @author orelzman
	 */
	public void onBCDModelChange(){
		bcdListSearch = FXCollections.observableArrayList();
		onMenuChange(bcdModelComboBox, bcdModelList, bcdListSearch);

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
		if(customerChosen == null)
			return;
		nameTextField.setText(customerChosen.getName());
		lastNameTextField.setText(customerChosen.getLastName());
		emailTextField.setText(customerChosen.getEmail());
		phoneTextField.setText(customerChosen.getPhone());
		idTextField.setText(customerChosen.getId());

	}

	/**
	 * Removes/changes/adds fields to the order window, depending on wether it's private equipment or deep's
	 * @author orelzman
	 */
	public void onDeepSelection(){
		privateCheckBox.setSelected(false);
		tankDNumComboBox.setVisible(true);
		tankDNumComboBox.setEditable(true);
		regDNumComboBox.setVisible(true);
		regDNumComboBox.setEditable(true);
		bcdDNumComboBox.setVisible(true);
		bcdDNumComboBox.setEditable(true);


	}
	/**
	 * Removes/changes/adds fields to the order window, depending on wether it's private equipment or deep's
	 *  @author orelzman
	 */
	public void onPrivateSelection(){
		deepCheckBox.setSelected(false);
		tankDNumComboBox.setVisible(false);
		tankDNumComboBox.setEditable(false);
		regDNumComboBox.setVisible(false);
		regDNumComboBox.setEditable(false);
		bcdDNumComboBox.setVisible(false);
		bcdDNumComboBox.setEditable(false);

	}





	/**
	 * Checks the order fields and issues an order by writing the information into the description String which will later be shown to the tech
	 *  @author orelzman
	 */

	/**
	 * This method returns the user to the main screen
	 * @author orelzman
	 */
	public void onBack(){
		if(Worker.getCurrentWorker().getIsManager()==Status.Dalpak)
			return;
		Main.showMenu("LoginWorkerScreen");
	}

	public void onClean(){
		commentsTextArea.setText("");
		regModelComboBox.getEditor().setText("");
		regManuComboBox.getEditor().setText("");
		bcdModelComboBox.getEditor().setText("");
		bcdManuComboBox.getEditor().setText("");
		tankManuComboBox.getEditor().setText("");
		tankManuComboBox.getEditor().setText("");
		idTextField.setText("");
		ccrManuComboBox.getEditor().setText("");
		nameTextField.setText("");
		lastNameTextField.setText("");
		emailTextField.setText("");
		phoneTextField.setText("");

	}
}
