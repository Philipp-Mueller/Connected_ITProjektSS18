package de.hdm.Connected.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.Connected.client.gui.ContactForm_Test;
import de.hdm.Connected.client.gui.ContactListForm;
import de.hdm.Connected.client.gui.ContactListForm2;
import de.hdm.Connected.client.gui.Test_CellTable;
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
				RootPanel.get("content").clear();
				ContactForm_Test newcontactForm = new ContactForm_Test();
			}

		});
		Button newContactListButton = new Button("Neue Kontaktliste anlegen");

		newContactListButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("content").clear();
				ContactListForm2 newcontactlistForm = new ContactListForm2();
				//Test_CellTable newform = new Test_CellTable(); 
			}

		});
		RootPanel.get("content").add(newContactButton);
		RootPanel.get("content").add(newContactListButton);
		
		HorizontalPanel footer = new HorizontalPanel();
		Anchor startPage = new Anchor ("Startseite", "Connected_ITProjektSS18.html");
		HTML copyrightText1 = new HTML(" | ");
		Anchor reportGeneratorLink = new Anchor (" ReportGenerator", "Connected_ITProjektSS18ReportGenerator.html");
		HTML copyrightText2 = new HTML(" | © 2018 Connected | ");
		Anchor impressumLink = new Anchor("Impressum");
		footer.add(startPage);
		footer.add(copyrightText1);
		footer.add(reportGeneratorLink);
		footer.add(copyrightText2);
		footer.add(impressumLink);
		
		impressumLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("content").clear();
				RootPanel.get("content").add(new HTML("<h2>Impressum nach §5 TMG</h2>"
						+ "<h3>Verantwortlich</h3>"
						+ "<p>Hochschule der Medien<br />"
						+ "Nobelstraße 8<br />"
						+ "70569 Stuttgart<br /></p>"
						+ "<p><strong>Projektarbeit innerhalb des Studiengangs "
						+ "Wirtschaftsinformatik und digitale Medien, "
						+ "IT-Projekt SS 18.</strong></p>"
						+ "<h3>Projektteam</h3>"
						+ "<ul><li>xxx</li>"
						+ "<li>xxx</li>"
						+ "<li>xxx</li>"
						+ "<li>xxx</li>"
						+ "<li>xxx</li>"
						+ "<li>xxx</li></ul>"
						+ "<h3>Kontakt</h3>"
						+ "<p><strong>Telefon:</strong> 0711 8923 10 (Zentrale)</p>"
						+ "<p><strong>Website:</strong> <a href='http://www.hdm-stuttgart.de' target='_blank'>"
						+ "www.hdm-stuttgart.de</a></p>"));
			}
			
		});
		RootPanel.get("footer").add(footer);
	}
}
