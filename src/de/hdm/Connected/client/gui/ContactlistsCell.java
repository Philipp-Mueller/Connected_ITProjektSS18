package de.hdm.Connected.client.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.Connected.shared.bo.ContactList;

public class ContactlistsCell extends AbstractCell <ContactList>{

	public void render (Context context, ContactList value, SafeHtmlBuilder sb) {
        if (value != null) {
            sb.appendEscaped(value.getName());
        }
	}
}
