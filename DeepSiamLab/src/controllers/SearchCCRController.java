package controllers;

import java.util.ArrayList;

import entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;


public class SearchCCRController {
	@FXML
	private TableView<CCR> ccrTableView;
	@FXML
	private TextField manuTextField, modelTextField;

	private GeneralMethods GM = new GeneralMethods();

	private boolean newClick = true;
	public int doubleClick = 0, selectedIndex = -1;
	
	
	public void initialize(){
		initTable();
		ObservableList<CCR> ccrList = FXCollections.observableArrayList(GeneralMessage.getCcrList());
		ccrTableView.setItems(ccrList);
	}
	
	@SuppressWarnings("unchecked")
	public void initTable(){
		TableColumn<CCR, String> manufacturer = (TableColumn<CCR, String>) ccrTableView.getColumns().get(0);
		manufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
		TableColumn<CCR, String> model = (TableColumn<CCR, String>) ccrTableView.getColumns().get(1);
		model.setCellValueFactory(new PropertyValueFactory<>("model"));
		TableColumn<CCR, String> serialNum = (TableColumn<CCR, String>) ccrTableView.getColumns().get(2);
		serialNum.setCellValueFactory(new PropertyValueFactory<>("serialNum"));

	}
	
	public void onContinue(){
		if((OpenCardController.ccrChosen = ccrTableView.getSelectionModel().getSelectedItem()) == null ){
			Windows.threadWarning("לא נבחרה מערכת סגורה");
			return;
		}
		OpenCardController.ccrChosen=ccrTableView.getSelectionModel().getSelectedItem();
		onBack();
	}
	
	public void onSearch(){
		ArrayList<CCR> ccrSearch = new ArrayList<CCR>();
		for(CCR ccr : GeneralMessage.getCcrList())
			if(ccr.getManufacturer().toLowerCase().contains(manuTextField.getText().toLowerCase()) && 
					ccr.getModel().toLowerCase().contains(modelTextField.getText().toLowerCase()))
				ccrSearch.add(ccr);
		ObservableList<CCR> ccrList = FXCollections.observableArrayList(ccrSearch);
		ccrTableView.setItems(ccrList);
	}
	
	public void onBack(){
		GM.closePopup(Main.popup);

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


		if(ccrTableView.getSelectionModel().getSelectedIndex() != selectedIndex){
			newClick = true;
			selectedIndex = ccrTableView.getSelectionModel().getSelectedIndex();
			doubleClick = 1;
			thread.start();
			return;
		}
		onContinue();
	}
	
}
