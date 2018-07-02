package de.hdm.Connected.server.ReportGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.Connected.server.ConnectedAdminImpl;
import de.hdm.Connected.server.db.ContactMapper;
import de.hdm.Connected.server.db.PropertyMapper;
import de.hdm.Connected.server.db.UserMapper;
import de.hdm.Connected.server.db.ValueMapper;
import de.hdm.Connected.shared.ConnectedAdmin;
import de.hdm.Connected.shared.ReportGenerator.ReportGeneratorService;
import de.hdm.Connected.shared.ReportGenerator.ReportObjekt;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

/**
 * 
 * @author Patricia
 * Applikationslogik des Report Generators zur Wiedergabe aller Kontakte, aller Kontakte per Nutzer,
 * aller geteilten Kontakte, aller geteilten Kontakte per Nutzer, Abfrage von Eigenschaften anhand Aussprägungen.
 *
 */

public class ReportGeneratorServiceImpl extends RemoteServiceServlet implements ReportGeneratorService{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7457694965996952587L;
	private ConnectedAdmin adminImpl = null;
	private ValueMapper valueMapper = null;
	private ContactMapper contactMapper = null;
	private UserMapper userMapper = null;
	private PropertyMapper propertyMapper = null;
	
	/*
	 * Init ist eine Initialisierungsmethode, welche für jede Instanz der 
	 * ReportGeneratorServiceImpl gerufen werden muss.
	 */
	public void init() throws IllegalArgumentException {
		if (adminImpl == null){
			adminImpl = new ConnectedAdminImpl();
		}
		adminImpl.init();
		this.valueMapper = ValueMapper.valueMapper();
		this.contactMapper = ContactMapper.contactMapper();
		this.userMapper = UserMapper.userMapper();
		this.propertyMapper = PropertyMapper.propertyMapper();
	}
	

	@Override
	public List<User> allUsers() {
		return userMapper.findAll();
	}


	@Override
	public List<Property> allProperties() {
		return this.propertyMapper.findAllProperties();
	}


	@Override
	public List<Value> allValues(Integer propertyId) {
		return valueMapper.findByProperty(propertyId);
	}


	@Override
	public List<ReportObjekt> searchContacts(boolean allContacts, boolean sharedContacts, String userEmail, Map<Integer, String> propertyValueMap) {

		List<Contact> ergebnisKontakte = new ArrayList<Contact>();
		List<ReportObjekt> ergebnisReport = new ArrayList<ReportObjekt>();
		
		//Wenn nicht nach allen Kontakten gesucht werden soll und ein Nutzer gesetzt ist
		if(!allContacts&&userEmail!=null && !userEmail.isEmpty()){
			
			User u = userMapper.findByEmail(userEmail);
			
			//Jetzt müssen wir unterscheiden, ob shared contacts geladen werden sollen
			if(sharedContacts){
				ergebnisKontakte = allSharedContactsPerUser(u.getBoId());
			}else{
				ergebnisKontakte = allContactsPerUser(u.getBoId());
			}
		}else{
			ergebnisKontakte = allContacts();
			//TODO Auch alle Shared Contacts
		}
		
		//Jetzt noch die property value filter prüfen...
		
		//Wenn es property filter gibt dann noch filtern...
		if(!propertyValueMap.isEmpty()){

			//Hier holen wir uns alle Property Ids aus der HashMap
			Set<Integer> propertyIds = propertyValueMap.keySet();
			
			//Für jeden Key (PropertyId) aus der HashMap suchen wir die Kontakte welche genau 
			//die werte zu einer Property haben
			for(Integer propertyKey : propertyIds){
				List<Contact> valuesAndProperties = contactsBasedOnPropertiesAndValues(propertyKey, propertyValueMap.get(propertyKey));
				ergebnisKontakte.retainAll(valuesAndProperties);
			}
		}
		
		ArrayList<Property> allproperties = adminImpl.findAllProperties();
		
		//Für jeden ErgebnisKontakt die Eigenschaften aufbauen
		for(Contact c : ergebnisKontakte) {
			//Eigenschaften lesen
			List<Value> eigenschaften = valueMapper.findByContactId(c.getBoId());
			
			//Reportobjekt aufbauen
			Map<Integer,String> eigenschaftsMap = new HashMap<Integer,String>();
			
			//Für jede mögliche Eigenschaft prüfen ob der Nutzer dazu ein Wert hat
			for(Property p : allproperties) {
				eigenschaftsMap.put(p.getBoId(), findeWertZuEigenschaft(p.getBoId(),eigenschaften));
			}
			ReportObjekt ro = new ReportObjekt(c.getPrename(), c.getSurname(), eigenschaftsMap);
			ergebnisReport.add(ro);
			
			
			//Reportobjekt dem Ergebnis hinzufügen
		}
		
		
		
		return ergebnisReport;
		
	}
	
	
	private List<Contact> allContacts() {	
		return this.adminImpl.findAllContacts();
	}

	
	private List<Contact> allContactsPerUser(int userid) {
		return this.adminImpl.findContactsByOwnerId(userid);
	}
	

	private List<Contact> allSharedContacts(int id) throws IllegalArgumentException{
		return null; //TODO implementierung fehlt
	}

	private List<Contact> allSharedContactsPerUser(int userId) {
		return null; //TODO implementierung fehlt 		
	}

	
	
	private String findeWertZuEigenschaft(int id, List<Value> list) {
		for(Value v : list) {
			if(v.getBoId()==id) {
				return v.getName();
			}
		}
		return "";
	}
	
	
	
	private List<Contact> contactsBasedOnPropertiesAndValues(int propertyId, String valueDescription) {
	
		//Liefert Kontakte, die einen bestimmten Property / Value haben.
		
		List<Value> listOfValuesWithGivenProperty = valueMapper.findByPropertyAndDescription(propertyId, valueDescription);
		
		
		List<Contact> result = new ArrayList<Contact>();
		for(Value value : listOfValuesWithGivenProperty){
			result.add(contactMapper.findById(value.getContactID()));
		}
		return result;
		
	}


	
	

}
