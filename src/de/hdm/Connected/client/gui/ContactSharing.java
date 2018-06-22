package de.hdm.Connected.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import de.hdm.Connected.shared.bo.Value;

public class ContactSharing extends Widget {

	private ListDataProvider<Entry<Property, Value>> dataProvider = new ListDataProvider<Entry<Property, Value>>();
	private CellTable<Entry<Property, Value>> propertyValueTable = new CellTable<Entry<Property, Value>>();
	private Set<Entry<Property,Value>> set1 = new HashSet<Entry<Property,Value>>();
	
	//Map<Property, Value> propertyValueMap = new HashMap<Property, Value>();
	
	
	public ContactSharing(Contact sharingContact){
		
		RootPanel.get("content").add(new HTML("<h3> Kontakt <i>" + sharingContact.getPrename() + " " + sharingContact.getSurname() + "</i> teilen</h3>" ));
		
		ClientSideSettings.getConnectedAdmin().findValueAndProperty(sharingContact.getBoId(), new AsyncCallback<Map<Property, Value>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Map<Property, Value> result) {
				// TODO Auto-generated method stub
				ArrayList<Object> propertyValueArray = new ArrayList<Object>();
				ArrayList<Property> propertyArray = new ArrayList<Property>();
				//ArrayList<Value> valueArray = new ArrayList<Value>();
				List<Entry<Property, Value>> listEntry = new ArrayList<Entry<Property, Value>>();				
				for(Map.Entry<Property, Value> entry : result.entrySet()){
					listEntry.add(entry);
					
					/*propertyArray.add(entry.getKey());
					propertyValueArray.add(entry.getKey());
					propertyValueArray.add(entry.getValue());*/
					
					Label propertyLabel = new Label(entry.getKey().getName());
					Label valueLabel = new Label (entry.getValue().getName());
					RootPanel.get("content").add(propertyLabel);
					RootPanel.get("content").add(valueLabel);
					
					// for (Contact cid : result) {
					// ClientSideSettings.getConnectedAdmin().findContactById(cid.getBoId(),
					// }
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
						set1 = selectionModel.getSelectedSet();

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
					dataProvider.getList().addAll(listEntry);
					dataProvider.addDataDisplay(propertyValueTable);
					Window.alert(Integer.toString(propertyValueArray.size()));
					
					RootPanel.get("content").add(propertyValueTable);
					Button selected = new Button("Zeig was ist ausgwählt");
					selected.addClickHandler(new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							Window.alert(Integer.toString(set1.size()));
						}
						
					});
					RootPanel.get("content").add(selected);
				
			}
			
		});
	
}
}
