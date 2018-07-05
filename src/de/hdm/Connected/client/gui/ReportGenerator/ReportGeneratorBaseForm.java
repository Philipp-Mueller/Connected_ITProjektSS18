package de.hdm.Connected.client.gui.ReportGenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
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
	//private VerticalPanel vPanel2 = new VerticalPanel();

	// Attribute Horizontal Panel
	private HorizontalPanel hPanelFilter = new HorizontalPanel();
	private HorizontalPanel hPanelRbAllContacts = new HorizontalPanel();
	private HorizontalPanel hPanelRbSharedContacts = new HorizontalPanel();
	private HorizontalPanel hPanelValueButton = new HorizontalPanel();
	private HorizontalPanel hPanelValueChosenButton = new HorizontalPanel();
	
	// Attribute RadioButton
	private RadioButton allContactsRb = new RadioButton(" Alle Kontakte anzeigen");
	private RadioButton sharedContactsRb = new RadioButton(" Alle getelten Kontakte anzeigen");

	// Attribute Listboxen
	private ListBox userListBox = new ListBox();
	private ListBox propertyListBox = new ListBox();
	private ListBox lb = new ListBox();

	// Attribute
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox box = new SuggestBox(oracle);
	private CellTable<ReportObjekt> table = new CellTable<ReportObjekt>();
	private ListDataProvider<ReportObjekt> dataProvider = new ListDataProvider<ReportObjekt>();
	private List<ReportObjekt> contactToShowInReport = new ArrayList<>();
	private boolean allContacts = false;
	private boolean sharedContacts = false;
	private Integer propertyId = null;
	private String valueDescription = "";
	protected String userEmail = "";
	private Map<Integer, String> propertyValueMap = new HashMap<>();
	private Map<Integer, String> propertyIdUndName = new HashMap<Integer, String>();
	private SimplePager pager;

	// Attribute Content
	private HTML labelFilter = new HTML("<strong>Filter wählen</strong>");
	private HTML labelProperty = new HTML("Eigenschaft wählen:");
	private HTML labelValue = new HTML("Wert eingeben:");
	private HTML labelPropertyValues = new HTML("Gewählte Eigenschaftswerte:");
	private HTML labelUser = new HTML("Nutzer wählen:");
	private HTML labelAllContactsText = new HTML(" Alle Kontakte anzeige");
	private HTML labelSharedContactsText = new HTML(" Alle geteilten Kontakte anzeige");

	// Footer Attribute
	private HorizontalPanel footer = new HorizontalPanel();
	private Anchor connectedLink = new Anchor("Connected", "Connected_ITProjektSS18.html");
	private HTML copyrightText2 = new HTML(" | ");
	private Anchor reportGeneratorLink = new Anchor(" ReportGenerator", "Connected_ITProjektSS18ReportGenerator.html");
	private HTML copyrightText = new HTML(" | © 2018 Connected | ");
	private Anchor impressumLink = new Anchor("Impressum");

	
	public ReportGeneratorBaseForm() {

		// Vertical Panel in Horizontal Panels
		hPanelFilter.add(filterVpanelLinks);
		hPanelFilter.add(datenHpanelRechts);
		
		//Überschrift filterVpanelLinks
		labelFilter.getElement().getStyle().setMarginBottom(2, Unit.EM);
		filterVpanelLinks.add(labelFilter);
		
		// AllContacts RadioButton
		allContactsRb.getElement().getStyle().setMarginBottom(1, Unit.EM);
		allContactsRb.setValue(allContacts);
		allContactsRb.setName(RadioButtonGruppe);
		allContactsRb.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				allContacts = ((RadioButton) event.getSource()).getValue();
				sharedContacts = false;
			}
		});
		hPanelRbAllContacts.add(allContactsRb);
		hPanelRbAllContacts.add(labelAllContactsText);
		filterVpanelLinks.add(hPanelRbAllContacts);

		// SharedContact RadioButton
		sharedContactsRb.getElement().getStyle().setMarginBottom(1, Unit.EM);
		sharedContactsRb.setValue(sharedContacts);
		sharedContactsRb.setName(RadioButtonGruppe);
		sharedContactsRb.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				sharedContacts = ((RadioButton) event.getSource()).getValue();
				allContacts = false;
			}
		});
		hPanelRbSharedContacts.add(sharedContactsRb);
		hPanelRbSharedContacts.add(labelSharedContactsText);
		filterVpanelLinks.add(hPanelRbSharedContacts);
		
		// Abstand zur userListbox und Aufforderungstext zur User-Auswahl
		labelUser.getElement().getStyle().setMarginRight(1, Unit.EM);
		labelUser.getElement().getStyle().setMarginTop(1, Unit.EM);
		filterVpanelLinks.add(labelUser);

		// Userlistbox
		userListBox.setVisibleItemCount(1);
		userListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				userEmail = ((ListBox) event.getSource()).getSelectedItemText();
			}
		});
		filterVpanelLinks.add(userListBox);

		// Abstand und Aufforderungstext zur interaktion mit denn Eigenschaften
		labelProperty.getElement().getStyle().setMarginTop(1, Unit.EM);
		filterVpanelLinks.add(labelProperty);

		// Abstand Property list box
		propertyListBox.setVisibleItemCount(1);
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
		labelValue.getElement().getStyle().setMarginTop(1, Unit.EM);
		box.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				valueDescription = ((SuggestBox) event.getSource()).getText();
			}
		});
		
		
		//Button zum Hinzufügen von Eigenschaften
		Button newPropertySelection = new Button("+");
		newPropertySelection.getElement().getStyle().setWidth(2, Unit.EM);
		newPropertySelection.getElement().getStyle().setMarginLeft(1, Unit.EM);
		newPropertySelection.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String value = box.getText();
				String propertyText = propertyListBox.getSelectedItemText();

				 // Werte aus der Listbox Anzeige in die HashMap für Übertragung an den Server merken
				propertyValueMap.put(propertyId, value);
				lb.addItem(propertyText + ": " + value, propertyId.toString());
			}
		});
		filterVpanelLinks.add(labelValue);
		hPanelValueButton.add(box);
		hPanelValueButton.add(newPropertySelection);
		filterVpanelLinks.add(hPanelValueButton);

		
		//Button zum entfernen ausgewählter Eigenschaften
		Button removePropertySelection = new Button("-");
		removePropertySelection.getElement().getStyle().setMarginLeft(1, Unit.EM);
		removePropertySelection.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Integer selektionsProperties = lb.getSelectedIndex();

				
				
				 // Werte aus der Hash Map für die Server Übertragung wieder entfernen, da nicht mehr Relevant für die Selektion
				propertyValueMap.remove(Integer.valueOf(lb.getSelectedValue()));
				lb.removeItem(selektionsProperties);
			}
		});
		
		removePropertySelection.getElement().getStyle().setWidth(2, Unit.EM);
		removePropertySelection.getElement().getStyle().setMarginTop(2, Unit.EM);
		
		
		//Ansicht der Listbox auf 3 Eigenschaften eingrenzen darüber erscheint Scrollbalken
		labelPropertyValues.getElement().getStyle().setMarginTop(1, Unit.EM);
		lb.setVisibleItemCount(3);
		lb.getElement().getStyle().setWidth(12, Unit.EM);
		
		
		filterVpanelLinks.add(labelPropertyValues);
		hPanelValueChosenButton.add(lb);
		hPanelValueChosenButton.add(removePropertySelection);
		filterVpanelLinks.add(hPanelValueChosenButton);
		
		
		// Anwenden button
		Button b = new Button("Filter Anwenden", new ClickHandler() {
			public void onClick(ClickEvent event) {

				ReportGeneratorBaseForm.this.valueDescription = box.getText();

				String mailToServer = "";
				if(!userEmail.equals("Ohne Nutzer")){
					mailToServer = userEmail;
				}
				
				rgsa.searchContacts(allContacts, sharedContacts, mailToServer, propertyValueMap,
						new AsyncCallback<List<ReportObjekt>>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Fehler beim lesen aller Kontakte aufgetreten");
							}

							@Override
							public void onSuccess(List<ReportObjekt> result) {

								// Daten in der Tabelle austauschen
								dataProvider.getList().clear();
								dataProvider.getList().addAll(result);
							}
						});
			}
		});
		// Abstand und Panel hinzufügen
		b.getElement().getStyle().setMarginTop(1, Unit.EM);
		b.getElement().getStyle().setMarginBottom(5, Unit.EM);
		filterVpanelLinks.add(b);

		
		// Contact Tabelle verbindet die Tabelle mit dem Data Provider.
		dataProvider.addDataDisplay(table);
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		
		//Spaltenbezeichnung "Vorname"
		TextColumn<ReportObjekt> nameColumn = new TextColumn<ReportObjekt>() {
			@Override
			public String getValue(ReportObjekt object) {
				return object.getVorname();
			}
		};
		nameColumn.setSortable(true);
		table.addColumn(nameColumn, "Vorname");

		//Spaltenbezeichnung "Nachname"
		TextColumn<ReportObjekt> surnameColumn = new TextColumn<ReportObjekt>() {
			@Override
			public String getValue(ReportObjekt object) {
				return object.getNachname();
			}
		};
		surnameColumn.setSortable(true);
		table.addColumn(surnameColumn, "Nachname");

		//
		table.setRowCount(contactToShowInReport.size(), true);
		table.setRowData(0, contactToShowInReport);

		//
	    ListHandler<ReportObjekt> columnSortHandler = new ListHandler<ReportObjekt>(contactToShowInReport);
	    
	    //
	    columnSortHandler.setComparator(nameColumn,
	        new Comparator<ReportObjekt>() {
	          public int compare(ReportObjekt c1, ReportObjekt c2) {
	            if (c1 == c2) {
	              return 0;
	            }

	            if (c1 != null) {
	              return (c2 != null) ? c1.getVorname().compareTo(c2.getVorname()) : 1;
	            }
	            return -1;
	          }
	        });
	    table.addColumnSortHandler(columnSortHandler);
	    
	    //
	    ListHandler<ReportObjekt> columnSortHandler2 = new ListHandler<ReportObjekt>(contactToShowInReport);
	    
	    //
	    columnSortHandler2.setComparator(surnameColumn,
	        new Comparator<ReportObjekt>() {
	          public int compare(ReportObjekt c1, ReportObjekt c2) {
	            if (c1 == c2) {
	              return 0;
	            }

	            // Vergleicht den Namen in den Spalten.
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
		
        
		// Nachdem die UI erstellt ist werden die Daten für das Dropdown und die Suggestbox geladen
		loadDataForFiltering();

		
		 // Vertical panel wird dem RootPanel hinzugefügt (Somit wirds sichtbar)
		RootPanel.get("content").add(hPanelFilter);
		
		
		//Paginierung der Tabelle
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(table);		

		HorizontalPanel hp = new HorizontalPanel();
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.add(pager);
		
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		hp.getElement().getStyle().setMarginLeft(10, Unit.EM);
		datenHpanelRechts.add(hp);
		
		
		//Footer Impressum
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
		footer.add(copyrightText2);
		footer.add(reportGeneratorLink);
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
				Window.alert("Property laden geht ned!");

			}

			@Override
			public void onSuccess(List<Property> result) {
				for (Property p : result) {
					String propertyId = String.valueOf(p.getId());
					propertyListBox.addItem(p.getName(), propertyId);
					propertyIdUndName.put(p.getId(), p.getName());
				}
				ReportGeneratorBaseForm.this.propertyId = Integer.valueOf(propertyListBox.getSelectedValue());

				 // Die Properties sind geladen, jetzt werden die Values für die Suggestbox im Property-Dropdown geladen
				loadValuesForSuggestion();
				eigenschaftSpaltenFuerTabelle();
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

	
	private void eigenschaftSpaltenFuerTabelle() {
		// Nachdem die Eigenschaften geladen wurden können diese der Tabelle hinzugefügt werden (alle Eingeschaften sind bekannt)
		for(Integer key : propertyIdUndName.keySet()) {
			final int keyP = key.intValue();
			
			TextColumn<ReportObjekt> eigenschaftscolumn = new TextColumn<ReportObjekt>() {
				@Override
				public String getValue(ReportObjekt object) {
					for(Integer kontaktEingeschaftKey : object.getPropertyValueMap().keySet()){
						if(kontaktEingeschaftKey.intValue() ==keyP) {
							return object.getPropertyValueMap().get(kontaktEingeschaftKey);
						}
					}
					return "";
				}
			};
			table.addColumn(eigenschaftscolumn, propertyIdUndName.get(key));
		}
	}
	
}
