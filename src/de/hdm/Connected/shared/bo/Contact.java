package de.hdm.Connected.shared.bo;


/**
 * 
 * @author Moritz
 *
 */
public class Contact extends BusinessObject{


	private static final long serialVersionUID = 1L;
	private String name ="";
	private int contactlistID = 0;
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
