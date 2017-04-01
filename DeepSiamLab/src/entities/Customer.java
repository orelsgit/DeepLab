package entities;

public class Customer extends GeneralMessage{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String name, lastName, custID, email, phone, id, dob;



public Customer( String name, String lastName, String custID, String email, String phone, String id, String dob) {//for customer list
	this.name = name;
	this.lastName = lastName;
	this.custID = custID;
	this.email = email;
	this.phone = phone;
	this.id = id;
}

public Customer(String name, String lastName, String phone, String email, String id, String dob) {//for customer add
	super();
	this.name = name;
	this.lastName = lastName;
	this.id = id;
	this.email = email;
	this.phone = phone;
	this.id = dob;
}

public Customer(){}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getLastName() {
	return lastName;
}

public void setLastName(String lastName) {
	this.lastName = lastName;
}

public String getCustID() {
	return custID;
}

public void setCustID(String custID) {
	this.custID = custID;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getPhone() {
	return phone;
}

public void setPhone(String phone) {
	this.phone = phone;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getDob() {
	return dob;
}

public void setDob(String dob) {
	this.dob = dob;
}

}
