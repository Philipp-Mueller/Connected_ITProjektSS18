package de.hdm.Connected.server.db;

import de.hdm.Connected.shared.bo.SharedObject;
import java.util.ArrayList;
import java.sql.*;

/**
 * Die Klasse SharedObjectsMapper bildet SharedObject-Objekte auf eine relationale 
 * Datenbank ab. Hierzu wird eine Reihe von Methoden (insert, update, delete, findBy)
 * zur Verfügung gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert 
 * und gelöscht werden können.
 * 
 * @author Viktoriya
 */

public class SharedObjectsMapper {
	
		/**
		 * Die Klasse SharedObjectsMapper wird nur einmal instantiiert (Singleton).
		 * Damit diese Eigenschaft erfüllt werden kann, wird zunächst
		 * eine Variable mit dem Bezeichner static und dem Standardwert
		 * null erzeugt. Sie speichert die einzige Instanz der Klasse.
		 */
		
		public static SharedObjectsMapper sharedObjectsMapper = null;
		
		/**
		 * Geschützter Konstruktor verhindert die Möglichkeit für das erneute 	 
		 * Erzeugen von weiteren Instanzen dieser Klasse.
		 */
		
		protected SharedObjectsMapper() {
		}
		
		/**
		 * Statische Methode zum Sicherstellen der Singleton-Eigenschaft. Sie sorgt dafür,
		 * dass nur eine einzige Instanz der SharedObjectsMapper-Klasse exsistiert.
		 * Aufgerufen wird die Klasse somit über SharedObjectsMapper.sharedObjectsMapper() und
		 * nicht über den New-Operator.
		 * 
		 * @return sharedObjectsMapper
		 */
		
		public static SharedObjectsMapper sharedObjectsMapper() {
			if (sharedObjectsMapper == null){
				sharedObjectsMapper = new SharedObjectsMapper();
			}
			return sharedObjectsMapper;
		}
		
		/**
		 * Einfügen eines SharedObject-Objekts in die Datenbank.
		 * 
		 * @parameter sharedObject
		 * @return sharedObject
		 */
		
		public SharedObject insert (SharedObject sharedObject) {
			//DB-Verbindung holen
			Connection con = DBConnection.connection();
		
			try {
				// Leeres SQL-Statement (JDBC) anlegen
				Statement stmt = con.createStatement();
				
				// Statement ausfüllen und als Query an die Datenbank schicken
				ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid FROM sharedObject");
			
				if (rs.next()) {
					// Ergebnis-Tupel in Objekt umwandeln
					sharedObject.setBoId(rs.getInt("maxid") + 1);
				}
				stmt = con.createStatement();
				// SQL-Anweisung zum Einfügen des neuen SharedObject-Tupels in die Datenbank
				stmt.executeUpdate("INSERT INTO sharedObject (id, modifiyerId, creatorId) VALUES " + "(" + sharedObject.getBoId() + ", '"
						+ sharedObject.getModifiyerId() + ", '"+ sharedObject.getCreatorId() + "')");
				
				/**
				 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
				 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
				 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
				 */
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// Rückgabe des ShareObjects
			return sharedObject;
		}	
			
		/**
		 * Aktualisieren eines SharedObject-Objekts in der Datenbank.
		 * 
		 * @param sharedObject
		 * @return sharedObject
		 */
		
		public SharedObject update (SharedObject sharedObject) {
			// DB-Verbindung holen
				Connection con = DBConnection.connection();

				try {
					// Leeres SQL Statement anlegen
					Statement stmt = con.createStatement();
					
					// SQL-Anweisung zum Aktualisieren des übergebenen Datensatzes in der Datenbank
					stmt.executeUpdate("UPDATE sharedObject  SET" + "sharedObjectId='" + sharedObject.getBoId() + "'," + "modifiyerId='" + sharedObject.getModifiyerId() + "'," + "creatorId='" 
							+ sharedObject.getCreatorId() + "'WHERE id='" + sharedObject.getBoId() + "'");
					
					/**
					 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
					 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
					 * ausgegeben, was passiert ist und wo im Code es passiert ist.
					 */
				} catch (SQLException e) {
					e.printStackTrace();
				}
				// Rückgabe des SharedObjects
				return sharedObject;
		
		}
		/**
		 * Löschen eines SharedObject-Objekts aus der Datenbank.
		 * 
		 * @param sharedObject
		 */
		public void delete (SharedObject sharedObject) {
			//DB-Verbindung holen
			Connection con = DBConnection.connection();
			
			try {
				//Leeres SQL-Statement (JDBC) anlegen
				Statement stmt = con.createStatement();
				
				//SQL-Anweisung zum Löschen des übergebenen Datensatzes in der Datenbank
				stmt.executeUpdate("DELETE FROM sharedObject WHERE id=" + sharedObject.getBoId());
			
				/**
				 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
				 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
				 * ausgegeben, was passiert ist und wo im Code es passiert ist.
				 */
			} catch (SQLException e) {
			e.printStackTrace();
		}	
		}
		/*
		
		/**
		 * Suchen eines SharedObject-Objekts anhand der übergebenen Id in der Datenbank.
		 * 
		 * @param id
		 * @return sharedObject
		 *//*
		public SharedObject findById (int id) {
			// DB-Verbindung holen
			Connection con = DBConnection.connection();
			
			try {
				// Leeres SQL-Statement (JDBC) anlegen
				Statement stmt = con.createStatement();
					
				// SQL-Anweisung zum Finden des übergebenen Datensatzes anhand der Id in der Datenbank
				ResultSet rs = stmt.executeQuery("SELECT id, modifiyerId, creatorId FROM sharedObject WHERE id=" + id);
				*//**
				 * Da id Primärschlüssel ist, kann max. nur ein Tupel zurückgegeben
			     * werden. Es wird geprüft, ob ein Ergebnis vorliegt.
				 *//*
				if (rs.next()) {
					SharedObject sharedObject = new SharedObject();
					sharedObject.setBoId(rs.getInt("id"));
					sharedObject.setModifiyerId(rs.getInt("modifiyerId"));
					sharedObject.setCreatorId(rs.getInt("creatorId"));
					return sharedObject;			
			    }
				*//**
				 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
				 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
				 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
				 *//*
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		*//**
		 * Suchen eines SharedObject-Objekts anhand der übergebenen ObjectId in der Datenbank.
		 * 
		 * @param objectId
		 * @return ArrayList<SharedObject>
		 *//*
		
		public ArrayList<SharedObject> findByObjectId (int objectId) {
			// DB-Verbindung holen
			Connection con = DBConnection.connection();
			
			ArrayList<SharedObject> result = new ArrayList<SharedObject>();
			
			try {
				// Leeres SQL-Statement (JDBC) anlegen
				Statement stmt = con.createStatement();

				// SQL-Anweisung zum Finden des übergebenen Datensatzes anhand der UserId in der Datenbank
				ResultSet rs = stmt.executeQuery("SELECT id, modifiyerId, creatorId FROM sharedObject " + "objectId=" + objectId);
				*//**
				 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der
				 * Tabelle sharedObject vorhanden ist, muss das Abfragen des ResultSet so
				 * oft erfolgen (while-Schleife), bis alle Tupel durchlaufen wurden.
				 * Die DB-Tupel werden in Java-Objekte transformiert und
				 * anschliessend der ArrayList hinzugefügt.
				 *//*
				
				while (rs.next()) {
					SharedObject sharedObject = new SharedObject();
					sharedObject.setBoId(rs.getInt("id"));
					sharedObject.setModifiyerId(rs.getInt("modifiyerId"));
					sharedObject.setCreatorId(rs.getInt("receiverUserID"));
					result.add(sharedObject);
				}
				*//**
				 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
				 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
				 * ausgegeben, was passiert ist und wo im Code es passiert ist.
				 *//*	
			} catch (SQLException e) {
				e.printStackTrace();
		    }	
			// Rückgabe der ArrayList
			return result;
		}
		
		*//**
		 * Suchen eines SharedObject-Objekts anhand der übergebenen UserId in der Datenbank.
		 * 
		 * @param userId
		 * @return ArrayList<SharedObject>
		 *//*

		public ArrayList<SharedObject> findByUserId (int userId) {
			// DB-Verbindung holen
			Connection con = DBConnection.connection();
			
			ArrayList<SharedObject> result = new ArrayList<SharedObject>();
			
			try {
				// Leeres SQL-Statement (JDBC) anlegen
				Statement stmt = con.createStatement();

				// SQL-Anweisung zum Finden des übergebenen Datensatzes anhand der ShareUserId in der Datenbank
				ResultSet rs = stmt.executeQuery("SELECT id, modifiyerId, creatorId FROM sharedObject " + "WHERE userId=" + userId);
				*//**
				 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der
				 * Tabelle sharedObject vorhanden ist, muss das Abfragen des ResultSet so
				 * oft erfolgen (while-Schleife), bis alle Tupel durchlaufen wurden.
				 * Die DB-Tupel werden in Java-Objekte transformiert und
				 * anschliessend der ArrayList hinzugefügt.
				 *//*
				
				while (rs.next()) {
					SharedObject sharedObject = new SharedObject();
					sharedObject.setBoId(rs.getInt("id"));
					sharedObject.setModifiyerId(rs.getInt("modifiyerId"));
					sharedObject.setCreatorId(rs.getInt("creatorId"));
					result.add(sharedObject);
				}
				*//**
				 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
				 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
				 * ausgegeben, was passiert ist und wo im Code es passiert ist.
				 *//*	
			  } catch (SQLException e) {
				e.printStackTrace();
		      }
			// Rückgabe der ArrayList
		      return result;
	}*/
}
