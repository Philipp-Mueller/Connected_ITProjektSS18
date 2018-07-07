package de.hdm.Connected.client.gui;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.ConnectedAdminAsync;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.ContactList;

/**
 * Klasse für das Laden des Inhalts einer Kontaktliste
 * 
 * @author Moritz
 *
 */
public class ContactListForm extends PopupPanel {

	private ConnectedAdminAsync connectedAdmin = ClientSideSettings.getConnectedAdmin();

	/**Klassenweiter Kontaktlisten speicher**/
	private ContactList mainContactlist = new ContactList();

	
	public ContactListForm(){
		// Set the dialog box's caption.

				// Enable animation.
				setAnimationEnabled(true);

				// Enable glass background.
				setGlassEnabled(true);

				VerticalPanel v = new VerticalPanel();
				v.add(new HTML("<h2> Neue Kontaktliste anlegen </h2>"));

				Label nameLabel = new Label("Name: ");
				final TextBox nameTextBox = new TextBox();
				HorizontalPanel h = new HorizontalPanel();
				h.add(nameLabel);
				h.add(nameTextBox);
				v.add(h);

				Button close = new Button("Abbrechen");
				/** Schließen des Popups **/
				close.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						hide();
					}

				});

				Button ok = new Button("Erstellen");
				/**
				 * Bei Klick auf erstellen wird die Service Methode zum erstellen der
				 * KOntaktliste aufgerufen
				 **/
				ok.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {

						// Eingabeüberprüfung
						if (nameTextBox.getText().matches("")) {
							Window.alert("Bitte Wert eintragen!");
							return;
						}

						ContactList cl = new ContactList();
						cl.setName(nameTextBox.getText());

						ClientSideSettings.getConnectedAdmin().createContactList(nameTextBox.getText(),
								ClientSideSettings.getCurrentUser().getId(), new AsyncCallback<ContactList>() {
									@Override
									public void onFailure(Throwable caught) {

									}

									@Override
									public void onSuccess(ContactList result) {
										Window.alert("Erfolgreich erstellt");
										RootPanel.get("content").clear();
										// Neuladen der Kontatliste
										ContactListForm reload = new ContactListForm(result);
										RootPanel.get("nav").clear();
										NavigationTreeModel treemodel = new NavigationTreeModel(result);
										RootPanel.get("nav").add(treemodel);

									}
								});
						hide();
					}
				});
				HorizontalPanel buttons = new HorizontalPanel();
				buttons.add(close);
				buttons.add(ok);
				
				v.add(buttons);
				setWidget(v);
	}
	
	
	/**Überladener Konstruktor, der eine Kontaktliste als Parameter enthält
	 * @param Kontaktliste**/
	public ContactListForm(final ContactList cl) {
		

		mainContactlist = cl;

		/**Servicemethoden aufruf, der den Inhalt der übergebenen Kontaktliste lädt**/
		ClientSideSettings.getConnectedAdmin().findContactsByContactListId(mainContactlist.getId(),
				new AsyncCallback<ArrayList<Contact>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ArrayList<Contact> result) {
						/**Laden der Kontaktliste im Table
						 * Übergabe von Inhalt und Kontaktliste**/
						ContactsTable load = new ContactsTable(result, mainContactlist);

					}

				});

	}

}