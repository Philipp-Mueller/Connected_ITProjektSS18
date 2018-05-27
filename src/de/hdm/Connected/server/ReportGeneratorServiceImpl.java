package de.hdm.Connected.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.Connected.server.db.ContactMapper;
import de.hdm.Connected.server.db.PermissionMapper;
import de.hdm.Connected.server.db.PropertyMapper;
import de.hdm.Connected.server.db.UserMapper;
import de.hdm.Connected.server.db.ValueMapper;
import de.hdm.Connected.shared.ConnectedAdmin;
import de.hdm.Connected.shared.ReportGeneratorService;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Permission;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

/**
 * 
 * @author Patricia
 * Applikationslogik des Report Generators zur Wiedergabe aller Kontakte, aller Kontakte per Nutzer,
 * aller geteilten Kontakte, alle geteilte Kontakte per Nutzer, Abfrage von Eigenschaften anhand Aussprägungen.
 *
 */

public class ReportGeneratorServiceImpl extends RemoteServiceServlet implements ReportGeneratorService{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7457694965996952587L;
	private ConnectedAdmin adminImpl = null;
	private PermissionMapper permissionMapper = null;
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
		this.permissionMapper = PermissionMapper.permissionMapper();
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
	public List<Value> allValues() {
		return valueMapper.findAllValues();
	}


	@Override
	public List<Contact> searchContacts(boolean allContacts, boolean sharedContacts, String userEmail, Integer propertyId,
			String valueDescription) {

		List<Contact> result = new ArrayList<Contact>();
		
		//Wenn nicht nach allen Kontakten gesucht werden soll, und ein Nutzer gesetzt ist
		if(!allContacts&&userEmail!=null && !userEmail.isEmpty()){
			
			User u = userMapper.findByEmail(userEmail);
			
			//Jetzt müssen wir unterscheiden, ob shared contacts geladen werden sollen
			if(sharedContacts){
				result = allSharedContactsPerUser(u.getBoId());
			}else{
				result = allContactsPerUser(u.getBoId());
			}
		}else{
			result = allContacts();
			//TODO Auch alle Shared Contacts
		}
		
		//Jetzt noch die property value filter prüfen...
		if(propertyId!=null && valueDescription !=null && !valueDescription.isEmpty()){
			List<Contact> valuesAndProperties = contactsBasedOnPropertiesAndValues(propertyId, valueDescription);
			
			result. retainAll(valuesAndProperties);
		}
		return result;
		
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
