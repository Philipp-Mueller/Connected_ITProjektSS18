package de.hdm.Connected.client.gui;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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
	ListBox propertyListBox = null;
	TextBox propertyTextBox = null;
	Button addButton = new Button("weitere Eigenschaften hinzufügen");
	Button addButton2 = new Button("+");
	HorizontalPanel itemPanel = new HorizontalPanel();
	
	VerticalPanel propertyPanel = new VerticalPanel();
	VerticalPanel valuePanel = new VerticalPanel();
		
	
	ArrayList<Property> propertyArray = new ArrayList<Property>();
	ArrayList<ListBox> selectedProperties = new ArrayList<ListBox>();
	ArrayList<TextBox> insertValue = new ArrayList<TextBox>();
	
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
		
		//RootPanel.get("content").clear();
		
		HorizontalPanel topPanel = new HorizontalPanel();
		/* TODO     style erstellen in CSS
		 * topPanel.addStyleName("headline"); 
		 */
		
		topPanel.add(new HTML("<h2> Neuen Kontakt erstellen</h2>"));
		
		
		
				
		RootPanel.get("content").add(topPanel);
	
		propertyPanel.add(new HTML ("<h3> Vorname: </h3>"));	
		
		valuePanel.add(firstNameBox);
		
		propertyPanel.add(new HTML ("<h3> Nachname: </h3>"));
		
		valuePanel.add(surnameBox);
		
		propertyPanel.add(addButton);
		//Eigenschaft "+" hinzufügen mit ListBox für Vorgaben bzw. für eigene des Users
		
	    addButton.addClickHandler(new addNewPropertyClickHandler());
		
		itemPanel.add(propertyPanel);
		itemPanel.add(valuePanel);
		
		/*	
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
		
	
		*/
		RootPanel.get("content").add(itemPanel);
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
				TextArea test = new TextArea();
				test.setText(selectedProperties.get(0).getSelectedItemText());
				valuePanel.add(test);
				
				
				 ClientSideSettings.getConnectedAdmin().createContact(firstNameBox.getText(),  
						 												surnameBox.getText(), creationTime, creationTime,
				 														ClientSideSettings.getCurrentUser().getBoId(), 
				 														new AsyncCallback<Contact>(){
					public void onFailure(Throwable caught){
						ClientSideSettings.getLogger().severe("Konnte Kontakt nicht anlegen");
					}
					public void onSuccess(Contact result) {
						
						RootPanel.get("content").add(new ContactForm(result));
						String[][] propertyvalue = new String[propertyArray.size()][2];
						
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
/*	private class findAllPropertiesCallback implements AsyncCallback<ArrayList<Property>>{

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			ClientSideSettings.getLogger().severe("Konnte nicht laden");
			Window.alert("Da ist wohl etwas schief gelaufen");
		}

		public void onSuccess(ArrayList<Property> result) {
			//alle Eigenschaften in Vektor laden
			for (int i = 0; i < result.size(); i++) {
				Property propertyItem = result.get(i);
				
				if(result.get(i).getName() != "Vorname" || result.get(i).getName() != "Nachname"){
				propertyArray.add(propertyItem);
				propertyListBox.addItem(propertyItem.getName());
				
				}
								
			}
			propertyListBox.addItem("oder neue Eigenschaft hinzufügen...");
		}
		
	}*/
	
// ----Clickhandler für add Button-----
	private class addNewPropertyClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			if(propertyListBox !=null) {selectedProperties.add(propertyListBox); insertValue.add(propertyTextBox);}
			// wennn kein "+" Button dann den addbutton entfernen sonst den "+" Button entfernen
			if(addButton != null){
				propertyPanel.remove(addButton);
				addButton = null;
				
				
			}
			else{
				propertyPanel.remove(addButton2);
				addButton2 = null;
			}
			propertyListBox = new ListBox();
			propertyListBox.addItem("oder neue Eigenschaft hinzufügen...");
			propertyListBox.addItem("alles cool");
			propertyPanel.add(propertyListBox);
			propertyTextBox = new TextBox();
			valuePanel.add(propertyTextBox);
			addButton2 = new Button("+");
			propertyPanel.add(addButton2);
			addButton2.addClickHandler(new addNewPropertyClickHandler());
			/*TextBox testBox = new TextBox();
			for(int i =0 ; i<selectedProperties.size(); i++){
			testBox.setText(selectedProperties.get(i).getSelectedItemText());
			valuePanel.add(testBox);			
			}*/
				
	/*TODO		ClientSideSettings.getConnectedAdmin().findAllProperties(new AsyncCallback<ArrayList<Property>>(){
				@Override
				public void onFailure(Throwable caught) {
					ClientSideSettings.getLogger().severe("Konnte die Eigenschaften nicht laden");
				}
				@Override
				public void onSuccess(ArrayList<Property> result) {					
					if(addButton != null){
						propertyPanel.remove(addButton);
						addButton = null;
						
						
					}
					else{
						propertyPanel.remove(addButton2);
					}
					for (int i = 0; i < result.size(); i++) {
						Property propertyItem = result.get(i);
						
						if(result.get(i).getName() != "Vorname" || result.get(i).getName() != "Nachname"){
						propertyArray.add(propertyItem);
						propertyListBox.addItem(propertyItem.getName());
						}
					}
					propertyListBox.addItem("oder neue Eigenschaft hinzufügen...");
					propertyPanel.add(propertyListBox);
					TextBox propertyTextBox = new TextBox();
					valuePanel.add(propertyTextBox);
					propertyPanel.add(addButton2);
					addButton.addClickHandler(new addNewPropertyClickHandler());
				}
			});
*/
		}
	
			
			
		}
		
	
}
