package de.hdm.Connected.shared;

import java.sql.Timestamp;

import com.google.gwt.user.client.rpc.RemoteService;

import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.ContactList;
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
	
	public Property createProperty(String name) throws IllegalArgumentException;
	
	public void updateProperty(Property property) throws IllegalArgumentException;
	
	public void deleteProperty(Property property) throws IllegalArgumentException;
	
	public Contact createContact(String prename, String surname, Timestamp creationDate, Timestamp modificationDate, int ownerId) throws IllegalArgumentException;
	
	public void updateContact(Contact contact) throws IllegalArgumentException;
	
	public void deleteContact(Contact contact) throws IllegalArgumentException;
	
	public Value createValue(String name, int propertyId, int contactId) throws IllegalArgumentException;
	
	public void updateValue(Value value) throws IllegalArgumentException;
	
	public void deleteValue(Value value) throws IllegalArgumentException;
	
	public ContactList createContactList(String name) throws IllegalArgumentException;
	
	public void updateContactList(ContactList contactlist) throws IllegalArgumentException;
	
	public void deleteContactList(ContactList contactlist) throws IllegalArgumentException;
}
