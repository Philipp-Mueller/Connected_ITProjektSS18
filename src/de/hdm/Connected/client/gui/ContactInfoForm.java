package de.hdm.Connected.client.gui;

//import java.util.Date;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.Value;

import de.hdm.Connected.shared.bo.User;

/**
 * Diese "ContactInfo"- Klasse stellt View zur Verfügung,
 * um Informationen über Kontakte anzuzeigen.
 * 
 * @author Philipp, Denise
 *
 */



public class ContactInfoForm extends PopupPanel {

String email;

/**
 * Konstruktor des PopUpPanels
 *
 */

public ContactInfoForm(Contact contact, Map<Property, Value> map)  {
	//PopUp schließt automatisch wenn daneben geklickt wird
	super(true);
	//ensureDebugId("cwBasicPopup-simplePopup");
		   
	// Enable animation.
	setAnimationEnabled(true);

	// Enable glass background.
	setGlassEnabled(true);
	
	setWidth("300px");
	
	
	/**
	 * Erzeugung eines Vertical Panels
	 */	
	VerticalPanel v = new VerticalPanel();
	
	/**
	 * Erzeugung eines FlexTables
	 * und Zuweisung der Zellengröße
	 */
	FlexTable contactInfoTable = new FlexTable();
	contactInfoTable.setCellSpacing(22);
	
	/**
	 * Variable zur Abfrage des Erstellers
	 * Zuweisung der ErstellerId
	 */
	int creatorId =contact.getCreatorId();

	
	/**
	 * Abfrage des Erstellers des anzuzeigenden Kontakts
	 * weist String email die Mailadresse des Erstellers zu
	 */
	
	ClientSideSettings.getConnectedAdmin().findUserById(creatorId, new AsyncCallback<User>(){
		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
					}

		
		@Override
		public void onSuccess(User result) {
			email=result.getLogEmail();
			
		}
		
	});
	
	/**
	 *Befüllt InfoTable mit Eigenschaften und Information 
	 *des jeweiligen Kontaktobjekts
	 *
	 */		
			
	contactInfoTable.setWidget(0, 0, new HTML("<strong>Ersteller: </strong>"));
	contactInfoTable.setWidget(0, 1, new HTML(email));
	contactInfoTable.setWidget(1, 0, new HTML("<strong>Erstellt: </strong>"));
	contactInfoTable.setWidget(1, 1, new HTML(contact.getCreationDate().toString()));
	contactInfoTable.setWidget(2, 0, new HTML("<strong>Zuletzt geändert: </strong>"));
	contactInfoTable.setWidget(2, 1, new HTML(contact.getModificationDate().toString()));
	contactInfoTable.setWidget(3, 0, new HTML("<strong>Vorname: </strong>"));
	contactInfoTable.setWidget(3, 1, new HTML(contact.getPrename()));
	contactInfoTable.setWidget(4, 0, new HTML("<strong>Nachname: </strong>"));
	contactInfoTable.setWidget(4, 1, new HTML(contact.getSurname()));

	/**
	 *Erzeugt für jede Eigenschaft eine neue Zeile
	 *und befüllt diese mit Eigenschaften und Ausprägungen 
	 *des jeweiligen Kontaktobjekts 
	 *
	 */	
	
	for(Map.Entry<Property, Value> entry : map.entrySet()){
		int rowCount = contactInfoTable.getRowCount();
		contactInfoTable.setWidget(rowCount, 0, new HTML("<strong>" + entry.getKey().getName() + ":</strong>"));
		contactInfoTable.setWidget(rowCount, 1, new HTML(entry.getValue().getName()));
	}
	
	/**
	 *Zuweisung des Flextables zum Panel
	 *zur Anzeige des Popus
	 *
	 */	
	
	v.add(new HTML("<h3> Kontakt: <i>" + contact.getPrename() + " " + contact.getSurname() +"</i></h3><br /><br />"));
	v.add(contactInfoTable);
	setWidget(v);
	
}


}

