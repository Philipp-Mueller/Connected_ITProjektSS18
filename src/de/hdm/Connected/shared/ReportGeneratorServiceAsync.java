package de.hdm.Connected.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Patricia
 *
 */

public interface ReportGeneratorServiceAsync {
	
	void allContacts(AsyncCallback callback);
	void allContactsPerUser(AsyncCallback callback);
	void allSharedContacts(AsyncCallback callback);
	void allSharedContactsPerUser(AsyncCallback callback);
	void contactsBasedOnPropertiesAndValues(AsyncCallback callback);
	
	

}
