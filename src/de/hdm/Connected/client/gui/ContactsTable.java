package de.hdm.Connected.client.gui;

import java.rmi.server.LoaderHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dev.shell.Icons;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.ConnectedAdminAsync;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.ContactList;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

public class ContactsTable extends CellTable {

	private ConnectedAdminAsync connectedAdmin = ClientSideSettings.getConnectedAdmin();

	// private HorizontalPanel hPanel = new HorizontalPanel();

	private CellTable<Contact> cellTable = new CellTable<Contact>();
	private Map<Property, Value> propertyValueMap = null;
	
	private ArrayList<Contact> selectedContactsArray = new ArrayList<Contact>();
	private ArrayList<User> selectedUser = new ArrayList<User>();
	private ListBox userListbox = new ListBox();
	private ArrayList<User> allUsers = new ArrayList<User>();
	
	private Button addContactButton = new Button(" + Kontakt");
	private Button shareSelectedContacts = new Button("Ausgewählte Kontakte teilen");
	private Button addContactstoCL = new Button("Kontakte einer Kontaktliste hinzufügen");

	private TextBox searchBox = new TextBox();
	ListDataProvider<Contact> dataProvider = new ListDataProvider<Contact>();

	List<Contact> contacts = new ArrayList<Contact>();
	Set<Contact> selectedContacts = new HashSet<Contact>();
	


	SimplePager pager;
	String imageHtml = "<img src=" + "Trash_Can.png" + " alt=" + "Kontakt löschen" + ">";
	boolean buttonPressed;

	public ContactsTable() {
		// Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(cellTable);

		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		ClientSideSettings.getConnectedAdmin().getContactsByUserPermission(2, new AsyncCallback<ArrayList<Contact>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ArrayList<Contact> result) {
				contacts = result;

				final MultiSelectionModel<Contact> selectionModel = new MultiSelectionModel<Contact>();
				cellTable.setSelectionModel(selectionModel,
						DefaultSelectionEventManager.<Contact>createCheckboxManager());

				selectionModel.addSelectionChangeHandler(new Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						selectedContacts = selectionModel.getSelectedSet();

					}
				});

				Column<Contact, Boolean> checkColumn = new Column<Contact, Boolean>(new CheckboxCell(false, false)) {
					@Override
					public Boolean getValue(Contact object) {
						// Get the value from the selection model.
						return selectionModel.isSelected(object);
					}
				};

				Header<Boolean> checkAllHeader = new Header<Boolean>(new CheckboxCell()) {

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
												
							for(Contact c : contacts){
								selectionModel.setSelected(c, value);
							}
						

					}

				});
				
				checkColumn.setCellStyleNames("iconButton");
				
				cellTable.addColumn(checkColumn, checkAllHeader);
				
				
				ClickableTextCell prenameCell = new ClickableTextCell();

				Column<Contact, String> prenameColumn = new Column<Contact, String>(prenameCell) {
					
					public void render(Context context, Contact value, SafeHtmlBuilder sb) {
						int userId = 2;
					//TODO getCurrentUser
						if(value == null) {
							return;
						}else if(value.getCreatorId() != userId){
						sb.appendHtmlConstant("<i>");
						sb.appendEscaped(value.getPrename());
						sb.appendHtmlConstant("</i>");
								
						
						}else if(value.getCreatorId() == userId){
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

				prenameColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {

					@Override
					public void update(int index, final Contact object, String value) {
						// TODO Auto-generated method stub

						ClientSideSettings.getConnectedAdmin().findValueAndProperty(object.getBoId(),
								new AsyncCallback<Map<Property, Value>>() {

									public void onFailure(Throwable caught) {
										Window.alert("Ops, da ist etwas schief gelaufen!");
									}

									public void onSuccess(Map<Property, Value> result) {
										propertyValueMap = result;
										// Window.alert(Integer.toString(result.size()));
										// Window.alert(Integer.toString(globalIndex));
										ContactInfoForm showContact = new ContactInfoForm(object, result);

										showContact.center();
										showContact.show();

									}

								});

					}

				});

				cellTable.addColumn(prenameColumn, "Vorname");

				ClickableTextCell surnameCell = new ClickableTextCell();

				Column<Contact, String> surnameColumn = new Column<Contact, String>(surnameCell) {
					
					public void render(Context context, Contact value, SafeHtmlBuilder sb) {
						int userId = 2;
					//TODO getCurrentUser
						if(value == null) {
							return;
						}else if(value.getCreatorId() != userId){
						sb.appendHtmlConstant("<div><i>");
						sb.appendEscaped(value.getSurname());
						sb.appendHtmlConstant("</i></div>");
								
						
						}else if(value.getCreatorId() == userId){
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

				surnameColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {

					@Override
					public void update(int index, final Contact object, String value) {
						// TODO Auto-generated method stub

						ClientSideSettings.getConnectedAdmin().findValueAndProperty(object.getBoId(),
								new AsyncCallback<Map<Property, Value>>() {

									public void onFailure(Throwable caught) {
										Window.alert("Ops, da ist etwas schief gelaufen!");
									}

									public void onSuccess(Map<Property, Value> result) {
										propertyValueMap = result;
										// Window.alert(Integer.toString(result.size()));
										// Window.alert(Integer.toString(globalIndex));
										ContactInfoForm showContact = new ContactInfoForm(object, result);

										showContact.center();
										showContact.show();

									}

								});

					}

				});
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
						final ContactSharing sharePopUp = new ContactSharing(object);

						// Enable glass background.
						sharePopUp.setGlassEnabled(true);
						// updatePopUp.setPopupPosition(200, 300);
						sharePopUp.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

							public void setPosition(int offsetWidth, int offsetHeight) {
								// TODO Auto-generated method stub
								int left = (Window.getClientWidth() - offsetWidth) / 3;
								int top = (Window.getClientHeight() - offsetHeight) / 3;

								sharePopUp.setPopupPosition(left, top);
							}
						});

						sharePopUp.show();

					}
				});

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
						
						// Window.alert("Hallooo");
						final ContactForm updatePopUp = new ContactForm(object);

						// Enable glass background.
						updatePopUp.setGlassEnabled(true);
						// updatePopUp.setPopupPosition(200, 300);
						updatePopUp.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

							public void setPosition(int offsetWidth, int offsetHeight) {
								// TODO Auto-generated method stub
								int left = (Window.getClientWidth() - offsetWidth) / 3;
								int top = (Window.getClientHeight() - offsetHeight) / 3;

								updatePopUp.setPopupPosition(left, top);
							}
						});

						updatePopUp.show();

					}
				});

				cellTable.addColumn(updateColumn);

				ClickableTextCell deleteButton = new ClickableTextCell() {
					@Override
					public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
						String title = "Kontakt löschen";
						if (data != null) {
							sb.appendHtmlConstant("<img title='" + title + "' src=" + "/delete.png" + " alt="
									+ "Kontakt löschen" + " height=" + "25" + " width=" + "25" + ">");

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
						// TODO Auto-generated method stub
						// Clickhandler
						User user = new User();
						user.setBoId(2);
						buttonPressed = true;
						ClientSideSettings.getConnectedAdmin().deleteContact(object, user, new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(Void result) {
								// TODO Auto-generated method stub
								for (int i = 0; i < contacts.size(); i++) {
									if (contacts.get(i).getBoId() == object.getBoId()) {
										contacts.remove(i);
									}
								}
								dataProvider.getList().clear();
								dataProvider.getList().addAll(contacts);
								Window.alert("Kontakt " + object.getPrename() + " " + object.getSurname()
										+ " wurde gelöscht");
							}

						});

					}
				});

				cellTable.addColumn(deleteColumn);
				dataProvider.getList().clear();
				dataProvider.addDataDisplay(cellTable);

				/*
				 * final SingleSelectionModel<Contact> selectionModel = new
				 * SingleSelectionModel<Contact>();
				 * cellTable.setSelectionModel(selectionModel);
				 * selectionModel.addSelectionChangeHandler(new
				 * SelectionChangeEvent.Handler() { //final PopupPanel
				 * contactPopup = new PopupPanel(true, false); public void
				 * onSelectionChange(SelectionChangeEvent event) { final Contact
				 * selected = selectionModel.getSelectedObject(); if (selected
				 * != null && !buttonPressed) { //Window.alert("You selected: "
				 * + selected.prename + " " + selected.surname);
				 * 
				 * ClientSideSettings.getConnectedAdmin().findValueAndProperty(
				 * selected.getBoId(), new AsyncCallback<Map<Property, Value>>()
				 * {
				 * 
				 * public void onFailure(Throwable caught) {
				 * Window.alert("Ops, da ist etwas schief gelaufen!"); }
				 * 
				 * public void onSuccess(Map<Property, Value> result) {
				 * propertyValueMap = result; //
				 * Window.alert(Integer.toString(result.size())); //
				 * Window.alert(Integer.toString(globalIndex));
				 * ShowContactInfo_Dialog showContact = new
				 * ShowContactInfo_Dialog(selected);
				 * 
				 * showContact.center(); showContact.show();
				 * 
				 * }
				 * 
				 * });
				 * 
				 * 
				 * final Anchor selectedContact = new
				 * Anchor(selected.getPrename() + selected.getSurname());
				 * 
				 * 
				 * HorizontalPanel contactPopupContainer = new
				 * HorizontalPanel(); contactPopupContainer.setSpacing(10);
				 * final HTML contactInfo = new HTML();
				 * contactPopupContainer.add(contactInfo);
				 * 
				 * ///contactPopup.setWidget(contactPopupContainer);
				 * 
				 * contactInfo.setHTML("Vorname: "+ selected.getPrename() +
				 * "<br>" + "Nachname: " + selected.getSurname() + "</i>");
				 * 
				 * // int left = selectedContact.getAbsoluteLeft() + 86; // int
				 * top = selectedContact.getAbsoluteTop() + 45;
				 * //contactPopup.setPopupPosition(left, top); //
				 * contactPopup.show(); } else if (buttonPressed) {buttonPressed
				 * = false;} } });
				 */

				List<Contact> list = dataProvider.getList();
				for (Contact Contact : contacts) {
					list.add(Contact);
				}

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
				
				addContactButton.addClickHandler(new ClickHandler(){
						//Bei Click "Kontakt anlegen" Pop Up anzeigen
					@Override
					public void onClick(ClickEvent event) {
						final ContactForm newContact = new ContactForm();
						// Enable glass background.
						newContact.setGlassEnabled(true);					
						
						newContact.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

							public void setPosition(int offsetWidth, int offsetHeight) {
								// TODO Auto-generated method stub
								int left = (Window.getClientWidth() - offsetWidth) / 3;
								int top = (Window.getClientHeight() - offsetHeight) / 3;

								newContact.setPopupPosition(left, top);
							}
						});
						
					}
					
				});
				
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
				
				searchBox.setText("Nach Kontakten suchen");
				searchBox.getElement().getStyle().setColor("lightgrey");
				searchBox.addBlurHandler(new BlurHandler(){

					@Override
					public void onBlur(BlurEvent event) {
						TextBoxKeyUpHandler handler = null;
						searchBox.getElement().getStyle().setColor("lightgrey");
						searchBox.setText("Nach Kontakten suchen");
												
					}
					
				});
				
				searchBox.addClickHandler(new ClickHandler(){

					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						searchBox.setText("");
						searchBox.getElement().getStyle().setColor("black");
						
						//Der Suchbox einen KeyUp Handler anfügen. Bei jeder Eingabe werden die Kontakte angezeigt die dem Text inkl Wildcards davor und dahinter entsprechen.
						searchBox.addKeyUpHandler(new TextBoxKeyUpHandler());						
						
					}
					
				});
			
				
				HorizontalPanel buttonPanel = new HorizontalPanel();
				//buttonPanel.setWidth("1000px");
				//buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);	
				buttonPanel.setSpacing(20);
				
				buttonPanel.add(addContactButton);
				buttonPanel.add(shareSelectedContacts);
				buttonPanel.add(addContactstoCL);
				buttonPanel.add(searchBox);
				
				RootPanel.get("content").add(buttonPanel);
				RootPanel.get("content").add(cellTable);
				RootPanel.get("content").add(pager);

				// cellTablePanel.setCellHorizontalAlignment(pager,HasHorizontalAlignment.ALIGN_CENTER);

			}

		});
	}
		private class ShareMultipleContacts extends PopupPanel {
			
			

			public ShareMultipleContacts(){
				// Enable animation.
				setAnimationEnabled(true);

				// Enable glass background.
				setGlassEnabled(true);
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

				Button ok = new Button("Teilen");
				ok.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {

						for (int i = 0; i < userListbox.getItemCount(); i++) {
							if (userListbox.isItemSelected(i)) {
								selectedUser.add(allUsers.get(i));
							}
						}
						Window.alert(Integer.toString(selectedContactsArray.size()));
						Window.alert(Integer.toString(selectedUser.size()));
						if (selectedContactsArray.size() > 1) {
							ClientSideSettings.getConnectedAdmin().giveContactPermissonToUsers(selectedContactsArray,
									selectedUser, 1, new AsyncCallback<Void>() {

										@Override
										public void onFailure(Throwable caught) {
											Window.alert("Ops, da ist etwas schief gelaufen!");
										}

										@Override
										// jede Kontaktliste wird der ListBox
										// hinzugefügt
										public void onSuccess(Void result) {
											Window.alert("Alle Kontakte erfolgreich geteilt!");
											Window.alert(Integer.toString(selectedContactsArray.size()));
											Window.alert(Integer.toString(selectedUser.size()));
											// Window.alert(Integer.toString(cArray.size()));
											// Window.alert(Integer.toString(uArray.size()));
											allUsers.clear();
											userListbox.clear();
											Window.Location.reload();
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
				
				ClientSideSettings.getConnectedAdmin().findAllUser(new AsyncCallback<ArrayList<User>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Die User konnten nicht geladen werden");
					}

					@Override
					// jede Kontaktliste wird der ListBox hinzugefügt
					public void onSuccess(ArrayList<User> result) {
						allUsers = result;
						for (User u : result) {
							userListbox.addItem(u.getLogEmail());
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
		
		private class TextBoxKeyUpHandler implements KeyUpHandler{
			
		

		@Override
		public void onKeyUp(KeyUpEvent event) {
			// TODO Auto-generated method stub
			if(searchBox.getText() == ""){
				dataProvider.getList().clear();
				dataProvider.getList().addAll(contacts);
				cellTable.redraw();
				
				}else{
				String searchString = "*" + searchBox.getText().toLowerCase() + "*";
				searchString= searchString.replaceAll("\\*", "\\\\w*");
				ArrayList<Contact> foundContacts = new ArrayList<Contact>();
				for(Contact c : contacts){
					if (c.getPrename().toLowerCase().matches(searchString) || c.getSurname().toLowerCase().matches(searchString)){
			            foundContacts.add(c);
				}
				}
				
				dataProvider.getList().clear();
				dataProvider.getList().addAll(foundContacts);
				cellTable.redraw();
				}
			
		}
		
		}
	}

