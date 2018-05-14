package de.hdm.Connected.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import de.hdm.Connected.shared.bo.Contact;

/**
 * 
 * @author Patricia
 *
 */


public interface ReportGeneratorService extends RemoteService {

	List<Contact> allContacts();
	

	void allContactsPerUser();

	void allSharedContacts();

	void allSharedContactsPerUser();

	void contactsBasedOnPropertiesAndValues();
	
	
	

}
