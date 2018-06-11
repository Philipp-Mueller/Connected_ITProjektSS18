package de.hdm.Connected.server;

import java.sql.Timestamp;
import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.Connected.server.db.ContactListMapper;
import de.hdm.Connected.server.db.ContactMapper;
import de.hdm.Connected.server.db.ContactContactListMapper;
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
	private ContactContactListMapper ccMapper = null;

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

	
	// erstellt Contact 
	public Contact createContact(String prename, String surname, Timestamp creationDate, Timestamp modificationDate, int ownerId) {
		Contact contact = new Contact();
		contact.setCreationDate(creationDate);
		contact.setModificationDate(modificationDate);
		contact.setPrename(prename);
		contact.setSurname(surname);
		contact.setCreatorId(ownerId);

		return this.contactMapper.insert(contact);

	}
	
	//Updated Contact
	@Override
	public void updateContact(Contact contact) throws IllegalArgumentException {
		contactMapper.update(contact);
	}
	
	// löscht Kontakt mit Values
	@Override
	public void deleteContact(Contact contact)throws IllegalArgumentException {
		// TODO Prüfung Permission bzw kann nur die selbst erstellten Kontakte löschen oder Fremdkontakte bei sich löschen --> eigene Permission löschen
		ArrayList<Value> values = this.findValuesByContactId(contact.getBoId());
		
		if (values != null){
			for (Value value: values){
				this.valueMapper.delete(value);
			}
		}
		this.contactMapper.delete(contact);
	}
	
	// gibt alle Contact Objekte zurück
	@Override
	public ArrayList<Contact> findAllContacts() throws IllegalArgumentException{
		return this.contactMapper.findAll();
		}
	
	// gibt Contact Objekte mit übergebener UserID zurück (alle Objekte die ein bestimmter User erstellt hat)
	@Override
	public ArrayList<Contact> findContactsByOwnerId(int id) throws IllegalArgumentException {
		return this.contactMapper.findByUserId(id);
	}
	
	//Gibt Contact Objekte mit übergebenen Eigenschaftsausprägung zurück
	public ArrayList<Contact> findContactsByValue(String value) throws IllegalArgumentException{
		return this.contactMapper.findByValue(value);
	}
	
	//Gibt Contact Objekte mit übergebenem Vornamen zurück
	
	public ArrayList<Contact> findContactsByPrename(String prename) throws IllegalArgumentException {
		return this.contactMapper.findByPrename(prename);
	}
	
	//Gibt Contact Objekte mit übergebenem Nachnamen zurück 
	
	public ArrayList<Contact> findContactsBySurname(String surname)throws IllegalArgumentException {
		return this.contactMapper.findBySurname(surname);
	}
	
	// *** ContactList ***
	
	
	//erstellt Kontaktliste
	@Override
	public ContactList createContactList(String name){
		ContactList contactList = new ContactList();
		contactList.setName(name);
		return this.contactListMapper.insert(contactList);
	}
	
	@Override
	public void updateContactList(ContactList contactList) throws IllegalArgumentException {
		contactListMapper.update(contactList);
	}
	
		// fügt einer Kontaktliste einen Kontakt hinzu
	
	@Override
	public void addContactToContactList(int contactid, int contactlistid) throws IllegalArgumentException {
		ccMapper.addContactToContactList(contactlistid, contactid);
	}
		
	@Override
	public void removeContactFromContactList(int contactid, int contactlistid) throws IllegalArgumentException {
		ccMapper.removeContactFromContactList(contactlistid, contactid);
	}
	
	//Löscht Kontaktliste
	// TODO Prüfung falls UserID ungleich CreatorID ist - Lösche Permission für alle Kontakte aus Kontaktliste 
	
	@Override
   	public void deleteContactList(ContactList contactlist) throws IllegalArgumentException {
			this.contactListMapper.delete(contactlist);
		}
		
		

	@Override
	public ArrayList<Contact> findContactsByContactListId(int contactlistId) throws IllegalArgumentException {	
		return this.contactMapper.findByContactListId(contactlistId);
	}


	@Override
	public ArrayList<ContactList> findAllContactlists() throws IllegalArgumentException {
		return this.contactListMapper.findAllContactLists();
	}

	/*
	public ContactList findContactListById(int id) throws IllegalArgumentException {
		return this.contactListMapper.findById(id);
	*/
	

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
		//TODO wollen wir properties löschen? Wenn ja: Lösche alle Values mit der Property
		propertyMapper.delete(property);

	}

	// Das sind deine! Wurden automatisch erstellt sonst hätte es einen Fehler
	// gegeben
	/*@Override
	public Contact createContact(String prename, String surname, Timestamp creationDate, Timestamp modificationDate,
			int ownerId) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
		}
	*/


	

	// Bis hier
	////////////////////////////////////////////////////////////////////////


	/*@Override
	public ArrayList<Contact> findContactsByValue(String value) throws IllegalArgumentException {
		return this.contactMapper.findByValue(value);
	}*/

	//Für welchen Fall brauchen wir diese Methode? Reicht nicht  Owner(User), Value, All?
	@Override
	public Contact findContactById(int id) throws IllegalArgumentException {
		return this.contactMapper.findById(id);
	}



	@Override
	public ArrayList<Value> findValuesByContactId(int id) throws IllegalArgumentException {
		return this.valueMapper.findByContactId(id);
	}

	@Override
	public Property findPropertyByPropertyId(int id) throws IllegalArgumentException {
		return this.propertyMapper.findById(id);
	}

	/*@Override
	public void addContactToContactList(int contactId, int contactlistId) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}*/



	@Override // Delete Permission Redundant?! --ähm ja - würde das in Permission schreiben (deletePermission Methode) Wieder die Frage nach Objekte oder ID´s übergeben ;) Grüssle Denise 
	public void removeAccessToObject(int userId, int shareObjectId) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Property> findAllProperties() throws IllegalArgumentException {
		return this.propertyMapper.findAllProperties();
	}
/*
	@Override
	public ArrayList<Contact> getContacts() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void deleteContactList(ContactList contactlist) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	// TODO @Patricia bitte wieder löschen - Methode existiert bereits
	/* @Override
	public void removeContactFromContactList(Contact contact, ContactList contactlist) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	} */

	@Override
	public ArrayList<Contact> getContacts() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*@Override
	public ArrayList<Contact> findAllContacts() throws IllegalArgumentException{
		return this.contactMapper.findAll();
	}*/

	/*@Override
	public void deleteContactList(ContactList contactlist) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Contact> findContactsByContactListId(int contactlistId) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ContactList> findAllContactlists() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
*/
	//public ArrayList<ContactList> findAllContactlists() throws IllegalArgumentException{
		//return this.contactListMapper.findAll();
	//}
}
