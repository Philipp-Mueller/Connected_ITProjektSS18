package de.hdm.Connected.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Permission;

/**
 * 
 * @author Patricia
 *
 */

public interface ReportGeneratorServiceAsync {
	
	void allContacts(AsyncCallback callback);
	void allContactsPerUser(int userId, AsyncCallback<List<Contact>> callback);
	void allSharedContacts(int id, AsyncCallback<List<Permission>> callback);
	void allSharedContactsPerUser(int userId, AsyncCallback<List<Permission>> callback);
	void contactsBasedOnPropertiesAndValues(int propertyId, String valueDescription,
			AsyncCallback<List<Contact>> callback);
	void init(AsyncCallback<Void> initReportGeneratorCallback);
	
	

}
