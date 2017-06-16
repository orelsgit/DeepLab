package controllers;

import java.util.ArrayList;

import entities.BCD;
import entities.GeneralMessage;
import entities.GeneralMethods;
import entities.Windows;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;

public class SearchBCDController {

	@FXML
	private TableView<BCD> bcdTableView;
	@FXML
	private TextField manuTextField, modelTextField;
	
	private GeneralMethods GM = new GeneralMethods();
	
	private static int selectedIndex = -1;
	public static int doubleClick = 0;
	private static boolean newClick = true;
	
	public void initialize(){
		initTable();
		ObservableList<BCD> bcdList = FXCollections.observableArrayList(GeneralMessage.getBcdList());
		bcdTableView.setItems(bcdList);
	}
	
	@SuppressWarnings("unchecked")
	public void initTable(){
		TableColumn<BCD, String> manufacturer = (TableColumn<BCD, String>) bcdTableView.getColumns().get(0);
		manufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
		TableColumn<BCD, String> model = (TableColumn<BCD, String>) bcdTableView.getColumns().get(1);
		model.setCellValueFactory(new PropertyValueFactory<>("model"));
		TableColumn<BCD, String> deepNum = (TableColumn<BCD, String>) bcdTableView.getColumns().get(2);
		deepNum.setCellValueFactory(new PropertyValueFactory<>("deepNum"));
		
	}

	
	public void onSearch(){
		ArrayList<BCD> bcdSearch = new ArrayList<BCD>();
		for(BCD bcd : GeneralMessage.getBcdList())
			if(bcd.getManufacturer().toLowerCase().contains(manuTextField.getText().toLowerCase()) && 
					bcd.getModel().toLowerCase().contains(modelTextField.getText().toLowerCase()))
				bcdSearch.add(bcd);
		ObservableList<BCD> bcdList = FXCollections.observableArrayList(bcdSearch);
		bcdTableView.setItems(bcdList);
	}
	
	public void onBack(){
		GM.closePopup(Main.popup);
	}
	
	public void onContinue(){
		if((OpenCardController.bcdChosen = bcdTableView.getSelectionModel().getSelectedItem()) == null ){
			Windows.threadWarning("לא נבחר מאזן");
			return;
		}
		OpenCardController.bcdChosen = bcdTableView.getSelectionModel().getSelectedItem();
		onBack();
		
	}
	
	public void onDoubleClick(){
		Thread thread = new Thread(){
			public void run(){
				GM.Sleep(200, null, 0);
				if(newClick){
					newClick=false;
					return;
				}
				doubleClick = 0;
			}
		};
		
		
		if(bcdTableView.getSelectionModel().getSelectedIndex() != selectedIndex){
			newClick = true;
			selectedIndex = bcdTableView.getSelectionModel().getSelectedIndex();
			doubleClick = 1;
			thread.start();
			return;
		}
		onContinue();
		
		
		
	}

}
