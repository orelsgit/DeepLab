package controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import entities.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import main.Client;
import main.Main;

public class AddEquipment {

	@FXML
	private TextField sizeTextField, bcdModelTextField, bcdManuTextField, regModelTextField, regManuTextField, interTextField,
	volumeTextField, tankManuTextField, bcdDeepNumTextField;
	@FXML
	private Text sizeText, bcdModelText, bcdManuText, regModelText, regManuText, interText,
	volumeText, tankManuText, fileNameText, bcdDeepNumText;

	private BCD bcd;
	private Tank tank;
	private Regulator regulator;
	private CCR ccr;

	public static boolean isBackFromServer = false;


	private GeneralMethods GM = new GeneralMethods();

	public AddEquipment(){
		bcd = null;
		tank = null;
		regulator = null;
		ccr = null;
	}

	public void onAddRegulator(){
		isBackFromServer=false;

		boolean isFilled = true;

		regModelText.setFill(Color.BLACK);
		regManuText.setFill(Color.BLACK);
		interText.setFill(Color.BLACK);

		if(regModelTextField.getText().equals("")){
			regModelText.setFill(Color.RED);isFilled=false;
		}
		if(regManuTextField.getText().equals("")){
			regManuText.setFill(Color.RED);isFilled=false;
		}
		if(interTextField.getText().equals("")){
			interText.setFill(Color.RED);isFilled=false;
		}


		if(!isFilled)
			return;

		if(!GM.checkText(regModelTextField.getText()))
			return;
		if(!GM.checkText(regManuTextField.getText()))
			return;
		if(!GM.checkText(interTextField.getText()))
			return;

		Regulator reg = new Regulator(regModelTextField.getText(), regManuTextField.getText(), Float.parseFloat(interTextField.getText()));
		GM.sendServerThread(reg, "AddRegulator");

		while(!isBackFromServer)
			GM.Sleep(2);

	}
	public void onAddBCD(){
		isBackFromServer=false;

		boolean isFilled = true;

		sizeText.setFill(Color.BLACK);
		bcdModelText.setFill(Color.BLACK);
		bcdManuText.setFill(Color.BLACK);


		if(sizeTextField.getText().equals("")){
			sizeText.setFill(Color.RED);isFilled=false;
		}
		if(bcdModelTextField.getText().equals("")){
			bcdModelText.setFill(Color.RED);isFilled=false;
		}
		if(bcdManuTextField.getText().equals("")){
			bcdManuText.setFill(Color.RED);isFilled=false;
		}
		if(bcdDeepNumTextField.getText().equals("")){
			bcdDeepNumText.setFill(Color.RED);isFilled=false;
		}
		if(!isFilled)
			return;

		if(!GM.checkText(sizeTextField.getText()))
			return;
		if(!GM.checkText(bcdModelTextField.getText()))
			return;
		if(!GM.checkText(bcdManuTextField.getText()))
			return;
		if(!GM.checkText(bcdDeepNumTextField.getText()))
			return;
		if(bcd == null)//No file was uploaded
			bcd = new BCD();
		bcd.setSize(sizeTextField.getText());
		bcd.setModel(bcdModelTextField.getText());
		bcd.setManufacturer(bcdManuTextField.getText());
		bcd.setDeepNum(bcdDeepNumTextField.getText());

		if(fileNameText.getText()==null)
			if(Windows.yesNo("האם אתה בטוח שברצונך להמשיך בלי להעלות קובץ?", "אין קובץ", "העלה קובץ", "המשך בלי להעלות קובץ"))
				onUploadFileBCD();

		GM.sendServerThread(bcd, "AddBCD");

		while(!isBackFromServer)
			GM.Sleep(2);




	}	
	public void onAddTank(){
		isBackFromServer=false;

		volumeText.setFill(Color.BLACK);
		tankManuText.setFill(Color.BLACK);


		boolean isFilled = true;

		volumeText.setFill(Color.BLACK);
		tankManuText.setFill(Color.BLACK);

		if(volumeTextField.getText().equals("")){
			volumeText.setFill(Color.RED);isFilled=false;
		}
		if(tankManuTextField.getText().equals("")){
			tankManuText.setFill(Color.RED);isFilled=false;
		}

		if(!isFilled)
			return;

		if(!GM.checkText(tankManuTextField.getText()))
			return;
		if(!GM.checkText(volumeTextField.getText()))
			return;

		//Tank tank = new Tank(tankModelTextField.getText(), tankManuTextField.getText(), volumeTextField.getText());
		//GM.sendServerThread(tank, "AddTank");

		while(!isBackFromServer)
			GM.Sleep(2);


	}

	public void onUploadFileBCD(){
		if(bcd==null)
			bcd = new BCD();
		bcd.getFiles().setFile();
		fileNameText.setText(bcd.getFiles().getFileName());

	}



	public void onBack(){
		GM.closePopup(Main.popup);
	}
}
