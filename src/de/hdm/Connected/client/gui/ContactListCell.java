package de.hdm.Connected.client.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.ContactList;
import de.hdm.Connected.shared.bo.User;

/**
 * Klasse um die Contactlisten kursiv darzustellen,wenn sie geshared worden sind
 * @author Philipp
 *
 */
public class ContactListCell extends AbstractCell<ContactList>{

	
	
	
private User user = ClientSideSettings.getCurrentUser();
	
	/*
	 * Ist der angemeldete Nutzer Teilhaber einer Kontaktliste, wird diese kursiv dargestellt.
	 * @see com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
	 */
	@Override
	public void render(Context context, ContactList value, SafeHtmlBuilder sb) {
	
	
		if(value == null) {
			return;
		}else if(value.getCreatorId() != user.getBoId()){
		sb.appendHtmlConstant("<i>");
		sb.appendEscaped(value.getName());
		sb.appendHtmlConstant("</i>");
				
		
		}else if(value.getCreatorId() == user.getBoId()){
			sb.appendHtmlConstant("<div>");
			sb.appendEscaped(value.getName());
			sb.appendHtmlConstant("</div>");
				}
	
}
}
