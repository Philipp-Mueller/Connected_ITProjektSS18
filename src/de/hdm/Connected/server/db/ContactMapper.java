package de.hdm.Connected.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.Connected.shared.bo.Contact;

/**
 * Die Klasse ContactMapper bildet Contact-Objekte auf eine relationale Datenbank
 * ab. Ebenfalls ist es moeglich aus Datenbank-Tupel Java-Objekte zu erzeugen.
 * 
 * Zur Verwaltung der Objekte implementiert die Mapper-Klasse entsprechende
 * Methoden (insert, search, delete, update).
 * 
 * @author Burak
 */
public class ContactMapper {

	/**
	 * Die Klasse ContactMapper wird nur einmal instantiiert
	 * (Singleton-Eigenschaft). Damit diese Eigenschaft erfuellt werden kann,
	 * wird zunaechst eine Variable mit dem Schluesselwort static und dem
	 * Standardwert null erzeugt. Sie speichert die Instanz dieser Klasse.
	 */
	private static ContactMapper contactMapper = null;

	/**
	 * Ein geschuetzter Konstruktor verhindert das erneute erzeugen von weiteren
	 * Instanzen dieser Klasse.
	 */
	protected ContactMapper() {
	}

	/**
	 * Methode zum Sicherstellen der Singleton-Eigenschaft. Diese sorgt dafuer,
	 * dass nur eine einzige Instanz der ContactMapper-Klasse existiert.
	 * Aufgerufen wird die Klasse somit ueber ContactMapper.contactMapper() und
	 * nicht ueber den New-Operator.
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
	 * Fuegt ein Contact-Objekt der Datenbank hinzu.
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
			 * leeres SQL-Statement (JDBC) anlegen.
			 */
			Statement stmt = con.createStatement();
			/**
			 * Abfrage des zuletzt hinzugefuegten Primaerschluessel (id). Die
			 * aktuelle id wird um eins erhoeht. Statement ausfuellen und als
			 * Query an die Datenbank senden.
			 */
			ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid FROM contact");

			if (rs.next()) {
				contact.setId(rs.getInt("maxid") + 1);
			}
			stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Einfuegen des neuen Contact-Tupels in die
			 * Datenbank.
			 */
			stmt.executeUpdate("INSERT INTO contact (id, name, contactlistid) VALUES (" + contact.getId()
					+ ", '" + contact.getName() + "', '" + contactlistID.getID() + "')");
			/**
			 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
			 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e2) {
			e2.printStackTrace();
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
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Aktualisieren des uebergebenen Datensatzes in
			 * der Datenbank.
			 */
			stmt.executeUpdate("UPDATE contact SET name='" + contact.getName() + "', contactlistid = '" + contact.getContactlistID() + "' WHERE id= " + contact.getId());
		}
		/**
		 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die
		 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
		 * ausgegeben, was passiert ist und wo im Code es passiert ist.
		 */
		catch (SQLException e2) {
			e2.printStackTrace();
		}
		return contact;
	}

	/**
	 * Loescht ein Contact-Objekt aus der Datenbank.
	 * 
	 * @param contact
	 */
	public void delete(Contact contact) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Loeschen des uebergebenen Datensatzes in der
			 * Datenbank.
			 */
			stmt.executeUpdate("DELETE FROM contact WHERE id=" + contact.getId());
		}
		/**
		 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die
		 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
		 * ausgegeben, was passiert ist und wo im Code es passiert ist.
		 */
		catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

	/**
	 * Findet ein Contact-Objekt anhand der Übergebenen Id in der Datenbank.
	 * 
	 * @param id
	 * @return contact
	 */
	public Contact findById(int id) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden des uebergebenen Datensatzes, anhand der
			 * Id, in der Datenbank.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT id,contactlistid, name FROM contact WHERE id=" + id);
			/**
			 * Zu einem Primaerschluessel exisitiert nur maximal ein
			 * Datenbank-Tupel, somit kann auch nur einer zurueckgegeben werden.
			 * Es wird mit einer If-Abfragen geprueft, ob es fuer den
			 * angefragten Primaerschluessel ein DB-Tupel gibt.
			 */
			if (rs.next()) {
				Contact contact = new contact();
				contact.setId(rs.getInt("id"));
				contact.setContactlistID(rs.getInt("contactlistid"))
				contact.setName(rs.getString("name"));
				
				
				return contact;
			}
			/**
			 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
			 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
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
		Connection con = DBConnection.connection();

		ArrayList<Contact> result = new ArrayList<Contact>();
		try {
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden aller Datensaetze in der Datenbank,
			 * sortiert nach der Id.
			 */
			ResultSet rs = stmt.executeQuery("SELECT id, contactlistid, name FROM contact ORDER BY id");
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der
			 * Tabelle contact vorhanden ist, muss das Abfragen des ResultSet so
			 * oft erfolgen (while-Schleife), bis alle Tupel durchlaufen wurden.
			 * Die DB-Tupel werden in Java-Objekte transformiert und
			 * anschliessend der ArrayList hinzugefuegt.
			 */

			while (rs.next()) {
				Contact contact = new contact();
				contact.setId(rs.getInt("id"));
				contact.setContactlistID(rs.getInt("contactlistid"))
				contact.setName(rs.getString("name"));
				
				result.add(contact);
			}
			/**
			 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
			 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return result;
	}

	/**
	 * Findet Contact-Objekte anhand des uebergebenen Namens in der Datenbank.
	 * 
	 * @param name
	 * @return ArrayList<Contact>
	 */
	public ArrayList<Contact> findByName(String name) {
		Connection con = DBConnection.connection();

		ArrayList<Contact> result = new ArrayList<Contact>();
		try {
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden des Datensatzes, anhand des uebergebenen
			 * Namens, in der Datenbank, sortiert nach der Id.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT id, contactlistid, name FROM contact WHERE name LIKE '" + name
							+ "' ORDER BY id");
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der
			 * Tabelle Contact mit dem uebergebenen Namen vorhanden ist, muss das
			 * Abfragen des ResultSet so oft erfolgen (while-Schleife), bis alle
			 * Tupel durchlaufen wurden. Die DB-Tupel werden in Java-Objekte
			 * transformiert und anschliessend der ArrayList hinzugefuegt.
			 */
			while (rs.next()) {
				Contact contact = new Contact();
				contact.setId(rs.getInt("id"));
				contact.setContactlistID(rs.getInt("contactlistid"))
				contact.setName(rs.getString("name"));
				
				result.add(contact);
			}
			/**
			 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
			 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return result;
	}

	/**
	 * Findet ein Contact-Objekt anhand des uebergebenen Vornamens in der
	 * Datenbank.
	 * 
	 * @param firstName
	 * @return ArrayList<Contact>
	 */
	public ArrayList<Contact> findByUserId(int id) {
		Connection con = DBConnection.connection();

		ArrayList<Contact> result = new ArrayList<Contact>();
		try {
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden aller Datensaetze, anhand des Vornamens,
			 * in der Datenbank, sortiert nach der Id.
			 */
			ResultSet rs = stmt.executeQuery("SELECT id, contactlistid, name FROM contact WHERE name LIKE '"
					+ id + "' ORDER BY id");
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der
			 * Tabelle Contact mit dem uebergebenen Vornamen vorhanden ist, muss
			 * das Abfragen des ResultSet so oft erfolgen (while-Schleife), bis
			 * alle Tupel durchlaufen wurden. Die DB-Tupel werden in
			 * Java-Objekte transformiert und anschliessend der ArrayList
			 * hinzugefuegt.
			 */
			while (rs.next()) {
				Contact contact = new Contact();
				contact.setId(rs.getInt("id"));
				contact.setContactlistID(rs.getInt("contactlistid"))
				contact.setName(rs.getString("name"));
				
				result.add(contact);
			}
			/**
			 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
			 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return result;
	}

}