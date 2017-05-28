package controllers;

import entities.GeneralMessage;
import entities.GeneralMethods;
import entities.Order;
//import entities.SpeechUtils;
import entities.Status;
import entities.Windows;
import entities.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import main.Main;

public class LoginWorkerScreenController{
	
	@FXML
	private ImageView redotImageView;
	@FXML
	private Button orderButton, ticketsButton;
	@FXML
	private AnchorPane loginAnchorPane;
	@FXML
	private Pane pane;
	
	private GeneralMethods GM;
	public static boolean newOrders,backFromServer;
	private static boolean currentWindow;//if currentWindow = false it means we are in another window, therefore the thread in init. wont run.
	public static String equipment = "";
	
	
	/**
	 * Grays out the buttons that aren't allowed for the current logged in worker and sets a thread to check if there are
	 * orders that require the tech's attention.
	 * @author orelzman
	 */
	public void initialize(){//	private int isManager;//1 - manager, 0 - tech, -1 - dalpak
		//Windows.message("test: iNITIalize loginworkerscreen \n" + GeneralMessage.getUnhandledOrders().get(1).getSummary(), "??");
		//orderButton.prefWidthProperty().bind(loginAnchorPane.widthProperty());
		////orderButton.prefHeightProperty().bind(loginAnchorPane.heightProperty());
		//orderButton.setMaxHeight(100);
		//orderButton.setMaxWidth(100);
	//	AnchorPane.setTopAnchor(pane, 100.0);
	//	AnchorPane.setRightAnchor(pane, 10.0);
	//	AnchorPane.setLeftAnchor(pane, 10.0);
		//AnchorPane.setBottomAnchor(pane, 10.0);
	//	loginAnchorPane.getChildren().setAll(pane);

		
		
		switch (Worker.getCurrentWorker().getIsManager()){
		case Manager:
			break;
		case Tech:
			orderButton.setStyle("-fx-background-color: #7F7F7F;");break;
		case Dalpak:
			ticketsButton.setStyle("-fx-background-color: #7F7F7F;");break;
		}
		currentWindow = true;
		newOrders=false;
		GM = new GeneralMethods();
		redotImageView.setVisible(false);
		
		Thread thread = new Thread(){
			public void run(){
				//boolean flag=false;
				while(currentWindow&&Worker.getCurrentWorker().getIsManager()!=Status.Dalpak){
					backFromServer=false;
					GM.sendServer(new Order(), "CheckNewOrders");
					while(!backFromServer)
						GM.Sleep(2);
					while(newOrders){
						redotImageView.setVisible(true);
						GM.Sleep(400);
						redotImageView.setVisible(false);
						GM.Sleep(400);
						//if(!flag&&Worker.getCurrentWorker().getIsManager()!=Status.Manager){
						//SpeechUtils work = new SpeechUtils("You have work to do!");
						//work.speak();
						//flag=true;
						//}
					}
						redotImageView.setVisible(false);
					GM.Sleep(10000);
				}this.interrupt();
			}
		};thread.start();
	}
	
	
	public static void setCurrentWindow(boolean currentWindow){
		LoginWorkerScreenController.currentWindow = currentWindow;
	}
	
	public void onAddRegulator(){
		equipment = "Regulator";
		GM.getPopup(Main.popup, "AddRegulator", "AddRegulator", "popup");
	}
	
	public void onAddBCD(){
		equipment = "BCD";
		GM.getPopup(Main.popup, "AddBCD", "AddBCD", "popup");

	}
	
	public void onAddTank(){
		equipment = "Tank";
		GM.getPopup(Main.popup, "AddTank", "AddTank", "popup");

	}
	
	/**
	 * Opens a customer registeration window.
	 * @author orelzman
	 */
	public void onAddCustomer(){
		GM.getPopup(Main.popup, "AddCustomer", "AddCustomer", "popup");
		currentWindow=false;
	}
	
	
	/**
	 * Sets up a window that will contain a TableView with all the unreviewed Orders, as foresaid, the LabOrders
	 * @author orelzman
	 */
	public void onTickets(){
		while(!GeneralMessage.getGotLists()){
			System.out.println(".asdasdasd.");
			GM.Sleep(1);
		}

		if(Worker.getCurrentWorker().getIsManager()==Status.Dalpak)
			return;
		if(!newOrders)//Will open only if there are new orders
			return;
		currentWindow=false;
		Main.showMenu("LabOrders");
	}
	
	public void onLogout(){
		currentWindow = false;
		 Main.showMenu("MainScreen");
	 }
	
	/**
	 * This method shows the equipment check screen.
	 * @author orelzman
	 */
	public void onCheckEquipment(){
		currentWindow = false;
			Main.showMenu("CheckEquipmentScreen");
	}

	/**
	 * This method shows the new card for the dalpak screen.
	 * @author orelzman
	 */
	public void onIssueOrder(){
		if(Worker.getCurrentWorker().getIsManager()==Status.Tech)
			return;
		currentWindow = false;
		Main.showMenu("OpenCardScreen");
	}
}
