package de.hdm.Connected.shared.ReportGenerator;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

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
	List<Property> allProperties();
	List<Value> allValues(Integer propertyId);
	List<ReportObjekt> searchContacts(boolean allContacts, boolean sharedContacts, boolean detailSearch,
			String userEmail, Map<Integer, String> propertyValueMap, int currentUser);
	List<User> allUsers(int currentUser);
	
	
	

}
