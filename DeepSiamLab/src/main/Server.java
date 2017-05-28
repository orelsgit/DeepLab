package main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entities.AnnualCheck;
import entities.BCD;
import entities.CCR;
import entities.Customer;
import entities.GeneralMessage;
import entities.GeneralMethods;
import entities.Order;
import entities.Regulator;
import entities.Tank;
import entities.Windows;
import entities.Worker;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class Server extends AbstractServer {    
	Connection conn;
	GeneralMethods GM;


	public Server(int port) {
		super(port);
		GM = new GeneralMethods();
		this.connectToDB();
		try {
			this.listen();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		
		switch(((GeneralMessage)msg).actionNow){
		case "Login":
			checkUserInfo((Worker)msg, client);break;
		case "IssueOrder":
			issueOrder((Order)msg, client);break;
			//issueAnOrder((Order)msg, client);break;
		case "ManagerPassword":
			managerPassword((Worker)msg, client);break;
		case "AddWorker":
			addWorker((Worker)msg, client);break;
		case "CheckNewOrders":
			checkNewOrders(client);break;
		case "GetNewOrders":
			getNewOrders(client);break;
		case "GetMail":
			getMail((Order)msg, client);break;
		case "GetPhone":
			getPhone((Customer)msg, client);break;
		case "GetCustomers":
			getCustomers((Customer)msg, client);break;
		case "FindInterPressure":
			findInterPressure((Regulator)msg, client);break;
		case "GetReg":
			getReg(client);break;
		case "GetBCD":
			getBCD(client);break;
		case "GetTank":
			getTank(client);break;
		case "GetCCR":
			getCCR(client);break;
		case "AnnualCheck":
			annualCheck((AnnualCheck)msg, client);break;
		case "NewCustomer":
			newCustomer((Customer)msg, client);break;
		case "AddRegulator":
			addRegulator((Regulator)msg, client);break;
		case "AddBCD":
			addBCD((BCD)msg, client);break;
		case "AddTank":
			addTank((Tank)msg, client);break;
		case "GetCCROwnersList":
			getCCROwnersList(client);break;
		case "Download":
			download((BCD)msg);break;
		case "OrderHandled":
			orderHandled((Order)msg, client);
			

		}
	}

	
	public void orderHandled(Order order, ConnectionToClient client){
		try{
			String query = "UPDATE orelDeepdivers.Orders SET Cost = ?, Summary = ?, DateHandled = ?, Handled = ? WHERE OrderNum = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, order.getCost());
			stmt.setString(2, order.getSummary());
			stmt.setString(3, GM.getCurrentDate());
			stmt.setInt(4, 1);
			stmt.setInt(5, order.getOrderNum());
			System.out.println(query);
			stmt.executeUpdate();
			System.out.println(query);
			order.actionNow = "RemoveOrder";
			getNewOrders(client);
			client.sendToClient(order);
		}catch(Exception e){e.printStackTrace();}
	}
	

	public void download(BCD bcd){
		byte[] buffer;
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(bcd.query);
			if(rs.next()){
				buffer = rs.getBytes(1);
				OutputStream targetFile = new FileOutputStream("C://Users//orels//Desktop//TESTYAY.docx");
				targetFile.write(buffer);
				targetFile.close();

			}
		}catch(Exception e){e.printStackTrace();}
	}


	public void getCCROwnersList(ConnectionToClient client){
		try{
			ArrayList<CCR> ccrList = new ArrayList<CCR>();
			Statement stmt = conn.createStatement();
			ResultSet rs1, rs2;
			ResultSet rs = stmt.executeQuery("SELECT SerialNum, CustID FROM orelDeepdivers.CustomersCCR");
			while(rs.next()){
				rs1 = stmt.executeQuery("SELECT Name, LastName FROM orelDeepdivers.Customers WHERE CustID = '" + rs.getString(2) + "';");
				rs2 = stmt.executeQuery("SELECT Manufacturer, Model FROM orelDeepdivers.CCR WHERE SerialNum = '" + rs.getString(1) +"';");
				ccrList.add(new CCR(rs2.getString(1), rs1.getString(1) + " " + rs1.getString(2), rs2.getString(2)));
			}
			//if()
			ccrList.get(0).actionNow = "GotCCRList";
			client.sendToClient(ccrList);
		}catch(Exception e){e.printStackTrace();}
	}





	public void addTank(Tank tank, ConnectionToClient client){
		try{
			PreparedStatement pstmt = conn.prepareStatement("insert into orelDeepdivers.Tanks values(?,?,?,?);");
			pstmt.setString(1, tank.getModel());
			pstmt.setString(2, tank.getManufacturer());
			pstmt.setInt(3, tank.getVolume());
			pstmt.executeUpdate();

			tank.actionNow = "NewTank";
			client.sendToClient(tank);
		}catch(Exception e){e.printStackTrace();}
	}

	public void addBCD(BCD bcd, ConnectionToClient client){
		try{
			PreparedStatement pstmt = conn.prepareStatement("insert into orelDeepdivers.BCDS values (?,?,?,?,?,?);");
			pstmt.setString(1, bcd.getSize());
			pstmt.setString(2, bcd.getModel());
			pstmt.setString(3, bcd.getManufacturer());
			pstmt.setString(4, bcd.getDeepNum());
			if(bcd.getFiles().getFile()!=null){
				FileInputStream fis = new FileInputStream(bcd.getFiles().getFile());
				pstmt.setBinaryStream(5, fis, bcd.getFiles().getLen());
				pstmt.setString(6, bcd.getFiles().getFileName());
			}
			else{
				pstmt.setBinaryStream(5, null);
				pstmt.setString(6, null);
			}
			pstmt.executeUpdate();

			bcd.actionNow = "NewBCD";
			client.sendToClient(bcd);

		}catch(Exception e){e.printStackTrace();}
	}

	public void addRegulator(Regulator reg, ConnectionToClient client){
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select LTRIM(RTRIM(Model)) from orelDeepdivers.Regulators");
			while(rs.next())
				if(reg.getModel().equals(rs.getString(1))){
					reg.actionNow = "ModelExists";
					client.sendToClient(reg);
					return;
				}
			PreparedStatement pstmt = conn.prepareStatement("insert into orelDeepdivers.Regulators values (?,?,?);");
			pstmt.setString(1, reg.getModel());
			pstmt.setString(2, reg.getManufacturer());
			pstmt.setFloat(3, reg.getInterPressure());
			pstmt.executeUpdate();

			reg.actionNow = "NewRegulator";
			client.sendToClient(reg);

		}catch(Exception e){e.printStackTrace();}
	}

	/**
	 * Inserts a new customer to the database.
	 * @param customer The new customer's information.
	 * @param client is an object that will send a message back to the client
	 * @author orelzman
	 */
	public void newCustomer(Customer customer, ConnectionToClient client){
		int max=0;
		try{
			PreparedStatement pStmt = conn.prepareStatement("insert into orelDeepdivers.Customers(Name, LastName, CustID, Email, Phone, ID) values"
					+ "(?,?,?,?,?,?);");
			if(customer.getCustID() != null)
				max = Integer.parseInt(customer.getCustID());
			else{
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT LTRIM(RTRIM(CustID)) FROM orelDeepdivers.Customers");
				while(rs.next())
					if(Integer.parseInt(rs.getString(1))>max)
						max=Integer.parseInt(rs.getString(1));
				max++;
			}
			pStmt.setString(1, customer.getName());
			pStmt.setString(2, customer.getLastName());
			pStmt.setString(3, Integer.toString(max));
			pStmt.setString(4, customer.getEmail());
			pStmt.setString(5, customer.getPhone());
			pStmt.setString(6, customer.getId());
			pStmt.executeUpdate();
			client.sendToClient(new GeneralMessage("NewCustomer"));	

		}catch(Exception e){e.printStackTrace();}
	}

	/**
	 * Inserts the information about the annual check to the database, for future affairs.
	 * @param ac Contains the information about the check for either a bcd/ccr/reg.
	 * @param client is an object that will send a message back to the client
	 * @author orelzman 
	 */
	public void annualCheck(AnnualCheck ac, ConnectionToClient client ){
		PreparedStatement pStmt;
		try{
			int orderNum;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(OrderNum) FROM orelDeepdivers.Orders");
			if(rs.next())
				orderNum=rs.getInt(1);
			else
				orderNum=0;
			orderNum++;

			if(ac.isReg()){
				pStmt = conn.prepareStatement("insert into orelDeepdivers.CustomersReg(OrderNum, CustID, "
						+ "Description, Date, Comments, Handled, Cost, IsClubEquipment, SerialNumber, KitChangeDate) values(?,?,?,?,?,?,?,?,?,?);");
				pStmt.setInt(1, orderNum);
				pStmt.setString(2, ac.getCustID());
				if(ac.isKit())
					pStmt.setString(10, ac.getKitChangeDate());

				if(!ac.getFixComments().equals(""))
					pStmt.setString(4, ac.getFixComments());
				else
					pStmt.setString(4, "");
				if(!ac.getAnnualComments().equals(""))
					pStmt.setString(5, ac.getAnnualComments());
				else
					pStmt.setString(5, "");

				pStmt.setString(6, GM.getCurrentDate());
				if(ac.isManagerApprove())
					pStmt.setInt(7, 1);
				else
					pStmt.setInt(7, 0);
				pStmt.executeUpdate();

				/** ONLY AFTER ALL THE EQUIPMENTS WERE REVIEWED! **/
				/*	Statement statement = conn.createStatement();
				String query;
				if(ac.isManagerApprove())//handled = 1
					query = "UPDATE orelDeepdivers.Orders SET handled = 1 WHERE OrderNum = " + ac.getOrderNum() + ";";
				else
					query = "UPDATE orelDeepdivers.Orders SET handled = 0 WHERE OrderNum = " + ac.getOrderNum() + ";";
				statement.executeUpdate(query);*/

			}

			if(ac.isCcr()){

			}

			if(ac.isBcd()){

			}

		}catch(Exception e){e.printStackTrace();}
	}


	/**
	 * Creates a list of all the regulators in the database and sends it back to the server.
	 * @param client is an object that will send a message back to the client
	 * @author orelzman 
	 */
	public void getReg(ConnectionToClient client){
		ArrayList<Regulator> regList = new ArrayList<Regulator>();
		try{

			Regulator reg;
			Statement stmt = conn.createStatement();
			ResultSet rs1 = stmt.executeQuery("Select LTRIM(RTRIM(Model)) From orelDeepdivers.Regulators");
			while(rs1.next())
				regList.add(new Regulator(rs1.getString(1)));

			int i=0;
			ResultSet rs2 = stmt.executeQuery("Select LTRIM(RTRIM(Manufacturer)) From orelDeepdivers.Regulators");
			while(rs2.next() && i<regList.size()){
				regList.get(i).setManufacturer(rs2.getString(1));
				i++;
			}
			i=0;
			ResultSet rs3 = stmt.executeQuery("Select LTRIM(RTRIM(DeepNum)) From orelDeepdivers.Regulators");
			while(rs3.next() && i<regList.size()){
				regList.get(i).setDeepNum(rs3.getString(1));
				i++;
			}

			i=0;
			rs3 = stmt.executeQuery("Select LTRIM(RTRIM(SerialNum)) From orelDeepdivers.Regulators");
			while(rs3.next() && i<regList.size()){
				regList.get(i).setSerialNum(rs3.getString(1));
				i++;
			}

			if(regList.isEmpty())
				regList.add(new Regulator());
			regList.get(0).actionNow = "GotRegs";
			client.sendToClient(regList);
		}catch(Exception e){e.printStackTrace();}
	}

	/**
	 * Creates a list of all the bcds in the database and sends it back to the server.
	 * @param client is an object that will send a message back to the client
	 * @author orelzman 
	 */
	public void getBCD(ConnectionToClient client){
		ArrayList<BCD> bcdList = new ArrayList<BCD>();
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs1 = stmt.executeQuery("Select LTRIM(RTRIM(Size)) From orelDeepdivers.BCDS");
			while(rs1.next())
				bcdList.add(new BCD(rs1.getString(1)));

			int i=0;

			ResultSet rs2 = stmt.executeQuery("Select LTRIM(RTRIM(Model)) From orelDeepdivers.BCDS");
			while(rs2.next() && i<bcdList.size()){
				bcdList.get(i).setModel(rs2.getString(1));
				i++;
			}
			i=0;
			ResultSet rs3 =  stmt.executeQuery("Select LTRIM(RTRIM(Manufacturer)) From orelDeepdivers.BCDS");
			while(rs3.next() && i<bcdList.size()){
				bcdList.get(i).setManufacturer(rs3.getString(1));
				i++;
			}
			i=0;
			ResultSet rs4 =  stmt.executeQuery("Select LTRIM(RTRIM(DeepNum)) From orelDeepdivers.BCDS");
			while(rs4.next() && i<bcdList.size()){
				bcdList.get(i).setDeepNum(rs4.getString(1));
				i++;
			}


			if(bcdList.isEmpty())
				bcdList.add(new BCD());
			bcdList.get(0).actionNow = "GotBCDs";
			client.sendToClient(bcdList);
		}catch(Exception e){e.printStackTrace();}
	}

	/**
	 * Creates a list of all the tanks in the database and sends it back to the server.
	 * @param client is an object that will send a message back to the client
	 * @author orelzman 
	 */
	public void getTank(ConnectionToClient client){
		ArrayList<Tank> tankList = new ArrayList<Tank>();
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs1 = stmt.executeQuery("Select LTRIM(RTRIM(Model)) From orelDeepdivers.Tanks");
			while(rs1.next())
				tankList.add(new Tank(rs1.getString(1)));

			int i = 0;
			ResultSet rs2 = stmt.executeQuery("Select LTRIM(RTRIM(Manufacturer)) From orelDeepdivers.Tanks");
			while(rs2.next() && i<tankList.size()){
				tankList.get(i).setManufacturer(rs2.getString(1));
				i++;
			}
			i=0;
			ResultSet rs3 = stmt.executeQuery("Select LTRIM(RTRIM(Volume)) From orelDeepdivers.Tanks");
			while(rs3.next() && i<tankList.size()){
				tankList.get(i).setVolume(rs3.getInt(1));
				i++;
			}

			i=0;
			rs3 = stmt.executeQuery("Select LTRIM(RTRIM(DeepNum)) From orelDeepdivers.Tanks");
			while(rs3.next() && i<tankList.size()){
				tankList.get(i).setDeepNum(rs3.getString(1));
				i++;
			}

			i=0;
			rs3 = stmt.executeQuery("Select LTRIM(RTRIM(SerialNum)) From orelDeepdivers.Tanks");
			while(rs3.next() && i<tankList.size()){
				tankList.get(i).setSerialNum(rs3.getString(1));
				i++;
			}

			if(tankList.isEmpty())
				tankList.add(new Tank());
			tankList.get(0).actionNow = "GotTanks";
			client.sendToClient(tankList);
		}catch(Exception e){e.printStackTrace();}
	}

	/**
	 * Creates a list of all the ccrs in the database and sends it back to the server.
	 * @param client is an object that will send a message back to the client
	 * @author orelzman 
	 */
	public void getCCR(ConnectionToClient client){
		ArrayList<CCR> ccrList = new ArrayList<CCR>();
		try{
			CCR ccr;
			Statement stmt = conn.createStatement();
			ResultSet rs1 = stmt.executeQuery("Select LTRIM(RTRIM(Manufacturer)) From orelDeepdivers.CCR");
			while(rs1.next())
				ccrList.add(new CCR(rs1.getString(1)));

			ResultSet rs2 = stmt.executeQuery("Select LTRIM(RTRIM(Model)) From orelDeepdivers.CCR");
			int i=0;
			while(rs2.next() && i<ccrList.size()){
				ccrList.get(i).setModel(rs2.getString(1));
				i++;
			}
			i=0;
			rs2 = stmt.executeQuery("Select LTRIM(RTRIM(SerialNum)) From orelDeepdivers.CCR");
			while(rs2.next() && i<ccrList.size()){
				ccrList.get(i).setSerialNum(rs2.getString(1));
				i++;
			}
			if(ccrList.isEmpty())
				ccrList.add(new CCR());
			ccrList.get(0).actionNow = "GotCCRs";
			client.sendToClient(ccrList);
		}catch(Exception e){e.printStackTrace();}
	}

	/**
	 * Finds the intermediate pressure of a regulator, for the annual check.
	 * @param reg the regulator for which the tech is looking the intermediate pressure
	 * @param client is an object that will send a message back to the client
	 * @author orelzman 
	 */
	public void findInterPressure(Regulator reg, ConnectionToClient client){
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT LTRIM(RTRIM(InterPressure)) FROM orelDeepdivers.Regulators WHERE Model = '" + reg.getModel() + "';");
			if(rs.next()){
				reg.setInterPressure(rs.getFloat(1));
				reg.actionNow="InterFound";
			}
			else
				reg.actionNow="InterNotFound";
			client.sendToClient(reg);
		}catch(Exception e){e.printStackTrace();}

	}

	/**
	 * Creates a list of all the customers in the database and sends it back to the server.
	 * @param client is an object that will send a message back to the client
	 * @author orelzman 
	 */
	public void getCustomers(Customer customer, ConnectionToClient client){
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("Select * FROM orelDeepdivers.Customers");
			ArrayList<Customer> custList = new ArrayList<Customer>();
			while(rs.next())
				custList.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));

			if(custList.isEmpty())
				custList.add(new Customer());
			custList.get(0).actionNow = "GotCustomers";
			client.sendToClient(custList);
		}catch(Exception e){e.printStackTrace();}
	}

	/**
	 * Gets the phone of a customer so the tech can contact the person.
	 * @param customer Contains the information about the customer of which we're looking for the number.
	 * @param client is an object that will send a message back to the client
	 * @author orelzman 
	 */

	public void getPhone(Customer customer, ConnectionToClient client){
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT LTRIM(RTRIM(Phone)) FROM orelDeepdivers.Customers WHERE CustID = '" + customer.getCustID() + "';");
			if(rs.next()){
				customer.actionNow = "YesPhone";
				customer.setPhone(rs.getString(1));
			}else customer.actionNow = "NoMail";

			client.sendToClient(customer);
		}catch(Exception e){e.printStackTrace();}
	}

	/**
	 * Gets the phone of a customer so the tech can contact the person.
	 * @param order Contains the information about the customer of which we're looking for the number.
	 * @param client is an object that will send a message back to the client
	 * @author orelzman 
	 */
	public void getMail(Order order, ConnectionToClient client){
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT LTRIM(RTRIM(Email)) FROM orelDeepdivers.Customers WHERE CustID = '" + order.getCustID() + "';");
			Customer customer = new Customer();
			if(rs.next()){
				customer.actionNow = "YesMail";
				customer.setEmail(rs.getString(1));
			}else customer.actionNow = "NoMail";
			client.sendToClient(customer);
		}catch(Exception e){e.printStackTrace();}
	}

	public void checkNewOrders(ConnectionToClient client){
		try{
			Order order = new Order();
			Statement stmt = conn.createStatement();
			order.actionNow = "NoNewOrders";
			ResultSet rs = stmt.executeQuery("Select * from orelDeepdivers.Orders");
			while(rs.next())
				if(rs.getInt(6)==-1){//unhandled message was found.
					order.actionNow = "NewOrders";break;
				}
			client.sendToClient(order);
		}catch(Exception e){e.printStackTrace();}
	}

	/**
	 * Checks if there are new orders that are not reviewed yet by the tech.
	 * @param client is an object that will send a message back to the client
	 * @author orelzman 
	 */
	public void getNewOrders(ConnectionToClient client){
		ArrayList<Order> orderList = new ArrayList<Order>();
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("Select * from orelDeepdivers.Customers;");
			String temp;
			ArrayList<Customer> custList = new ArrayList<Customer>();

			while(rs.next()){
				temp = rs.getString(3).replaceAll("\\s+", "");//custID
				Customer customer = new Customer();
				customer.setCustID(temp);
				customer.setName(rs.getString(1).replaceAll("\\s+", ""));
				customer.setLastName(rs.getString(2).replaceAll("\\s+", ""));
				custList.add(customer);
			}

			rs = stmt.executeQuery("Select * from orelDeepdivers.Orders;");
			int i=0;
			while(rs.next())
				if(rs.getInt(6)==-1){//Add new unhandled order
					orderList.add(new Order(rs.getInt(6), rs.getString(2).replaceAll("\\s+", "")/*custID*/,
							rs.getString(3), rs.getString(5), 
							rs.getString(4), rs.getInt(1)));
					if(rs.getInt(8)==0)
						orderList.get(i).setClubOrPrivate("ציוד מועדון");
					else
						orderList.get(i).setClubOrPrivate("ציוד פרטי");
					for(Customer cust : custList)
						if(cust.getCustID().equals(orderList.get(i).getCustID())){
							orderList.get(i).setName(cust.getName());
							orderList.get(i).setLastName(cust.getLastName());
							i++;
							break;
						}
				}
			


			if(orderList.isEmpty())
				orderList.add(new Order());
			orderList.get(0).actionNow="OrderListReady";
			client.sendToClient(orderList);

		}catch(Exception e){e.printStackTrace();}
	}


	/**
	 * This method is called when the manager wants to add a new worker and the inputs are valid
	 * @param worker holds the information about the new worker 
	 * @param client is an object that will send a message back to the client
	 * @author orelzman
	 */
	public void addWorker(Worker worker, ConnectionToClient client){
		PreparedStatement pStmt;
		Statement stmt;
		try{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("Select * FROM orelDeepdivers.Workers;");
			String id, email;
			while(rs.next()){
				id=rs.getString(3).replaceAll("\\s+","");email=rs.getString(5).replaceAll("\\s+","");//Pulling info from the SQL server appears to dispatch it with spaces.
				if(id.equals(worker.getID())){
					worker.actionNow="IDExists";
					client.sendToClient(worker);
					return;
				}else if(email.equals(worker.getEmail())){
					worker.actionNow="EmailExists";
					client.sendToClient(worker);
					return;
				}

			}
			pStmt = conn.prepareStatement("insert into orelDeepdivers.Workers(Name, LastName, ID, "
					+ "isManager, Email, Password) values(?,?,?,?,?,?)");
			pStmt.setString(1, worker.getfName());
			pStmt.setString(2, worker.getlName());
			pStmt.setString(3, worker.getID());
			switch(worker.getIsManager()){
			case Dalpak:
				pStmt.setInt(4,-1);break;
			case Tech:
				pStmt.setInt(4,0);break;
			case Manager:
				pStmt.setInt(4,1);break;
			}
			pStmt.setString(5, worker.getEmail());
			pStmt.setString(6, worker.getPassword());
			pStmt.executeUpdate();
			worker.actionNow="WorkerAdded";
			client.sendToClient(worker);
		}catch(Exception e){e.printStackTrace();}
	}

	/**
	 * This method is called in order to confirm the manager's password
	 * @param worker holds the manager's password
	 * @param client is an object that will send a message back to the client
	 * @author orelzman
	 */
	public void managerPassword(Worker worker, ConnectionToClient client){
		Statement stmt;
		String query = "Select * from orelDeepdivers.ManagersPasswords where Password = '" + worker.getManagerPassword() +"';";
		try{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next())
				worker.actionNow = "ManagerPasswordCorrect";
			else
				worker.actionNow = "ManagerPasswordIncorrect";
			client.sendToClient(worker);
		}catch (Exception e){e.printStackTrace();}
	}

	/**
	 * This method is called when the dalpak wants to issue a new order for the lab.
	 * @param order holds the information about the new order.
	 * @param client is an object that will send a message back to the client
	 * @author orelzman
	 */
	public void issueOrder(Order order, ConnectionToClient client){
		PreparedStatement preparedStmt;
		Statement stmt;
		int max=0;
		try{
			preparedStmt = conn.prepareStatement("insert into orelDeepdivers.Orders(OrderNum, CustID, Description, "
					+ "Date, Comments, Handled, Cost, IsClubEquipment) values(?,?,?,?,?,?,?,?)");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Max(OrderNum) FROM OrelDeepdivers.Orders;");
			rs.next();max=rs.getInt(1);
			max++;
			preparedStmt.setInt(1, max);
			if(order.getCustID() == null){
				rs = stmt.executeQuery("SELECT LTRIM(RTRIM(CustID)) FROM OrelDeepdivers.Customers");			
				if(rs.next()){
					while(rs.next())
						if(Integer.parseInt(rs.getString(1))>max)
							max=Integer.parseInt(rs.getString(1));
					max++;
				}
				order.customer.setCustID(Integer.toString(max));
				newCustomer(order.customer, client);
				//Add a new customer to force the fk constraint.
				order.setCustID(Integer.toString(max));
				preparedStmt.setString(2, Integer.toString(max));
				order.actionNow = "NewClientOrder";
			}
			else{
				preparedStmt.setString(2, order.getCustID());
				order.actionNow = "OldClientOrder";
			}
			preparedStmt.setString(3, order.getDescription());
			preparedStmt.setString(4, order.getDate());
			preparedStmt.setString(5, order.getComments());
			preparedStmt.setInt(6, order.getHandled());
			preparedStmt.setInt(7, 0);
			if(order.isClubEquipment())
				preparedStmt.setInt(8, 1);
			else
				preparedStmt.setInt(8, 0);
			preparedStmt.executeUpdate();
			client.sendToClient(order);
		}catch(Exception e){e.printStackTrace();}
	}


	/**
	 * This method checks if the id and password exist in the system.
	 * @param worker holds the information about the logged in worker, if his info exist in the system.
	 * @param client is an object that will send a message back to the client
	 * @author orelzman
	 */
	public void checkUserInfo(Worker worker, ConnectionToClient client){
		Statement stmt;
		try {stmt = conn.createStatement(); 
		ResultSet rs = stmt.executeQuery(worker.query);
		if(!rs.next()){
			worker.actionNow = "Incorrect";
			client.sendToClient(worker);
		}
		else{
			Worker w = new Worker(rs.getString(3), rs.getString(1),rs.getString(2), rs.getString(5), rs.getInt(4));
			w.actionNow = "Correct";
			client.sendToClient(w);
		}
		}catch (SQLException | IOException e) {e.printStackTrace();}
	}


	/**
	 * This methods sets a connection with the SQL server, which contains the DB.
	 */
	public void connectToDB() {
		try {
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			this.conn = DriverManager.getConnection("jdbc:sqlserver://188.121.44.212:1433;databaseName=orel;", "orelDeepdivers", "1qaz2wsx");
		}
		catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			Windows.warning("החיבור לאינטרנט כשל" + "\n" + "...נסה שוב מאוחר יותר");
		}
	}
}
