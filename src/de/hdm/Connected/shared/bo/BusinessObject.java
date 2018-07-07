package de.hdm.Connected.shared.bo;

import java.io.Serializable;

/**
 * 
 * @author Moritz
 *
 */

abstract class BusinessObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id = 0;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int boId) {
		this.id = boId;
	}
	
	/**
	 * Klassenname und ID des Objekts werden als String zurück gegeben
	 */
	public String toString() {
	
		return this.getClass().getName() + " #" + this.id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusinessObject other = (BusinessObject) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
