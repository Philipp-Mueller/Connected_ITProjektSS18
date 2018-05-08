package de.hdm.Connected.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.Connected.client.gui.ContactForm;
import de.hdm.Connected.client.gui.ContactListForm;
import de.hdm.Connected.shared.ConnectedAdminAsync;
import de.hdm.Connected.shared.FieldVerifier;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Connected_ITProjektSS18 implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final ConnectedAdminAsync connectedAdmin = ClientSideSettings.getConnectedAdmin();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		Button newContactButton = new Button("Neuen Kontakt anlegen");

		newContactButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ContactForm newcontactForm = new ContactForm();
			}

		});
		Button newContactListButton = new Button("Neue Kontaktliste anlegen");

		newContactListButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ContactListForm newcontactlistForm = new ContactListForm();
			}

		});
		RootPanel.get("content").add(newContactButton);
		RootPanel.get("content").add(newContactListButton);
	}
}
