package de.hdm.Connected.client.gui;

import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Die Klasse Navigation erweitert die von GWT angebotene Klasse VerticalPanel
 * und dient der Darstellung einer Navigationsleiste, mit deren Hilfe der Nutzer
 * zwischen verschiedenen Ansichten (Kontakten und Kontaktlisten) wechseln sowie
 * neue Kontakte und Kontaklisten erstellen kann.
 * 
 * @autor
 * 
 */

public class Navigation extends VerticalPanel {

	/**
	 * <code>onLoad()</code>-Methode, die aufgerufen wird, sobald das Widget zur
	 * Anzeige gebracht wird.
	 */

	public void onLoad() {

		/*
		 * Anlegen des Baumes mit dem zuvor definierten TreeViewModel. Als
		 * zweiter Parameter wird null übergeben, damit wird der Default-Wert
		 * für einen Baumknoten festgelegt.
		 */

		NavigationTreeModel navigation = new NavigationTreeModel(null);

		this.add(navigation);

	}
}