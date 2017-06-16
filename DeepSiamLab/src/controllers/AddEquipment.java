package controllers;

import entities.BCD;
import entities.CCR;
import entities.GeneralMethods;
import entities.Regulator;
import entities.Tank;
import entities.Windows;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.Main;
import entities.Error;

public class AddEquipment {

	@FXML
	private TextField sizeTextField, bcdModelTextField, bcdManuTextField, regModelTextField, regManuTextField, interTextField,
	volumeTextField, tankManuTextField, bcdDeepNumTextField, ccrManuTextField, ccrModelTextField, ccrSerialNumTextField,
	tankModelTextField, tankDeepNumTextField, tankSNumTextField, regSNumTextField, regDeepNumTextField;
	@FXML
	private Text sizeText, bcdModelText, bcdManuText, regModelText, regManuText, interText, ccrManuText, ccrModelText, ccrSerialNumText,
	volumeText, tankManuText, bcdFileNameText, bcdDeepNumText, regFileNameText, ccrFileNameText, tankFileNameText,
	tankModelText, tankDeepNumText, tankSNumText, regSNumText, regDeepNumText;
	@FXML
	private CheckBox aluminiumCheckBox, steelCheckBox;

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


	public void onSteel(){
		if(aluminiumCheckBox.isSelected())
			aluminiumCheckBox.setSelected(false);
	}

	public void onAluminium(){
		if(steelCheckBox.isSelected())
			steelCheckBox.setSelected(false);
	}

	public void onAddCCR(){
		isBackFromServer = false;

		boolean isFilled=true;

		ccrModelText.setFill(Color.BLACK);
		ccrManuText.setFill(Color.BLACK);
		ccrSerialNumText.setFill(Color.BLACK);

		if(ccrModelTextField.getText().equals("")){
			ccrModelText.setFill(Color.RED);isFilled=false;
		}
		if(ccrManuTextField.getText().equals("")){
			ccrManuText.setFill(Color.RED);isFilled=false;
		}
		if(ccrSerialNumTextField.getText().equals("")){
			ccrSerialNumText.setFill(Color.RED);isFilled=false;
		}

		if(!isFilled)
			return;

		if(!GM.checkText(ccrModelTextField.getText()) 
				&& !GM.checkText(ccrManuTextField.getText()) && !GM.checkText(ccrSerialNumTextField.getText()))
			return;
		if(ccr == null)
			ccr = new CCR();

		ccr = new CCR(ccrModelTextField.getText(), ccrManuTextField.getText(), ccrSerialNumTextField.getText());
		GM.sendServerThread(ccr, "AddCCR");

		Error error = new Error("AddEquipment", "onAddCCR", 0);
		int timesCalled = 0;
		while(!isBackFromServer)
			if(!GM.Sleep(70, error, timesCalled++))
				return;


	}


	public void onAddRegulator(){
		isBackFromServer=false;

		boolean isFilled = true;

		regModelText.setFill(Color.BLACK);
		regManuText.setFill(Color.BLACK);
		interText.setFill(Color.BLACK);
		regSNumText.setFill(Color.BLACK);
		regDeepNumText.setFill(Color.BLACK);

		if(regModelTextField.getText().equals("")){
			regModelText.setFill(Color.RED);isFilled=false;
		}
		if(regManuTextField.getText().equals("")){
			regManuText.setFill(Color.RED);isFilled=false;
		}
		if(interTextField.getText().equals("")){
			interText.setFill(Color.RED);isFilled=false;
		}
		if(regSNumTextField.getText().equals("")){
			regSNumText.setFill(Color.RED);isFilled=false;
		}
		if(regDeepNumTextField.getText().equals("")){
			regDeepNumText.setFill(Color.RED);isFilled=false;
		}


		if(!isFilled)
			return;

		if(!GM.checkText(regModelTextField.getText()))
			return;
		if(!GM.checkText(regManuTextField.getText()))
			return;
		if(!GM.checkText(interTextField.getText()))
			return;
		if(!GM.checkText(regDeepNumTextField.getText()))
			return;
		if(!GM.checkText(regSNumTextField.getText()))
			return;
		
		if(regulator == null)
			regulator = new Regulator();
		
		if(interTextField.getText().contains("[a-zA-Z]+")){
			Windows.warning("הלחץ החלקי חייב להכיל מספרים בלבד");
			return;
		}

		regulator = new Regulator(regModelTextField.getText(), regManuTextField.getText(), Float.parseFloat(interTextField.getText()),
				regSNumTextField.getText(), regDeepNumTextField.getText());
		GM.sendServerThread(regulator, "AddRegulator");


		Error error = new Error("AddEquipment", "onAddRegulator", 1);
		int timesCalled = 0;
		while(!isBackFromServer)
			if(!GM.Sleep(70, error, timesCalled++))
				return;


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

		if(bcdFileNameText.getText()==null)
			if(Windows.yesNo("האם אתה בטוח שברצונך להמשיך בלי להעלות קובץ?", "אין קובץ", "העלה קובץ", "המשך בלי להעלות קובץ"))
				onUploadFileBCD();


		GM.sendServer(bcd, "AddBCD");

		Error error = new Error("AddEquipment", "onAddBCD", 2);
		int timesCalled = 0;
		while(!isBackFromServer)
			if(!GM.Sleep(70, error, timesCalled++))
				return;
			



	}	
	public void onAddTank(){
		isBackFromServer=false;

		volumeText.setFill(Color.BLACK);
		tankManuText.setFill(Color.BLACK);
		tankModelText.setFill(Color.BLACK);


		boolean isFilled = true;

		volumeText.setFill(Color.BLACK);
		tankManuText.setFill(Color.BLACK);

		if(volumeTextField.getText().equals("")){
			volumeText.setFill(Color.RED);isFilled=false;
		}
		if(tankManuTextField.getText().equals("")){
			tankManuText.setFill(Color.RED);isFilled=false;
		}
		if(tankModelTextField.getText().equals("")){
			tankModelText.setFill(Color.RED);isFilled=false;
		}
		if(tankSNumTextField.getText().equals("")){
			tankSNumText.setFill(Color.RED);isFilled=false;
		}
		if(tankDeepNumTextField.getText().equals("")){
			tankDeepNumText.setFill(Color.RED);isFilled=false;
		}
		if(!isFilled)
			return;

		if(!GM.checkText(tankManuTextField.getText()))
			return;
		if(!GM.checkText(volumeTextField.getText()))
			return;
		if(!GM.checkText(tankModelTextField.getText()))
			return;
		if(!GM.checkText(tankSNumTextField.getText()))
			return;
		if(!GM.checkText(tankDeepNumTextField.getText()))
			return;


		int a;
		if(aluminiumCheckBox.isSelected())
			a=1;
		else
			a=0;
		
		if(tank == null)
			tank = new Tank(); 
		if(volumeTextField.getText().contains("[a-zA-Z]+")){
			Windows.warning("הנפח חייב להכיל מספרים בלבד");
			return;
		}
		
		tank = new Tank(tankModelTextField.getText(), tankManuTextField.getText(), Integer.parseInt(volumeTextField.getText()),
				tankSNumTextField.getText(), tankDeepNumTextField.getText(), a);
		GM.sendServerThread(tank, "AddTank");

		Error error = new Error("AddEquipment", "onAddTank", 3);
		int timesCalled = 0;
		while(!isBackFromServer)
			if(!GM.Sleep(70, error, timesCalled++))
				return;
			


	}

	public void onUploadFileTank(){
		if(tank==null)
			tank = new Tank();
		tank.getFiles().setFile();
		tankFileNameText.setText(tank.getFiles().getFileName());
	}

	public void onUploadFileBCD(){
		if(bcd==null)
			bcd = new BCD();
		bcd.getFiles().setFile();
		bcdFileNameText.setText(bcd.getFiles().getFileName());
	}

	public void onUploadFileCCR(){
		if(ccr==null)
			ccr = new CCR();
		ccr.getFiles().setFile();
		ccrFileNameText.setText(ccr.getFiles().getFileName());
	}

	public void onUploadFileReg(){
		if(regulator==null)
			regulator = new Regulator();
		regulator.getFiles().setFile();
		regFileNameText.setText(regulator.getFiles().getFileName());
	}




	public void onBack(){
		GM.closePopup(Main.popup);
	}
}
