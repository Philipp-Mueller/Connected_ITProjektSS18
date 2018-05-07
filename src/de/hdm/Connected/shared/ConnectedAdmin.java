package de.hdm.Connected.shared;

import java.sql.Timestamp;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;

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
	public User createUser(String email) throws IllegalArgumentException;
	
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
	public void updateContact(Contact contact) throws IllegalArgumentException;
	
	/**
	 * Löscht ein Contact-Objekt und alle eventuell darauf basierenden
	 * Objekte.
	 * 
	 * @param contact
	 * @throws IllegalArgumentException
	 */
	public void deleteContact(Contact contact) throws IllegalArgumentException;
	
	/**
	 * Erstellt ein neues Value-Objekt.
	 * 
	 * @return neu erstelltes Value-Objekt
	 * @throws IllegalArgumentException
	 */
	public Value createValue(String name, int propertyId, int contactId) throws IllegalArgumentException;
	
	/**
	 * Aktuallisiert ein Value-Objekt.
	 * 
	 * @return aktualisiertes Value-Objekt
	 * @throws IllegalArgumentException
	 */
	public void updateValue(Value value) throws IllegalArgumentException;
	
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
	public ContactList createContactList(String name) throws IllegalArgumentException;
	
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
	 * Erstellt ein neues Permission-Objekt.
	 * 
	 * @return neu erstelltes Permission-Objekt
	 * @throws IllegalArgumentException
	 */
	public Permission createPermission(int shareUserId, int shareObjectId, int receiverUserId) throws IllegalArgumentException;
	
	/**
	 * Löscht ein Permission-Objekt 
	 * 
	 * @param permission
	 * @throws IllegalArgumentException
	 */
	public void deletePermission(Permission permission) throws IllegalArgumentException;
	
	/**
	 * Fügt ein Contact einer ContactList hinzu
	 * 
	 * @param contactId, contactlistId
	 * @throws IllegalArgumentException
	 */
	public void addContactToContactList(int contactId, int contactlistId) throws IllegalArgumentException;
	
	/**
	 * Entfernt ein Contact einer ContactList
	 * 
	 * @param contactId, contactlistId
	 * @throws IllegalArgumentException
	 */
	public void removeContactFromContactList(int contactId, int contactlistid) throws IllegalArgumentException;
	
	/**
	 * Löscht die Permission des Users auf das Objekt
	 * 
	 * @param shareObjectId, userId
	 * @throws IllegalArgumentException
	 */
	public void removeAccessToObject(int userId, int shareObjectId) throws IllegalArgumentException;
	
	/**
	 * Gibt alle Properties zurückk
	 * 
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Property> findAllProperties() throws IllegalArgumentException;
}
