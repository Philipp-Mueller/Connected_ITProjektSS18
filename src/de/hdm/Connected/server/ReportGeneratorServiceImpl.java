package de.hdm.Connected.server;

import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.Connected.shared.ReportGeneratorService;
import de.hdm.Connected.shared.bo.Contact;

/**
 * 
 * @author Patricia
 * Applikationslogik des Report Generators zur Wiedergabe aller Kontakte, aller Kontakte per Nutzer,
 * aller geteilten Kontakte, alle geteilte Kontakte per Nutzer, Abfrage von Eigenschaften anhand Aussprägungen.
 *
 */

public class ReportGeneratorServiceImpl extends RemoteServiceServlet implements ReportGeneratorService{

	@Override
	public List<Contact> allContacts() {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allContactsPerUser() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allSharedContacts() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allSharedContactsPerUser() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contactsBasedOnPropertiesAndValues() {
		// TODO Auto-generated method stub
		
	}

}
