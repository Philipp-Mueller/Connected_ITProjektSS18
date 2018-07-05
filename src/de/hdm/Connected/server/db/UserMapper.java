package de.hdm.Connected.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.User;

/**
 * Die Klasse UserMapper bildet User-Objekte auf eine relationale Datenbank
 * ab. Ebenfalls ist es moeglich aus Datenbank-Tupel Java-Objekte zu erzeugen.
 * 
 * Zur Verwaltung der Objekte implementiert die Mapper-Klasse entsprechende
 * Methoden (insert, search, delete, update).
 * 
 * @author Philipp
 */

public class UserMapper {

	/**
	 * Die Klasse UserMapper wird nur einmal instantiiert
	 * (Singleton-Eigenschaft). Damit diese Eigenschaft erfüllt werden kann,
	 * wird zunächst eine Variable mit dem Schlüsselwort static und dem
	 * Standardwert null erzeugt. Sie speichert die Instanz dieser Klasse.
	 */

	private static UserMapper userMapper = null;

	/**
	 * Ein geschützter Konstruktor verhindert das erneute Erzeugen von weiteren
	 * Instanzen dieser Klasse.
	 */

	protected UserMapper() {
	}

	/**
	 * Methode zum Sicherstellen der Singleton-Eigenschaft. Diese sorgt dafür,
	 * dass nur eine einzige Instanz der UserMapper-Klasse exsistiert.
	 * Aufgerufen wird die Klasse somit über UserMapper.userMapper() und
	 * nicht über den New-Operator.
	 * 
	 * @return userMapper
	 */

	public static UserMapper userMapper() {
		if (userMapper == null) {
			userMapper = new UserMapper();
		}
		return userMapper;
	}

	/**
	 * Fügt ein User-Objekt der Datenbank hinzu.
	 * 
	 * @param user
	 * @return user
	 */

	public User insert(User user) {
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
			 * Abfrage des zuletzt hinzugefügten Primärschlüssel (id). Die
			 * aktuelle id wird um eins erhöht. Statement ausfüllen und als
			 * Query an die Datenbank senden.
			 */

			ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid FROM user");

			if (rs.next()) {
				user.setId(rs.getInt("maxid") + 1);
			}
			stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Einfügen des neuen User-Tupels in die
			 * Datenbank.
			 */

			stmt.executeUpdate("INSERT INTO user (id, name, logEmail) VALUES (" + user.getId() + ", '"
					+ user.getName() + "', '"
					+ user.getLogEmail() + "')");

			/**
			 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
			 * Fehlermeldung genauer zu analyisieren. Es werden Informationen
			 * dazu ausgegeben, was passiert ist und wo im Code es passiert ist.
			 */
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return user;
	}

	/**
	 * Aktualisiert ein User-Objekt in der Datenbank.
	 * 
	 * @param user
	 * @return user
	 */

	// macht es Sinn, die Methode auf logEmail zu beziehen, da es keinen Namen mehr gibt? Oder lieber ganz rauslöschen?
	public User update(User user) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Aktualisieren des übergebenen Datensatzes in
			 * der Datenbank.
			 */
			stmt.executeUpdate("UPDATE user SET logEmail='" + user.getLogEmail() + "', name='"+ user.getName() +"' WHERE id= " + user.getId());
		}
		/**
		 * Das Aufrufen des printStackTrace bietet die Möglichkeit, die
		 * Fehlermeldung genauer zu analyisieren. Es werden Informationen dazu
		 * ausgegeben, was passiert ist und wo im Code es passiert ist.
		 */
		catch (SQLException e2) {
			e2.printStackTrace();
		}
		return user;
	}

	/**
	 * Löscht ein User-Objekt aus der Datenbank.
	 * 
	 * @param user
	 */

	public void delete(User user) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Löschen des übergebenen Datensatzes in der
			 * Datenbank.
			 */
			stmt.executeUpdate("DELETE FROM user WHERE id=" + user.getId());
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
	 * Findet ein User-Objekt anhand der übergebenen Id in der Datenbank.
	 * 
	 * @param id
	 * @return user
	 */

	public User findById(int id) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden des übergebenen Datensatzes, anhand der
			 * Id, in der Datenbank.
			 */
			ResultSet rs = stmt.executeQuery("SELECT id, name, logEmail FROM user WHERE id=" + id);
			/**
			 * Zu einem eindeutigen Wert exisitiert nur maximal ein
			 * Datenbank-Tupel, somit kann auch nur einer zurückgegeben werden.
			 * Es wird mit einer If-Abfragen geprüft, ob es für den angefragten
			 * Primärschlüssel ein DB-Tupel gibt.
			 */
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setLogEmail(rs.getString("logEmail"));
				user.setName(rs.getString("name"));
				return user;
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
	 * Findet ein User-Objekt anhand der übergebenen logEmail in der Datenbank.
	 * 
	 * @param logEmail
	 * @return user
	 */

	public User findByEmail(String logEmail) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden aller Datensätze, anhand der eMail, in
			 * der Datenbank.
			 */
			ResultSet rs = stmt.executeQuery("SELECT id, name, logEmail FROM user WHERE logEmail LIKE '" + logEmail + "'");
			/**
			 * Zu einem eindeutigen Wert exisitiert nur maximal ein
			 * Datenbank-Tupel, somit kann auch nur einer zurückgegeben werden.
			 * Es wird mit einer If-Abfragen geprüft, ob es für den angefragten
			 * Primaerschlüssel ein DB-Tupel gibt.
			 */
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setLogEmail(rs.getString("logEmail"));
				user.setName(rs.getString("name"));
				return user;
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
	 * Findet alle User-Objekte in der Datenbank.
	 * 
	 * @return ArrayList<Contact>
	 */
	public ArrayList<User> findAll() {
		Connection con = DBConnection.connection();

		ArrayList<User> result = new ArrayList<User>();
		try {
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden aller Datensaetze in der Datenbank, sortiert nach
			 * der Id.
			 */
			ResultSet rs = stmt.executeQuery("SELECT id, name, logEmail from user ORDER BY id");
			/**
			 * Da es sein kann, dass mehr als nur ein Datenbank-Tupel in der Tabelle contact
			 * vorhanden ist, muss das Abfragen des ResultSet so oft erfolgen
			 * (while-Schleife), bis alle Tupel durchlaufen wurden. Die DB-Tupel werden in
			 * Java-Objekte transformiert und anschliessend der ArrayList hinzugefuegt.
			 */

			while (rs.next()) {
				User u = new User();
				u.setId(rs.getInt("id"));
				u.setLogEmail(rs.getString("logEmail"));
				u.setName(rs.getString("name"));
				result.add(u);
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


}
