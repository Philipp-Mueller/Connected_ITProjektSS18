package de.hdm.Connected.shared.bo;


/**
*** Klasse um Eigenschaftausprägungen darzustellen
*	@author Moritz
*/
public class Value extends SharedObject{

	private static final long serialVersionUID = 1L;
	private String name = "";
	
	// Fremdschlüsselbeziehung zu Eigenschaft
	private int propertyID = 0;
	
	//Fremdschlüssel um Beziehung zu Contact zu realisieren
	private int contactID = 0;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param description the description to set
	 */
	public void setName(String name) {
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
