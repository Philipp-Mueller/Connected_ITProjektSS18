package de.hdm.Connected.server.db;

import java.sql.Connection;
import java.sql.DriverManager;

import com.google.appengine.api.utils.SystemProperty;

/**
 * Klasse zum Aufbau zur Verbindung der Datenbank.
 * 
 * @author Philipp
 */

public class DBConnection {

	/**
	 * Die Klasse DBConnection wird nur einmal instanziiert.
	 */

	private static Connection con = null;

	/**
	 * Mit dieser Url wird die Datenbank angesprochen. Die TestUrl ist für
	 * Testwecke und hat im Produktivsystem keinerlei Funktion.
	 */

	private static String googleUrl = "";

	private static String TestUrl = "";

	/**
	 * Stellt die Verbindung zur Datenbank her.
	 * 
	 * @return con
	 */

	public static Connection connection() {
		/**
		 * Herstellung einer DB Verbindung, wenn bisher keine Verbindung besteht
		 **/
		if (con == null) {
			String url = null;
			try {
				if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
					Class.forName("com.mysql.jdbc.GoogleDriver");
					url = googleUrl;
				} else {
					/**
					 * Wenn die GoogleUrl nicht erreichbar ist, wird die TestUrl
					 * (Lokal) aufgerufen. Alternativlösung während des
					 * Entwickelns, wenn Lokal deployed werden soll für
					 * Testzwecke.
					 */
					Class.forName("com.mysql.jdbc.Driver");
					url = TestUrl;
				}
				/**
				 * Die Verbindung zur Datenbank wird in der Variablen con mit
				 * den dazugehoerigen Informationen gespeichert
				 */
				con = DriverManager.getConnection(url);

			} catch (Exception e) {
				con = null;
				e.printStackTrace();
			}
		}
		/**
		 * Verbindung wird in der Variable con zurückgegeben
		 */
		return con;
	}

}
