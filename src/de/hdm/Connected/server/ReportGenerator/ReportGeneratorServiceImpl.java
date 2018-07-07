package de.hdm.Connected.server.ReportGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import de.hdm.Connected.server.ConnectedAdminImpl;
import de.hdm.Connected.server.LoginServiceImpl;
import de.hdm.Connected.server.ServersideSettings;
import de.hdm.Connected.shared.ConnectedAdmin;
import de.hdm.Connected.shared.ReportGenerator.ReportGeneratorService;
import de.hdm.Connected.shared.ReportGenerator.ReportObjekt;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Permission;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

/**
 * 
 * @author Patricia Applikationslogik des Report Generators zur Wiedergabe aller
 *         Kontakte, aller Kontakte per Nutzer, aller geteilten Kontakte, aller
 *         geteilten Kontakte per Nutzer, Abfrage von Eigenschaften anhand
 *         Aussprägungen.
 *
 */

public class ReportGeneratorServiceImpl extends RemoteServiceServlet implements ReportGeneratorService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7457694965996952587L;
	private ConnectedAdmin adminImpl = null;
	private LoginServiceImpl loginServiceImpl = null;

	/*
	 * Init ist eine Initialisierungsmethode, welche für jede Instanz der
	 * ReportGeneratorServiceImpl gerufen werden muss.
	 */
	public void init() throws IllegalArgumentException {
		if (adminImpl == null) {
			adminImpl = new ConnectedAdminImpl();
		}
		if (loginServiceImpl == null) {
			loginServiceImpl = new LoginServiceImpl();
		}
		adminImpl.init();
	}

	@Override
	public List<User> allUsers(int currentUser) {
		List<User> result = new ArrayList<User>();
		List<Permission> sharedPermissions = this.adminImpl.getPermissionsByShareUserId(currentUser);
		for(Permission p : sharedPermissions){
			User u = adminImpl.findUserById(p.getReceiverUserID());
			if(!result.contains(u)){
				result.add(u);
			}
		}
		return result;
	}

	@Override
	public List<Property> allProperties() {
		return this.adminImpl.findAllProperties();
	}

	@Override
	public List<Value> allValues(Integer propertyId) {
		return adminImpl.findAllValues(propertyId);
	}

	@Override
	public List<ReportObjekt> searchContacts(boolean allContacts, boolean sharedContacts, boolean detailSearch,
			String userEmail, Map<Integer, String> propertyValueMap, int currentUser) {

		// Input parameter werden Protokolliert
		ServersideSettings.getLogger().info("allContacts: " + allContacts);
		ServersideSettings.getLogger().info("sharedContacts: " + sharedContacts);
		ServersideSettings.getLogger().info("detailSearch: " + detailSearch);
		ServersideSettings.getLogger().info("userEmail: " + userEmail);
		ServersideSettings.getLogger().info("propertyValueMap size: " + propertyValueMap.size());
		ServersideSettings.getLogger().info("currentUser: " + currentUser);

		List<Contact> ergebnisKontakte = new ArrayList<Contact>();
		List<ReportObjekt> ergebnisReport = new ArrayList<ReportObjekt>();

		// alle meine Kontakte
		if (allContacts) {
			ergebnisKontakte = allContactsPerUser(currentUser);
			List<Permission> permissionReceiver = this.adminImpl.getPermissionsByRecieveUserId(currentUser);
			ServersideSettings.getLogger().info("PermissionReceiver: " + permissionReceiver.size());
			for (Permission p : permissionReceiver) {
				ergebnisKontakte.add(adminImpl.findContactById(p.getSharedObjectId()));
			}
		}
		// alle meine geteilten Kontakte
		else if (sharedContacts) {

			// alle meine geteilten Kontakte die ich mit einem bestimmten Nutzer
			// geteilt habe
			if (!userEmail.isEmpty()) {
				// Wenn nicht nach allen Kontakten gesucht werden soll und ein
				// Nutzer gesetzt ist
				if (userEmail != null && !userEmail.isEmpty()) {
					User u = adminImpl.findUserByEmail(userEmail);
					ergebnisKontakte = myContactsSharedWithUser(currentUser, u);

				}
			} else {
				ergebnisKontakte = allSharedContactsPerUser(currentUser);

			}

		}
		// alle Kontakte mit bestimmten Eigenschaften
		else if (detailSearch) {
			ergebnisKontakte = allContactsPerUser(currentUser);
			// Wenn es property filter gibt dann noch filtern...
			if (!propertyValueMap.isEmpty()) {

				// Hier holen wir uns alle Property Ids aus der HashMap
				Set<Integer> propertyIds = propertyValueMap.keySet();

				// Für jeden Key (PropertyId) aus der HashMap suchen wir die
				// Kontakte welche genau
				// die werte zu einer Property haben
				for (Integer propertyKey : propertyIds) {
					List<Contact> valuesAndProperties = contactsBasedOnPropertiesAndValues(propertyKey,
							propertyValueMap.get(propertyKey));
					ergebnisKontakte.retainAll(valuesAndProperties);
				}
			}
		} else {
			// Fehler in der Programmlogik... das darf nicht passieren....
		}

		ArrayList<Property> allproperties = adminImpl.findAllProperties();
		// Für jeden ErgebnisKontakt die Eigenschaften aufbauen
		for (Contact c : ergebnisKontakte) {
			if(c!=null){
			// Eigenschaften lesen
			List<Value> eigenschaften = adminImpl.findValuesByContactId(c.getBoId());

			// Reportobjekt aufbauen
			Map<Integer, String> eigenschaftsMap = new HashMap<Integer, String>();

			// Für jede mögliche Eigenschaft prüfen ob der Nutzer dazu ein Wert
			// hat
			for (Property p : allproperties) {
				eigenschaftsMap.put(p.getBoId(), findeWertZuEigenschaft(p.getBoId(), eigenschaften));
			}

			// ReportObjekt aus den berechneten Werten zusammenbauen
			ReportObjekt ro = new ReportObjekt(c.getPrename(), c.getSurname(), eigenschaftsMap);
			// Reportobjekt dem Ergebnis hinzufügen
			ergebnisReport.add(ro);
			}
		}

		ServersideSettings.getLogger().info("Gefundenen ReportObjekte: " + ergebnisReport.size());
		return ergebnisReport;

	}

	/**
	 * Liefert die zu einem eingeloggtem Nutzer mit einem speziellen Kontakt die
	 * geteilten Kontakte zurück.
	 * 
	 * @param currentUser
	 *            eingeloggte Nutzer id
	 * @param sharedWith
	 *            Nutzer für welchen die geteilten Kontakte angezeigt werden
	 *            sollen
	 * @return
	 */
	private List<Contact> myContactsSharedWithUser(int currentUser, User sharedWith) {
		List<Contact> sharedContacts = new ArrayList<Contact>();
		List<Permission> sharedPermissions = this.adminImpl.getPermissionsByShareUserId(currentUser);

		// Für jede Permission muss der Kontakt des Shared Objekt geladen werden
		// wenn dieser mit dem sharedWith user übereinstimmt
		for (Permission p : sharedPermissions) {
			if (p.getReceiverUserID() == sharedWith.getBoId()) {
				Contact c = adminImpl.findContactById(p.getSharedObjectId());
				if (!sharedContacts.contains(c)) {
					sharedContacts.add(c);
				}
			}
		}

		return sharedContacts;
	}

	/**
	 * Liefert alle Kontakte zurück, die der übergebene Nutzer angelegt hat (ist
	 * owner von diesen)
	 * 
	 * @param userid
	 *            Id des eingeloggten Nutzers
	 * @return Alle Kontakte zu denen die übergebene userId Owner ist.
	 */
	private List<Contact> allContactsPerUser(int userid) {
		return this.adminImpl.findContactsByOwnerId(userid);
	}

	/**
	 * Liefer alle geteilten Kontakte zu einem übergebenen (eingeloggten) Nutzer
	 * zurück.
	 * 
	 * @param userId
	 *            userId des eingeloggten Nutzers
	 * @return Alle Kontakte die der eingeloggte Nutzer geteilt hat.
	 */
	private List<Contact> allSharedContactsPerUser(int userId) {

		List<Contact> sharedContacts = new ArrayList<Contact>();
		List<Permission> sharedPermissions = this.adminImpl.getPermissionsByShareUserId(userId);
		// Für jede Permission muss der Kontakt des Shared Objekt geladen werden
		for (Permission p : sharedPermissions) {
			Contact c = adminImpl.findContactById(p.getSharedObjectId());
			if (!sharedContacts.contains(c)) {
				sharedContacts.add(c);
			}
		}

		return sharedContacts;
	}

	/**
	 * Berechnet alle Eigenschaftsausprägungen als Komma separierten String zu
	 * einer übergebenen property id und value Liste
	 * 
	 * @param id
	 *            property ID der Eingenschaft für welche die Werte berechnet
	 *            werden sollen.
	 * @param list
	 *            Liste der Eingenschaften die als Basis für die Berechnung
	 *            herangezogen werden soll.
	 * @return Eregbnisstring (Komma separiert) alle eingeschaften der Liste die
	 *         zu der Property id gehören.
	 */
	private String findeWertZuEigenschaft(int id, List<Value> list) {
		String shownValue = "";
		for (Value v : list) {
			if (v.getPropertyID() == id) {
				if (shownValue.isEmpty()) {
					shownValue = v.getName();
				} else {
					/*
					 * Wenn schonmal eine Ausprägung zur eigenschaft gefunden
					 * wurde, dann Komma separiert die nächste Ausprägung
					 * anhängen
					 */
					shownValue = shownValue + ", " + v.getName();
				}
			}
		}
		return shownValue;
	}

	/**
	 * Liefert alle Kontakte für welche eine bestimmte Eingeschaft und
	 * Ausprägung zutrifft.
	 * 
	 * @param propertyId
	 *            Eingenschaft (id) für welche Kontakte gesucht werden sollen
	 * @param valueDescription
	 *            Eigenschaftsausprägung für welche Kontakte gesucht werdne
	 *            sollen
	 * @return Liste aller Kontakte die eine bestimmte Eingeschaft und
	 *         Ausprägung haben.
	 */
	private List<Contact> contactsBasedOnPropertiesAndValues(int propertyId, String valueDescription) {

		// Liefert Kontakte, die einen bestimmten Property / Value haben.
		List<Value> listOfValuesWithGivenProperty = adminImpl.findValuesByPropertyAndDescription(propertyId,
				valueDescription);

		List<Contact> result = new ArrayList<>();
		for (Value value : listOfValuesWithGivenProperty) {
			result.add(adminImpl.findContactById(value.getContactID()));
		}
		return result;

	}

}
