package de.hdm.Connected.client.gui;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.ibm.icu.text.DateFormat;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.Value;
import de.hdm.Connected.shared.bo.User;

public class ContactInfoForm extends PopupPanel {
private static final String Date = null;
String email;

@SuppressWarnings("deprecation")
public ContactInfoForm(Contact contact, Map<Property, Value> map)  {
	//PopUp schließt automatisch wenn daneben geklickt wird
	super(true);
	//ensureDebugId("cwBasicPopup-simplePopup");
		   
	// Enable animation.
	setAnimationEnabled(true);

	// Enable glass background.
	setGlassEnabled(true);
	
	setWidth("300px");
	
	VerticalPanel d = new VerticalPanel();
	//DateBox dateBox = new DateBox();
	
	
	VerticalPanel v = new VerticalPanel();
	FlexTable contactInfoTable = new FlexTable();
	contactInfoTable.setCellSpacing(25);
	int creatorId =contact.getCreatorId();
	
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
	
	//TODO einheitliche Methode für richtige Zeitausgabe finden/implementieren evtl timestamp zu Date (Mappern & Impl) ändern
	
	Date stamp =contact.getModificationDate();
	Date mDate = new Date(stamp.getTime());
//	String moDate = contact.getModificationDate().toString();
	
	
	// java.sql.Date umgewandeltesDate = new java.sql.Date(new java.util.Date().getTime());

	
	contactInfoTable.setWidget(0, 0, new HTML("<strong>Ersteller: </strong>"));
	contactInfoTable.setWidget(0, 1, new HTML(email));
	contactInfoTable.setWidget(1, 0, new HTML("<strong>Erstellt am: </strong>"));
	contactInfoTable.setWidget(1, 1, new HTML(contact.getCreationDate().toString()));
	contactInfoTable.setWidget(2, 0, new HTML("<strong>Zuletzt geändert am: </strong>"));
	contactInfoTable.setWidget(2, 1, new HTML(mDate.toGMTString()));
	contactInfoTable.setWidget(3, 0, new HTML("<strong>Vorname: </strong>"));
	contactInfoTable.setWidget(3, 1, new HTML(contact.getPrename()));
	contactInfoTable.setWidget(4, 0, new HTML("<strong>Nachname: </strong>"));
	contactInfoTable.setWidget(4, 1, new HTML(contact.getSurname()));

	
	for(Map.Entry<Property, Value> entry : map.entrySet()){
		int rowCount = contactInfoTable.getRowCount();
		contactInfoTable.setWidget(rowCount, 0, new HTML("<strong>" + entry.getKey().getName() + ":</strong>"));
		contactInfoTable.setWidget(rowCount, 1, new HTML(entry.getValue().getName()));
	}
	
	v.add(new HTML("<h3> Kontakt: <i>" + contact.getPrename() + " " + contact.getSurname() +"</i></h3><br /><br />"));
	v.add(contactInfoTable);
	setWidget(v);
	
//	d.add(new HTML("<h4> Erstellungs- und Bearbeitungsdatum"));
//	d.add(dateBox);
//	setWidget(d);
	
}


}

