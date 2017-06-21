package controllers;

import java.util.ArrayList;

//import entities.SpeechUtils;
import entities.Error;
import entities.Files;
import entities.GeneralMessage;
import entities.GeneralMethods;
import entities.Order;
import entities.Status;
import entities.Update;
import entities.Windows;
import entities.Worker;
import entities.Write;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import main.Main;

public class LoginWorkerScreenController{

	@FXML
	private ImageView redotImageView;
	@FXML
	private Button orderButton, ticketsButton, updateButton, newUpdateButton;
	@FXML
	private AnchorPane loginAnchorPane;
	@FXML
	private TextArea updateDescriptionTextArea;
	@FXML
	private Pane pane;

	private GeneralMethods GM;
	public static boolean newOrders,backFromServer, newUpdates;
	private static boolean currentWindow;//if currentWindow = false it means we are in another window, therefore the thread in init. wont run.
	public static String equipment = "";

	public static ArrayList<Order> orders = null;
	public static int currentVersion;



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


		newUpdateButton.setVisible(false);newUpdateButton.setDisable(true);updateDescriptionTextArea.setVisible(false);
		switch (Worker.getCurrentWorker().getIsManager()){
		case Manager:
			break;
		case Tech:
			orderButton.setStyle("-fx-background-color: #7F7F7F;");break;
		case Dalpak:
			ticketsButton.setStyle("-fx-background-color: #7F7F7F;");break;
		case Programmer:
			newUpdateButton.setVisible(true);newUpdateButton.setDisable(false);updateDescriptionTextArea.setVisible(true);break;
		default:
			break;
		}
		GM = new GeneralMethods();

		checkUpdates();

		currentWindow = true;
		newOrders=false;
		redotImageView.setVisible(false);

		@SuppressWarnings("unused")
		Thread thread = new Thread(){
			public void run(){
				//boolean flag=false;
				while(currentWindow&&Worker.getCurrentWorker().getIsManager()!=Status.Dalpak){
					backFromServer=false;
					GM.sendServer(new Order(), "CheckNewOrders");

					Error error = new Error("LoginWorkerScreenController", "initialize", 0);
					int timesCalled = 0;
					while(!backFromServer)
						if(!GM.Sleep(70, error, timesCalled++))
							return;


					GM.refresh(null);
					while(newOrders){
						redotImageView.setVisible(true);
						GM.Sleep(400, null, 0);
						redotImageView.setVisible(false);
						GM.Sleep(400, null, 0);
						//if(!flag&&Worker.getCurrentWorker().getIsManager()!=Status.Manager){
						//SpeechUtils work = new SpeechUtils("You have work to do!");
						//work.speak();
						//flag=true;
						//}
					}
					redotImageView.setVisible(false);
					GM.Sleep(10000, null, 0);
				}this.interrupt();
			}
		};//thread.start();
	}


	public void checkUpdates(){
		backFromServer=false;
		newUpdates=false;

		GM.sendServer(new GeneralMessage(), "CheckUpdates");
		Error error = new Error("LoginWorkerScreenController", "initialize", -1);
		int timesCalled = 1;
		while(!backFromServer)
			if(!GM.Sleep(70, error, timesCalled++))
				return;

		if(newUpdates){
			updateButton.setVisible(true);
			updateButton.setDisable(false);
		}
		else{
			updateButton.setVisible(false);
			updateButton.setDisable(true);
		}
		backFromServer=false;
		newUpdates=false;
	}

	public void downloadUpdate(){
		String dest = Files.getDestination();
		if(dest.equals(""))
			return;
		Update update = new Update();
		update.setDestination(dest);
		GM.sendServer(update, "DownloadUpdate");
		backFromServer = false;
		Error error = new Error("LoginWorkerScreenController", "downloadUpdate", 3);
		int timesCalled = 0;
		while(!backFromServer){
			Windows.optionWindow("המעבדה בהורדה, אנא המתן...", "מעבדה בהורדה");
			if(!GM.Sleep(300, error, timesCalled++))
				return;
		}
		backFromServer = false;

		
		int version = ++currentVersion;
		Write.getInstance().writeVersion(version);
	}
	public void uploadUpdate(){
		Update update = new Update(updateDescriptionTextArea.getText(), -1);
		update.getFile().setFile();
		GM.sendServer(update, "AddNewUpdate");
	}

	public void onRefresh(){
		GM.refresh(null);
	}


	public void getInfo(){
		GM.sendServer(new Order(), "GetInfo");

		Error error = new Error("LoginWorkerScreenController", "getInfo", 1);
		int timesCalled = 0;
		while(orders == null)
			if(!GM.Sleep(70, error, timesCalled++))
				return;

		for(Order order : orders)
			Windows.message(order.getSummary(), "summary");
	}

	public static void setCurrentWindow(boolean currentWindow){
		LoginWorkerScreenController.currentWindow = currentWindow;
	}

	public void addCCR(){
		equipment = "CCR";
		GM.getPopup(Main.popup, "AddCCR", "הוספת מערכת סגורה", "popup");
	}


	public void onAddRegulator(){
		equipment = "Regulator";
		GM.getPopup(Main.popup, "AddRegulator", "הוספת וסת", "popup");
	}

	public void onAddBCD(){
		equipment = "BCD";
		GM.getPopup(Main.popup, "AddBCD", "הוספת מאזן", "popup");

	}

	public void onAddTank(){
		equipment = "Tank";
		GM.getPopup(Main.popup, "AddTank", "הוספת מיכל", "popup");

	}

	/**
	 * Opens a customer registeration window.
	 * @author orelzman
	 */
	public void onAddCustomer(){
		GM.getPopup(Main.popup, "AddCustomer", "הוספת לקוח", "popup");
		currentWindow=false;
	}


	/**
	 * Sets up a window that will contain a TableView with all the unreviewed Orders, as foresaid, the LabOrders
	 * @author orelzman
	 */
	public void onTickets(){

		Error error = new Error("LoginWorkerScreenController", "onAddCustomer", 2);
		int timesCalled = 0;
		while(!GeneralMessage.getGotLists())
			if(!GM.Sleep(70, error, timesCalled++))
				return;



		if(Worker.getCurrentWorker().getIsManager()==Status.Dalpak)
			return;
		if(!newOrders)//Will open only if there are new orders
			return;
		currentWindow=false;
		Main.showMenu("LabOrders", "מסך הזמנות");
	}

	public void onLogout(){
		currentWindow = false;
		Main.showMenu("MainScreen", "מסך ראשי");
	}

	/**
	 * This method shows the equipment check screen.
	 * @author orelzman
	 */
	public void onCheckEquipment(){
		currentWindow = false;
		Main.showMenu("CheckEquipmentScreen", "בדיקת ציוד");
	}

	/**
	 * This method shows the new card for the dalpak screen.
	 * @author orelzman
	 */
	public void onIssueOrder(){
		if(Worker.getCurrentWorker().getIsManager()==Status.Tech)
			return;
		currentWindow = false;
		Main.showMenu("OpenCardScreen", "כרטיס חדש");
	}
}
