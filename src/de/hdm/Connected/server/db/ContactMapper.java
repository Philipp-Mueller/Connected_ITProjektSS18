package de.hdm.Connected.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Permission;

/**
 * Die Klasse ContactMapper bildet Contact-Objekte auf eine relationale
 * Datenbank ab. Ebenfalls ist es möglich aus Datenbank-Tupel Java-Objekte zu
 * erzeugen.
 * 
 * Zur Verwaltung der Objekte implementiert die Mapper-Klasse entsprechende
 * Methoden (insert, search, delete, update).
 * 
 * Durch extends SharedObjectMapper wird die Vererbung von SharedObjects dargestellt und in der DB-Ebene verdeutlicht.
 * 
 * @author Burak
 */
public class ContactMapper {

	/**
	 * Die Klasse ContactMapper wird nur einmal instantiiert
	 * (Singleton-Eigenschaft). Damit diese Eigenschaft erfüllt werden kann, wird
	 * zunächst eine Variable mit dem Schlüsselwort static und dem Standardwert
	 * null erzeugt. Sie speichert die Instanz dieser Klasse.
	 */
	private static ContactMapper contactMapper = null;

	/**
	 * Ein geschützter Konstruktor verhindert das erneute erzeugen von weiteren
	 * Instanzen dieser Klasse.
	 */
	protected ContactMapper() {
	}

	/**
	 * Methode zum Sicherstellen der Singleton-Eigenschaft. Diese sorgt dafür, dass
	 * nur eine einzige Instanz der ContactMapper-Klasse existiert. Aufgerufen wird
	 * die Klasse somit über ContactMapper.contactMapper() und nicht über den
	 * New-Operator.
	 * 
	 * @return contactMapper
	 */
	public static ContactMapper contactMapper() {
		if (contactMapper == null) {
			contactMapper = new ContactMapper();
		}
		return contactMapper;
	}

	/**
	 * Fügt ein Contact-Objekt der Datenbank hinzu.
	 * 
	 * @param contact
	 * @return contact
	 */
	public Contact insert(Contact contact) {
		/**
		 * DB-Verbindung holen.
		 */
		Connection con = DBConnection.connection();

		try {
			/**
			 * Auto-commit ausschalten, um sicherzustellen, dass beide Statements, also die ganze Ttansaktion ausgeführt wird.
			 */
			
			con.setAutoCommit(false);
			
			/**
			 * leeres SQL-Statement (JDBC) anlegen.
			 */
			Statement stmt = con.createStatement();
			
			/**
			 * Abfrage des zuletzt hinzugefügten Primaerschlüssel (id) in der SharedObject-Klasse. Es wird durch den Aufruf von "super.insert() in der Superklasse SharedObjectMapper die
			 * aktuelle id um eins erhöht. 
			 */
		    ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid FROM sharedobject");
			
		    if(rs.next()){
				contact.setBoId(rs.getInt("maxid")+1);
			}
		    
		    stmt = con.createStatement();
		    
			/**
			 * SQL-Anweisung zum Einfügen des neuen Contact-Tupels in die Datenbank.
			 */
		    stmt.executeUpdate("INSERT INTO sharedobject (id) VALUES " + "(" + contact.getBoId() + ")");
			
			stmt.executeUpdate("INSERT INTO contact (id, prename, surname, ownerId, creationDate, modificationDate) VALUES (" + contact.getBoId() + ", '"
					+ contact.getPrename() + "', '" + contact.getSurname() + "', " + contact.getCreatorId() + ", '" + contact.getCreationDate() +"', '"+ contact.getModificationDate() + "')");
			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die Fehlermeldung
			 * genauer zu analyisieren. Es werden Informationen dazu ausgegeben, was
			 * passiert ist und wo im Code es passiert ist.
			 */
			con.commit();
		
		} catch (SQLException e2) {
			e2.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return contact;
	}

	/**
	 * Aktualisiert ein Contact-Objekt in der Datenbank.
	 * 
	 * @param contact
	 * @return contact
	 */
	public Contact update(Contact contact) {
		/**
		 * DB-Verbindung holen.
		 */
		Connection con = DBConnection.connection();
		
		/**
		 * Timestamp erzeugen
		 */
		Timestamp currentTime = new Timestamp (System.currentTimeMillis());
		
		try {
<<<<<<< HEAD
			
=======
			con.setAutoCommit(true);
>>>>>>> master
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Aktualisieren des uebergebenen Datensatzes in der
			 * Datenbank.
			 */
			stmt.executeUpdate("UPDATE contact SET prename='" + contact.getPrename() + "', surname= '"
					+ contact.getSurname() + "', modificationDate= '"+ currentTime + "' WHERE id= " + contact.getBoId());
		}
		
		/**
		 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die Fehlermeldung
		 * genauer zu analyisieren. Es werden Informationen dazu ausgegeben, was
		 * passiert ist und wo im Code es passiert ist.
		 */
		catch (SQLException e2) {
			e2.printStackTrace();
		}
		return contact;
	}

	/**
	 * Löscht ein Contact-Objekt aus der Datenbank.
	 * 
	 * @param contact
	 */
	public void delete(Contact contact) {
		/**
		 * DB-Verbindung holen.
		 */
		Connection con = DBConnection.connection();

		try {
			/**
			 * Auto-commit ausschalten, um sicherzustellen dass beide Statements, also die ganze TRansaktion ausgeführt wird.
			 */
			
			con.setAutoCommit(false);
			
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Löschen des übergebenen Datensatzes in der Datenbank.
			 */
			stmt.executeUpdate("DELETE FROM contact WHERE id=" + contact.getBoId());
			
			stmt.executeUpdate("DELETE FROM sharedobject WHERE id=" + contact.getBoId());
			
			con.commit();
		
		}
		
		/**
		 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die Fehlermeldung
		 * genauer zu analyisieren. Es werden Informationen dazu ausgegeben, was
		 * passiert ist und wo im Code es passiert ist.
		 */
		catch (SQLException e2) {
			e2.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Findet ein Contact-Objekt anhand der übergebenen Id in der Datenbank.
	 * 
	 * @param id
	 * @return contact
	 */
	public Contact findById(int id) {
		/**
		 * DB-Verbindung holen.
		 */
		Connection con = DBConnection.connection();

		try {
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden des übergebenen Datensatzes anhand der Id in der
			 * Datenbank.
			 */
			ResultSet rs = stmt.executeQuery("SELECT id, prename, surname, ownerId, creationDate, modificationDate FROM contact WHERE id=" + id);
			/**
			 * Zu einem Primaerschlüssel exisitiert nur maximal ein Datenbank-Tupel, somit
			 * kann auch nur einer zurückgegeben werden. Es wird mit einer If-Abfragen
			 * geprüft, ob es für den angefragten Primärschlüssel ein DB-Tupel gibt.
			 */
			if (rs.next()) {
				Contact contact = new Contact();
				contact.setBoId(rs.getInt("id"));
				contact.setPrename(rs.getString("prename"));
				contact.setSurname(rs.getString("surname"));
				contact.setCreatorId(rs.getInt("ownerId"));
				contact.setCreationDate(rs.getDate("creationDate"));
				contact.setModificationDate(rs.getDate("modificationDate"));
				return contact;
			}
			/**
			 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die Fehlermeldung
			 * genauer zu analyisieren. Es werden Informationen dazu ausgegeben, was
			 * passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return null;
	}

	/**
	 * Findet alle Contact-Objekte in der Datenbank.
	 * 
	 * @return ArrayList<Contact>
	 */
	public ArrayList<Contact> findAll() {
		/**
		 * DB-Verbindung holen.
		 */
		Connection con = DBConnection.connection();

		ArrayList<Contact> result = new ArrayList<Contact>();
		try {
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden aller Datensätze in der Datenbank, sortiert nach
			 * der Id.
			 */
			ResultSet rs = stmt.executeQuery("SELECT id, prename, surname, ownerid, creationDate, modificationDate FROM contact ORDER BY prename");
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der Tabelle contact
			 * vorhanden ist, muss das Abfragen des ResultSet so oft erfolgen
			 * (while-Schleife), bis alle Tupel durchlaufen wurden. Die DB-Tupel werden in
			 * Java-Objekte transformiert und anschliessend der ArrayList hinzugefuegt.
			 */

			while (rs.next()) {
				Contact contact = new Contact();
				contact.setBoId(rs.getInt("id"));
				contact.setPrename(rs.getString("prename"));
				contact.setSurname(rs.getString("surname"));
				contact.setCreatorId(rs.getInt("ownerid"));
				contact.setCreationDate(rs.getDate("creationDate"));
				contact.setModificationDate(rs.getDate("modificationDate"));
				result.add(contact);
			}
			/**
			 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die Fehlermeldung
			 * genauer zu analyisieren. Es werden Informationen dazu ausgegeben, was
			 * passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return result;
	}

	/**
	 * Findet Contact-Objekte anhand des uebergebenen Vornamens in der Datenbank.
	 * 
	 * @param prename
	 * @return ArrayList<Contact>
	 */
	public ArrayList<Contact> findByPrename(String prename) {
		/**
		 * DB-Verbindung holen.
		 */
		Connection con = DBConnection.connection();

		ArrayList<Contact> result = new ArrayList<Contact>();
		try {
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden des Datensatzes, anhand des uebergebenen Namens, in
			 * der Datenbank, sortiert nach der Id.
			 */
			ResultSet rs = stmt.executeQuery(
					"SELECT id, prename, surname FROM contact WHERE prename LIKE '" + prename + "' ORDER BY id");
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der Tabelle Contact
			 * mit dem uebergebenen Namen vorhanden ist, muss das Abfragen des ResultSet so
			 * oft erfolgen (while-Schleife), bis alle Tupel durchlaufen wurden. Die
			 * DB-Tupel werden in Java-Objekte transformiert und anschliessend der ArrayList
			 * hinzugefuegt.
			 */
			while (rs.next()) {
				Contact contact = new Contact();
				contact.setBoId(rs.getInt("id"));
				contact.setPrename(rs.getString("prename"));
				contact.setSurname(rs.getString("surname"));
				result.add(contact);
			}
			/**
			 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die Fehlermeldung
			 * genauer zu analyisieren. Es werden Informationen dazu ausgegeben, was
			 * passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		/**
		 * Rückgabe der ArrayList
		 */
		return result;
	}

	/**
	 * Findet Contact-Objekte anhand des uebergebenen Namens in der Datenbank.
	 * 
	 * @param surname
	 * @return ArrayList<Contact>
	 */
	public ArrayList<Contact> findBySurname(String surname) {
		/**
		 * DB-Verbindung holen.
		 */
		Connection con = DBConnection.connection();

		ArrayList<Contact> result = new ArrayList<Contact>();
		try {
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden des Datensatzes, anhand des übergebenen Namens in
			 * der Datenbank, sortiert nach der Id.
			 */
			ResultSet rs = stmt.executeQuery(
					"SELECT id, prename, surname FROM contact WHERE surname LIKE '" + surname + "' ORDER BY id");
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der Tabelle Contact
			 * mit dem übergebenen Namen vorhanden ist, muss das Abfragen des ResultSet so
			 * oft erfolgen (while-Schleife), bis alle Tupel durchlaufen wurden. Die
			 * DB-Tupel werden in Java-Objekte transformiert und anschliessend der ArrayList
			 * hinzugefügt.
			 */
			while (rs.next()) {
				Contact contact = new Contact();
				contact.setBoId(rs.getInt("id"));
				contact.setPrename(rs.getString("prename"));
				contact.setSurname(rs.getString("surname"));
				result.add(contact);
			}
			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die Fehlermeldung
			 * genauer zu analyisieren. Es werden Informationen dazu ausgegeben, was
			 * passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		/**
		 * Rückgabe der ArrayList
		 */
		return result;
	}

	/**
	 * Suchen ein Contact-Objekt anhand der übergebenen ContactListId in der
	 * Datenbank.
	 * 
	 * @param contactListID
	 * @return ArrayList<Contact>
	 */

	public ArrayList<Contact> findByContactListId(int contactListID) {
		/**
		 * DB-Verbindung holen.
		 */
		Connection con = DBConnection.connection();

		ArrayList<Contact> result = new ArrayList<Contact>();

		try {
<<<<<<< HEAD
			/**
			 * Leeres SQL-Statement (JDBC) anlegen
			 */
=======
			con.setAutoCommit(true);
			// Leeres SQL-Statement (JDBC) anlegen
>>>>>>> master
			Statement stmt = con.createStatement();

			/**
			 * SQL-Anweisung zum Finden des übergebenen Datensatzes anhand der ContactListId in der Datenbank
			 */
			ResultSet rs = stmt.executeQuery(
					"SELECT id, prename, surname  FROM contact " + " WHERE contactListID=" + contactListID);
			
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der Tabelle
			 * permission vorhanden ist, muss das Abfragen des ResultSet so oft erfolgen
			 * (while-Schleife), bis alle Tupel durchlaufen wurden. Die DB-Tupel werden in
			 * Java-Objekte transformiert und anschliessend der ArrayList hinzugefügt.
			 */
			while (rs.next()) {
				Contact contact = new Contact();
				contact.setBoId(rs.getInt("id"));
				contact.setPrename(rs.getString("prename"));
				contact.setSurname(rs.getString("surname"));
				result.add(contact);
			}
			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die Fehlermeldung
			 * genauer zu analyisieren. Es werden Informationen dazu ausgegeben, was
			 * passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/**
		 * Rückgabe der ArrayList
		 */
		return result;
	}

	/**
	 * Suchen eines Contact-Objekts anhand der übergebenen UserID in der Datenbank.
	 * 
	 * @param userID
	 * @return ArrayList<Contact>
	 */
	public ArrayList<Contact> findByOwnerId(int userID) {
		/**DB-Verbindung holen
		 * 
		 */
		Connection con = DBConnection.connection();

		ArrayList<Contact> result = new ArrayList<Contact>();

		try {
<<<<<<< HEAD
			/**Leeres SQL-Statement (JDBC) anlegen
			 * 
			 */
=======
			con.setAutoCommit(true);
			// Leeres SQL-Statement (JDBC) anlegen
>>>>>>> master
			Statement stmt = con.createStatement();

			/**SQL-Anweisung zum Finden des übergebenen Datensatzes anhand der userId in der Datenbank
			 * 
			 */
			ResultSet rs = stmt.executeQuery("SELECT id , prename, surname, ownerid, creationDate, modificationDate FROM contact WHERE ownerid =" + userID);
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der Tabelle
			 * permission vorhanden ist, muss das Abfragen des ResultSet so oft erfolgen
			 * (while-Schleife), bis alle Tupel durchlaufen wurden. Die DB-Tupel werden in
			 * Java-Objekte transformiert und anschliessend der ArrayList hinzugefügt.
			 */

			while (rs.next()) {
				Contact contact = new Contact();
				contact.setBoId(rs.getInt("id"));
				contact.setPrename(rs.getString("prename"));
				contact.setSurname(rs.getString("surname"));
				contact.setCreatorId(rs.getInt("ownerid"));
				contact.setCreationDate(rs.getDate("creationDate"));
				contact.setModificationDate(rs.getDate("modificationDate"));
				result.add(contact);
				
			}
				/**
				 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die Fehlermeldung
				 * genauer zu analyisieren. Es werden Informationen dazu ausgegeben, was
				 * passiert ist und wo im Code es passiert ist.
				 */
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/**Rückgabe der ArrayList
		 * 
		 */
		return result;
	}
	
	/**
	 * Findet Contact-Objekte anhand des uebergebenen Value in der Datenbank.
	 * 
	 * @param value
	 * @return ArrayList<Contact>
	 */
	public ArrayList<Contact> findByValue(String value) {
		/**
		 * DB-Verbindung holen
		 */
		Connection con = DBConnection.connection();

		ArrayList<Contact> result = new ArrayList<Contact>();
		try {
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden des Datensatzes, anhand des übergebenen Value in
			 * der Datenbank, sortiert nach der Id.
			 */
			ResultSet rs = stmt.executeQuery(
					"SELECT id, prename, surname FROM contact WHERE value LIKE '" + value + "' ORDER BY id");
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der Tabelle Contact
			 * mit dem übergebenen Namen vorhanden ist, muss das Abfragen des ResultSet so
			 * oft erfolgen (while-Schleife), bis alle Tupel durchlaufen wurden. Die
			 * DB-Tupel werden in Java-Objekte transformiert und anschliessend der ArrayList
			 * hinzugefuegt.
			 */
			while (rs.next()) {
				Contact contact = new Contact();
				contact.setBoId(rs.getInt("id"));
				contact.setPrename(rs.getString("prename"));
				contact.setSurname(rs.getString("surname"));
				result.add(contact);
			}
			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die Fehlermeldung
			 * genauer zu analyisieren. Es werden Informationen dazu ausgegeben, was
			 * passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		/**
		 * Rückgabe der ArrayList
		 */
		return result;
	}
	
	/**
	 * Aktualisieren des Modifizierungsdatums, wenn eine Eigenschaft geändert wurde oder neu hinzugefügt.
	 * 
	 * @param contactId
	 */
	
	public void updateContactModificationDate(int contactId){
		/**
		 * DB-Verbindung holen
		 */
		Connection con = DBConnection.connection();
		/**
		 * Modifizierungsdatum erzeugen
		 */
		java.sql.Date currentTime = new java.sql.Date(System.currentTimeMillis());
		
		try {
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Aktualisieren des übergebenen Datensatzes in der
			 * Datenbank.
			 */
			stmt.executeUpdate("UPDATE contact SET modificationDate='" + currentTime + "' WHERE id= " + contactId);
		}
		/**
		 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die Fehlermeldung
		 * genauer zu analyisieren. Es werden Informationen dazu ausgegeben, was
		 * passiert ist und wo im Code es passiert ist.
		 */
		catch (SQLException e2) {
			e2.printStackTrace();
		}
	}
}