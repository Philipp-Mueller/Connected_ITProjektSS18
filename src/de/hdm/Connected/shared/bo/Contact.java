package de.hdm.Connected.shared.bo;

import java.sql.Timestamp;

/**
 * 
 * @author Moritz Realisierung eines Kontakts
 *
 */
public class Contact extends SharedObject{


	private static final long serialVersionUID = 1L;
	/**
	 * Der Default-Wert von Strings ist "null". Hier wird ein leerer
	 * String verwendet, um das Werfen einer NullPointException
	 * zu vermeiden.
	 */
	
	private String prename ="";
	private String surname ="";
	
	/**
	 * Realisierung der Beziehung zu einem User durch einen Fremdschlüssel.
	 */
	private int creatorId= 0;
	
	
	private Timestamp creationDate = new Timestamp(System.currentTimeMillis());
	private Timestamp modificationDate = new Timestamp(System.currentTimeMillis());
	
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
	 * @return the name
	 */
	public String getPrename() {
		return prename;
	}
	/**
	 * @param name the name to set
	 */
	public void setPrename(String prename) {
		this.prename = prename;
	}
	/**
	 * @return gibt creatur -User-Id zurück
	 */
	public int getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId wird gesetzt
	 */
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
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
	
}
