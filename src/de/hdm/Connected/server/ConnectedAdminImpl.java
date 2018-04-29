package de.hdm.Connected.server;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import de.hdm.Connected.server.db.ContactListMapper;
import de.hdm.Connected.server.db.ContactMapper;
import de.hdm.Connected.server.db.PermissionMapper;
import de.hdm.Connected.server.db.PropertyMapper;
import de.hdm.Connected.server.db.UserMapper;
import de.hdm.Connected.server.db.ValueMapper;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.ContactList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * Implemetierungsklasse des Interface ConnectedAdmin. Sie enthält die
 * Applikationslogik, stellt die Zusammenhänge konstistent dar und ist
 * zuständig um einen geordneten Ablauf zu gewährleisten.
 * 
 * @author Denise
 *
 */
public class ConnectedAdminImpl extends RemoteServiceServlet {
	
	private static final long serialVersionUID = 1L;

	public ConnectedAdminImpl() throws IllegalArgumentException {
	}
	
	Timestamp ts = new Timestamp(System.currentTimeMillis());

/*	public void init() {
		this.contactListMapper = ContactListMapper.contactListMapper();
		this.contactMapper = ContactMapper.contactMapper();
		this.permissionMapper = PermissionMapper.permissionMapper();
		this.propertyMapper = PropertyMapper.propertyMapper();
		this.valueMapper = ValueMapper.valueMapper();
		this.userMapper = UserMapper.userMapper(); 
		};*/


	/**
	 * Referenzen auf die DatenbankMapper, die Objekte mit der Datenbank
	 * abgleicht.
	 */	
	private ContactListMapper 	contactListMapper = null;
	private ContactMapper 	contactMapper = null;
	private PermissionMapper 	permissionMapper = null;
	private PropertyMapper	propertyMapper = null;
	private UserMapper	userMapper = null;
	private ValueMapper	valueMapper = null;
	
	@Override
	public void init() throws IllegalArgumentException {
	

		/*
		 * Vollständiger Satz von Mappern mit deren Hilfe ConnectedAdminImpl mit der Datenbank kommunizieren kann.
		 */

		this.contactListMapper = ContactListMapper.contactListMapper();
		this.contactMapper = ContactMapper.contactMapper();
		this.permissionMapper = PermissionMapper.permissionMapper();
		this.propertyMapper = PropertyMapper.propertyMapper();
		this.userMapper = UserMapper.userMapper();
		this.valueMapper = ValueMapper.valueMapper();


	}

	//			 **** CONTACT****
	
	public Contact createContact(String prename, String surname, int ownerId){
		Contact contact = new Contact();
		contact.setPrename(prename);
		contact.setSurname(surname);
		contact.setOwnerId(ownerId);

		return this.contactMapper.insert(contact);

	}

	
	public void updateContact(Contact contact) throws IllegalArgumentException {
		contactMapper.update(contact);
	}

	public void deleteContact(Contact contact, int userId, int permissionId) throws IllegalArgumentException {
	
		if (contact != null) {
			this.contactMapper.delete(contact);
		}

		this.contactMapper.delete(contact);
	}

	
	public ArrayList<Contact> getContacts() throws IllegalArgumentException {
		return this.contactMapper.findAll();
	}

	public ArrayList<Contact> getContactByUser(int userId) throws IllegalArgumentException {
		return this.contactMapper.findByUserId(userId);
	}

	// 			*** ContactList ***
	
	
	public ContactList createContactList(String name){
		ContactList contactList = new ContactList();

		return this.contactListMapper.insert(contactList);
	}
	
	public void updateContactList(ContactList contactList) throws IllegalArgumentException {
		contactListMapper.update(contactList);
	}

	// fügt einer Kontaktliste einen Kontakt hinzu
	
	public void addContact(Timestamp modificationDate, ContactList cl, int contactId, int userId, int permissionId) 
	throws IllegalArgumentException {
		
		if(cl.getPermissionId()==permissionId || userId==cl.getCreatorID())	{
			cl.setModificationDate(modificationDate);
			cl.setContactId(contactId);	
			this.contactListMapper.update(cl);
		}
	
	
		

	

	
	
}
		
		

		
		
	}

	
	

