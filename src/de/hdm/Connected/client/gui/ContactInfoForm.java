package de.hdm.Connected.client.gui;

import java.util.Map;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.Value;

public class ContactInfoForm extends PopupPanel {
	
public ContactInfoForm(Contact contact, Map<Property, Value> map)  {
	//PopUp schließt automatisch wenn daneben geklickt wird
	super(true);
	//ensureDebugId("cwBasicPopup-simplePopup");
		   
	// Enable animation.
	setAnimationEnabled(true);

	// Enable glass background.
	setGlassEnabled(true);
	
	setWidth("300px");

	VerticalPanel v = new VerticalPanel();
	FlexTable contactInfoTable = new FlexTable();
	contactInfoTable.setCellSpacing(20);
	

	contactInfoTable.setWidget(0, 0, new HTML("<strong>Vorname: </strong>"));
	contactInfoTable.setWidget(0, 1, new HTML(contact.getPrename()));
	contactInfoTable.setWidget(1, 0, new HTML("<strong>Nachname: </strong>"));
	contactInfoTable.setWidget(1, 1, new HTML(contact.getSurname()));
	
	for(Map.Entry<Property, Value> entry : map.entrySet()){
		int rowCount = contactInfoTable.getRowCount();
		contactInfoTable.setWidget(rowCount, 0, new HTML("<strong>" + entry.getKey().getName() + ":</strong>"));
		contactInfoTable.setWidget(rowCount, 1, new HTML(entry.getValue().getName()));
	}
	
	v.add(new HTML("<h3> Kontakt: <i>" + contact.getPrename() + " " + contact.getSurname() +"</i></h3><br /><br />"));
	v.add(contactInfoTable);
	setWidget(v);
	
}


}

