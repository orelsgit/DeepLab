package controllers;

import java.util.ArrayList;

import entities.GeneralMessage;
import entities.GeneralMethods;
import entities.Regulator;
import entities.Windows;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;

public class SearchRegController {

	@FXML
	private TableView<Regulator> regTableView;
	@FXML
	private TextField manuTextField, modelTextField;

	private GeneralMethods GM = new GeneralMethods();

	private boolean newClick = true;
	private int doubleClick = 0, selectedIndex = -1;


	public void initialize(){
		initTable();
		ObservableList<Regulator> regList = FXCollections.observableArrayList(GeneralMessage.getRegList());
		regTableView.setItems(regList);
	}


	@SuppressWarnings("unchecked")
	public void initTable(){
		TableColumn<Regulator, String> manufacturer = (TableColumn<Regulator, String>) regTableView.getColumns().get(0);
		manufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
		TableColumn<Regulator, String> model = (TableColumn<Regulator, String>) regTableView.getColumns().get(1);
		model.setCellValueFactory(new PropertyValueFactory<>("model"));
	}

	public void onSearch(){
		ArrayList<Regulator> regSearch = new ArrayList<Regulator>();
		for(Regulator reg : GeneralMessage.getRegList())
			if(reg.getManufacturer().toLowerCase().contains(manuTextField.getText().toLowerCase()) && 
					reg.getModel().toLowerCase().contains(modelTextField.getText().toLowerCase()))
				regSearch.add(reg);
		ObservableList<Regulator> regList = FXCollections.observableArrayList(regSearch);
		regTableView.setItems(regList);

	}

	public void onContinue(){
		if((OpenCardController.regChosen = regTableView.getSelectionModel().getSelectedItem()) == null ){
			Windows.threadWarning("לא נבחר וסת");
			return;
		}
		onBack();
	}

	public void onBack(){
		GM.closePopup(Main.popup);
	}



	public void onDoubleClick(){
		Thread thread = new Thread(){
			public void run(){
				GM.Sleep(200);
				if(newClick)
					return;
				doubleClick = 0;
			}
		};


		if(regTableView.getSelectionModel().getSelectedIndex() != selectedIndex){
			newClick = true;
			selectedIndex = regTableView.getSelectionModel().getSelectedIndex();
			doubleClick = 1;
			thread.start();
			return;
		}
		onContinue();
	}

}