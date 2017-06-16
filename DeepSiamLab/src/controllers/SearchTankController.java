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


public class SearchTankController {
	@FXML
	private TableView<Tank> tankTableView;
	@FXML
	private TextField manuTextField, modelTextField;

	private GeneralMethods GM = new GeneralMethods();

	private boolean newClick = true;
	public int doubleClick = 0, selectedIndex = -1;
	
	
	public void initialize(){
		initTable();
		ObservableList<Tank> tankList = FXCollections.observableArrayList(GeneralMessage.getTankList());
		tankTableView.setItems(tankList);
	}
	
	@SuppressWarnings("unchecked")
	public void initTable(){
		TableColumn<Tank, String> manufacturer = (TableColumn<Tank, String>) tankTableView.getColumns().get(0);
		manufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
		TableColumn<Tank, String> model = (TableColumn<Tank, String>) tankTableView.getColumns().get(1);
		model.setCellValueFactory(new PropertyValueFactory<>("model"));
		TableColumn<Tank, String> serialNum = (TableColumn<Tank, String>) tankTableView.getColumns().get(2);
		serialNum.setCellValueFactory(new PropertyValueFactory<>("serialNum"));
		TableColumn<Tank, String> deepNum = (TableColumn<Tank, String>) tankTableView.getColumns().get(3);
		deepNum.setCellValueFactory(new PropertyValueFactory<>("deepNum"));
	}
	
	public void onContinue(){
		if((OpenCardController.tankChosen = tankTableView.getSelectionModel().getSelectedItem()) == null ){
			Windows.threadWarning("לא נבחר מיכל");
			return;
		}
		OpenCardController.tankChosen = tankTableView.getSelectionModel().getSelectedItem();
		onBack();
	}
	
	public void onSearch(){
		ArrayList<Tank> tankSearch = new ArrayList<Tank>();
		for(Tank tank : GeneralMessage.getTankList())
			if(tank.getManufacturer().toLowerCase().contains(manuTextField.getText().toLowerCase()) && 
					tank.getModel().toLowerCase().contains(modelTextField.getText().toLowerCase()))
				tankSearch.add(tank);
		ObservableList<Tank> tankList = FXCollections.observableArrayList(tankSearch);
		tankTableView.setItems(tankList);
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


		if(tankTableView.getSelectionModel().getSelectedIndex() != selectedIndex){
			newClick = true;
			selectedIndex = tankTableView.getSelectionModel().getSelectedIndex();
			doubleClick = 1;
			thread.start();
			return;
		}
		onContinue();
	}
	
}
