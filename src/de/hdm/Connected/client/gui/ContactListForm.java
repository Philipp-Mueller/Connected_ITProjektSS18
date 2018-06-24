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

public class ContactListForm extends Widget {

	Label nameLabel = new Label("Name:");
	TextBox nameBox = new TextBox();
	Label yourContactsLabel = new Label("Deine Kontakte:");
	ListBox contactListBox = new ListBox();
	Button addContactButton = new Button("Kontakt hinzufügen");
	Button createListButton = new Button("Liste erstellen", new createContactListClickhandler());

	HorizontalPanel namePanel = new HorizontalPanel();
	HorizontalPanel contactsPanel = new HorizontalPanel();
	
	private ListDataProvider<Contact> dataProvider = new ListDataProvider<Contact>();
	Set<Contact> set1 = null;
	String sizeSt;

	public ContactListForm() {

		VerticalPanel topPanel = new VerticalPanel();

		namePanel.add(nameLabel);
		namePanel.add(nameBox);

		topPanel.add(new HTML("<h2> Neue Kontaktliste erstellen</h2>"));
		topPanel.add(namePanel);
		topPanel.add(addContactButton);
		topPanel.add(createListButton);

		RootPanel.get("content").add(topPanel);

		
		
		
		CellTable<Contact> contacttable = new CellTable<Contact>();
		

		
		
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
		

		Contact c1 = new Contact();
		c1.setPrename("Frank");
		c1.setSurname("herbert");
		Contact c2 = new Contact();
		c2.setPrename("Addi");
		c2.setSurname("Bert");
		
		
		topPanel.add(contacttable);
		
		
		final MultiSelectionModel<Contact> selectionModel = new MultiSelectionModel<Contact>(
				Contact.KEY_PROVIDER);
			contacttable.setSelectionModel(selectionModel,
			    DefaultSelectionEventManager.<Contact> createCheckboxManager());
		
			Column<Contact, Boolean> checkColumn = new Column<Contact, Boolean>(
				    new CheckboxCell(false, false)) {
				  @Override
				  public Boolean getValue(Contact object) {
				    // Get the value from the selection model.
				    return selectionModel.isSelected(object);
				  }
				  
				};
				contacttable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
				contacttable.setColumnWidth(checkColumn, 40, Unit.PX);
		
		
		
		contacttable.addColumn(prenameColumn, "Vorname");
		contacttable.addColumn(surnameColumn, "Nachname");
		
		List<Contact> listcontacts = new ArrayList<>();
		listcontacts.add(c1);
		listcontacts.add(c2);
		
		
		dataProvider.getList().clear();
		dataProvider.getList().addAll(listcontacts);
		dataProvider.addDataDisplay(contacttable);

		//Set<Contact> selectedObjects = selectionModel.getSelectedSet();
		set1 = selectionModel.getSelectedSet();
		//set1.add(selectionModel.getSelectedObject());
		//set1 = selectionModel.
		//set1 = selectionModel.getSelectedSet();
		int setSize = set1.size();
		sizeSt = Integer.toString(setSize);
 
				
				

		
	}

	private class createContactListClickhandler implements ClickHandler {

		public void onClick(ClickEvent event) {

			ClientSideSettings.getConnectedAdmin().createContactList(nameBox.getText(), 1, 
					new createContactListCallback());
			Window.alert(sizeSt);
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
