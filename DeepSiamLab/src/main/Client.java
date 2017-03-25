package main;

import java.io.IOException;
import java.util.ArrayList;

import controllers.CustomerSearchController;
import controllers.EmailController;
import controllers.LabOrdersController;
import controllers.LoginWorkerScreenController;
import controllers.ManagersPasswordController;
import controllers.OrderInfoController;
import controllers.RegisterController;
import entities.Customer;
import entities.GeneralMessage;
import entities.Order;
import entities.Regulator;
import entities.Windows;
import entities.Worker;
import ocsf.client.AbstractClient;

public class Client extends AbstractClient {

	public Client() {
		super(Main.host, Main.port);
		try {
			this.openConnection();
		} catch (IOException e) {
			System.out.println("caught");
			e.printStackTrace();
		}
	}

	/***************Return the window message*********************/

	@SuppressWarnings("unchecked")
	@Override
	protected void handleMessageFromServer(Object msg) {

		if(msg instanceof ArrayList<?>){
			switch(((ArrayList<GeneralMessage>)msg).get(0).actionNow){
			case "OrderListReady":
				for(Order order : (ArrayList<Order>)msg)
					LabOrdersController.orderList.add(order);
				LabOrdersController.isBackFromServer=true;break;
			case "GotCustomers":
				CustomerSearchController.customerList = new ArrayList<Customer>((ArrayList<Customer>)msg);
				CustomerSearchController.isBackFromServer = true;
			}
		}

		else
			switch(((GeneralMessage)msg).actionNow){
			case "Incorrect"://Incorrect login information
				Windows.warning("Incorrect information. Try again.");Worker.setCurrentWorker(new Worker());break;
			case "Correct"://correct login information
				Worker.setCurrentWorker((Worker)msg);Windows.threadMessage("Welcome back " + Worker.getCurrentWorker().getfName(), "Deepsiam Lab");break;
			case "IssueOrder"://An order was issued by the dalpak
				Order.currentOrder.actionNow+=",";break;
			case "ManagerPasswordCorrect":
				ManagersPasswordController.worker.actionNow="Correct";break;
			case "ManagerPasswordIncorrect":
				ManagersPasswordController.worker.actionNow="Incorrect";break;
			case "IDExists"://Adding new worker
				Windows.message(".שם המשתמש שנבחר קיים כבר במערכת", "שם משתמש לא תקין");RegisterController.worker.actionNow = "IDExists";break;
			case "EmailExists"://Adding new worker
				Windows.message(".האימייל שנבחר קיים כבר במערכת", "אימייל לא תקין");RegisterController.worker.actionNow = "IDExists";break;
			case "WorkerAdded":
				Windows.message("!המשתמש נוסף בהצלחה", "משתמש חדש");RegisterController.worker.actionNow=".";break;
			case "NewOrders"://Reddot
				LoginWorkerScreenController.newOrders=true;
				LoginWorkerScreenController.backFromServer=true;break;
			case "NoNewOrders"://NoReddot
				LoginWorkerScreenController.newOrders=false;
				LoginWorkerScreenController.backFromServer=true;break;
			case "YesMail":
				EmailController.emailTo=((Customer)msg).getEmail();break;
			case "NoMail":
				EmailController.emailTo=null;break;
			case "YesPhone":
				OrderInfoController.getCustomerSelected().setPhone(((Customer)msg).getPhone());OrderInfoController.isBackFromServer=true;break;
			case "NoPhone":
				OrderInfoController.getCustomerSelected().setPhone(null);OrderInfoController.isBackFromServer=true;break;
			case "InterFound":
				OrderInfoController.regChosen.actionNow = "InterFound";OrderInfoController.regChosen.setInterPressure(((Regulator)msg).getInterPressure());
				OrderInfoController.isGotEquipments = true;break;
			case "InterNotFound":
				OrderInfoController.regChosen.actionNow="InterNotFound";OrderInfoController.isGotEquipments = true;break;
			}
	}

}
