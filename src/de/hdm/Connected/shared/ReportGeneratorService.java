package de.hdm.Connected.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

/**
 * 
 * @author Patricia
 *
 */

@RemoteServiceRelativePath("connectedreportgenerator")
public interface ReportGeneratorService extends RemoteService {

	
	void init();
	List<User> allUsers();
	List<Property> allProperties();
	List<Value> allValues(Integer propertyId);
	List<Contact> searchContacts(boolean allContacts, boolean sharedContacts, String userEmail, Integer propertyId, String valueDescription);
	
	
	

}
