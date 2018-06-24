package de.hdm.Connected.client.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.Value;

public class StartPage extends Widget {

	 /**
	   * The UiBinder interface used by this example.
	   */
	 
	CellTable<Contact> overview = new CellTable<Contact>();
	private ListDataProvider<Contact> dataProvider = new ListDataProvider<Contact>();
	private ArrayList<Contact> allContacts = new ArrayList<Contact>();
	 /**
	   * The pager used to change the range of data.
	   */
	 // @UiField(provided = true)
	  SimplePager pager;

	
	    
	  public StartPage () {
		  
		// Create a Pager to control the table.
		    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		    pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		    pager.setDisplay(overview);
		  
		  ClientSideSettings.getConnectedAdmin().findAllContacts(new AsyncCallback<ArrayList<Contact>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<Contact> result) {
				for(Contact c : result){
					allContacts.add(c);								
				   
				}
				  TextColumn<Contact> prenameColumn = new TextColumn<Contact>() {

			          
			            public String getValue(Contact contact) {
			                return contact.getPrename();
			            }
			        };
			        overview.addColumn(prenameColumn, "Vorname");
			        
			        TextColumn<Contact> surnameColumn = new TextColumn<Contact>() {

			            public String getValue(Contact contact) {
			                return contact.getSurname();
			            }
			        };
			        overview.addColumn(surnameColumn, "Nachname");
			        overview.setVisibleRange(new Range(0, 25));
			        overview.getColumnSortList();
			        
					
					dataProvider.getList().clear();
					dataProvider.getList().addAll(allContacts);
					dataProvider.addDataDisplay(overview);
			    
					
			}
			
			
			  
		  });
		  RootPanel.get("content").add(overview);
		  RootPanel.get("content").add(pager);
	
	      
}
}