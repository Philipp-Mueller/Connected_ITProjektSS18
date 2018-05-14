package de.hdm.Connected.client.gui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/** 
 * Klasse für die Bereistellung eines Formulars zum Anlegen/Bearbeiten einer Kontaltiste
 * 
 * @author Moritz
 *
 */

public class ContactListForm extends Widget{
	
	Label nameLabel = new Label("Name:");
	TextBox nameBox = new TextBox();
	Label yourContactsLabel = new Label("Deine Kontakte:");
	ListBox contactListBox = new ListBox();
	Button addContactButton = new Button("Kontakt hinzufügen");
	Button createListButton = new Button("Liste erstellen");
	
	HorizontalPanel namePanel = new HorizontalPanel();
	HorizontalPanel contactsPanel = new HorizontalPanel();
	
	
	public ContactListForm () {
		
		
		VerticalPanel topPanel = new VerticalPanel();
		
		
		
		namePanel.add(nameLabel);
		namePanel.add(nameBox);
		
		topPanel.add(new HTML("<h2> Neue Kontaktliste erstellen</h2>"));
		topPanel.add(namePanel);
		topPanel.add(addContactButton);
		topPanel.add(createListButton);
		
		RootPanel.get("content").add(topPanel);
	}

}
