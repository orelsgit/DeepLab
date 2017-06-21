package main;

import java.io.IOException;
import java.util.ArrayList;

import controllers.AddCustomerController;
import controllers.AddEquipment;
import controllers.CCROwnerSearchController;
import controllers.EmailController;
import controllers.LoginScreenController;
import controllers.LoginWorkerScreenController;
import controllers.ManagersPasswordController;
import controllers.OrderInfoController;
import controllers.RegisterController;
import entities.BCD;
import entities.CCR;
import entities.Customer;
import entities.GeneralMessage;
import entities.Order;
import entities.Read;
import entities.Regulator;
import entities.Tank;
import entities.Update;
import entities.Windows;
import entities.Worker;
import ocsf.client.AbstractClient;

public class Client extends AbstractClient {


	public Client() {
		super(Main.host, Main.port);
		try {
			this.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***************Return the window message*********************/

	@SuppressWarnings("unchecked")
	@Override
	protected void handleMessageFromServer(Object msg) {


		try{

			if(msg instanceof ArrayList<?>){
				switch(((ArrayList<GeneralMessage>)msg).get(0).actionNow){
				case "OrderListReady":
					GeneralMessage.setUnhandledOrders(new ArrayList<Order>());
					for(Order order : (ArrayList<Order>)msg)
						GeneralMessage.getUnhandledOrders().add(order);

					break;
					//				LabOrdersController.isBackFromServer=true;break;
				case "GotCustomers":
					GeneralMessage.setCustList((ArrayList<Customer>)msg);break;
				case "GotCCRs":
					GeneralMessage.setCcrList((ArrayList<CCR>)msg);break;
				case "GotRegs":
					GeneralMessage.setRegList((ArrayList<Regulator>)msg);break;
				case "GotTanks":
					GeneralMessage.setTankList((ArrayList<Tank>)msg);break;
				case "GotBCDs":
					GeneralMessage.setBcdList((ArrayList<BCD>)msg);break;
				case "GotCCRList"://for opencard owner search
					CCROwnerSearchController.ccrListSearch = new ArrayList<CCR>((ArrayList<CCR>)msg);
					CCROwnerSearchController.isBackFromServer = true;break;
				case "GotInfo":
					LoginWorkerScreenController.orders = new ArrayList<Order>(((ArrayList<Order>)msg));break;
				}

			}

			else
				switch(((GeneralMessage)msg).actionNow){
				case "Incorrect"://Incorrect login information
					Worker worker = new Worker();worker.actionNow="Incorrect;";
					Windows.warning("Incorrect information. Try again.");Worker.setCurrentWorker(worker);LoginScreenController.isBackFromServer = true;
					System.out.println("General: " + LoginScreenController.isBackFromServer);break;
				case "Correct"://correct login information
					Worker.setCurrentWorker((Worker)msg);break;
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
				case "NewCustomer":
					AddCustomerController.isBackFromServer = true;break;
				case "ModelExists":
					Windows.warning("הוסת קיים כבר במערכת");AddEquipment.isBackFromServer=true;break;
				case "NewRegulator":
					Windows.message("וסת חדש נוסף למסד הנתונים", "וסת חדש");AddEquipment.isBackFromServer=true;break;
				case "NewTank":
					Windows.message("מיכל חדש נוסף למסד הנתונים", "מיכל חדש");AddEquipment.isBackFromServer=true;break;
				case "NewBCD":
					Windows.message("מאזן חדש נוסף למסד הנתונים", "מאזן חדש");AddEquipment.isBackFromServer=true;break;
				case "NewCCR":
					Windows.message("מערכת סגורה חדשה נוספה למסד הנתונים", "מערכת סגורה חדשה");AddEquipment.isBackFromServer=true;break;
				case "NewClientOrder":
					Order.currentOrder.setCustID(((Order)msg).getCustID());Order.currentOrder.actionNow = "NewClientOrder";break;
				case "OldClientOrder":
					Order.currentOrder.actionNow = "OldClientOrder";break;
				case "RemoveOrder":
					int i = 0;
					for(Order order : Order.getUnhandledOrderList()){
						++i;
						if(order.getOrderNum() == ((Order)msg).getOrderNum())
							Order.getUnhandledOrderList().remove(i);
					}break;
				case "AlreadyInSystem":
					AddEquipment.isBackFromServer=true;break;
				case "NewUpdateCheck":
					int version;
					Read read = Read.getInstance();
					System.out.println(read.readVersion(((Update)msg).getVersion())+ " VS " +((Update)msg).getVersion() );
					if(read.readVersion(((Update)msg).getVersion())<((Update)msg).getVersion()){//getversion gets me the latest update
						LoginWorkerScreenController.newUpdates = true;LoginWorkerScreenController.backFromServer = true;
						version = ((Update)msg).getVersion()-1;
					}
					else{//Have the latest update already.
						LoginWorkerScreenController.newUpdates = false;LoginWorkerScreenController.backFromServer = true;
						version = ((Update)msg).getVersion();
					}
					LoginWorkerScreenController.currentVersion = version;
					break;
				}
		}catch(Exception e){e.printStackTrace();}
	}

}
