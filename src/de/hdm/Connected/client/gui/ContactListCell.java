package de.hdm.Connected.client.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.Connected.shared.bo.ContactList;


public class ContactListCell extends AbstractCell <ContactList>{

	@Override
	public void render (Context context, ContactList value, SafeHtmlBuilder sb) {
		
		
	if (value == null) {
		return;
	}
		
	sb.appendHtmlConstant("<div class=' cell ContactListCell'><p>");
	sb.appendEscaped(value.getName());
	sb.appendHtmlConstant("</p></div>");
		
	}
	
}
