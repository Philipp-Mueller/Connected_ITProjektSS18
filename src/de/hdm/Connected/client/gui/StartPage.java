package de.hdm.Connected.client.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

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
	private Set<Contact> selectedContacts = new HashSet<Contact>();
	private Button selectedButton = new Button("Was ist den ausgewählt, hmm?!");
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
			       
			        
			        TextColumn<Contact> surnameColumn = new TextColumn<Contact>() {

			            public String getValue(Contact contact) {
			                return contact.getSurname();
			            }
			        };
			        
			      
			     
			    	final MultiSelectionModel<Contact> selectionModel = new MultiSelectionModel<Contact>();
					overview.setSelectionModel(selectionModel,
							DefaultSelectionEventManager.<Contact>createCheckboxManager());
					
					 selectionModel.addSelectionChangeHandler(new Handler() {
						@Override
						public void onSelectionChange(SelectionChangeEvent event) {
							selectedContacts = selectionModel.getSelectedSet();
							

						}
					});
					 
					 
					Column<Contact, Boolean> checkColumn = new Column<Contact, Boolean>(new CheckboxCell(false, false)) {
						@Override
						public Boolean getValue(Contact object) {
							// Get the value from the selection model.
							return selectionModel.isSelected(object);
						}
					};
					
					selectedButton.addClickHandler(new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							
							for(Contact c: allContacts){
								if(!selectionModel.isSelected(c)){
								selectionModel.setSelected(c, true);
								}else {selectionModel.setSelected(c,false);}
							}
							Window.alert(Integer.toString(selectedContacts.size()));
						}
						
					});
					
			        overview.addColumn(checkColumn);
			     
			        overview.addColumn(prenameColumn, "Vorname");
			        overview.addColumn(surnameColumn, "Nachname");
			        overview.setVisibleRange(new Range(0, 25));
					
					dataProvider.getList().clear();
					dataProvider.getList().addAll(allContacts);
					dataProvider.addDataDisplay(overview);
			    
					
			}
			
			
			  
		  });
		  RootPanel.get("content").add(overview);
		  RootPanel.get("content").add(pager);
		  RootPanel.get("content").add(selectedButton);
	      
}
}