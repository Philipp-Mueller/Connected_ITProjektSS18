package de.hdm.Connected.server;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	/**Erzeugen einer neune Permission
	 * @param shareUserId user, der Teilhaberschaft anlegt
	 * @param shareObejctId Objekte, auf das sich die Permission bezieht
	 * @param receiverUserId User, die die Permission erhalten*/
	@Override
	public void createPermission(int shareUserId, ArrayList<Integer> shareObjectId, ArrayList<Integer> receiverUserId)
			throws IllegalArgumentException {

		for (int i = 0; i < shareObjectId.size(); i++) {
			for (int j = 0; j < receiverUserId.size(); j++) {
				// doppelte Permissions vermeiden
				if (!(hasPermission(shareObjectId.get(i), receiverUserId.get(j)))) {
					Permission permission = new Permission();
					permission.setShareUserID(shareUserId);
					permission.setSharedObjectId(shareObjectId.get(i));
					permission.setReceiverUserID(receiverUserId.get(j));
					permissionMapper.insert(permission);
				}
			}
		}

	}

	// löscht eine Permission wenn User sie selbst erstellt hat
	/**Löschen einer Permission
	 * @param permission das zu löschende Permission Objekt**/
	@Override
	public void deletePermission(Permission permission) throws IllegalArgumentException {
		permissionMapper.delete(permission);

	}

	/**Update einer Permission
	 * @param permission das zu updatende Permission Objekt**/
	@Override
	public void updatePermission(Permission permission) throws IllegalArgumentException {
		permissionMapper.update(permission);

	}

	// Updaten der Permissions für einen User
	/**Aktuallisieren der Permissions auf Values für einen User
	 * @param newPermissions ArrayList mit Ids der Permissions
	 * @param contactId betroffene Kontakt Id
	 * @param userId betroffene User Id**/
	public void updatePermissionsForUser(ArrayList<Integer> newPermissions, int contactId, int userId)
			throws IllegalArgumentException {

		ArrayList<Integer> oldPermissions = new ArrayList<Integer>();
		ArrayList<Integer> receiverUser = new ArrayList<Integer>();
		ArrayList<Integer> createPermissions = new ArrayList<Integer>();

		receiverUser.add(userId);

		for (int i = 0; i < getValuesByReceiveUserPermission(contactId, userId).size(); i++) {
			oldPermissions.add(getValuesByReceiveUserPermission(contactId, userId).get(i).getBoId());
		}
		;

		for (int j = 0; j < oldPermissions.size(); j++) {
			if (!newPermissions.contains(Integer.valueOf(oldPermissions.get(j)))) {
				deletePermission(permissionMapper.findBySharedObjectIdAndReceiverId(oldPermissions.get(j), userId));
			}
		}

		for (int k = 0; k < newPermissions.size(); k++) {
			if (!hasPermission(newPermissions.get(k), userId)) {
				createPermissions.add(newPermissions.get(k));

			}
		}
		this.createPermission(2, createPermissions, receiverUser);

	}

	// gibt alle Permsission-Objekte aus der DB zurück
	/**Gibt alle Permissions zurück**/
	@Override
	public ArrayList<Permission> getAllPermissions() throws IllegalArgumentException {
		return this.permissionMapper.findAll();
	}

	/** Gibt alle Permission-Objekte für einen User (Empfänger) zurück
	 * @param receiverUId Empangender User**/
	@Override
	public ArrayList<Permission> getPermissionsByRecieveUserId(int recieveUId) throws IllegalArgumentException {
		return this.permissionMapper.findByContactId(recieveUId);
	}

	/** Gibt Permissions für User auf Kontakt-Objekte zurück*
	 *@param userId User*/

	public ArrayList<Contact> getContactsByUserPermission(int userId) throws IllegalArgumentException {
		ArrayList<Contact> allContacts = new ArrayList<Contact>();

		for (int i = 0; i < contactMapper.findAll().size(); i++) {
			if (hasPermission(contactMapper.findAll().get(i).getBoId(), userId)) {
				allContacts.add(contactMapper.findAll().get(i));
			}
		}

		for (int j = 0; j < contactMapper.findByOwnerId(userId).size(); j++) {
			allContacts.add(contactMapper.findByOwnerId(userId).get(j));
		}

		return allContacts;

	}

	/**Gibt alle Kontaktlisten zurück auf denen der User Zugriff hat
	 * @param userId**/
	public ArrayList<ContactList> getContactListsByUserPermission(int userId) throws IllegalArgumentException {
		ArrayList<ContactList> allContactLists = new ArrayList<ContactList>();

		for (int i = 0; i < contactListMapper.findAllContactLists().size(); i++) {
			if (hasPermission(contactListMapper.findAllContactLists().get(i).getBoId(), userId)) {
				allContactLists.add(contactListMapper.findAllContactLists().get(i));
			}
		}

		for (int j = 0; j < contactListMapper.findByOwnerId(userId).size(); j++) {
			allContactLists.add(contactListMapper.findByOwnerId(userId).get(j));
		}

		return allContactLists;

	}

	/**Gibt Values zurück auf welche der User Zugriff hat
	 * @param contactId der betroffene Kontakt
	 * @param userId der betroffene User**/
	public ArrayList<Value> getValuesByUserPermission(int contactId, int userId) throws IllegalArgumentException {
		ArrayList<Value> allValues = new ArrayList<Value>();

		for (int i = 0; i < valueMapper.findByContactId(contactId).size(); i++) {
			if (valueMapper.findByContactId(contactId).get(i).getCreatorId() == userId) {

				allValues.add(valueMapper.findByContactId(contactId).get(i));

			} else if (hasPermission(valueMapper.findByContactId(contactId).get(i).getBoId(), userId)) {

				allValues.add(valueMapper.findByContactId(contactId).get(i));
			}

		}

		return allValues;

	}

	/**Gibt Vaulues zurück, die Reeiver User geteilt bekommen hab
	 * @param contactId betroffener KOntakt
	 * @param userid User**/
	public ArrayList<Value> getValuesByReceiveUserPermission(int contactId, int userId)
			throws IllegalArgumentException {
		ArrayList<Value> allValues = new ArrayList<Value>();

		for (int i = 0; i < valueMapper.findByContactId(contactId).size(); i++) {
			if (hasPermission(valueMapper.findByContactId(contactId).get(i).getBoId(), userId)) {

				allValues.add(valueMapper.findByContactId(contactId).get(i));
			}
		}

		return allValues;
	}

	/**Prüft ob User Permission auf ShareObject besitzt
	 * @param shareObjectId Id des Objekts
	 * @param receiverUserId Id des Users**/
	public boolean hasPermission(int shareObjectId, int receiverUserId) throws IllegalArgumentException {
		if (permissionMapper.hasPermission(shareObjectId, receiverUserId)) {
			return true;
		}
		return false;
	}

	/** Gibt alle Permission-Objekte für einen User (Teilender) zurück
	 * @param sharUId id des teilenden User**/
	@Override
	public ArrayList<Permission> getPermissionsByShareUserId(int shareUId) throws IllegalArgumentException {
		return this.permissionMapper.findByShareUserId(shareUId);
	}

	/** Gibt alle Permission-Objekte für ein geteiltes Objekt zurück
	 * @param sharedOId share Object Id**/
	@Override
	public ArrayList<Permission> getPermissionsBySharedObjectId(int sharedOId) throws IllegalArgumentException {
		return this.permissionMapper.findBySharedObjectId(sharedOId);
	}

	/** Gibt alle Permission-Objekte für eine Eigenschaft zurück
	 * @param valueId **/
	@Override
	public ArrayList<Permission> getPermissionsByValueId(int valueId) throws IllegalArgumentException {
		return this.permissionMapper.findByContactId(valueId);
	}

	/** Gibt eine Permission-Objekt anhand seiner Id zurück
	 * @param id**/
	@Override
	public Permission getPermissionById(int id) throws IllegalArgumentException {
		return this.permissionMapper.findById(id);
	}

	// **** CONTACT****

	// erstellt Contact und fügt eine Berechtigung für User hinzu, der Contact
	// erstellt hat

	/**Erstellen eines neuen Kontakts
	 * @param prename
	 * @param surname
	 * @param creationDate
	 * @param modificationDate
	 * @param ownerId**/
	public Contact createContact(String prename, String surname, Date creationDate, Date modificationDate,
			int ownerId) {
		Contact contact = new Contact();

		contact.setPrename(prename);
		contact.setSurname(surname);
		contact.setCreatorId(ownerId);
		contact.setCreationDate(creationDate);
		contact.setModificationDate(modificationDate);

		return this.contactMapper.insert(contact);

	}

	/**Aktuallisieren von Kontakt
	 * @param contact Kontakt**/
	@Override
	public Contact updateContact(Contact contact) throws IllegalArgumentException {
		// if (contact.getBoId()==permissionMapper.findById(userId).getBoId())
		// --> quatsch!
		return this.contactMapper.update(contact);
	}

	// löscht Kontakt mit Values
	// Editor-GUI dar nur Kontakte anzeigen mit find by Shared User und find by
	// RecieverUser anzeigen
	// Permission bzw kann nur die selbst erstellten Kontakte löschen sonst -->
	// eigene Permission löschen

	/**Löschen eines Kontakts und zugehörigen Values
	 * @param contact
	 * @param cUser**/
	@Override
	public void deleteContact(Contact contact, User cUser) throws IllegalArgumentException {

		if (contact.getCreatorId() == cUser.getBoId()) {

			ArrayList<Value> values = this.findValuesByContactId(contact.getBoId());
			ArrayList<Permission> permission = new ArrayList<Permission>();
			permission.addAll(permissionMapper.findBySharedObjectId(contact.getBoId()));

			// Values Permission und Value selbst löschen
			if (values != null) {
				for (Value value : values) {
					for (Permission p : this.permissionMapper.findBySharedObjectId(value.getBoId())) {
						this.permissionMapper.delete(p);
					}
					;
					this.valueMapper.delete(value);
				}
			}

			// Kontakt aus allen Kontaktlisten entfernen.
			ccMapper.removeContactFromAllContactList(contact.getBoId());

			// Kontakt Permissions löschen und Kontakt selbst löschen
			for (Permission p : this.permissionMapper.findBySharedObjectId(contact.getBoId())) {
				this.permissionMapper.delete(p);
			}
			;

			this.contactMapper.delete(contact);
		}

		else {
			deletePermission(permissionMapper.findBySharedObjectIdAndReceiverId(contact.getBoId(), cUser.getBoId()));
			ArrayList<Value> vPermissionsToDelete = getValuesByReceiveUserPermission(contact.getBoId(),
					cUser.getBoId());
			for (Value v : vPermissionsToDelete) {
				deletePermission(permissionMapper.findBySharedObjectIdAndReceiverId(v.getBoId(), cUser.getBoId()));
			}
		}
	}

	/** gibt alle Contact Objekte zurück**/
	@Override
	public ArrayList<Contact> findAllContacts() throws IllegalArgumentException {
		return this.contactMapper.findAll();
	}

	/** gibt Contact Objekte mit übergebener UserID zurück (alle Objekte die ein
	// bestimmter User erstellt hat)
	 * @param id User Id**/
	@Override
	public ArrayList<Contact> findContactsByOwnerId(int id) throws IllegalArgumentException {
		return this.contactMapper.findByOwnerId(id);
	}

	/**Gibt Contact Objekte mit übergebenen Eigenschaftsausprägung zurück
	 * @param value Eigenschatsausprägung**/
	public ArrayList<Contact> findContactsByValue(String value) throws IllegalArgumentException {
		return this.contactMapper.findByValue(value);
	}

	/** Gibt Contact Objekte mit übergebenem Vornamen zurück
	 * @param prename Vorname **/

	public ArrayList<Contact> findContactsByPrename(String prename) throws IllegalArgumentException {
		return this.contactMapper.findByPrename(prename);
	}

	/** Gibt Contact Objekte mit übergebenem Nachnamen zurück
	 * @param surname Nachname **/

	public ArrayList<Contact> findContactsBySurname(String surname) throws IllegalArgumentException {
		return this.contactMapper.findBySurname(surname);
	}

	// *** ContactList ***

	/**Erstellt neue Kontaktliste und gibt dem Ersteller eine Permission
	 * @param name Name der Kontaktliste
	 * @param ownerId Ersteller*/

	@Override
	public ContactList createContactList(String name, int ownerId) throws IllegalArgumentException {
		ContactList contactList = new ContactList();
		contactList.setName(name);
		contactList.setCreatorId(ownerId);

		/*
		 * Permission autoPermission = new Permission();
		 * autoPermission.setReceiverUserID(ownerId);
		 * autoPermission.setSharedObjectId(contactList.getBoId());
		 * autoPermission.setShareUserID(ownerId);
		 */

		return this.contactListMapper.insert(contactList);
	}

	// updatet Kontaktliste

	/**Kontaktliste aktuallisieren**/
	@Override
	public ContactList updateContactList(ContactList contactList) throws IllegalArgumentException {
		return this.contactListMapper.update(contactList);
	}

	// fügt einer Kontaktliste einen Kontakt hinzu

	/**Fügt Kontakte zu Kontaktlisten hinzu und überprüft ob diese bereits in der Kontaktliste enthalten sind
	 * @param contactArray Array mit Kontakten
	 * @param contactlistArray Array mit Kontaktlisten**/
	@Override
	public void addContactsToContactList(ArrayList<Contact> contactArray, ArrayList<ContactList> contactlistArray)
			throws IllegalArgumentException {

		for (int i = 0; i < contactlistArray.size(); i++) {
			for (int j = 0; j < contactArray.size(); j++) {
				boolean flag = false;
				int idvonContact = contactArray.get(j).getBoId();
				int idvonCL = contactlistArray.get(i).getBoId();
				ArrayList<Contact> contactsinCL = ccMapper.findContactsByContactListId(idvonCL);

				for (int v = 0; v < contactsinCL.size(); v++) {
					if (contactsinCL.get(v).getBoId() == idvonContact) {
						flag = true;
					}

				}
				if (flag == false) {
					ccMapper.addContactToContactList(contactArray.get(j).getBoId(), contactlistArray.get(i).getBoId());
				}
			}
		}
	}


	/**Löscht Kontakte aus einer Kontaktliste**/
	@Override
	public void removeContactFromContactList(int contactid, int contactlistid) throws IllegalArgumentException {
		ccMapper.removeContactFromContactList(contactid, contactlistid);
	}

	/**Gibt einem Array von Usern Permissions auf ein Array von Kontakten**/

	@Override
	public void giveContactPermissonToUsers(ArrayList<Contact> contactArray, ArrayList<User> userArray, int shareuserid)
			throws IllegalArgumentException {

		for (int i = 0; i < contactArray.size(); i++) {
			for (int j = 0; j < userArray.size(); j++) {
				Permission p = new Permission();
				p.setSharedObjectId(contactArray.get(i).getBoId());
				p.setReceiverUserID(userArray.get(j).getBoId());
				p.setShareUserID(shareuserid);
				permissionMapper.insert(p);
			}
		}
	}
	
	public void giveContactlistPermissionToUsers(ContactList contactlist, ArrayList<User> userArray, int shareuserid)throws IllegalArgumentException{
		for( int i = 0; i< userArray.size(); i++){
			//Erstellen der Permission für die Kontaktliste
			Permission p = new Permission();
			p.setSharedObjectId(contactlist.getBoId());
			p.setReceiverUserID(userArray.get(i).getBoId());
			p.setShareUserID(shareuserid);
			permissionMapper.insert(p);
			
			//Receiver User der Permission
			User u = userArray.get(i);
			ArrayList<User> singleUser = new ArrayList<User>();
			singleUser.add(u);
			
			//Erstellen der Permissions für alle Contacts der Liste
			ArrayList<Contact> shareContacts = findContactsByContactListId(contactlist.getBoId());
			this.giveContactPermissonToUsers(shareContacts, singleUser, shareuserid);
			
			//Erstellen von Permissions für alle Values der Kontakte 
			for(int m = 0; m<shareContacts.size(); m++){
			ArrayList<Value> shareValues = this.findValuesByContactId(shareContacts.get(m).getBoId());
			for(Value v : shareValues){
				givePermissionToUsers(v.getBoId(), singleUser, shareuserid);
			}
			}
		}
	}

	/** Anlegen von Permission auf Shareobject für ein Array an User**/

	@Override
	public void givePermissionToUsers(int shareObjectId, ArrayList<User> userArray, int shareuserid)
			throws IllegalArgumentException {
		for (int i = 0; i < userArray.size(); i++) {
			Permission p = new Permission();
			p.setSharedObjectId(shareObjectId);
			p.setReceiverUserID(userArray.get(i).getBoId());
			p.setShareUserID(shareuserid);
			permissionMapper.insert(p);
		}

	}

	/** Löschen von Kontaktliste und dazugehörige Permissions 
	 * Wenn User nicht Owner der KOntaktliste dann wird nur die Permission und nicht die KOntaktliste gelöscht
	 * @param conactList
	 * @param cUser**/
@Override
	public void deleteContactList(ContactList contactList, int userid) throws IllegalArgumentException {


		//wenn user nicht owner dann permission löschen
		if (userid != contactList.getCreatorId()) {			
			permissionMapper.delete(permissionMapper.findBySharedObjectIdAndReceiverId(contactList.getBoId(), userid));
		}
		//wenn user owner kontaktliste löschen und kontakte vorher davon entfernen
		else {
			ArrayList<Contact> contacts = this.findContactsByContactListId(contactList.getBoId());
			if (contacts != null) {
				for (Contact contact : contacts) {
					this.ccMapper.removeContactFromContactList(contact.getBoId(), contactList.getBoId());
					
					}
				}
			contactListMapper.delete(contactList);
			}
		}
	

	/**gibt Kontakte einer Kontaktliste zurück
	 * @param contactlistId id der Kontaktliste**/
	@Override
	public ArrayList<Contact> findContactsByContactListId(int contactlistId) throws IllegalArgumentException {

		ArrayList<Contact> contactsInList = new ArrayList<Contact>();
		int length = ccMapper.findContactsByContactListId(contactlistId).size();
		for (int i = 0; i < length; i++) {
			int contactid = ccMapper.findContactsByContactListId(contactlistId).get(i).getBoId();
			contactsInList.add(findContactById(contactid));
		}

		return contactsInList;
	}

	/**gibt alle Kontaktlisten zurück**/
	@Override
	public ArrayList<ContactList> findAllContactlists() throws IllegalArgumentException {
		return this.contactListMapper.findAllContactLists();
	}

	// *** User ***
	/**erstellen eines neuen User
	 * @param info Informationen zum User (email,nickname)**/
	@Override
	public User createUser(LoginInfo info) throws IllegalArgumentException {
		User user = new User();
		user.setLogEmail(info.getEmailAddress());
		user.setName(info.getNickname());

		return this.userMapper.insert(user);
	}

	/**Aktuallisieren des User
	 * @param user**/
	@Override
	public void updateUser(User user) throws IllegalArgumentException {
		userMapper.update(user);
	}

	/**löschen von User
	 * @param user**/
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

	/**gibt Permissions für eine UserId zurück
	 * @param userId**/
	@Override
	public ArrayList<Permission> findPermissionsByUserId(int userId) throws IllegalArgumentException {
		return this.permissionMapper.findByUserId(userId);
	}

	/**gibt alle User zurück**/
	public ArrayList<User> findAllUser() throws IllegalArgumentException {
		return this.userMapper.findAll();
	}

	/**gibt User-Objekt für User id zurück**/
	@Override
	public User findUserById(int userId) throws IllegalArgumentException {
		return this.userMapper.findById(userId);
	}

	/**gibt UserObjekt für email zurück**/
	@Override
	public User findUserByEmail(String email) throws IllegalArgumentException {
		return this.userMapper.findByEmail(email);
	}

	// *** Value ***
	/**Erstellen einer Neuen Eigenschaftsausrägung
	 * @param propertyId dazugehörige Eigenschaft
	 * @param contactId dazugehöriger Kontakt
	 * @param ownerId ersteller der Ausprägung**/
	@Override
	public Value createValue(String name, int propertyId, int contactId, int ownerId) throws IllegalArgumentException {
		Value value = new Value();
		value.setName(name);
		value.setPropertyID(propertyId);
		value.setContactID(contactId);
		value.setCreatorId(ownerId);

		// modifizierungsdatum aktualisieren
		this.contactMapper.updateContactModificationDate(contactId);

		/*
		 * Permission autoPermission = new Permission();
		 * autoPermission.setReceiverUserID(ownerId);
		 * autoPermission.setSharedObjectId(value.getBoId());
		 * autoPermission.setShareUserID(ownerId);
		 */

		return this.valueMapper.insert(value);
	}

	/**Aktuallisieren einer Ausprägung
	 * @param value Ausprägung
	 * @param oldPropertyId id zum checken ob Property noch values hat**/
	@Override
	public Value updateValue(Value value, int oldPropertyId) throws IllegalArgumentException {
		
		this.contactMapper.updateContactModificationDate(value.getContactID());
		checkIfPropertyHasValue(oldPropertyId);	
		
		return this.valueMapper.update(value);
	}

	/**löschen einer Value**/
	@Override
	public void deleteValue(Value value) throws IllegalArgumentException {
		this.valueMapper.delete(value);
		checkIfPropertyHasValue(value.getPropertyID());		
	}

	/**gibt Value für eine String suche zurück**/
	@Override
	public ArrayList<Value> findValueByValue(String value) throws IllegalArgumentException {
		return this.valueMapper.findByValue(value);
	}

	/**gibt value für eine Id zurück**/
	@Override
	public Value findValueById(int id) throws IllegalArgumentException {
		return this.valueMapper.findById(id);
	}

	// *** Property ***
	/**Erstellen einer neuen Eigenschaft**/
	@Override
	public Property createProperty(String name) throws IllegalArgumentException {
		Property property = new Property();
		property.setName(name);
		property.setName(name);

		return this.propertyMapper.insert(property);
	}

	/**Aktuallisieren einer Eigenschaft**/
	@Override
	public void updateProperty(Property property) throws IllegalArgumentException {
		propertyMapper.update(property);
	}

	/**Löschen einer Eigenschaft**/
	@Override
	public void deleteProperty(Property property) throws IllegalArgumentException {
		// TODO wollen wir properties löschen? Wenn ja: Lösche alle Values mit
		// der Property
		propertyMapper.delete(property);

	}

	/**
	 * Methode zur Überprüfung ob eine Eigenschaft noch Values besitzt wird
	 * nicht nicht bei vorgegebenen Eigenschaften angewandt
	 */

	public void checkIfPropertyHasValue(int propertyId) throws IllegalArgumentException {

		if (propertyId >= 11) {
			if (this.valueMapper.findByProperty(propertyId).size() == 0) {
				this.propertyMapper.delete(propertyMapper.findById(propertyId));
			}

		}
	}
	
	/**
	 * Methode zur Überprüfung ob eine Eigenschaft mit einem Namen schon gibt
	 */
	
	public Property findPropertyByName(String name) throws IllegalArgumentException {
		return this.propertyMapper.findByName(name);
	}



	// Für welchen Fall brauchen wir diese Methode? Reicht nicht Owner(User),
	// Value, All?
	@Override
	public Contact findContactById(int id) throws IllegalArgumentException {
		return this.contactMapper.findById(id);
	}

	/**Gibt alle Values eines Kontakts zurück**/
	@Override
	public ArrayList<Value> findValuesByContactId(int id) throws IllegalArgumentException {

		return this.valueMapper.findByContactId(id);
	}

	/**Gibt eine Map mit Values und dazugehöriger Property zurück, auf die ein User die Berechtigung hat**/
	@Override
	public Map<Property, Value> findValueAndProperty(int contactId, int userId) throws IllegalArgumentException {
		Map<Property, Value> mapi = new HashMap<Property, Value>();

		for (int i = 0; i < getValuesByUserPermission(contactId, userId).size(); i++) {
			Property property = findPropertyByPropertyId(
					getValuesByUserPermission(contactId, userId).get(i).getPropertyID());
			Value value = getValuesByUserPermission(contactId, userId).get(i);
			mapi.put(property, value);
		}

		return mapi;

	}

	/**Gibt eine Eigenschaft für eine bestimmte Id zurück**/
	@Override
	public Property findPropertyByPropertyId(int id) throws IllegalArgumentException {
		return this.propertyMapper.findById(id);
	}

	@Override // Delete Permission Redundant?! --ähm ja - würde das in
				// Permission schreiben (deletePermission Methode) Wieder die
				// Frage nach Objekte oder ID´s übergeben ;) Grüssle Denise
	public void deletePermissionFromContact(int userId, int contactId) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		ArrayList<Permission> permissionToDelete = new ArrayList<Permission>();

		permissionToDelete.add(permissionMapper.findBySharedObjectIdAndReceiverId(contactId, userId));

		for (int i = 0; i < findValuesByContactId(contactId).size(); i++) {

			if (hasPermission(findValuesByContactId(contactId).get(i).getBoId(), userId)) {
				permissionToDelete.add(permissionMapper
						.findBySharedObjectIdAndReceiverId(findValuesByContactId(contactId).get(i).getBoId(), userId));
			}
		}

		for (int j = 0; j < permissionToDelete.size(); j++) {
			deletePermission(permissionToDelete.get(j));
		}

	}

	/**Gibt alle Eigenschaften zurück**/
	@Override
	public ArrayList<Property> findAllProperties() throws IllegalArgumentException {
		return this.propertyMapper.findAllProperties();
	}

	@Override
	public void deleteContactList(ContactList contactlist) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Contact> getContacts() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	//Report Generator Methoden
	
	@Override
	public List<Value> findAllValues(Integer propertyId) {
		return this.valueMapper.findByProperty(propertyId);
	}

	@Override
	public List<Value> findValuesByPropertyAndDescription(int propertyId, String valueDescription) {
		return this.valueMapper.findByPropertyAndDescription(propertyId, valueDescription);
	}




}
