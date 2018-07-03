package de.hdm.Connected.client.gui;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.Vector;

import com.google.gwt.dev.javac.Shared;
//import com.google.appengine.labs.repackaged.com.google.common.collect.Multiset.Entry;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.thirdparty.javascript.jscomp.Result;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowScrollListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

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

	Label firstNameLabel = new Label("Vorname:");
	TextBox firstNameBox = new TextBox();
	Label surnameLabel = new Label("Nachname:");
	TextBox surnameBox = new TextBox();
	TextBox newPropertyTextBox = null;
	ListBox propertyListBox = null;

	TextBox valueTextBox = null;
	Button addButton = new Button("weitere Eigenschaften hinzufügen");
	Button newPropertyBtn = null;
	Button updateBtn = null;
	Button deleteBtn = null;
	Button closeButton = new Button("Schließen");
	HorizontalPanel itemPanel = new HorizontalPanel();

	VerticalPanel propertyPanel = new VerticalPanel();
	VerticalPanel valuePanel = new VerticalPanel();
	VerticalPanel root = new VerticalPanel();

	FlexTable nameTable = new FlexTable();
	FlexTable propertyTable = new FlexTable();
	FlexTable newPropertyTable = new FlexTable();

	private ArrayList<Property> propertyArray = new ArrayList<Property>();
	ArrayList<Integer> selectedProperties = new ArrayList<Integer>();
	ArrayList<String> propertyValueArray = new ArrayList<String>();
	ArrayList<TextBox> values = new ArrayList<TextBox>();
	Map<Integer, String> valuesMap = new HashMap<Integer, String>();
	Map<Widget, Widget> widgetMap = new HashMap<Widget, Widget>();

	FlexTable checkboxTable = new FlexTable();
	CheckBox checkContactlist = new CheckBox();
	final ListBox contactlist = new ListBox(true);
	int eventRow = 0;
	String propertyName = "";
	ArrayList<Value> valuesArray = new ArrayList<Value>();
	Value updatingValue = null;

	ArrayList<ContactList> contactListArray = null;

	/**
	 * Konstruktor wenn ein Kontakt schon existiert.
	 * 
	 * @param contact
	 */

	public ContactForm(Contact contact, final ContactList contactList, final ArrayList<Contact> contacts) {
		this.selectedContact = contact;

		// RootPanel.get("content").clear();

		// Enable animation.
		this.setAnimationEnabled(true);
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Popup schließen bei Betägigung des Buttons
				hide();
				RootPanel.get("content").clear();
				ContactsTable table = new ContactsTable(contacts, contactList);
			}
		});

		// this.setStylePrimaryName("content");
		// this.ensureDebugId("cwBasicPopup-simplePopup");

		root.add(new HTML("<h3> Kontakt bearbeiten</h3>"));
		root.add(nameTable);

		// Wenn Kontakt gefunden, dann die Values dazu finden.

		Button changeNameButton = new Button("Namen ändern");

		firstNameBox.setText(selectedContact.getPrename());
		surnameBox.setText(selectedContact.getSurname());

		changeNameButton.addClickHandler(new changeNameClickHandler());
		nameTable.getColumnFormatter().setWidth(0, "100px");
		propertyTable.getColumnFormatter().setWidth(0, "150px");
		newPropertyTable.getColumnFormatter().setWidth(0, "150px");
		

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
		/*
		 * TODO style erstellen in CSS topPanel.addStyleName("headline");
		 */

		topPanel.add(new HTML("<h2> Neuen Kontakt erstellen</h2>"));

		root.add(topPanel);
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
	
		addButton.addClickHandler(new addNewPropertyClickHandler());

		itemPanel.add(propertyPanel);
		itemPanel.add(valuePanel);

		// Widgets dem rootPanel hinzufügen
		root.add(itemPanel);
		root.add(nameTable);
		root.add(propertyTable);
		root.add(newPropertyTable);

		HorizontalPanel bottomPanel = new HorizontalPanel();

		/**
		 * java.util.Date berücksichtigt auch Stunden, Minuten, Sekunden und
		 * sogar Millisekunden, java.sql.Date hingegen entspricht dem SQL DATE.
		 * Deshalb muss das Datum vorher konvertiert werden.
		 */

		Button saveButton = new Button("Kontakt abspeichern");
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			// ---------- saveButton ClickHandler------------

			public void onClick(ClickEvent event) {
				// Nur wenn die CheckBox geklickt ist, wird dies ausgeführt, da
				// sonst der Kontakt keiner Liste hinzugefügt werden soll
				final ArrayList<ContactList> contactListToAdd = new ArrayList<ContactList>();
				if (addButton != null) {
					//TODO currentUSer
					java.sql.Date creationTime = new java.sql.Date(System.currentTimeMillis());
					ClientSideSettings.getConnectedAdmin().createContact(firstNameBox.getText(), surnameBox.getText(),
							creationTime, creationTime, 2,
							new AsyncCallback<Contact>() {
								ArrayList<Contact> contacts = new ArrayList<Contact>();

								@Override
								public void onFailure(Throwable caught) {
									
									Window.alert("Konnte Eigenschaft nicht anlegen");
								}

								@Override
								public void onSuccess(Contact result) {
									contacts.add(result);
									ArrayList<Integer> contactId = new ArrayList<Integer>();
									ArrayList<Integer> userId = new ArrayList<Integer>();
									contactId.add(result.getBoId());
									userId.add(2);
									Window.alert("Kontakt wurde angelegt!");

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
														Window.alert(Integer.toString(contactListToAdd.size()));

													}

												});
									}
									hide();
									Window.Location.reload();
								}

							});
				}
				/*
				 * if (checkContactlist.getValue()) { for (int i = 0; i <
				 * contactlist.getItemCount(); i++) { if
				 * (contactlist.isItemSelected(i)) {
				 * Window.alert(Integer.toString(contactListArray.get(i).getBoId
				 * ())); ClientSideSettings.getConnectedAdmin().
				 * addContactToContactList(createdContact.getBoId(),
				 * contactListArray.get(i).getBoId(), new AsyncCallback<Void>()
				 * {
				 * 
				 * @Override public void onFailure(Throwable caught) { Window.
				 * alert("Kontakt konnte Kontaktliste nicht hinzugefügt werden"
				 * ); }
				 * 
				 * @Override public void onSuccess(Void result) { Window.
				 * alert("Kontakt wurde angelegt und den Kontaktlisten hinzugefügt!"
				 * ); Window.Location.reload();
				 * 
				 * }
				 * 
				 * }); }
				 * 
				 * }
				 * 
				 * }
				 */ else {
					Window.alert("Kontakt angelegt!");
					hide();
					Window.Location.reload();
				}

			}

		});

		bottomPanel.add(saveButton);

		Button cancelButton = new Button("Abbrechen");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();

			}
		});

		bottomPanel.add(cancelButton);

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
		//TODO current user
		ClientSideSettings.getConnectedAdmin().getContactListsByUserPermission(2, new AsyncCallback<ArrayList<ContactList>>() {

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

	private class findValueCallback implements AsyncCallback<ArrayList<Value>> {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSuccess(ArrayList<Value> result) {

			propertyTable = new FlexTable();
			newPropertyTable = new FlexTable();
			root.add(propertyTable);
			root.add(closeButton);
			setWidget(root);
			ArrayList<Value> valuesByContact = result;

			try {
				/*if(result.size() == 0){
					int rowCount = nameTable.getRowCount();
				nameTable.setWidget(rowCount, 0 , addButton);
				addButton.addClickHandler(new addNewPropertyClickHandler());
				}*/
				
				for (final Value v : valuesByContact) {
					propertyListBox = new ListBox();
					final Value updatingOldValue = v;
					ClientSideSettings.getConnectedAdmin().findPropertyByPropertyId(updatingOldValue.getPropertyID(),
							new AsyncCallback<Property>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onSuccess(Property result) {
									final Property p = result;
									int rowCount = propertyTable.getRowCount();
									if (result.getName().equals("Geburtsdatum")) {
										for (int i = 0; i < propertyListBox.getItemCount(); i++) {
											if (propertyListBox.getItemText(i).equals("Geburtsdatum")) {
												propertyListBox.removeItem(i);
											}
										}
									}
									updateBtn = new Button("Eigenschaft bearbeiten");
									updateBtn.addClickHandler(new ClickHandler() {

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
																propertyChangeListBox
																		.addItem(propertyArray.get(i).getName());
																if ((propertyArray.get(i).getName())
																		.equals(changingProperty.getName())) {
																	propertyChangeListBox.setSelectedIndex(i);
																}
															}
															
															Button discardChangesButton = new Button("Abbrechen");
															discardChangesButton.addClickHandler(new ClickHandler(){

																@Override
																public void onClick(ClickEvent event) {
																	updatingValue = updatingOldValue;
																	
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
															

															
															Button saveChangesButton = new Button(
																	"Änderungen speichern");
															saveChangesButton.addClickHandler(new ClickHandler() {

																@Override
																public void onClick(ClickEvent event) {
																	int propertyId = 0;

																	for (Property p : propertyArray) {
																		if ((propertyChangeListBox
																				.getSelectedItemText())
																						.equals(p.getName())) {
																			propertyId = p.getBoId();
																		}
																	}

																	updatingOldValue
																			.setName(valueChangeTextBox.getText());
																	updatingOldValue.setPropertyID(propertyId);
																	ClientSideSettings.getConnectedAdmin().updateValue(
																			updatingOldValue,
																			new AsyncCallback<Value>() {

																				@Override
																				public void onFailure(
																						Throwable caught) {
																					Window.alert("Eigenschaft konnte nicht aktualisiert werden");
																				}

																				@Override
																				public void onSuccess(
																						final Value result) {
																					updatingValue = result;
																					
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
															propertyTable.removeCell(eventRow, 3);
															propertyTable.setWidget(eventRow, 0, propertyChangeListBox);
															propertyTable.setWidget(eventRow, 1, valueChangeTextBox);
															propertyTable.setWidget(eventRow, 2, saveChangesButton);
															propertyTable.setWidget(eventRow, 3, discardChangesButton);
														}

													});
										}

									});

									deleteBtn = new Button("Eigenschaft entfernen");
									deleteBtn.addClickHandler(new ClickHandler() {

										@Override
										public void onClick(ClickEvent event) {
											eventRow = propertyTable.getCellForEvent(event).getRowIndex();

											ClientSideSettings.getConnectedAdmin().deleteValue(updatingOldValue,
													new AsyncCallback<Void>() {

														@Override
														public void onFailure(Throwable caught) {
															// TODO
															// Auto-generated
															// method stub

														}

														@Override
														public void onSuccess(Void result) {
															Window.alert("Eigenschaft wurde gelöscht!");
															propertyTable.removeRow(eventRow);
															if (p.getName().equals("Geburtsdatum")) {
																propertyListBox.clear();
																for (Property p : propertyArray) {
																	propertyListBox.addItem(p.getName());
																}
																propertyListBox
																		.addItem("oder neue Eigenschaft hinzufügen...");

															}
														}

													});
										}

									});

									propertyTable.setWidget(rowCount, 0,
											new HTML("<p><strong>" + p.getName() + ":</strong></p>"));
									propertyTable.setWidget(rowCount, 1, new HTML(updatingOldValue.getName()));
									propertyTable.setWidget(rowCount, 2, updateBtn);
									propertyTable.setWidget(rowCount, 3, deleteBtn);

								}

							});
				}

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

			} catch (Exception e) {
				Window.alert(e.toString());
				e.printStackTrace();
			}

		}
	}

	private class findAllPropertiesCallback implements AsyncCallback<ArrayList<Property>> {

		@Override
		public void onFailure(Throwable caught) {

			ClientSideSettings.getLogger().severe("Konnte nicht laden");
			Window.alert("Da ist wohl etwas schief gelaufen");

		}

		public void onSuccess(ArrayList<Property> result) {
			// alle Eigenschaften in Vektor laden
			if (selectedContact != null) {
				propertyArray = new ArrayList<Property>();
				for (int i = 0; i < result.size(); i++) {
					Property propertyItem = result.get(i);

					if (propertyItem.getName() != "Vorname" || propertyItem.getName() != "Nachname") {

						propertyArray.add(propertyItem);

					}

				}
				ClientSideSettings.getConnectedAdmin().findValuesByContactId(selectedContact.getBoId(),
						new findValueCallback());
			} else {

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
				int rowCount = newPropertyTable.getRowCount();

				newPropertyTable.setWidget(rowCount - 1, 0, propertyListBox);

			}
		}
	}

	// ----Clickhandler für add Button-----
	private class addNewPropertyClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			if (selectedContact != null) {

				int propertyId = 0;
				for (Property p : propertyArray) {
					if ((propertyListBox.getSelectedItemText()).equals(p.getName())) {
						propertyId = p.getBoId();
					}
				}

				ClientSideSettings.getConnectedAdmin().createValue(valueTextBox.getText(), propertyId,
						selectedContact.getBoId(), 2, new AsyncCallback<Value>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(Value result) {
								// TODO hier Dialogbox einfügen um zu fragen ob
								// neue Eigenschaft direkt geteilt werden soll^
								int rowCount = propertyTable.getRowCount();
								updatingValue = result;
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
									propertyListBox.removeItem(propertyListBox.getSelectedIndex());
								}
								// shareNewValue.show();

							}

						});
			} else {

				if (newPropertyBtn != null) {
					if(propertyTable.getRowCount() != 0){
					eventRow = newPropertyTable.getCellForEvent(event).getRowIndex();
					}else{
						eventRow = 0; 
					}
				}

				if (addButton != null) {
					// TODO currentUser
					try {
						java.sql.Date creationTime = new java.sql.Date(System.currentTimeMillis());
						ClientSideSettings.getConnectedAdmin().createContact(firstNameBox.getText(),
								surnameBox.getText(), creationTime, creationTime, 2, new AsyncCallback<Contact>() {

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

										newPropertyTable.setWidget(rowCount, 0,
												new HTML("<h3>Neue Eigenschaften hinzufügen</h3>"));

										newPropertyTable.setWidget(rowCount + 1, 1, valueTextBox);
										newPropertyTable.setWidget(rowCount + 1, 2, newPropertyBtn);
									}
								});
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					int propertyId = 0;
					newPropertyBtn.removeFromParent();
					newPropertyBtn = null;
					propertyName = propertyListBox.getSelectedItemText();
					for (Property p : propertyArray) {
						if ((propertyListBox.getSelectedItemText()).equals(p.getName())) {
							propertyId = p.getBoId();
							if (p.getName().equals("Geburtsdatum")) {
								propertyListBox.removeItem(propertyListBox.getSelectedIndex());
							}
						}
					}

					ClientSideSettings.getConnectedAdmin().createValue(valueTextBox.getText(), propertyId,
							createdContact.getBoId(), 2, new createValueCallback());
				}
			}
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
		public void onSuccess(final Value result) {
			// Am Ende wird der Kontakt den ausgewählten Kontaktlisten
			// hinzugefügt.
			Window.alert("Eigenschaft erfolgreich hinzugefügt");
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
					// TODO Auto-generated method stub
					final Value updatedValue = result;
					eventRow = propertyTable.getCellForEvent(event).getRowIndex();
					final TextBox valueChangeTextBox = new TextBox();
					valueChangeTextBox.setWidth("200px");
					valueChangeTextBox.setText(updatedValue.getName());

					ClientSideSettings.getConnectedAdmin().findPropertyByPropertyId(updatedValue.getPropertyID(),
							new AsyncCallback<Property>() {
								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub

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
									
									Button discardChangesButton= new Button("Abbrechen");
									discardChangesButton.addClickHandler(new ClickHandler(){

										@Override
										public void onClick(ClickEvent event) {
											updatingValue = updatedValue;
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

									Button saveChangesButton = new Button("Änderungen speichern");
									saveChangesButton.addClickHandler(new ClickHandler() {

										@Override
										public void onClick(ClickEvent event) {
											int propertyId = 0;

											for (Property p : propertyArray) {
												if ((propertyChangeListBox.getSelectedItemText()).equals(p.getName())) {
													propertyId = p.getBoId();
												}
											}

											updatedValue.setName(valueChangeTextBox.getText());
											updatedValue.setPropertyID(propertyId);
											ClientSideSettings.getConnectedAdmin().updateValue(updatedValue,
													new AsyncCallback<Value>() {

														@Override
														public void onFailure(Throwable caught) {
															Window.alert("Eigenschaft konnte nicht aktualisiert werden");
														}

														@Override
														public void onSuccess(final Value result) {
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
									propertyTable.removeCell(eventRow, 3);
									propertyTable.setWidget(eventRow, 0, propertyChangeListBox);
									propertyTable.setWidget(eventRow, 1, valueChangeTextBox);
									propertyTable.setWidget(eventRow, 2, saveChangesButton);
									propertyTable.setWidget(eventRow, 3, discardChangesButton);
								}

							});

				}

			});

			deleteBtn = new Button("Eigenschaft entfernen");
			deleteBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					eventRow = propertyTable.getCellForEvent(event).getRowIndex();

					ClientSideSettings.getConnectedAdmin().deleteValue(result, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(Void result) {
							Window.alert("Eigenschaft wurde gelöscht!");
							propertyTable.removeRow(eventRow);
							if (propertyLabel.getText().equals("Geburtsdatum")) {
								propertyListBox.clear();
								for (Property p : propertyArray) {
									propertyListBox.addItem(p.getName());
								}
								propertyListBox.addItem("oder neue Eigenschaft hinzufügen...");
							}
						}

					});
				}

			});
			int rowCount = propertyTable.getRowCount();
			propertyTable.setWidget(rowCount, 0,
					new HTML("<p><strong>" + propertyLabel.getText() + ":</strong></p>"));
			propertyTable.setWidget(rowCount, 1, valueLabel);
			propertyTable.setWidget(rowCount, 2, updateBtn);
			propertyTable.setWidget(rowCount, 3, deleteBtn);

			newPropertyTable.setWidget(0, 0, new HTML("<h3>Neue Eigenschaften hinzufügen</h3>"));
			newPropertyTable.setWidget(1, 0, propertyListBox);
			newPropertyTable.setWidget(1, 1, valueTextBox);
			newPropertyTable.setWidget(1, 2, newPropertyBtn);

		}

	}

	// ---------------------Change Handler der
	// Eigenschafts-Listboxen------------------------------

	private class listBoxChangeHandler implements ChangeHandler {

		@Override
		public void onChange(ChangeEvent event) {
			// TODO Auto-generated method stub

			if (propertyListBox.getSelectedItemText().equals("oder neue Eigenschaft hinzufügen...")) {

				int rowCount = newPropertyTable.getRowCount();
				newPropertyTable.removeRow(rowCount - 1);

				newPropertyTextBox = new TextBox();
				newPropertyTextBox.setWidth("200px");
				Button propertySaveButton = new Button("Speichern");
				propertySaveButton.addClickHandler(new savePropertyClickHandler());
				newPropertyTable.setWidget(rowCount, 0, new HTML("Eigenschaftsname:"));
				newPropertyTable.setWidget(rowCount, 1, newPropertyTextBox);
				newPropertyTable.setWidget(rowCount, 2, propertySaveButton);

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

		}
	}

	private class updateBtnClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub

			eventRow = propertyTable.getCellForEvent(event).getRowIndex();
			final TextBox valueChangeTextBox = new TextBox();
			valueChangeTextBox.setWidth("200px");
			valueChangeTextBox.setText(updatingValue.getName());

			ClientSideSettings.getConnectedAdmin().findPropertyByPropertyId(updatingValue.getPropertyID(),
					new AsyncCallback<Property>() {
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

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
							
							Button discardChangesButton= new Button("Abbrechen");
							discardChangesButton.addClickHandler(new ClickHandler(){

								@Override
								public void onClick(ClickEvent event) {
									
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

							Button saveChangesButton = new Button("Änderungen speichern");
							saveChangesButton.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									int propertyId = 0;

									for (Property p : propertyArray) {
										if ((propertyChangeListBox.getSelectedItemText()).equals(p.getName())) {
											propertyId = p.getBoId();
										}
									}

									updatingValue.setName(valueChangeTextBox.getText());
									updatingValue.setPropertyID(propertyId);
									ClientSideSettings.getConnectedAdmin().updateValue(updatingValue,
											new AsyncCallback<Value>() {

												@Override
												public void onFailure(Throwable caught) {
													Window.alert("Eigenschaft konnte nicht aktualisiert werden");
												}

												@Override
												public void onSuccess(final Value result) {
													updatingValue = result;
													// TODO Auto-generated
													// method stub
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

	private class deleteBtnClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			eventRow = propertyTable.getCellForEvent(event).getRowIndex();

			ClientSideSettings.getConnectedAdmin().deleteValue(updatingValue, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess(Void result) {
					Window.alert("Eigenschaft wurde gelöscht!");
					propertyTable.removeRow(eventRow);

				}
			});
		}

	}

	private class changeNameClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			Button saveChangeNameButton = new Button("Änderungen speichern");

			saveChangeNameButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					selectedContact.setPrename(firstNameBox.getText());
					selectedContact.setSurname(surnameBox.getText());

					ClientSideSettings.getConnectedAdmin().updateContact(selectedContact, new AsyncCallback<Contact>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

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