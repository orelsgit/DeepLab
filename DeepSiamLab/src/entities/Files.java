package entities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

import javax.swing.JFileChooser;

import javafx.stage.FileChooser;
import main.Main;

public class Files implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] fileTypes = {"pdf", "docx", "exe"};
	private String fileName=null, fileType;
	private int len = 0;
	@SuppressWarnings("unused")
	private GeneralMethods GM = new GeneralMethods();
	private byte[] buffer;
	private File file;



	public int getLen(){
		return len;
	}

	public File getFile(){
		return file;
	}


	@SuppressWarnings("resource")
	public void setFile(){
		boolean isType=false;
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Files", "*.pdf", "*.docx", "*.exe");
		fileChooser.getExtensionFilters().add(extFilter);
		file = fileChooser.showOpenDialog(Main.primaryStage);
		if(file == null)
			return;
		fileType=file.getName().substring(file.getName().lastIndexOf(".") + 1);
		for(String str : fileTypes)
			if(str.equals(fileType)){
				isType=true;break;
			}
		if(!isType){
			Windows.warning("סוג הקובץ לא מתאים. סוגים מתאימים: " + fileTypes);
		}
		fileName=file.getName();
		len = (int)file.length();
		ByteArrayOutputStream ous;

		try {
			FileInputStream fis = new FileInputStream(file);

			buffer = new byte[len];
			ous = new ByteArrayOutputStream();

			int read = 0;
			while ((read = fis.read(buffer)) != -1) {
				ous.write(buffer, 0, read);
			}


		}catch(Exception e){e.printStackTrace(); }

	}

	public static String getDestination(){
		JFileChooser chooser = new JFileChooser(); 
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("בחר מיקום");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//
		// disable the "All files" option.
		//
		chooser.setAcceptAllFileFilterUsed(false);
		//    
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
			return chooser.getSelectedFile().toString();
		}
		else {
			return "";
		}
		 
	}


	public byte[] getBuffer(){
		return this.buffer;
	}


	public String[] getFileTypes() {
		return fileTypes;
	}



	public void setFileTypes(String[] fileTypes) {
		this.fileTypes = fileTypes;
	}



	public String getFileName() {
		return fileName;
	}



	public void setFileName(String fileName) {
		this.fileName = fileName;
	}



	public String getFileType() {
		return fileType;
	}



	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
