package controllers;

import entities.GeneralMethods;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import main.Main;

public class FixController {
	
	private GeneralMethods GM =new GeneralMethods();
	@FXML
	private Text titleText;
	@FXML
	private TextArea fixTextArea;
	@FXML
	private TextField costTextField;

public void onBack(){
	GM.closePopup(Main.popup2);

}

public void onContinue(){
	
}
	
}
