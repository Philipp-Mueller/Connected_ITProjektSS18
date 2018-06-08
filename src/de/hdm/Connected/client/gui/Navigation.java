package de.hdm.Connected.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Navigation extends VerticalPanel {
	
	public void onLoad() {
		
		ContactListsTreeViewModel contactListsTreeModel = new ContactListsTreeViewModel();
		
		CellTree contactListsTree = new CellTree (contactListsTreeModel, null);
		
		Button BNewContact = new Button ("Neuen Kontakt anlegen");
		// BNewContact.addStyleName("");
		
		BNewContact.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ContactForm addContactForm = new ContactForm();
			
			}
		});
		
		Button BNewContactList = new Button ("Neue Kontaktliste anlegen");
		// BNewContact.addStyleName("");
		
		BNewContactList.addClickHandler(new ClickHandler() {
		// BNewContactList.addStyleName("");
			
			@Override
			public void onClick(ClickEvent event) {
				ContactListForm addContactListForm = new ContactListForm();
			
			}
		});
			
		this.add(BNewContact);
		this.add(BNewContactList);
		this.add(contactListsTree);
			
	}		
}