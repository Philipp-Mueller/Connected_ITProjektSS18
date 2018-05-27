/**
 * 
 */
package de.hdm.Connected.shared.bo;

import java.sql.Timestamp;

/**
 * Die Klasse soll die Gemeinsamkeiten von Contat, ContactList und Value zusammenfassen 
 * und eine Struktur vorgeben, da User und Permissions nicht teilbar sind.
 * 
 * @author Denise
 *
 */
public abstract class SharedObject extends BusinessObject{

	private static final long serialVersionUID = 1L;
	
	// Fremdschlüssel zu User um deren Bearbeiter-Beziehung darszustellen 
	private int modifiyerId = 0;
	
	// Fremdschlüssel zu User um deren Besitzer/Ersteller-Beziehung darszustellen
	private int creatorId = 0;
	
	// Realisierung des Modificationsdatums um Status propagieren zu können
	private Timestamp creationDate = new Timestamp(System.currentTimeMillis());
	
	/**
	 * AutoKonstruktor
	 */
	public SharedObject() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @return gibt die ID des letzen Bearbeiters zurück
	 */
	public int getModifiyerId() {
		return modifiyerId;
	}

	/**
	 * @param setzt ID des letzten Users als ModifierID
	 */
	public void setModifiyerId(int modifiyerId) {
		this.modifiyerId = modifiyerId;
	}



	/**
	 * @return gibt die ID des Ersteller-Users zurück
	 */
	public int getCreatorId() {
		return creatorId;
	}


	/**
	 * @param stzt die ID des Erstellers als OwnerID
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + creatorId;
		result = prime * result + modifiyerId;
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
		SharedObject other = (SharedObject) obj;
		if (creatorId != other.creatorId)
			return false;
		if (modifiyerId != other.modifiyerId)
			return false;
		return true;
	}
	

}
