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
	
	/**
	 * Realisierung der Beziehung zu einer Contactlist durch einen Fremdschlüssel.
	 */
	//@Moritz Musste ich anlegen für die Impl.
	
	private int contactListId= 0;
	
	private Timestamp creationDate = new Timestamp(System.currentTimeMillis());
	private Timestamp modificationDate= new Timestamp(System.currentTimeMillis());
	
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
	/**
	 * @return the contactListId
	 */
	public int getContactListId() {
		return contactListId;
	}
	/**
	 * @param contactListId the contactListId to set
	 */
	public void setContactListId(int contactListId) {
		this.contactListId = contactListId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + contactListId;
		result = prime * result + creatorId;
		result = prime * result + ((prename == null) ? 0 : prename.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (contactListId != other.contactListId)
			return false;
		if (creatorId != other.creatorId)
			return false;
		if (prename == null) {
			if (other.prename != null)
				return false;
		} else if (!prename.equals(other.prename))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		return true;
	}

	
	
	
	
}
