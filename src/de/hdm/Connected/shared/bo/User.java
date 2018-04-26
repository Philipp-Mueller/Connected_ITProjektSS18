package de.hdm.Connected.shared.bo;

/**
 * 
 * @author Moritz
 *
 */
public class User extends BusinessObject{

	private static final long serialVersionUID = 1L;
	private String logEmail = "";
	
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
	

	
}
