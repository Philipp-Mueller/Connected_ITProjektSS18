package de.hdm.Connected.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.Connected.server.db.ContactMapper;
import de.hdm.Connected.server.db.PermissionMapper;
import de.hdm.Connected.server.db.ValueMapper;
import de.hdm.Connected.shared.ConnectedAdmin;
import de.hdm.Connected.shared.ReportGeneratorService;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Permission;
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
	
	/*
	 * Init ist eine Initialisierungsmethode, welche für jede Instanz der 
	 * ReportGeneratorServiceImpl gerufen werden muss.
	 */
	public void init() throws IllegalArgumentException {
		if (adminImpl == null){
			adminImpl = new ConnectedAdminImpl();
		}
		this.permissionMapper = PermissionMapper.permissionMapper();
		this.valueMapper = ValueMapper.valueMapper();
		this.contactMapper = ContactMapper.contactMapper();
	}
	

	@Override
	public List<Contact> allContacts() {	
		return this.adminImpl.findAllContacts();
	}

	
	@Override
	public List<Contact> allContactsPerUser(int userid) {
		return this.adminImpl.findContactsByOwnerId(userid);
	}
	

	@Override
	public List<Permission> allSharedContacts(int id) throws IllegalArgumentException{
		return this.permissionMapper.findByContactId(id);
		
	}

	@Override
	public List<Permission> allSharedContactsPerUser(int userId) {
		return this.adminImpl.findPermissionsByUserId(userId);
		
		
	}

	@Override
	public List<Contact> contactsBasedOnPropertiesAndValues(int propertyId, String valueDescription) {
	
		//Liefert Kontakte, die einen bestimmten Property / Value haben.
		
		List<Value> listOfValuesWithGivenProperty = valueMapper.findByPropertyAndDescription(propertyId, valueDescription);
		
		
		List<Contact> result = new ArrayList<Contact>();
		for(Value value : listOfValuesWithGivenProperty){
			result.add(contactMapper.findById(value.getContactID()));
		}
		return result;
		
	}

}
