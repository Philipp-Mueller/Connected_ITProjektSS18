package de.hdm.Connected.shared;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.Connected.client.LoginInfo;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.ContactList;
import de.hdm.Connected.shared.bo.Permission;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

/**
 * Schnittstelle für die RPC-Fähige Klasse ConnectedAdminImpl.
 * 
 * @author Denise, Moritz
 *
 */
@RemoteServiceRelativePath("connectedadmin")
public interface ConnectedAdmin extends RemoteService {

	/**
	 * Initialisierung des Objekts. Diese Methode ist vor dem Hintergrund von
	 * GWT RPC zusätzlich zum No Argument Constructor der implementierenden
	 * Klasse {@link ConnectedAdminImpl} notwendig. Bitte diese Methode direkt
	 * nach der Instantiierung aufrufen.
	 * 
	 * @throws IllegalArgumentException
	 */
	public void init() throws IllegalArgumentException;

	/**
	 * Erstellt ein neues User-Objekt.
	 * 
	 * @return neu erstelltes User-Objekt
	 * @throws IllegalArgumentException
	 */
	public User createUser(LoginInfo info) throws IllegalArgumentException;
	
	/**
	 * Aktuallisiert ein User-Objekt.
	 * 
	 * @return aktualisiertes User-Objekt
	 * @throws IllegalArgumentException
	 */
	public void updateUser(User user) throws IllegalArgumentException;

	/**
	 * Löscht ein User-Objekt und alle eventuell darauf basierenden
	 * Objekte.
	 * 
	 * @param user
	 * @throws IllegalArgumentException
	 */
	public void deleteUser(User user) throws IllegalArgumentException;
	
	/**
	 * Erstellt ein neues Property-Objekt.
	 * 
	 * @return neu erstelltes Property-Objekt
	 * @throws IllegalArgumentException
	 */
	
	public ArrayList<User> findAllUser() throws IllegalArgumentException;
	/**
	 * Gibt alle User zurück
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 */
	
	
	public Property createProperty(String name) throws IllegalArgumentException;
	
	/**
	 * Aktuallisiert ein Property-Objekt.
	 * 
	 * @return aktualisiertes Property-Objekt
	 * @throws IllegalArgumentException
	 */
	public void updateProperty(Property property) throws IllegalArgumentException;
	
	/**
	 * Löscht ein Proeprty-Objekt und alle eventuell darauf basierenden
	 * Objekte.
	 * 
	 * @param property
	 * @throws IllegalArgumentException
	 */
	public void deleteProperty(Property property) throws IllegalArgumentException;
	
	/**
	 * Erstellt ein neues Contact-Objekt.
	 * 
	 * @return neu erstelltes Contact-Objekt
	 * @throws IllegalArgumentException
	 */
	public Contact createContact(String prename, String surname, Timestamp creationDate, Timestamp modificationDate, int ownerId) throws IllegalArgumentException;
	
	/**
	 * Aktuallisiert ein Contact-Objekt.
	 * 
	 * @return aktualisiertes Contact-Objekt
	 * @throws IllegalArgumentException
	 */
	public Contact updateContact(Contact contact) throws IllegalArgumentException;
	
	/**
	 * Löscht ein Contact-Objekt und alle eventuell darauf basierenden
	 * Objekte.
	 * 
	 * @param contact
	 * @throws IllegalArgumentException
	 */
	public void deleteContact(Contact contact, User cUser) throws IllegalArgumentException;
	
	/**
	 * Erstellt ein neues Value-Objekt.
	 * 
	 * @return neu erstelltes Value-Objekt
	 * @throws IllegalArgumentException
	 */
	public Value createValue(String name, int propertyId, int contactId, int ownerId) throws IllegalArgumentException;
	
	/**
	 * Aktuallisiert ein Value-Objekt.
	 * 
	 * @return aktualisiertes Value-Objekt
	 * @throws IllegalArgumentException
	 */
	public Value updateValue(Value value) throws IllegalArgumentException;
	
	/**
	 * Löscht ein Value-Objekt und alle eventuell darauf basierenden
	 * Objekte.
	 * 
	 * @param value
	 * @throws IllegalArgumentException
	 */
	public void deleteValue(Value value) throws IllegalArgumentException;
	
	/**
	 * Erstellt ein neues ContactList-Objekt.
	 * 
	 * @return neu erstelltes ContactList-Objekt
	 * @throws IllegalArgumentException
	 */
	public ContactList createContactList(String name, int ownerId) throws IllegalArgumentException;
	
	/**
	 * Aktuallisiert ein ContactList-Objekt.
	 * 
	 * @return aktualisiertes ContactList-Objekt
	 * @throws IllegalArgumentException
	 */
	public void updateContactList(ContactList contactlist) throws IllegalArgumentException;
	
	/**
	 * Löscht ein ContactList-Objekt und alle eventuell darauf basierenden
	 * Objekte.
	 * 
	 * @param contactlist
	 * @throws IllegalArgumentException
	 */
	public void deleteContactList(ContactList contactlist) throws IllegalArgumentException;
	
	
	/**
	 * Gibt alle Contacts einer ContactList aus
	 * 
	 * @param contactlistId
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Contact> findContactsByContactListId(int contactlistId) throws IllegalArgumentException;
	
	/**
	 * Gibt alle Contacts mit einer bestimmten Ausprägung aus
	 * 
	 * @param valueId
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Contact> findContactsByValue(String value) throws IllegalArgumentException;
	
	/**
	 * Gibt einen spezifischen Contact anhand seiner Id aus.
	 * 
	 * @param contactId
	 * @throws IllegalArgumentException
	 */
	public Contact findContactById(int id) throws IllegalArgumentException;
	
	/**
	 * Gibt alle Contacte eines Users aus, bei denen er Owner ist
	 * 
	 * @param userId
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Contact> findContactsByOwnerId(int id) throws IllegalArgumentException;
	
	/**
	 * Gibt alle Values eines Contacts aus
	 * 
	 * @param contactlist
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Value> findValuesByContactId(int id) throws IllegalArgumentException;
	
	/**
	 * Gibt eine spezifische Property anhand seiner id aus
	 * 
	 * @param contactlist
	 * @throws IllegalArgumentException
	 */
	public Property findPropertyByPropertyId(int id) throws IllegalArgumentException;
	
	/**
	 * Gibt alle Permissions eines User aus
	 * 
	 * @param userId
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Permission> findPermissionsByUserId(int id) throws IllegalArgumentException;
	

	/**
	 * Löscht ein Permission-Objekt 
	 * 
	 * @param permission
	 * @throws IllegalArgumentException
	 */
	public void deletePermission(Permission permission, User cUser) throws IllegalArgumentException;
	
	/**
	 * Fügt ein Contact einer ContactList hinzu
	 * 
	 * @param contactId, contactlistId
	 * @throws IllegalArgumentException
	 */
	public void addContactsToContactList(ArrayList<Contact> contactArray, ArrayList<ContactList> contactlistArray) throws IllegalArgumentException;
	
	/**
	 * Entfernt ein Contact einer ContactList
	 * 
	 * @param contactId, contactlistId
	 * @throws IllegalArgumentException
	 */
	public void removeContactFromContactList(int contactid, int contactlistid) throws IllegalArgumentException;
			
	
	
	/**
	 * Löscht die Permission des Users auf das Objekt
	 * 
	 * @param shareObjectId, userId
	 * @throws IllegalArgumentException
	 */
	public void removeAccessToObject(int userId, int shareObjectId) throws IllegalArgumentException;
	
	/**
	 * Gibt alle Properties zurück
	 * 
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Property> findAllProperties() throws IllegalArgumentException;
	
	/**
	 * Gibt alle Contacts zurück
	 * 
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Contact> findAllContacts() throws IllegalArgumentException;
	
	/**
	 * Gibt alle Contactlists zurück
	 * 
	 * @throws IllegalArgumentException
	 */
	public ArrayList<ContactList> findAllContactlists () throws IllegalArgumentException;

	ArrayList<Contact> getContacts() throws IllegalArgumentException;

	ArrayList<Contact> findContactsByPrename(String prename) throws IllegalArgumentException;

	ArrayList<Contact> findContactsBySurname(String surname) throws IllegalArgumentException;

	ArrayList<Permission> getAllPermissions() throws IllegalArgumentException;

	ArrayList<Permission> getPermissionsByRecieveUserId(int recieveUId) throws IllegalArgumentException;

	ArrayList<Permission> getPermissionsByShareUserId(int shareUId) throws IllegalArgumentException;

	ArrayList<Permission> getPermissionsBySharedObjectId(int sharedOId) throws IllegalArgumentException;

	ArrayList<Permission> getPermissionsByValueId(int valueId) throws IllegalArgumentException;

	void updatePermission(Permission permission) throws IllegalArgumentException;

	Permission getPermissionById(int id) throws IllegalArgumentException;

	Map<Property, Value> findValueAndProperty(int id) throws IllegalArgumentException;

	ArrayList<Value> findValueByValue(String value) throws IllegalArgumentException;

	Value findValueById(int id) throws IllegalArgumentException;

	public void giveContactPermissonToUsers(ArrayList<Contact> contactArray, ArrayList<User> userArray, int shareuserid)
			throws IllegalArgumentException;
	
	public void givePermissionToUsers(int shareObjectId, ArrayList<User> userArray, int shareuserid) throws IllegalArgumentException;

	/**
	 * Erstellt ein neues Permission-Objekt.
	 * 
	 * @return neu erstelltes Permission-Objekt
	 * @throws IllegalArgumentException
	 */
	void createPermission(int shareUserId, ArrayList<Integer> shareObjectId, ArrayList<Integer> receiverUserId)
			throws IllegalArgumentException;

	User findUserById(int userId) throws IllegalArgumentException;

	User findUserByEmail(String email) throws IllegalArgumentException;

	boolean hasPermission(int shareObjectId, int receiverUserId) throws IllegalArgumentException;
	
	ArrayList<Contact> getContactsByUserPermission(int userId) throws IllegalArgumentException;

}
