package main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import controllers.CustomerSearchController;
import entities.Customer;
import entities.GeneralMessage;
import entities.Order;
import entities.Windows;
import entities.Worker;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class Server extends AbstractServer {    
	Connection conn;



	public Server(int port) {
		super(port);
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
			
		}
	}
	
	public void getCustomers(Customer customer, ConnectionToClient client){
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM orelDeepdivers.Customers");
			ArrayList<Customer> custList = new ArrayList<Customer>();
			while(rs.next())
				custList.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
			custList.get(0).actionNow = "GotCustomers";
			client.sendToClient(custList);
		}catch(Exception e){e.printStackTrace();}
	}
	
	public void getPhone(Customer customer, ConnectionToClient client){
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Phone FROM orelDeepdivers.Customers WHERE CustID = '" + customer.getCustID() + "';");
			if(rs.next()){
			customer.actionNow = "YesPhone";
			customer.setPhone(rs.getString(1));
			}else customer.actionNow = "NoMail";
			
			client.sendToClient(customer);
		}catch(Exception e){e.printStackTrace();}
	}

	
	public void getMail(Order order, ConnectionToClient client){
		System.out.println("getemail server");
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Email FROM orelDeepdivers.Customers WHERE CustID = '" + order.getCustID() + "';");
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


	public void getNewOrders(ConnectionToClient client){
		ArrayList<Order> orderList = new ArrayList<Order>();
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("Select * from orelDeepdivers.Orders;");
			while(rs.next())
				if(rs.getInt(6)==-1)//Add new unhandled order
					orderList.add(new Order(rs.getInt(6), rs.getString(2).replaceAll("\\s+", "")/*custID*/,
							rs.getString(3), rs.getString(5), 
							rs.getString(4), rs.getInt(1)));
			rs = stmt.executeQuery("Select * from orelDeepdivers.Customers;");
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

			for(Order order : orderList)
				for(Customer customer : custList)
					if(order.getCustID().equals(customer.getCustID())){
						order.setName(customer.getName() + " " + customer.getLastName());break;
					}

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
			ResultSet rs = stmt.executeQuery("SELECT * FROM orelDeepdivers.Workers;");
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
					+ "Date, Comments, Handled) values(?,?,?,?,?,?)");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(OrderNum) FROM OrelDeepdivers.Orders");
			rs.next();max=rs.getInt(1);
			max++;
			preparedStmt.setInt(1, max);
			preparedStmt.setString(2, order.getCustID());
			preparedStmt.setString(3, order.getDescription());
			preparedStmt.setString(4, order.getDate());
			preparedStmt.setString(5, order.getComments());
			preparedStmt.setInt(6, order.getHandled());
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

