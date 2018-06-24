package de.hdm.Connected.client.gui;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.Vector;

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
	Button newPropertyBtn = null;
	Button updateBtn = null;
	Button deleteBtn = null;
	HorizontalPanel itemPanel = new HorizontalPanel();

	VerticalPanel propertyPanel = new VerticalPanel();
	VerticalPanel valuePanel = new VerticalPanel();

	FlexTable nameTable = new FlexTable();
	FlexTable propertyTable = new FlexTable();

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
	/**
	 * Konstruktor wenn ein Kontakt schon existiert.
	 * 
	 * @param contact
	 */

	public ContactForm_Test(Contact contact) {

		this.selectedContact = contact;

		RootPanel.get("content").clear();

		RootPanel.get("content").add(new HTML("<h3> Kontakt bearbeiten</h3>"));

		ClientSideSettings.getConnectedAdmin().findContactById(selectedContact.getBoId(), new AsyncCallback<Contact>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Contact result) {
				// Wenn Kontakt gefunden, dann die Values dazu finden.
				ClientSideSettings.getConnectedAdmin().findAllProperties(new findAllPropertiesCallback());

			}

		});

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
				// Zu erst wird das Kontakt-Objekt angelegt.
				ClientSideSettings.getConnectedAdmin().createContact(firstNameBox.getText(), surnameBox.getText(),
						creationTime, creationTime, 1, new AsyncCallback<Contact>() {

							@Override
							public void onFailure(Throwable caught) {
								ClientSideSettings.getLogger().severe("Kontakt-Objekt konnte nicht erstellt werden");
								Window.alert("Da geht noch was ned");
							}

							@Override
							// War dies erfolgreich wird dem Kontakt die
							// einzelnen festgelegten Eigeschaften&Ausprägungen
							// zugeordnet bzw. gespeichert.
							public void onSuccess(Contact result) {
								createdContact = result;
								int rowCount = propertyTable.getRowCount();
								int propertyId = 0;
								String value = "";
								Window.alert(propertyArray.toString());
								ArrayList<Integer> propertyIDs = new ArrayList<Integer>();
								ArrayList<String> values = new ArrayList<String>();
								// try {
								// Iterator<Widget> propertyWidgets =
								// propertyTable.iterator();
								// Alle Reihen im FlexTable durchgehen um alles
								// ListBoxen und TextBoxen zu bekommen
								Window.alert(Integer.toString(propertyTable.getRowCount()));

								for (int i = 0; i < rowCount; i++) {

									try {
										if (propertyTable.getWidget(i, 0) instanceof ListBox) {
											ListBox propertyListBox = (ListBox) propertyTable.getWidget(i, 0);
											for (int j = 0; j < propertyArray.size(); j++) {
												// Wenn Name des Properties
												// übereinstimmt, dann Id des
												// Property abspeichern

												if (propertyListBox.getSelectedItemText()
														.equals(propertyArray.get(j).getName())) {
													propertyId = propertyArray.get(j).getBoId();
												}
												propertyIDs.add(propertyId);
											}
										}
										if (propertyTable.getWidget(i, 1) instanceof TextBox) {
											TextBox valueTextBox = (TextBox) propertyTable.getWidget(i, 1);
											value = valueTextBox.getText();
											values.add(value);
										}
									} catch (Exception e) {
										Window.alert(e.toString());
										e.printStackTrace();
									}
									// valuesMap.put(propertyId, value);
								}

								for (int k = 0; k < propertyIDs.size(); k++) {

									ClientSideSettings.getConnectedAdmin().createValue(values.get(k),
											propertyIDs.get(k), result.getBoId(), 1, new createValueCallback());
								}

								for (int l = 0; l < propertyIDs.size(); l++) {
									Window.alert(propertyIDs.get(l) + " = " + values.get(l));
								}

								// Durch Map iterieren und jeweils den Value
								// abspeichern

								/*
								 * for(Map.Entry<Integer, String> entry :
								 * valuesMap.entrySet()){
								 * ClientSideSettings.getConnectedAdmin().
								 * createValue(entry.getValue(), entry.getKey(),
								 * result.getBoId(), new createValueCallback());
								 * } /* /* while (propertyWidgets.hasNext()) {
								 * Widget ch = propertyWidgets.next(); if (ch
								 * instanceof ListBox) { ListBox propertyListBox
								 * = (ListBox) ch; for (int i = 0; i <
								 * propertyArray.size(); i++) { if
								 * (propertyListBox.getSelectedItemText() ==
								 * propertyArray.get(i) .getName()) {
								 * selectedProperties.add(propertyArray.get(i).
								 * getBoId()); } }
								 * 
								 * } else if (ch instanceof TextBox) { TextBox
								 * valueTextBox = (TextBox) ch;
								 * values.add(valueTextBox); }
								 * Window.alert(Integer.toString(propertyArray.
								 * size()));
								 * 
								 * }
								 * 
								 * for (int i = 0; i <
								 * selectedProperties.size(); i++) {
								 * 
								 * ClientSideSettings.getConnectedAdmin().
								 * createValue(values.get(i).getText(),
								 * selectedProperties.get(i), result.getBoId(),
								 * new createValueCallback()); } } catch
								 * (Exception e) { Window.alert(e.toString());
								 * e.printStackTrace();
								 */
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
		// TODO nur KOntaktlisten des aktuellen Users abrufen!
		ClientSideSettings.getConnectedAdmin().findAllContactlists(new AsyncCallback<ArrayList<ContactList>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Die Kontaktlisten konnten nicht geladen werden");
			}

			@Override
			// jede Kontaktliste wird der ListBox hinzugefügt
			public void onSuccess(ArrayList<ContactList> result) {
				for (ContactList cl : result) {
					contactlist.addItem(cl.getName());
				}

			}

		});
		// ListBox deaktivieren, da CheckBox nicht aktiviert.
		contactlist.setEnabled(false);

		// Dies dem FlexTable hinzufügen
		RootPanel.get("content").add(new HTML("<h3> Kontakt einer Kontaktliste hinzufügen? </h3>"));
		checkboxTable.setWidget(0, 0, checkContactlist);
		// Disabled Style
		checkboxTable.setWidget(0, 1,
				new HTML("<h3 style=\"color:#D3D3D3;\"> Bitte eine oder mehrere Kontaktlisten auswählen: </h3>"));
		checkboxTable.setWidget(0, 2, contactlist);

		RootPanel.get("content").add(checkboxTable);
		RootPanel.get("content").add(bottomPanel);

	}

	private class findValueAndPropertyCallback implements AsyncCallback<Map<String, String>> {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSuccess(Map<String, String> result) {

			propertyTable = new FlexTable();

			try {
				for (Map.Entry<String, String> mapi : result.entrySet()) {
					propertyListBox = new ListBox();
					propertyListBox.setWidth("250px");

					for (int j = 0; j < propertyArray.size(); j++) {
						propertyListBox.addItem(propertyArray.get(j).getName());
						if (propertyArray.get(j).getName().equals(mapi.getKey())) {
							propertyListBox.setSelectedIndex(j);
						}

					}

					TextBox valueBox = new TextBox();
					valueBox.setText(mapi.getValue());

					int rowCount = propertyTable.getRowCount();
					propertyTable.setWidget(rowCount, 0, propertyListBox);
					propertyTable.setWidget(rowCount, 1, valueBox);

				}

				int rowCount = propertyTable.getRowCount();
				newPropertyBtn = new Button("+");
				newPropertyBtn.addClickHandler(new addNewPropertyClickHandler());

				propertyTable.setWidget(rowCount - 1, 2, newPropertyBtn);
				RootPanel.get("content").add(propertyTable);

				HorizontalPanel bottomPanel = new HorizontalPanel();
				Button saveButton = new Button("Änderungen speichern");
				RootPanel.get("content").add(saveButton);
				// bottomPanel.add(saveButton);

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
				/*ClientSideSettings.getConnectedAdmin().findValueAndProperty(selectedContact.getBoId(),
						new findValueAndPropertyCallback());*/
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
				int rowCount = propertyTable.getRowCount();

				// if(selectedContact == null){
				propertyTable.setWidget(rowCount - 1, 0, propertyListBox);

			}
		}
	}

	// ----Clickhandler für add Button-----
	private class addNewPropertyClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			if (newPropertyBtn != null) {

				eventRow = propertyTable.getCellForEvent(event).getRowIndex();
			}
			// eventRow= propertyTable.getCellForEvent(event).getRowIndex();

			/*
			 * if(selectedContact!= null){ newPropertyBtn.removeFromParent();
			 * newPropertyBtn = null; //da hier die das PropertyArray schon
			 * gefüllt worden ist, kann es direkt in die ListBox geladen werden.
			 * propertyListBox = new ListBox();
			 * 
			 * for(int i=0; i<propertyArray.size();i++){
			 * propertyListBox.addItem(propertyArray.get(i).getName()); }
			 * propertyListBox.addItem("oder neue Eigenschaft hinzufügen...");
			 * propertyListBox.setWidth("250px");
			 * propertyListBox.addChangeHandler(new listBoxChangeHandler());
			 * 
			 * newPropertyBtn = new Button("+"); // propertyListBox = new
			 * ListBox(); valueTextBox = new TextBox();
			 * 
			 * int rowCount = propertyTable.getRowCount();
			 * propertyTable.setWidget(rowCount, 0, propertyListBox);
			 * propertyTable.setWidget(rowCount, 1, valueTextBox);
			 * propertyTable.setWidget(rowCount, 2, newPropertyBtn);
			 * 
			 * newPropertyBtn.addClickHandler(new addNewPropertyClickHandler());
			 * 
			 * }else{
			 */
			if(addButton !=null){
				
						
				java.sql.Timestamp creationTime = new Timestamp(System.currentTimeMillis());
				ClientSideSettings.getConnectedAdmin().createContact(firstNameBox.getText(), surnameBox.getText(),
						creationTime, creationTime, 1, new AsyncCallback<Contact>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								Window.alert("Geht noch ned");
							}

							@Override
							public void onSuccess(Contact result) {
								Label prenameLabel = new Label(result.getPrename());
								Label surnameLabel = new Label(result.getSurname());
								nameTable.setWidget(0, 1, prenameLabel );
								nameTable.setWidget(1, 1, surnameLabel );
								
								createdContact = result;
									Window.alert("Hier bin ich");
									addButton.removeFromParent();
									// propertyTable.setWidget(rowCount, 0,
									// propertyListBox);
									addButton = null;
									// propertyTable = new FlexTable();

									ClientSideSettings.getConnectedAdmin()
											.findAllProperties(new findAllPropertiesCallback());
									// propertyTable.setWidget(rowCount, 0,
									// propertyListBox);
									int rowCount = propertyTable.getRowCount();
									newPropertyBtn = new Button("+");
									newPropertyBtn.addClickHandler(new addNewPropertyClickHandler());
									valueTextBox = new TextBox();
									propertyTable.setWidget(rowCount, 1, valueTextBox);
									propertyTable.setWidget(rowCount, 2, newPropertyBtn);
							}});}
							else {
								int propertyId = 0;
										newPropertyBtn.removeFromParent();
									newPropertyBtn = null;
									propertyName = propertyListBox.getSelectedItemText();
									for (Property p : propertyArray) {
										if ((propertyListBox.getSelectedItemText()).equals(p.getName())) {
											propertyId = p.getBoId();
										}
									}

									ClientSideSettings.getConnectedAdmin().createValue(valueTextBox.getText(),
											propertyId, createdContact.getBoId(), 1, new createValueCallback());
								}

							}

					
				// wennn kein "+" Button dann den addbutton entfernen sonst den
				// "+"
				// Button entfernen

				/*
				 * ClientSideSettings.getConnectedAdmin().findAllProperties(new
				 * findAllPropertiesCallback());
				 * 
				 * // if(propertyListBox !=null) //
				 * {selectedProperties.add(propertyListBox); //
				 * insertValue.add(valueTextBox);}
				 * 
				 * newPropertyBtn = new Button("+"); // propertyListBox = new
				 * ListBox(); valueTextBox = new TextBox();
				 * 
				 * int rowCount = propertyTable.getRowCount(); //
				 * propertyTable.setWidget(rowCount, 0, propertyListBox);
				 * propertyTable.setWidget(rowCount, 1, valueTextBox);
				 * propertyTable.setWidget(rowCount, 2, newPropertyBtn);
				 * 
				 * newPropertyBtn.addClickHandler(new
				 * addNewPropertyClickHandler()); propertyArray.clear();
				 */
		
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
			Window.alert("Kontakt vollständig angelegt");
			Label propertyLabel = new Label(propertyName);
			Label valueLabel = new Label(result.getName());
			
			valueTextBox = new TextBox();
			
			newPropertyBtn= new Button("+");
			newPropertyBtn.addClickHandler(new addNewPropertyClickHandler());
			
			updateBtn = new Button ("Eigenschaft bearbeiten");
			updateBtn.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					final Value updatedValue = result;
					eventRow = propertyTable.getCellForEvent(event).getRowIndex();					
					final TextBox valueChangeTextBox = new TextBox();
					valueChangeTextBox.setText(updatedValue.getName());
					
					
					ClientSideSettings.getConnectedAdmin().findPropertyByPropertyId(updatedValue.getPropertyID(), new AsyncCallback<Property>(){
												@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(Property result) {
							final Property changingProperty = result;
							final ListBox propertyChangeListBox = new ListBox();
							propertyChangeListBox.addChangeHandler(new listBoxChangeHandler());
							
							for(int i=0; i<propertyArray.size(); i++){
								propertyChangeListBox.addItem(propertyArray.get(i).getName());
								if((propertyArray.get(i).getName()).equals(changingProperty.getName())){
									propertyChangeListBox.setSelectedIndex(i);
								}
							}
							
							Button saveChangesButton = new Button("Änderungen speichern");
							saveChangesButton.addClickHandler(new ClickHandler(){

								@Override
								public void onClick(ClickEvent event) {
									int propertyId =0;
									
									for (Property p : propertyArray) {
										if ((propertyChangeListBox.getSelectedItemText()).equals(p.getName())) {
											propertyId = p.getBoId();
										}
									}
									
									updatedValue.setName(valueChangeTextBox.getText());
									updatedValue.setPropertyID(propertyId);
									ClientSideSettings.getConnectedAdmin().updateValue(updatedValue, new AsyncCallback<Value>(){

										@Override
										public void onFailure(Throwable caught) {
											Window.alert("Geht nicht updaten");
										}

										@Override
										public void onSuccess(final Value result) {
											updatingValue = result;
											// TODO Auto-generated method stub
											updateBtn= new Button("Eigenschaft bearbeiten");
											updateBtn.addClickHandler(new updateBtnClickHandler());
											deleteBtn= new Button("Eigenschaft entfernen");
											propertyTable.setWidget(eventRow, 0, new HTML("<p><strong>" + propertyChangeListBox.getSelectedItemText() + "</strong></p>"));
											propertyTable.setWidget(eventRow, 1, new HTML("<p>" + valueChangeTextBox.getText() + "</p>"));
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
						}
						
					});
				
				}
				
			});
			
			deleteBtn = new Button ("Eigenschaft entfernen");
			deleteBtn.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					eventRow = propertyTable.getCellForEvent(event).getRowIndex();
					
				ClientSideSettings.getConnectedAdmin().deleteValue(result, new AsyncCallback<Void>(){

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
				
			});
			// propertyTable.clear();

			// int rowCount = propertyTable.getRowCount();

			propertyTable.setWidget(eventRow, 0, new HTML("<p><strong>" + propertyLabel.getText() + "</strong></p>"));
			propertyTable.setWidget(eventRow, 1, valueLabel);
			propertyTable.setWidget(eventRow, 2, updateBtn);
			propertyTable.setWidget(eventRow, 3, deleteBtn);

			propertyTable.setWidget(eventRow + 1, 0, propertyListBox);
			propertyTable.setWidget(eventRow + 1, 1, valueTextBox);
			propertyTable.setWidget(eventRow + 1, 2, newPropertyBtn);
			

			// RootPanel.get("content").add(propertyTable);
			// Nur wenn die CheckBox geklickt ist, wird dies ausgeführt, da
			// sonst der Kontakt keiner Liste hinzugefügt werden soll
			/*
			 * if (checkContactlist.getValue()) { for (int i = 0; i <
			 * contactlist.getItemCount(); i++) { if
			 * (contactlist.isItemSelected(i)) {
			 * ClientSideSettings.getConnectedAdmin().addContactToContactList(
			 * createdContact.getBoId(), i, new AsyncCallback<Void>() {
			 * 
			 * @Override public void onFailure(Throwable caught) { Window.
			 * alert("Kontakt konnte Kontaktliste nicht hinzugefügt werden"); }
			 * 
			 * @Override public void onSuccess(Void result) { Window.
			 * alert("Kontakt wurde angelegt und den Kontaktlisten hinzugefügt!"
			 * );
			 * 
			 * }
			 * 
			 * }); }
			 * 
			 * }
			 * 
			 * }
			 */
			// RootPanel.get("content").clear();
		
			
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
				Window.alert(Integer.toString(rowCount));
				newPropertyTextBox = new TextBox();
				Button propertySaveButton = new Button("Speichern");
				propertySaveButton.addClickHandler(new savePropertyClickHandler());
				propertyTable.setWidget(rowCount, 0, new HTML("Eigenschaftsname:"));
				propertyTable.setWidget(rowCount, 1, newPropertyTextBox);
				propertyTable.setWidget(rowCount, 2, propertySaveButton);
				Window.alert(Integer.toString(rowCount));
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
							ArrayList<String> insertedValues = new ArrayList<String>();
							Iterator<Widget> listBoxWidgets = propertyTable.iterator();

							while (listBoxWidgets.hasNext()) {
								Widget w = listBoxWidgets.next();

								if (w instanceof ListBox) {
									ListBox oldListbox = (ListBox) w;
									selectedItems.add(oldListbox.getSelectedIndex());
									oldListbox.removeFromParent();
								} else if (w instanceof TextBox) {
									TextBox values = (TextBox) w;
									insertedValues.add(values.getText());

								}
							}

							propertyArray.add(result);

							propertyListBox.setItemText(propertyListBox.getItemCount() - 1, result.getName());

							propertyListBox.addItem("oder neue Eigenschaft hinzufügen...");
							Window.alert("Size= " + Integer.toString(selectedItems.size()));

							int rowIndex = propertyTable.getRowCount();
							propertyTable.removeRow(rowIndex - 1);

							for (int i = 0; i < selectedItems.size(); i++) {
								ListBox propertyListBoxnew = new ListBox();
								TextBox valuesTextBoxnew = new TextBox();
								propertyListBoxnew.setWidth("250px");
								for (Property p : propertyArray) {
									propertyListBoxnew.addItem(p.getName());
								}
								propertyTable.setWidget(i, 0, propertyListBoxnew);
								propertyTable.setWidget(i, 1, valuesTextBoxnew);
								propertyListBoxnew.setSelectedIndex(selectedItems.get(i));
								valuesTextBoxnew.setText(insertedValues.get(i));
								Window.alert(Integer.toString(i));
							}

							propertyTable.setWidget(rowCount, 0, propertyListBox);
							propertyTable.setWidget(rowCount, 1, valueTextBox);
							propertyTable.setWidget(rowCount, 2, newPropertyBtn);

							Window.alert(Integer.toString(rowCount));
						}

					});

		}
	}
	
	private class updateBtnClickHandler implements ClickHandler{
		
		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub

			eventRow = propertyTable.getCellForEvent(event).getRowIndex();					
			final TextBox valueChangeTextBox = new TextBox();
			valueChangeTextBox.setText(updatingValue.getName());
			
			
			ClientSideSettings.getConnectedAdmin().findPropertyByPropertyId(updatingValue.getPropertyID(), new AsyncCallback<Property>(){
										@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(Property result) {
					final Property changingProperty = result;
					final ListBox propertyChangeListBox = new ListBox();
					propertyChangeListBox.addChangeHandler(new listBoxChangeHandler());
					
					for(int i=0; i<propertyArray.size(); i++){
						propertyChangeListBox.addItem(propertyArray.get(i).getName());
						if((propertyArray.get(i).getName()).equals(changingProperty.getName())){
							propertyChangeListBox.setSelectedIndex(i);
						}
					}
					
					Button saveChangesButton = new Button("Änderungen speichern");
					saveChangesButton.addClickHandler(new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							int propertyId =0;
							
							for (Property p : propertyArray) {
								if ((propertyChangeListBox.getSelectedItemText()).equals(p.getName())) {
									propertyId = p.getBoId();
								}
							}
							
							updatingValue.setName(valueChangeTextBox.getText());
							updatingValue.setPropertyID(propertyId);
							ClientSideSettings.getConnectedAdmin().updateValue(updatingValue, new AsyncCallback<Value>(){

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Geht nicht updaten");
								}

								@Override
								public void onSuccess(final Value result) {
									updatingValue = result;
									// TODO Auto-generated method stub
									updateBtn= new Button("Eigenschaft bearbeiten");
									updateBtn.addClickHandler(new updateBtnClickHandler());
									deleteBtn= new Button("Eigenschaft entfernen");
									deleteBtn.addClickHandler(new deleteBtnClickHandler());
									propertyTable.setWidget(eventRow, 0, new HTML("<p><strong>" + propertyChangeListBox.getSelectedItemText() + "</strong></p>"));
									propertyTable.setWidget(eventRow, 1, new HTML("<p>" + valueChangeTextBox.getText() + "</p>"));
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
				}
				
			});
		
		
			
		}
		
	
	}
	
	private class deleteBtnClickHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			eventRow = propertyTable.getCellForEvent(event).getRowIndex();
			
			ClientSideSettings.getConnectedAdmin().deleteValue(updatingValue, new AsyncCallback<Void>(){

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
}