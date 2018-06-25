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
		
		Button myContacts = new Button ("Meine Kontakte");
		//myContacts.addStyleName("");
		
		myContacts.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//Kontakte
			}
		});
		
		Button myContactlists = new Button ("Meine Kontaktlisten");
		//myContactlists.addStyleName("");
		
		myContactlists.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//Kontaktlisten
			}
		});
		
		Button sharedContacts = new Button ("Kontakte");
		//sharedContactlists.addStyleName("");
		
		sharedContacts.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//Kontakte
			}
		});
		
		Button sharedContactlists = new Button ("Kontaktlisten");
		//sharedContactlists.addStyleName("");
		
		myContacts.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//Kontaktlisten
			}
		});
		
		DisclosurePanel shared = new DisclosurePanel("Geteilt");
		
		VerticalPanel sharedPanel = new VerticalPanel();
		sharedPanel.add(sharedContacts);
		sharedPanel.add(sharedContactlists);
		
		shared.setContent(sharedPanel);
	    	
		this.add(myContacts);
		this.add(myContactlists);
		this.add(shared);

			
	}		
}