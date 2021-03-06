/**
 * 
 */
package de.hdm.Connected.shared.bo;

import java.sql.Timestamp;

/**
 * Klasse um ContactListen darzustellen
 * @author Denise
 *
 */
public class ContactList extends SharedObject {

	private static final long serialVersionUID = 1L;
	private String name ="";
	
	// Realisierung eine Beziehung zu Contact durch Fremdschlüssel

	private int contactId = 0;
	
	// Realisierung eine Beziehung zu einem User durch Fremdschlüssel

	private int UserId = 0;
	
	// Realisierung eine Beziehung zu einer Permission durch Fremdschlüssel
	
	private int permissionId = 0;
	
	// Realisierung des ModifikationsDatums um Status einer Contactliste kommunizieren zu können
	
	private Timestamp modificationDate= null;
	
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

	public ContactList() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the contactId
	 */
	public int getContactId() {
		return contactId;
	}
	/**
	 * @param contactId the contactId to set
	 */
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return UserId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		UserId = userId;
	}
	/**
	 * @return the permissionId
	 */
	public int getPermissionId() {
		return permissionId;
	}
	/**
	 * @param permissionId the permissionId to set
	 */
	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
	}
	
	public Timestamp getModificationDate(){
		return modificationDate;
	}
	
	public void setModificationDate(Timestamp modificationDate) {
		this.modificationDate = modificationDate;
		
	}
	
	

}
