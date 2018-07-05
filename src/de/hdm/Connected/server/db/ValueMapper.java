package de.hdm.Connected.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.Connected.shared.bo.SharedObject;
import de.hdm.Connected.shared.bo.Value;

/**
 * Die Klasse ValueMapper bildet Value-Objekte auf eine relationale Datenbank
 * ab. Ebenfalls ist es moeglich aus Datenbank-Tupel Java-Objekte zu erzeugen.
 * 
 * Zur Verwaltung der Objekte implementiert die Mapper-Klasse entsprechende
 * Methoden (insert, search, delete, update).
 * 
 * Durch extends SharedObjectMapper wird die Vererbung von SharedObjects dargestellt und in der DB-Ebene verdeutlicht.
 * 
 * @author Burak
 */
public class ValueMapper {

	/**
	 * Die Klasse ValueMapper wird nur einmal instantiiert
	 * (Singleton-Eigenschaft). Damit diese Eigenschaft erfuellt werden kann,
	 * wird zunaechst eine Variable mit dem Schluesselwort static und dem
	 * Standardwert null erzeugt. Sie speichert die Instanz dieser Klasse.
	 */
	private static ValueMapper valueMapper = null;

	/**
	 * Ein geschuetzter Konstruktor verhindert das erneute erzeugen von weiteren
	 * Instanzen dieser Klasse.
	 */
	protected ValueMapper() {
	}

	/**
	 * Methode zum Sicherstellen der Singleton-Eigenschaft. Diese sorgt dafuer,
	 * dass nur eine einzige Instanz der ValueMapper-Klasse existiert.
	 * Aufgerufen wird die Klasse somit ueber ValueMapper.valueMapper() und
	 * nicht ueber den New-Operator.
	 * 
	 * @return valueMapper
	 */
	public static ValueMapper valueMapper() {
		if (valueMapper == null) {
			valueMapper = new ValueMapper();
		}
		return valueMapper;
	}

	/**
	 * Fuegt ein Value-Objekt der Datenbank hinzu.
	 * 
	 * @param value
	 * @return value
	 */
	public Value insert(Value value) {
		/**
		 * DB-Verbindung holen.
		 */
		Connection con = DBConnection.connection();
		
			
		

		try {
			/**
			 * auto-commit ausschalten um sicherzustellen dass beide Statements, also die ganze TRansaktion ausgeführt wird.
			 */
			
			con.setAutoCommit(false);			
			
			/**
			 * leeres SQL-Statement (JDBC) anlegen.
			 */
			Statement stmt = con.createStatement();
			/**
			 * Abfrage des zuletzt hinzugefuegten Primaerschluessel (id) in der SharedObject-Klasse. Es wird durch den Aufruf von "super.insert()" in der Superklasse SharedObjectMapper die
			 * aktuelle id um eins erhoeht. 
			 */
		

		    ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid FROM sharedobject");
			
		    
		    if(rs.next()){
				value.setBoId(rs.getInt("maxid")+1);
			}
		
		    
		    stmt = con.createStatement();
		    
			/**
			 * SQL-Anweisung zum Einfügen des neuen ContactList-Tupels in die
			 * Datenbank.
			 */
		    stmt.executeUpdate("INSERT INTO sharedobject (id) VALUES " + "(" + value.getBoId() + ")");
			

	
			stmt.executeUpdate("INSERT INTO value (id, name, propertyId, contactId, ownerId) VALUES (" + value.getBoId() + ", '"
					+ value.getName() + "', " + value.getPropertyID() + ", " + value.getContactID() +  ", " + value.getCreatorId() + ")");
			/**
			 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
			 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
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
		
		return value;
	}

	/**
	 * Aktualisiert ein Value-Objekt in der Datenbank.
	 * 
	 * @param value
	 * @return value
	 */
	public Value update(Value value) {
		Connection con = DBConnection.connection();

		try {
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Aktualisieren des uebergebenen Datensatzes in
			 * der Datenbank.
			 */
			stmt.executeUpdate("UPDATE value SET name='" + value.getName() + "', propertyId = '" + value.getPropertyID()
					+ "', contactId = '" + value.getContactID() + "' WHERE id= " + value.getBoId());
		}
		/**
		 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die
		 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
		 * ausgegeben, was passiert ist und wo im Code es passiert ist.
		 */
		catch (SQLException e2) {
			e2.printStackTrace();
		}
		return value;
	}

	/**
	 * Loescht ein Value-Objekt aus der Datenbank.
	 * 
	 * @param value
	 */
	public void delete(Value value) {
		Connection con = DBConnection.connection();

		try {			
			/**
			 * auto-commit ausschalten um sicherzustellen dass beide Statements, also die ganze TRansaktion ausgeführt wird.
			 */
			
			con.setAutoCommit(false);
			
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Loeschen des uebergebenen Datensatzes in der
			 * Datenbank.
			 */
			stmt.executeUpdate("DELETE FROM value WHERE id=" + value.getBoId());
			
			stmt.executeUpdate("DELETE FROM sharedobject WHERE id=" + value.getBoId());
			
			con.commit();
		
		}
		/**
		 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die
		 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
		 * ausgegeben, was passiert ist und wo im Code es passiert ist.
		 */
		catch (SQLException e2) {
			e2.printStackTrace();
		}try {
			con.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Findet ein Value-Objekt anhand der Übergebenen Id in der Datenbank.
	 * 
	 * @param id
	 * @return value
	 */
	public Value findById(int id) {
		Connection con = DBConnection.connection();

		try {
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden des uebergebenen Datensatzes, anhand der
			 * Id, in der Datenbank.
			 */
			ResultSet rs = stmt.executeQuery("SELECT id, name, propertyId, contactId, ownerId FROM value WHERE id=" + id);
			/**
			 * Zu einem Primaerschluessel exisitiert nur maximal ein
			 * Datenbank-Tupel, somit kann auch nur einer zurueckgegeben werden.
			 * Es wird mit einer If-Abfragen geprueft, ob es fuer den
			 * angefragten Primaerschluessel ein DB-Tupel gibt.
			 */
			if (rs.next()) {
				Value value = new Value();
				value.setBoId(rs.getInt("id"));
				value.setName(rs.getString("name"));
				value.setPropertyID(rs.getInt("propertyId"));
				value.setContactID(rs.getInt("contactId"));
				value.setCreatorId(rs.getInt("ownerId"));
				return value;
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
	 * Findet Value-Objekte anhand des uebergebenen bez in der Datenbank.
	 * 
	 * @param bez
	 * @return ArrayList<Value>
	 */
	public ArrayList<Value> findByValue(String bez) {
		Connection con = DBConnection.connection();

		ArrayList<Value> result = new ArrayList<Value>();
		try {
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden des Datensatzes, anhand des uebergebenen
			 * bez, in der Datenbank, sortiert nach der Id.
			 */
			ResultSet rs = stmt.executeQuery(
					"SELECT id, name, propertyId, contactId, ownerId FROM value WHERE bez LIKE '" + bez + "' ORDER BY id");

			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der
			 * Tabelle Value mit dem uebergebenen bez vorhanden ist, muss das
			 * Abfragen des ResultSet so oft erfolgen (while-Schleife), bis alle
			 * Tupel durchlaufen wurden. Die DB-Tupel werden in Java-Objekte
			 * transformiert und anschliessend der ArrayList hinzugefuegt.
			 */
			while (rs.next()) {
				Value value = new Value();
				value.setBoId(rs.getInt("id"));
				value.setName(rs.getString("name"));
				value.setPropertyID(rs.getInt("propertyId"));
				value.setContactID(rs.getInt("contactId"));
				value.setCreatorId(rs.getInt("ownerId"));
				result.add(value);
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
	 * Findet ein Value-Objekt anhand des uebergebenen ContactId in der
	 * Datenbank.
	 * 
	 * @param contactid
	 * @return ArrayList<Value>
	 */

	public ArrayList<Value> findByContactId(int contactId) {
		Connection con = DBConnection.connection();

		ArrayList<Value> result = new ArrayList<Value>();
		try {
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden aller Datensaetze, anhand der ContactId,
			 * in der Datenbank, sortiert nach der Id.
			 */
			ResultSet rs = stmt.executeQuery("SELECT id, name, propertyId, contactId, ownerId FROM value WHERE contactId = "
					+ contactId + " ORDER BY id");
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der
			 * Tabelle Value mit dem uebergebenen ContactID vorhanden ist, muss
			 * das Abfragen des ResultSet so oft erfolgen (while-Schleife), bis
			 * alle Tupel durchlaufen wurden. Die DB-Tupel werden in
			 * Java-Objekte transformiert und anschliessend der ArrayList
			 * hinzugefuegt.
			 */
			while (rs.next()) {
				Value value = new Value();
				value.setBoId(rs.getInt("id"));
				value.setName(rs.getString("name"));
				value.setPropertyID(rs.getInt("propertyId"));
				value.setContactID(rs.getInt("contactId"));
				value.setCreatorId(rs.getInt("ownerId"));
				result.add(value);
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
	 * Findet ein Value-Objekt anhand des uebergebenen PropertyId in der
	 * Datenbank.
	 * 
	 * @param propertyId
	 * @return ArrayList<Value>
	 */

	public ArrayList<Value> findByProperty(int propertyId) {
		Connection con = DBConnection.connection();

		ArrayList<Value> result = new ArrayList<Value>();
		try {
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden aller Datensaetze, anhand der
			 * PropertyId, in der Datenbank, sortiert nach der Id.
			 */
			ResultSet rs = stmt.executeQuery("SELECT id, name, propertyId, contactId, ownerId FROM value WHERE propertyId = "
					+ propertyId + " ORDER BY id");
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der
			 * Tabelle Value mit dem uebergebenen PropertyId vorhanden ist, muss
			 * das Abfragen des ResultSet so oft erfolgen (while-Schleife), bis
			 * alle Tupel durchlaufen wurden. Die DB-Tupel werden in
			 * Java-Objekte transformiert und anschliessend der ArrayList
			 * hinzugefuegt.
			 */
			while (rs.next()) {
				Value value = new Value();
				value.setBoId(rs.getInt("id"));
				value.setName(rs.getString("name"));
				value.setPropertyID(rs.getInt("propertyId"));
				value.setContactID(rs.getInt("contactId"));
				value.setCreatorId(rs.getInt("ownerId"));
				result.add(value);
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
	 * Findet ein Value-Objekt anhand des uebergebenen PropertyId und
	 * Beschreibung in der Datenbank.
	 * 
	 * @param propertyId
	 * @return ArrayList<Value>
	 */

	public ArrayList<Value> findByPropertyAndDescription(int propertyId, String valueDescription) {
		Connection con = DBConnection.connection();

		ArrayList<Value> result = new ArrayList<Value>();
		try {
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden aller Datensaetze, anhand der
			 * PropertyId, in der Datenbank, sortiert nach der Id.
			 */
			ResultSet rs = stmt.executeQuery("SELECT id, name, propertyId, contactId, creatorId FROM value WHERE propertyId = '"
					+ propertyId + "' AND LOWER(name) LIKE '%" + valueDescription.toLowerCase() + "%'" + "ORDER BY id");
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der
			 * Tabelle Value mit dem uebergebenen PropertyId vorhanden ist, muss
			 * das Abfragen des ResultSet so oft erfolgen (while-Schleife), bis
			 * alle Tupel durchlaufen wurden. Die DB-Tupel werden in
			 * Java-Objekte transformiert und anschliessend der ArrayList
			 * hinzugefuegt.
			 */
			while (rs.next()) {
				Value value = new Value();
				value.setBoId(rs.getInt("id"));
				value.setName(rs.getString("name"));
				value.setPropertyID(rs.getInt("propertyId"));
				value.setContactID(rs.getInt("contactId"));
				value.setCreatorId(rs.getInt("ownerId"));
				result.add(value);
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
	 * Findet alle Value-Objekte in der Datenbank.
	 * 
	 * @return ArrayList<Value>
	 */

	public ArrayList<Value> findAllValues() {
		Connection con = DBConnection.connection();

		ArrayList<Value> result = new ArrayList<Value>();
		try {
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden aller Datensaetze, anhand der
			 * PropertyId, in der Datenbank, sortiert nach der Id.
			 */
			ResultSet rs = stmt.executeQuery("SELECT id, description, propertyId, contactId, ownerId FROM value ORDER BY id");

			while (rs.next()) {
				Value value = new Value();
				value.setBoId(rs.getInt("id"));
				value.setName(rs.getString("description"));
				value.setPropertyID(rs.getInt("propertyId"));
				value.setContactID(rs.getInt("contactId"));
				value.setCreatorId(rs.getInt("ownerId"));
				result.add(value);
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