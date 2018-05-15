package de.hdm.Connected.shared;

import java.sql.Timestamp;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.ContactList;
import de.hdm.Connected.shared.bo.Permission;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

/**
 * @author Denise, Moritz
 * Das asynchrone Gegenstück des Interface ConnectedAdmin. Es wird
 * semiautomatisch durch das Google Plugin erstellt und gepflegt.
 */

public interface ConnectedAdminAsync {

	void init(AsyncCallback<Void> callback);
	
	void createUser(String email, AsyncCallback<User> callback);
	
	void updateUser(User user, AsyncCallback<Void> callback);
	
	void deleteUser(User user, AsyncCallback<Void> callback);
	
	void createProperty(String name, AsyncCallback<Property> callback);
	
	void updateProperty(Property property, AsyncCallback<Void> callback);
	
	void deleteProperty(Property property, AsyncCallback<Void> callback);
	
	void createContact(String prename, String surname, Timestamp creationDate, Timestamp modificationDate, int ownerId, AsyncCallback<Contact> callback);
	
	void updateContact(Contact contact, AsyncCallback<Void> callback);
	
	void deleteContact(Contact contact, AsyncCallback<Void> callback);
	
	void createValue(String name, int propertyId, int contactId, AsyncCallback<Value> callback);
	
	void updateValue(Value value, AsyncCallback<Void> callback);
	
	void deleteValue(Value value, AsyncCallback<Void> callback);
	
	void createContactList(String name, AsyncCallback<ContactList> callback); 
	
	void updateContactList(ContactList contactlist, AsyncCallback<Void> callback);
	
	void deleteContactList(ContactList contactlist, AsyncCallback<Void> callback);
	
	void findContactsByContactListId(int contactlistId, AsyncCallback<ArrayList<Contact>> callback);
	
	void findContactsByValue(String value, AsyncCallback<ArrayList<Contact>> callback);
	
	void findContactById(int id, AsyncCallback<Contact> callback);
	
	void findContactsByOwnerId(int id, AsyncCallback <ArrayList<Contact>> callback);
	
	void findValuesByContactId(int id, AsyncCallback <ArrayList<Value>> callback);
	
	void findPropertyByPropertyId(int id, AsyncCallback <Property> callback);
	
	void findPermissionsByUserId(int id, AsyncCallback <ArrayList<Permission>> callback);
	
	void createPermission(int shareUserId, int shareObjectId, int receiverUserId, AsyncCallback<Permission> callback);
	
	void deletePermission(Permission permission, AsyncCallback<Void> callback);
	
	void addContactToContactList(int contactId, int contactlistId, AsyncCallback<Void> callback);
	
	void removeContactFromContactList(int contactId, int contactListId, AsyncCallback<Void> callback);
	
	void removeAccessToObject(int userId, int shareObjectId, AsyncCallback<Void> callback);
	
	void findAllProperties(AsyncCallback<ArrayList<Property>> callback);
	
	void findAllContacts(AsyncCallback<ArrayList<Contact>> callback);
	
	void findAllContactlists(AsyncCallback<ArrayList<ContactList>> callback);
	
}
