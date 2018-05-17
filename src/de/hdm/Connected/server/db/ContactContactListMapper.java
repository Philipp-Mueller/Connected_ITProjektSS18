package de.hdm.Connected.server.db;

import java.sql.*;
import java.util.ArrayList;

import de.hdm.Connected.shared.bo.ContactList;
import de.hdm.Connected.shared.bo.Contact;

/**
 * @author Viktoriya
 */

public class ContactContactListMapper {
 
	/**
	 * Fügt ein Contact-Objekt einer ContactList hinzu.
	 * 
	 * @param contactList, contact
	 * @return contactList
	 */
	
	public ContactList addContacttoContactList(ContactList contactList, Contact contact) {
		Connection con = DBConnection.connection();
		
		try {
			
			Statement stmt = con.createStatement();
			
			/**
			 * SQL-Anweisung zum Aktualisieren des übergebenen Datensatzes in
			 * der Datenbank.
			 */
			stmt.executeUpdate("INSERT INTO contactcontactlist (contactlistid, contactid) VALUES " 
			 + "("+ contactList.getBoId() + ", '" + contact.getBoId() + "')");
		}
		/**
		 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
		 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
		 * ausgegeben, was passiert ist und wo im Code es passiert ist.
		 */
		catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}
	
	/**
	 * Löscht ein Contact-Objekt aus der ContactList.
	 * 
	 * @param contactList, contact
	 */

	public void removeContactfromContactList(ContactList contactList, Contact contact) {
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Löschen des übergebenen Datensatzes in der
			 * Datenbank.
			 */
			stmt.executeUpdate("DELETE FROM contactcontactlist WHERE contactlistid= " + contactList.getBoId() + 
					" AND " + "contactid= " + contact.getBoId());
		}
		
		/**
	 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
	 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
	 * ausgegeben, was passiert ist und wo im Code es passiert ist.
	 */
	catch (SQLException e) {
		e.printStackTrace();
		
		}
	}
	
	/**
	 * Sucht Contact-Objekte ahnand einer ContactListId.
	 * 
	 * @param contactListId
	 * @return ArrayList <Contact>
	 */
	
	public ArrayList <Contact> findContactsByContactList(int contactListId) {
		
	Connection con = DBConnection.connection();
	
	ArrayList<Contact> result = new ArrayList<Contact>();
	
	try {
		Statement stmt = con.createStatement();
		/**
		 * SQL-Anweisung zum Finden des Datensatzes, anhand des uebergebenen Namens, in
		 * der Datenbank, sortiert nach der Id.
		 */
		
		ResultSet rs = stmt.executeQuery(
				"SELECT contactid FROM contactcontactlist WHERE " + "contactlistid= " + contactListId);
		/**
		 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der Tabelle
		 * mit dem uebergebenen Namen vorhanden ist, muss das Abfragen des ResultSet so
		 * oft erfolgen (while-Schleife), bis alle Tupel durchlaufen wurden. Die
		 * DB-Tupel werden in Java-Objekte transformiert und anschliessend der ArrayList
		 * hinzugefuegt.
		 */
		while (rs.next()) {
			Contact contact = new Contact();
			contact.setBoId(rs.getInt("contactid"));
			result.add(contact);
		}
		/**
		 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die Fehlermeldung
		 * genauer zu analyisieren. Es werden Informationen dazu ausgegeben, was
		 * passiert ist und wo im Code es passiert ist.
		 */
		} catch (SQLException e) {
		e.printStackTrace();
		
		}
		 return result;
	}
			
}