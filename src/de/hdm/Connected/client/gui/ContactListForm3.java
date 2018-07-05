package de.hdm.Connected.client.gui;

import java.util.ArrayList;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
public class ContactListForm3 extends CellTable {

	private ConnectedAdminAsync connectedAdmin = ClientSideSettings.getConnectedAdmin();

	/**Klassenweiter Kontaktlisten speicher**/
	private ContactList mainContactlist = new ContactList();

	/**Überladener Konstruktor, der eine Kontaktliste als Parameter enthält
	 * @param Kontaktliste**/
	public ContactListForm3(final ContactList cl) {
		

		mainContactlist = cl;

		/**Servicemethoden aufruf, der den Inhalt der übergebenen Kontaktliste lädt**/
		ClientSideSettings.getConnectedAdmin().findContactsByContactListId(mainContactlist.getBoId(),
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