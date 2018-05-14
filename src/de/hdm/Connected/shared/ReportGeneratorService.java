package de.hdm.Connected.shared;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * 
 * @author Patricia
 *
 */


public interface ReportGeneratorService extends RemoteService {

	void allContacts();

	void allContactsPerUser();

	void allSharedContacts();

	void allSharedContactsPerUser();

	void contactsBasedOnPropertiesAndValues();
	
	
	

}
