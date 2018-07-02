package de.hdm.Connected.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
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
	
	private ConnectedAdminAsync connectedAdmin = ClientSideSettings.getConnectedAdmin();
	
	private ContactlistsCell clCell = new ContactlistsCell();
	
	ListDataProvider<ContactList> dataProvider = new ListDataProvider<ContactList>();
	
	List<ContactList> cll = new ArrayList<ContactList>();
	
    private CellList<ContactList> cellList;
   
    boolean buttonPressed;
    
	public ContactlistsCellList() {
		
		ProvidesKey<ContactList> keyProvider = new ProvidesKey<ContactList>() {
	        public Object getKey(ContactList cl) {
	           // Always do a null check.
	           return (cl == null) ? null : cl.getBoId();
	        }
	     };	
	     
    ContactlistsCell clCell = new ContactlistsCell();
	     
	//cellList = new CellList<ContactList>(clCell, keyProvider);
	
	ClientSideSettings.getConnectedAdmin().findAllContactlists(new AsyncCallback<ArrayList<ContactList>>() {
	     
	     @Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(ArrayList<ContactList> result) {
				cll = result;
			
			
	cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
	dataProvider.addDataDisplay(cellList);
	
	final SingleSelectionModel<ContactList> selectionModel = new SingleSelectionModel<ContactList>();
	
	cellList.setSelectionModel(selectionModel);
	
	selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	
		public void onSelectionChange(SelectionChangeEvent event) {
			
			final ContactList selected = selectionModel.getSelectedObject();
			if (selected != null && !buttonPressed) {
				RootPanel.get("content").clear();
				ContactListForm2 clForm = new ContactListForm2(selected.getBoId());
			}
			
			else if (buttonPressed) {
				buttonPressed = false;
			}
		}
		
	});
	
	cellList.setRowCount(cll.size(), true);
	cellList.setRowData(0, cll);
	
	cellList.setStylePrimaryName("cellList");
	
<<<<<<< HEAD
	VerticalPanel panel = new VerticalPanel();
	panel.setBorderWidth(1);	    
    panel.setWidth("200");
	panel.add(cellList);
	
	panel.setStylePrimaryName("cellListPanel");
	RootPanel.get().add(panel);
	
=======
	RootPanel.get("nav").add(cellList);
			
>>>>>>> master
			}
	 	});
	 }
}