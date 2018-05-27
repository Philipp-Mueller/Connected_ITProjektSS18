package de.hdm.Connected.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.ReportGeneratorServiceAsync;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

public class ReportGeneratorBaseForm extends Widget{

	private ReportGeneratorServiceAsync rgsa = ClientSideSettings.getReportGenerator();
	
	
	private VerticalPanel vPanel = new VerticalPanel();
    private HorizontalPanel hPanel1 = new HorizontalPanel();
    private HorizontalPanel hPanel2 = new HorizontalPanel();
    private CheckBox allContactsCb = new CheckBox("All Contacts");
    private CheckBox sharedContactsCb = new CheckBox("Shared Contacts");
    private ListBox userListBox = new ListBox();
    private ListBox propertyListBox = new ListBox();
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox box = new SuggestBox(oracle);
	private CellTable<Contact> table = new CellTable<Contact>();

	
	private List<Contact> contactToShowInReport = new ArrayList<>();
	private boolean allContacts = false;
	private boolean sharedContacts = false;
	private Integer propertyId = null;
	private String  valueDescription = "";
	protected String userEmail ="";
	
	
	public ReportGeneratorBaseForm() {

	    // Zwei horizontal panel in einem V panel
	    vPanel.add(hPanel1);
	    vPanel.add(hPanel2);

	    //AllContacts checkbox
	    allContactsCb.setValue(allContacts);
	    allContactsCb.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  allContacts = ((CheckBox) event.getSource()).getValue();
	    	  if(!allContacts){
	    		  userListBox.setEnabled(true);
	    	  }else{
	    		  userListBox.setEnabled(false);
	    	  }
	      }
	    });
	    hPanel1.add(allContactsCb);
	    
	    //Userlistbox
	    userListBox.setVisibleItemCount(1);
	    hPanel1.add(userListBox);
	    
	    //SharedContact checkbox
	    sharedContactsCb.setValue(sharedContacts);
	    sharedContactsCb.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  sharedContacts = ((CheckBox) event.getSource()).getValue();
	    	  
	      }
	    });
	    hPanel1.add(sharedContactsCb);
	    
	    
	    //Property list box
	    propertyListBox.setVisibleItemCount(1);
	    propertyListBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				String idAsString =  ((ListBox) event.getSource()).getSelectedValue();
				propertyId = Integer.valueOf(idAsString);
				Window.alert(propertyId.toString());
			}
		});
	    hPanel1.add(propertyListBox);
	    

	    //ValueDescription suggestbox
	    box.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				valueDescription = ((SuggestBox) event.getSource()).getText();
			}
		});	
	    hPanel1.add(box);
	    
	    
	    //Suchen button
		 Button b = new Button("Suchen", new ClickHandler() {
		      public void onClick(ClickEvent event) {
		    	  	
		    	  	ReportGeneratorBaseForm.this.valueDescription = box.getText();
		    	  
		    	  
		    		  rgsa.searchContacts(allContacts, sharedContacts, userEmail, propertyId, valueDescription, 
		    		  new AsyncCallback<List<Contact>>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Fehler beim lesen aller Kontakte aufgetreten");
						}

						@Override
						public void onSuccess(List<Contact> result) {

							// Push the data into the widget.
							table.setRowData(0, result);
							
						}
					});
		    	  }
		    });
		 hPanel1.add(b);
	    
	
		 //Contact Tabelle
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		TextColumn<Contact> nameColumn = new TextColumn<Contact>() {
		@Override
			public String getValue(Contact object) {
				return object.getPrename();
			}
		};
		table.addColumn(nameColumn, "Vorname");
		
		TextColumn<Contact> surnameColumn = new TextColumn<Contact>() {
			@Override
			public String getValue(Contact object) {
				return object.getSurname();
			}
		};
		table.addColumn(surnameColumn, "Nachname");
		
		final SingleSelectionModel<Contact> selectionModel = new SingleSelectionModel<Contact>();
		table.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			 public void onSelectionChange(SelectionChangeEvent event) {
				 Contact selected = selectionModel.getSelectedObject();
				 if (selected != null) {
					 Window.alert("You selected: " + selected.getPrename() + " " + selected.getSurname());
				 }
			 }
		});
		table.setRowCount(contactToShowInReport.size(), true);
		table.setRowData(0, contactToShowInReport);
		hPanel2.add(table);
	    
		
		
		//Nachdem die UI erstellt ist daten für die Dropdowns und Suggestbox laden
	    loadDataForFiltering();
	    
	    //Vertical panel dem contect RootPanel hinzufügen (Somit wirds sichtbar)
	    RootPanel.get("content").add(vPanel);
		
	}


	private void loadDataForFiltering() {

		//Users für User Dropbox
		rgsa.allUsers(new AsyncCallback<List<User>>(){
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("all user geht ned!");
				
			}
			@Override
			public void onSuccess(List<User> result) {
				for(User u : result){
					userListBox.addItem(u.getLogEmail());
				}
				ReportGeneratorBaseForm.this.userEmail = userListBox.getSelectedValue(); 
			}
	    });
		
		//Properties für Property dropbox
	    rgsa.allProperties(new AsyncCallback<List<Property>>(){
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Property laden geht ned!");
				
			}
			@Override
			public void onSuccess(List<Property> result) {
				for(Property p : result){
					String propertyId = String.valueOf(p.getBoId());
					propertyListBox.addItem(p.getName(),propertyId);
				}
				ReportGeneratorBaseForm.this.propertyId = Integer.valueOf(propertyListBox.getSelectedValue());
			}
	    });
	    
	    //All Values for suggestbox
	    rgsa.allValues(new AsyncCallback<List<Value>>(){
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("all Value geht ned!");
				
			}
			@Override
			public void onSuccess(List<Value> result) {
				for(Value v : result){
					oracle.add(v.getName());
				}
			}
	    });
		
		
	}
	
	
	

}