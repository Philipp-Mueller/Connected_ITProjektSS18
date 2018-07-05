 package de.hdm.Connected.server.db;

 import java.sql.*;
import java.util.ArrayList;

import de.hdm.Connected.shared.bo.ContactList;

 /**
  * Die Klasse ContactListMapper bildet ContactList-Objekte auf eine relationale Datenbank
  * ab. Ebenfalls ist es moeglich aus Datenbank-Tupel Java-Objekte zu erzeugen.
  * 
  * Zur Verwaltung der Objekte implementiert die Mapper-Klasse entsprechende
  * Methoden (insert, search, delete, update).
  * 
  * Durch extends SharedObjectMapper wird die Vererbung von SharedObjects dargestellt und in der DB-Ebene verdeutlicht.
  * 
  * @author Philipp
  */
 
public class ContactListMapper extends SharedObjectMapper{
	/**
	 * Die Klasse ContactListMapper wird nur einmal instantiiert
	 * (Singleton-Eigenschaft). Damit diese Eigenschaft erfüllt werden kann,
	 * wird zunächst eine Variable mit dem Schlüsselwort static und dem
	 * Standardwert null erzeugt. Sie speichert die Instanz dieser Klasse.
	 */
	
	private static ContactListMapper contactListMapper = null;
	
	/**
	 * Ein geschützter Konstruktor verhindert das erneute Erzeugen von weiteren
	 * Instanzen dieser Klasse.
	 */
	protected ContactListMapper() {
	}
	

	/**
	 * Methode zum Sicherstellen der Singleton-Eigenschaft. Diese sorgt dafür,
	 * dass nur eine einzige Instanz der ContactListMapper-Klasse exsistiert.
	 * Aufgerufen wird die Klasse somit über ContactListMapper.contactListMapper() und
	 * nicht über den New-Operator.
	 * 
	 * @return contactListMapper
	 */
	
	public static ContactListMapper contactListMapper() {
		if (contactListMapper == null){
			contactListMapper = new ContactListMapper();
		}
		return contactListMapper;
	}
	
	/**
	 * Fügt ein ContactList-Objekt der Datenbank hinzu.
	 * 
	 * @param contactList
	 * @return contactList
	 */
	
	public ContactList insert(ContactList contactList) {
		/**
		 * DB-Verbindung holen.
		 */
		Connection con = DBConnection.connection();
	
		
		try {
			
			/**
			 * leeres SQL-Statement (JDBC) anlegen.
			 */
			Statement stmt = con.createStatement();
			/**
			 * Abfrage des zuletzt hinzugefuegten Primaerschluessel (id) in der SharedObject-Klasse. Es wird durch den Aufruf von "super.insert()" in der Superklasse SharedObjectMapper die
			 * aktuelle id um eins erhoeht. 
			 */
		
		
			contactList.setId(super.insert());
	
			/**
			 * SQL-Anweisung zum Einfügen des neuen ContactList-Tupels in die
			 * Datenbank.
			 */
	
			
			stmt.executeUpdate("INSERT INTO contactlist (id, name, ownerId) VALUES " + "(" + contactList.getId() + ", '"
					+ contactList.getName() + "', " + contactList.getCreatorId()+ ")");
			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
			 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		return contactList;
	}
	
	/**
	 * Aktualisiert ein ContactList-Objekt in der Datenbank.
	 * 
	 * @param contactList
	 * @return contactList
	 */
	
	public ContactList update(ContactList contactList){
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			
			/**
			 * SQL-Anweisung zum Aktualisieren des übergebenen Datensatzes in
			 * der Datenbank.
			 */
			stmt.executeUpdate("UPDATE contactlist SET name='" + contactList.getName() + "' WHERE id=" + contactList.getId());
		}
		/**
		 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
		 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
		 * ausgegeben, was passiert ist und wo im Code es passiert ist.
		 */
		catch (SQLException e2) {
			e2.printStackTrace();
		}
		return contactList;
	}
	
	/**
	 * Löscht ein ContactList-Objekt aus der Datenbank.
	 * 
	 * @param contactList
	 */

	public void delete(ContactList contactList) {
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Löschen des übergebenen Datensatzes in der
			 * Datenbank.
			 */
			stmt.executeUpdate("DELETE FROM contactlist WHERE id=" + contactList.getId());
		}
		
		/**
	 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
	 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
	 * ausgegeben, was passiert ist und wo im Code es passiert ist.
	 */
	catch (SQLException e2) {
		e2.printStackTrace();
	}
	}
	/**
	 * Findet ein ContactList-Objekt anhand der übergebenen Id in der Datenbank.
	 * 
	 * @param id
	 * @return contactList
	 */
	
	public ContactList findById(int id){
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden des übergebenen Datensatzes, anhand der
			 * Id, in der Datenbank.
			 */
			ResultSet rs = stmt.executeQuery("SELECT id, ownerId name FROM contactlist WHERE id=" + id);
			/**
			 * Zu einem eindeutigen Wert exisitiert nur maximal ein
			 * Datenbank-Tupel, somit kann auch nur einer zurückgegeben werden.
			 * Es wird mit einer If-Abfragen geprüft, ob es für den angefragten
			 * Primärschlüssel ein DB-Tupel gibt.
			 */
			if (rs.next()) {
				ContactList contactList = new ContactList();
				contactList.setId(rs.getInt("id"));
				contactList.setName(rs.getString("name"));
				contactList.setCreatorId(rs.getInt("ownerId"));
				return contactList;			
		}
			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
			 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * Findet ein ContactList-Objekt anhand des übergebenen User-Id in der Datenbank.
	 * 
	 * @param userId
	 * @return ArrayList<ContactList>
	 */
	
	public ArrayList<ContactList> findByOwnerId(int userId){
		Connection con = DBConnection.connection();
		
		ArrayList<ContactList> result = new ArrayList<ContactList>();
		
		try{
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden des Datensatzes, nach dem gesuchten Namen, in der Datenbank, sortiert nach der Id.
			 */
			ResultSet rs = stmt.executeQuery("SELECT id, name, ownerId FROM contactlist " + "WHERE ownerId=" + userId);
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der
			 * Tabelle contactlist vorhanden ist, muss das Abfragen des ResultSet so
			 * oft erfolgen (while-Schleife), bis alle Tupel durchlaufen wurden.
			 * Die DB-Tupel werden in Java-Objekte transformiert und
			 * anschliessend der ArrayList hinzugefügt.
			 */
			
			while (rs.next()) {
				ContactList contactList = new ContactList();
				contactList.setId(rs.getInt("id"));
				contactList.setName(rs.getString("name"));
			    contactList.setCreatorId(rs.getInt("ownerId"));
				result.add(contactList);
			}
			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
			 * ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */	
		}catch (SQLException e2) {
			e2.printStackTrace();
	}
		return result;
	}
	
	/**
	 * Findet alle ContactList-Objekte in der Datenbank.
	 * 
	 * @return ArrayList<ContactList>
	 */
	
	public ArrayList<ContactList> findAllContactLists() {
		//DB-Verbindung holen
		Connection con = DBConnection.connection();

		ArrayList<ContactList> result = new ArrayList<ContactList>();
		
		try {
			// Leeres SQL-Statement (JDBC) anlegen
			Statement stmt = con.createStatement();
			
			// SQL-Anweisung zum Finden des übergebenen Datensatzes anhand der Id in der Datenbank
			ResultSet rs = stmt.executeQuery("SELECT id, name, ownerId FROM contactlist ORDER BY id");
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der
			 * Tabelle contactlist vorhanden ist, muss das Abfragen des ResultSet so
			 * oft erfolgen (while-Schleife), bis alle Tupel durchlaufen wurden.
			 * Die DB-Tupel werden in Java-Objekte transformiert und
			 * anschliessend der ArrayList hinzugefügt.
			 */

			while (rs.next()) {
				ContactList contactlist = new ContactList();
				contactlist.setId(rs.getInt("id"));
				contactlist.setName(rs.getString("name"));
				contactlist.setCreatorId(rs.getInt("ownerId"));
				result.add(contactlist);
			}
			/**
			 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
			 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		// Rückgabe der ArrayList
			return result;
	}
}
