package de.hdm.Connected.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.thirdparty.javascript.jscomp.Result;
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
import de.hdm.Connected.shared.bo.User;

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
	Button newContactListButton = new Button("Neue Kontaktliste erstellen", new newContactListClickhandler());
	Button uptdateContactListButton = new Button("Kontaktliste bearbeiten");
	Button shareContactListButton = new Button("Kontaktliste teilen", new shareCotactListClickhandler());
	Button sharePartOfClButton = new Button("Kontakte von Kontaktliste teilen", new sharePartofClClickhandler());
	Button addContactButton = new Button("Kontakt hinzufügen");
	Button createListButton = new Button("Liste erstellen", new createContactListClickhandler());
	Button visitbutton = null;
	Button shareSeletedContactsButton = new Button("Auswahl teilen", new shareSelectedContactsClickhandler());

	VerticalPanel topPanel = new VerticalPanel();
	HorizontalPanel namePanel = new HorizontalPanel();
	HorizontalPanel contactsPanel = new HorizontalPanel();
	HorizontalPanel buttonPanel = new HorizontalPanel();
	// Array, das alle CLists von User speichert
	ArrayList<ContactList> clArray = null;
	// Array, das Contact CL beziehung hält
	ArrayList<Contact> contactArray = null;
	ArrayList<Contact> c = null;
	ArrayList<Contact> globalContactArray = null;
	CellTable<Contact> contacttable = new CellTable<Contact>();
	CellTable<Contact> contacttable2 = new CellTable<Contact>();
	final ListBox userListbox = new ListBox(true);

	Grid clGrid = new Grid();
	public int row;

	private ListDataProvider<Contact> dataProvider = new ListDataProvider<Contact>();
	Set<Contact> set1 = new HashSet<Contact>();
	String sizeSt;

	public ContactListForm2() {

		// namePanel.add(nameLabel);
		// namePanel.add(nameBox);
		//
		buttonPanel.add(new HTML("<h2>Deine Kontaktlisten:</h2>"));
		buttonPanel.add(newContactListButton);

		// Share kommt erst in die einzelansicht einer CL
		// buttonPanel.add(shareContactListButton);
		// topPanel.add(namePanel);
		// topPanel.add(addContactButton);
		// topPanel.add(createListButton);

		RootPanel.get("content").add(buttonPanel);
		RootPanel.get("content").add(topPanel);
		// Muss später findContactlists per User id sein
		ClientSideSettings.getConnectedAdmin().findAllContactlists(new AsyncCallback<ArrayList<ContactList>>() {
			int i = 0;

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Die Kontaktlisten konnten nicht geladen werden");
			}

			@Override
			// jede Kontaktliste wird der ListBox hinzugefügt
			public void onSuccess(ArrayList<ContactList> result) {
				topPanel.add(clGrid);
				clArray = result;
				clGrid.resize(result.size(), 2);
				for (ContactList cl : result) {
					// contactlist.addItem(cl.getName());
					clGrid.setWidget(i, 0, new Label(cl.getName()));
					visitbutton = new Button("Visit");
					visitbutton.addClickHandler(new visitClickhandler());
					clGrid.setWidget(i, 1, visitbutton);
					i = i + 1;
				}

			}

		});

	}

	private class visitClickhandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			contactArray = new ArrayList<Contact>();
			RootPanel.get("content").clear();
			RootPanel.get("content").add(topPanel);
			topPanel.clear();
			row = (clGrid.getCellForEvent(event).getRowIndex());
			Window.alert(Integer.toString(clArray.get(row).getBoId())); // id
			int idvonCl = clArray.get(row).getBoId(); // von
														// ContactList

			ClientSideSettings.getConnectedAdmin().findContactsByContactListId(idvonCl, new showContactListCallback());
			try {
				Window.alert(Integer.toString(contactArray.size()));

			} catch (Exception e) {
				Window.alert(e.toString());
				e.printStackTrace();
			}
			// Window.alert(Integer.toString(row));
			// Contaktliste anzeigen (id aus tabelle = id aus result Array -> id
			// von cl)
			// Oben 2 buttons mit cl bearbeiten und cl löschen
			// Kontakt cl hinzufügen button
			// Cl teilen button

			// RootPanel.get("content").clear();
			// ContactForm_Test newcontactForm = new ContactForm_Test();

		}
	}

	private class createContactListClickhandler implements ClickHandler {

		public void onClick(ClickEvent event) {

			ClientSideSettings.getConnectedAdmin().createContactList(nameBox.getText(),
					new createContactListCallback());

		}
	};

	private class newContactListClickhandler implements ClickHandler {

		public void onClick(ClickEvent event) {

			RootPanel.get("content").clear();
			topPanel.clear();
			RootPanel.get("content").add(namePanel);
			RootPanel.get("content").add(topPanel);
			namePanel.add(nameLabel);
			namePanel.add(nameBox);

			topPanel.add(new HTML("<h2> Neue Kontaktliste erstellen</h2>"));
			topPanel.add(namePanel);
			topPanel.add(addContactButton);
			topPanel.add(createListButton);

		}
	};

	private class createContactListCallback implements AsyncCallback<ContactList> {

		public void onFailure(Throwable caught) {
			Window.alert("Da ist wohl etwas schief gelaufen");
		}

		public void onSuccess(ContactList result) {
			Window.alert("Contactlist angelegt");
			RootPanel.get("content").clear();

		}

	}

	private class showContactListCallback implements AsyncCallback<ArrayList<Contact>> {

		public void onFailure(Throwable caught) {
			Window.alert("Da ist wohl etwas schief gelaufen 1");

		}

		public void onSuccess(final ArrayList<Contact> result) {
			Window.alert("Contactlist gefunden");

			globalContactArray = new ArrayList<Contact>();
			globalContactArray = result;
			// for (Contact cid : result) {
			// ClientSideSettings.getConnectedAdmin().findContactById(cid.getBoId(),
			// new getContactCallback());
			// }

			TextColumn<Contact> prenameColumn = new TextColumn<Contact>() {
				public String getValue(Contact contact) {
					return contact.getPrename();
				}
			};
			TextColumn<Contact> surnameColumn = new TextColumn<Contact>() {
				public String getValue(Contact contact) {
					return contact.getSurname();
				}
			};
			ButtonCell buttonCell = new ButtonCell();
			Column buttonColumn = new Column<Contact, String>(buttonCell) {
				@Override
				public String getValue(Contact object) {
					// The value to display in the button.
					return "Visit";
				}
			};

			buttonColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {
				public void update(int index, Contact object, String value) {
					// Value is the button value. Object is the row object.
					Window.alert("You clicked: " + index);
					clGrid.clear();
					// ClientSideSettings.getConnectedAdmin().findValueAndProperty(result.get(index).getBoId(),
					// callback);

				}
			});

			// Contact c1 = new Contact();
			// c1.setPrename("Frank");
			// c1.setSurname("herbert");
			// Contact c2 = new Contact();
			// c2.setPrename("Addi");
			// c2.setSurname("Bert");

			RootPanel.get("content").add(buttonPanel);
			buttonPanel.clear();
			buttonPanel.add(shareContactListButton);
			buttonPanel.add(sharePartOfClButton);
			topPanel.add(new HTML("<h2> Kontaktliste " + clArray.get(row).getName() + ": </h2>"));
			topPanel.add(contacttable);

			final MultiSelectionModel<Contact> selectionModel = new MultiSelectionModel<Contact>(Contact.KEY_PROVIDER);
			contacttable.setSelectionModel(selectionModel,
					DefaultSelectionEventManager.<Contact>createCheckboxManager());

			Column<Contact, Boolean> checkColumn = new Column<Contact, Boolean>(new CheckboxCell(true, false)) {
				@Override
				public Boolean getValue(Contact object) {
					// Get the value from the selection model.
					return selectionModel.isSelected(object);
				}

			};
			// contacttable.addColumn(checkColumn,
			// SafeHtmlUtils.fromSafeConstant("<br/>"));
			// contacttable.setColumnWidth(checkColumn, 40, Unit.PX);

			contacttable.addColumn(prenameColumn, "Vorname");
			contacttable.addColumn(surnameColumn, "Nachname");
			contacttable.addColumn(buttonColumn, "");

			// List<Contact> listcontacts = result;

			dataProvider.getList().clear();
			dataProvider.getList().addAll(result);
			dataProvider.addDataDisplay(contacttable);

			// Set<Contact> selectedObjects = selectionModel.getSelectedSet();
			set1 = selectionModel.getSelectedSet();
			// set1.add(selectionModel.getSelectedObject());
			// set1 = selectionModel.
			// set1 = selectionModel.getSelectedSet();
			int setSize = set1.size();
			sizeSt = Integer.toString(setSize);
			topPanel.add(contacttable);

		}

	}

	private class sharePartofClClickhandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			RootPanel.get("content").clear();
			RootPanel.get("content").add(topPanel);
			topPanel.clear();

			TextColumn<Contact> prenameColumn = new TextColumn<Contact>() {
				public String getValue(Contact contact) {
					return contact.getPrename();
				}
			};
			TextColumn<Contact> surnameColumn = new TextColumn<Contact>() {
				public String getValue(Contact contact) {
					return contact.getSurname();
				}
			};

			topPanel.add(new HTML("<h2> Kontakte von Kontaktliste " + clArray.get(row).getName() + " auswählen </h2>"));
			topPanel.add(contacttable2);

			final MultiSelectionModel<Contact> selectionModel = new MultiSelectionModel<Contact>(Contact.KEY_PROVIDER);
			contacttable2.setSelectionModel(selectionModel,
					DefaultSelectionEventManager.<Contact>createCheckboxManager());

			Column<Contact, Boolean> checkColumn = new Column<Contact, Boolean>(new CheckboxCell(false, false)) {
				@Override
				public Boolean getValue(Contact object) {
					// Get the value from the selection model.
					return selectionModel.isSelected(object);
				}
			};

			contacttable2.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
			contacttable2.setColumnWidth(checkColumn, 40, Unit.PX);

			contacttable2.addColumn(prenameColumn, "Vorname");
			contacttable2.addColumn(surnameColumn, "Nachname");

			// List<Contact> listcontacts = result;

			dataProvider.getList().clear();
			dataProvider.getList().addAll(globalContactArray);
			dataProvider.addDataDisplay(contacttable2);

			// Set<Contact> selectedObjects = selectionModel.getSelectedSet();
			set1 = selectionModel.getSelectedSet();
			// set1.add(selectionModel.getSelectedObject());
			// set1 = selectionModel.
			// set1 = selectionModel.getSelectedSet();
			int setSize = set1.size();
			sizeSt = Integer.toString(setSize);
			topPanel.add(contacttable2);
			topPanel.add(shareSeletedContactsButton);

		}
	};

	private class getContactCallback implements AsyncCallback<Map> {

		public void onFailure(Throwable caught) {
			Window.alert("Da ist wohl etwas schief gelaufen 2");
		}

		public void onSuccess(Map mapi) {

		}

	}

	private class visitContactClickhandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			row = (clGrid.getCellForEvent(event).getRowIndex());
			Window.alert(Integer.toString(row));
		}
	}

	private class shareSelectedContactsClickhandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			Window.alert(Integer.toString(set1.size()));

		}
	};

	private class shareCotactListClickhandler implements ClickHandler {

		public void onClick(ClickEvent event) {

			RootPanel.get("content").clear();
			topPanel.clear();
			buttonPanel.clear();
			RootPanel.get("content").add(topPanel);
			RootPanel.get("content").add(buttonPanel);

			topPanel.add(new HTML("<h2> Teilen von " + clArray.get(row).getName()));
			topPanel.add(userListbox);

			userListbox.setEnabled(true);

			// multi auswahl freischalten in ListBox
			userListbox.ensureDebugId("cwListBox-multiBox");
			userListbox.setVisibleItemCount(7);
			// Alle Kontaktlisten aus DB abrufen
			// TODO nur KOntaktlisten des aktuellen Users abrufen!
			ClientSideSettings.getConnectedAdmin().findAllUser(new AsyncCallback<ArrayList<User>>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Die Kontaktlisten konnten nicht geladen werden");
				}

				@Override
				// jede Kontaktliste wird der ListBox hinzugefügt
				public void onSuccess(ArrayList<User> result) {
					for (User u : result) {
						userListbox.addItem(u.getLogEmail());
					}

				}

			});

		}
	};

}