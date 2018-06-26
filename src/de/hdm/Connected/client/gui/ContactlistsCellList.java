package de.hdm.Connected.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.ConnectedAdminAsync;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.ContactList;

public class ContactlistsCellList extends Widget {
	
	private ContactList selectedContactlist;
	
	CellList<ContactList> cellList;
	
	public CellList<ContactList> createContactlistsCellList() {
		
	ProvidesKey<ContactList> keyProvider = new ProvidesKey<ContactList>() {
	        public Object getKey(ContactList cl) {
	           // Always do a null check.
	           return (cl == null) ? null : cl.getBoId();
	        }
	     };	
	     
    ContactlistsCell clCell = new ContactlistsCell();
	     
	cellList = new CellList<ContactList>(clCell, keyProvider);
	     
    cellList.setStyleName("cellList");
    
    cellList.setPageSize(30);
	cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
	cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);    
	     
	final NoSelectionModel<ContactList> clSelectionModel = new NoSelectionModel<ContactList>(keyProvider);
	
	cellList.setSelectionModel(clSelectionModel);
	
	clSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		
		public void onSelectionChange(SelectionChangeEvent event) {
			selectedContactlist = clSelectionModel.getLastSelectedObject();
		}

	});
 
	 return cellList;
	 
	}
	
	public ContactList getSelectedContactlist () {
		
		return selectedContactlist;
	}
	

}