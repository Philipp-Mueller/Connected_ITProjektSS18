package de.hdm.Connected.server.db;

import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Property;
import java.sql.*;
import java.util.ArrayList;

/**
 * Die Klasse PropertyMapper bildet Property-Objekte auf eine relationale
 * Datenbank ab. Hierzu wird eine Reihe von Methoden (insert, update, delete,
 * findBy) zur Verfügung gestellt, mit deren Hilfe z.B. Objekte gesucht,
 * erzeugt, modifiziert und gelöscht werden können.
 * 
 * @author Viktoriya
 */

public class PropertyMapper {

	/**
	 * Die Klasse PropertyMapper wird nur einmal instantiiert (Singleton). Damit
	 * diese Eigenschaft erfüllt werden kann, wird zunächst eine Variable mit
	 * dem Bezeichner static und dem Standardwert null erzeugt. Sie speichert
	 * die einzige Instanz der Klasse.
	 */
	private static PropertyMapper propertyMapper = null;

	/**
	 * Geschützter Konstruktor verhindert die Möglichkeit für das erneute
	 * Erzeugen von weiteren Instanzen dieser Klasse.
	 */
	protected PropertyMapper() {
	}

	/**
	 * Statische Methode zum Sicherstellen der Singleton-Eigenschaft. Sie sorgt
	 * dafür, dass nur eine einzige Instanz der PropertyMapper-Klasse
	 * exsistiert. Aufgerufen wird die Klasse somit über
	 * PropertyMapper.propertyMapper() und nicht über den New-Operator.
	 * 
	 * @return propertyMapper
	 */
	public static PropertyMapper propertyMapper() {
		if (propertyMapper == null) {
			propertyMapper = new PropertyMapper();
		}
		return propertyMapper;
	}

	/**
	 * Einfügen eines Property-Objekts in die Datenbank.
	 * 
	 * @param property
	 * @return property
	 */
	public Property insert(Property property) {
		
		/**
		 * DB-Verbindung holen
		 */
		Connection con = DBConnection.connection();
		try {
			/**
			 * Leeres SQL-Statement (JDBC) anlegen
			 */
			Statement stmt = con.createStatement();

			/**
			 * Statement ausfüllen und als Query an die Datenbank schicken
			 */
			ResultSet rs = stmt.executeQuery("SELECT MAX(id) as maxid FROM property");

			if (rs.next()) {
				
				/**
				 * Ergebnis-Tupel in Objekt umwandeln
				 */
				property.setBoId(rs.getInt("maxid") + 1);
			}

			stmt = con.createStatement();
			
			/**
			 * SQL-Anweisung zum Einfügen des neuen Property-Tupels in die Datenbank
			 */
			stmt.executeUpdate("INSERT INTO property (id, name) VALUES " + "(" + property.getBoId() + ",'"
					+ property.getName() + "')");

			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
			 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/**
		 * Rückgabe der Property
		 */
		return property;
	}

	/**
	 * Aktualisieren eines Property-Objekts in der Datenbank.
	 * 
	 * @param property
	 * @return property
	 */
	public Property update(Property property) {
		
		/**
		 * DB-Verbindung holen
		 */
		Connection con = DBConnection.connection();
		try {
			
			/**
			 * Leeres SQL-Statement (JDBC) anlegen
			 */
			Statement stmt = con.createStatement();

			/**
			 * SQL-Anweisung zum Aktualisieren des übergebenen Datensatzes in der Datenbank
			 */
			stmt.executeUpdate("UPDATE property SET name='" + property.getName() + "'WHERE id=" + property.getBoId());

			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
			 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/**
		 * Rückgabe der Property
		 */
		return property;
	}

	/**
	 * Löschen eines Property-Objekts aus der Datenbank.
	 * 
	 * @param property
	 */
	public void delete(Property property) {
		
		/**
		 * DB-Verbindung holen
		 */
		Connection con = DBConnection.connection();
		try {
			
			/**
			 * Leeres SQL-Statement (JDBC) anlegen
			 */
			Statement stmt = con.createStatement();

			/**
			 * SQL-Anweisung zum Löschen des übergebenen Datensatzes in der Datenbank
			 */
			stmt.executeUpdate("DELETE FROM property WHERE id=" + property.getBoId());

			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
			 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Suchen eines Property-Objekts anhand der übergebenen Id in der Datenbank.
	 * 
	 * @param id
	 * @return property
	 */
	public Property findById(int id) {
		
		/**
		 * DB-Verbindung holen
		 */
		Connection con = DBConnection.connection();
		try {
			
			/**
			 * Leeres SQL-Statement (JDBC) anlegen
			 */
			Statement stmt = con.createStatement();

			/**
			 * SQL-Anweisung zum Finden des übergebenen Datensatzes anhand der Id in der Datenbank
			 */
			ResultSet rs = stmt.executeQuery("SELECT id, name FROM property WHERE id=" + id);
			/**
			 * Da id Primärschlüssel ist, kann max. nur ein Tupel zurückgegeben
			 * werden. Es wird geprüft, ob ein Ergebnis vorliegt.
			 */
			if (rs.next()) {
				
				/**
				 * Ergebnis-Tupel in Objekt umwandeln
				 */
				Property property = new Property();
				property.setBoId(rs.getInt("id"));
				property.setName(rs.getString("name"));
				return property;
			}
			
			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
			 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Suchen aller Property-Objekte in der Datenbank.
	 * 
	 * @return ArrayList<Property>
	 */
	public ArrayList<Property> findAllProperties() {
		
		/**
		 * DB-Verbindung holen
		 */
		Connection con = DBConnection.connection();
		
		ArrayList<Property> result = new ArrayList<Property>();
		try {
			
			/**
			 * Leeres SQL-Statement (JDBC) anlegen
			 */
			Statement stmt = con.createStatement();
			
			/**
			 * SQL-Anweisung zum Finden aller Datensätze in der Datenbank, sortiert nach Id
			 */
			ResultSet rs = stmt.executeQuery("SELECT id, name FROM property ORDER BY id");
			
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der
			 * Tabelle property vorhanden ist, muss das Abfragen des ResultSet
			 * so oft erfolgen (while-Schleife), bis alle Tupel durchlaufen
			 * wurden. Die DB-Tupel werden in Java-Objekte transformiert und
			 * anschliessend der ArrayList hinzugefügt.
			 */
			while (rs.next()) {
				Property property = new Property();
				property.setBoId(rs.getInt("id"));
				property.setName(rs.getString("name"));
				result.add(property);
			}
			
			/**
			 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
			 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/**Rückgabe der ArrayList
		 * 
		 */
		return result;
	}
}