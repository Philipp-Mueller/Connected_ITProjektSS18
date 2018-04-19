package de.hdm.Connected.shared.bo;


/**
 * 
 * @author Moritz
 *
 */
public class Contact extends BusinessObject{


	private static final long serialVersionUID = 1L;
	private String prename ="";
	private String surname ="";
	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}
	/**
	 * @return the logInEmail
	 */
	public String getLogInEmail() {
		return logInEmail;
	}
	/**
	 * @param logInEmail the logInEmail to set
	 */
	public void setLogInEmail(String logInEmail) {
		this.logInEmail = logInEmail;
	}
	private int contactlistID = 0;
	private String logInEmail = "";
	/**
	 * @return the name
	 */
	public String getPrename() {
		return prename;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String prename) {
		this.prename = prename;
	}
	/**
	 * @return the contactlistID
	 */
	public int getContactlistID() {
		return contactlistID;
	}
	/**
	 * @param contactlistID the contactlistID to set
	 */
	public void setContactlistID(int contactlistID) {
		this.contactlistID = contactlistID;
	}
	
}
