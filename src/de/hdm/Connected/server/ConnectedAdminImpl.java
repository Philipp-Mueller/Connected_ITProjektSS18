package de.hdm.Connected.server;

import java.sql.Date;
import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet; //weiß gerade noch nicht wo bzw wie ich sie sonst importieren soll...

import de.pitchMen.server.db.ApplicationMapper;
import de.pitchMen.server.db.CompanyMapper;
import de.pitchMen.server.db.JobPostingMapper;
import de.pitchMen.server.db.MarketplaceMapper;
import de.pitchMen.server.db.ParticipationMapper;
import de.pitchMen.server.db.PartnerProfileMapper;
import de.pitchMen.server.db.PersonMapper;
import de.pitchMen.server.db.ProjectMapper;
import de.pitchMen.server.db.RatingMapper;
import de.pitchMen.server.db.TeamMapper;
import de.pitchMen.server.db.TraitMapper;
import de.pitchMen.shared.bo.Application;
import de.pitchMen.shared.bo.Rating;

/**
 * Implemetierungsklasse des Interface ConnectedAdmin. Sie enthält die
 * Applikationslogik, stellt die Zusammenhänge konstistent dar und ist
 * zuständig um einen geordneten Ablauf zu gewährleisten.
 * 
 * @author Denise
 *
 */
public class ConnectedAdminImpl extends RemoteServiceServlet {

	public ConnectedAdminImpl() throws IllegalArgumentException {
	}

	private static final long serialVersionUID = 1L;

	/**
	 * Referenzen auf die DatenbankMapper, die Objekte mit der Datenbank
	 * abgleicht.
	 */	
	private ContactListMapper 	contactlistMapper = null;
	private ContactMapper 	contactMapper = null;
	private PermissionMapper 	permissionMapper = null;
	private PropertyMapper	propertyMapper = null;
	private UserMapper	userMapper = null;
	private ValueMapper	valueMapper = null;
	
	@Override
	public void init() throws IllegalArgumentException {
	

		/*
		 * Vollständiger Satz von Mappern mit deren Hilfe ConnectedAdminImpl mit der Datenbank kommunizieren kann.
		 */

		this.contactlistMapper = ContactListMapper.contactlistMapper();
		this.contactMapper = ContactMapper.contactMapper();
		this.permissionMapper = PermissionMapper.permissionMapper();
		this.propertyMapper = PropertyMapper.propertyMapper();
		this.userMapper = UserMapper.userMapper();
		this.valueMapper = ValueMapper.valueMapper();


	}

	//			 **** CONTACT****
	
	public Contact createContact(String prename, String surname, int ownerId){
		Contact contact = new Contact();
		contact.setPrename(prename);
		contact.setSurname(surname);
		contact.setOwnerId(ownerId);

		return this.contactMapper.insert(contact);

	}

	@Override
	public void updateContact(Contact contact) throws IllegalArgumentException {
		contactMapper.update(contact);
	}

	@Override
	public void deleteContact(Contact contact) throws IllegalArgumentException {
		Contact contact = this.getContactByContactId(contact.getId());

		if (contact != null) {
			this.contactMapper.delete(contact);
		}

		this.contactMapper.delete(contact);
	}

	@Override
	public ArrayList<Contact> getContacts() throws IllegalArgumentException {
		return this.contactMapper.findAll();
	}

	@Override
	public ArrayList<Contact> getContactByUser(int userId) throws IllegalArgumentException {
		return this.contactMapper.findContactsByUserId(userId);
	}

	// 			*** ContactList ***
	
	
	
	
	
}
