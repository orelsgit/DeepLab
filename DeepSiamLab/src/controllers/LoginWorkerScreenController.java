package controllers;

import entities.GeneralMessage;
import entities.GeneralMethods;
import entities.Order;
//import entities.SpeechUtils;
import entities.Status;
import entities.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import main.Main;

public class LoginWorkerScreenController{
	
	@FXML
	private ImageView redotImageView;
	@FXML
	private Button orderButton, ticketsButton;
	
	
	private GeneralMethods GM;
	public static boolean newOrders,backFromServer;
	private static boolean currentWindow;//if currentWindow = false it means we are in another window, therefore the thread in init. wont run.
	
	
	/**
	 * Grays out the buttons that aren't allowed for the current logged in worker and sets a thread to check if there are
	 * orders that require the tech's attention.
	 * @author orelzman
	 */
	public void initialize(){//	private int isManager;//1 - manager, 0 - tech, -1 - dalpak
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
						GM.Sleep(600);
						redotImageView.setVisible(false);
						GM.Sleep(600);
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
	
	
	/**
	 * Opens a customer registeration window.
	 * @author orelzman
	 */
	public void onAddCustomer(){
		Main.showMenu("AddCustomer");
		GeneralMessage.currentWindow = "LoginWorkerScreen";
		currentWindow=false;
	}
	
	/**
	 * Sets up a window that will contain a TableView with all the unreviewed Orders, as foresaid, the LabOrders
	 * @author orelzman
	 */
	public void onTickets(){
		while(!GeneralMessage.getGotLists()){
			GM.Sleep(1);
		}
		if(Worker.getCurrentWorker().getIsManager()==Status.Dalpak)
			return;
		if(!redotImageView.isVisible())//Will open only if there are new orders
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
