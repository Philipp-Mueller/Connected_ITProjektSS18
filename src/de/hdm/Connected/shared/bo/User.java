package de.hdm.Connected.shared.bo;

/**
 * 
 * @author Moritz
 *
 */
public class User extends BusinessObject{

	private static final long serialVersionUID = 1L;
	private String name = "";
	private String email = "";
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
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	

	
}
