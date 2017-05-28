package controllers;

import java.util.ArrayList;

import entities.Customer;
import entities.GeneralMessage;
import entities.GeneralMethods;
import entities.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import main.Main;

public class LabOrdersController {
	@FXML
	private TableView<Order> ordersTableView;
	@FXML
	private Text dateText;
	@FXML
	private TextField orderNumTextField, custIDTextField, dateTextField;//custIDTextField holds the name of the customer.

	public static ArrayList<Order> orderList;
	public static boolean isBackFromServer;
	public GeneralMethods GM;
	public static Order orderSelected;//This is the order the tech/manager double clicked.

	private static int doubleClick, selectedLine;
	private static Thread doubleClickThread;

	/**
	 * Initializes the TableView with orders that are yet to be reviewed and sets listeners for the search fields.
	 * @author orelzman
	 */
	

	public void initialize(){

		doubleClick=0;
		onNoDate();
		initTableView();
		initListeners();
		GM = new GeneralMethods();
		orderList = new ArrayList<Order>();
		isBackFromServer=false;
		
		orderList = new ArrayList<Order>(GeneralMessage.getUnhandledOrders());

		ObservableList<Order> orders = FXCollections.observableArrayList(orderList);
		ordersTableView.setItems(orders);
		
		

	}

	/**
	 * Initializes the TableView with Order class.
	 * @author orelzman
	 */
	@SuppressWarnings("unchecked")
	public void initTableView(){
//			System.out.println(order.getName() + " NAME");
//			for(Customer customer : GeneralMessage.getCustList())
//				if(customer.getCustID().equals(order.getCustID())){
//					order.setName(customer.getName());
//					order.setLastName(customer.getLastName());
//					System.out.println(customer.getName());
//					break;
//				}
//				}
		
		TableColumn<Order, String> customerName = (TableColumn<Order, String>) ordersTableView.getColumns().get(0);
		customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<Order, String> custID = (TableColumn<Order, String>) ordersTableView.getColumns().get(1);
		custID.setCellValueFactory(new PropertyValueFactory<>("clubOrPrivate"));
		TableColumn<Order, String> name = (TableColumn<Order, String>) ordersTableView.getColumns().get(3);
		name.setCellValueFactory(new PropertyValueFactory<>("date"));
		TableColumn<Order, String> description = (TableColumn<Order, String>) ordersTableView.getColumns().get(2);
		description.setCellValueFactory(new PropertyValueFactory<>("description"));
		TableColumn<Order, String> date = (TableColumn<Order, String>) ordersTableView.getColumns().get(4);
		date.setCellValueFactory(new PropertyValueFactory<>("comments"));
		
	}


	/**
	 * Searches through the order's list, according to the filled fields.
	 * @author orelzman
	 */
	public void onSearch(){
		onNoDate();
		ArrayList<Order> orderL = new ArrayList<Order>(orderList);
		ArrayList<Order> orderLFinal = new ArrayList<Order>();
		ArrayList<Integer> cellsToRemove = new ArrayList<Integer>();

		for(int i=0;i<orderList.size();i++){
			if((!dateTextField.getText().equals("")))	
				if(!orderL.get(i).getDate().contains(dateTextField.getText())){//Check date field
					cellsToRemove.add(i);
				}
		}
		for(int i=0;i<orderL.size();i++){
			if((!orderNumTextField.getText().equals("")))//Check orderNum field
				if(!(orderL.get(i).getOrderNum()==Integer.parseInt(orderNumTextField.getText())))
					if(!cellsToRemove.contains(i))
						cellsToRemove.add(i);
		}
		for(int i=0;i<orderL.size();i++){
			if(!custIDTextField.getText().equals(""))//Check custID field
				if(!custIDTextField.getText().contains(orderL.get(i).getCustID()))
					if(!cellsToRemove.contains(i))
						cellsToRemove.add(i);
		}

		for(int i = 0 ; i<orderL.size();i++)//O((cellsToRemove.size())^2) ->huge
			if(!cellsToRemove.contains(i))
				orderLFinal.add(orderL.get(i));

		ObservableList<Order> orders = FXCollections.observableArrayList(orderLFinal);
		ordersTableView.setItems(orders);

	}

	/**
	 * Initializes the listeners on the searching fields.
	 * @author orelzman
	 */
	public void initListeners(){

		orderNumTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER)  {
					onSearch();
				}
			}
		});
		custIDTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER)  {
					onSearch();
				}
			}
		});
		dateTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER)  {
					onSearch();
				}
			}
		});

		ordersTableView.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER)  {
					onSelect();
				}
			}
		});
	}
/**
 * Sets a static Order object that will contain information about the order that was chosen from the TableView.
 * @author orelzman
 */
	public void onSelect(){
		doubleClick = 0;
		if((orderSelected = ordersTableView.getSelectionModel().getSelectedItem())==null)
			return;
		OrderInfoController.equipmentCnt = 0;
		if(orderSelected.getDescription().contains("BCD")){
			++OrderInfoController.equipmentCnt;
		}
		if(orderSelected.getDescription().contains("Regulator")){
			++OrderInfoController.equipmentCnt;
		}
		if(orderSelected.getDescription().contains("Tank")){
			++OrderInfoController.equipmentCnt;
		}
		if(orderSelected.getDescription().contains("CCR")){
			++OrderInfoController.equipmentCnt;
		}
		Main.showMenu("OrderInfo");
	}

	/**
	 * Assures the doubleclick happened safetly and correctly.
	 * @author orelzman
	 */
	public void onRelease(){
		if(doubleClick == 1&&selectedLine == ordersTableView.getSelectionModel().getSelectedIndex())
			onSelect();
		else
			setDoubleClickThread();
	}

	/**
	 * Sets a double click thread timer, to assure an accurate double click.
	 * @author orelzman
	 */
	public void setDoubleClickThread(){
		doubleClick=0;
		doubleClick++;
		selectedLine = ordersTableView.getSelectionModel().getSelectedIndex();
		doubleClickThread = new Thread(){
			public void run(){
				try {Thread.sleep(250);} catch (InterruptedException e) {e.printStackTrace();}
				doubleClick=0;
			}
		};doubleClickThread.start();
	}
	
	/**
	 * Sets the date text, to provide the tech with the date format.
	 * @author orelzman
	 */
	public void onDate(){
		dateText.setVisible(true);
	}
	
	/**
	 * removes the date text that provides the tech with the date format.
	 * @author orelzman
	 */
	public void onNoDate(){
		dateText.setVisible(false);
	}
	
	/**
	 * Closes the orders window.
	 * @author orelzman
	 */
	public void onBack(){
		Main.showMenu("LoginWorkerScreen");
	}

}
