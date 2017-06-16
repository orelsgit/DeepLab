package controllers;

import java.util.ArrayList;

import entities.CCR;
import entities.Error;
import entities.GeneralMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CCROwnerSearchController {
	
	public static boolean isBackFromServer = false;
	private GeneralMethods GM = new GeneralMethods();
	public static ArrayList<CCR> ccrListSearch;

	
	@FXML
	TableView<CCR> ccrOwnerTableView;
	
	public void initialize(){
		initTableView();
		GM.sendServerThread(new CCR(), "GetCCROwnersList");
		
		Error error = new Error("CCROwnerSearch", "initialize", 0);
		int timesCalled = 0;
		while(!isBackFromServer)
			if(!GM.Sleep(70, error, timesCalled++))
				return;
			
		
		ObservableList<CCR> ccrList = FXCollections.observableArrayList(ccrListSearch);
		ccrOwnerTableView.setItems(ccrList);

		 
		
	}
	@SuppressWarnings("unchecked")
	public void initTableView(){
		ccrOwnerTableView.setPlaceholder(new Label("לא נמצאו לקוחות"));//If table is empty it sets the label.

		TableColumn<CCR, String> owner = (TableColumn<CCR, String>) ccrOwnerTableView.getColumns().get(0);
		owner.setCellValueFactory(new PropertyValueFactory<>("owner"));
		TableColumn<CCR, String> manufacturer = (TableColumn<CCR, String>) ccrOwnerTableView.getColumns().get(1);
		manufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
		TableColumn<CCR, String> model = (TableColumn<CCR, String>) ccrOwnerTableView.getColumns().get(2);
		model.setCellValueFactory(new PropertyValueFactory<>("model"));
	}
	
}
