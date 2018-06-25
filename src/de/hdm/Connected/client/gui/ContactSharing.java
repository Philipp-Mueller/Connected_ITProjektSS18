package de.hdm.Connected.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SelectionChangeEvent.HasSelectionChangedHandlers;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

public class ContactSharing extends Widget {

	private ListDataProvider<Entry<Property, Value>> dataProvider = new ListDataProvider<Entry<Property, Value>>();
	private CellTable<Entry<Property, Value>> propertyValueTable = new CellTable<Entry<Property, Value>>();
	private Set<Entry<Property,Value>> selectedSet = new HashSet<Entry<Property,Value>>();
	ArrayList<User> allUsers = null;
	final ListBox userListBox = new ListBox(true);
	
	//Map<Property, Value> propertyValueMap = new HashMap<Property, Value>();
	
	
	public ContactSharing(final Contact sharingContact){
		
		RootPanel.get("content").add(new HTML("<h3> Kontakt <i>" + sharingContact.getPrename() + " " + sharingContact.getSurname() + "</i> teilen</h3><br />" ));
		
		RootPanel.get("content").add(new HTML("Bitte wählen Sie die Eigenschaften aus, die Sie teilen möchten:<br />" ));
		
		ClientSideSettings.getConnectedAdmin().findValueAndProperty(sharingContact.getBoId(), new AsyncCallback<Map<Property, Value>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Map<Property, Value> result) {
				//List für DataProvider mit den Properties und Values
				List<Entry<Property, Value>> propertiesAndValues = new ArrayList<Entry<Property, Value>>();		
				
				for(Map.Entry<Property, Value> entry : result.entrySet()){
					propertiesAndValues.add(entry);				
				}
							
				
				TextColumn<Entry<Property, Value>> propertyColumn = new TextColumn<Entry<Property, Value>>() {
					public String getValue(Entry<Property, Value> property) {
						return property.getKey().getName();
					}
				};
				TextColumn<Entry<Property, Value>> valueColumn = new TextColumn<Entry<Property, Value>>() {
					public String getValue(Entry<Property, Value> value) {
						return value.getValue().getName();
					}
				};
					
				final MultiSelectionModel<Entry<Property,Value>> selectionModel = new MultiSelectionModel<Entry<Property,Value>>();
				propertyValueTable.setSelectionModel(selectionModel,
						DefaultSelectionEventManager.<Entry<Property,Value>>createCheckboxManager());
				
				 selectionModel.addSelectionChangeHandler(new Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						selectedSet = selectionModel.getSelectedSet();

					}
				});

				Column<Entry<Property,Value>, Boolean> checkColumn = new Column<Entry<Property,Value>, Boolean>(new CheckboxCell(false, false)) {
					@Override
					public Boolean getValue(Entry<Property,Value> object) {
						// Get the value from the selection model.
						return selectionModel.isSelected(object);
					}
				};

				propertyValueTable.addColumn(checkColumn, "Eigenschaft teilen");
				propertyValueTable.setColumnWidth(checkColumn, 40, Unit.PX);				
				
				
				
				
					propertyValueTable.addColumn(propertyColumn, "Eigenschaft");
					propertyValueTable.addColumn(valueColumn, "Ausprägung");
							
					dataProvider.getList().clear();
					dataProvider.getList().addAll(propertiesAndValues);
					dataProvider.addDataDisplay(propertyValueTable);
					
					// multi auswahl freischalten in ListBox
					userListBox.ensureDebugId("cwListBox-multiBox");
					userListBox.setVisibleItemCount(7);
					
					ClientSideSettings.getConnectedAdmin().findAllUser(new AsyncCallback<ArrayList<User>>(){
						
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(ArrayList<User> result) {
							allUsers = result;
							for(User u : result){
								//Eigenen User nicht in Liste laden
								//TODO if(u.getLogEmail() != ClientSideSettings.getCurrentUser().getLogEmail()){
								userListBox.addItem(u.getLogEmail());//}
							}
						}
						
					});
					
					RootPanel.get("content").add(propertyValueTable);
					
					RootPanel.get("content").add(new HTML("<br />Bitte wählen Sie die/den User aus, mit dem Sie diesen Kontakt teilen möchten:<br />" ));
					
					RootPanel.get("content").add(userListBox);
					
					Button selected = new Button("Zeig was ist ausgwählt");
					selected.addClickHandler(new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							Window.alert(Integer.toString(selectedSet.size()));
							ArrayList<Integer> selectedValues = new ArrayList<Integer>();
							ArrayList<Integer> selectedUsers = new ArrayList<Integer>();
							selectedValues.add(sharingContact.getBoId());
							Iterator<Entry<Property, Value>> iterator = selectedSet.iterator();
							
							while(iterator.hasNext()){
								Map.Entry<Property,Value> entry = iterator.next();
								selectedValues.add(entry.getValue().getBoId());								
							}
							
							
							
							for(int i=0; i<userListBox.getItemCount();i++){
								if(userListBox.isItemSelected(i)){
									for(User u: allUsers){
										if(userListBox.getItemText(i).equals(u.getLogEmail())){
											selectedUsers.add(u.getBoId());
										}
									}
								}
							}
							
							
							
							ClientSideSettings.getConnectedAdmin().createPermission(3, selectedValues, selectedUsers, new AsyncCallback<Void>(){

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public void onSuccess(Void result) {
									// TODO Auto-generated method stub
									Window.alert("Values wurden geshared");
								}
								
							});
							
						}
						
					});
					RootPanel.get("content").add(selected);
				
			}
			
		});
	
}
}
