package de.hdm.Connected.shared;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.Connected.client.LoginInfo;
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
	
	void createUser(LoginInfo info, AsyncCallback<User> callback);
	
	void updateUser(User user, AsyncCallback<Void> callback);
	
	void deleteUser(User user, AsyncCallback<Void> callback);
	
	void findAllUser(AsyncCallback<ArrayList<User>> callback);
	
	void createProperty(String name, AsyncCallback<Property> callback);
	
	void updateProperty(Property property, AsyncCallback<Void> callback);
	
	void deleteProperty(Property property, AsyncCallback<Void> callback);
	
	void createContact(String prename, String surname, Date creationDate, Date modificationDate, int ownerId, AsyncCallback<Contact> callback);
	
	void updateContact(Contact contact, AsyncCallback<Contact> callback);
	
	void deleteContact(Contact contact, User cUser, AsyncCallback<Void> callback);
	
	void createValue(String name, int propertyId, int contactId, int ownerId, AsyncCallback<Value> callback);
	
	void updateValue(Value value, AsyncCallback<Value> callback);
	
	void deleteValue(Value value, AsyncCallback<Void> callback);
	
	void createContactList(String name, int ownerId, AsyncCallback<ContactList> callback); 
	
	void updateContactList(ContactList contactlist, AsyncCallback<Void> callback);
	
	void deleteContactList(ContactList contactlist, AsyncCallback<Void> callback);
	
	void findContactsByContactListId(int contactlistId, AsyncCallback<ArrayList<Contact>> callback);
	
	void findContactsByValue(String value, AsyncCallback<ArrayList<Contact>> callback);
	
	void findContactById(int id, AsyncCallback<Contact> callback);
	
	void findContactsByOwnerId(int id, AsyncCallback <ArrayList<Contact>> callback);
	
	void findValuesByContactId(int id, AsyncCallback <ArrayList<Value>> callback);
	
	void findPropertyByPropertyId(int id, AsyncCallback <Property> callback);
	
	void findPermissionsByUserId(int id, AsyncCallback <ArrayList<Permission>> callback);
	
	void createPermission(int shareUserId, ArrayList<Integer> shareObjectId, ArrayList<Integer> receiverUserId, AsyncCallback<Void> callback);
	
	void deletePermission(Permission permission, AsyncCallback<Void> callback);
	
	void addContactsToContactList(ArrayList<Contact> contactArray, ArrayList<ContactList> contactlistArray, AsyncCallback<Void> callback);
	
	void removeContactFromContactList(int contactid, int contactlistid, AsyncCallback<Void> callback);
	
	void findAllProperties(AsyncCallback<ArrayList<Property>> callback);
	
	void findAllContacts(AsyncCallback<ArrayList<Contact>> callback);
	
	void findAllContactlists(AsyncCallback<ArrayList<ContactList>> callback);

	void getContacts(AsyncCallback<ArrayList<Contact>> callback);

	void findContactsByPrename(String prename, AsyncCallback<ArrayList<Contact>> callback);

	void findContactsBySurname(String surname, AsyncCallback<ArrayList<Contact>> callback);

	void getAllPermissions(AsyncCallback<ArrayList<Permission>> callback);

	void getPermissionsByRecieveUserId(int recieveUId, AsyncCallback<ArrayList<Permission>> callback);

	void getPermissionsByShareUserId(int shareUId, AsyncCallback<ArrayList<Permission>> callback);

	void getPermissionsBySharedObjectId(int sharedOId, AsyncCallback<ArrayList<Permission>> callback);

	void getPermissionsByValueId(int valueId, AsyncCallback<ArrayList<Permission>> callback);

	void updatePermission(Permission permission, AsyncCallback<Void> callback);

	void getPermissionById(int id, AsyncCallback<Permission> callback);

	void findValueAndProperty(int contactId, int userId, AsyncCallback<Map<Property, Value>> callback);

	void findValueByValue(String value, AsyncCallback<ArrayList<Value>> callback);

	void findValueById(int id, AsyncCallback<Value> callback);
	
	void giveContactPermissonToUsers(ArrayList<Contact> contactArray, ArrayList<User> userArray, int shareuserid, AsyncCallback<Void> callback);
	
	void givePermissionToUsers(int shareObjectId, ArrayList<User> userArray, int shareuserid, AsyncCallback<Void> callback);

	void findUserById(int userId, AsyncCallback<User> callback);

	void findUserByEmail(String email, AsyncCallback<User> callback);
	
	void hasPermission(int shareObjectId, int receiverUserId, AsyncCallback<Boolean> callback);
	
	void getContactsByUserPermission(int userId, AsyncCallback<ArrayList<Contact>> callback);

	void getContactListsByUserPermission(int userId, AsyncCallback<ArrayList<ContactList>> callback);

	void getValuesByUserPermission(int contactId, int userId, AsyncCallback<ArrayList<Value>> callback);

	void deletePermissionFromContact(int userId, int contactId, AsyncCallback<Void> callback);
	
}
