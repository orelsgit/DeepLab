package entities;

public class Update extends GeneralMessage{
	private Files file;
	private String description, destination;
	private int version;

	
	public Update(){}
	
	public Update(String description, int version){
		this.setDescription(description);
		this.version = version;
		setFile(new Files());
	}

	public Files getFile() {
		return file;
	}

	public void setFile(Files file) {
		this.file = file;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		version = version;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
}