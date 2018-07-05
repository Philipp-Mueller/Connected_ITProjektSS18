package de.hdm.Connected.client.gui;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.bo.ContactList;


public class Navigation extends VerticalPanel {
	 
	 ArrayList<ContactList> allContactsLists = null;
	 
	 private VerticalPanel contactListPanel = new VerticalPanel();
	 
	 Button headerButton = new Button();
	 
	 int i = 0;
	 
	 Button BMyContacts = new Button ("Meine Kontakte");
	 
	 public void onLoad() {

		BMyContacts.setStyleName("gwt-Button-buttonpressednav");

		final Button BNewContact = new Button ("Neuen Kontakt anlegen");
		
		final Button BNewContactList = new Button ("Neue Kontaktliste anlegen");
		
		BNewContact.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				BNewContact.removeStyleName("gwt-Button-buttonpressednav");
				
				BNewContactList.removeStyleName("gwt-Button-buttonpressednav");

				Iterator<Widget> iterator = contactListPanel.iterator();
				
				while (iterator.hasNext()) {
					Widget w = iterator.next();
					if (w instanceof Button) {
						w.removeStyleName("gwt-Button-buttonpressednav");									
					}
				}
				
				final ContactForm newContact = new ContactForm();
				
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
		
		BNewContactList.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				BNewContact.removeStyleName("gwt-Button-buttonpressednav");
				BMyContacts.removeStyleName("gwt-Button-buttonpressednav");

				Iterator<Widget> iterator = contactListPanel.iterator();
				
				while (iterator.hasNext()){
					Widget w = iterator.next();
					if (w instanceof Button){
						w.removeStyleName("gwt-Button-buttonpressednav");									
					}
				}
				
				NewContactListPopup addContactListForm = new NewContactListPopup();
				addContactListForm.center();
				addContactListForm.show();
				Navigation reload = new Navigation();
				
			}
		});
		
		BMyContacts.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("content").clear();
				BNewContact.removeStyleName("gwt-Button-buttonpressednav");
				BNewContactList.removeStyleName("gwt-Button-buttonpressednav");
			

				Iterator<Widget> iterator = contactListPanel.iterator();
				
				while (iterator.hasNext()){
					Widget w = iterator.next();
					if (w instanceof Button){
						w.removeStyleName("gwt-Button-buttonpressednav");									
					}
				}
				
				BMyContacts.addStyleName("gwt-Button-buttonpressednav");
			
				ContactsTable allContacts = new ContactsTable(null, null);
			}
		});
		
		DisclosurePanel myContactLists = new DisclosurePanel();
		
		headerButton.setHTML("&#x25BA  Meine Kontaktlisten");
		headerButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				if (headerButton.getHTML() == "►  Meine Kontaktlisten") {
					headerButton.setHTML("&#x25BC  Meine Kontaktlisten");
					} else { 
						headerButton.setHTML("&#x25BA  Meine Kontaktlisten");
				}
			}
			
		});
	
	    myContactLists.setHeader(headerButton);

	    ClientSideSettings.getConnectedAdmin().getContactListsByUserPermission(ClientSideSettings.getCurrentUser().getBoId(), new AsyncCallback<ArrayList<ContactList>>(){
	   
			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(ArrayList<ContactList> result) {
				allContactsLists = result;
				for (final ContactList cl : allContactsLists) {
					final Button showCL = new Button();

					if (cl.getCreatorId() != ClientSideSettings.getCurrentUser().getBoId()){
						showCL.setHTML("<strong><i> &ensp;" + cl.getName() + "</i></strong>");
					} else {
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
							
							while (iterator.hasNext()) {
								Widget w = iterator.next();
								if (w instanceof Button) {
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

		this.add(BNewContact);
		this.add(BNewContactList);
		this.add(new HTML("<div style=\"margin: 0px 0px 5px 5px;\"><hr></div>"));
		this.add(BMyContacts);
		this.add(myContactLists);

	}		
}