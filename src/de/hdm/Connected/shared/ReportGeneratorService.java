package de.hdm.Connected.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Permission;

/**
 * 
 * @author Patricia
 *
 */


public interface ReportGeneratorService extends RemoteService {

	List<Contact> allContacts();
	List<Contact> allContactsPerUser(int userId);
	List<Permission> allSharedContacts();
	List<Permission> allSharedContactsPerUser(int userId);
	List<Contact> contactsBasedOnPropertiesAndValues(int propertyId, int valueId);
	
	
	

}
