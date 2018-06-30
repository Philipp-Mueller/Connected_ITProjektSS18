package de.hdm.Connected.server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.Connected.server.db.ContactListMapper;
import de.hdm.Connected.server.db.ContactMapper;
import de.hdm.Connected.client.LoginInfo;
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
		this.ccMapper = ContactContactListMapper.contactContactListMapper();
	}
	
	
	// *** Permission ***
	
	
	
	@Override
	public void createPermission(int shareUserId, ArrayList<Integer> shareObjectId, ArrayList<Integer> receiverUserId)
			throws IllegalArgumentException {
		
		for(int i =0; i<shareObjectId.size();i++){
			for(int j=0; j<receiverUserId.size();j++){
			Permission permission = new Permission();
			permission.setShareUserID(shareUserId);
			permission.setSharedObjectId(shareObjectId.get(i));
			permission.setReceiverUserID(receiverUserId.get(j));
			permissionMapper.insert(permission);
			}
		}
		
	}
	
	//löscht eine Permission wenn User sie selbst erstellt hat
	@Override
	public void deletePermission(Permission permission, User cUser) throws IllegalArgumentException {
			
		if (cUser.getBoId()==permissionMapper.findById(cUser.getBoId()).getShareUserID()){
				
						permissionMapper.delete(permission, cUser);						 }
																						
																				}				 	
	@Override
	public void updatePermission(Permission permission) throws IllegalArgumentException{
		permissionMapper.update(permission);
		
	}
	
	// gibt alle Permsission-Objekte aus der DB zurück
	@Override
	public ArrayList<Permission> getAllPermissions() throws IllegalArgumentException {
		return this.permissionMapper.findAll();
	} 
	
	// Gibt alle Permission-Objekte für einen User (Empfänger) zurück
	@Override
	public ArrayList<Permission> getPermissionsByRecieveUserId(int recieveUId) throws IllegalArgumentException {
		return this.permissionMapper.findByContactId(recieveUId);
	}
	
	//Gibt Permissions für User auf Kontakt-Objekte zurück
	
	public ArrayList<Contact> getContactsByUserPermission(int userId) throws IllegalArgumentException {
		ArrayList<Contact> allContacts = new ArrayList<Contact>();
		
		
		
		for(int i=0; i<contactMapper.findAll().size(); i++){
			if(hasPermission(contactMapper.findAll().get(i).getBoId(), userId)){
				allContacts.add(contactMapper.findAll().get(i));
			}
		}
		
		for(int j=0; j<contactMapper.findByOwnerId(userId).size(); j++){
			allContacts.add(contactMapper.findByOwnerId(userId).get(j));
		}
		
		return allContacts;
		
	}
	
	public boolean hasPermission(int shareObjectId, int receiverUserId) throws IllegalArgumentException{
		if(permissionMapper.hasPermission(shareObjectId, receiverUserId)) {
			return true;
		}
		return false;
	}
	
	// Gibt alle Permission-Objekte für einen User (Teilender) zurück
	@Override
	public ArrayList<Permission> getPermissionsByShareUserId(int shareUId) throws IllegalArgumentException {
		return this.permissionMapper.findByContactId(shareUId);
	}
	// Gibt alle Permission-Objekte für ein geteiltes Objekt zurück
	@Override
	public ArrayList<Permission> getPermissionsBySharedObjectId(int sharedOId) throws IllegalArgumentException {
		return this.permissionMapper.findByContactId(sharedOId);
	}
	
	// Gibt alle Permission-Objekte für eine Eigenschaft zurück
	@Override
	public ArrayList<Permission> getPermissionsByValueId(int valueId) throws IllegalArgumentException {
		return this.permissionMapper.findByContactId(valueId);
	}
	
	// Gibt eine Permission-Objekt anhand seiner Id zurück
	@Override
	public Permission getPermissionById(int id) throws IllegalArgumentException{
		return this.permissionMapper.findById(id);
	}
	
		

	// **** CONTACT****

	
	// erstellt Contact und fügt eine Berechtigung für User hinzu, der Contact erstellt hat
	
	public Contact createContact(String prename, String surname, Timestamp creationDate, Timestamp modificationDate, int ownerId) {
		Contact contact = new Contact();
		//contact.setCreationDate(creationDate);
		//contact.setModificationDate(modificationDate);
		contact.setPrename(prename);
		contact.setSurname(surname);
		contact.setCreatorId(ownerId);
		
	/*	Permission autoPermission = new Permission();
		autoPermission.setReceiverUserID(ownerId);
		autoPermission.setSharedObjectId(contact.getBoId());
		autoPermission.setShareUserID(ownerId);
		this.permissionMapper.insert(autoPermission);*/
	
		return this.contactMapper.insert(contact);
		
	}
	
	//Updated Contact
	@Override
	public Contact updateContact(Contact contact) throws IllegalArgumentException {
		//if (contact.getBoId()==permissionMapper.findById(userId).getBoId()) --> quatsch!
		return this.contactMapper.update(contact);
	}
	
	// löscht Kontakt mit Values 
	// Editor-GUI dar nur Kontakte anzeigen mit find by Shared User und find by RecieverUser anzeigen
	//Permission bzw kann nur die selbst erstellten Kontakte löschen sonst --> eigene Permission löschen
	
	@Override
	public void deleteContact(Contact contact, User cUser)throws IllegalArgumentException {
		int sharedObjectId = permissionMapper.findById(permissionMapper.findById(cUser.getBoId()).getReceiverUserID()).getBoId();
							
			if (cUser.getBoId()==permissionMapper.findById(cUser.getBoId()).getShareUserID()
					&& permissionMapper.findById(contact.getBoId()).getBoId() == contact.getBoId()){
					
						Permission cPermission = permissionMapper.findById(sharedObjectId);
							permissionMapper.delete(cPermission, cUser);									}
																							
			else {
				ArrayList<Value> values = this.findValuesByContactId(contact.getBoId());
			
				if (values != null){
				for (Value value: values){
					this.valueMapper.delete(value);
										 }
							   		}
				
			this.contactMapper.delete(contact);
				 }
																						}
		
	// gibt alle Contact Objekte zurück
	@Override
	public ArrayList<Contact> findAllContacts() throws IllegalArgumentException{
		return this.contactMapper.findAll();
		}
	
	// gibt Contact Objekte mit übergebener UserID zurück (alle Objekte die ein bestimmter User erstellt hat)
	@Override
	public ArrayList<Contact> findContactsByOwnerId(int id) throws IllegalArgumentException {
		return this.contactMapper.findByOwnerId(id);
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
	// und fügt eine Berechtigung für User hinzu, der ContactList erstellt hat
	
	@Override
	public ContactList createContactList(String name, int ownerId){
		ContactList contactList = new ContactList();
		contactList.setName(name);
		contactList.setCreatorId(ownerId);
				
			/*Permission autoPermission = new Permission();
			autoPermission.setReceiverUserID(ownerId);
			autoPermission.setSharedObjectId(contactList.getBoId());
			autoPermission.setShareUserID(ownerId);*/
	
				return this.contactListMapper.insert(contactList);
	}
	
	// updatet Kontaktliste
	
	@Override
	public void updateContactList(ContactList contactList) throws IllegalArgumentException {
		contactListMapper.update(contactList);
	}
	
	// fügt einer Kontaktliste einen Kontakt hinzu
	
	@Override
	public void addContactsToContactList(ArrayList<Contact> contactArray, ArrayList<ContactList> contactlistArray) throws IllegalArgumentException {
		
		for(int i=0; i<contactArray.size(); i++){
			for( int j=0; j<contactlistArray.size(); j++){
				ccMapper.addContactToContactList(contactArray.get(i).getBoId(), contactlistArray.get(j).getBoId());
			}
		}
		
	}
		
	//Löscht einen Kontakt von einer Kontaktliste
	
	@Override
	public void removeContactFromContactList(int contactid, int contactlistid) throws IllegalArgumentException {
			ccMapper.removeContactFromContactList(contactid, contactlistid);
	}
	
	//Anlegen von Permissions auf Array von Contacts für Array von User TODO @MoritzBittner --bitte verständlicher formulieren
	
	@Override
	public void giveContactPermissonToUsers(ArrayList<Contact> contactArray, ArrayList<User> userArray, int shareuserid) throws IllegalArgumentException{
		
		for(int i=0; i < contactArray.size(); i++){
			for(int j=0; j< userArray.size(); j++){
				Permission p = new Permission();
				p.setSharedObjectId(contactArray.get(i).getBoId());
				p.setReceiverUserID(userArray.get(j).getBoId());
				p.setShareUserID(shareuserid);
				permissionMapper.insert(p);
			}
		}
	}
	
	//Anlegen von Permission auf ContactList für Array an User
	
	@Override
	public void givePermissionToUsers(int shareObjectId, ArrayList<User> userArray, int shareuserid) throws IllegalArgumentException
	{
		for(int i = 0; i<userArray.size(); i++){
			Permission p = new Permission();
			p.setSharedObjectId(shareObjectId);
			p.setReceiverUserID(userArray.get(i).getBoId());
			p.setShareUserID(shareuserid);
			permissionMapper.insert(p);
		}
		
	}
	
	// Löscht KontaktListen und zugehörige Kontakte 
	@Override
   	public void deleteContactList(ContactList contactlist) throws IllegalArgumentException {

		ArrayList<Contact> contactArray = new ArrayList<Contact>();
		contactArray = ccMapper.findContactsByContactListId(contactlist.getBoId());
		
		for( int i = 0; i <contactArray.size(); i++)
		{
			ccMapper.removeContactFromContactList(contactArray.get(i).getBoId(), contactlist.getBoId());
		}		
		
			this.contactListMapper.delete(contactlist);
		}
		
	public void deleteContactList(ContactList contactList, User cUser)throws IllegalArgumentException {
		int sharedObjectId = permissionMapper.findById(permissionMapper.findById(cUser.getBoId()).getReceiverUserID()).getBoId();
							
			if (cUser.getBoId()==permissionMapper.findById(cUser.getBoId()).getShareUserID()
					&& permissionMapper.findById(contactList.getBoId()).getBoId() == contactList.getBoId()){
					
						Permission cPermission = permissionMapper.findById(sharedObjectId);
							permissionMapper.delete(cPermission,cUser);
							}
											
			else {
				ArrayList<Contact> contacts = this.findContactsByOwnerId(cUser.getBoId());
				if (contacts != null){
					for (Contact contact: contacts){
						this.ccMapper.removeContactFromContactList(contact.getBoId(), contactList.getBoId());
						this.contactMapper.delete(contact);
																
				ArrayList<Value> values = this.findValuesByContactId(contact.getBoId());
			
					if (values != null){
						for (Value value: values){
							this.valueMapper.delete(value);
												  }
							   		   }
				 					   }
				}}
																									}	
	@Override
	public ArrayList<Contact> findContactsByContactListId(int contactlistId) throws IllegalArgumentException {	
		
		ArrayList<Contact> contactsInList = new ArrayList<Contact>();
		int length = ccMapper.findContactsByContactListId(contactlistId).size();
		for(int i =0; i< length; i++){
			int contactid = ccMapper.findContactsByContactListId(contactlistId).get(i).getBoId();
			contactsInList.add(findContactById(contactid));					
		}
		
		return contactsInList;
	}


	@Override
	public ArrayList<ContactList> findAllContactlists() throws IllegalArgumentException {
		return this.contactListMapper.findAllContactLists();
	}

	// *** User ***
	@Override
	public User createUser(LoginInfo info) throws IllegalArgumentException {
		User user = new User();
		user.setLogEmail(info.getEmailAddress());
		user.setName(info.getNickname());

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
				this.permissionMapper.delete(permission, user);
			}
		}
		this.userMapper.delete(user);
	}

	@Override
	public ArrayList<Permission> findPermissionsByUserId(int userId) throws IllegalArgumentException {
		return this.permissionMapper.findByUserId(userId);
	}
	
	public ArrayList<User> findAllUser() throws IllegalArgumentException {
		return this.userMapper.findAll();
	}
	@Override
	public User findUserById(int userId) throws IllegalArgumentException{
		return this.userMapper.findById(userId);
	}
	
	@Override
	public User findUserByEmail(String email) throws IllegalArgumentException{
		return this.userMapper.findByEmail(email);
	}

	// *** Value ***
	@Override
	public Value createValue(String name, int propertyId, int contactId, int ownerId) throws IllegalArgumentException {
		Value value = new Value();
		value.setName(name);
		value.setPropertyID(propertyId);
		value.setContactID(contactId);
		value.setCreatorId(ownerId);
		
		//modifizierungsdatum aktualisieren
		this.contactMapper.updateContactModificationDate(contactId);
		
		/*	Permission autoPermission = new Permission();
			autoPermission.setReceiverUserID(ownerId);
			autoPermission.setSharedObjectId(value.getBoId());
			autoPermission.setShareUserID(ownerId);*/

		return this.valueMapper.insert(value);
	}

	@Override
	public Value updateValue(Value value) throws IllegalArgumentException {
		this.contactMapper.updateContactModificationDate(value.getContactID());
		return this.valueMapper.update(value);
	}

	@Override
	public void deleteValue(Value value) throws IllegalArgumentException {
		valueMapper.delete(value);
	}
	
	@Override
	public ArrayList<Value> findValueByValue(String value) throws IllegalArgumentException {
		return this.valueMapper.findByValue(value);
	}
	
	@Override
	public Value findValueById(int id) throws IllegalArgumentException {
		return this.valueMapper.findById(id);
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
	public Map<Property, Value> findValueAndProperty(int id) throws IllegalArgumentException {
		Map<Property, Value> mapi = new HashMap<Property, Value>();
		for(int i=0; i<findValuesByContactId(id).size();i++){
			Property property = findPropertyByPropertyId(findValuesByContactId(id).get(i).getPropertyID());
			Value value = findValuesByContactId(id).get(i);			
			mapi.put(property, value);
		}
		
		
		return mapi;
		
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
