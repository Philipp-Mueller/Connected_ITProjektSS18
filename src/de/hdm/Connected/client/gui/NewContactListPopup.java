package de.hdm.Connected.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.Connected.client.ClientSideSettings;

import de.hdm.Connected.shared.bo.ContactList;

public class NewContactListPopup extends DialogBox {
	
	public NewContactListPopup() {
		// Set the dialog box's caption.
		setText("Neue Kontaktliste erstellen:");

		// Enable animation.
		setAnimationEnabled(true);

		// Enable glass background.
		setGlassEnabled(true);

		VerticalPanel v = new VerticalPanel();

		Label nameLabel = new Label("Name: ");
		final TextBox nameTextBox = new TextBox();
		HorizontalPanel h = new HorizontalPanel();
		h.add(nameLabel);
		h.add(nameTextBox);
		v.add(h);

		Button close = new Button("Abbrechen");
		close.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				NewContactListPopup.this.hide();
			}

		});

		Button ok = new Button("Erstellen");
		ok.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ContactList cl = new ContactList();
				cl.setName(nameTextBox.getText());

				ClientSideSettings.getConnectedAdmin().createContactList(nameTextBox.getText(), ClientSideSettings.getCurrentUser().getId(),
						new AsyncCallback<ContactList>() {
							@Override
							public void onFailure(Throwable caught) {

							}

							@Override
							public void onSuccess(ContactList result) {
								Window.alert("Erfolgreich erstellt");
								RootPanel.get("content").clear();
//								Navigation reloadN = new Navigation();
								ContactListForm3 reload = new ContactListForm3(result);
								
							}
						});
				NewContactListPopup.this.hide();
			}
		});
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.add(ok);
		buttons.add(close);
		v.add(buttons);
		setWidget(v);
	}
	
}
