package de.hdm.Connected.client.gui;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Property;

/** 
 * Klasse für die Bereistellung eines Formulars zum Anlegen/Bearbeiten eines Kontakts
 * 
 * @author Philipp
 *
 */

public class ContactForm extends Widget {
	
	//ConnectedAdminAsync connectedAdmin = ClientSideSettings.getConnectedAdmin();
	private Contact selectedContact = null;
	
	
	Label firstNameLabel = new Label("Vorname:");
	TextBox firstNameBox = new TextBox();
	Label surnameLabel = new Label("Nachname:");
	TextBox surnameBox = new TextBox();
	Button addButton = new Button("+");
	ListBox propertyListBox = new ListBox();
	
	
	ArrayList<Property> propertyArray = new ArrayList<Property>();
	
	/**
	 * Konstruktor wenn ein Kontakt schon existiert.
	 * @param contact
	 */
	
	public ContactForm(Contact contact) {
		
		this.selectedContact = contact;
		
		RootPanel.get("content").clear();
		
	}
	
	/**
	 * Konstruktor wenn ein neuer Kontakt angelegt wird.
	 */
	
	
	public ContactForm() {
		
		RootPanel.get("content").clear();
		
		HorizontalPanel topPanel = new HorizontalPanel();
		/* TODO     style erstellen in CSS
		 * topPanel.addStyleName("headline"); 
		 */
		
		topPanel.add(new HTML("<h2> Neuen Kontakt erstellen</h2>"));
		
		RootPanel.get("content").add(topPanel);
		
		RootPanel.get("content").add(new HTML ("<h3> Vorname: </h3>"));
		
		RootPanel.get("content").add(firstNameBox);
		
		RootPanel.get("content").add(new HTML ("<h3> Nachname: </h3>"));
		
		RootPanel.get("content").add(surnameBox);
	
		//TODO  Eigenschaft "+" hinzufügen mit ListBox für Vorgaben bzw. für eigene des Users
		
	    
		addButton.addClickHandler(new ClickHandler (){
			@Override
			
		public void onClick(ClickEvent event){
				ClientSideSettings.getConnectedAdmin().findAllProperties(new findAllPropertiesCallback(){
					public void onFailure(Throwable caught) {
						ClientSideSettings.getLogger().severe("Konnte die Eigenschaften nicht laden");
					}
					
					public void onSuccess(Void result) {
						RootPanel.get("Content").add(propertyListBox);
						RootPanel.get("Content").add(addButton);
					}
				});
			}
		});
		
	
		
		
		HorizontalPanel bottomPanel = new HorizontalPanel();
		
		/**
		 * java.util.Date berücksichtigt auch Stunden, Minuten, Sekunden und sogar Millisekunden,
		 * java.sql.Date hingegen entspricht dem SQL DATE. Deshalb muss das Datum vorher konvertiert
		 * werden. 
		 */
		
	
		
		Button saveButton = new Button("Kontakt erstellen");
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			
		
			public void onClick(ClickEvent event){
				java.sql.Timestamp creationTime = new Timestamp(System.currentTimeMillis());
				
				 ClientSideSettings.getConnectedAdmin().createContact(firstNameBox.getText(),  
						 												surnameBox.getText(), creationTime, creationTime,
				 														ClientSideSettings.getCurrentUser().getBoId(), 
				 														new AsyncCallback<Contact>(){
					public void onFailure(Throwable caught){
						ClientSideSettings.getLogger().severe("Konnte Kontakt nicht anlegen");
					}
					public void onSuccess(Contact result) {
						
						RootPanel.get("content").add(new ContactForm(result));
						
					}
					
									});
			}
		
		
	}); 
	
	bottomPanel.add(saveButton);
	
	Button cancelButton = new Button("Abbrechen");
	cancelButton.addClickHandler(new ClickHandler() {
		@Override
		public void onClick(ClickEvent event){
			Window.Location.reload();
		}
	});
	
	bottomPanel.add(cancelButton);
	
	RootPanel.get("content").add(bottomPanel);
	
	
}
	private class findAllPropertiesCallback implements AsyncCallback<ArrayList<Property>>{

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}

		public void onSuccess(ArrayList<Property> result) {
			//alle Eigenschaften in Vektor laden
			for (int i = 0; i < result.size(); i++) {
				Property propertyItem = result.get(i);
				
				if(result.get(i).getDescription() != "Vorname" || result.get(i).getDescription() != "Nachname"){
				propertyArray.add(propertyItem);
				propertyListBox.addItem(propertyItem.getDescription());
				}
								
			}
			
		}
		
	}
}
