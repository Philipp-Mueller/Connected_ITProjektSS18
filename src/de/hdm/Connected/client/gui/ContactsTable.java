package de.hdm.Connected.client.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.ContactList;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

public class ContactsTable extends CellTable<Contact> {

	private CellTable<Contact> cellTable = new CellTable<Contact>();
	private ArrayList<Contact> selectedContactsArray = new ArrayList<Contact>();
	private ArrayList<User> selectedUser = new ArrayList<User>();
	private ListBox userListbox = new ListBox();
	private ArrayList<User> allUsers = new ArrayList<User>();
	private HorizontalPanel actionsPanel = new HorizontalPanel();
	private VerticalPanel hintPanel = new VerticalPanel();

	private ArrayList<ContactList> allCLs = new ArrayList<ContactList>();
	private ArrayList<ContactList> selectedCLs = new ArrayList<ContactList>();
	private ListBox contactlistListbox = new ListBox();
	private Label search = new Label();
	private HTML breaks = new HTML("<br />");
	private HTML hint = new HTML();

	private Button shareSelectedContacts = new Button("Ausgewählte Kontakte teilen");
	private Button addContactstoCL = new Button("Kontakte zu Kontaktlisten hinzufügen");
	private Button shareSelectedContacts2 = new Button("Ausgewählte Kontakte teilen");

	private TextBox searchBox = new TextBox();
	private int row = 0;

	boolean isAppend = false;

	private ListDataProvider<Contact> dataProvider = new ListDataProvider<Contact>();

	private List<Contact> contacts = new ArrayList<Contact>();
	private Set<Contact> selectedContacts = new HashSet<Contact>();

	// Buttons für ContactList
	private Button shareContactListButton = new Button(
			"<img border='0' src='share_white.png' width = '20' length = '20'/>");
	private Button updateContactListButton = new Button(
			"<img border='0' src='edit_white.png' width = '20'  length = '20'/>");
	private Button deleteContactListButton = new Button(
			"<img border='0' src='delete_white.png' width = '20' length = '20'/>");
	private ContactList mainContactlist = null;
	private ArrayList<Contact> withinContactlist = null;
	private SimplePager pager;

	boolean buttonPressed;

	public ContactsTable(final ArrayList<Contact> contactlist, final ContactList contactlistObject) {

		mainContactlist = contactlistObject;
		withinContactlist = contactlist;

		// Pager um umschalten zu können wenn es mehr als 15 Einträge in dem
		// Cell Table();
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(cellTable);

		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		/**
		 * CellPreviewHandler hinzufügen um die Reihe eines Doppelklicks zu
		 * erfahren
		 */

		cellTable.addCellPreviewHandler(new CellPreviewEvent.Handler<Contact>() {

			@Override
			public void onCellPreview(final CellPreviewEvent<Contact> event) {
				if (BrowserEvents.CLICK.equalsIgnoreCase(event.getNativeEvent().getType())) {
					row = event.getIndex();

				}

			}

		});

		/**
		 * Dom-Handler um herauszufinden in welche Reihe geklickt wurde umd
		 * somit mit Doppeklick die ContactInfo zu öffnen.
		 */
		cellTable.addDomHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(final DoubleClickEvent event) {

				ClientSideSettings.getConnectedAdmin().getValuesByUserPermission(
						cellTable.getVisibleItem(row).getBoId(), ClientSideSettings.getCurrentUser().getBoId(),
						new AsyncCallback<ArrayList<Value>>() {

							public void onFailure(Throwable caught) {
								Window.alert("Ops, da ist etwas schief gelaufen!");
							}

							public void onSuccess(ArrayList<Value> result) {

								ContactInfoForm contact = new ContactInfoForm(cellTable.getVisibleItem(row), result);

								contact.center();
								contact.show();

							}

						});

			}
		}, DoubleClickEvent.getType());

		/** Zeigt alle Kontakte auf die der User Zugriff hat */

		ClientSideSettings.getConnectedAdmin().getContactsByUserPermission(
				ClientSideSettings.getCurrentUser().getBoId(), new AsyncCallback<ArrayList<Contact>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Kontakte konnten nicht geladen werden");

					}

					@Override
					public void onSuccess(ArrayList<Contact> result) {

						if (contactlist == null) {
							contacts = result;
						}
						if (contactlist != null) {
							contacts = contactlist;

						}
						/**
						 * Multiselection Model für den CellTable, mehrfache
						 * Auswahl möglich
						 */

						final MultiSelectionModel<Contact> selectionModel = new MultiSelectionModel<Contact>();
						cellTable.setSelectionModel(selectionModel,
								DefaultSelectionEventManager.<Contact>createDefaultManager());

						selectionModel.addSelectionChangeHandler(new Handler() {
							@Override
							public void onSelectionChange(SelectionChangeEvent event) {
								// Je nach ausgwählten Kontakten werden
								// verschiedene Buttons über dem Table
								// angezeigt.
								selectedContacts = selectionModel.getSelectedSet();
								if (contactlist == null) {
									if (selectedContacts != null && selectionModel.getSelectedSet().size() > 1) {

										shareSelectedContacts.setVisible(true);
										search.getElement().getStyle().setMarginLeft(184, Unit.PX);

									} else if (selectedContacts != null
											&& selectionModel.getSelectedSet().size() == 1) {
										shareSelectedContacts.setVisible(false);
										addContactstoCL.setVisible(true);
										search.getElement().getStyle().setMarginLeft(368, Unit.PX);
										hint.setHTML(
												"<i>Strg + Linke Maustaste klicken, um mehrere Einträgen auszuwählen</i>");

									} else if (selectionModel.getSelectedSet().size() == 0) {
										shareSelectedContacts.setVisible(false);
										addContactstoCL.setVisible(false);
										hint.setHTML("");

										search.getElement().getStyle().setMarginLeft(610, Unit.PX);

									}

								} else {
									if (selectedContacts != null && selectionModel.getSelectedSet().size() > 1) {

										shareSelectedContacts2.setVisible(true);
										search.getElement().getStyle().setMarginLeft(184, Unit.PX);

									} else if (selectedContacts != null
											&& selectionModel.getSelectedSet().size() == 1) {
										shareSelectedContacts2.setVisible(false);
										search.getElement().getStyle().setMarginLeft(368, Unit.PX);
										hint.setHTML(
												"<i>Strg + Linke Maustaste klicken, um mehrere Einträgen auszuwählen</i>");

									} else if (selectionModel.getSelectedSet().size() == 0) {
										shareSelectedContacts.setVisible(false);
										hint.setHTML("");

										search.getElement().getStyle().setMarginLeft(610, Unit.PX);

									}
								}

							}

						});

						ImageCell image = new ImageCell();
						// Wenn kontakt geteilt ist, wird dem User eine Kontakt
						// Symbol angezeigt
						Column<Contact, String> sharedColumn = new Column<Contact, String>(image) {

							@Override
							public String getValue(Contact object) {

								return "";
							}

							public void render(Context context, Contact data, SafeHtmlBuilder sb) {
								String title = "Geteilter Kontakt";
								if (data != null
										&& (data.getCreatorId() != ClientSideSettings.getCurrentUser().getBoId())) {
									sb.appendHtmlConstant("<img title='" + title + "' src=" + "/sharing.png" + " alt="
											+ "Geteilter Kontakt" + " height=" + "23" + " width=" + "23" + ">");

								}
							}

						};
						// Cell Box damit alle Kontakte
						CheckboxCell cell = new CheckboxCell(true, true);

						Header<Boolean> checkAllHeader = new Header<Boolean>(cell) {

							@Override

							public Boolean getValue()

				{

								return selectionModel.getSelectedSet().size() == cellTable.getRowCount();

							}

						};

						checkAllHeader.setUpdater(new ValueUpdater<Boolean>() {

							@Override

							public void update(Boolean value)

				{

								for (Contact c : contacts) {
									selectionModel.setSelected(c, value);
								}

								if (!addContactstoCL.isVisible()) {
									addContactstoCL.setVisible(true);
								} else if (!shareSelectedContacts.isVisible()) {
									shareSelectedContacts.setVisible(true);
								}

							}

						});

						sharedColumn.setCellStyleNames("iconButton");

						if (contactlist == null) {
							cellTable.addColumn(sharedColumn, checkAllHeader);
						}

						TextColumn<Contact> prenameColumn = new TextColumn<Contact>() {

							public void render(Context context, Contact value, SafeHtmlBuilder sb) {
								int userId = ClientSideSettings.getCurrentUser().getBoId();

								if (value == null) {
									return;
								} else if (value.getCreatorId() != userId) {
									sb.appendHtmlConstant("<i>");
									sb.appendEscaped(value.getPrename());
									sb.appendHtmlConstant("</i>");

								} else if (value.getCreatorId() == userId) {
									sb.appendHtmlConstant("<div>");
									sb.appendEscaped(value.getPrename());
									sb.appendHtmlConstant("</div>");
								}

							}

							public String getValue(Contact object) {
								return object.getPrename();
							}

						};

						prenameColumn.setSortable(true);

						cellTable.addColumn(prenameColumn, "Vorname");

						TextColumn<Contact> surnameColumn = new TextColumn<Contact>() {

							public void render(Context context, Contact value, SafeHtmlBuilder sb) {
								int userId = ClientSideSettings.getCurrentUser().getBoId();

								if (value == null) {
									return;
								} else if (value.getCreatorId() != userId) {
									sb.appendHtmlConstant("<div><i>");
									sb.appendEscaped(value.getSurname());
									sb.appendHtmlConstant("</i></div>");

								} else if (value.getCreatorId() == userId) {
									sb.appendHtmlConstant("<div>");
									sb.appendEscaped(value.getSurname());
									sb.appendHtmlConstant("</div>");
								}

							}

							public String getValue(Contact contact) {
								return contact.getSurname();
							}
						};

						surnameColumn.setSortable(true);

						cellTable.addColumn(surnameColumn, "Nachname");

						ClickableTextCell shareButton = new ClickableTextCell() {
							@Override
							public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
								String title = "Kontakt teilen";
								if (data != null) {
									sb.appendHtmlConstant("<img title='" + title + "' src=" + "/share.png" + " alt="
											+ "Kontakt bearbeiten" + " height=" + "25" + " width=" + "25" + ">");

								}
							}
						};

						Column<Contact, String> shareColumn = new Column<Contact, String>(shareButton) {
							public String getValue(Contact object) {
								return "";
							}
						};

						shareColumn.setCellStyleNames("iconButton");
						shareColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {
							@Override
							public void update(int index, Contact object, String value) {
								// Window.alert("Hallooo");
								final Contact shareContact = object;
								ClientSideSettings.getConnectedAdmin().findUserById(shareContact.getCreatorId(),
										new AsyncCallback<User>() {

											@Override
											public void onFailure(Throwable caught) {
												Window.alert(" User nicht gefunden");

											}

											@Override
											public void onSuccess(User result) {
												final ContactSharing sharePopUp = new ContactSharing(shareContact,
														result);

												// Enable glass background.
												sharePopUp.setGlassEnabled(true);

												sharePopUp.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

													public void setPosition(int offsetWidth, int offsetHeight) {

														int left = (Window.getClientWidth() - offsetWidth) / 3;
														int top = (Window.getClientHeight() - offsetHeight) / 3;

														sharePopUp.setPopupPosition(left, top);
													}
												});

												sharePopUp.show();

											}

										});
							}
						});
						// ClickAbleText Cell für den Bearbeiten Modus
						cellTable.addColumn(shareColumn);

						ClickableTextCell updateButton = new ClickableTextCell() {
							@Override
							public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
								String title = "Kontakt bearbeiten";
								if (data != null) {
									sb.appendHtmlConstant("<img title='" + title + "'src=" + "/edit.png" + " alt="
											+ "Kontakt bearbeiten" + " height=" + "25" + " width=" + "25" + ">");

								}
							}
						};

						Column<Contact, String> updateColumn = new Column<Contact, String>(updateButton) {
							public String getValue(Contact object) {
								return "";
							}
						};

						updateColumn.setCellStyleNames("iconButton");
						updateColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {
							@Override
							public void update(int index, Contact object, String value) {
								// Popup öffnen um Kontakt zu bearbeiten
								final ContactForm updatePopUp = new ContactForm(object, mainContactlist,
										withinContactlist);

								// Enable glass background.
								updatePopUp.setGlassEnabled(true);

								updatePopUp.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

									public void setPosition(int offsetWidth, int offsetHeight) {

										int left = (Window.getClientWidth() - offsetWidth) / 3;
										int top = (Window.getClientHeight() - offsetHeight) / 3;

										updatePopUp.setPopupPosition(left, top);
									}
								});

								updatePopUp.show();

							}
						});

						cellTable.addColumn(updateColumn);
						// Löschen Button für den Kontakt
						ClickableTextCell deleteButton = new ClickableTextCell() {
							@Override
							public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
								String title = "Kontakt löschen";
								String titelCL = "Kontakt von Kontaktliste entfernen";
								if (data != null) {
									if (contactlist == null) {
										sb.appendHtmlConstant("<img title='" + title + "' src=" + "/delete.png"
												+ " alt=" + "Kontakt löschen" + " height=" + "25" + " width=" + "25"
												+ ">");
									} else {
										sb.appendHtmlConstant("<img title='" + titelCL + "' src="
												+ "/deleteFromList.png" + " alt=" + "Kontakt von Kontaktliste entfernen"
												+ " height=" + "25" + " width=" + "25" + ">");
									}

								}
							}
						};

						Column<Contact, String> deleteColumn = new Column<Contact, String>(deleteButton) {
							public String getValue(Contact object) {
								return "";
							}
						};
						deleteColumn.setCellStyleNames("iconButton");

						deleteColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {

							@Override
							public void update(int index, final Contact object, String value) {

								// Wenn auf Löschen gedrückt wird, passiert
								// folgendes. Das Bestätigungsfenster wird
								// geöffnet.
								final DialogBox agreeDelete = new DialogBox();
								VerticalPanel vpanel = new VerticalPanel();
								HorizontalPanel buttonPanel = new HorizontalPanel();
								Button yesButton = new Button("Ja");
								Button noButton = new Button("Nein");
								noButton.addClickHandler(new ClickHandler() {
									@Override
									public void onClick(ClickEvent event) {
										agreeDelete.hide();
									}
								});
								// Bei Bestätigung den Kontakt löschen
								yesButton.addClickHandler(new ClickHandler() {
									@Override
									public void onClick(ClickEvent event) {

										User user = new User();
										user.setBoId(ClientSideSettings.getCurrentUser().getBoId());
										buttonPressed = true;
										ClientSideSettings.getConnectedAdmin().deleteContact(object, user,
												new AsyncCallback<Void>() {

													@Override
													public void onFailure(Throwable caught) {
														Window.alert("Kontakt konnte nicht gelöscht werden");

													}

													@Override
													public void onSuccess(Void result) {
														// Objekt aus dem
														// DataProvider löschen
														for (int i = 0; i < contacts.size(); i++) {
															if (contacts.get(i).getBoId() == object.getBoId()) {
																contacts.remove(i);
															}
														}
														dataProvider.getList().clear();
														dataProvider.getList().addAll(contacts);
														Window.alert("Kontakt " + object.getPrename() + " "
																+ object.getSurname() + " wurde gelöscht");
														agreeDelete.hide();
													}

												});
									};
								});

								vpanel.add(new HTML("Wollen Sie diesen Kontakt wirklich löschen?"));
								buttonPanel.add(noButton);
								buttonPanel.add(yesButton);
								if (contactlist == null) {
									vpanel.add(buttonPanel);
								}
								agreeDelete.setWidget(vpanel);
								agreeDelete.setGlassEnabled(true);
								agreeDelete.center();
								agreeDelete.show();
							}
						});

						// Bei Kontaktliste dessen Dialog öffnen
						if (contactlist != null) {
							deleteColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {

								@Override
								public void update(int index, final Contact object, String value) {

									// Clickhandler
									User user = new User();
									user.setBoId(2);
									buttonPressed = true;

									deleteDialog deleteD = new deleteDialog(index);
									deleteD.center();
									deleteD.show();

								}
							});
						}

						cellTable.addColumn(deleteColumn);
						dataProvider.getList().clear();
						dataProvider.addDataDisplay(cellTable);
						// Daten dem ListProvider hinzufügen
						List<Contact> list = dataProvider.getList();
						for (Contact Contact : contacts) {
							list.add(Contact);
						}
						/**
						 * Der SortHandler ermöglicht das Sortieren des
						 * CellTables nach Vor- und Nachname
						 **/
						ListHandler<Contact> columnSortHandler = new ListHandler<Contact>(list);

						columnSortHandler.setComparator(prenameColumn, new Comparator<Contact>() {
							public int compare(Contact c1, Contact c2) {
								if (c1 == c2) {
									return 0;
								}

								if (c1 != null) {
									return (c2 != null) ? c1.getPrename().compareTo(c2.getPrename()) : 1;
								}
								return -1;
							}
						});

						cellTable.addColumnSortHandler(columnSortHandler);

						ListHandler<Contact> columnSortHandler2 = new ListHandler<Contact>(list);

						columnSortHandler2.setComparator(surnameColumn, new Comparator<Contact>() {
							public int compare(Contact c1, Contact c2) {
								if (c1 == c2) {
									return 0;
								}

								// Compare the name columns.
								if (c1 != null) {
									return (c2 != null) ? c1.getSurname().compareTo(c2.getSurname()) : 1;
								}
								return -1;
							}
						});

						cellTable.addColumnSortHandler(columnSortHandler2);

						cellTable.getColumnSortList().push(prenameColumn);

						cellTable.setRowCount(contacts.size(), true);
						cellTable.setRowData(0, contacts);
						cellTable.setWidth("70%");

						// Clickhandler für den Button um Kontakte der
						// Kontaktliste hinzuzufügen

						addContactstoCL.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								for (Contact c : selectedContacts) {
									selectedContactsArray.add(c);
								}
								final AddContactsToContactList addCToCl = new AddContactsToContactList();
								// Enable glass background.
								addCToCl.setGlassEnabled(true);

								addCToCl.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
									// Position des Popups zentrieren
									public void setPosition(int offsetWidth, int offsetHeight) {

										int left = (Window.getClientWidth() - offsetWidth) / 3;
										int top = (Window.getClientHeight() - offsetHeight) / 3;

										addCToCl.setPopupPosition(left, top);
									}
								});

							}

						});
						/** ClickHandler um ausgewählte Kontakte zu teielen */
						shareSelectedContacts.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {

								for (Contact c : selectedContacts) {
									selectedContactsArray.add(c);
								}

								ShareMultipleContacts shareMultiplePopup = new ShareMultipleContacts();
								shareMultiplePopup.center();
								shareMultiplePopup.show();
							}

						});
						/**
						 * ClickHandler um ausgewählte Kontakte zu teilen, die
						 * sich in einer Kontaktliste befinden
						 */
						shareSelectedContacts2.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {

								for (Contact c : selectedContacts) {
									selectedContactsArray.add(c);
								}

								ShareMultipleContacts shareMultiplePopup = new ShareMultipleContacts();
								shareMultiplePopup.center();
								shareMultiplePopup.show();
							}

						});
						// Der searchbox einen KeyUPHandler zuweisen um Eingaben
						// zu kontrollieren
						searchBox.addKeyUpHandler(new TextBoxKeyUpHandler());
						HorizontalPanel buttonPanel = new HorizontalPanel();

						if (contactlist == null) {
							// Die WIdgets des Popups setzen

							buttonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
							buttonPanel.setSpacing(20);

							buttonPanel.add(shareSelectedContacts);
							buttonPanel.add(addContactstoCL);

							hint.setHTML("");

							hintPanel.add(hint);
							hintPanel.add(breaks);

							shareSelectedContacts.setVisible(false);
							addContactstoCL.setVisible(false);

							search.getElement().setInnerHTML("<strong>Kontakt suchen:</strong>");
							buttonPanel.add(search);
							buttonPanel.add(searchBox);
							searchBox.setWidth("215px");
							search.getElement().getStyle().setMarginLeft(610, Unit.PX);
							RootPanel.get("content").add(new HTML("<h3> Meine Kontakte <h3>"));
							RootPanel.get("content").add(buttonPanel);
							RootPanel.get("content").add(hintPanel);
							RootPanel.get("content").add(cellTable);
							RootPanel.get("content").add(pager);
						}

						// Widgets bei einer Kontaktliste
						if (contactlist != null) {

							actionsPanel.add(shareSelectedContacts2);
							shareSelectedContacts2.setVisible(false);
							search.getElement().setInnerHTML("<strong>Kontakt suchen:</strong>");
							actionsPanel.add(search);
							actionsPanel.add(searchBox);
							searchBox.setWidth("215px");
							search.getElement().getStyle().setMarginLeft(610, Unit.PX);

							hint.setHTML("");

							hintPanel.add(hint);
							hintPanel.add(breaks);

							buttonPanel.clear();
							buttonPanel.setSpacing(20);
							if (mainContactlist.getCreatorId() != ClientSideSettings.getCurrentUser().getBoId()) {
								buttonPanel.add(
										new HTML("<img border='0' src='sharing.png' width = '20' length = '20'/>"));
							}
							buttonPanel.add(new HTML("<h2> Kontaktliste: " + mainContactlist.getName() + "</h2>"));

							shareContactListButton.addClickHandler(new shareCotactListClickhandler());
							updateContactListButton.addClickHandler(new updateContactListClickhandler());
							deleteContactListButton.addClickHandler(new deleteContactListClickhandler());
							buttonPanel.add(shareContactListButton);
							buttonPanel.add(updateContactListButton);
							buttonPanel.add(deleteContactListButton);
							// buttonPanel.add(shareSelectedContacts);
							shareSelectedContacts.setVisible(false);

							RootPanel.get("content").add(buttonPanel);
							RootPanel.get("content").add(actionsPanel);
							RootPanel.get("content").add(cellTable);
							RootPanel.get("content").add(pager);
						}

					}

				});
	}

	/** PopUp wenn mehrere KOntakte geteilt werden sollen */
	private class ShareMultipleContacts extends PopupPanel {

		public ShareMultipleContacts() {
			// Enable animation.
			setAnimationEnabled(true);

			// Enable glass background.
			setGlassEnabled(true);
			userListbox.setMultipleSelect(true);

			Button zurück = new Button("Zurück");
			zurück.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					selectedUser.clear();
					selectedContacts.clear();
					userListbox.clear();
					allUsers.clear();
					ShareMultipleContacts.this.hide();
				}
			});
			// Anzeigen des Teilen Fensters
			Button ok = new Button("Teilen");
			ok.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {

					for (int i = 0; i < userListbox.getItemCount(); i++) {
						if (userListbox.isItemSelected(i)) {
							for (User u : allUsers) {
								if (u.getLogEmail().equals(userListbox.getItemText(i))) {
									selectedUser.add(u);
								}
							}

						}
					}

					if (selectedContactsArray.size() > 1) {
						ClientSideSettings.getConnectedAdmin().giveContactPermissonToUsers(selectedContactsArray,
								selectedUser, ClientSideSettings.getCurrentUser().getBoId(), new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										Window.alert("Ops, da ist etwas schief gelaufen!");
									}

									@Override
									// jede Kontaktliste wird der ListBox
									// hinzugefügt
									public void onSuccess(Void result) {
										allUsers.clear();
										userListbox.clear();
										Window.alert("Kontakte wurden geteilt");
									}

								});
					} else {

					}

					ShareMultipleContacts.this.hide();
				}
			});

			VerticalPanel v = new VerticalPanel();
			HorizontalPanel buttonPanel = new HorizontalPanel();

			userListbox.ensureDebugId("cwListBox-multiBox");
			userListbox.setVisibleItemCount(7);

			// Alle User finden mit denen geteilt werden kann.
			ClientSideSettings.getConnectedAdmin().findAllUser(new AsyncCallback<ArrayList<User>>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Die User konnten nicht geladen werden");
				}

				@Override
				// jeder User wird der ListBox hinzugefügt außer der eigene
				public void onSuccess(ArrayList<User> result) {
					allUsers = result;
					for (User u : result) {
						if (u.getBoId() != ClientSideSettings.getCurrentUser().getBoId()) {
							userListbox.addItem(u.getLogEmail());
						}

					}

				}

			});

			buttonPanel.add(ok);
			buttonPanel.add(zurück);
			v.add(new HTML("<h3> Mit welchen Usern sollen die Kontakte geteilt werden ?</h3>"));
			v.add(userListbox);
			v.add(buttonPanel);
			setWidget(v);

		}
	}

	/**
	 * Mit dem KeyUpHandler werden die Eingaben der SearchBox kontrolliert, wird
	 * ein
	 * 
	 * Zeichen eingegebenm sucht es nach Kontakten die diese Zeichen beinhalten
	 * 
	 */
	private class TextBoxKeyUpHandler implements KeyUpHandler {

		@Override
		public void onKeyUp(KeyUpEvent event) {

			if (searchBox.getText() != "") {
				String searchString = "*" + searchBox.getText().toLowerCase() + "*";
				searchString = searchString.replaceAll("\\*", "\\\\w*");
				ArrayList<Contact> foundContacts = new ArrayList<Contact>();
				for (Contact c : contacts) {
					if (c.getPrename().toLowerCase().matches(searchString)
							|| c.getSurname().toLowerCase().matches(searchString)) {
						foundContacts.add(c);
					}
				}

				dataProvider.getList().clear();
				dataProvider.getList().addAll(foundContacts);
				cellTable.redraw();

			} else {

				dataProvider.getList().clear();
				dataProvider.getList().addAll(contacts);
				cellTable.redraw();

			}

		}

	}

	// KOntakte zu einer KOntaktliste hinzufügen Popup
	private class AddContactsToContactList extends PopupPanel {

		public AddContactsToContactList() {

			// Enable animation.
			setAnimationEnabled(true);
			contactlistListbox.ensureDebugId("cwListBox-multiBox");
			contactlistListbox.setMultipleSelect(true);
			contactlistListbox.setVisibleItemCount(7);

			ClientSideSettings.getConnectedAdmin().getContactListsByUserPermission(
					ClientSideSettings.getCurrentUser().getBoId(), new AsyncCallback<ArrayList<ContactList>>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Ops, da ist wohl etwas schief gelafuen");

						}

						@Override
						public void onSuccess(ArrayList<ContactList> result) {
							// Anzeige der Kontaktliste mit Permission
							allCLs = result;
							for (ContactList cl : result) {
								contactlistListbox.addItem(cl.getName());
							}

						}

					});

			Button zurück = new Button("Abbrechen");
			zurück.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					selectedCLs.clear();
					selectedContactsArray.clear();
					contactlistListbox.clear();
					AddContactsToContactList.this.hide();
				}
			});

			Button ok = new Button("Hinzufügen");
			ok.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {

					for (int i = 0; i < contactlistListbox.getItemCount(); i++) {
						if (contactlistListbox.isItemSelected(i)) {
							selectedCLs.add(allCLs.get(i));
						}
					}

					if (selectedContactsArray.size() > 0) {
						ClientSideSettings.getConnectedAdmin().addContactsToContactList(selectedContactsArray,
								selectedCLs, new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										Window.alert("Ops, da ist etwas schief gelaufen!");
									}

									@Override
									// jede Kontaktliste wird der ListBox
									// hinzugefügt
									public void onSuccess(Void result) {
										Window.alert("Alle Kontakte wurden den Kontaktlisten hinzugefügt!");
										AddContactsToContactList.this.hide();
									}

								});
					}

					AddContactsToContactList.this.hide();
				}
			});

			VerticalPanel v = new VerticalPanel();
			HorizontalPanel buttonPanel = new HorizontalPanel();

			buttonPanel.add(zurück);
			buttonPanel.add(ok);

			v.add(contactlistListbox);
			v.add(buttonPanel);
			setWidget(v);

		}
	}

	/** Clickhandler zur Teilen einer Contactlist **/
	private class shareCotactListClickhandler implements ClickHandler {

		public void onClick(ClickEvent event) {

			ClientSideSettings.getConnectedAdmin().findUserById(mainContactlist.getCreatorId(),
					new AsyncCallback<User>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(" User nicht gefunden");

						}

						@Override
						public void onSuccess(User result) {
							// ContactSharing PopUp öffnen
							final ContactListSharing sharing = new ContactListSharing(mainContactlist, result);

							// Enable glass background.
							sharing.setGlassEnabled(true);
							// Position des PopUp setzen
							sharing.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

								public void setPosition(int offsetWidth, int offsetHeight) {

									int left = (Window.getClientWidth() - offsetWidth) / 3;
									int top = (Window.getClientHeight() - offsetHeight) / 3;

									sharing.setPopupPosition(left, top);
								}
							});

							sharing.show();

						}

					});

		}
	}

	/** Clickhandler zum Löschen einer Contactlist **/
	private class deleteContactListClickhandler implements ClickHandler {
		public void onClick(ClickEvent event) {

			deleteContactListDialog dia = new deleteContactListDialog();
			dia.center();
			dia.show();

		}
	}

	/** Clickhandler für Updaten einer Kontaktlist **/
	private class updateContactListClickhandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			updateDialog dia = new updateDialog();
			dia.center();
			dia.show();
		}
	}

	/** PopUp zum ändern des Namens der Kontaktliste **/
	private class updateDialog extends PopupPanel {

		public updateDialog() {

			// Enable animation.
			setAnimationEnabled(true);

			// Enable glass background.
			setGlassEnabled(true);

			VerticalPanel v = new VerticalPanel();
			v.add(new HTML("<h2> Kontaktliste " + mainContactlist.getName() + " umbenennen: </h2>"));

			Label nameLabel = new Label("Name: ");
			final TextBox nameTextBox = new TextBox();
			HorizontalPanel h = new HorizontalPanel();
			h.add(nameLabel);
			h.add(nameTextBox);
			v.add(h);

			Button close = new Button("Abbrechen");
			close.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					updateDialog.this.hide();
				}

			});

			Button ok = new Button("Speichern");
			ok.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {

					// Eingabeüberprüfung
					if (nameTextBox.getText().matches("")) {
						Window.alert("Bitte Wert eintragen!");
						return;
					}

					ContactList cl = new ContactList();
					cl.setName(nameTextBox.getText());
					cl.setBoId(mainContactlist.getBoId());
					ClientSideSettings.getConnectedAdmin().updateContactList(cl, new AsyncCallback<ContactList>() {
						@Override
						public void onFailure(Throwable caught) {

						}

						@Override
						public void onSuccess(ContactList result) {
							Window.alert("Erfolgreich umbenannt");
							RootPanel.get("content").clear();
							ContactListForm3 reload = new ContactListForm3(result);
							RootPanel.get("nav").clear();
							NavigationTreeModel treemodel = new NavigationTreeModel(result);
							RootPanel.get("nav").add(treemodel);

						}
					});
					updateDialog.this.hide();
				}
			});

			HorizontalPanel buttons = new HorizontalPanel();
			buttons.add(ok);
			buttons.add(close);
			v.add(buttons);
			setWidget(v);

		}
	}

	/** Dialog zum Löschen einses Kontakt von einer Kontaktliste **/
	private class deleteDialog extends DialogBox {

		public deleteDialog(final int index) {

			// Set the dialog box's caption.
			setText("Kontakt entfernen");

			// Enable animation.
			setAnimationEnabled(true);

			// Enable glass background.
			setGlassEnabled(true);

			VerticalPanel v = new VerticalPanel();

			Label deleteLabel = new Label("Kontakt von Kontaktliste entfernen?");
			v.add(deleteLabel);

			Button delete = new Button("Ja");
			Button abort = new Button("Abrechen");
			// Kontakt aus einer Kontaktliste entfernen
			delete.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					// Kontakt aus der Kontaktliste entfernen
					ClientSideSettings.getConnectedAdmin().removeContactFromContactList(contacts.get(index).getBoId(),
							mainContactlist.getBoId(), new AsyncCallback<Void>() {

								public void onFailure(Throwable caught) {
									Window.alert("Ops, da ist etwas schief gelaufen!");
								}

								public void onSuccess(Void result) {
									Window.alert("Kontakt erfolgreich von Kontaktliste entfernt");
									dataProvider.getList().remove(index);
									dataProvider.refresh();

								}

							});

					deleteDialog.this.hide();

				}

			});

			abort.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					deleteDialog.this.hide();
				}

			});
			HorizontalPanel h = new HorizontalPanel();

			h.add(abort);
			h.add(delete);
			v.add(h);
			setWidget(v);

		}

	}

	/** DialogBox zum Löschen einer Kontaktliste **/
	private class deleteContactListDialog extends DialogBox {

		public deleteContactListDialog() {

			// Set the dialog box's caption.
			setText("Löschen");

			// Enable animation.
			setAnimationEnabled(true);

			// Enable glass background.
			setGlassEnabled(true);

			VerticalPanel v = new VerticalPanel();

			Label deleteLabel = new Label("Löschen wirklich durchführen?");
			v.add(deleteLabel);

			Button delete = new Button("Ja");
			Button abort = new Button("Abrechen");

			delete.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {

					ClientSideSettings.getConnectedAdmin().deleteContactList(mainContactlist,
							ClientSideSettings.getCurrentUser().getBoId(), new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Ops, da ist etwas schief gelaufen!");
								}

								@Override
								// jede Kontaktliste wird der ListBox
								// hinzugefügt
								public void onSuccess(Void result) {
									Window.alert("Löschen von " + mainContactlist.getName() + " war erfolgreich!");
									RootPanel.get("content").clear();
									ContactsTable table = new ContactsTable(null, null);
									RootPanel.get("nav").clear();
									NavigationTreeModel navi = new NavigationTreeModel(null);
									RootPanel.get("nav").add(navi);
								}

							});

					deleteContactListDialog.this.hide();

				}

			});

			abort.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					deleteContactListDialog.this.hide();
				}

			});
			HorizontalPanel h = new HorizontalPanel();

			h.add(delete);
			h.add(abort);
			v.add(h);
			setWidget(v);

		}

	}

}