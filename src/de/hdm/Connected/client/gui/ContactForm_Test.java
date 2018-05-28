package de.hdm.Connected.client.gui;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.google.appengine.labs.repackaged.com.google.common.collect.Multiset.Entry;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowScrollListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.ConnectedAdminAsync;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.ContactList;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.Value;

public class ContactForm_Test extends Widget {

	/**
	 * Klasse für die Bereistellung eines Formulars zum Anlegen/Bearbeiten eines
	 * Kontakts
	 * 
	 * @author Philipp
	 *
	 */

	// ConnectedAdminAsync connectedAdmin =
	// ClientSideSettings.getConnectedAdmin();
	private Contact selectedContact = null;
	private Contact createdContact = null;

	Label firstNameLabel = new Label("Vorname:");
	TextBox firstNameBox = new TextBox();
	Label surnameLabel = new Label("Nachname:");
	TextBox surnameBox = new TextBox();
	TextBox newPropertyTextBox = null;
	ListBox propertyListBox = null;

	TextBox valueTextBox = null;
	Button addButton = new Button("weitere Eigenschaften hinzufügen");
	Button newPropertyBtn = new Button("+");
	HorizontalPanel itemPanel = new HorizontalPanel();

	VerticalPanel propertyPanel = new VerticalPanel();
	VerticalPanel valuePanel = new VerticalPanel();

	FlexTable nameTable = new FlexTable();
	FlexTable propertyTable = new FlexTable();

	ArrayList<Property> propertyArray = new ArrayList<Property>();
	ArrayList<Integer> selectedProperties = new ArrayList<Integer>();
	ArrayList<String> propertyValueArray = new ArrayList<String>();
	ArrayList<TextBox> values = new ArrayList<TextBox>();
	Map<Integer, String> valuesMap = new HashMap<Integer, String>();
	Map<Widget, Widget> widgetMap = new HashMap<Widget, Widget>();

	FlexTable checkboxTable = new FlexTable();
	CheckBox checkContactlist = new CheckBox();
	final ListBox contactlist = new ListBox(true);

	/**
	 * Konstruktor wenn ein Kontakt schon existiert.
	 * 
	 * @param contact
	 */

	public ContactForm_Test(Contact contact) {

		this.selectedContact = contact;

		RootPanel.get("content").clear();

	}

	/**
	 * Konstruktor wenn ein neuer Kontakt angelegt wird.
	 */

	public ContactForm_Test() {

		// RootPanel.get("content").clear();

		HorizontalPanel topPanel = new HorizontalPanel();
		/*
		 * TODO style erstellen in CSS topPanel.addStyleName("headline");
		 */

		topPanel.add(new HTML("<h2> Neuen Kontakt erstellen</h2>"));

		RootPanel.get("content").add(topPanel);

		nameTable.setWidget(0, 0, new HTML("<h3> Vorname: </h3>"));
		nameTable.setWidget(0, 1, firstNameBox);
		nameTable.setWidget(1, 0, new HTML("<h3> Nachname: </h3>"));
		nameTable.setWidget(1, 1, surnameBox);
		nameTable.setWidget(2, 0, addButton);
		// Eigenschaft "+" hinzufügen mit ListBox für Vorgaben bzw. für eigene
		// des Users

		addButton.addClickHandler(new addNewPropertyClickHandler());

		itemPanel.add(propertyPanel);
		itemPanel.add(valuePanel);

		RootPanel.get("content").add(itemPanel);
		RootPanel.get("content").add(nameTable);
		RootPanel.get("content").add(propertyTable);

		HorizontalPanel bottomPanel = new HorizontalPanel();

		/**
		 * java.util.Date berücksichtigt auch Stunden, Minuten, Sekunden und
		 * sogar Millisekunden, java.sql.Date hingegen entspricht dem SQL DATE.
		 * Deshalb muss das Datum vorher konvertiert werden.
		 */

		Button saveButton = new Button("Kontakt erstellen");
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			// ---------- saveButton ClickHandler------------

			public void onClick(ClickEvent event) {
				java.sql.Timestamp creationTime = new Timestamp(System.currentTimeMillis());

				ClientSideSettings.getConnectedAdmin().createContact(firstNameBox.getText(), surnameBox.getText(),
						creationTime, creationTime, 1, new AsyncCallback<Contact>() {

							@Override
							public void onFailure(Throwable caught) {
								ClientSideSettings.getLogger().severe("Kontakt-Objekt konnte nicht erstellt werden");
								Window.alert("Da geht noch was ned");
							}

							@Override
							public void onSuccess(Contact result) {
								createdContact = result;
								try {
									Iterator<Widget> propertyWidgets = propertyTable.iterator();

									while (propertyWidgets.hasNext()) {
										Widget ch = propertyWidgets.next();
										if (ch instanceof ListBox) {
											ListBox propertyListBox = (ListBox) ch;
											for (int i = 0; i < propertyArray.size(); i++) {
												if (propertyListBox.getSelectedItemText() == propertyArray.get(i)
														.getName()) {
													selectedProperties.add(propertyArray.get(i).getBoId());
												}
											}

										} else if (ch instanceof TextBox) {
											TextBox valueTextBox = (TextBox) ch;
											values.add(valueTextBox);
										}
										Window.alert(Integer.toString(propertyArray.size()));

									}

									for (int i = 0; i < selectedProperties.size(); i++) {

										ClientSideSettings.getConnectedAdmin().createValue(values.get(i).getText(),
												selectedProperties.get(i), result.getBoId(), new createValueCallback());
									}
								} catch (Exception e) {
									Window.alert(e.toString());
									e.printStackTrace();
								}
							
							}

						});

			}

		});

		bottomPanel.add(saveButton);

		Button cancelButton = new Button("Abbrechen");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.reload();
			}
		});

		bottomPanel.add(cancelButton);
		
		
		checkContactlist.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
			if(checkContactlist.getValue()){
				contactlist.setEnabled(true);
				checkboxTable.setWidget(0, 1, new HTML("<h3> Bitte eine oder mehrere Kontaktlisten auswählen: </h3>"));
			}else {
				contactlist.setEnabled(false);
				checkboxTable.setWidget(0, 1, new HTML("<h3 style=\"color:#D3D3D3;\"> Bitte eine oder mehrere Kontaktlisten auswählen: </h3>"));
			}
				
			}
			
		});
		//multi auswahl freischalten in ListBox
		contactlist.ensureDebugId("cwListBox-multiBox");
		contactlist.setVisibleItemCount(7);
		//Alle Kontaktlisten aus DB abrufen
		ClientSideSettings.getConnectedAdmin().findAllContactlists(new AsyncCallback<ArrayList<ContactList>>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Die Kontaktlisten konnten nicht geladen werden");
			}

			@Override
			public void onSuccess(ArrayList<ContactList> result) {
				for(ContactList cl : result){
					contactlist.addItem(cl.getName());
				}
				
			}
			
		});		
		//ListBox deaktivieren, da CheckBox nicht aktiviert.
		contactlist.setEnabled(false);
	
		
		RootPanel.get("content").add(new HTML("<h3> Kontakt einer Kontaktliste hinzufügen? </h3>"));
		checkboxTable.setWidget(0, 0, checkContactlist);
		checkboxTable.setWidget(0, 1, new HTML("<h3 style=\"color:#D3D3D3;\"> Bitte eine oder mehrere Kontaktlisten auswählen: </h3>"));
		checkboxTable.setWidget(0, 2, contactlist);
		
		
		RootPanel.get("content").add(checkboxTable);
		RootPanel.get("content").add(bottomPanel);

	}

	private class findAllPropertiesCallback implements AsyncCallback<ArrayList<Property>> {

		@Override
		public void onFailure(Throwable caught) {

			ClientSideSettings.getLogger().severe("Konnte nicht laden");
			Window.alert("Da ist wohl etwas schief gelaufen");

		}

		public void onSuccess(ArrayList<Property> result) {
			// alle Eigenschaften in Vektor laden

			propertyListBox = new ListBox();
			propertyListBox.setWidth("250px");
			for (int i = 0; i < result.size(); i++) {
				Property propertyItem = result.get(i);

				if (propertyItem.getName() != "Vorname" || propertyItem.getName() != "Nachname") {

					propertyArray.add(propertyItem);
					propertyListBox.addItem(propertyArray.get(i).getName());
				}

			}
			propertyListBox.addItem("oder neue Eigenschaft hinzufügen...");
			propertyListBox.addChangeHandler(new listBoxChangeHandler());
			int rowCount = propertyTable.getRowCount();
			propertyTable.setWidget(rowCount - 1, 0, propertyListBox);

		}

	}

	// ----Clickhandler für add Button-----
	private class addNewPropertyClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			ClientSideSettings.getConnectedAdmin().findAllProperties(new findAllPropertiesCallback());

			// if(propertyListBox !=null)
			// {selectedProperties.add(propertyListBox);
			// insertValue.add(valueTextBox);}
			// wennn kein "+" Button dann den addbutton entfernen sonst den "+"
			// Button entfernen

			if (addButton != null) {
				addButton.removeFromParent();
				// propertyTable.setWidget(rowCount, 0, propertyListBox);
				addButton = null;

			} else {
				newPropertyBtn.removeFromParent();
				newPropertyBtn = null;
			}

			newPropertyBtn = new Button("+");
			// propertyListBox = new ListBox();
			valueTextBox = new TextBox();

			int rowCount = propertyTable.getRowCount();
			// propertyTable.setWidget(rowCount, 0, propertyListBox);
			propertyTable.setWidget(rowCount, 1, valueTextBox);
			propertyTable.setWidget(rowCount, 2, newPropertyBtn);

			newPropertyBtn.addClickHandler(new addNewPropertyClickHandler());
			propertyArray.clear();

		}

	}

	// ----------------createValueCallback

	private class createValueCallback implements AsyncCallback<Value> {

		@Override
		public void onFailure(Throwable caught) {
			ClientSideSettings.getLogger().severe("Konnte nicht laden");
			Window.alert("Da ist wohl beim Value etwas schief gelaufen");

		}

		@Override
		public void onSuccess(Value result) {
			Window.alert("Kontakt vollständig angelegt");
			if(checkContactlist.getValue()){
				for (int i=0; i< contactlist.getItemCount(); i++){
					if(contactlist.isItemSelected(i)) {
						ClientSideSettings.getConnectedAdmin().addContactToContactList(createdContact.getBoId(), i, new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
							Window.alert("Kontakt konnte Kontaktliste nicht hinzugefügt werden");
							}

							@Override
							public void onSuccess(Void result) {
							Window.alert("Kontakt wurde angelegt und den Kontaktlisten hinzugefügt!");
								
							}
							
						});
					}
					
				}
				
			}
		
			RootPanel.get("content").clear();

		}

	}

	// ---------------------Change Handler der
	// Eigenschafts-Listboxen------------------------------

	private class listBoxChangeHandler implements ChangeHandler {

		@Override
		public void onChange(ChangeEvent event) {
			// TODO Auto-generated method stub
			if (propertyListBox.getSelectedItemText().equals("oder neue Eigenschaft hinzufügen...")) {
				int rowCount = propertyTable.getRowCount();
				propertyTable.removeRow(rowCount - 1);
				newPropertyTextBox = new TextBox();
				Button propertySaveButton = new Button("Speichern");
				propertySaveButton.addClickHandler(new savePropertyClickHandler());
				propertyTable.setWidget(rowCount, 0, new HTML("Eigenschaftsname:"));
				propertyTable.setWidget(rowCount, 1, newPropertyTextBox);
				propertyTable.setWidget(rowCount, 2, propertySaveButton);
			}

		}

	}

	// ---------------------Click Handler für den neue Eigenschaft speichern
	// Button----------------------

	private class savePropertyClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			ClientSideSettings.getConnectedAdmin().createProperty(newPropertyTextBox.getText(),
					new AsyncCallback<Property>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Eigenschaft nicht gespeichert");

						}

						@Override

						public void onSuccess(Property result) {

							int rowCount = propertyTable.getRowCount();

							ArrayList<Integer> selectedItems = new ArrayList<Integer>();
							Iterator<Widget> listBoxWidgets = propertyTable.iterator();

							while (listBoxWidgets.hasNext()) {
								Widget w = listBoxWidgets.next();

								if (w instanceof ListBox) {
									ListBox oldListbox = (ListBox) w;
									selectedItems.add(oldListbox.getSelectedIndex());
									oldListbox.removeFromParent();
								}
							}

							propertyArray.add(result);

							propertyListBox.setItemText(propertyListBox.getItemCount() - 1, result.getName());

							propertyListBox.addItem("oder neue Eigenschaft hinzufügen...");

							propertyTable.removeRow(rowCount - 1);

							for (int i = 0; i < selectedItems.size(); i++) {
								ListBox propertyListBoxnew = new ListBox();
								propertyListBoxnew.setWidth("250px");
								for (Property p : propertyArray) {
									propertyListBoxnew.addItem(p.getName());
								}
								propertyTable.setWidget(i, 0, propertyListBoxnew);
								propertyListBoxnew.setSelectedIndex(selectedItems.get(i));
								Window.alert(Integer.toString(i));
							}

							propertyTable.setWidget(rowCount, 0, propertyListBox);
							propertyTable.setWidget(rowCount, 1, valueTextBox);
							propertyTable.setWidget(rowCount, 2, newPropertyBtn);

						}

					});

		}
	}
}
