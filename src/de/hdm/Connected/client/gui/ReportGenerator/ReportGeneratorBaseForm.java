package de.hdm.Connected.client.gui.ReportGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.ReportGenerator.ReportGeneratorServiceAsync;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

public class ReportGeneratorBaseForm extends Widget {

	// Attributt ClientSiedeSetting
	private ReportGeneratorServiceAsync rgsa = ClientSideSettings.getReportGenerator();

	// Attribute Vertical Panel
	private VerticalPanel vPanel1 = new VerticalPanel();
	private VerticalPanel vPanel2 = new VerticalPanel();

	// Attribute Horizontal Panel
	private HorizontalPanel hPanel1 = new HorizontalPanel();
	private HorizontalPanel hPanel2 = new HorizontalPanel();
	private HorizontalPanel hPanel3 = new HorizontalPanel();
	private HorizontalPanel hPanel4 = new HorizontalPanel();

	// Attribute Checkboxen
	private CheckBox allContactsCb = new CheckBox(" Alle Kontakte anzeigen");
	private CheckBox sharedContactsCb = new CheckBox(" Alle getelten Kontakte anzeigen");

	// Attribute Listboxen
	private ListBox userListBox = new ListBox();
	private ListBox propertyListBox = new ListBox();
	private ListBox lb = new ListBox();

	// Attribute
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox box = new SuggestBox(oracle);
	private CellTable<Contact> table = new CellTable<Contact>();
	private ListDataProvider<Contact> dataProvider = new ListDataProvider<Contact>();

	// Attribute
	private List<Contact> contactToShowInReport = new ArrayList<>();
	private boolean allContacts = false;
	private boolean sharedContacts = false;
	private Integer propertyId = null;
	private String valueDescription = "";
	protected String userEmail = "";
	private Map<Integer, String> propertyValueMap = new HashMap<>();

	// Attribute Content
	private HTML property = new HTML(" Suchen Sie hier nach bestimmten Eigenschaften: ");
	private HTML user = new HTML(" Nutzer wählen: ");

	// Footer Attribute
	private HorizontalPanel footer = new HorizontalPanel();
	private Anchor connectedLink = new Anchor("Connected", "Connected_ITProjektSS18.html");
	private HTML copyrightText = new HTML(" | © 2018 Connected | ");
	private Anchor impressumLink = new Anchor("Impressum");

	public ReportGeneratorBaseForm() {

		// Horizontal Panels in Vertical Panels
		vPanel1.add(hPanel1);
		vPanel1.add(hPanel2);
		vPanel1.add(hPanel3);
		vPanel1.add(hPanel4);

		// AllContacts checkbox
		allContactsCb.getElement().getStyle().setMarginBottom(2, Unit.EM);
		allContactsCb.setValue(allContacts);
		allContactsCb.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				allContacts = ((CheckBox) event.getSource()).getValue();
				if (!allContacts) {
					userListBox.setEnabled(true);
				} else {
					userListBox.setEnabled(false);
				}
			}
		});
		hPanel1.add(allContactsCb);

		// SharedContact checkbox
		sharedContactsCb.getElement().getStyle().setMarginBottom(2, Unit.EM);
		sharedContactsCb.setValue(sharedContacts);
		sharedContactsCb.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				sharedContacts = ((CheckBox) event.getSource()).getValue();

			}
		});
		hPanel2.add(sharedContactsCb);

		// Abstand zur userListbox und Aufforderungstext zur User-Auswahl
		user.getElement().getStyle().setMarginRight(1, Unit.EM);
		user.getElement().getStyle().setMarginTop(1, Unit.EM);
		hPanel3.add(user);

		// Userlistbox
		userListBox.getElement().getStyle().setMarginBottom(5, Unit.EM);
		userListBox.getElement().getStyle().setMarginTop(1, Unit.EM);
		userListBox.setVisibleItemCount(1);
		userListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				userEmail = ((ListBox) event.getSource()).getSelectedItemText();
			}
		});
		hPanel3.add(userListBox);

		// Abstand und Aufforderungstext zur interaktion mit denn Eigenschaften
		property.getElement().getStyle().setMarginLeft(19, Unit.EM);
		property.getElement().getStyle().setMarginBottom(2, Unit.EM);

		hPanel1.add(property);

		// Abstand Property list box
		propertyListBox.getElement().getStyle().setMarginLeft(15, Unit.EM);

		propertyListBox.setVisibleItemCount(1);
		propertyListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String idAsString = ((ListBox) event.getSource()).getSelectedValue();
				propertyId = Integer.valueOf(idAsString);
				loadValuesForSuggestion();
			}
		});
		hPanel2.add(propertyListBox);

		// Abstand und ValueDescription suggestbox
		box.getElement().getStyle().setMarginLeft(1, Unit.EM);

		box.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				valueDescription = ((SuggestBox) event.getSource()).getText();
			}
		});
		hPanel2.add(box);

		Button newPropertySelection = new Button("+");
		newPropertySelection.getElement().getStyle().setWidth(2, Unit.EM);
		newPropertySelection.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String value = box.getText();
				String propertyText = propertyListBox.getSelectedItemText();

				/*
				 *  Werte aus der Listbox Anzeige in die HashMap für Übertragung an den Server merken
				 */
				propertyValueMap.put(propertyId, value);

				lb.addItem(propertyText + ": " + value, propertyId.toString());

			}
		});
		Button removePropertySelection = new Button("-");
		removePropertySelection.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Integer selektionsProperties = lb.getSelectedIndex();
				/*
				 *  Werte aus der Hash Map für die Server Übertragung wieder entfernen, da nicht mehr Relevant für die Selektion
				 */
				propertyValueMap.remove(Integer.valueOf(lb.getSelectedValue()));
				lb.removeItem(selektionsProperties);
			}
		});
		removePropertySelection.getElement().getStyle().setWidth(2, Unit.EM);
		removePropertySelection.getElement().getStyle().setMarginTop(1, Unit.EM);

		VerticalPanel vButtons = new VerticalPanel();
		vButtons.add(newPropertySelection);
		vButtons.add(removePropertySelection);
		vButtons.getElement().getStyle().setMarginLeft(1, Unit.EM);
		hPanel2.add(vButtons);

		lb.setVisibleItemCount(5);
		lb.getElement().getStyle().setMarginLeft(1, Unit.EM);
		lb.getElement().getStyle().setWidth(20, Unit.EM);
		hPanel2.add(lb);

		// Suchen button
		Button b = new Button("Suchen", new ClickHandler() {
			public void onClick(ClickEvent event) {

				ReportGeneratorBaseForm.this.valueDescription = box.getText();

					rgsa.searchContacts(allContacts, sharedContacts, userEmail, propertyValueMap,
							new AsyncCallback<List<Contact>>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Fehler beim lesen aller Kontakte aufgetreten");
								}

								@Override
								public void onSuccess(List<Contact> result) {

									// Daten in der Tabelle austauschen
									dataProvider.getList().clear();
									dataProvider.getList().addAll(result);
								}
							});
			}
		});
		// Abstand und Panel hinzufügen
		b.getElement().getStyle().setMarginLeft(35, Unit.EM);
		b.getElement().getStyle().setMarginTop(1, Unit.EM);
		b.getElement().getStyle().setMarginBottom(5, Unit.EM);
		hPanel3.add(b);

		// Contact Tabelle
		// Verbindet die Tabelle mit dem Data Provider.
		dataProvider.addDataDisplay(table);
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		TextColumn<Contact> nameColumn = new TextColumn<Contact>() {
			@Override
			public String getValue(Contact object) {
				return object.getPrename();
			}
		};
		table.addColumn(nameColumn, "Vorname");

		TextColumn<Contact> surnameColumn = new TextColumn<Contact>() {
			@Override
			public String getValue(Contact object) {
				return object.getSurname();
			}
		};
		table.addColumn(surnameColumn, "Nachname");

		final SingleSelectionModel<Contact> selectionModel = new SingleSelectionModel<Contact>();
		table.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				Contact selected = selectionModel.getSelectedObject();
				if (selected != null) {
					Window.alert("You selected: " + selected.getPrename() + " " + selected.getSurname());
				}
			}
		});
		table.setRowCount(contactToShowInReport.size(), true);
		table.setRowData(0, contactToShowInReport);
		hPanel4.add(table);

		/*
		 * Nachdem die UI erstellt ist werden die Daten für das Dropdown und die
		 * Suggestbox geladen
		 */
		loadDataForFiltering();

		/*
		 * Vertical panel wird dem RootPanel hinzugefügt (Somit wirds sichtbar)
		 */
		RootPanel.get("content").add(vPanel1);
		RootPanel.get("content").add(vPanel2);

		impressumLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("content").clear();
				RootPanel.get("content")
						.add(new HTML("<h2>Impressum nach §5 TMG</h2>" + "<h3>Verantwortlich</h3>"
								+ "<p>Hochschule der Medien<br />" + "Nobelstraße 8<br />" + "70569 Stuttgart<br /></p>"
								+ "<p><strong>Projektarbeit innerhalb des Studiengangs "
								+ "Wirtschaftsinformatik und digitale Medien, " + "IT-Projekt SS 18.</strong></p>"
								+ "<h3>Projektteam</h3>" + "<ul><li>xxx</li>" + "<li>xxx</li>" + "<li>xxx</li>"
								+ "<li>xxx</li>" + "<li>xxx</li>" + "<li>xxx</li></ul>" + "<h3>Kontakt</h3>"
								+ "<p><strong>Telefon:</strong> 0711 8923 10 (Zentrale)</p>"
								+ "<p><strong>Website:</strong> <a href='http://www.hdm-stuttgart.de' target='_blank'>"
								+ "www.hdm-stuttgart.de</a></p>"));

			}

		});
		footer.add(connectedLink);
		footer.add(copyrightText);
		footer.add(impressumLink);
		RootPanel.get("footer").add(footer);
	}

	private void loadDataForFiltering() {

		// Users für User Dropbox
		rgsa.allUsers(new AsyncCallback<List<User>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("all user geht ned!");

			}

			@Override
			public void onSuccess(List<User> result) {
				for (User u : result) {
					userListBox.addItem(u.getLogEmail());
				}
				ReportGeneratorBaseForm.this.userEmail = userListBox.getSelectedValue();
			}
		});

		// Properties für Property dropbox
		rgsa.allProperties(new AsyncCallback<List<Property>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Property laden geht ned!");

			}

			@Override
			public void onSuccess(List<Property> result) {
				for (Property p : result) {
					String propertyId = String.valueOf(p.getBoId());
					propertyListBox.addItem(p.getName(), propertyId);
				}
				ReportGeneratorBaseForm.this.propertyId = Integer.valueOf(propertyListBox.getSelectedValue());

				/*
				 * Die Properties sind geladen, jetzt werden die Values für die
				 * Suggestbox im Property-Dropdown geladen
				 */
				loadValuesForSuggestion();
			}
		});

	}

	private void loadValuesForSuggestion() {
		// Alle Values für suggestbox
		rgsa.allValues(ReportGeneratorBaseForm.this.propertyId, new AsyncCallback<List<Value>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("all Value geht ned!");

			}

			@Override
			public void onSuccess(List<Value> result) {
				oracle.clear();
				for (Value v : result) {
					oracle.add(v.getName());
				}
			}
		});

	}

}
