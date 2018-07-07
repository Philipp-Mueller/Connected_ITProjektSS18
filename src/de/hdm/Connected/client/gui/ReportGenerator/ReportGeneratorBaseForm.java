package de.hdm.Connected.client.gui.ReportGenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.ReportGenerator.ReportGeneratorServiceAsync;
import de.hdm.Connected.shared.ReportGenerator.ReportObjekt;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

public class ReportGeneratorBaseForm extends Widget {

	private static final String RadioButtonGruppe = null;

	// Attributt ClientSiedeSetting
	private ReportGeneratorServiceAsync rgsa = ClientSideSettings.getReportGenerator();

	// Attribute Vertical Panel
	private VerticalPanel filterVpanelLinks = new VerticalPanel();
	private VerticalPanel datenHpanelRechts = new VerticalPanel();
	private VerticalPanel vPanel2 = new VerticalPanel();

	// Attribute Horizontal Panel
	private HorizontalPanel hPanelFilter = new HorizontalPanel();
	private HorizontalPanel hPanelRbAllContacts = new HorizontalPanel();
	private HorizontalPanel hPanelRbSharedContacts = new HorizontalPanel();
	private HorizontalPanel hPanelRbDetailSearch = new HorizontalPanel();
	private HorizontalPanel hPanelValueButton = new HorizontalPanel();
	private HorizontalPanel hPanelValueChosenButton = new HorizontalPanel();

	// Attribute RadioButton
	private RadioButton allContactsRb = new RadioButton(" Alle meine Kontakte");
	private RadioButton sharedContactsRb = new RadioButton(" Alle meinte geteilten Kontakte");
	private RadioButton propertySearchRb = new RadioButton(" Eigenschaftssuche");

	// Attribute Listboxen
	private ListBox userListBox = new ListBox();
	private ListBox propertyListBox = new ListBox();
	private ListBox valueListBox = new ListBox();

	// Attribute
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox valueBox = new SuggestBox(oracle);
	private CellTable<ReportObjekt> table = new CellTable<ReportObjekt>();
	private ListDataProvider<ReportObjekt> dataProvider = new ListDataProvider<ReportObjekt>();
	private List<ReportObjekt> contactToShowInReport = new ArrayList<ReportObjekt>();
	private boolean allContacts = false;
	private boolean sharedContacts = false;
	private boolean detailSearch = false;
	private Integer propertyId = null;
	private String valueDescription = "";
	protected String userEmail = "";
	private Map<Integer, String> propertyValueMap = new HashMap<>();
	private Map<Integer, String> propertyIdUndName = new HashMap<Integer, String>();
	private SimplePager pager;

	// HTML Attribute
	private HTML labelFilter = new HTML("<strong>Filter wählen</strong>");
	private HTML labelProperty = new HTML("Eigenschaft wählen:");
	private HTML labelValue = new HTML("Wert eingeben:");
	private HTML labelPropertyValues = new HTML("Gewählte Eigenschaftswerte:");
	private HTML labelUser = new HTML("Nutzer wählen:");
	private HTML labelAllContactsText = new HTML(" Alle meine Kontakte");
	private HTML labelSharedContactsText = new HTML(" Alle meine geteilten Kontakte");
	private HTML labelPropertySearchText = new HTML(" Eigenschaftssuche");
	private HTML trennStrich1 = new HTML("<hr>");
	private HTML trennStrich2 = new HTML("<hr>");
	private HTML trennStrich3 = new HTML("<hr>");
	private HTML trennStrich4 = new HTML("<hr>");

	public ReportGeneratorBaseForm(final User currentUser) {

		// Panels
		hPanelFilter.add(filterVpanelLinks);
		hPanelFilter.add(datenHpanelRechts);
		filterVpanelLinks.add(trennStrich1);
		filterVpanelLinks.add(trennStrich2);
		filterVpanelLinks.add(trennStrich3);
		filterVpanelLinks.add(trennStrich4);

		// Überschrift filterVpanelLinks
		labelFilter.getElement().getStyle().setMarginBottom(1, Unit.EM);
		filterVpanelLinks.add(labelFilter);
		filterVpanelLinks.add(trennStrich1);
		trennStrich1.getElement().getStyle().setMarginBottom(2, Unit.EM);
		trennStrich1.getElement().getStyle().setWidth(20, Unit.EM);

		// AllContacts RadioButton
		allContactsRb.getElement().getStyle().setMarginBottom(2, Unit.EM);
		allContactsRb.setValue(allContacts);
		allContactsRb.setName(RadioButtonGruppe);
		allContactsRb.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				allContacts = ((RadioButton) event.getSource()).getValue();
				sharedContacts = false;

				// Nutzer bei Allen Kontakten verbergen
				if (!allContacts) {
					allContactsRb.setEnabled(true);
				} else {
					userListBox.setEnabled(false);
					propertyListBox.setEnabled(false);
					valueBox.setEnabled(false);
					valueListBox.setEnabled(false);
				}

			}
		});

		hPanelRbAllContacts.add(allContactsRb);
		hPanelRbAllContacts.add(labelAllContactsText);
		filterVpanelLinks.add(hPanelRbAllContacts);
		filterVpanelLinks.add(trennStrich2);
		trennStrich2.getElement().getStyle().setMarginTop(2, Unit.EM);
		trennStrich2.getElement().getStyle().setMarginBottom(2, Unit.EM);
		trennStrich2.getElement().getStyle().setWidth(20, Unit.EM);

		// SharedContact RadioButton
		sharedContactsRb.getElement().getStyle().setMarginTop(2, Unit.EM);
		sharedContactsRb.getElement().getStyle().setMarginBottom(2, Unit.EM);
		sharedContactsRb.setValue(sharedContacts);
		sharedContactsRb.setName(RadioButtonGruppe);
		sharedContactsRb.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				sharedContacts = ((RadioButton) event.getSource()).getValue();
				allContacts = false;

				// Nutzer bei Allen geteilten Kontakten anzeigen
				if (!sharedContacts) {
					sharedContactsRb.setEnabled(false);
				} else {
					userListBox.setEnabled(true);
					propertyListBox.setEnabled(false);
					valueBox.setEnabled(false);
					valueListBox.setEnabled(false);
				}
			}
		});
		hPanelRbSharedContacts.add(sharedContactsRb);
		hPanelRbSharedContacts.add(labelSharedContactsText);
		filterVpanelLinks.add(hPanelRbSharedContacts);

		// Abstand und Userlabel
		labelUser.getElement().getStyle().setMarginRight(2, Unit.EM);
		labelUser.getElement().getStyle().setMarginTop(2, Unit.EM);
		filterVpanelLinks.add(labelUser);

		// Userlistbox
		userListBox.setVisibleItemCount(1);
		userListBox.getElement().getStyle().setMarginLeft(1, Unit.EM);
		userListBox.getElement().getStyle().setWidth(13, Unit.EM);
		userListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				userEmail = ((ListBox) event.getSource()).getSelectedItemText();
			}
		});
		filterVpanelLinks.add(userListBox);
		filterVpanelLinks.add(trennStrich3);
		trennStrich3.getElement().getStyle().setMarginTop(2, Unit.EM);
		trennStrich3.getElement().getStyle().setMarginBottom(2, Unit.EM);
		trennStrich3.getElement().getStyle().setWidth(20, Unit.EM);

		// Eigenschaftssuche RadioButton
		propertySearchRb.getElement().getStyle().setMarginTop(2, Unit.EM);
		propertySearchRb.getElement().getStyle().setMarginBottom(2, Unit.EM);
		propertySearchRb.setValue(detailSearch);
		propertySearchRb.setName(RadioButtonGruppe);
		propertySearchRb.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				detailSearch = ((RadioButton) event.getSource()).getValue();
				allContacts = false;
				sharedContacts = false;

				// Detailsuche anzeigen wenn weder alle Kontakte noch geteilte
				// Kontakte ausgewählt sind
				if (!detailSearch) {
					propertySearchRb.setEnabled(true);
				} else {
					userListBox.setEnabled(false);
					propertyListBox.setEnabled(true);
					valueBox.setEnabled(true);
					valueListBox.setEnabled(true);

				}
			}
		});
		hPanelRbDetailSearch.add(propertySearchRb);
		hPanelRbDetailSearch.add(labelPropertySearchText);
		filterVpanelLinks.add(hPanelRbDetailSearch);

		// Default page view
		allContactsRb.setChecked(true);
		userListBox.setEnabled(false);
		propertyListBox.setEnabled(false);
		valueBox.setEnabled(false);
		valueListBox.setEnabled(false);
		detailSearch = false;
		allContacts = true;
		sharedContacts = false;

		// Abstand und Eigenschaftslabel
		labelProperty.getElement().getStyle().setMarginTop(2, Unit.EM);
		filterVpanelLinks.add(labelProperty);

		// Eigenschaftslistbox
		propertyListBox.setVisibleItemCount(1);
		propertyListBox.getElement().getStyle().setMarginLeft(1, Unit.EM);
		propertyListBox.getElement().getStyle().setWidth(13, Unit.EM);
		propertyListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String idAsString = ((ListBox) event.getSource()).getSelectedValue();
				propertyId = Integer.valueOf(idAsString);
				loadValuesForSuggestion();
			}
		});
		filterVpanelLinks.add(propertyListBox);

		// Abstand und ValueDescription suggestbox
		labelValue.getElement().getStyle().setMarginTop(2, Unit.EM);
		valueBox.getElement().getStyle().setWidth(13, Unit.EM);
		valueBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				valueDescription = ((SuggestBox) event.getSource()).getText();
			}
		});

		// Button zum Hinzufügen von Eigenschaften
		Button newPropertySelection = new Button("+");
		newPropertySelection.getElement().getStyle().setWidth(2, Unit.EM);
		newPropertySelection.getElement().getStyle().setMarginLeft(1, Unit.EM);
		newPropertySelection.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String value = valueBox.getText();
				String propertyText = propertyListBox.getSelectedItemText();

				// Werte aus der Listbox Anzeige in die HashMap für Übertragung
				// an den Server merken
				propertyValueMap.put(propertyId, value);
				valueListBox.addItem(propertyText + ": " + value, propertyId.toString());
			}
		});
		filterVpanelLinks.add(labelValue);
		hPanelValueButton.add(valueBox);
		hPanelValueButton.add(newPropertySelection);
		filterVpanelLinks.add(hPanelValueButton);

		// Button zum entfernen ausgewählter Eigenschaften
		Button removePropertySelection = new Button("-");
		removePropertySelection.getElement().getStyle().setWidth(2, Unit.EM);
		removePropertySelection.getElement().getStyle().setMarginLeft(1, Unit.EM);
		removePropertySelection.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Integer selektionsProperties = valueListBox.getSelectedIndex();

				// Werte aus der Hash Map für die Server Übertragung wieder
				// entfernen, da nicht mehr Relevant für die Selektion
				propertyValueMap.remove(Integer.valueOf(valueListBox.getSelectedValue()));
				valueListBox.removeItem(selektionsProperties);
			}
		});
		removePropertySelection.getElement().getStyle().setWidth(2, Unit.EM);
		removePropertySelection.getElement().getStyle().setMarginTop(2, Unit.EM);

		// Ansicht der Listbox auf 3 Eigenschaften eingrenzen darüber erscheint
		// Scrollbalken
		labelPropertyValues.getElement().getStyle().setMarginTop(2, Unit.EM);
		valueListBox.setVisibleItemCount(3);
		valueListBox.getElement().getStyle().setWidth(14, Unit.EM);
		filterVpanelLinks.add(labelPropertyValues);
		hPanelValueChosenButton.add(valueListBox);
		hPanelValueChosenButton.add(removePropertySelection);
		filterVpanelLinks.add(hPanelValueChosenButton);
		filterVpanelLinks.add(trennStrich4);
		trennStrich4.getElement().getStyle().setMarginTop(2, Unit.EM);
		trennStrich4.getElement().getStyle().setMarginBottom(2, Unit.EM);
		trennStrich4.getElement().getStyle().setWidth(20, Unit.EM);

		// Filter Anwenden button
		Button filterAnwenden = new Button("Filter anwenden", new ClickHandler() {
			public void onClick(ClickEvent event) {

				ReportGeneratorBaseForm.this.valueDescription = valueBox.getText();

				String mailToServer = "";
				Map<Integer, String> propertyValueMapToServer = new HashMap<>();
				if (!userEmail.equals("Ohne Nutzer") && userListBox.isEnabled()) {
					mailToServer = userEmail;
				}
				if (propertyListBox.isEnabled() && propertySearchRb.isChecked()) {
					if(propertyValueMap.size()==0){
						Window.alert("Bitte geben Sie eine Ausprägung zur Eigenschaft an.");
					}
					propertyValueMapToServer = propertyValueMap;
				}
				

				rgsa.searchContacts(allContacts, sharedContacts, detailSearch, mailToServer, propertyValueMapToServer,
						currentUser.getBoId(), new AsyncCallback<List<ReportObjekt>>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Fehler beim lesen aller Kontakte aufgetreten");
							}

							@Override
							public void onSuccess(List<ReportObjekt> result) {

								dataProvider.getList().clear();
								for (ReportObjekt r : result) {
									dataProvider.getList().add(r);
								}
							}
						});
			}
		});
		// Abstand und Panel hinzufügen

		// Filter löschen Button
		Button filterLoeschen = new Button("Filter löschen", new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Seitenansicht mit Default werten
				allContactsRb.setChecked(true);
				userListBox.setEnabled(false);
				userListBox.setSelectedIndex(0);
				userEmail = "Ohne Nutzer";
				propertyListBox.setEnabled(false);
				valueBox.setEnabled(false);
				valueBox.setValue("");
				valueListBox.setEnabled(false);
				valueListBox.setSelectedIndex(0);
				propertyValueMap = new HashMap<>();
				valueListBox.clear();
				detailSearch = false;
				allContacts = true;
				sharedContacts = false;

			}
		});
		filterLoeschen.getElement().getStyle().setMarginRight(1, Unit.EM);
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(filterLoeschen);
		buttonPanel.add(filterAnwenden);
		filterVpanelLinks.add(buttonPanel);

		// Contact Tabelle verbindet die Tabelle mit dem Data Provider.
		// Add the data to the data provider, which automatically pushes it to
		// the widget.
		dataProvider.addDataDisplay(table);
		table.setRowCount(contactToShowInReport.size(), true);
		table.setRowData(0, contactToShowInReport);
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		// Spaltenbezeichnung "Vorname"
		TextColumn<ReportObjekt> nameColumn = new TextColumn<ReportObjekt>() {
			@Override
			public String getValue(ReportObjekt object) {
				return object.getVorname();
			}
		};
		nameColumn.setSortable(true);
		table.addColumn(nameColumn, "Vorname");

		// Spaltenbezeichnung "Nachname"
		TextColumn<ReportObjekt> surnameColumn = new TextColumn<ReportObjekt>() {
			@Override
			public String getValue(ReportObjekt object) {
				return object.getNachname();
			}
		};
		surnameColumn.setSortable(true);
		table.addColumn(surnameColumn, "Nachname");

		ListHandler<ReportObjekt> columnSortHandler = new ListHandler<ReportObjekt>(dataProvider.getList());
		// Filter in der Tabelle Vorname
		columnSortHandler.setComparator(nameColumn, new Comparator<ReportObjekt>() {
			public int compare(ReportObjekt c1, ReportObjekt c2) {
				if (c1 == c2) {
					return 0;
				}
				// Vergleicht den Vornamen in den Spalten.
				if (c1 != null) {
					return (c2 != null) ? c1.getVorname().compareTo(c2.getVorname()) : 1;
				}
				return -1;
			}
		});
		table.addColumnSortHandler(columnSortHandler);

		ListHandler<ReportObjekt> columnSortHandler2 = new ListHandler<ReportObjekt>(dataProvider.getList());
		// Filter in der Tabelle Nachname
		columnSortHandler2.setComparator(surnameColumn, new Comparator<ReportObjekt>() {
			public int compare(ReportObjekt c1, ReportObjekt c2) {
				if (c1 == c2) {
					return 0;
				}

				// Vergleicht den Nachnamen in den Spalten.
				if (c1 != null) {
					return (c2 != null) ? c1.getNachname().compareTo(c2.getNachname()) : 1;
				}
				return -1;
			}
		});
		table.addColumnSortHandler(columnSortHandler2);
		table.getColumnSortList().push(nameColumn);
		table.getElement().getStyle().setMarginLeft(10, Unit.EM);
		datenHpanelRechts.add(table);

		// Nachdem die UI erstellt ist werden die Daten für das Dropdown und die
		// Suggestbox geladen
		loadDataForFiltering();

		// Vertical panel wird dem RootPanel hinzugefügt (Somit wirds sichtbar)
		RootPanel.get("content").add(hPanelFilter);

		// Paginierung der Tabelle
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(table);

		HorizontalPanel hp = new HorizontalPanel();
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.add(pager);

		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		hp.getElement().getStyle().setMarginLeft(10, Unit.EM);
		datenHpanelRechts.add(hp);

	}

	/**
	 * Private Hilfsmethode die alle Daten für die Filterung liest. Zuerst
	 * werden alle User geladen anschließend alle Eigenschaften und Ihre
	 * ausprägungen dazu. Wenn alle Daten bekannt sind, wird für jede
	 * Eingenschaft dynamisch eine Spalte in der Ergebnistabelle inkl.
	 * Sortierfunktion angelegt.
	 * 
	 */
	private void loadDataForFiltering() {
		// Users für User Dropbox
		rgsa.allUsers(new AsyncCallback<List<User>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Das Laden der Benutzer ist fehlgeschlagen!");
			}

			//
			@Override
			public void onSuccess(List<User> result) {
				userListBox.addItem("Ohne Nutzer");
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
				Window.alert("Das Laden der Eigenschaften ist fehlgeschlagen!");
			}

			//
			@Override
			public void onSuccess(List<Property> result) {
				for (Property p : result) {
					String propertyId = String.valueOf(p.getBoId());
					propertyListBox.addItem(p.getName(), propertyId);
					propertyIdUndName.put(p.getBoId(), p.getName());
				}
				ReportGeneratorBaseForm.this.propertyId = Integer.valueOf(propertyListBox.getSelectedValue());

				// Die Properties sind geladen, jetzt werden die Values für die
				// Suggestbox im Property-Dropdown geladen
				loadValuesForSuggestion();
				eigenschaftSpaltenFuerTabelle();
			}
		});

	}

	/**
	 * Private Hilfsmethode die für eine Ausgewählte eingenschaft alle bereits
	 * vorhandenen Ausprägungen lädt und für die Auto-Vervollständigung der
	 * Suggestbox bereitstellt
	 */
	private void loadValuesForSuggestion() {
		// Alle Values für suggestbox
		rgsa.allValues(ReportGeneratorBaseForm.this.propertyId, new AsyncCallback<List<Value>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Laden der Vorschläge für die Eingenschaftsausprägungen ist fehlgeschlagen!");
			}

			@Override
			public void onSuccess(List<Value> result) {
				// Suggestbox leeren
				oracle.clear();
				// jeden Element in die Suggestbox laden
				for (Value v : result) {
					oracle.add(v.getName());
				}
			}
		});

	}

	/**
	 * Legt für jede geladenen Eigenschaft eine Spalte und einen Sortierhandler
	 * an und fügt sie der Ergebnistabelle hinzu
	 */
	private void eigenschaftSpaltenFuerTabelle() {
		// Nachdem die Eigenschaften geladen wurden können diese der Tabelle
		// hinzugefügt werden (alle Eingeschaften sind bekannt)
		for (Integer key : propertyIdUndName.keySet()) {
			final int keyP = key.intValue();

			// Anlegen einer eingenschaftsspalte
			TextColumn<ReportObjekt> eigenschaftscolumn = new TextColumn<ReportObjekt>() {
				@Override
				public String getValue(ReportObjekt object) {
					/*
					 * Der Wert der in der Spalte angezeigt wird, ist aus der
					 * Eigenschaftsmap des darzustellenden ReportObjekts zum
					 * Eigenschaftsschlüssel, für welchen die Spalte gerade
					 * angelegt wird, zu lesen.
					 */
					for (Integer kontaktEingeschaftKey : object.getPropertyValueMap().keySet()) {
						if (kontaktEingeschaftKey.intValue() == keyP) {
							return object.getPropertyValueMap().get(kontaktEingeschaftKey);
						}
					}
					return "";
				}
			};
			eigenschaftscolumn.setSortable(true);

			// Für jede Eingenschaftsspalte ist jeweils ein Sortierhandler zu
			// erstellen.
			ListHandler<ReportObjekt> eigenschaftscolumnHandler = new ListHandler<ReportObjekt>(dataProvider.getList());
			// Filter in der Tabelle Nachname
			eigenschaftscolumnHandler.setComparator(eigenschaftscolumn, new Comparator<ReportObjekt>() {
				public int compare(ReportObjekt c1, ReportObjekt c2) {
					if (c1 == c2) {
						return 0;
					}

					/*
					 * Vergleicht die Eingeschaftsausprägungen der zu
					 * vergleichenden Objekte anhand der Eingenschaft (keyP) für
					 * welchen dieser Handler gerade erstellt wird. Es wird der
					 * Wert Zum schlüssel (keyP) aus der Eigenschaftsmap des
					 * Objekts c1 mit dem Wert zum Schlüssel (keyP) aus der
					 * Eigenschaftsmap des Objekts c2 verlgichen
					 * 
					 */
					if (c1 != null) {
						return (c2 != null)
								? c1.getPropertyValueMap().get(keyP).compareTo(c2.getPropertyValueMap().get(keyP)) : 1;
					}
					return -1;
				}
			});
			table.addColumnSortHandler(eigenschaftscolumnHandler);

			// Spalte der Tabelle hinzufügen.
			table.addColumn(eigenschaftscolumn, propertyIdUndName.get(key));
		}
	}

}
