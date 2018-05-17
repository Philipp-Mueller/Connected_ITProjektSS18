package de.hdm.Connected.client.gui;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.google.appengine.labs.repackaged.com.google.common.collect.Multiset.Entry;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.ConnectedAdminAsync;
import de.hdm.Connected.shared.bo.Contact;
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

	Label firstNameLabel = new Label("Vorname:");
	TextBox firstNameBox = new TextBox();
	Label surnameLabel = new Label("Nachname:");
	TextBox surnameBox = new TextBox();
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
								try {
									Iterator<Widget> propertyWidgets = propertyTable.iterator();

									
										while (propertyWidgets.hasNext()) {
											Widget ch = propertyWidgets.next();
											if (ch instanceof ListBox) {
												ListBox propertyListBox = (ListBox) ch;
												for (int i = 0; i < propertyArray.size();i++) {
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
												selectedProperties.get(i), result.getBoId(),
												new createValueCallback());
									}
								} catch (Exception e) {
									Window.alert(e.toString());
									e.printStackTrace();
								}
								/*
								 * for(int i = 0;
								 * i<=propertyTable.getRowCount(); i++){
								 * widgetMap.put(propertyTable.getWidget(i, 0),
								 * propertyTable.getWidget(i, 1)); }
								 * for(Map.Entry<Widget, Widget> entry :
								 * widgetMap.entrySet()){
								 * ClientSideSettings.getConnectedAdmin().
								 * createValue(entry..getValue(),
								 * entry.getKey(), result.getBoId(), new
								 * createValueCallback()); }
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
			for (int i = 0; i < result.size(); i++) {
				Property propertyItem = result.get(i);

				if (propertyItem.getName() != "Vorname" || propertyItem.getName() != "Nachname") {

					propertyArray.add(propertyItem);
					propertyListBox.addItem(propertyArray.get(i).getName());
				}

			}
			propertyListBox.addItem("oder neue Eigenschaft hinzufügen...");
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

			/*
			 * newPropertyBtn = new Button("+");
			 * propertyPanel.add(newPropertyBtn);
			 * newPropertyBtn.addClickHandler(new addNewPropertyClickHandler());
			 * /*for(Property p :propertyArray){
			 * if(propertyListBox.getSelectedItemText() == p.getName()){
			 * valuesMap.put(p.getBoId(), valueTextBox.getText()); } }
			 */
			/*
			 * valuesMap.put(propertyArray.get(propertyListBox.getSelectedIndex(
			 * )).getBoId(), valueTextBox.getText());
			 */

			/*
			 * TODO ClientSideSettings.getConnectedAdmin().findAllProperties(new
			 * AsyncCallback<ArrayList<Property>>(){
			 * 
			 * @Override public void onFailure(Throwable caught) {
			 * ClientSideSettings.getLogger().
			 * severe("Konnte die Eigenschaften nicht laden"); }
			 * 
			 * @Override public void onSuccess(ArrayList<Property> result) {
			 * if(addButton != null){ propertyPanel.remove(addButton); addButton
			 * = null;
			 * 
			 * 
			 * } else{ propertyPanel.remove(newPropertyBtn); } for (int i = 0; i
			 * < result.size(); i++) { Property propertyItem = result.get(i);
			 * 
			 * if(result.get(i).getName() != "Vorname" ||
			 * result.get(i).getName() != "Nachname"){
			 * propertyArray.add(propertyItem);
			 * propertyListBox.addItem(propertyItem.getName()); } }
			 * propertyListBox.addItem("oder neue Eigenschaft hinzufügen...");
			 * propertyPanel.add(propertyListBox); TextBox propertyTextBox = new
			 * TextBox(); valuePanel.add(propertyTextBox);
			 * propertyPanel.add(newPropertyBtn); addButton.addClickHandler(new
			 * addNewPropertyClickHandler()); } });
			 */
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
			RootPanel.get("content").clear();

		}

	}

}
