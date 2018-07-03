package de.hdm.Connected.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.bo.ContactList;


public class Navigation extends VerticalPanel {
	 ArrayList<ContactList> allContactsLists = null;
	 private VerticalPanel contactListPanel = new VerticalPanel();
	 Button headerButton = new Button();
	 int i =0;
	 Button BMyContacts = new Button ("Meine Kontakte");
	public void onLoad() {
		
		BMyContacts.setStyleName("gwt-Button-buttonpressednav");
		//ContactlistsCell cellTreeModel = new ContactlistsCell();
		
		//CellTree navTree = new CellTree(cellTreeModel, null);
		//ContactListsTreeViewModel contactListsTreeModel = new ContactListsTreeViewModel();
		
		//CellTree contactListsTree = new CellTree (contactListsTreeModel, null);
		
		final Button BNewContact = new Button ("Neuen Kontakt anlegen");
		final Button BNewContactList = new Button ("Neue Kontaktliste anlegen");
		// BNewContact.addStyleName("");
		
		BNewContact.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				
				
				BNewContact.removeStyleName("gwt-Button-buttonpressednav");
				BNewContactList.removeStyleName("gwt-Button-buttonpressednav");

				Iterator<Widget> iterator = contactListPanel.iterator();
				
				while(iterator.hasNext()){
					Widget w = iterator.next();
					if(w instanceof Button){
						w.removeStyleName("gwt-Button-buttonpressednav");									
					}
				}
				
				
				final ContactForm newContact = new ContactForm();
				// Enable glass background.
				newContact.setGlassEnabled(true);					
				
				newContact.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

					public void setPosition(int offsetWidth, int offsetHeight) {
						int left = (Window.getClientWidth() - offsetWidth) / 3;
						int top = (Window.getClientHeight() - offsetHeight) / 3;

						newContact.setPopupPosition(left, top);
					}
				});
			
			}
		});
		
		
		// BNewContactList.addStyleName("");
		
		BNewContactList.addClickHandler(new ClickHandler() {
		// BNewContactList.addStyleName("");
			
			@Override
			public void onClick(ClickEvent event) {
				BNewContact.removeStyleName("gwt-Button-buttonpressednav");
				BMyContacts.removeStyleName("gwt-Button-buttonpressednav");

				Iterator<Widget> iterator = contactListPanel.iterator();
				
				while(iterator.hasNext()){
					Widget w = iterator.next();
					if(w instanceof Button){
						w.removeStyleName("gwt-Button-buttonpressednav");									
					}
				}
				
				NewContactListPopup addContactListForm = new NewContactListPopup();
				addContactListForm.center();
				addContactListForm.show();
				Navigation reload = new Navigation();
				
				
			}
		});
		
	
		// BMyContacts.addStyleName("");
		
		
		BMyContacts.addClickHandler(new ClickHandler() {
		// BMyContacts.addStyleName("");
			
		
			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("content").clear();
				BNewContact.removeStyleName("gwt-Button-buttonpressednav");
				BNewContactList.removeStyleName("gwt-Button-buttonpressednav");
			

				Iterator<Widget> iterator = contactListPanel.iterator();
				
				while(iterator.hasNext()){
					Widget w = iterator.next();
					if(w instanceof Button){
						w.removeStyleName("gwt-Button-buttonpressednav");									
					}
				}
				
				BMyContacts.addStyleName("gwt-Button-buttonpressednav");
			
				ContactsTable allContacts = new ContactsTable(null, null);
			}
		});
		
	//	ContactlistsCellList cellList = new ContactlistsCellList();
		//CellTree?
		//String header  = "Meine Kontaktlisten";
		
		DisclosurePanel myContactLists = new DisclosurePanel();
			//myContactLists.setContent(contactListsTree);
		
		
		headerButton.setHTML("&#x25BA  Meine Kontaktlisten");
		headerButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				if(headerButton.getHTML() == "►  Meine Kontaktlisten"){
					headerButton.setHTML("&#x25BC  Meine Kontaktlisten");}
				else{headerButton.setHTML("&#x25BA  Meine Kontaktlisten");}
				
			}
			
		});
	
	    myContactLists.setHeader(headerButton);
	   // myContactLists.setContent(cellList);
	    Button BSharedContacts = new Button ("Geteilte Kontakte");
		// BSharedContacts.addStyleName("");
	    
	    
	    
	    ClientSideSettings.getConnectedAdmin().getContactListsByUserPermission(ClientSideSettings.getCurrentUser().getBoId(), new AsyncCallback<ArrayList<ContactList>>(){
	   
			@Override
			public void onFailure(Throwable caught) {

				
			}

			@Override
			public void onSuccess(ArrayList<ContactList> result) {
				allContactsLists = result;
				for (final ContactList cl : allContactsLists){
					final Button showCL = new Button();

					if(cl.getCreatorId() != ClientSideSettings.getCurrentUser().getBoId()){
						showCL.setHTML("<strong><i> &ensp;" + cl.getName() + "</i></strong>");
					}else{
						showCL.setHTML("&ensp;" + cl.getName());
					}
					
					showCL.addClickHandler(new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							RootPanel.get("content").clear();
							ContactListForm3 showContactList = new ContactListForm3(cl);
							BNewContact.removeStyleName("gwt-Button-buttonpressednav");
							BNewContactList.removeStyleName("gwt-Button-buttonpressednav");
							BMyContacts.removeStyleName("gwt-Button-buttonpressednav");
							
							Iterator<Widget> iterator = contactListPanel.iterator();
							
							while(iterator.hasNext()){
								Widget w = iterator.next();
								if(w instanceof Button){
									w.removeStyleName("gwt-Button-buttonpressednav");									
								}
							}
							showCL.addStyleName("gwt-Button-buttonpressednav");
							
						}
						
					});
					contactListPanel.add(showCL);
				}
			}
	    	
	    });
		
	    myContactLists.setContent(contactListPanel);
		BSharedContacts.addClickHandler(new ClickHandler() {
		// BSharedContacts.addStyleName("");
			
			
			@Override
			public void onClick(ClickEvent event) {
				//geteilte Kontakte 
			}
		});

			
		this.add(BNewContact);
		this.add(BNewContactList);
		this.add(new HTML("<div style=\"margin: 0px 0px 5px 5px;\"><hr></div>"));
		this.add(BMyContacts);
		this.add(myContactLists);

	}		
}