package de.hdm.Connected.shared.bo;

public class Value extends BusinessObject{

	private static final long serialVersionUID = 1L;
	private String name = "";
	private int propertyID = 0;
	private int contactID = 0;
	/**
	 * @return the description
	 */
	public String getDescription() {
		return name;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String name) {
		this.name = name;
	}
	/**
	 * @return the propertyID
	 */
	public int getPropertyID() {
		return propertyID;
	}
	/**
	 * @param propertyID the propertyID to set
	 */
	public void setPropertyID(int propertyID) {
		this.propertyID = propertyID;
	}
	/**
	 * @return the contactID
	 */
	public int getContactID() {
		return contactID;
	}
	/**
	 * @param contactID the contactID to set
	 */
	public void setContactID(int contactID) {
		this.contactID = contactID;
	}
	
	

}
