package de.hdm.Connected.shared.bo;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 
 * @author Moritz
 *
 */

public class BusinessObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id = 0;
	private Timestamp creationDate = new Timestamp(System.currentTimeMillis());
	private Timestamp modificationDate = new Timestamp(System.currentTimeMillis());
	private int modifiyerID = 0;
	private int creatorID = 0;

	
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the creationDate
	 */
	public Timestamp getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return the modificationDate
	 */
	public Timestamp getModificationDate() {
		return modificationDate;
	}
	/**
	 * @param modificationDate the modificationDate to set
	 */
	public void setModificationDate(Timestamp modificationDate) {
		this.modificationDate = modificationDate;
	}
	/**
	 * @return the modifiyerID
	 */
	public int getModifiyerID() {
		return modifiyerID;
	}
	/**
	 * @param modifiyerID the modifiyerID to set
	 */
	public void setModifiyerID(int modifiyerID) {
		this.modifiyerID = modifiyerID;
	}
	/**
	 * @return the creatorID
	 */
	public int getCreatorID() {
		return creatorID;
	}
	/**
	 * @param creatorID the creatorID to set
	 */
	public void setCreatorID(int creatorID) {
		this.creatorID = creatorID;
	}

	/**
	 * Klassenname und ID des Objekts werden als String zurück gegeben
	 */
	public String toString() {
	
		return this.getClass().getName() + " #" + this.id;
	}
	
	/**
	 * zu programmieren 
	 */
	public boolean equals(Object o) {
		
	return false;
	}
	
	/**
	 * liefert einen eindeutigen Wert zur Identifikation des Objekts
	 */
	public int hashCode() {
		return this.id;
	}

}
