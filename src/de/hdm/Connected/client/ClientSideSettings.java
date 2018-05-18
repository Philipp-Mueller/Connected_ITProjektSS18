package de.hdm.Connected.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.Connected.shared.CommonSettings;
import de.hdm.Connected.shared.ConnectedAdmin;
import de.hdm.Connected.shared.ConnectedAdminAsync;
import de.hdm.Connected.shared.ReportGeneratorService;
import de.hdm.Connected.shared.ReportGeneratorServiceAsync;
import de.hdm.Connected.shared.bo.User;

/**
 * Diese Klasse enthält Dienste und Eigenschaften, die für Klassen des client-Packages 
 * relevant sind.
 * 
 * @author Philipp
 *
 */

public class ClientSideSettings extends CommonSettings {
	
	/**
	 * currentUser wird gesetzt.
	 */
	
	private static User currentUser = null;
	
	 /**
	   * Remote Service Proxy. Hiermit wird eine Verbindung mit dem Server-seitgen Dienst
	   * <code>ConnectedAdmin</code> hergestellt.
	   */
	
	  private static ConnectedAdminAsync connectedAdmin = null;

	  /**
	   * Remote Service Proxy. Hiermit wird eine Verbindung mit dem Server-seitgen Dienst
	   * <code>ReportGenerator</code> hergestellt.
	   */
	  
	 private static ReportGeneratorServiceAsync reportGenerator = null;

	  /**
	   * Name des Client-Loggers.
	   */
	  private static final String LOGGER_NAME = "Connected Client";
	  
	  /**
	   * Instanz des Client-seitigen Loggers.
	   */
	  private static final Logger log = Logger.getLogger(LOGGER_NAME);

	  /**
	   * <p>
	   * Auslesen des Client-Loggers.
	   * </p>
	   * 
	   * @return die Logger-Instanz für die Server-Seite
	   */
	  public static Logger getLogger() {
	    return log;
	  }

	  /**
	   * <p>
	   * Anlegen und Auslesen der applikationsweit eindeutigen ConnectedAdmin. Diese
	   * Methode erstellt die ConnectedAdmin, wenn sie noch nicht existiert. Ansonsten 
	   * wird das bereits angelegte Objekt übergeben.
	   * </p>
	   * 
	   * @return eindeutige Instanz des Typs <code>ConnectedAdminAsync</code>
	   * @author Philipp
	   */
	  public static ConnectedAdminAsync getConnectedAdmin() {
	    if (connectedAdmin == null) {
	      // falls noch nicht geschehen, per GWT.create ein Objekt von ConnectedAdmin erzeugen
	      connectedAdmin = GWT.create(ConnectedAdmin.class);
	    }

	    // Zurückgeben des Objekts
	    return connectedAdmin;
	  }

	  /**
	   * <p>
	   * Anlegen und Auslesen des applikationsweit eindeutigen ReportGenerator. Diese
	   * Methode erstellt den ReportGenerator, wenn er noch nicht existiert. Ansonsten 
	   * wird das bereits angelegte Objekt übergeben.
	   * </p>
	   * 
	   * @return eindeutige Instanz des Typs <code>ReportGeneratorAsync</code>
	   * @author Philipp
	   */
	public static ReportGeneratorServiceAsync getReportGenerator() {
	    if (reportGenerator == null) {
	      // Instantiierung von ReportGenerator
	      reportGenerator = GWT.create(ReportGeneratorService.class);

	      final AsyncCallback<Void> initReportGeneratorCallback = new AsyncCallback<Void>() {
	        @Override
			public void onFailure(Throwable caught) {
	          ClientSideSettings.getLogger().severe(
	              "Der ReportGenerator konnte nicht initialisiert werden!");
	        }

	        @Override
			public void onSuccess(Void result) {
	          ClientSideSettings.getLogger().info(
	              "Der ReportGenerator wurde initialisiert.");
	        }
	      };

	      reportGenerator.init(initReportGeneratorCallback);
	    }

	    // Rückgabe des ReportGenerator
	    return reportGenerator;
	  }

	public static User getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(User currentUser) {
		ClientSideSettings.currentUser = currentUser;
	}

}
