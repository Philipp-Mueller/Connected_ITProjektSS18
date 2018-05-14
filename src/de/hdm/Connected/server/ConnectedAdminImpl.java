package de.hdm.Connected.server;

import java.sql.Timestamp;
import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.Connected.server.db.ContactListMapper;
import de.hdm.Connected.server.db.ContactMapper;
import de.hdm.Connected.server.db.PermissionMapper;
import de.hdm.Connected.server.db.PropertyMapper;
import de.hdm.Connected.server.db.UserMapper;
import de.hdm.Connected.server.db.ValueMapper;
import de.hdm.Connected.shared.ConnectedAdmin;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.ContactList;
import de.hdm.Connected.shared.bo.Permission;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

/**
 * Implemetierungsklasse des Interface ConnectedAdmin. Sie enthält die
 * Applikationslogik, stellt die Zusammenhänge konstistent dar und ist zuständig
 * um einen geordneten Ablauf zu gewährleisten.
 * 
 * @author Denise
 *
 */
public class ConnectedAdminImpl extends RemoteServiceServlet implements ConnectedAdmin {

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
		// return this.contactMapper.findByUserId(userId);
		return null;
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

	// @Denise bitte an UML orientieren oder siehe ConnectedAdmin Grüßle Mo
	// AddContactToContactList heißt das glaub
	public void addContact(Timestamp modificationDate, ContactList cl, int contactId, int userId)
			throws IllegalArgumentException {

		// if(cl.getPermissionId()==permissionId || userId==cl.getOwnerID()) {
		cl.setModificationDate(modificationDate);
		cl.setContactId(contactId);
		this.contactListMapper.update(cl);

	}

	// *** User ***
	@Override
	public User createUser(String email) throws IllegalArgumentException {
		User user = new User();
		user.setLogEmail(email);

		return this.userMapper.insert(user);
	}

	@Override
	public void updateUser(User user) throws IllegalArgumentException {
		userMapper.update(user);
	}

	@Override
	public void deleteUser(User user) throws IllegalArgumentException {
		ArrayList<Permission> permissions = this.findPermissionsByUserId(user.getBoId());

		if (permissions != null) {
			for (Permission permission : permissions) {
				this.permissionMapper.delete(permission);
			}
		}
		this.userMapper.delete(user);
	}

	@Override
	public ArrayList<Permission> findPermissionsByUserId(int userId) throws IllegalArgumentException {
		return this.permissionMapper.findByUserId(userId);
	}

	// *** Permission ***
	@Override
	public Permission createPermission(int shareUserId, int shareObjectId, int receiverUserId)
			throws IllegalArgumentException {
		Permission permission = new Permission();
		permission.setShareUserID(shareUserId);
		permission.setSharedObjectId(shareObjectId);
		permission.setReceiverUserID(receiverUserId);

		return this.permissionMapper.insert(permission);
	}

	@Override
	public void deletePermission(Permission permission) throws IllegalArgumentException {
		permissionMapper.delete(permission);
	}

	// *** Value ***
	@Override
	public Value createValue(String name, int propertyId, int contactId) throws IllegalArgumentException {
		Value value = new Value();
		value.setName(name);
		value.setPropertyID(propertyId);
		value.setContactID(contactId);

		return this.valueMapper.insert(value);
	}

	@Override
	public void updateValue(Value value) throws IllegalArgumentException {
		valueMapper.update(value);
	}

	@Override
	public void deleteValue(Value value) throws IllegalArgumentException {
		valueMapper.delete(value);
	}

	// *** Property ***
	@Override
	public Property createProperty(String name) throws IllegalArgumentException {
		Property property = new Property();
		property.setName(name);
		property.setName(name);

		return this.propertyMapper.insert(property);
	}

	@Override
	public void updateProperty(Property property) throws IllegalArgumentException {
		propertyMapper.update(property);
	}

	@Override
	public void deleteProperty(Property property) throws IllegalArgumentException {
		propertyMapper.delete(property);

	}

	// Das sind deine! Wurden automatisch erstellt sonst hätte es einen Fehler
	// gegeben
	@Override
	public Contact createContact(String prename, String surname, Timestamp creationDate, Timestamp modificationDate,
			int ownerId) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteContact(Contact contact) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteContactList(ContactList contactlist) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}
	// Bis hier
	////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public ArrayList<Contact> findContactsByContactListId(int contactlistId) throws IllegalArgumentException {
		return this.contactMapper.findByContactListId(contactlistId);
	}

	@Override
	public ArrayList<Contact> findContactsByValue(String value) throws IllegalArgumentException {
		return this.contactMapper.findByValue(value);
	}

	@Override
	public Contact findContactById(int id) throws IllegalArgumentException {
		return this.contactMapper.findById(id);
	}

	@Override
	public ArrayList<Contact> findContactsByOwnerId(int id) throws IllegalArgumentException {
		return this.contactMapper.findByUserId(id);
	}

	@Override
	public ArrayList<Value> findValuesByContactId(int id) throws IllegalArgumentException {
		return this.valueMapper.findByContactId(id);
	}

	@Override
	public Property findPropertyByPropertyId(int id) throws IllegalArgumentException {
		return this.propertyMapper.findById(id);
	}

	@Override
	public void addContactToContactList(int contactId, int contactlistId) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeContactFromContactList(int contactId, int contactlistid) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override // Delete Permission Redundant?!
	public void removeAccessToObject(int userId, int shareObjectId) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Property> findAllProperties() throws IllegalArgumentException {
		return this.propertyMapper.findAllProperties();
	}

}
