package de.hdm.Connected.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.ConnectedAdminAsync;
import de.hdm.Connected.shared.bo.Contact;

public class ContactsTable extends CellTable {
	
	private ConnectedAdminAsync connectedAdmin = ClientSideSettings.getConnectedAdmin();
	
	//private HorizontalPanel hPanel = new HorizontalPanel();
	
	private CellTable<Contact> cellTable = new CellTable<Contact>();
	
	ListDataProvider<Contact> dataProvider = new ListDataProvider<Contact>();
	
	List<Contact> contacts = new ArrayList<Contact>();
	
	public ContactsTable() {
		
		dataProvider.addDataDisplay(cellTable);
		
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		
		TextColumn<Contact> prenameColumn = new TextColumn<Contact>() {

			@Override
			public String getValue(Contact object) {
					 return object.getPrename();
				}
			};
			
			prenameColumn.setSortable(true);
			cellTable.addColumn(prenameColumn, "Vorname");
			
		TextColumn<Contact> surnameColumn = new TextColumn<Contact>() {

				@Override
				public String getValue(Contact object) {
						 return object.getSurname();
					}
				};
				
			surnameColumn.setSortable(true);
			cellTable.addColumn(surnameColumn, "Nachname");	
			
		ButtonCell updateButton = new ButtonCell();
		
		Column<Contact,String> updateColumn = new Column<Contact,String> (updateButton) {
			  public String getValue(Contact object) {
			    return "bearbeiten";
			  }
			};
			
		updateColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {
			@Override
			public void update(int index, Contact object, String value) {
				// TODO Auto-generated method stub
				//Clickhandler
			}		
		});
		
		cellTable.addColumn(updateColumn);
		
		ButtonCell deleteButton = new ButtonCell();
		Column<Contact,String> deleteColumn = new Column<Contact,String> (updateButton) {
			  public String getValue(Contact object) {
			    return "löschen";
			  }
			};
			
		deleteColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {

			@Override
			public void update(int index, Contact object, String value) {
				// TODO Auto-generated method stub
				//Clickhandler
			}		
		});
		
		cellTable.addColumn(deleteColumn);
		
		final SingleSelectionModel<Contact> selectionModel = new SingleSelectionModel<Contact>();
		cellTable.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
				public void onSelectionChange(SelectionChangeEvent event) {
				final Contact selected = selectionModel.getSelectedObject();
				if (selected != null) {
				//Window.alert("You selected: " + selected.prename + " " + selected.surname);
					
				final Anchor selectedContact = new Anchor(selected.getPrename() + selected.getSurname());
				
				     
				HorizontalPanel contactPopupContainer = new HorizontalPanel();
				contactPopupContainer.setSpacing(10);
				final HTML contactInfo = new HTML();
				contactPopupContainer.add(contactInfo);
				final PopupPanel contactPopup = new PopupPanel(true, false);
				contactPopup.setWidget(contactPopupContainer);

				contactInfo.setHTML("Vorname: "+ selected.getPrename() + "<br>" + "Nachname: " + selected.getSurname() + "</i>");

			  //int left = selectedContact.getAbsoluteLeft() + 86;
			  //int top = selectedContact.getAbsoluteTop() + 45;
		      //contactPopup.setPopupPosition(left, top);
				contactPopup.show();
			} 
		} 
   }); 
		
		 	List<Contact> list = dataProvider.getList();
		    for (Contact Contact : contacts) {
		      list.add (Contact);
		    }
		
		    ListHandler<Contact> columnSortHandler = new ListHandler<Contact>(list);
		    
		    columnSortHandler.setComparator(prenameColumn,
		        new Comparator<Contact>() {
		          public int compare(Contact c1, Contact c2) {
		            if (c1 == c2) {
		              return 0;
		            }

		            if (c1 != null) {
		              return (c2 != null) ? c1.getPrename().compareTo(c2.getPrename()) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    cellTable.addColumnSortHandler(columnSortHandler);
		    
		    ListHandler<Contact> columnSortHandler2 = new ListHandler<Contact>(list);
		    
		    columnSortHandler2.setComparator(surnameColumn,
		        new Comparator<Contact>() {
		          public int compare(Contact c1, Contact c2) {
		            if (c1 == c2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (c1 != null) {
		              return (c2 != null) ? c1.getSurname().compareTo(c2.getSurname()) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    cellTable.addColumnSortHandler(columnSortHandler2);

		    cellTable.getColumnSortList().push(prenameColumn);

		cellTable.setRowCount(contacts.size(), true);
		cellTable.setRowData(0, contacts);
		cellTable.setWidth("70%");
		
		
		RootPanel.get("content").add(cellTable);

	}
}