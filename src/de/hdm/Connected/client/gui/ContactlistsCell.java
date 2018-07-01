package de.hdm.Connected.client.gui;

import java.util.ArrayList;




import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.bo.ContactList;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;


class ContactListHeader{
	private final String name;
	private final ArrayList<ContactList> contactlists = new ArrayList<ContactList>();
	
	public ContactListHeader(String name){
		this.name = name;
	}
	
	 public String getName() {
         return name;
      }
	
    public ContactList addContactList(ContactList contactlist) {   
        contactlists.add(contactlist);
        return contactlist;
     }
	 
    public ArrayList<ContactList> getContactLists() {
       return contactlists;
    }
}

public class ContactlistsCell implements TreeViewModel {
	User user = new User();

		
	
	final ArrayList<ContactListHeader> contactslists;
	ArrayList<ContactList> allContactLists = new ArrayList<ContactList>();

	   /**
	    * This selection model is shared across all leaf nodes. 
	    * A selection model can also be shared across all nodes 
	    * in the tree, or each set of child nodes can have 
	    * its own instance. This gives you flexibility to 
	    * determine how nodes are selected.
	    */
	   final SingleSelectionModel<ContactList> selectionModel 
	   = new SingleSelectionModel<ContactList>();
	  
	
public ContactlistsCell(){
	
	contactslists = new ArrayList<ContactListHeader>();
	final ContactListHeader alleKontakte = new ContactListHeader("Meine Kontaktlisten");
	final ContactListHeader overview = new ContactListHeader("Meine Kontakte");
	contactslists.add(alleKontakte);
	contactslists.add(overview);
	
	 
	   selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() { 
			 
			 
			 public void onSelectionChange(SelectionChangeEvent event) { 
				 final ContactList selected = selectionModel.getSelectedObject(); 
			 
			 if (selected != null ) { 
				 RootPanel.get("content").clear();
						  ContactListForm3 showCL = new ContactListForm3(selected.getBoId());
			 			
			 }
			 }
			 });

	
<<<<<<< HEAD
		if(value == null) {
			return;
		} else if(value.getCreatorId() != user.getBoId()){
		sb.appendHtmlConstant("<i>");
		sb.appendEscaped(value.getName());
		sb.appendHtmlConstant("</i>");
				
		
		} else if(value.getCreatorId() == user.getBoId()){
			sb.appendHtmlConstant("<div>");
			sb.appendEscaped(value.getName());
			sb.appendHtmlConstant("</div>");
=======
	
ClientSideSettings.getConnectedAdmin().findAllContactlists(new AsyncCallback<ArrayList<ContactList>>(){

	@Override
	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(ArrayList<ContactList> result) {
		 for(ContactList cl: result){
		 alleKontakte.addContactList(cl);
		 }
>>>>>>> master
	}
	
});
	



}

public <T> NodeInfo<?> getNodeInfo(T value) {
    if (value == null) {
       // LEVEL 0.
       // We passed null as the root value. Return the composers.

       // Create a data provider that contains the list of composers.
       ListDataProvider<ContactListHeader> dataProvider 
       = new ListDataProvider<ContactListHeader>(contactslists);

       // Create a cell to display a composer.
       Cell<ContactListHeader> cell = new AbstractCell<ContactListHeader>() {
    	   @Override
          public void render( Context context, ContactListHeader key,
          SafeHtmlBuilder sb) {
             if (key != null) {
           	 sb.appendHtmlConstant("    "); 
                sb.appendEscaped(key.getName());
             }				
          }
       };

       // Return a node info that pairs the data provider and the cell.
       return new DefaultNodeInfo<ContactListHeader>(dataProvider, cell);
    } else if (value instanceof ContactListHeader) {
       
       // LEVEL 1.
       // We want the children of the composer. Return the playlists.
       ListDataProvider<ContactList> dataProvider
       = new ListDataProvider<ContactList>(
       ((ContactListHeader) value).getContactLists());
       Cell<ContactList> cell = 
       new AbstractCell<ContactList>() {
          @Override
          public void render(Context context, ContactList key, SafeHtmlBuilder sb) {
        	
        	  user.setBoId(2);
        		
      		if(key == null) {
      			return;
      		} else if(key.getCreatorId() != user.getBoId()){
      		sb.appendHtmlConstant("<i>");
      		sb.appendEscaped(key.getName());
      		sb.appendHtmlConstant("</i>");
      				
      		
      		} else if(key.getCreatorId() == user.getBoId()){
      			sb.appendHtmlConstant("<div>");
      			sb.appendEscaped(key.getName());
      			sb.appendHtmlConstant("</div>");
      	}
          }
       };
       return new DefaultNodeInfo<ContactList>(dataProvider, cell, selectionModel, null);
    } 

    return null;
 }

 /**
  * Check if the specified value represents a leaf node. 
  * Leaf nodes cannot be opened.
  */
 public boolean isLeaf(Object value) {
 
 // The leaf nodes are the songs, which are Strings.
 if (value instanceof ContactList) {
    return true;
 }
 
 return false;
 }


}