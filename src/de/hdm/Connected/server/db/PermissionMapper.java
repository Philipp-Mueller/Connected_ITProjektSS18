package de.hdm.Connected.server.db;

import de.hdm.Connected.shared.bo.Permission;
import java.util.ArrayList;
import java.sql.*;

/**
 * Die Klasse PermissionMapper bildet Permission-Objekte auf eine relationale 
 * Datenbank ab. Hierzu wird eine Reihe von Methoden (insert, update, delete, findBy)
 * zur Verfügung gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert 
 * und gelöscht werden können.
 * 
 * @author Viktoriya
 */

public class PermissionMapper {
	
	/**
	 * Die Klasse PermissionMapper wird nur einmal instantiiert (Singleton).
	 * Damit diese Eigenschaft erfüllt werden kann, wird zunächst
	 * eine Variable mit dem Bezeichner static und dem Standardwert
	 * null erzeugt. Sie speichert die einzige Instanz der Klasse.
	 */
	
	public static PermissionMapper permissionMapper = null;
	
	/**
	 * Geschützter Konstruktor verhindert die Möglichkeit für das erneute 	 
	 * Erzeugen von weiteren Instanzen dieser Klasse.
	 */
	
	protected PermissionMapper() {
	}
	
	/**
	 * Statische Methode zum Sicherstellen der Singleton-Eigenschaft. Sie sorgt dafür,
	 * dass nur eine einzige Instanz der PermissionMapper-Klasse exsistiert.
	 * Aufgerufen wird die Klasse somit über PermissionMapper.permissionMapper() und
	 * nicht über den New-Operator.
	 * 
	 * @return permissionMapper
	 */
	
	public static PermissionMapper permissionMapper() {
		if ( permissionMapper == null){
			permissionMapper = new PermissionMapper();
		}
		return permissionMapper;
	}
	
	/**
	 * Einfügen eines Permission-Objekts in die Datenbank.
	 * 
	 * @param permission
	 * @return permission
	 */
	
	public Permission insert (Permission permission) {
		//DB-Verbindung holen
		Connection con = DBConnection.connection();
	
		try {
			// Leeres SQL-Statement (JDBC) anlegen
			Statement stmt = con.createStatement();
			
			// Statement ausfüllen und als Query an die Datenbank schicken
			ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid FROM permission");
		
			if (rs.next()) {
				// Ergebnis-Tupel in Objekt umwandeln
				permission.setId(rs.getInt("maxid") + 1);
			}
			stmt = con.createStatement();
			// SQL-Anweisung zum Einfügen des neuen Permission-Tupels in die Datenbank
			stmt.executeUpdate("INSERT INTO permission (id, businessObjectID, userID, shareUserID) VALUES " + "(" + permission.getId() + ", '"
					+ permission.getBusinessObjectID() + ", '"+ permission.getUserID() + ", '" + permission.getShareUserID() + "')");
			
			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
			 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Rückgabe der Permission
		return permission;
	}	
		
	/**
	 * Aktualisieren eines Permission-Objekts in der Datenbank.
	 * 
	 * @param permission
	 * @return permission
	 */
	
	public Permission update (Permission permission) {
		// DB-Verbindung holen
			Connection con = DBConnection.connection();

			try {
				// Leeres SQL Statement anlegen
				Statement stmt = con.createStatement();
				
				// SQL-Anweisung zum Aktualisieren des übergebenen Datensatzes in der Datenbank
				stmt.executeUpdate("UPDATE permission  SET" + "BusinessObjectID='" + permission.getBusinessObjectID() + "'," + "UserID='" + permission.getUserID() + "'," + "shareUserID='" 
						+ permission.getShareUserID() + "'WHERE id='" + permission.getId() + "'");
				
				/**
				 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
				 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
				 * ausgegeben, was passiert ist und wo im Code es passiert ist.
				 */
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// Rückgabe der Permission
			return permission;
	
	}
	/**
	 * Löschen eines Permission-Objekts aus der Datenbank.
	 * 
	 * @param permission
	 */
	public void delete (Permission permission) {
		//DB-Verbindung holen
		Connection con = DBConnection.connection();
		
		try {
			//Leeres SQL-Statement (JDBC) anlegen
			Statement stmt = con.createStatement();
			
			//SQL-Anweisung zum Löschen des übergebenen Datensatzes in der Datenbank
			stmt.executeUpdate("DELETE FROM permission WHERE id=" + permission.getId());
		
			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
			 * ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e) {
		e.printStackTrace();
	}	
	}
	/**
	 * Suchen eines Permission-Objekts anhand der übergebenen Id in der Datenbank.
	 * 
	 * @param id
	 * @return permission
	 */
	public Permission findById (int id) {
		// DB-Verbindung holen
		Connection con = DBConnection.connection();
		
		try {
			// Leeres SQL-Statement (JDBC) anlegen
			Statement stmt = con.createStatement();
				
			// SQL-Anweisung zum Finden des übergebenen Datensatzes anhand der Id in der Datenbank
			ResultSet rs = stmt.executeQuery("SELECT id, businessObjectID, userID, shareUserID FROM permission WHERE id=" + id);
			/**
			 * Da id Primärschlüssel ist, kann max. nur ein Tupel zurückgegeben
		     * werden. Es wird geprüft, ob ein Ergebnis vorliegt.
			 */
			if (rs.next()) {
				Permission permission = new Permission();
				permission.setId(rs.getInt("id"));
				permission.setBusinessObjectID(rs.getInt("businessObjectID"));
				permission.setUserID(rs.getInt("userID"));
				permission.setShareUserID(rs.getInt("shareUserID"));
				return permission;			
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
	 * Suchen eines Permission-Objekts anhand der übergebenen ContactId in der Datenbank.
	 * 
	 * @param contactID
	 * @return ArrayList<Permission>
	 */
	
	public ArrayList<Permission> findByContactId (int contactID) {
		// DB-Verbindung holen
		Connection con = DBConnection.connection();
		
		ArrayList<Permission> result = new ArrayList<Permission>();
		
		try {
			// Leeres SQL-Statement (JDBC) anlegen
			Statement stmt = con.createStatement();
			
			// SQL-Anweisung zum Finden des übergebenen Datensatzes anhand der ContactId in der Datenbank
			ResultSet rs = stmt.executeQuery("SELECT id, businessObjectID, userID, shareUserID FROM permission " + "WHERE contactID=" + contactID);
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der
			 * Tabelle permission vorhanden ist, muss das Abfragen des ResultSet so
			 * oft erfolgen (while-Schleife), bis alle Tupel durchlaufen wurden.
			 * Die DB-Tupel werden in Java-Objekte transformiert und
			 * anschliessend der ArrayList hinzugefügt.
			 */
			
			while (rs.next()) {
				Permission permission = new Permission();
				permission.setId(rs.getInt("id"));
				permission.setBusinessObjectID(rs.getInt("businessObjectID"));
				permission.setUserID(rs.getInt("userID"));
				permission.setShareUserID(rs.getInt("shareUserID"));
				result.add(permission);
			}
			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
			 * ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */	
		} catch (SQLException e) {
			e.printStackTrace();
	}
		//Rückgabe des Ergebnisvektors
		return result;
}
	/**
	 * Suchen eines Permission-Objekts anhand der übergebenen UserId in der Datenbank.
	 * 
	 * @param userID
	 * @return ArrayList<Permission>
	 */
	
	public ArrayList<Permission> findByUserId (int userID) {
		// DB-Verbindung holen
		Connection con = DBConnection.connection();
		
		ArrayList<Permission> result = new ArrayList<Permission>();
		
		try{
			// Leeres SQL-Statement (JDBC) anlegen
			Statement stmt = con.createStatement();

			// SQL-Anweisung zum Finden des übergebenen Datensatzes anhand der UserId in der Datenbank
			ResultSet rs = stmt.executeQuery("SELECT id, businessObjectID, userID, shareUserID FROM permission " + "WHERE userID=" + userID);
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der
			 * Tabelle permission vorhanden ist, muss das Abfragen des ResultSet so
			 * oft erfolgen (while-Schleife), bis alle Tupel durchlaufen wurden.
			 * Die DB-Tupel werden in Java-Objekte transformiert und
			 * anschliessend der ArrayList hinzugefügt.
			 */
			
			while (rs.next()) {
				Permission permission = new Permission();
				permission.setId(rs.getInt("id"));
				permission.setBusinessObjectID(rs.getInt("businessObjectID"));
				permission.setUserID(rs.getInt("userID"));
				permission.setShareUserID(rs.getInt("shareUserID"));
				result.add(permission);
			}
			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
			 * ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */	
		} catch (SQLException e) {
			e.printStackTrace();
	}	
		// Rückgabe des Ergebnisvektors
		return result;
	
	}
	/**
	 * Suchen ein Permission-Objekt anhand der übergebenen ContactListId in der Datenbank.
	 * 
	 * @param contactListID
	 * @return ArrayList<Permission>
	 */
	
	public ArrayList<Permission> findByContactListId (int contactListID) {
		// DB-Verbindung holen
		Connection con = DBConnection.connection();
		
		ArrayList<Permission> result = new ArrayList<Permission>();
		
		try {
			// Leeres SQL-Statement (JDBC) anlegen
			Statement stmt = con.createStatement();

			// SQL-Anweisung zum Finden des übergebenen Datensatzes anhand der ContactListId in der Datenbank
			ResultSet rs = stmt.executeQuery("SELECT id, businessObjectID, userID, shareUserID FROM permission " + "WHERE contactListID=" + contactListID);
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der
			 * Tabelle permission vorhanden ist, muss das Abfragen des ResultSet so
			 * oft erfolgen (while-Schleife), bis alle Tupel durchlaufen wurden.
			 * Die DB-Tupel werden in Java-Objekte transformiert und
			 * anschliessend der ArrayList hinzugefügt.
			 */
			
			while (rs.next()) {
				Permission permission = new Permission();
				permission.setId(rs.getInt("id"));
				permission.setBusinessObjectID(rs.getInt("businessObjectID"));
				permission.setUserID(rs.getInt("userID"));
				permission.setShareUserID(rs.getInt("shareUserID"));
				result.add(permission);
			}
			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
			 * ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */	
		}catch (SQLException e) {
			e.printStackTrace();
	}
		//Rückgabe des Ergebnisvektors
		return result;
	}
	
	/**
	 * Suchen eines Permission-Objekts anhand der übergebenen ValueId in der Datenbank.
	 * 
	 * @param valueID
	 * @return ArrayList<Permission>
	 */
	public ArrayList<Permission> findByValueId (int valueID) {
		// DB-Verbindung holen
		Connection con = DBConnection.connection();
		
		ArrayList<Permission> result = new ArrayList<Permission>();
		
		try {
			// Leeres SQL-Statement anlegen
			Statement stmt = con.createStatement();

			// SQL-Anweisung zum Finden des übergebenen Datensatzes anhand der ValueId in der Datenbank
			ResultSet rs = stmt.executeQuery("SELECT id, businessObjectID, userID, shareUserID FROM permission " + "WHERE valueID=" + valueID);
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der
			 * Tabelle permission vorhanden ist, muss das Abfragen des ResultSet so
			 * oft erfolgen (while-Schleife), bis alle Tupel durchlaufen wurden.
			 * Die DB-Tupel werden in Java-Objekte transformiert und
			 * anschliessend der ArrayList hinzugefügt.
			 */
			
			while (rs.next()) {
				Permission permission = new Permission();
				permission.setId(rs.getInt("id"));
				permission.setBusinessObjectID(rs.getInt("businessObjectID"));
				permission.setUserID(rs.getInt("userID"));
				permission.setShareUserID(rs.getInt("shareUserID"));
				result.add(permission);
			}
			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
			 * ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */	
		} catch (SQLException e) {
			e.printStackTrace();
	}
		// Rückgabe des Ergebnisvektors
		return result;
	}
	
	/**
	 * Suchen eines Permission-Objekts anhand der übergebenen ShareUserId in der Datenbank.
	 * 
	 * @param shareUserID
	 * @return ArrayList<Permission>
	 */

	public ArrayList<Permission> findByShareUserId (int shareUserID) {
		// DB-Verbindung holen
		Connection con = DBConnection.connection();
		
		ArrayList<Permission> result = new ArrayList<Permission>();
		
		try {
			// Leeres SQL-Statement (JDBC) anlegen
			Statement stmt = con.createStatement();

			// SQL-Anweisung zum Finden des übergebenen Datensatzes anhand der ShareUserId in der Datenbank
			ResultSet rs = stmt.executeQuery("SELECT id, businessObjectID, userID, shareUserID FROM permission " + "WHERE shareUserID=" + shareUserID);
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der
			 * Tabelle permission vorhanden ist, muss das Abfragen des ResultSet so
			 * oft erfolgen (while-Schleife), bis alle Tupel durchlaufen wurden.
			 * Die DB-Tupel werden in Java-Objekte transformiert und
			 * anschliessend der ArrayList hinzugefügt.
			 */
			
			while (rs.next()) {
				Permission permission = new Permission();
				permission.setId(rs.getInt("id"));
				permission.setBusinessObjectID(rs.getInt("businessObjectID"));
				permission.setUserID(rs.getInt("userID"));
				permission.setShareUserID(rs.getInt("shareUserID"));
				result.add(permission);
			}
			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
			 * ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */	
		}catch (SQLException e) {
			e.printStackTrace();
	}
		// Rückgabe des Ergebnisvektors
		return result;
		
	}
}
