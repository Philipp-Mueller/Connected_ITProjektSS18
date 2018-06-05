package de.hdm.Connected.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.client.Connected_ITProjektSS18;
import de.hdm.Connected.shared.ConnectedAdminAsync;
import de.hdm.Connected.shared.bo.BusinessObject;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.ContactList;
import de.hdm.Connected.shared.bo.User;

// toDo: ContactListForm durch die Ausgabe von Kontakten ersetzen
public class ContactListsTreeViewModel implements TreeViewModel{
	
	private ConnectedAdminAsync connectedAdmin = null;
	
	private static User currentUser = ClientSideSettings.getCurrentUser();
	
	private ListDataProvider <ContactList> contactListDataProvider = null;
	
	private ContactList selectedContactList = null;
	
	//Klasse fehlt
	//public ArrayList<Contact> allContactsOfTheContactList = null; 
	//private ContactListForm contactListForm = null;
	
	private class BusinessObjectKeyProvider implements ProvidesKey<BusinessObject> {
		@Override
			public Integer getKey(BusinessObject bo) {
			if (bo == null) {
				return null;
			}
			if (bo instanceof ContactList) {
				return new Integer(bo.getBoId());
			} else {
				return new Integer(-bo.getBoId());
			}
		}
    }
	
	private BusinessObjectKeyProvider boKeyProvider = null;
	
	private SingleSelectionModel<BusinessObject> selectionModel = null;
	
	private class SelectionChangeEventHandler implements SelectionChangeEvent.Handler {

		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			
			BusinessObject selection = selectionModel.getSelectedObject();
			
			if (selection instanceof ContactList) {
				//RootPanel.get("content").add(--- alle Kontakte der Liste((ContactList) selection));
		}
	}
}
	public ContactListsTreeViewModel() {
			this.connectedAdmin = ClientSideSettings.getConnectedAdmin();
			boKeyProvider = new BusinessObjectKeyProvider();
			selectionModel = new SingleSelectionModel<BusinessObject>(boKeyProvider);
			selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());
			//contactListDataProviders = new HashMap<ContactList, ListDataProvider<ContactList>>();
	}
	
	//public void contactListForm(ContactListForm contactListForm) {
	//this.contactListForm = contactListForm;
	//}

	public ContactList getSelectedContactList() {
		return selectedContactList;
	}
	
	public void setSelectedContactList(ContactList selectedContactList) {

		this.selectedContactList = selectedContactList;
		//this.contactListForm.setSelectedContactList(selectedContactList);
		
		if (selectedContactList != null) {
			
			//this.connectedAdmin.findContactListById(selectedContactList.findContactListById);
			//die Methode fehlt noch
			
			//public void onFailure (Throwable caught) {
				//ClientSideSettings.getLogger().severe("Fehler");
			//}

			//public void onSuccess (ContactList result) {
				//selectedContactList = result;
				//contactListForm.setSelectedContactList(result);
			}
		}
	//}
	
	
	public void addContactList(ContactList contactList) {
		this.contactListDataProvider.getList().add(contactList);
		this.selectionModel.setSelected(contactList, true);
		
	}
	
	public void updateContactList(ContactList contactList) {
		List<ContactList> contactListList = this.contactListDataProvider.getList();
		int i = 0;
		for (ContactList current : contactListList) {
			if (current.getBoId() == contactList.getBoId()) {
				contactListList.set(i, contactList);
				break;
			} else {
					i++;
			}
		}
		
		this.contactListDataProvider.refresh();
	}
	
	public void deleteContactList (ContactList contactList) {
		this.contactListDataProvider.getList().remove(contactList);
	}

	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		if (value == null) {
			
			this.contactListDataProvider = new ListDataProvider<ContactList>();
			
			this.connectedAdmin.findAllContactlists(new AsyncCallback<ArrayList<ContactList>>() {
			
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}

				public void onSuccess(ArrayList<ContactList> result) {
				
					for (ContactList contactList : result) {
						contactListDataProvider.getList().add(contactList);
					}
				}
			});
			
			return new DefaultNodeInfo<ContactList>(contactListDataProvider, new ContactListCell(), selectionModel, null);
		}
		
		return null;
	}
	

	@Override
	public boolean isLeaf(Object value) {
		
		return (value instanceof ContactList);
	}
}
