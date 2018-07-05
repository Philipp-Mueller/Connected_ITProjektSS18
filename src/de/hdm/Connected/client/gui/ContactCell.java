package de.hdm.Connected.client.gui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.User;

public class ContactCell extends AbstractCell<Contact>{

	
	
	
private User user = new User();
	
	/*
	 * Ist der angemeldete Nutzer Teilhaber einer Kontaktliste, wird diese kursiv dargestellt.
	 * @see com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
	 */
	@Override
	public void render(Context context, Contact value, SafeHtmlBuilder sb) {
		user.setId(2);
	
		if(value == null) {
			return;
		}else if(value.getCreatorId() != user.getId()){
		sb.appendHtmlConstant("<i>");
		sb.appendEscaped(value.getPrename());
		sb.appendHtmlConstant("</i>");
				
		
		}else if(value.getCreatorId() == user.getId()){
			sb.appendHtmlConstant("<div>");
			sb.appendEscaped(value.getPrename());
			sb.appendHtmlConstant("</div>");
				}
	
}
}
