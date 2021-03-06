package de.hdm.Connected.server.db;

import java.sql.*;
import java.util.ArrayList;

import de.hdm.Connected.shared.bo.ContactList;
import de.hdm.Connected.shared.bo.Contact;

/**
 * Die Klasse bildet die Beziehung zwischen Contact- und Contactlist-Objekten auf eine
 * relationale Datenbank ab. 
 * 
 * Zur Verwaltung dieser Beziehung stellt die Mapper-Klasse entsprechende
 * Methoden (add, remove und find) zuf Verfügung.
 * 
 * @author Viktoriya
 */

public class ContactContactListMapper {
	
	/**
	 * Die Klasse ContactContactListMapper wird nur einmal instantiiert
	 * (Singleton-Eigenschaft). Damit diese Eigenschaft erfüllt werden kann,
	 * wird zunächst eine Variable mit dem Schlüsselwort static und dem
	 * Standardwert null erzeugt. Sie speichert die Instanz dieser Klasse.
	 */
	private static ContactContactListMapper contactContactListMapper = null;

	/**
	 * Geschützter Konstruktor verhindert die Möglichkeit für das erneute
	 * Erzeugen von weiteren Instanzen dieser Klasse.
	 */
	protected ContactContactListMapper() {
	}

	/**
	 * Statische Methode zum Sicherstellen der Singleton-Eigenschaft. Sie sorgt
	 * dafür, dass nur eine einzige Instanz der PropertyMapper-Klasse
	 * exsistiert. Aufgerufen wird die Klasse somit über
	 * PropertyMapper.propertyMapper() und nicht über den New-Operator.
	 * 
	 * @return propertyMapper
	 */
	public static ContactContactListMapper contactContactListMapper() {
		if (contactContactListMapper == null) {
			contactContactListMapper = new ContactContactListMapper();
		}
	  return contactContactListMapper;
	}

	
	/**
	 * Fügt ein Contact-Objekt einer Contactlist hinzu.
	 * 
	 * @param contactId, contactListId
	 */
	public void addContactToContactList(int contactId, int contactListId) {
		/*
		 * DB-Verbindung holen
		 */
		Connection con = DBConnection.connection();
		
		try {
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			
			/*
			 * SQL-Anweisung zum Aktualisieren des übergebenen Datensatzes in
			 * der Datenbank.
			 */
			stmt.executeUpdate("INSERT INTO contactcontactlist (contactid, contactlistid) VALUES " 
			 + "("+ contactId + ", " + contactListId + ")");
		}
		/*
		 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
		 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
		 * ausgegeben, was passiert ist und wo im Code es passiert ist.
		 */
		catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/*
	 * Löscht ein Contact-Objekt aus der ContactList.
	 * 
	 * @param contactId, contactListId
	 */
	public void removeContactFromContactList(int contactId, int contactListId) {
		/*
		 * DB-Verbindung holen
		 */
		Connection con = DBConnection.connection();
		
		try {
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			/*
			 * SQL-Anweisung zum Löschen des übergebenen Datensatzes in der
			 * Datenbank.
			 */
			stmt.executeUpdate("DELETE FROM contactcontactlist WHERE contactid= " + contactId + 
					" AND " + "contactlistid= " + contactListId);
		}
		
	/*
	 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
	 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
	 * ausgegeben, was passiert ist und wo im Code es passiert ist.
	 */
		catch (SQLException e) {
		e.printStackTrace();
		}
	}
	
	/**
	 * Löscht ein Contact-Objekt aus allen ContactListen.
	 * 
	 * @param contactId
	 */
	public void removeContactFromAllContactList(int contactId) {
		/*
		 * DB-Verbindung holen
		 */
		Connection con = DBConnection.connection();
		
		try {
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			/*
			 * SQL-Anweisung zum Löschen des übergebenen Datensatzes in der
			 * Datenbank.
			 */
			stmt.executeUpdate("DELETE FROM contactcontactlist WHERE contactid= " + contactId);
		}
		
	/*
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
	public ArrayList <Contact> findContactsByContactListId(int contactListId) {
	/*
	 * DB-Verbindung holen
	 */
	Connection con = DBConnection.connection();
	
	ArrayList<Contact> result = new ArrayList<Contact>();
	
	try {
		con.setAutoCommit(true);
		Statement stmt = con.createStatement();
		
		/*
		 * SQL-Anweisung zum Finden des Datensatzes, anhand des uebergebenen Namens, in
		 * der Datenbank, sortiert nach der Id.
		 */
		ResultSet rs = stmt.executeQuery(
				"SELECT contactid FROM contactcontactlist WHERE " + "contactlistid= " + contactListId);
		
		/*
		 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der Tabelle
		 * mit dem uebergebenen Namen vorhanden ist, muss das Abfragen des ResultSet so
		 * oft erfolgen (while-Schleife), bis alle Tupel durchlaufen wurden. Die
		 * DB-Tupel werden in Java-Objekte transformiert und anschliessend der ArrayList
		 * hinzugefuegt.
		 */
		while (rs.next()) {
			Contact contact = new Contact();
			contact.setId(rs.getInt("contactid"));
			result.add(contact);
		}
		/*
		 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die Fehlermeldung
		 * genauer zu analyisieren. Es werden Informationen dazu ausgegeben, was
		 * passiert ist und wo im Code es passiert ist.
		 */
		} catch (SQLException e) {
		e.printStackTrace();
		
		}
		 /*
		  * Rückgabe der ArrayList
		  */
		 return result;
	}			
}