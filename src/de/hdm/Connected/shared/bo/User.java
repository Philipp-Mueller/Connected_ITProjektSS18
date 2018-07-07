package de.hdm.Connected.shared.bo;

/**
 * 
 * @author Moritz
 *
 */
public class User extends BusinessObject{

	private static final long serialVersionUID = 1L;
	private String logEmail = "";
	private String name = "";
	
	
	/**
	 * @return the email
	 */
	public String getLogEmail() {
		return logEmail;
	}
	/**
	 * @param email the email to set
	 */
	public void setLogEmail(String email) {
		this.logEmail = email;
	}
	
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((logEmail == null) ? 0 : logEmail.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		User other = (User) obj;
		if (logEmail == null) {
			if (other.logEmail != null)
				return false;
		} else if (!logEmail.equals(other.logEmail))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	

	
}
