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
	private CheckBox deepCheckBox, privateCheckBox;
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

	public static Customer customerChosen=null;
	public static BCD bcdChosen=null;
	public static Tank tankChosen=null;
	public static Regulator regChosen=null;
	public static CCR ccrChosen=null;

	private static ObservableList<String> regModelList, regListSearch, bcdModelList, bcdListSearch, tankModelList, tankListSearch,
	bcdManuList, regManuList, tankManuList, ccrManuList, ccrModelList, ccrListSearch, bcdDNumList, regDNumList, tankDNumList,
	ccrSNumList, regSNumList, tankSNumList;



	public void initialize(){		

		deepCheckBox.setSelected(false);
		privateCheckBox.setSelected(true);
		GM = new GeneralMethods();
		GeneralMessage.currentWindow = "OpenCard";
		
		
		onPrivateSelection();


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


		//Initialize comboboxes.
		initComboBoxes();


	}//END INITIALIZE


	public void initComboBoxes(){
		initModel();
		initManu();
		initDNum();
		initSNum();
	}

	/**
	 * Initalizes model ComboBoxes.
	 * @author orelzman
	 */
	public void initModel(){

		ArrayList<String> regsModel = new ArrayList<String>();
		ArrayList<String> bcdsModel = new ArrayList<String>();
		ArrayList<String> tanksModel = new ArrayList<String>();
		ArrayList<String> ccrsModel = new ArrayList<String>();

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
	}
	/**
	 * Initalizes manufacturer ComboBoxes.
	 * @author orelzman
	 */
	public void initManu(){
		
		ArrayList<String> bcdsManu = new ArrayList<String>();
		ArrayList<String> regsManu = new ArrayList<String>();
		ArrayList<String> tanksManu = new ArrayList<String>();		
		ArrayList<String> ccrsManu = new ArrayList<String>();

		// Manufacturer ~
		for(Regulator reg : GeneralMessage.getRegList())
			regsManu.add(reg.getManufacturer());
		for(BCD bcd : GeneralMessage.getBcdList())
			bcdsManu.add(bcd.getManufacturer());
		for(Tank tank : GeneralMessage.getTankList())
			tanksManu.add(tank.getManufacturer());
		for(CCR ccr : GeneralMessage.getCcrList())
			ccrsManu.add(ccr.getManufacturer());

		bcdManuList = FXCollections.observableArrayList(bcdsManu);
		regManuList = FXCollections.observableArrayList(regsManu);
		tankManuList = FXCollections.observableArrayList(tanksManu);
		ccrManuList = FXCollections.observableArrayList(ccrsManu);
		bcdManuComboBox.getItems().addAll(bcdManuList);
		regManuComboBox.getItems().addAll(regManuList);
		tankManuComboBox.getItems().addAll(tankManuList);
		ccrManuComboBox.getItems().addAll(ccrManuList);
	}
	/**
	 * Initalizes deep number  ComboBoxes.
	 * @author orelzman
	 */
	public void initDNum(){
		ArrayList<String> bcdsDNum = new ArrayList<String>();
		ArrayList<String> regsDNum = new ArrayList<String>();
		ArrayList<String> tanksDNum = new ArrayList<String>();	

		//DeepNum ~
		for(Regulator reg : GeneralMessage.getRegList())
			regsDNum.add(reg.getDeepNum());
		for(BCD bcd : GeneralMessage.getBcdList())
			bcdsDNum.add(bcd.getDeepNum());
		for(Tank tank : GeneralMessage.getTankList())
			tanksDNum.add(tank.getDeepNum());

		bcdDNumList = FXCollections.observableArrayList(bcdsDNum);
		regDNumList = FXCollections.observableArrayList(regsDNum);
		tankDNumList = FXCollections.observableArrayList(tanksDNum);
		bcdDNumComboBox.getItems().addAll(bcdDNumList);
		regDNumComboBox.getItems().addAll(regDNumList);
		tankDNumComboBox.getItems().addAll(tankDNumList);

	}
	/**
	 * Initalizes serial number ComboBoxes.
	 * @author orelzman
	 */
	public void initSNum(){
		ArrayList<String> regsSNum = new ArrayList<String>();
		ArrayList<String> tanksSNum = new ArrayList<String>();		
		ArrayList<String> ccrsSNum = new ArrayList<String>();

		//SerialNum ~ 

		for(Regulator reg : GeneralMessage.getRegList())
			regsSNum.add(reg.getSerialNum());
		for(CCR ccr : GeneralMessage.getCcrList())
			ccrsSNum.add(ccr.getSerialNum());
		for(Tank tank : GeneralMessage.getTankList())
			tanksSNum.add(tank.getSerialNum());

		ccrSNumList = FXCollections.observableArrayList(ccrsSNum);
		regSNumList = FXCollections.observableArrayList(regsSNum);
		tankSNumList = FXCollections.observableArrayList(tanksSNum);
		ccrSNumComboBox.getItems().addAll(ccrSNumList);
		regSNumComboBox.getItems().addAll(regSNumList);
		tankSNumComboBox.getItems().addAll(tankSNumList);

	}



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


		String description = "", regManu, regModel, tankManu, tankModel, bcdManu, bcdModel, ccrManu, ccrModel, regDNum, regSNum, tankDNum, tankSNum,
				bcdDNum, ccrSNum;
		Order.currentOrder = new Order();
		if(customerChosen != null)
			Order.currentOrder.setCustID(customerChosen.getCustID());
		Order.currentOrder.setDate(GM.getCurrentDate());
		Order.currentOrder.setHandled(-1);



		if(privateCheckBox.isSelected()){
			Order.currentOrder.setIsClubEquipment(false);
			
			if(!(regManu = regManuComboBox.getEditor().getText()).equals("") && !(regModel = regModelComboBox.getEditor().getText()).equals("")
					&& !(regSNum = regSNumComboBox.getEditor().getText()).equals("")){
				description += "Regulator: " + "\n" + "Model: " + regModel + "\n" + "Manufacturer: " + regManu + "\n"
						+ "Serial Number: " + regSNum + "\n";
			}
			if(!(tankManu = tankManuComboBox.getEditor().getText()).equals("") && !(tankModel = tankModelComboBox.getEditor().getText()).equals("")
					&& !(tankSNum = tankSNumComboBox.getEditor().getText()).equals("")){
				description+="Tank: " + "\n" + "Model: " + tankModel + "\n" + "Manufacturer: " + tankManu + "\n"
						+ "Serial Number: " + tankSNum + "\n";
			}
			if(!(bcdManu = bcdManuComboBox.getEditor().getText()).equals("") && !(bcdModel = bcdModelComboBox.getEditor().getText()).equals("")){
				description += "BCD: " + "\n" + "Model: " + bcdModel + "\n" + "Manufacturer: " + bcdManu + "\n";
			}
			if(!(ccrManu = ccrManuComboBox.getEditor().getText()).equals("") && !(ccrModel = ccrModelComboBox.getEditor().getText()).equals("")
					&& !(ccrSNum = ccrSNumComboBox.getEditor().getText()).equals("")){
				description += "CCR: " + "\n" + "Model: " + ccrManu + "\n" + "Manufacturer: " + ccrModel + "\n" + "Serial Number: " + ccrSNum + "\n" ;
			}
		}//end if
		
		else{
			Order.currentOrder.setIsClubEquipment(true);
			
			if(!(regManu = regManuComboBox.getEditor().getText()).equals("") && !(regModel = regModelComboBox.getEditor().getText()).equals("")
					&& !(regSNum = regSNumComboBox.getEditor().getText()).equals("") && !(regDNum = regDNumComboBox.getEditor().getText()).equals("")){
				description += "Regulator: " + "\n" + "Model: " + regModel + "\n" + "Manufacturer: " + regManu + "\n"
						+ "Serial Number: " + regSNum + "\n" + "Deep Number: " + regDNum + "\n";
			}
			if(!(tankManu = tankManuComboBox.getEditor().getText()).equals("") && !(tankModel = tankModelComboBox.getEditor().getText()).equals("")
					&& !(tankSNum = tankSNumComboBox.getEditor().getText()).equals("") && !(tankDNum = tankDNumComboBox.getEditor().getText()).equals("")){
				description+="Tank: " + "\n" + "Model: " + tankModel + "\n" + "Manufacturer: " + tankManu + "\n"
						+ "Serial Number: " + tankSNum + "\n" + "Deep Number: " + tankDNum + "\n";
			}
			if(!(bcdManu = bcdManuComboBox.getEditor().getText()).equals("") && !(bcdModel = bcdModelComboBox.getEditor().getText()).equals("")
					&& !(bcdDNum = bcdDNumComboBox.getEditor().getText()).equals("")){
				description += "BCD: " + "\n" + "Model: " + bcdModel + "\n" + "Manufacturer: " + bcdManu + "\n" 
						+ "Deep Number: " + bcdDNum  + "\n";
			}
			if(!(ccrManu = ccrManuComboBox.getEditor().getText()).equals("") && !(ccrModel = ccrModelComboBox.getEditor().getText()).equals("")
					&& !(ccrSNum = ccrSNumComboBox.getEditor().getText()).equals("")){
				description += "CCR: " + "\n" + "Model: " + ccrManu + "\n" + "Manufacturer: " + ccrModel + "\n" + "Serial Number: " + ccrSNum + "\n" ;
			}

		}//end else 


		if(!commentsTextArea.getText().equals(""))
			Order.currentOrder.setComments(commentsTextArea.getText());
		
		Order.currentOrder.customer = new Customer(nameTextField.getText(), lastNameTextField.getText(),idTextField.getText(), emailTextField.getText(), phoneTextField.getText(), 
				idTextField.getText());

		Order.currentOrder.setName(nameTextField.getText());
		Order.currentOrder.setLastName(lastNameTextField.getText());
		Order.currentOrder.setDescription(description);
		
		GM.sendServer(Order.currentOrder, "IssueOrder");
		while(Order.currentOrder.actionNow.equals("IssueOrder"))
			GM.Sleep(2);

		System.out.println("The order was issued");

		if(Order.currentOrder.actionNow != null && Order.currentOrder.actionNow.equals("NewClientOrder"))//A new client was added to the db.
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
							if(search.equals("")){
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
		ccrSNumComboBox.getEditor().setText(ccrChosen.getSerialNum());
	}

	public void findTank(){
		GM.getPopup(Main.popup, "SearchTank", "SearchTank", "popup");
		if(tankChosen == null)
			return;

		tankModelComboBox.getEditor().setText(tankChosen.getModel());
		tankManuComboBox.getEditor().setText(tankChosen.getManufacturer());
		tankSNumComboBox.getEditor().setText(tankChosen.getSerialNum());
		tankDNumComboBox.getEditor().setText(tankChosen.getDeepNum());
	}

	public void findBCD(){
		GM.getPopup(Main.popup, "SearchBCD", "SearchBCD", "popup");
		if(bcdChosen == null)
			return;


		System.out.println(bcdChosen.getModel() + bcdChosen.getDeepNum());

		bcdModelComboBox.getEditor().setText(bcdChosen.getModel());
		bcdManuComboBox.getEditor().setText(bcdChosen.getManufacturer());
		bcdDNumComboBox.getEditor().setText(bcdChosen.getDeepNum());



	}

	public void findReg(){
		GM.getPopup(Main.popup, "SearchReg", "SearchReg", "popup");
		if(regChosen == null)
			return;

		regModelComboBox.getEditor().setText(regChosen.getModel());
		regManuComboBox.getEditor().setText(regChosen.getManufacturer());
		regSNumComboBox.getEditor().setText(regChosen.getSerialNum());
		regDNumComboBox.getEditor().setText(regChosen.getDeepNum());

	}

	/**
	 * Updates the regs combo box according to user's input.
	 * @author orelzman
	 */

	public void onRegModelChange(){
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
		
		if(!deepCheckBox.isSelected()){//JavaFX first changes status then calls this handler
			deepCheckBox.setSelected(true);
			return;
		}
		
		privateCheckBox.setSelected(false);
		
		
		deepCheckBox.setSelected(true);
		
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
		if(!privateCheckBox.isSelected()){//JavaFX first changes status then calls this handler
			privateCheckBox.setSelected(true);
			return;
		}
		
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
		regSNumComboBox.getEditor().setText("");
		regDNumComboBox.getEditor().setText("");
		bcdModelComboBox.getEditor().setText("");
		bcdManuComboBox.getEditor().setText("");
		bcdDNumComboBox.getEditor().setText("");
		tankManuComboBox.getEditor().setText("");
		tankModelComboBox.getEditor().setText("");
		tankSNumComboBox.getEditor().setText("");
		tankDNumComboBox.getEditor().setText("");
		ccrManuComboBox.getEditor().setText("");
		ccrModelComboBox.getEditor().setText("");
		ccrSNumComboBox.getEditor().setText("");
		
		idTextField.setText("");
		nameTextField.setText("");
		lastNameTextField.setText("");
		emailTextField.setText("");
		phoneTextField.setText("");

	}
}
