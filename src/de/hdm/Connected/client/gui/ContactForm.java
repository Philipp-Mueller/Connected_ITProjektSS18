package de.hdm.Connected.client.gui;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.Vector;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.google.gwt.dev.javac.Shared;
//import com.google.appengine.labs.repackaged.com.google.common.collect.Multiset.Entry;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
//import com.google.gwt.thirdparty.javascript.jscomp.Result;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowScrollListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.client.Connected_ITProjektSS18;
import de.hdm.Connected.shared.ConnectedAdminAsync;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.ContactList;
import de.hdm.Connected.shared.bo.Permission;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

public class ContactForm extends PopupPanel {

	/**
	 * Klasse für die Bereistellung eines Formulars zum Anlegen/Bearbeiten eines
	 * Kontakts
	 * 
	 * @author Philipp
	 *
	 */

	private Contact selectedContact = null;
	private Contact createdContact = null;

	private Label firstNameLabel = new Label("Vorname:");
	private TextBox firstNameBox = new TextBox();
	private Label surnameLabel = new Label("Nachname:");
	private TextBox surnameBox = new TextBox();
	private TextBox newPropertyTextBox = null;
	private ListBox propertyListBox = null;
	private DateBox birthday = new DateBox();

	private TextBox valueTextBox = null;
	private Button addButton = new Button("weitere Eigenschaften hinzufügen");
	private Button newPropertyBtn = null;
	private Button updateBtn = null;
	private Button deleteBtn = null;
	private Button closeButton = new Button("Schließen");
	private HorizontalPanel itemPanel = new HorizontalPanel();

	private VerticalPanel propertyPanel = new VerticalPanel();
	private VerticalPanel valuePanel = new VerticalPanel();
	private VerticalPanel root = new VerticalPanel();

	private FlexTable nameTable = new FlexTable();
	private FlexTable propertyTable = new FlexTable();
	private FlexTable newPropertyTable = new FlexTable();

	private ArrayList<Property> propertyArray = new ArrayList<Property>();
	private ArrayList<Integer> selectedProperties = new ArrayList<Integer>();
	private ArrayList<String> propertyValueArray = new ArrayList<String>();
	private ArrayList<TextBox> values = new ArrayList<TextBox>();
	private Map<Integer, String> valuesMap = new HashMap<Integer, String>();
	private Map<Widget, Widget> widgetMap = new HashMap<Widget, Widget>();
	private ContactList selectedCL = new ContactList();
	private ArrayList<Contact> contactsInCL = new ArrayList<Contact>();

	private FlexTable checkboxTable = new FlexTable();
	private CheckBox checkContactlist = new CheckBox();
	final ListBox contactlist = new ListBox(true);
	int eventRow = 0;
	private String propertyName = "";
	private ArrayList<Value> valuesArray = new ArrayList<Value>();
	private Value updatingValue = null;
	boolean birthdayFlag = false;
	boolean loadFlag = true;

	private ArrayList<ContactList> contactListArray = null;
	private ArrayList<Value> valuesByContact;

	/**
	 * Konstruktor wenn ein Kontakt schon existiert.
	 * 
	 * @param contact
	 */

	public ContactForm(Contact contact, final ContactList contactList, final ArrayList<Contact> contacts) {
		this.selectedContact = contact;
		
		selectedCL = contactList;
		contactsInCL = contacts;
		


		// Enable animation.
		this.setAnimationEnabled(true);
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Popup schließen bei Betägigung des Buttons
				hide();
				
			}
		});


		root.add(new HTML("<h3> Kontakt bearbeiten</h3>"));
		root.add(nameTable);
		root.add(propertyTable);

		// Wenn Kontakt gefunden, dann die Values dazu finden.

		Button changeNameButton = new Button("Namen ändern");

		firstNameBox.setText(selectedContact.getPrename());
		surnameBox.setText(selectedContact.getSurname());

		changeNameButton.addClickHandler(new changeNameClickHandler());
		nameTable.getColumnFormatter().setWidth(0, "100px");
		propertyTable.getColumnFormatter().setWidth(0, "150px");
		newPropertyTable.getColumnFormatter().setWidth(0, "150px");
		//NameTable erstellen mit Vor/Nachname
		nameTable.setWidget(0, 0, new HTML("<strong>Vorname: <strong>"));
		nameTable.setWidget(0, 1, new HTML(selectedContact.getPrename()));
		nameTable.setWidget(1, 0, new HTML("<strong>Nachname: <strong>"));
		nameTable.setWidget(1, 1, new HTML(selectedContact.getSurname()));
		nameTable.getFlexCellFormatter().setRowSpan(1, 2, 2);
		nameTable.getFlexCellFormatter().setAlignment(1, 2, HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
		nameTable.setWidget(1, 2, changeNameButton);
		propertyTable.setCellSpacing(10);
		newPropertyTable.setCellSpacing(10);
		
		//Alle Properties abrufen
		ClientSideSettings.getConnectedAdmin().findAllProperties(new findAllPropertiesCallback());
	}

	/**
	 * Konstruktor wenn ein neuer Kontakt angelegt wird.
	 */

	public ContactForm() {

		this.setAnimationEnabled(true);
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Popup schließen bei Betägigung des Buttons
				hide();
				RootPanel.get("content").clear();
				ContactsTable overview = new ContactsTable(null, null);

			}
		});

		VerticalPanel root = new VerticalPanel();

		HorizontalPanel topPanel = new HorizontalPanel();

		topPanel.add(new HTML("<h2> Neuen Kontakt erstellen</h2>"));

		root.add(topPanel);
		
		//NameTable erstellen mit Vor/Nachname
		nameTable.getColumnFormatter().setWidth(0, "150px");
		nameTable.getFlexCellFormatter().setWidth(2, 0, "200px");
		nameTable.setWidget(0, 0, new HTML("<h3> Vorname: </h3>"));
		nameTable.setWidget(0, 1, firstNameBox);
		nameTable.setWidget(1, 0, new HTML("<h3> Nachname: </h3>"));
		nameTable.setWidget(1, 1, surnameBox);
		nameTable.setWidget(2, 0, addButton);
		// Eigenschaft "+" hinzufügen mit ListBox für Vorgaben bzw. für eigene
		// des Users
		propertyTable.getColumnFormatter().setWidth(0, "300px");
		propertyTable.getColumnFormatter().setWidth(1, "200px");
		propertyTable.setCellSpacing(8);

		newPropertyTable.getColumnFormatter().setWidth(0, "300px");
		newPropertyTable.getColumnFormatter().setWidth(1, "200px");
		propertyTable.setCellSpacing(8);

		// Erste neue eigenschaften Hinzufügen
		addButton.addClickHandler(new addNewPropertyClickHandler());

		itemPanel.add(propertyPanel);
		itemPanel.add(valuePanel);

		// Widgets dem rootPanel hinzufügen
		root.add(itemPanel);
		root.add(nameTable);
		root.add(propertyTable);
		root.add(newPropertyTable);

		HorizontalPanel bottomPanel = new HorizontalPanel();

	

		Button saveButton = new Button("Kontakt abspeichern");
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			/**
			 * save Button clickHander, wenn Kontakt gespeichert wird.
			 */

			public void onClick(ClickEvent event) {
				// Nur wenn die CheckBox geklickt ist, wird dies ausgeführt, da
				// sonst der Kontakt keiner Liste hinzugefügt werden soll
				
				if (firstNameBox.getText().matches("")) {
					Window.alert("Bitte einen Namen eintragen!");
					return;
				}
				if (surnameBox.getText().matches("")) {
					Window.alert("Bitte einen Namen eintragen!");
					return;
				}
				final ArrayList<ContactList> contactListToAdd = new ArrayList<ContactList>();

				if (checkContactlist.getValue()) {
					for (int i = 0; i < contactlist.getItemCount(); i++) {
						if (contactlist.isItemSelected(i)) {
							for (ContactList cl : contactListArray) {
								if (contactlist.getItemText(i).equals(cl.getName())) {
									contactListToAdd.add(cl);
								}
							}
						}
					}
				}
				//wenn ohne Eigenschaften gespeichert wird
				if (addButton != null) {

					java.sql.Date creationTime = new java.sql.Date(System.currentTimeMillis());
					ClientSideSettings.getConnectedAdmin().createContact(firstNameBox.getText(), surnameBox.getText(),
							creationTime, creationTime, ClientSideSettings.getCurrentUser().getBoId(),
							new AsyncCallback<Contact>() {
								

								@Override
								public void onFailure(Throwable caught) {

									Window.alert("Konnte Eigenschaft nicht anlegen");
								}

								@Override
								public void onSuccess(Contact result) {
									createdContact = result;
									ArrayList<Contact> contacts = new ArrayList<Contact>();
									contacts.add(result);
								
									

									if (contactListToAdd.size() != 0) {
										ClientSideSettings.getConnectedAdmin().addContactsToContactList(contacts,
												contactListToAdd, new AsyncCallback<Void>() {

													@Override
													public void onFailure(Throwable caught) {
														Window.alert(
																"Kontakt konnte Kontaktliste nicht hinzugefügt werden");
													}

													@Override
													public void onSuccess(Void result) {
														Window.alert(
																"Kontakt wurde angelegt und den Kontaktlisten hinzugefügt!");

													}

												});
									}else {
										Window.alert("Kontakt wurde angelegt!");
									}
									hide();
									RootPanel.get("content").clear();
									ContactsTable table = new ContactsTable(null, null);
								}

							});
				}
			else {
				
				//Aufruf wenn Kontakt direkt KOntaktliste hinzugefügt werden soll
				if (contactListToAdd.size() != 0) {
					ArrayList<Contact> contacts = new ArrayList<Contact>();
					ClientSideSettings.getConnectedAdmin().addContactsToContactList(contacts,
							contactListToAdd, new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert(
											"Kontakt konnte Kontaktliste nicht hinzugefügt werden");
								}

								@Override
								public void onSuccess(Void result) {
									Window.alert(
											"Kontakt wurde angelegt und den Kontaktlisten hinzugefügt!");

								
								RootPanel.get("content").clear();
								ContactsTable table = new ContactsTable(null, null);
								Window.alert("Kontakt angelegt!");
								hide();
								}

							});
				} else{
				RootPanel.get("content").clear();
				ContactsTable table = new ContactsTable(null, null);
				Window.alert("Kontakt angelegt!");
				hide();
				}	
				}

			}

		});

		bottomPanel.add(saveButton);
		/**
		 * cancelButton Clickhandler
		 * 
		 */
		Button cancelButton = new Button("Abbrechen");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(createdContact != null){
				ClientSideSettings.getConnectedAdmin().deleteContact(createdContact, ClientSideSettings.getCurrentUser(), new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						
						
					}

					@Override
					public void onSuccess(Void result) {
						hide();
						createdContact = null;
					}
					
				});
				}else{
					hide();
				}
			

			}
		});

		bottomPanel.add(cancelButton);

		/**
		 * contactlist auswählen Clickhandler
		 * 
		 */
		
		checkContactlist.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (checkContactlist.getValue()) {
					contactlist.setEnabled(true);
					checkboxTable.setWidget(0, 1,
							new HTML("<h3> Bitte eine oder mehrere Kontaktlisten auswählen: </h3>"));
				} else {
					contactlist.setEnabled(false);
					checkboxTable.setWidget(0, 1, new HTML(
							"<h3 style=\"color:#D3D3D3;\"> Bitte eine oder mehrere Kontaktlisten auswählen: </h3>"));
				}

			}

		});
		// multi auswahl freischalten in ListBox
		contactlist.ensureDebugId("cwListBox-multiBox");
		contactlist.setVisibleItemCount(7);
		// Alle Kontaktlisten aus DB abrufen
		
		ClientSideSettings.getConnectedAdmin().getContactListsByUserPermission(
				ClientSideSettings.getCurrentUser().getBoId(), new AsyncCallback<ArrayList<ContactList>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Die Kontaktlisten konnten nicht geladen werden");
					}

					@Override
					// jede Kontaktliste wird der ListBox hinzugefügt
					public void onSuccess(ArrayList<ContactList> result) {
						contactListArray = result;
						for (ContactList cl : result) {
							contactlist.addItem(cl.getName());
						}

					}

				});
		// ListBox deaktivieren, da CheckBox nicht aktiviert.
		contactlist.setEnabled(false);

		// Dies dem FlexTable hinzufügen
		root.add(new HTML("<h3> Kontakt einer Kontaktliste hinzufügen? </h3>"));
		checkboxTable.setWidget(0, 0, checkContactlist);
		// Disabled Style
		checkboxTable.setWidget(0, 1,
				new HTML("<h3 style=\"color:#D3D3D3;\"> Bitte eine oder mehrere Kontaktlisten auswählen: </h3>"));
		checkboxTable.setWidget(0, 2, contactlist);

		root.add(checkboxTable);
		root.add(bottomPanel);
		setWidget(root);

	}
 /**
  * 	FindValueCallback, wenn ein Kontakt bearbeitet wird , werden die Values abgerufen und dem FlexTable hinzugefügt.
  * 
  */
	private class findValueCallback implements AsyncCallback<ArrayList<Value>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Value konnte nicht gefunden werden");

		}

		@Override
		public void onSuccess(ArrayList<Value> result) {

			valuesByContact = result;

			try {

				for (final Value v : valuesByContact) {
					propertyListBox = new ListBox();
					final Value updatingOldValue = v;
					ClientSideSettings.getConnectedAdmin().findPropertyByPropertyId(updatingOldValue.getPropertyID(),
							new AsyncCallback<Property>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Property nicht gefunden");

								}

								@Override
								public void onSuccess(Property result) {
									final Property p = result;
									int rowCount = propertyTable.getRowCount();
									if (result.getName().equals("Geburtsdatum")) {
										for (int i = 0; i < propertyListBox.getItemCount(); i++) {
											if (propertyListBox.getItemText(i).equals("Geburtsdatum")) {
												propertyListBox.removeItem(i);
												birthdayFlag = true;
											}
										}
									}
									updateBtn = new Button("Eigenschaft bearbeiten");
									updateBtn.addClickHandler(new ClickHandler() {
										/**`
										 * update Btn ClickHandler
										 */
										@Override
										public void onClick(ClickEvent event) {
											eventRow = propertyTable.getCellForEvent(event).getRowIndex();
											final TextBox valueChangeTextBox = new TextBox();
											valueChangeTextBox.setWidth("200px");
											
										
											valueChangeTextBox.setText(updatingOldValue.getName());

											ClientSideSettings.getConnectedAdmin().findPropertyByPropertyId(
													updatingOldValue.getPropertyID(), new AsyncCallback<Property>() {
														@Override
														public void onFailure(Throwable caught) {
															Window.alert("Eigenschaft konnte nicht gefunden werden");

														}

														@Override
														public void onSuccess(Property result) {
															final Property changingProperty = result;
															final ListBox propertyChangeListBox = new ListBox();
															propertyChangeListBox
																	.addChangeHandler(new listBoxChangeHandler());

															for (int i = 0; i < propertyArray.size(); i++) {
																if (birthdayFlag) {
																	if (propertyArray.get(i)
																			.getName() != "Geburtsdatum") {
																		propertyChangeListBox.addItem(
																				propertyArray.get(i).getName());
																		if ((propertyArray.get(i).getName())
																				.equals(changingProperty.getName())) {
																			propertyChangeListBox.setSelectedIndex(i);
																		}
																	} else if (result.getName() == "Geburtsdatum") {
																		propertyChangeListBox.addItem(
																				propertyArray.get(i).getName());
																		if ((propertyArray.get(i).getName())
																				.equals(changingProperty.getName())) {
																			propertyChangeListBox.setSelectedIndex(i);
																		}
																	}
																} else {
																	propertyChangeListBox
																			.addItem(propertyArray.get(i).getName());
																	if ((propertyArray.get(i).getName())
																			.equals(changingProperty.getName())) {
																		propertyChangeListBox.setSelectedIndex(i);
																	}
																}
															}

															Button discardChangesButton = new Button("Abbrechen");
															discardChangesButton.addClickHandler(new ClickHandler() {

																@Override
																public void onClick(ClickEvent event) {
																
																	updatingValue = updatingOldValue;

																	updateBtn = new Button("Eigenschaft bearbeiten");
																	updateBtn.addClickHandler(
																			new updateBtnClickHandler());
																	deleteBtn = new Button("Eigenschaft entfernen");
																	deleteBtn.addClickHandler(
																			new deleteBtnClickHandler());
																	propertyTable.setWidget(eventRow, 0,
																			new HTML("<p><strong>"
																					+ propertyChangeListBox
																							.getSelectedItemText()
																					+ ":</strong></p>"));
																	propertyTable.setWidget(eventRow, 1, new HTML("<p>"
																			+ updatingOldValue.getName() + "</p>"));
																	propertyTable.setWidget(eventRow, 2, updateBtn);
																	propertyTable.setWidget(eventRow, 3, deleteBtn);

																}

															});
															
															/**
															 * Änderungen Speichern Clickhandler
															 * 
															 */

															Button saveChangesButton = new Button(
																	"Änderungen speichern");
															saveChangesButton.addClickHandler(new ClickHandler() {
															
															
																@Override
																public void onClick(ClickEvent event) {
																	if (valueChangeTextBox.getText().matches("")) {
																		Window.alert("Bitte eine Eigenschaft eintragen!");
																		return;
																	}
																	eventRow = propertyTable.getCellForEvent(event)
																			.getRowIndex();
																	int propertyId = 0;
																	final int oldPropertyId = updatingOldValue
																			.getPropertyID();
																	for (Property p : propertyArray) {
																		if ((propertyChangeListBox
																				.getSelectedItemText())
																						.equals(p.getName())) {
																			propertyId = p.getBoId();
																		}
																	}

																							
																	try {
																		if (propertyChangeListBox.getSelectedItemText().equals("Geburtsdatum")) {
																			birthdayFlag = true;
																			DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
																			java.util.Date date = dateTimeFormat.parse(valueChangeTextBox.getText());
																			updatingOldValue.setName(valueChangeTextBox.getText());
																		} else if (oldPropertyId == 1) {
																			birthdayFlag = false;
																			updatingOldValue.setName(valueChangeTextBox.getText());
																		} else{
																			updatingOldValue.setName(valueChangeTextBox.getText());
																		}
																	} catch (Exception e) {
																		Window.alert("Geburtsdatum bitte im Format \"01.01.99\" eingeben");
																		return;
																	}
																	
																	updatingOldValue
																			.setName(valueChangeTextBox.getText());
																	updatingOldValue.setPropertyID(propertyId);
																	/**
																	 * Updaten des ausgwählten bzw. geänderten Values
																	 * 
																	 */
																	
																	ClientSideSettings.getConnectedAdmin().updateValue(
																			updatingOldValue, oldPropertyId,
																			new AsyncCallback<Value>() {
																				
																				@Override
																				public void onFailure(
																						Throwable caught) {
																					Window.alert(
																							"Eigenschaft konnte nicht aktualisiert werden");
																				}

																				@Override
																				public void onSuccess(
																						final Value result) {
																					Window.alert(Boolean.toString(birthdayFlag));
																					ClientSideSettings
																							.getConnectedAdmin()
																							.checkIfPropertyHasValue(
																									oldPropertyId,
																									new AsyncCallback<Void>() {

																										@Override
																										public void onFailure(
																												Throwable caught) {
																											Window.alert(
																													"Wurde leider nicht gefunden");

																										}

																										@Override
																										public void onSuccess(
																												Void result) {
																											ClientSideSettings
																													.getConnectedAdmin()
																													.findAllProperties(
																															new findAllPropertiesCallback());

																										}

																									});
																					updatingValue = result;
																					//aktualiserte Row anzeigen	
																					updateBtn = new Button(
																							"Eigenschaft bearbeiten");
																					updateBtn.addClickHandler(
																							new updateBtnClickHandler());
																					deleteBtn = new Button(
																							"Eigenschaft entfernen");
																					deleteBtn.addClickHandler(
																							new deleteBtnClickHandler());
																					propertyTable.setWidget(eventRow, 0,
																							new HTML("<p><strong>"
																									+ propertyChangeListBox
																											.getSelectedItemText()
																									+ ":</strong></p>"));
																					propertyTable.setWidget(eventRow, 1,
																							new HTML("<p>"
																									+ valueChangeTextBox
																											.getText()
																									+ "</p>"));
																					propertyTable.setWidget(eventRow, 2,
																							updateBtn);
																					propertyTable.setWidget(eventRow, 3,
																							deleteBtn);

																				}

																			});
																}

															});
															//Bearbeitungsmodus in Zeile anheften
															propertyTable.removeCell(eventRow, 3);
															propertyTable.setWidget(eventRow, 0, propertyChangeListBox);
															propertyTable.setWidget(eventRow, 1, valueChangeTextBox);
															propertyTable.setWidget(eventRow, 2, saveChangesButton);
															propertyTable.setWidget(eventRow, 3, discardChangesButton);
														}

													});
										}

									});
									/**
									 * Eigenschaft löschen ClickHandler
									 * 
									 */
									deleteBtn = new Button("Eigenschaft entfernen");
									deleteBtn.addClickHandler(new ClickHandler() {

										@Override
										public void onClick(ClickEvent event) {
											eventRow = propertyTable.getCellForEvent(event).getRowIndex();

											ClientSideSettings.getConnectedAdmin().deleteValue(updatingOldValue,
													new AsyncCallback<Void>() {

														@Override
														public void onFailure(Throwable caught) {
															Window.alert("Das Löschen ging schief");

														}

														@Override
														public void onSuccess(Void result) {
															Window.alert("Eigenschaft wurde gelöscht!");
															propertyTable.removeRow(eventRow);
															if (p.getName().equals("Geburtsdatum")) {
																birthdayFlag = false;
															}
															ClientSideSettings.getConnectedAdmin()
																	.findAllProperties(new findAllPropertiesCallback());
														}

													});
										}

									});
									//Anzeigen der Eigenschaften, die Verfügbar sind.
									propertyTable.setWidget(rowCount, 0,
											new HTML("<p><strong>" + p.getName() + ":</strong></p>"));
									propertyTable.setWidget(rowCount, 1, new HTML(updatingOldValue.getName()));
									propertyTable.setWidget(rowCount, 2, updateBtn);
									propertyTable.setWidget(rowCount, 3, deleteBtn);

								}

							});
				}
				//Befüllen der ListBox zum Anzeigen
				for (Property property : propertyArray) {
					propertyListBox.addItem(property.getName());
				}
				propertyListBox.addItem("oder neue Eigenschaft hinzufügen...");
				propertyListBox.addChangeHandler(new listBoxChangeHandler());

				newPropertyBtn = new Button("neue Eigenschaft hinzufügen");
				newPropertyBtn.addClickHandler(new addNewPropertyClickHandler());

				valueTextBox = new TextBox();
				valueTextBox.setWidth("200px");

				newPropertyTable.setWidget(0, 0, new HTML("<h3>Neue Eigenschaften hinzufügen</h3>"));
				newPropertyTable.setWidget(1, 0, propertyListBox);
				newPropertyTable.setWidget(1, 1, valueTextBox);
				newPropertyTable.setWidget(1, 2, newPropertyBtn);
				root.add(newPropertyTable);
				root.add(closeButton);
				setWidget(root);

			} catch (Exception e) {
				Window.alert(e.toString());
				e.printStackTrace();
			}

		}
	}
	/**
	 * Hier werden alle Properties abegerufen um diese in der ListBox anzeigen zu können.
	 * 
	 */
	private class findAllPropertiesCallback implements AsyncCallback<ArrayList<Property>> {

		@Override
		public void onFailure(Throwable caught) {

			ClientSideSettings.getLogger().severe("Konnte nicht laden");
			Window.alert("Da ist wohl etwas schief gelaufen");

		}

		public void onSuccess(ArrayList<Property> result) {
			// alle Eigenschaften in Vektor laden
			if (selectedContact != null ) {
				propertyArray = new ArrayList<Property>();
				for (int i = 0; i < result.size(); i++) {
					Property propertyItem = result.get(i);

					propertyArray.add(propertyItem);

				}
				if (loadFlag) {
					ClientSideSettings.getConnectedAdmin().getValuesByUserPermission(selectedContact.getBoId(), ClientSideSettings.getCurrentUser().getBoId(), 
							new findValueCallback());
					loadFlag = false;
				} 
			}
		
			if((selectedContact == null) || (selectedContact != null && !loadFlag) ) {
							
				propertyArray = new ArrayList<Property>();
				propertyListBox = new ListBox();
				propertyListBox.setWidth("250px");
				for (int i = 0; i < result.size(); i++) {
					Property propertyItem = result.get(i);
					propertyArray.add(propertyItem);

					if (birthdayFlag) {
						if (propertyItem.getName() != "Geburtsdatum") {

							propertyListBox.addItem(propertyArray.get(i).getName());
						}
					} else {

						propertyListBox.addItem(propertyArray.get(i).getName());
					}

				}
				propertyListBox.addItem("oder neue Eigenschaft hinzufügen...");
				propertyListBox.addChangeHandler(new listBoxChangeHandler());

				newPropertyTable.setWidget(1, 0, propertyListBox);

			}
		}
	}

	/**
	 * neue EigenschaftsValue hinzufügen und abspeichern Clickhandler
	 * 
	 */
	private class addNewPropertyClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
		
		
			if (selectedContact != null) {
				if (valueTextBox.getText().matches("")) {
					Window.alert("Bitte eine Eigenschaft eintragen!");
					return;
				}
				
				int propertyId = 0;
				for (Property p : propertyArray) {
					if ((propertyListBox.getSelectedItemText()).equals(p.getName())) {
						propertyId = p.getBoId();
					}
				}
				
				String value = "";		
				try{
				
				if(propertyListBox.getSelectedItemText().equals("Geburtsdatum")){
					DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
					java.util.Date date = dateTimeFormat.parse(valueTextBox.getText());
					value = valueTextBox.getText();
				}else {value = valueTextBox.getText();}
				} catch (Exception e) {
					Window.alert("Geburtsdatum bitte im Format \"01.01.99\" eingeben");
					return;
				}
				
				ClientSideSettings.getConnectedAdmin().createValue(value, propertyId,
						selectedContact.getBoId(), ClientSideSettings.getCurrentUser().getBoId(),
						new AsyncCallback<Value>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Konnte Eigenschaft nicht erstellen");
							}

							@Override
							public void onSuccess(Value result) {
								// die Reihen Widget setzen
								int rowCount = propertyTable.getRowCount();
								updatingValue = result;
								valueTextBox.setText("");
								updateBtn = new Button("Eigenschaft bearbeiten");
								deleteBtn = new Button("Eigenschaft entfernen");
								updateBtn.addClickHandler(new updateBtnClickHandler());
								deleteBtn.addClickHandler(new deleteBtnClickHandler());

								propertyTable.setWidget(rowCount, 0,
										new HTML("<strong>" + propertyListBox.getSelectedItemText() + ":</strong>"));
								propertyTable.setWidget(rowCount, 1, new HTML(result.getName()));
								propertyTable.setWidget(rowCount, 2, updateBtn);
								propertyTable.setWidget(rowCount, 3, deleteBtn);

								ContactSharing shareValue = new ContactSharing(selectedContact, result);
								if (propertyListBox.getSelectedItemText().equals("Geburtsdatum")) {
									birthdayFlag = true;
								}

							}

						});
			} else {

				if (newPropertyBtn != null) {
					if (propertyTable.getRowCount() != 0) {
						eventRow = newPropertyTable.getCellForEvent(event).getRowIndex();
					} else {
						eventRow = 0;
					}
				}

				if (addButton != null) {

					try {
						
						if (firstNameBox.getText().matches("")) {
							Window.alert("Bitte einen Vorname eintragen!");
							return;
						}
						if (surnameBox.getText().matches("")) {
							Window.alert("Bitte Nachname eintragen!");
							return;
						}
						java.sql.Date creationTime = new java.sql.Date(System.currentTimeMillis());
						ClientSideSettings.getConnectedAdmin().createContact(firstNameBox.getText(),
								surnameBox.getText(), creationTime, creationTime,
								ClientSideSettings.getCurrentUser().getBoId(), new AsyncCallback<Contact>() {

									@Override
									public void onFailure(Throwable caught) {

										Window.alert("Kontakt konnte nicht angelegt werden");
									}

									@Override
									public void onSuccess(Contact result) {
										createdContact = result;

										Label prenameLabel = new Label(createdContact.getPrename());
										Label surnameLabel = new Label(createdContact.getSurname());
										nameTable.setWidget(0, 1, prenameLabel);
										nameTable.setWidget(1, 1, surnameLabel);

										addButton.removeFromParent();

										addButton = null;

										ClientSideSettings.getConnectedAdmin()
												.findAllProperties(new findAllPropertiesCallback());

										int rowCount = newPropertyTable.getRowCount();
										newPropertyBtn = new Button("Eigenschaft speichern");
										newPropertyBtn.addClickHandler(new addNewPropertyClickHandler());
										valueTextBox = new TextBox();
										valueTextBox.setWidth("200px");

										newPropertyTable.setWidget(0, 0,
												new HTML("<h3>Neue Eigenschaften hinzufügen</h3>"));
									
										newPropertyTable.setWidget( 1, 1, valueTextBox);
										newPropertyTable.setWidget( 1, 2, newPropertyBtn);

									}
								});
					} catch (Exception e) {

						e.printStackTrace();
					}
				} else {
					if (valueTextBox.getText().matches("")) {
						Window.alert("Bitte eine Eigenschaft eintragen!");
						return;
					}
					
					int propertyId = 0;
					newPropertyBtn.removeFromParent();
					newPropertyBtn = null;
					propertyName = propertyListBox.getSelectedItemText();
					for (Property p : propertyArray) {
						if ((propertyListBox.getSelectedItemText()).equals(p.getName())) {
							propertyId = p.getBoId();
							if (p.getName().equals("Geburtsdatum")) {
								birthdayFlag = true;
							}
						}
					}
				
					try {
						if(propertyListBox.getSelectedItemText().equals("Geburtsdatum")){
							DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
							java.util.Date date = dateTimeFormat.parse(valueTextBox.getText());
						
							ClientSideSettings.getConnectedAdmin().createValue(valueTextBox.getText(),propertyId,
									createdContact.getBoId(), ClientSideSettings.getCurrentUser().getBoId(),
									new createValueCallback());					
						} else{			
						
						ClientSideSettings.getConnectedAdmin().createValue(valueTextBox.getText(), propertyId,
								createdContact.getBoId(), ClientSideSettings.getCurrentUser().getBoId(),
								new createValueCallback());
						}
					} catch (Exception e) {
						Window.alert("Geburtsdatum bitte im Format \"01.01.99\" eingeben");
						return;
					}
					
				}
			}
		}

	}

	/**
	 * Nachdem ein Value erfolgreich gefunden wurde. Wird Die Form aktualisiert und es kann ein neuer Value hinzugefügt werden.
	 * 
	 */

	private class createValueCallback implements AsyncCallback<Value> {

		@Override
		public void onFailure(Throwable caught) {
			ClientSideSettings.getLogger().severe("Konnte nicht laden");
			Window.alert("Da ist wohl beim Value etwas schief gelaufen");

		}

		@Override
		public void onSuccess(final Value result) {
			// Am Ende wird der Kontakt den ausgewählten Kontaktlisten
			// hinzugefügt.
			Window.alert("Eigenschaft erfolgreich hinzugefügt");
			// updatingValue = result;
			final Label propertyLabel = new Label(propertyName);
			Label valueLabel = new Label(result.getName());
			newPropertyTable.removeRow(1);
			newPropertyTable.removeRow(0);

			valueTextBox = new TextBox();
			valueTextBox.setWidth("200px");

			newPropertyBtn = new Button("Eigenschaft hinzufügen");
			newPropertyBtn.addClickHandler(new addNewPropertyClickHandler());

			updateBtn = new Button("Eigenschaft bearbeiten");
			updateBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					final Value updatedValue = result;
					eventRow = propertyTable.getCellForEvent(event).getRowIndex();
					final TextBox valueChangeTextBox = new TextBox();
					valueChangeTextBox.setWidth("200px");
					valueChangeTextBox.setText(updatedValue.getName());

					ClientSideSettings.getConnectedAdmin().findPropertyByPropertyId(updatedValue.getPropertyID(),
							new AsyncCallback<Property>() {
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Property nicht gefunden");

								}

								@Override
								public void onSuccess(Property result) {
									final Property changingProperty = result;
									final ListBox propertyChangeListBox = new ListBox();
									propertyChangeListBox.addChangeHandler(new listBoxChangeHandler());

									for (int i = 0; i < propertyArray.size(); i++) {
										propertyChangeListBox.addItem(propertyArray.get(i).getName());
										if ((propertyArray.get(i).getName()).equals(changingProperty.getName())) {
											propertyChangeListBox.setSelectedIndex(i);
										}
									}

									Button discardChangesButton = new Button("Abbrechen");
									discardChangesButton.addClickHandler(new ClickHandler() {

										@Override
										public void onClick(ClickEvent event) {
										
											updatingValue = updatedValue;
											updateBtn = new Button("Eigenschaft bearbeiten");
											updateBtn.addClickHandler(new updateBtnClickHandler());
											deleteBtn = new Button("Eigenschaft entfernen");
											deleteBtn.addClickHandler(new deleteBtnClickHandler());
											propertyTable.setWidget(eventRow, 0, new HTML("<p><strong>"
													+ propertyChangeListBox.getSelectedItemText() + ":</strong></p>"));
											propertyTable.setWidget(eventRow, 1,
													new HTML("<p>" + updatedValue.getName() + "</p>"));
											propertyTable.setWidget(eventRow, 2, updateBtn);
											propertyTable.setWidget(eventRow, 3, deleteBtn);
										}

									});

									Button saveChangesButton = new Button("Änderungen speichern");
									saveChangesButton.addClickHandler(new ClickHandler() {

										@Override
										public void onClick(ClickEvent event) {
																				
											if (valueChangeTextBox.getText().matches("")) {
												Window.alert("Bitte eine Eigenschaft eintragen!");
												return;
											}
										
											int propertyId = 0;
											final int oldPropertyId = updatedValue.getPropertyID();
											for (Property p : propertyArray) {
												if ((propertyChangeListBox.getSelectedItemText()).equals(p.getName())) {
													propertyId = p.getBoId();
												}
											}

											
												try {
													if (propertyChangeListBox.getSelectedItemText().equals("Geburtsdatum")) {
														birthdayFlag = true;
														DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
														java.util.Date date = dateTimeFormat.parse(valueChangeTextBox.getText());
														
																										
													} else if (oldPropertyId == 1) {
														birthdayFlag = false;
													}
												} catch (Exception e) {
													Window.alert("Geburtsdatum bitte im Format \"01.01.99\" eingeben");
													return;
												}
											
											updatedValue.setName(valueChangeTextBox.getText());
										
											updatedValue.setPropertyID(propertyId);
											ClientSideSettings.getConnectedAdmin().updateValue(updatedValue,
													oldPropertyId, new AsyncCallback<Value>() {

														@Override
														public void onFailure(Throwable caught) {
															Window.alert(
																	"Eigenschaft konnte nicht aktualisiert werden");
														}

														@Override
														public void onSuccess(final Value result) {
															ClientSideSettings.getConnectedAdmin()
																	.checkIfPropertyHasValue(oldPropertyId,
																			new AsyncCallback<Void>() {

																				@Override
																				public void onFailure(
																						Throwable caught) {
																					Window.alert(
																							"Wurde leider nicht gefunden");
																				}

																				@Override
																				public void onSuccess(Void result) {
																					ClientSideSettings
																							.getConnectedAdmin()
																							.findAllProperties(
																									new findAllPropertiesCallback());

																				}

																			});

															updatingValue = result;

															updateBtn = new Button("Eigenschaft bearbeiten");
															updateBtn.addClickHandler(new updateBtnClickHandler());
															deleteBtn = new Button("Eigenschaft entfernen");
															deleteBtn.addClickHandler(new deleteBtnClickHandler());
															propertyTable.setWidget(eventRow, 0,
																	new HTML("<p><strong>"
																			+ propertyChangeListBox
																					.getSelectedItemText()
																			+ ":</strong></p>"));
															propertyTable.setWidget(eventRow, 1, new HTML(
																	"<p>" + valueChangeTextBox.getText() + "</p>"));
															propertyTable.setWidget(eventRow, 2, updateBtn);
															propertyTable.setWidget(eventRow, 3, deleteBtn);

														}

													});
										}

									});
									// Anzeige des Bearbeitungsmodus
									propertyTable.removeCell(eventRow, 3);
									propertyTable.setWidget(eventRow, 0, propertyChangeListBox);
									propertyTable.setWidget(eventRow, 1, valueChangeTextBox);
									propertyTable.setWidget(eventRow, 2, saveChangesButton);
									propertyTable.setWidget(eventRow, 3, discardChangesButton);
								}

							});

				}

			});
			/**
			 * delete Button Clickhandler
			 * 
			 */
			deleteBtn = new Button("Eigenschaft entfernen");
			deleteBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					eventRow = propertyTable.getCellForEvent(event).getRowIndex();

					ClientSideSettings.getConnectedAdmin().deleteValue(result, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Value konnte nicht gelöscht werden");

						}

						@Override
						public void onSuccess(Void result) {
							Window.alert("Eigenschaft wurde gelöscht!");
							propertyTable.removeRow(eventRow);
							if (propertyLabel.getText().equals("Geburtsdatum")) {
								birthdayFlag = false;
							}
							ClientSideSettings.getConnectedAdmin().findAllProperties(new findAllPropertiesCallback());
						}

					});
				}

			});
			ClientSideSettings.getConnectedAdmin().findAllProperties(new findAllPropertiesCallback());
			int rowCount = propertyTable.getRowCount();
			propertyTable.setWidget(rowCount, 0, new HTML("<p><strong>" + propertyLabel.getText() + ":</strong></p>"));
			propertyTable.setWidget(rowCount, 1, valueLabel);
			propertyTable.setWidget(rowCount, 2, updateBtn);
			propertyTable.setWidget(rowCount, 3, deleteBtn);

			newPropertyTable.setWidget(0, 0, new HTML("<h3>Neue Eigenschaften hinzufügen</h3>"));
			newPropertyTable.setWidget(1, 1, valueTextBox);
			newPropertyTable.setWidget(1, 2, newPropertyBtn);

		}

	}

	/**Change Handler der
	// Eigenschafts-Listboxen------------------------------
**/
	private class listBoxChangeHandler implements ChangeHandler {

		@Override
		public void onChange(ChangeEvent event) {

			if (propertyListBox.getSelectedItemText().equals("oder neue Eigenschaft hinzufügen...")) {

				int rowCount = newPropertyTable.getRowCount();
				newPropertyTable.removeRow(rowCount - 1);

				newPropertyTextBox = new TextBox();
				newPropertyTextBox.setWidth("200px");
				
				Button cancelButton = new Button ("Abbrechen");
				cancelButton.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						newPropertyTable.setWidget(1, 0, propertyListBox);
						newPropertyTable.setWidget(1, 1, valueTextBox);
						newPropertyTable.setWidget(1, 2, newPropertyBtn);
						newPropertyTable.removeCell(1, 3)
						;}
				});
				
				Button propertySaveButton = new Button("Speichern");
				propertySaveButton.addClickHandler(new savePropertyClickHandler());
				newPropertyTable.setWidget(1, 0, new HTML("Eigenschaftsname:"));
				newPropertyTable.setWidget(1, 1, newPropertyTextBox);
				newPropertyTable.setWidget(1, 2, propertySaveButton);
				newPropertyTable.setWidget(1, 3, cancelButton);
			}
			if (propertyListBox.getSelectedItemText().equals("Geburtsdatum")) {
				birthday = new DateBox();
				birthday.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd.MM.yyyy")));
				newPropertyTable.setWidget(1, 1, birthday);
				
			}
		}

	}

	/**Click Handler für den neue Eigenschaft speichern
	// Button*/

	private class savePropertyClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			if (newPropertyTextBox.getText().matches("")) {
				Window.alert("Bitte einen Eigenschaftsnamen eintragen!");
				return;
			}
			
			ClientSideSettings.getConnectedAdmin().findPropertyByName(newPropertyTextBox.getText(), new AsyncCallback<Property>(){

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Da lief etwas schief");
				}

				@Override
				public void onSuccess(Property result) {
					if (result == null){
						ClientSideSettings.getConnectedAdmin().createProperty(newPropertyTextBox.getText(),
								new AsyncCallback<Property>() {

									@Override
									public void onFailure(Throwable caught) {
										Window.alert("Eigenschaft nicht gespeichert");

									}

									@Override

									public void onSuccess(Property result) {

										propertyArray.add(result);

										propertyListBox.setItemText(propertyListBox.getItemCount() - 1, result.getName());

										propertyListBox.addItem("oder neue Eigenschaft hinzufügen...");
										propertyListBox.addChangeHandler(new listBoxChangeHandler());

										int rowIndex = newPropertyTable.getRowCount();
										newPropertyTable.removeRow(rowIndex - 1);

										newPropertyTable.setWidget(0, 0, new HTML("<h3>Neue Eigenschaften hinzufügen</h3>"));
										newPropertyTable.setWidget(1, 0, propertyListBox);
										newPropertyTable.setWidget(1, 1, valueTextBox);
										newPropertyTable.setWidget(1, 2, newPropertyBtn);
									}

								});
					
						
					}else{
						Window.alert("Diese Eigenschaft ist bereits vorhanden, bitte aus der Liste auswählen");
					}
					
				}
				
			});

		

		}
	}
	
	/**
	 * update BUtton ClickHandler, der ClickHandler wenn eine vorhandene Eigenschaft geändert werden soll.
	 * 
	 * @author Philipp
	 *
	 */
	private class updateBtnClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			eventRow = propertyTable.getCellForEvent(event).getRowIndex();
			final TextBox valueChangeTextBox = new TextBox();
			valueChangeTextBox.setWidth("200px");
			if(updatingValue.getName().equals("Geburtsdatum")){
				
				
			}else{
			valueChangeTextBox.setText(updatingValue.getName());}

			ClientSideSettings.getConnectedAdmin().findPropertyByPropertyId(updatingValue.getPropertyID(),
					new AsyncCallback<Property>() {
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Wurde leider nicht gefunden");

						}

						@Override
						public void onSuccess(Property result) {
							final Property changingProperty = result;
							final ListBox propertyChangeListBox = new ListBox();
							propertyChangeListBox.addChangeHandler(new listBoxChangeHandler());

							for (int i = 0; i < propertyArray.size(); i++) {
								propertyChangeListBox.addItem(propertyArray.get(i).getName());
								if ((propertyArray.get(i).getName()).equals(changingProperty.getName())) {
									propertyChangeListBox.setSelectedIndex(i);
								}
							}

							Button discardChangesButton = new Button("Abbrechen");
							discardChangesButton.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
								
									updateBtn = new Button("Eigenschaft bearbeiten");
									updateBtn.addClickHandler(new updateBtnClickHandler());
									deleteBtn = new Button("Eigenschaft entfernen");
									deleteBtn.addClickHandler(new deleteBtnClickHandler());
									propertyTable.setWidget(eventRow, 0, new HTML("<p><strong>"
											+ propertyChangeListBox.getSelectedItemText() + ":</strong></p>"));
									propertyTable.setWidget(eventRow, 1,
											new HTML("<p>" + updatingValue.getName() + "</p>"));
									propertyTable.setWidget(eventRow, 2, updateBtn);
									propertyTable.setWidget(eventRow, 3, deleteBtn);
								}

							});

							Button saveChangesButton = new Button("Änderungen speichern");
							saveChangesButton.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									if (valueChangeTextBox.getText().matches("")) {
										Window.alert("Bitte eine Eigenschaft eintragen!");
										return;
									}
									int propertyId = 0;
									final int oldPropertyId = updatingValue.getPropertyID();
									for (Property p : propertyArray) {
										if ((propertyChangeListBox.getSelectedItemText()).equals(p.getName())) {
											propertyId = p.getBoId();
										}
									}

									try {
										if (propertyChangeListBox.getSelectedItemText().equals("Geburtsdatum")) {
											birthdayFlag = true;
											DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
											java.util.Date date = dateTimeFormat.parse(valueChangeTextBox.getText());
											
										} else if (oldPropertyId == 1) {
											birthdayFlag = false;
										}
									} catch (Exception e) {
										Window.alert("Geburtsdatum bitte im Format \"01.01.99\" eingeben");
										return;
									}
									
									updatingValue.setName(valueChangeTextBox.getText());
									updatingValue.setPropertyID(propertyId);
									ClientSideSettings.getConnectedAdmin().updateValue(updatingValue, oldPropertyId,
											new AsyncCallback<Value>() {

												@Override
												public void onFailure(Throwable caught) {
													Window.alert("Eigenschaft konnte nicht aktualisiert werden");
												}

												@Override
												public void onSuccess(final Value result) {
													ClientSideSettings.getConnectedAdmin().checkIfPropertyHasValue(
															oldPropertyId, new AsyncCallback<Void>() {

																@Override
																public void onFailure(Throwable caught) {
																	Window.alert("Wurde leider nicht gefunden");

																}

																@Override
																public void onSuccess(Void result) {
																	ClientSideSettings.getConnectedAdmin()
																			.findAllProperties(
																					new findAllPropertiesCallback());

																}

															});

													updatingValue = result;

													updateBtn = new Button("Eigenschaft bearbeiten");
													updateBtn.addClickHandler(new updateBtnClickHandler());
													deleteBtn = new Button("Eigenschaft entfernen");
													deleteBtn.addClickHandler(new deleteBtnClickHandler());
													propertyTable.setWidget(eventRow, 0,
															new HTML("<p><strong>"
																	+ propertyChangeListBox.getSelectedItemText()
																	+ ":</strong></p>"));
													propertyTable.setWidget(eventRow, 1,
															new HTML("<p>" + valueChangeTextBox.getText() + "</p>"));
													propertyTable.setWidget(eventRow, 2, updateBtn);
													propertyTable.setWidget(eventRow, 3, deleteBtn);

												}

											});
								}

							});
							propertyTable.removeCell(eventRow, 3);
							propertyTable.setWidget(eventRow, 0, propertyChangeListBox);
							propertyTable.setWidget(eventRow, 1, valueChangeTextBox);
							propertyTable.setWidget(eventRow, 2, saveChangesButton);
							propertyTable.setWidget(eventRow, 3, discardChangesButton);
						}

					});

		}

	}

	/**
	 * deleteButton Clickhandler
	 * 
	 */
	private class deleteBtnClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			eventRow = propertyTable.getCellForEvent(event).getRowIndex();

			ClientSideSettings.getConnectedAdmin().deleteValue(updatingValue, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Kein Value gefunden");

				}

				@Override
				public void onSuccess(Void result) {
					// Wenn Geburtsdatum, dann Flag wieder auf false setzen
					if (updatingValue.getPropertyID() == 1) {
						birthdayFlag = false;
					}

					propertyTable.removeRow(eventRow);
					ClientSideSettings.getConnectedAdmin().findAllProperties(new findAllPropertiesCallback());
					Window.alert("Eigenschaft wurde gelöscht!");
				}
			});
		}

	}
	/**
	 * wenne der Namen geändert wird , wird dieser ClickHandler aufgerufen, dieser aktualisiert das Objekt.
	 * 
	 */
	private class changeNameClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			if (firstNameBox.getText().matches("")) {
				Window.alert("Bitte einen Name eintragen!");
				return;
			}
			if (surnameBox.getText().matches("")) {
				Window.alert("Bitte einen Namen eintragen!");
				return;
			}

			Button saveChangeNameButton = new Button("Änderungen speichern");

			saveChangeNameButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (firstNameBox.getText().matches("")) {
						Window.alert("Bitte einen Namen eintragen!");
						return;
					}
					if (surnameBox.getText().matches("")) {
						Window.alert("Bitte einen Namen eintragen!");
						return;
					}
					selectedContact.setPrename(firstNameBox.getText());
					selectedContact.setSurname(surnameBox.getText());

					ClientSideSettings.getConnectedAdmin().updateContact(selectedContact, new AsyncCallback<Contact>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Kontakt wurde leider nicht gefunden");

						}

						@Override
						public void onSuccess(Contact result) {
							Button changeNameButton = new Button("Namen ändern");
							nameTable.setWidget(0, 0, new HTML("<strong>Vorname: <strong>"));
							nameTable.setWidget(0, 1, new HTML(result.getPrename()));
							nameTable.setWidget(1, 0, new HTML("<strong>Nachname: <strong>"));
							nameTable.setWidget(1, 1, new HTML(result.getSurname()));
							nameTable.getFlexCellFormatter().setRowSpan(1, 2, 2);
							nameTable.getFlexCellFormatter().setAlignment(1, 2, HasHorizontalAlignment.ALIGN_CENTER,
									HasVerticalAlignment.ALIGN_MIDDLE);
							nameTable.setWidget(1, 2, changeNameButton);
							changeNameButton.addClickHandler(new changeNameClickHandler());
							RootPanel.get("content").clear();
							ContactsTable table = new ContactsTable(contactsInCL,selectedCL);
						}

					});
				}
			});
			nameTable.setWidget(0, 1, firstNameBox);
			nameTable.setWidget(1, 1, surnameBox);
			nameTable.setWidget(1, 2, saveChangeNameButton);

		}
	}

}