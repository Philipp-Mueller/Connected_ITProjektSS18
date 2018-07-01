package de.hdm.Connected.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.client.gui.ContactsTable;


public class Navigation extends VerticalPanel {
	
	public void onLoad() {
		
		Button cButton = new Button ("Meine Kontakte");
		//myContacts.addStyleName("");
		
		cButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//ContactsTable ctable = new ContactsTable();
				//RootPanel.get("content").clear();
				//RootPanel.get("content").add(ctable);
			}
		});
		
		ContactlistsCellList cellList = new ContactlistsCellList();
		
		DisclosurePanel clPanel = new DisclosurePanel();
		Button headerButton = new Button("Meine Kontaktlisten");
		clPanel.setHeader(headerButton);
		
		//VerticalPanel vpanel = new VerticalPanel();
		//vpanel.setBorderWidth(1);	    
	    //vpanel.setWidth("200");
	    
	    //vpanel.add(cellList);
		clPanel.setContent(cellList);
		
		this.add(cButton);
		this.add(clPanel);	

	}		
}
