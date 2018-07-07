package de.hdm.Connected.client.gui;

import java.util.ArrayList;
//import java.util.Date;
import java.util.Map;

import com.google.gwt.user.client.Window;
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
 * Diese "ContactInfo"- Klasse stellt View zur Verfügung, um Informationen über
 * Kontakte anzuzeigen.
 * 
 * @author Philipp, Denise
 *
 */

public class ContactInfoForm extends PopupPanel {

	private User creator = new User();
	private Contact contactShown = new Contact();
	private ArrayList<Value> allValues = new ArrayList<Value>();
	private FlexTable contactInfoTable = new FlexTable();

	/**
	 * Konstruktor des PopUpPanels
	 *
	 */

	public ContactInfoForm(Contact contact, ArrayList<Value> values) {
		// PopUp schließt automatisch wenn daneben geklickt wird

		super(true);

		contactShown = contact;
		allValues = values;

		// Enable animation.
		setAnimationEnabled(true);

		// Enable glass background.
		setGlassEnabled(true);

		setWidth("500px");

		/**
		 * Erzeugung eines Vertical Panels
		 */
		VerticalPanel v = new VerticalPanel();

		/**
		 * Erzeugung eines FlexTables und Zuweisung der Zellengröße
		 */

		contactInfoTable.setCellSpacing(22);

		/**
		 * Abfrage des Erstellers des anzuzeigenden Kontakts weist String email
		 * die Mailadresse des Erstellers zu
		 */

		if (contact.getCreatorId() != ClientSideSettings.getCurrentUser().getId()) {
			v.add(new HTML("<br /><h3> &nbsp; Kontakt: <i>" + contact.getPrename() + " " + contact.getSurname()
					+ "</i></h3><br />"));
		} else {
			v.add(new HTML(
					"<br /><h3> &nbsp; Kontakt: " + contact.getPrename() + " " + contact.getSurname() + "</h3><br />"));
		}

		v.add(new HTML("<hr>"));

		ClientSideSettings.getConnectedAdmin().findUserById(contact.getCreatorId(), new AsyncCallback<User>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Konnte User nicht finden!");
			}

			@Override
			public void onSuccess(User result) {
				creator = result;
				contactInfoTable.setWidget(0, 0, new HTML("<strong>Ersteller: </strong>"));
				contactInfoTable.setWidget(0, 1, new HTML(creator.getLogEmail()));
				contactInfoTable.setWidget(1, 0, new HTML("<strong>Erstellt: </strong>"));
				contactInfoTable.setWidget(1, 1, new HTML(contactShown.getCreationDate().toString()));
				contactInfoTable.setWidget(2, 0, new HTML("<strong>Zuletzt geändert: </strong>"));
				contactInfoTable.setWidget(2, 1, new HTML(contactShown.getModificationDate().toString()));
				contactInfoTable.setWidget(3, 0, new HTML("<strong>Vorname: </strong>"));
				contactInfoTable.setWidget(3, 1, new HTML(contactShown.getPrename()));
				contactInfoTable.setWidget(4, 0, new HTML("<strong>Nachname: </strong>"));
				contactInfoTable.setWidget(4, 1, new HTML(contactShown.getSurname()));
				center();

				/**
				 * Befüllt InfoTable mit Eigenschaften und Information des
				 * jeweiligen Kontaktobjekts
				 *
				 ** Erzeugt für jede Eigenschaft eine neue Zeile und befüllt
				 * diese mit Eigenschaften und Ausprägungen des jeweiligen
				 * Kontaktobjekts
				 *
				 */

				for (final Value value : allValues) {

					ClientSideSettings.getConnectedAdmin().findPropertyByPropertyId(value.getPropertyID(),
							new AsyncCallback<Property>() {

								@Override
								public void onFailure(Throwable caught) {

								}

								@Override
								public void onSuccess(Property result) {
									int rowCount = contactInfoTable.getRowCount();
									if(value.getCreatorId() != ClientSideSettings.getCurrentUser().getId()) {
									contactInfoTable.setWidget(rowCount, 0,
											new HTML("<p><i><strong>" + result.getName() + ":</strong></i></p>"));
									
									contactInfoTable.setWidget(rowCount, 1,
											new HTML("<i>" + value.getName() + "</i>"));
									} else {
										contactInfoTable.setWidget(rowCount, 0,
										new HTML("<p><strong>" + result.getName() + ":</strong></p>"));
										contactInfoTable.setWidget(rowCount, 1, new HTML(value.getName()));
									}
									center();
								}

							});

				}
			}

		});

		v.add(contactInfoTable);
		setWidget(v);

	}

}
