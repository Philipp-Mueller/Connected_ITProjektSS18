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
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.ContactList;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

public class StartPage extends Widget {

	 /**
	   * The UiBinder interface used by this example.
	   */
	 
	CellTable<Contact> overview = new CellTable<Contact>();
	private ListDataProvider<Contact> dataProvider = new ListDataProvider<Contact>();
	private ArrayList<Contact> allContacts = new ArrayList<Contact>();
	private Set<Contact> selectedContacts = new HashSet<Contact>();
	private ArrayList<Contact> selectedContactsArray = new ArrayList<Contact>();
	private ArrayList<User> allUsers = new ArrayList<User>();
	private ArrayList<User> selectedUser = new ArrayList<User>();
	private ArrayList<ContactList> allCLs = new ArrayList<ContactList>();
	private ArrayList<ContactList> selectedCLs = new ArrayList<ContactList>();	
	private Button selectedButton = new Button("Was ist den ausgewählt, hmm?!");
	private Button shareSelectedContacts = new Button("Ausgewählte Kontakte teilen");
	private Button addContactstoCL = new Button("Kontakte einer Kontaktliste hinzufügen");
	private final ListBox userListbox = new ListBox(true);
	private final ListBox contactlistListbox = new ListBox(true);
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
					
					shareSelectedContacts.addClickHandler(new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							
							for(Contact c : selectedContacts){
								selectedContactsArray.add(c);
							}
							
							MyDialog popup = new MyDialog();
							popup.center();
							popup.show();
						}
						
					});
					
					addContactstoCL.addClickHandler(new ClickHandler(){
						@Override
						public void onClick(ClickEvent event){
							
							for(Contact c : selectedContacts){
								selectedContactsArray.add(c);
							}
							
							MyDialog2 popup2 = new MyDialog2();
							popup2.center();
							popup2.show();
											
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
		  RootPanel.get("content").add(shareSelectedContacts);
		  RootPanel.get("content").add(addContactstoCL);
		  
		  
	      
}
	  
		private class MyDialog extends DialogBox {

		    public MyDialog() {
		      // Set the dialog box's caption.
		      setText("Mit wem möchtest Du die ausgewählten Kontakte teilen?");

		      // Enable animation.
		      setAnimationEnabled(true);

		      // Enable glass background.
		      setGlassEnabled(true);
		      Button zurück = new Button ("Zurück");
		      zurück.addClickHandler(new ClickHandler(){
		    	  public void onClick(ClickEvent event) {
		    		  selectedUser.clear();
		    		  selectedContactsArray.clear();
		    		  userListbox.clear();
		    		  MyDialog.this.hide();
		    	  }
		    	  });
		      
		      
		      Button ok = new Button("Teilen");
		      ok.addClickHandler(new ClickHandler() {
		        public void onClick(ClickEvent event) {
		          
		          
					for (int i = 0; i < userListbox.getItemCount(); i++) {
						if (userListbox.isItemSelected(i)) {
							selectedUser.add(allUsers.get(i));
						}
					}
					
		          if(selectedContactsArray.size() > 1){
					ClientSideSettings.getConnectedAdmin().giveContactPermissonToUsers(selectedContactsArray, selectedUser, 1, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Ops, da ist etwas schief gelaufen!");
						}

						@Override
						// jede Kontaktliste wird der ListBox hinzugefügt
						public void onSuccess(Void result) {
							Window.alert("Alle Kontakte erfolgreich geteilt!");
							Window.alert(Integer.toString(selectedContactsArray.size()));
							Window.alert(Integer.toString(selectedUser.size()));
							//Window.alert(Integer.toString(cArray.size()));
							//Window.alert(Integer.toString(uArray.size()));
							Window.Location.reload();
						}

					});
		          }
		          else{
		        	  //ContactSharing fiki = new ContactSharing(selectedContactsArray.get(1).getBoId(), selectedUser);
		          }
					
					MyDialog.this.hide();
		        }
		      });

		      VerticalPanel v = new VerticalPanel();
		      HorizontalPanel buttonPanel = new HorizontalPanel();
		      
				userListbox.ensureDebugId("cwListBox-multiBox");
				userListbox.setVisibleItemCount(7);
		      
				ClientSideSettings.getConnectedAdmin().findAllUser(new AsyncCallback<ArrayList<User>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Die User konnten nicht geladen werden");
					}

					@Override
					// jede Kontaktliste wird der ListBox hinzugefügt
					public void onSuccess(ArrayList<User> result) {
						allUsers = result;
						for (User u : result) {
							userListbox.addItem(u.getLogEmail());
						}

					}

				});
				

				
				buttonPanel.add(ok);
				buttonPanel.add(zurück);
		      v.add(userListbox);
		      v.add(buttonPanel);
		      setWidget(v);

		    }
		  }
		
		private class MyDialog2 extends DialogBox {

		    public MyDialog2() {
		      // Set the dialog box's caption.
		      setText("Welchen Listen möchtest Du die Kontakte hinzufügen?");

		      // Enable animation.
		      setAnimationEnabled(true);

		      // Enable glass background.
		      setGlassEnabled(true);
		      Button zurück = new Button ("Zurück");
		      zurück.addClickHandler(new ClickHandler(){
		    	  public void onClick(ClickEvent event) {
		    		  selectedCLs.clear();
		    		  selectedContactsArray.clear();
		    		  contactlistListbox.clear();
		    		  MyDialog2.this.hide();
		    	  }
		    	  });
		      
		      
		      Button ok = new Button("Hinzufügen");
		      ok.addClickHandler(new ClickHandler() {
		        public void onClick(ClickEvent event) {
		          
		          
					for (int i = 0; i < contactlistListbox.getItemCount(); i++) {
						if (contactlistListbox.isItemSelected(i)) {
							selectedCLs.add(allCLs.get(i));
						}
					}
					
		          if(selectedContactsArray.size() > 1){
					ClientSideSettings.getConnectedAdmin().addContactsToContactList(selectedContactsArray, selectedCLs,  new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Ops, da ist etwas schief gelaufen!");
						}

						@Override
						// jede Kontaktliste wird der ListBox hinzugefügt
						public void onSuccess(Void result) {
							Window.alert("Alle Kontakte erfolgreich geteilt!");
							Window.alert(Integer.toString(selectedContactsArray.size()));
							Window.alert(Integer.toString(selectedCLs.size()));
							//Window.alert(Integer.toString(cArray.size()));
							//Window.alert(Integer.toString(uArray.size()));
							Window.Location.reload();
						}

					});
		          }
		          else{
		        	  //ContactSharing fiki = new ContactSharing(selectedContactsArray.get(1).getBoId(), selectedUser);
		          }
					
					MyDialog2.this.hide();
		        }
		      });

		      VerticalPanel v = new VerticalPanel();
		      HorizontalPanel buttonPanel = new HorizontalPanel();
		      
		      contactlistListbox.ensureDebugId("cwListBox-multiBox");
		      contactlistListbox.setVisibleItemCount(7);
		      
				ClientSideSettings.getConnectedAdmin().findAllContactlists(new AsyncCallback<ArrayList<ContactList>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Die Cls konnten nicht geladen werden");
					}

					@Override
					// jede Kontaktliste wird der ListBox hinzugefügt
					public void onSuccess(ArrayList<ContactList> result) {
						allCLs = result;
						for (ContactList cl : result) {
							contactlistListbox.addItem(cl.getName());
						}

					}

				});
				

				
				buttonPanel.add(ok);
				buttonPanel.add(zurück);
		      v.add(userListbox);
		      v.add(buttonPanel);
		      setWidget(v);

		    }
		  }
}