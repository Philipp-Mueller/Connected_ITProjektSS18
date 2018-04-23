package de.hdm.Connected.client.gui;

import com.google.gwt.user.client.ui.*;

import de.hdm.Connected.shared.bo.Contact;

/** 
 * Klasse für die Bereistellung eines Formulars zum Anlegen/Bearbeiten eines Kontakts
 * 
 * @author Philipp
 *
 */

public class ContactForm {
	
	//ConnectedAdminAsync connectedAdmin = ClientSideSettings.getConnectedAdmin();
	private Contact selectedContact = null;
	
	
	Label firstNameLabel = new Label("Vorname:");
	TextBox firstNameBox = new TextBox();
	Label surnameLabel = new Label("Nachname:");
	TextBox surnameBox = new TextBox();
	
	/**
	 * Konstruktor wenn ein Kontakt schon existiert.
	 * @param contact
	 */
	
	public ContactForm(Contact contact) {
		
		this.selectedContact = contact;
		
		RootPanel.get("content").clear();
		
	}
	
	/**
	 * Konstruktor wenn ein neuer Kontakt angelegt wird.
	 */
	
	
	public ContactForm() {
		
		RootPanel.get("content").clear();
		
	}
	
	
	
	
	
	
}
