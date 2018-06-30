package de.hdm.Connected.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
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
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.client.gui.ContactsTable;


public class Navigation extends VerticalPanel {
	
	public void onLoad() {
		
		Button myContacts = new Button ("Meine Kontakte");
		//myContacts.addStyleName("");
		
		myContacts.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ContactsTable ctable = new ContactsTable();
			}
		});
		
		/*
		
		Button myContactlists = new Button ("Meine Kontaktlisten");
		myContactlists.addStyleName("");
		
		myContactlists.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//Kontaktlisten
			}
		}); */
		
		
		/* Button sharedContacts = new Button ("Kontakte");
		//sharedContactlists.addStyleName("");
		
		sharedContacts.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//Kontakte
			}
		}); */
		
		/* Button sharedContactlists = new Button ("Kontaktlisten");
		sharedContactlists.addStyleName("");
		
		myContacts.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//Kontaktlisten
			}
		}); */
		
		ContactlistsCellList clcl = new ContactlistsCellList();
		
		DisclosurePanel clists = new DisclosurePanel("Meine Kontaktlisten");
		
		//clists.getHeaderTextAccessor().setText("Meine Kontaktlisten");
		
		clists.setContent(clcl);
		
		
		/*VerticalPanel sharedPanel = new VerticalPanel();
		sharedPanel.add(sharedContacts);
		sharedPanel.add(sharedContactlists); */
		
		//shared.setContent(sharedPanel);
		
		this.add(myContacts);
		this.add(clists);
		//this.add(myContactlists);		
		
//		RootPanel.get("nav").add(sharedPanel);
//		RootPanel.get("nav").add(myContacts);
//		RootPanel.get("nav").add(clists);
//		RootPanel.get("nav").add(myContactlists);
	}		
}
