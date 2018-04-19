package de.hdm.Connected.shared.bo;

/**
 * 
 * @author Moritz
 *
 */
public class Property extends BusinessObject{

	private static final long serialVersionUID = 1L;
	private String name = "";
	
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
	

}
