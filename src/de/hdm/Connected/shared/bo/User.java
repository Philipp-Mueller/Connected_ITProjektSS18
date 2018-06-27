package de.hdm.Connected.shared.bo;

/**
 * 
 * @author Moritz
 *
 */
public class User extends BusinessObject{

	private static final long serialVersionUID = 1L;
	private String logEmail = "";
	private String name = "";
	
	
	/**
	 * @return the email
	 */
	public String getLogEmail() {
		return logEmail;
	}
	/**
	 * @param email the email to set
	 */
	public void setLogEmail(String email) {
		this.logEmail = email;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	

	
}
