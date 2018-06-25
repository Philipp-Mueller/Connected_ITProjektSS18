package de.hdm.Connected.client.gui;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;


public class Navigation extends VerticalPanel {
	
	public void onLoad() {
		
		//ContactListsTreeViewModel contactListsTreeModel = new ContactListsTreeViewModel();
		
		//CellTree contactListsTree = new CellTree (contactListsTreeModel, null);
		
		Button BNewContact = new Button ("Neuen Kontakt anlegen");
		// BNewContact.addStyleName("");
		
		BNewContact.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ContactForm addContactForm = new ContactForm();
			
			}
		});
		
		Button BNewContactList = new Button ("Neue Kontaktliste anlegen");
		// BNewContactList.addStyleName("");
		
		BNewContactList.addClickHandler(new ClickHandler() {
		// BNewContactList.addStyleName("");
			
			@Override
			public void onClick(ClickEvent event) {
				ContactListForm addContactListForm = new ContactListForm();
			}
		});
		
		Button BMyContacts = new Button ("Meine Kontakte");
		// BMyContacts.addStyleName("");
		
		BMyContacts.addClickHandler(new ClickHandler() {
		// BMyContacts.addStyleName("");
			
			@Override
			public void onClick(ClickEvent event) {
				//Kontakt-Tabelle;
			
			}
		});

		//CellTree?
		DisclosurePanel myContactLists = new DisclosurePanel("Meine Kontaktlisten");
			//myContactLists.setContent(contactListsTree);
	    
	    Button BSharedContacts = new Button ("Geteilte Kontakte");
		// BSharedContacts.addStyleName("");
		
		BSharedContacts.addClickHandler(new ClickHandler() {
		// BSharedContacts.addStyleName("");
			
			@Override
			public void onClick(ClickEvent event) {
				//geteilte Kontakte 
			}
		});

			
		this.add(BNewContact);
		this.add(BNewContactList);
		this.add(BMyContacts);
		this.add(myContactLists);
		this.add(BSharedContacts);
		
			
	}		
}