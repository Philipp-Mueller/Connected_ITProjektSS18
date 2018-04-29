package de.hdm.Connected.shared.bo;


/**
 * 
 * @author Moritz
 *
 */
public class Contact extends BusinessObject{


	private static final long serialVersionUID = 1L;
	private String prename ="";
	private String surname ="";
	private int ownerId= 0;
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
	 * @return the ownerId
	 */
	public int getOwnerId() {
		return ownerId;
	}
	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	
}
