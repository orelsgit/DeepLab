package entities;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import entities.Windows;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.Client;
import main.Main;


public class GeneralMethods {

	private static Thread thread;

	public void sendMail(String to, String subject, String content, String fr, String pass){
		thread = new Thread(){
			public void run(){
				// Get a Properties object
				String from;
				String password;
				Properties props = setProperties();
					from = fr;
					password = pass;
				try{
					Session session = Session.getDefaultInstance(props, 
							new Authenticator(){
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(from, password);
						}});
					
					// -- Create a new message --
					Message msg = new MimeMessage(session);

					// -- Set the FROM and TO fields --
					msg.setFrom(new InternetAddress(from));
					msg.setRecipients(Message.RecipientType.TO, 
							InternetAddress.parse(to,false));
					msg.setSubject(subject);
					msg.setText(content);
					msg.setSentDate(new Date());
					Transport.send(msg);
					Windows.message("!אימייל נשלח", "נשלח");
					System.out.println("To: " + to + "From: " + from + "Password" + password);
				}catch (MessagingException e){ e.printStackTrace(); }
			}
		};thread.start();
	
	}

	
	public Properties setProperties(){
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.store.protocol", "pop3");
		props.put("mail.transport.protocol", "smtp");	
		return props;
	}



	public void closePopup(Stage popup){
		popup.close();
	}

	public void setPopup(Stage popup, String window, String title){
		try {
			Pane mainLayout = FXMLLoader.load(Main.class.getResource("/GUI/" + window + ".fxml"));
			popup.setScene(new Scene(mainLayout));
			popup.setTitle(title);
		} catch (IOException e) {e.printStackTrace();}
	}

	public void getPopup(Stage popup, String window, String title){
		try {
			Pane mainLayout = FXMLLoader.load(Main.class.getResource("/GUI/" + window + ".fxml"));
			popup.setScene(new Scene(mainLayout));
			popup.setTitle(title);
			popup.showAndWait();
		} catch (IOException e) {e.printStackTrace();}
	}


	public void sendServer(Object msg, String actionNow){
		try {
			((GeneralMessage)msg).actionNow = actionNow;
			Client client = new Client();
			try {
				client.openConnection();
				client.sendToServer(msg);
			} catch (Exception e) {e.printStackTrace();}
		} catch (Exception e) {	e.printStackTrace();}
	}


	public void sendServerThread(Object msg, String actionNow){
		Thread thread = new Thread(){
			public void run(){
				try {
					((GeneralMessage)msg).actionNow = actionNow;
					Client client = new Client();
					try {
						client.openConnection();
						client.sendToServer(msg);
					} catch (Exception e) {e.printStackTrace();}
				} catch (Exception e) {	e.printStackTrace();}
			}
		};thread.start();
	}


	/**
	 * This method sends a message to the server via new joined thread.
	 * @param msg holds crucial information that we want to send the server in order to run some qeuries.
	 * @param actionNow holds information about the current job we want our server to accomplish.
	 * @author orelzman
	 */


	public void sendServerJoin(Object msg, String actionNow){/******************************/
		Thread thread = new Thread(){
			public void run(){
				try {
					((GeneralMessage)msg).actionNow = actionNow;
					Client client = new Client();
					try {
						client.openConnection();
						client.sendToServer(msg);
					} catch (Exception e) {e.printStackTrace();}
				} catch (Exception e) {	e.printStackTrace();}
			}
		};thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	/**
	 * This method makes the current running thread sleep, in order to wait for the server's response and react accordingly.
	 * @param time will tell the method for how long will the thread sleep.
	 * @author orelzman
	 */
	public void Sleep(int time){
		try{Thread.sleep(time);}catch(InterruptedException e){e.printStackTrace();}
	}
	/**
	 * This method checks if the string the user inserted is compatible with SQL syntax
	 * @param str The user's string
	 * @return if false, the string in not compatible, else compatible.
	 * @author orelzman
	 */
	public boolean checkText(String str){
		if(str.contains("'") || str.contains("\\") || str.contains("/") || str.contains(",") || str.contains("\\s+") ){
			Windows.warning("The characters :, \\, ' are not allowed.");
			return false;
		}
		return true;

	}

}
