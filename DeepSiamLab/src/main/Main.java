package main;

import java.io.IOException;

import entities.SpeechUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

	public static Stage popup, popup2, popup3;
	private static Stage primaryStage;
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

		//   primaryStage.getIcons().add(new Image("/src/41.png")); set icon in the corner
		popup = new Stage(); 
		popup.initModality(Modality.APPLICATION_MODAL);
		popup.initOwner(primaryStage);
		popup2 = new Stage(); 
		popup2.initModality(Modality.APPLICATION_MODAL);
		popup2.initOwner(popup);
		popup3 = new Stage(); 
		popup3.initModality(Modality.APPLICATION_MODAL);
		popup3.initOwner(popup2);
		showMenu("MainScreen");
	}

	public static void showMenu(String screen) {
		FXMLLoader loader = new FXMLLoader(); 
		loader.setLocation(Main.class.getResource("/GUI/" + screen + ".fxml"));
		try {mainLayout = loader.load();} catch (IOException e) {e.printStackTrace();}
		primaryStage.setTitle(screen);
		primaryStage.setScene(new Scene(mainLayout));
		primaryStage.show();
	}


	public static void setPrimaryStage(Stage primaryStage) {
		Main.primaryStage = primaryStage;
	}


	public static void main(String[] args) 
	{
		Thread t = new Thread(){
			public void run(){
				Server s1 = new Server(Main.port);
			}
		};t.start();
		//try {t.join();} catch (InterruptedException e) {e.printStackTrace();}
		launch(args);


	}//end main
}//end class


