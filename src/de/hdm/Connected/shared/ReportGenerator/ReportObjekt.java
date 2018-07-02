package de.hdm.Connected.shared.ReportGenerator;

import java.io.Serializable;
import java.util.Map;

public class ReportObjekt implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6450501952458402470L;
	private String vorname;
	private String nachname;
	private Map<Integer, String> propertyValueMap;
	

	public ReportObjekt() {
	}

	public ReportObjekt(String vorname, String nachname, Map<Integer, String> propertyValueMap) {
		super();
		this.vorname = vorname;
		this.nachname = nachname;
		this.propertyValueMap = propertyValueMap;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public Map<Integer, String> getPropertyValueMap() {
		return propertyValueMap;
	}

	public void setPropertyValueMap(Map<Integer, String> propertyValueMap) {
		this.propertyValueMap = propertyValueMap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nachname == null) ? 0 : nachname.hashCode());
		result = prime * result + ((propertyValueMap == null) ? 0 : propertyValueMap.hashCode());
		result = prime * result + ((vorname == null) ? 0 : vorname.hashCode());
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
		ReportObjekt other = (ReportObjekt) obj;
		if (nachname == null) {
			if (other.nachname != null)
				return false;
		} else if (!nachname.equals(other.nachname))
			return false;
		if (propertyValueMap == null) {
			if (other.propertyValueMap != null)
				return false;
		} else if (!propertyValueMap.equals(other.propertyValueMap))
			return false;
		if (vorname == null) {
			if (other.vorname != null)
				return false;
		} else if (!vorname.equals(other.vorname))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ReportObjekt [vorname=" + vorname + ", nachname=" + nachname + ", propertyValueMap=" + propertyValueMap
				+ "]";
	}

}
