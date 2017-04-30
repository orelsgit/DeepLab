package main;

import java.io.IOException;

import entities.GeneralMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

	public static Stage popup, popup2, popup3;
	public static Stage primaryStage;
	public static Parent mainLayout;
	public static int port=3307;
	public static String host = "localhost";

	public void start(Stage primaryStage) throws IOException {

		Main.primaryStage = primaryStage;
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				
				Platform.exit();
				System.exit(0);
			}
		});
		
		Image duck = new Image("file:///C:/Users/orels/Desktop/Java/DeepSiamLab/DeepDuck.png");
		primaryStage.getIcons().add(duck); //set icon in the corner
		popup = new Stage(); 
		popup.initModality(Modality.APPLICATION_MODAL);
		popup.initOwner(primaryStage);
		popup.getIcons().add(duck);
		popup2 = new Stage(); 
		popup2.initModality(Modality.APPLICATION_MODAL);
		popup2.initOwner(popup);
		popup2.getIcons().add(duck);
		popup3 = new Stage(); 
		popup3.initModality(Modality.APPLICATION_MODAL);
		popup3.initOwner(popup2);
		popup3.getIcons().add(duck);
		showMenu("MainScreen");
	}

	public static void showMenu(String screen) {
		GeneralMessage.currentWindow = screen;
		FXMLLoader loader = new FXMLLoader(); 
		loader.setLocation(Main.class.getResource("/GUI/" + screen + ".fxml"));
		try {mainLayout = loader.load();} catch (IOException e) {e.printStackTrace();}
		primaryStage.setTitle(screen);
		primaryStage.setScene(new Scene(mainLayout));
		primaryStage.sizeToScene();
		primaryStage.show();
	}


	public static void setPrimaryStage(Stage primaryStage) {
		Main.primaryStage = primaryStage;
	}


	public static void main(String[] args) 
	{
		Thread t = new Thread(){
			@SuppressWarnings("unused")
			public void run(){
				Server s1 = new Server(Main.port);
			}
		};t.start();
		launch(args);


	}//end main
}//end class


