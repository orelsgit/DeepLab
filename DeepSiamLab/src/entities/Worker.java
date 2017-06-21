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
		case 2:
			this.isManager = Status.Programmer;break;
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
		case 2:
			this.isManager = Status.Programmer;break;
		}
	}
	public static Worker getCurrentWorker() {
		return currentWorker;
	}
	public static void setCurrentWorker(Worker currentWorker) {//(String ID,String fName,String lName,String email,int isManager)

		if(currentWorker == null){

			Worker.currentWorker = null;
			return;
		}
		if(currentWorker.actionNow.equals("Incorrect")){
			System.out.println("Incorrect");
			currentWorker = new Worker();
			return;
		}

		if(currentWorker.getIsManager() == null)
			return;
		int isM=0;
		switch(currentWorker.getIsManager()){
		case Dalpak:
			isM=-1;break;
		case Tech:
			isM=0;break;
		case Manager:
			isM=1;break;
		case Programmer:
			isM=2;break;
		default:
			break;
		}
		
		Worker.currentWorker = new Worker(currentWorker.getID(), currentWorker.getfName(), currentWorker.getlName(),currentWorker.getEmail(), isM);
		Worker.currentWorker.actionNow="";
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
