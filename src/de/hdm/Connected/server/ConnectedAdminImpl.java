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
import de.hdm.Connected.shared.bo.Permission;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;
import de.pitchMen.shared.bo.Participation;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Implemetierungsklasse des Interface ConnectedAdmin. Sie enthält die
 * Applikationslogik, stellt die Zusammenhänge konstistent dar und ist zuständig
 * um einen geordneten Ablauf zu gewährleisten.
 * 
 * @author Denise
 *
 */
public class ConnectedAdminImpl extends RemoteServiceServlet {

	private static final long serialVersionUID = 1L;

	public ConnectedAdminImpl() throws IllegalArgumentException {
	}

	Timestamp ts = new Timestamp(System.currentTimeMillis());

	/*
	 * public void init() { this.contactListMapper =
	 * ContactListMapper.contactListMapper(); this.contactMapper =
	 * ContactMapper.contactMapper(); this.permissionMapper =
	 * PermissionMapper.permissionMapper(); this.propertyMapper =
	 * PropertyMapper.propertyMapper(); this.valueMapper =
	 * ValueMapper.valueMapper(); this.userMapper = UserMapper.userMapper(); };
	 */

	/**
	 * Referenzen auf die DatenbankMapper, die Objekte mit der Datenbank
	 * abgleicht.
	 */
	private ContactListMapper contactListMapper = null;
	private ContactMapper contactMapper = null;
	private PermissionMapper permissionMapper = null;
	private PropertyMapper propertyMapper = null;
	private UserMapper userMapper = null;
	private ValueMapper valueMapper = null;

	@Override
	public void init() throws IllegalArgumentException {

		/*
		 * Vollständiger Satz von Mappern mit deren Hilfe ConnectedAdminImpl mit
		 * der Datenbank kommunizieren kann.
		 */

		this.contactListMapper = ContactListMapper.contactListMapper();
		this.contactMapper = ContactMapper.contactMapper();
		this.permissionMapper = PermissionMapper.permissionMapper();
		this.propertyMapper = PropertyMapper.propertyMapper();
		this.userMapper = UserMapper.userMapper();
		this.valueMapper = ValueMapper.valueMapper();

	}

	// **** CONTACT****

	public Contact createContact(String prename, String surname, int ownerId) {
		Contact contact = new Contact();
		contact.setPrename(prename);
		contact.setSurname(surname);
		contact.setCreatorId(ownerId);

		return this.contactMapper.insert(contact);

	}

	public void updateContact(Contact contact) throws IllegalArgumentException {
		contactMapper.update(contact);
	}

	// löscht Kontakt wenn User entweder Owner ist oder Permission besitzt

	/*
	 * public void deleteContact(Contact contact, int userId, int permissionId,
	 * int boId) throws IllegalArgumentException {
	 * 
	 * ArrayList<Permission> permissions = this.permissionMapper
	 * .findByUserId(userId); Contact currentContact =
	 * this.contactMapper.findById(contact.getBoId());
	 * System.out.println("current contact objekt: " +
	 * currentContact.getPrename()); System.out.println("übergebene UserID: " +
	 * userId); try { // Wenn der User der das Notebookerstellt hat, es löschen
	 * möchte if (currentContact.getCreatorId()== userId) {
	 * System.out.println("userID = notebook.getUserId"); } // wenn es
	 * Permissions gibt if (permissionId !=0) {
	 * System.out.println("es gibt permissions"); for (Permission
	 * foundedPermission : permissions) { // lösche zuerst alle Permissions
	 * this.permissionMapper.delete(foundedPermission); }
	 * 
	 * if (contact != null) { this.contactMapper.delete(contact); } else {
	 * System.out.println("cotenance");} } } }
	 */

	public ArrayList<Contact> getContacts() throws IllegalArgumentException {
		return this.contactMapper.findAll();
	}

	public ArrayList<Contact> getContactByUser(int userId) throws IllegalArgumentException {
		return this.contactMapper.findByUserId(userId);
	}

	// *** ContactList ***

	public ContactList createContactList(String name) {
		ContactList contactList = new ContactList();

		return this.contactListMapper.insert(contactList);
	}

	public void updateContactList(ContactList contactList) throws IllegalArgumentException {
		contactListMapper.update(contactList);
	}
	// fügt einer Kontaktliste einen Kontakt hinzu

	//@Denise bitte an UML orientieren oder siehe ConnectedAdmin Grüßle Mo
	public void addContact(Timestamp modificationDate, ContactList cl, int contactId, int userId)
			throws IllegalArgumentException {

		// if(cl.getPermissionId()==permissionId || userId==cl.getOwnerID()) {
		cl.setModificationDate(modificationDate);
		cl.setContactId(contactId);
		this.contactListMapper.update(cl);

	}

	// *** User ***
	public User createUser(String name) throws IllegalArgumentException{
		User user = new User();

		return this.userMapper.insert(user);
	}
	
	public void updateUser(User user) throws IllegalArgumentException {
		userMapper.update(user);
	}
	
	public void deleteUser(User user) throws IllegalArgumentException {
		ArrayList<Permission> permissions = this.findPermissionsByUserId(user.getBoId());
		
		if(permissions != null) {
			for (Permission permission : permissions)
			{
				this.permissionMapper.delete(permission);
			}
		}
		this.userMapper.delete(user);
	}

	public ArrayList<Permission> findPermissionsByUserId (int userId) throws IllegalArgumentException {
		return this.permissionMapper.findByUserId(userId);
	}

	// *** Permission ***
	public Permission createPermission(String name) throws IllegalArgumentException{
		Permission permission = new Permission();

		return this.permissionMapper.insert(permission);
	}
	
	public void updatePermission(Permission permission) throws IllegalArgumentException {
		permissionMapper.update(permission);
	}
	
	public void deletePermission(Permission permission) throws IllegalArgumentException{
		permissionMapper.delete(permission);
	}
	
	// *** Value ***
	public Value createValue(String name) throws IllegalArgumentException{
		Value value = new Value();

		return this.valueMapper.insert(value);
	}
	
	public void updateValue(Value value) throws IllegalArgumentException {
		valueMapper.update(value);
	}
	
	public void deleteValue(Value value) throws IllegalArgumentException{
		valueMapper.delete(value);
	}
	
	// *** Property ***
	public Property createProperty(String name) throws IllegalArgumentException{
		Property property = new Property();

		return this.propertyMapper.insert(property);
	}
	
	public void updateProperty(Property property) throws IllegalArgumentException {
		propertyMapper.update(property);
	}
	
}
