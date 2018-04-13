package de.hdm.Connected.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

			stmt.executeUpdate("INSERT INTO user (id, name, email) VALUES " + "(" + user.getId() + ", '"
					+ user.getName() + "', '" + user.getEmail() + "')");

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

	public User update(User user) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Aktualisieren des übergebenen Datensatzes in
			 * der Datenbank.
			 */
			stmt.executeUpdate("UPDATE user SET name='" + user.getName() + "' WHERE id= " + user.getId());
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
			ResultSet rs = stmt.executeQuery("SELECT id, name FROM user WHERE id=" + id);
			/**
			 * Zu einem eindeutigen Wert exisitiert nur maximal ein
			 * Datenbank-Tupel, somit kann auch nur einer zurückgegeben werden.
			 * Es wird mit einer If-Abfragen geprüft, ob es für den angefragten
			 * Primärschlüssel ein DB-Tupel gibt.
			 */
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
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

	public User findByEmail(String email) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			/**
			 * SQL-Anweisung zum Finden aller Datensätze, anhand der eMail, in
			 * der Datenbank.
			 */
			ResultSet rs = stmt.executeQuery("SELECT id, name, email FROM user WHERE email LIKE '" + email + "'");
			/**
			 * Zu einem eindeutigen Wert exisitiert nur maximal ein
			 * Datenbank-Tupel, somit kann auch nur einer zurückgegeben werden.
			 * Es wird mit einer If-Abfragen geprüft, ob es für den angefragten
			 * Primaerschlüssel ein DB-Tupel gibt.
			 */
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
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

}
