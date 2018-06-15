package de.hdm.Connected.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager.SelectAction;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.ContactList;

/**
 * Klasse für die Bereistellung eines Formulars zum Anlegen/Bearbeiten einer
 * Kontaltiste
 * 
 * @author Moritz
 *
 */

public class ContactListForm2 extends Widget {

	Label nameLabel = new Label("Name:");
	TextBox nameBox = new TextBox();
	Label yourContactsLabel = new Label("Deine Kontakte:");
	ListBox contactListBox = new ListBox();
	Button addContactButton = new Button("Kontakt hinzufügen");
	Button createListButton = new Button("Liste erstellen", new createContactListClickhandler());
	Button visitbutton = new Button("Visit");

	HorizontalPanel namePanel = new HorizontalPanel();
	HorizontalPanel contactsPanel = new HorizontalPanel();
	
	Grid clGrid = new Grid();


	public ContactListForm2() {

		VerticalPanel topPanel = new VerticalPanel();

		namePanel.add(nameLabel);
		namePanel.add(nameBox);

		topPanel.add(new HTML("<h2> Neue Kontaktliste erstellen</h2>"));
		topPanel.add(namePanel);
		topPanel.add(addContactButton);
		topPanel.add(createListButton);
		topPanel.add(clGrid);
		
		

		RootPanel.get("content").add(topPanel);
		ClientSideSettings.getConnectedAdmin().findAllContactlists(new AsyncCallback<ArrayList<ContactList>>() {
			int i = 0;
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Die Kontaktlisten konnten nicht geladen werden");
			}

			@Override
			// jede Kontaktliste wird der ListBox hinzugefügt
			public void onSuccess(ArrayList<ContactList> result) {
				clGrid.resize(result.size(), 2);
				for (ContactList cl : result) {
//					contactlist.addItem(cl.getName());
					clGrid.setWidget(i, 0, new Label(cl.getName()));
					clGrid.setWidget(i, 1, new Button("Visit"), new visitClickhandler());
					i = i +1;
					Window.alert(Integer.toString(i));
				}

			}

		});
				
				

		
	}
	
	private class visitClickhandler implements ClickHandler
	{
		public void onClick(ClickEvent event) {

			//rootpanel.clear();
			//Contaktliste anzeigen (id aus tabelle = id aus result Array -> id von cl)
			//Oben 2 buttons mit cl bearbeiten und cl löschen
			//Kontakt cl hinzufügen button
			//Cl teilen button

		}
	}

	private class createContactListClickhandler implements ClickHandler {

		public void onClick(ClickEvent event) {

			ClientSideSettings.getConnectedAdmin().createContactList(nameBox.getText(),
					new createContactListCallback());

		}
	};

	private class createContactListCallback implements AsyncCallback<ContactList> {

		public void onFailure(Throwable caught) {
			Window.alert("Da ist wohl etwas schief gelaufen");
		}

		public void onSuccess(ContactList result) {
			Window.alert("Contactlist angelegt");
			RootPanel.get("Content").clear();

		}

	}
}
