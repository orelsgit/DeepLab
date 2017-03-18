package entities;

public class Worker extends GeneralMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ID,fName,lName,email, managerPassword, password;
	private Status isManager;//1 - manager, 0 - tech, -1 - dalpak
	private static Worker currentWorker;
	public Worker(){}
	public Worker(String ID,String fName,String lName,String email,int isManager)
	{
		this.ID = ID;
		this.fName = fName;
		this.lName = lName;
		this.email = email;
		switch(isManager){
		case -1:
			this.isManager = Status.Dalpak;break;
		case 0:
			this.isManager = Status.Tech;break;
		case 1:
			this.isManager = Status.Manager;break;
		}
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Status getIsManager() {
		return isManager;
	}
	public void setIsManager(int isManager) {
		switch(isManager){
		case -1:
			this.isManager = Status.Dalpak;break;
		case 0:
			this.isManager = Status.Tech;break;
		case 1:
			this.isManager = Status.Manager;break;
		}
	}
	public static Worker getCurrentWorker() {
		return currentWorker;
	}
	public static void setCurrentWorker(Worker currentWorker) {
		Worker.currentWorker = currentWorker;
	}
	public String getManagerPassword() {
		return managerPassword;
	}
	public void setManagerPassword(String managerPassword) {
		this.managerPassword = managerPassword;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}	
