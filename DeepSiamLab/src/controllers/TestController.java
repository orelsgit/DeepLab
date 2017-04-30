package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import main.Main;

public class TestController {

	//@FXML
	//private VBox buttonVBox;
	@FXML
	private GridPane gridPane;
	@FXML
	private AnchorPane test;
	@FXML
	private Button button;
	
	public void initialize(){
		button.setMaxHeight(800);
		button.setMaxWidth(600);
		/*
		Main.primaryStage.heightProperty().addListener(new ChangeListener<Object>(){
			@Override
			public void changed(ObservableValue<?> arg0, Object oldHeight, Object newHeight){
				System.out.println(oldHeight + "  " + newHeight);
			double newH = (double)newHeight;
			double oldH = (double)oldHeight;
			double buttonSize = button.getHeight()*((newH)/(oldH));
			button.setPrefHeight(buttonSize);
			
			}
		});*/
		//gridPane.prefHeightProperty().bind(test.heightProperty());
		//gridPane.prefWidthProperty().bind(test.widthProperty());
		//button.prefHeightProperty().bind(test.heightProperty());
		//button.prefWidthProperty().bind(test.widthProperty());
	}
}
