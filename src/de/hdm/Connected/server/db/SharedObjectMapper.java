package de.hdm.Connected.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.hdm.Connected.shared.bo.SharedObject;


public class SharedObjectMapper {
	
	
	/**
	 * Die Klasse SharedObjectMapper bildet Shared-Objekte auf eine relationale Datenbank
	 * ab. Ebenfalls ist es moeglich aus Datenbank-Tupel Java-Objekte zu erzeugen.
	 * 
	 * Zur Verwaltung der Objekte implementiert die Mapper-Klasse entsprechende
	 * Methoden (insert, delete, update). Die SharedObjects Klasse dient zur verwaltung der IDs aller Shared Objects (Contact, ContactList und Value)
	 * 
	 * @author Philipp
	 */

		/**
		 * Die Klasse SharedObjectMapper wird nur einmal instantiiert
		 * (Singleton-Eigenschaft). Damit diese Eigenschaft erfuellt werden kann,
		 * wird zunaechst eine Variable mit dem Schluesselwort static und dem
		 * Standardwert null erzeugt. Sie speichert die Instanz dieser Klasse.
		 */
		private static SharedObjectMapper sharedObjectMapper = null;

		/**
		 * Ein geschuetzter Konstruktor verhindert das erneute erzeugen von weiteren
		 * Instanzen dieser Klasse.
		 */
		protected SharedObjectMapper() {
		}

		/**
		 * Methode zum Sicherstellen der Singleton-Eigenschaft. Diese sorgt dafuer,
		 * dass nur eine einzige Instanz der SharedObject-Klasse existiert.
		 * Aufgerufen wird die Klasse somit ueber SharedObjectMapper.sharedObjectMapper() und
		 * nicht ueber den New-Operator.
		 * 
		 * @return sharedObjectMapper
		 */
		public static SharedObjectMapper sharedObjectMapper() {
			if (sharedObjectMapper == null) {
				sharedObjectMapper = new SharedObjectMapper();
			}
			return sharedObjectMapper;
		}

		/**
		 * Fuegt ein SharedObject-Objekt der Datenbank hinzu.
		 * 
		 * @param sharedObject
		 * @return sharedObject
		 */
		public int insert() {
			/**
			 * DB-Verbindung holen.
			 */
			Connection con = DBConnection.connection();
			int sharedObjectId = 0;

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
				ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid FROM sharedobject");

				if (rs.next()) {
					sharedObjectId = (rs.getInt("maxid") + 1);
				}
				stmt = con.createStatement();
				/*
				 * SQL-Anweisung zum Einfuegen des neuen SharedObject-Tupels in die
				 * Datenbank.
				 */

				stmt.executeUpdate("INSERT INTO sharedobject (id) VALUES (" + sharedObjectId + ")");
				/**
				 * Das Aufrufen des printStackTrace bietet die Moeglichkeit, die
				 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
				 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
				 */
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			return sharedObjectId;
		}

		
		/**
		 * Loescht ein SharedObject-Objekt aus der Datenbank.
		 * 
		 * @param sharedObject
		 */
		public void delete(int sharedObjectId) {
			Connection con = DBConnection.connection();

			try {
				Statement stmt = con.createStatement();
				/**
				 * SQL-Anweisung zum Loeschen des uebergebenen Datensatzes in der
				 * Datenbank.
				 */
				stmt.executeUpdate("DELETE FROM sharedobject WHERE id=" + sharedObjectId);
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

}
