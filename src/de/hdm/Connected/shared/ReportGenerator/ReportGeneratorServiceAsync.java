package de.hdm.Connected.shared.ReportGenerator;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

/**
 * 
 * @author Patricia
 *
 */

public interface ReportGeneratorServiceAsync {
	
	void init(AsyncCallback<Void> initReportGeneratorCallback);
	void allUsers(AsyncCallback<List<User>> callback);
	void allProperties(AsyncCallback<List<Property>> callback);
	void allValues(Integer propertyId, AsyncCallback<List<Value>> callback);
	void searchContacts(boolean allContacts, boolean sharedContacts, String userEmail, Integer propertyId,
			String valueDescription, AsyncCallback<List<Contact>> callback);
	
	

}
