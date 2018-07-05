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

public class NavigationTreeModel extends VerticalPanel{
	
	 //ArrayList für die Spreicherung der Kontaklisten
	 ArrayList<ContactList> allContactsLists = null;
	 
	 //VerticalPanel, welches dem DisclosurePanel hinzugefügt wird, um Kontaklisten-Buttons anzuzeigen
	 private VerticalPanel contactListPanel = new VerticalPanel();
	 
	 //HeaderButton für das DisclosurePanel
	 Button headerButton = new Button();
	 
	 int i = 0;
	 
	 Button myContactsButton = new Button ("Meine Kontakte");
	 
	 /**
	  * Die onLoad()-Methode wird aufgerufen, sobald das Widget 
	  * (VerticalPanel) zur Anzeige gebracht wird.
	  */
	
		 
	public NavigationTreeModel (final ContactList contactList){
		//Zuweisen einer css-Klasse für späteres Styling
		if(contactList == null){
			myContactsButton.setStyleName("gwt-Button-buttonpressednav");
		}
		

		final Button newContactButton = new Button ("Neuen Kontakt anlegen");
		
		final Button newContactListButton = new Button ("Neue Kontaktliste anlegen");
		
		//Beim Klicken wird ein PopUp für das Erstellen eines neuen Kontakts aufgerufen
		newContactButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				newContactButton.removeStyleName("gwt-Button-buttonpressednav");
				
				newContactListButton.removeStyleName("gwt-Button-buttonpressednav");

				Iterator<Widget> iterator = contactListPanel.iterator();
				
				while (iterator.hasNext()) {
					Widget w = iterator.next();
					if (w instanceof Button) {
						w.removeStyleName("gwt-Button-buttonpressednav");									
					}
				}
				
				final ContactForm newContact = new ContactForm();
				
				newContact.setGlassEnabled(true);					
				
				//Position des PopUp-Panels
				newContact.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

					public void setPosition(int offsetWidth, int offsetHeight) {
						int left = (Window.getClientWidth() - offsetWidth) / 3;
						int top = (Window.getClientHeight() - offsetHeight) / 3;

						newContact.setPopupPosition(left, top);
					}
				});
			
			}
		});
		
		//Beim Klicken wird ein PopUp für das Erstellen einer neuen Kontaktliste aufgerufen
		newContactListButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				newContactButton.removeStyleName("gwt-Button-buttonpressednav");
				myContactsButton.removeStyleName("gwt-Button-buttonpressednav");

				Iterator<Widget> iterator = contactListPanel.iterator();
				
				while (iterator.hasNext()){
					Widget w = iterator.next();
					if (w instanceof Button){
						w.removeStyleName("gwt-Button-buttonpressednav");									
					}
				}
				
				//Anzeigen des PopUp-Panels
				NewContactListPopup addContactListForm = new NewContactListPopup();
				addContactListForm.center();
				addContactListForm.show();
				Navigation reload = new Navigation();
				
			}
		});
		
		//Beim Klicken werden im content-Bereich alle Kontakte des Users in CellTable-Form angezeigt
		myContactsButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("content").clear();
				newContactButton.removeStyleName("gwt-Button-buttonpressednav");
				newContactListButton.removeStyleName("gwt-Button-buttonpressednav");
			

				Iterator<Widget> iterator = contactListPanel.iterator();
				
				while (iterator.hasNext()){
					Widget w = iterator.next();
					if (w instanceof Button){
						w.removeStyleName("gwt-Button-buttonpressednav");									
					}
				}
				
				myContactsButton.addStyleName("gwt-Button-buttonpressednav");
			
				ContactsTable allContacts = new ContactsTable(null, null);
			}
		});
		
		//DisclosurePanel, welches beim Aufklappen alle Kontaktlisten des Users anzeigt
		DisclosurePanel myContactLists = new DisclosurePanel();
		if(contactList != null)
		{
			myContactLists.setOpen(true);
		}
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
		
		//Header des DisclosurePanels wird als Button realisiert
	    myContactLists.setHeader(headerButton);
	    
	    /* Darstellen aller Kontaklisten des Users in der Navigation, 
	     * wobei jeder Kontakliste ein Button unter dem DisclosurePanel entspricht.
	     */
	    ClientSideSettings.getConnectedAdmin().getContactListsByUserPermission(ClientSideSettings.getCurrentUser().getBoId(), new AsyncCallback<ArrayList<ContactList>>(){
		
	    	@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(ArrayList<ContactList> result) {
				allContactsLists = result;
				for (final ContactList cl : allContactsLists) {
					final Button showCL = new Button();
					if(contactList != null){
						if (cl.getName() == contactList.getName()){
							showCL.addStyleName("gwt-Button-buttonpressednav");
						}
					}
					
					if (cl.getCreatorId() != ClientSideSettings.getCurrentUser().getBoId()){
						showCL.setHTML("<strong><i> &ensp;" + cl.getName() + "</i></strong>");
					} else {
						showCL.setHTML("&ensp;" + cl.getName());
					}
					
				/* Beim Klicken auf den Button werden im content-Bereich 
				 * alle Kontakte angezeigt, die in dieser Kontaktliste gespeicht sind.
				 */
					showCL.addClickHandler(new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							RootPanel.get("content").clear();
							ContactListForm3 showContactList = new ContactListForm3(cl);
							newContactButton.removeStyleName("gwt-Button-buttonpressednav");
							newContactListButton.removeStyleName("gwt-Button-buttonpressednav");
							myContactsButton.removeStyleName("gwt-Button-buttonpressednav");
							
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
					//Kontaklisten-Buttons werden dem Kontaklisten-VerticalPanel zugewiesen
					contactListPanel.add(showCL);
				}
			}
	    	
	    });
		
	    //Zuweisen dem DisclosurePanel eines VerticalPanels mit Kontaklisten
	    myContactLists.setContent(contactListPanel);

	    //Alle Buttons und DisclosurePanel werden der Navigation zugewiesen
		this.add(newContactButton);
		this.add(newContactListButton);
		this.add(new HTML("<div style=\"margin: 0px 0px 5px 5px;\"><hr></div>"));
		this.add(myContactsButton);
		this.add(myContactLists);
	}

}
