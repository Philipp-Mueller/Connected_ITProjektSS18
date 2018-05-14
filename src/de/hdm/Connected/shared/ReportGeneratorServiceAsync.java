package de.hdm.Connected.shared;

/**
 * 
 * @author Patricia
 *
 */

public interface ReportGeneratorServiceAsync {
	
	void allContacts(asyncCallback callback);
	void allContactsPerUser(asyncCallback callback);
	void allSharedContacts(asyncCallback callback);
	void allSharedContactsPerUser(asyncCallback callback);
	void showContactsBasedOnPropertiesAndValues(asyncCallback callback);
	
	

}
