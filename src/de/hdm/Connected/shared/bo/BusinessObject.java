package de.hdm.Connected.shared.bo;

import java.io.Serializable;

/**
 * 
 * @author Moritz
 *
 */

public class BusinessObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private int boId = 0;
	
	/**
	 * @return the id
	 */
	public int getBoId() {
		return boId;
	}
	/**
	 * @param id the id to set
	 */
	public void setBoId(int boId) {
		this.boId = boId;
	}
	
	/**
	 * Klassenname und ID des Objekts werden als String zurück gegeben
	 */
	public String toString() {
	
		return this.getClass().getName() + " #" + this.boId;
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
		return this.boId;
	}

}
