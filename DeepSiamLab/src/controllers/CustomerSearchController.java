package controllers;

import java.util.ArrayList;
import java.util.HashMap;

import entities.Customer;
import entities.GeneralMethods;
import entities.Windows;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.Main;

public class CustomerSearchController {

	@FXML
	private TableView<Customer> customersTableView;
	@FXML
	private TextField idTextField, nameTextField, lastNameTextField, custIDTextField; 

	private GeneralMethods GM;
	public static boolean isBackFromServer;
	public static ArrayList<Customer> customerList, customerListSearch;
	private static HashMap<Integer, Integer> custToDelete;
	private static int doubleClick, selectedLine;


	/**
	 * Sets ENTER listeners, removes spaces from the information from the server and sets it into the tableview.
	 * @author orelzman
	 */
	public void initialize(){

		doubleClick=0;
		initTableView();
		isBackFromServer=false;
		GM = new GeneralMethods();
		setListener(idTextField);
		setListener(nameTextField);
		setListener(lastNameTextField);
		setListener(custIDTextField);
		customersTableView.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER)  {
					onSelect();
				}
			}
		});


		GM.sendServerJoin(new Customer(), "GetCustomers");
		while(!isBackFromServer)
			GM.Sleep(2);

		isBackFromServer=false;
		for(Customer customer : customerList){
			customer.setEmail(customer.getEmail().replaceAll("\\s++", ""));
			customer.setPhone(customer.getPhone().replaceAll("\\s++", ""));
			customer.setId(customer.getId().replaceAll("\\s++", ""));
			customer.setCustID(customer.getCustID().replaceAll("\\s++", ""));
			customer.setName(customer.getName().replaceAll("\\s++", ""));
			customer.setLastName(customer.getLastName().replaceAll("\\s++", ""));
		}
		customerListSearch = new ArrayList<Customer>(customerList);
		customersTableView.setPlaceholder(new Label("לא נמצאו לקוחות"));
		ObservableList<Customer> customers = FXCollections.observableArrayList(customerList);
		customersTableView.setItems(customers);
	}//End initialize


	/**
	 * Sets a key(ENTER) listener for a certain TextField.
	 * @param textField is the TextField that the listener is assigned to.
	 * @author orelzman
	 */
	public void setListener(TextField textField){
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER)  {
					onSearch();
				}
			}
		});
	}//end setListener


	/**
	 * Initializes the TableView with a Customer class.
	 * @author orelzman
	 */
	@SuppressWarnings("unchecked")
	public void initTableView(){
		TableColumn<Customer, String> name = (TableColumn<Customer, String>) customersTableView.getColumns().get(0);
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<Customer, String> lastName = (TableColumn<Customer, String>) customersTableView.getColumns().get(1);
		lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		TableColumn<Customer, String> custID = (TableColumn<Customer, String>) customersTableView.getColumns().get(2);
		custID.setCellValueFactory(new PropertyValueFactory<>("custID"));
		TableColumn<Customer, String> email = (TableColumn<Customer, String>) customersTableView.getColumns().get(3);
		email.setCellValueFactory(new PropertyValueFactory<>("email"));
		TableColumn<Customer, String> id = (TableColumn<Customer, String>) customersTableView.getColumns().get(4);
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumn<Customer, String> phone = (TableColumn<Customer, String>) customersTableView.getColumns().get(5);
		phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
	}

	
	/**
	 * Searches through the customer's list according to the user's filled fields, using a HashMap for low complexity.
	 * @author orelzman
	 */
	public void onSearch(){

		custToDelete = new HashMap<Integer, Integer>();
		String id=idTextField.getText(), name = nameTextField.getText();
		String lastName = lastNameTextField.getText(), custID = custIDTextField.getText();
		try{
			if(!id.equals(""))
				Integer.parseInt(id);
		}catch(NumberFormatException e){Windows.warning("תעודת זהות מורכבת מספרות בלבד");return;}
		try{
			if(!custIDTextField.getText().equals(""))
				Integer.parseInt(custID);
		}catch(NumberFormatException e){Windows.warning("מספר לקוח מורכב מספרות בלבד");return;}


		if(!id.equals("")&&GM.checkText(id))
			for(int i=0;i<customerList.size();i++)
				if(!customerList.get(i).getId().equals(id))
					custToDelete.put(i, i);

		if(!name.equals("")&&GM.checkText(name))
			for(int i=0;i<customerList.size();i++)
				if(!customerList.get(i).getName().contains(name))
					custToDelete.put(i, i);
				


		if(!lastName.equals("")&&GM.checkText(lastName))
			for(int i=0;i<customerList.size();i++)
				if(!customerList.get(i).getLastName().contains(lastName))
					custToDelete.put(i, i);


		if((!custID.equals(""))&&GM.checkText(custID))
			for(int i=0;i<customerList.size();i++)
				if(!customerList.get(i).getCustID().equals(custID))
					custToDelete.put(i, i);


		customerListSearch = new ArrayList<Customer>();

		for(int i=0;i<customerList.size();i++)
			if(custToDelete.get(i)!=null)
				custToDelete.remove(i);
			else
				custToDelete.put(i, i);

		for(int  x : custToDelete.keySet())
			customerListSearch.add(customerList.get(x));

		ObservableList<Customer> customers = FXCollections.observableArrayList(customerListSearch);
		customersTableView.setItems(customers);


	}
	
	
	/**
	 * Closes the customer search window.
	 * @author orelzman
	 */
	public void onBack(){
		GM.closePopup(Main.popup);
	}

	/**
	 * Sends back to OpenCardController information about the chosen customer, which will later be used to get the customer's id.
	 * @author orelzman
	 */
	public void onSelect(){
		OpenCardController.customerChosen = new Customer();
		OpenCardController.isBackFromSearch = true;
		if((OpenCardController.customerChosen=customersTableView.getSelectionModel().getSelectedItem())!=null)
			GM.closePopup(Main.popup);
		return;
	}
	
/**
 * Used to assure the double click safetly.
 * @author orelzman
 */
	public void onRelease(){
		if(doubleClick == 1&&selectedLine == customersTableView.getSelectionModel().getSelectedIndex())
			onSelect();
		else
			setDoubleClickThread();
	}

	
	/**
	 * Sets a double click thread with a timer, to make the double click accurate.
	 * @author orelzman
	 */
	public void setDoubleClickThread(){
		doubleClick=0;
		doubleClick++;
		selectedLine = customersTableView.getSelectionModel().getSelectedIndex();
		Thread doubleClickThread = new Thread(){
			public void run(){
				try {Thread.sleep(250);} catch (InterruptedException e) {e.printStackTrace();}
				doubleClick=0;
			}
		};doubleClickThread.start();
	}

	/**
	 * Clears all the fields on the window, for comfort.
	 * @author orelzman
	 */
	public void onRefresh(){//	private TextField idTextField, nameTextField, lastNameTextField, custIDTextField; 
		idTextField.setText("");
		nameTextField.setText("");
		lastNameTextField.setText("");
		custIDTextField.setText("");
		ObservableList<Customer> customers = FXCollections.observableArrayList(customerList);
		customersTableView.setItems(customers);

	}



}
