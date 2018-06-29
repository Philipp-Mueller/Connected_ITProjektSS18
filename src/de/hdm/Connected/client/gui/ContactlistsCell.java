package de.hdm.Connected.client.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;


import de.hdm.Connected.shared.bo.ContactList;
import de.hdm.Connected.shared.bo.User;

public class ContactlistsCell extends AbstractCell <ContactList>{
	
	User user = new User();

	public void render(Context context, ContactList value, SafeHtmlBuilder sb) {
		user.setBoId(2);
	
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
