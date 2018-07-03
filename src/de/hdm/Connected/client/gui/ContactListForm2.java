package de.hdm.Connected.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.thirdparty.javascript.jscomp.Result;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager.SelectAction;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.ContactList;
import de.hdm.Connected.shared.bo.Permission;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

/**
 * Klasse für die Bereistellung eines Formulars zum Anlegen/Bearbeiten einer
 * Kontaltiste
 * 
 * @author Moritz
 *
 */

public class ContactListForm2 extends Widget {

	Label nameLabel = new Label("Name:");
	TextBox nameBox = new TextBox();
	Label yourContactsLabel = new Label("Deine Kontakte:");
	ListBox contactListBox = new ListBox();
	Button newContactListButton = new Button("Neue Kontaktliste erstellen", new newContactListClickhandler());
	Button shareContactListButton = new Button("Kontaktliste teilen", new shareCotactListClickhandler());
	Button updateContactListButton = new Button("Kontaktliste umbenennen", new updateContactListClickhandler());
	// Button sharePartOfClButton = new Button("Kontakte von Kontaktliste
	// teilen", new sharePartofClClickhandler());
	// Button addContactButton = new Button("Kontakt hinzufügen", new
	// addContactsToContatListClickHandler());
	Button createListButton = new Button("Liste erstellen", new createContactListClickhandler());
	Button visitbutton = null;
	Button shareSeletedContactsButton = new Button("Auswahl teilen", new shareSelectedContactsClickhandler());
	Button shareContactList = new Button("Teilen", new shareContactListwithUserClickhandler());
	Button deleteCLButton = new Button("Kontaktliste löschen", new deleteContactListClickhandler());

	HorizontalPanel masterPanel = new HorizontalPanel();
	VerticalPanel topPanel = new VerticalPanel();
	VerticalPanel contentPanel = new VerticalPanel();
	HorizontalPanel namePanel = new HorizontalPanel();
	HorizontalPanel contactsPanel = new HorizontalPanel();
	HorizontalPanel buttonPanel = new HorizontalPanel();
	// Array, das alle CLists von User speichert
	ArrayList<ContactList> clArray = null;
	// Array, das Contact CL beziehung hält
	ArrayList<Contact> contactArray = null;
	ArrayList<Contact> cArray = new ArrayList<Contact>();
	ArrayList<User> uArray = new ArrayList<User>();
	ArrayList<Contact> c = null;
	ArrayList<User> publicUserArray = null;
	ArrayList<Contact> globalContactArray = null;
	CellTable<Contact> contacttable = new CellTable<Contact>();
	CellTable<Contact> contacttable2 = new CellTable<Contact>();
	final ListBox userListbox = new ListBox(true);
	final ListBox userListbox2 = new ListBox(true);

	Map<Property, Value> propertyValueMap = new HashMap<Property, Value>();

	Grid clGrid = new Grid();
	public int row;
	public int globalIndex;
	boolean answer;
	private int publicindex;

	private ListDataProvider<Contact> dataProvider = new ListDataProvider<Contact>();
	Set<Contact> set1 = new HashSet<Contact>();
	String sizeSt;
	
	ClickableTextCell prenameCell = new ClickableTextCell();
	
	Column<Contact, String> prenameColumn = new Column<Contact, String>(prenameCell) {
		public String getValue(Contact contact) {
			return contact.getPrename();
		}
	};

	public ContactListForm2(int selectedId) {

		row = selectedId;
		
		Window.alert(Integer.toString(row));

		// namePanel.add(nameLabel);
		// namePanel.add(nameBox);
		//
		// buttonPanel.add(new HTML("<h2>Deine Kontaktlisten:</h2>"));
		// buttonPanel.add(newContactListButton);

		// Share kommt erst in die einzelansicht einer CL
		// buttonPanel.add(shareContactListButton);
		// topPanel.add(namePanel);
		// topPanel.add(addContactButton);
		// topPanel.add(createListButton);
		RootPanel.get("content").add(buttonPanel);
		buttonPanel.add(newContactListButton);

		RootPanel.get("content").add(masterPanel);

		masterPanel.add(topPanel);
		masterPanel.add(contentPanel);
		// Muss später findContactlists per User id sein
		ClientSideSettings.getConnectedAdmin().findAllContactlists(new AsyncCallback<ArrayList<ContactList>>() {
			int i = 0;

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Die Kontaktlisten konnten nicht geladen werden");
			}

			@Override
			// jede Kontaktliste wird der ListBox hinzugefügt
			public void onSuccess(ArrayList<ContactList> result) {
				topPanel.add(clGrid);
				clArray = result;
				clGrid.resize(result.size(), 2);
				for (ContactList cl : result) {
					// contactlist.addItem(cl.getName());
					clGrid.setWidget(i, 0, new Label(cl.getName()));
					visitbutton = new Button(">>");
					visitbutton.addClickHandler(new visitClickhandler());
					clGrid.setWidget(i, 1, visitbutton);
					i = i + 1;
				}

			}

		});


		
		prenameColumn.setSortable(true);
		
		prenameColumn.setFieldUpdater(new FieldUpdater<Contact, String>(){

			@Override
			public void update(int index, final Contact object, String value) {
				// TODO Auto-generated method stub
				
				ClientSideSettings.getConnectedAdmin().findValueAndProperty(object.getBoId(),2,
						new AsyncCallback<Map<Property, Value>>() {

							public void onFailure(Throwable caught) {
								Window.alert("Ops, da ist etwas schief gelaufen!");
							}

							public void onSuccess(Map<Property, Value> result) {
								propertyValueMap = result;
								// Window.alert(Integer.toString(result.size()));
								// Window.alert(Integer.toString(globalIndex));
//								ContactInfoForm showContact = new ContactInfoForm(object, result);
//
//								showContact.center();
//								showContact.show();

							}

						});
				
			}
			
		});
		
		ClickableTextCell surnameCell = new ClickableTextCell();	
		
		Column<Contact, String> surnameColumn = new Column<Contact, String>(surnameCell) {
			public String getValue(Contact contact) {
				return contact.getSurname();
			}
		};
		
		surnameColumn.setSortable(true);
		
		surnameColumn.setFieldUpdater(new FieldUpdater<Contact, String>(){

			@Override
			public void update(int index, final Contact object, String value) {
				// TODO Auto-generated method stub
				
				ClientSideSettings.getConnectedAdmin().findValueAndProperty(object.getBoId(),2,
						new AsyncCallback<Map<Property, Value>>() {

							public void onFailure(Throwable caught) {
								Window.alert("Ops, da ist etwas schief gelaufen!");
							}

							public void onSuccess(Map<Property, Value> result) {
								propertyValueMap = result;
								// Window.alert(Integer.toString(result.size()));
								// Window.alert(Integer.toString(globalIndex));
//								ContactInfoForm showContact = new ContactInfoForm(object, result);
//
//								showContact.center();
//								showContact.show();

							}

						});
				
			}
			
		});
		
		
		
		
		ButtonCell visitButtonCell = new ButtonCell();
		ButtonCell deleteButtonCell = new ButtonCell();
		Column visitbuttonColumn = new Column<Contact, String>(visitButtonCell) {
			@Override
			public String getValue(Contact object) {
				// The value to display in the button.
				return "Visit";
			}
		};
		Column deletebuttonColumn = new Column<Contact, String>(visitButtonCell) {
			@Override
			public String getValue(Contact object) {
				// The value to display in the button.
				return "Delete";
			}
		};

		visitbuttonColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {
			public void update(int index, Contact object, String value) {
				// Value is the button value. Object is the row object.

				ClientSideSettings.getConnectedAdmin().findValueAndProperty(globalContactArray.get(index).getBoId(),2,
						new AsyncCallback<Map<Property, Value>>() {

							public void onFailure(Throwable caught) {
								Window.alert("Ops, da ist etwas schief gelaufen!");
							}

							public void onSuccess(Map<Property, Value> result) {
								propertyValueMap = result;
								// Window.alert(Integer.toString(result.size()));
								// Window.alert(Integer.toString(globalIndex));
								MyDialog d = new MyDialog();
								d.center();
								d.show();

							}

						});
				// Window.alert("You clicked:" + index);
				// clGrid.clear();
				globalIndex = index;
				// MyDialog d = new MyDialog();
				// d.center();
				// d.show();

			}
		});

		deletebuttonColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {
			public void update(final int index, Contact object, String value) {
				// Value is the button value. Object is the row object.
				// Window.alert("You clicked: " + index);
				publicindex = index;

				deleteDialog deleteD = new deleteDialog();
				deleteD.center();
				deleteD.show();

				// ClientSideSettings.getConnectedAdmin().removeContactFromContactList(
				// globalContactArray.get(index).getBoId(),
				// clArray.get(row).getBoId(),
				// new AsyncCallback<Void>() {
				// public int boIdvonContact =
				// globalContactArray.get(index).getBoId();
				// public int boIdvonCL = clArray.get(row).getBoId();
				//
				// public void onFailure(Throwable caught) {
				// Window.alert("Ops, da ist etwas schief gelaufen!");
				// }
				//
				// public void onSuccess(Void result) {
				// Window.alert("Kontakt erfolgreich von Kontaktliste
				// entfernt");
				// dataProvider.getList().remove(index);
				// dataProvider.refresh();
				//
				// }
				//
				// });
			}
			// clGrid.clear();

		});
		contacttable.addColumn(prenameColumn, "Vorname");
		contacttable.addColumn(surnameColumn, "Nachname");
		contacttable.addColumn(visitbuttonColumn, "");
		contacttable.addColumn(deletebuttonColumn, "");

		ClientSideSettings.getConnectedAdmin().findContactsByContactListId(selectedId, new showContactListCallback());

	}

	private class visitClickhandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			contactArray = new ArrayList<Contact>();
			// RootPanel.get("content").clear();
			masterPanel.add(contentPanel);
			contentPanel.clear();
			row = (clGrid.getCellForEvent(event).getRowIndex());
			// Window.alert(Integer.toString(clArray.get(row).getBoId())); // id
			int idvonCl = clArray.get(row).getBoId(); // von
														// ContactList

			ClientSideSettings.getConnectedAdmin().findContactsByContactListId(idvonCl, new showContactListCallback());
			try {
				// Window.alert(Integer.toString(contactArray.size()));

			} catch (Exception e) {
				Window.alert(e.toString());
				e.printStackTrace();
			}

		}
	}

	private class createContactListClickhandler implements ClickHandler {

		public void onClick(ClickEvent event) {

			ClientSideSettings.getConnectedAdmin().createContactList(nameBox.getText(), 2,
					new createContactListCallback());

			RootPanel.get("content").clear();
			ContactListForm2 neuLaden = new ContactListForm2(row);

		}
	};

	private class newContactListClickhandler implements ClickHandler {

		public void onClick(ClickEvent event) {

			// RootPanel.get("content").clear();
			// topPanel.clear();
			// RootPanel.get("content").add(namePanel);
			// RootPanel.get("content").add(topPanel);
			// namePanel.add(nameLabel);
			// namePanel.add(nameBox);
			//
			// topPanel.add(new HTML("<h2> Neue Kontaktliste erstellen</h2>"));
			// topPanel.add(namePanel);
			// //topPanel.add(addContactButton);
			// topPanel.add(createListButton);
			newDialog newi = new newDialog();
			newi.center();
			newi.show();

		}
	};

	private class createContactListCallback implements AsyncCallback<ContactList> {

		public void onFailure(Throwable caught) {
			Window.alert("Ops, da ist etwas schief gelaufen!");
		}

		public void onSuccess(ContactList result) {
			Window.alert("Contactlist erfolgreich angelegt!");
			// RootPanel.get("content").clear();

		}

	}

	private class showContactListCallback implements AsyncCallback<ArrayList<Contact>> {

		public void onFailure(Throwable caught) {
			Window.alert("Ops, da ist etwas schief gelaufen!");

		}

		public void onSuccess(final ArrayList<Contact> result) {
			// Window.alert("Contactlist gefunden");

			globalContactArray = new ArrayList<Contact>();
			globalContactArray = result;
			// for (Contact cid : result) {
			// ClientSideSettings.getConnectedAdmin().findContactById(cid.getBoId(),
			// }

			// masterPanel.add(buttonPanel);
			// buttonPanel.clear();
			// buttonPanel.add(newContactListButton);
			buttonPanel.add(shareContactListButton);
			buttonPanel.add(updateContactListButton);
			buttonPanel.add(deleteCLButton);

			// contentPanel.add(new HTML("<h2> Kontaktliste " +
			// clArray.get(row).getName() + ": </h2>"));
			if (result.size() == 0) {
				Label empty = new Label("Kontaktliste ist leer");
				contentPanel.add(empty);
				Button addCtoCl = new Button("Kontakte hinzufügen");
				addCtoCl.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						ContactsTable contactstable = new ContactsTable(null, null);
					}
				});
				contentPanel.add(addCtoCl);
			} else {
				contentPanel.add(contacttable);
			}

			// List<Contact> listcontacts = result;

			dataProvider.getList().clear();
			dataProvider.getList().addAll(result);
			dataProvider.addDataDisplay(contacttable);

			int setSize = set1.size();
			sizeSt = Integer.toString(setSize);
			// contentPanel.add(contacttable);
			List<Contact> list = dataProvider.getList();
		    for (Contact Contact : result) {
		      list.add (Contact);
		    }
			
			
		    ListHandler<Contact> columnSortHandler = new ListHandler<Contact>(result);
		    
		    columnSortHandler.setComparator(prenameColumn,
		        new Comparator<Contact>() {
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
		    
		    contacttable.addColumnSortHandler(columnSortHandler);
		    
		    contacttable.getColumnSortList().push(prenameColumn);
		    contacttable.setRowCount(result.size(), true);
		    contacttable.setRowData(0, result);
		    contacttable.setWidth("70%");
		    
		    contacttable.redraw();
			//RootPanel.get("content").add(contacttable);
			//RootPanel.get("content").add(pager);
			

		}

	}

	private class shareCotactListClickhandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			//
			shareDialog dia = new shareDialog();
			dia.center();
			dia.show();

			// RootPanel.get("content").clear();
			// topPanel.clear();
			// //buttonPanel.clear();
			// userListbox.clear();
			// RootPanel.get("content").add(topPanel);
			// //RootPanel.get("content").add(buttonPanel);
			//
			// topPanel.add(new HTML("<h2> Teilen von " +
			// clArray.get(row).getName()));
			// topPanel.add(userListbox);
			// topPanel.add(shareContactList);
			//
			// userListbox.setEnabled(true);
			//
			// // multi auswahl freischalten in ListBox
			// userListbox.ensureDebugId("cwListBox-multiBox");
			// userListbox.setVisibleItemCount(7);
			// // Alle Kontaktlisten aus DB abrufen
			//
			// publicUserArray = new ArrayList<User>();
			// // TODO nur KOntaktlisten des aktuellen Users abrufen!
			// ClientSideSettings.getConnectedAdmin().findAllUser(new
			// AsyncCallback<ArrayList<User>>() {
			//
			// @Override
			// public void onFailure(Throwable caught) {
			// Window.alert("Die User konnten nicht geladen werden");
			// }
			//
			// @Override
			// // jede Kontaktliste wird der ListBox hinzugefügt
			// public void onSuccess(ArrayList<User> result) {
			// publicUserArray = result;
			// for (User u : result) {
			// userListbox.addItem(u.getLogEmail());
			// }
			//
			// }
			//
			// });
			//
		}
	}

	private class shareContactListwithUserClickhandler implements ClickHandler {
		public void onClick(ClickEvent event) {

			uArray.clear();
			for (int i = 0; i < userListbox.getItemCount(); i++) {
				if (userListbox.isItemSelected(i)) {
					uArray.add(publicUserArray.get(i));
				}
			}

			ClientSideSettings.getConnectedAdmin().givePermissionToUsers(clArray.get(row).getBoId(), uArray, 1,
					new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Ops, da ist etwas schief gelaufen!");
						}

						@Override
						// jede Kontaktliste wird der ListBox
						// hinzugefügt
						public void onSuccess(Void result) {
							Window.alert("Teilen von " + clArray.get(row).getName() + " war erfolgreich!");
							Window.Location.reload();
						}

					});

		}
	}

	private class deleteContactListClickhandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			
			deleteContactListDialog dia = new deleteContactListDialog();
			dia.center();
			dia.show();
//			ContactList cl = new ContactList();
//			cl.setBoId(clArray.get(row).getBoId());
//			ClientSideSettings.getConnectedAdmin().deleteContactList(cl, new AsyncCallback<Void>() {
//
//				@Override
//				public void onFailure(Throwable caught) {
//					Window.alert("Ops, da ist etwas schief gelaufen!");
//				}
//
//				@Override
//				// jede Kontaktliste wird der ListBox
//				// hinzugefügt
//				public void onSuccess(Void result) {
//					Window.alert("Löschen von " + clArray.get(row).getName() + " war erfolgreich!");
//					RootPanel.get("content").clear();
//					ContactListForm2 neuLaden = new ContactListForm2(row);
//
//				}
//
//			});

		}
	}

	private class updateContactListClickhandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			updateDialog dia = new updateDialog();
			dia.center();
			dia.show();
		}
	}

	private class MyDialog extends DialogBox {

		public MyDialog() {
			// Set the dialog box's caption.
			setText(globalContactArray.get(globalIndex).getPrename() + " "
					+ globalContactArray.get(globalIndex).getSurname());

			// Enable animation.
			setAnimationEnabled(true);

			// Enable glass background.
			setGlassEnabled(true);

			Button ok = new Button("OK");
			ok.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					MyDialog.this.hide();
				}
			});

			VerticalPanel v = new VerticalPanel();

			for (Map.Entry<Property, Value> entry : propertyValueMap.entrySet()) {
				Label propertyLabel = new Label(entry.getKey().getName());
				Label valueLabel = new Label(entry.getValue().getName());
				HorizontalPanel h = new HorizontalPanel();
				h.add(propertyLabel);
				h.add(new Label(": "));
				h.add(valueLabel);
				v.add(h);

			}
			if (propertyValueMap.size() == 0) {
				v.add(new Label("Keine Eigenschaften gespeichert"));
			}

			v.add(ok);
			setWidget(v);

		}
	}

	private class updateDialog extends DialogBox {

		public updateDialog() {
			// Set the dialog box's caption.
			setText("Kontaktliste " + clArray.get(row).getName() + " umbenennen:");

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
					updateDialog.this.hide();
				}

			});

			Button ok = new Button("Speichern");
			ok.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					ContactList cl = new ContactList();
					cl.setName(nameTextBox.getText());
					cl.setBoId(clArray.get(row).getBoId());
					ClientSideSettings.getConnectedAdmin().updateContactList(cl, new AsyncCallback<ContactList>() {
						@Override
						public void onFailure(Throwable caught) {

						}

						@Override
						public void onSuccess(ContactList result) {
							Window.alert("Erfolgreich umbenannt");
							RootPanel.get("content").clear();
							ContactListForm2 mycontactlistForm = new ContactListForm2(clArray.get(row).getBoId());
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

	private class shareDialog extends DialogBox {

		public shareDialog() {
			// Set the dialog box's caption.
			setText("Kontaktliste" + clArray.get(row).getName() + "teilen:");

			// Enable animation.
			setAnimationEnabled(true);

			// Enable glass background.
			setGlassEnabled(true);

			VerticalPanel v = new VerticalPanel();
			v.clear();

			v.add(userListbox);

			userListbox.clear();

			// RootPanel.get("content").add(buttonPanel);

			userListbox.setEnabled(true);

			// multi auswahl freischalten in ListBox
			userListbox.ensureDebugId("cwListBox-multiBox");
			userListbox.setVisibleItemCount(7);
			// Alle Kontaktlisten aus DB abrufen

			publicUserArray = new ArrayList<User>();
			// TODO nur KOntaktlisten des aktuellen Users abrufen!
			ClientSideSettings.getConnectedAdmin().findAllUser(new AsyncCallback<ArrayList<User>>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Die User konnten nicht geladen werden");
				}

				@Override
				// jede Kontaktliste wird der ListBox hinzugefügt
				public void onSuccess(ArrayList<User> result) {
					publicUserArray = result;
					for (User u : result) {
						userListbox.addItem(u.getLogEmail());
					}

				}

			});

			Button close = new Button("Abbrechen");
			close.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					shareDialog.this.hide();
				}

			});

			Button ok = new Button("Teilen");
			ok.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {

					uArray.clear();
					for (int i = 0; i < userListbox.getItemCount(); i++) {
						if (userListbox.isItemSelected(i)) {
							uArray.add(publicUserArray.get(i));
						}
					}

					ClientSideSettings.getConnectedAdmin().givePermissionToUsers(clArray.get(row).getBoId(), uArray, 1,
							new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Ops, da ist etwas schief gelaufen!");
								}

								@Override
								// jede Kontaktliste wird der ListBox
								// hinzugefügt
								public void onSuccess(Void result) {
									Window.alert("Teilen von " + clArray.get(row).getName() + " war erfolgreich!");
									Window.alert(Integer.toString(uArray.size()));
									RootPanel.get("contant").clear();
									ContactListForm2 mycontactlistForm = new ContactListForm2(
											clArray.get(row).getBoId());
								}

							});
					shareDialog.this.hide();
				}
			});

			HorizontalPanel buttons = new HorizontalPanel();
			buttons.add(ok);
			buttons.add(close);
			v.add(buttons);
			setWidget(v);
		}
	}

	private class newDialog extends DialogBox {

		public newDialog() {
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
					newDialog.this.hide();
				}

			});

			Button ok = new Button("Erstellen");
			ok.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					ContactList cl = new ContactList();
					cl.setName(nameTextBox.getText());

					ClientSideSettings.getConnectedAdmin().createContactList(nameTextBox.getText(), 2,
							new AsyncCallback<ContactList>() {
								@Override
								public void onFailure(Throwable caught) {

								}

								@Override
								public void onSuccess(ContactList result) {
									Window.alert("Erfolgreich erstellt");
									RootPanel.get("content").clear();
									ContactListForm2 mycontactlistForm = new ContactListForm2(result.getBoId());
								}
							});
					newDialog.this.hide();
				}
			});
			HorizontalPanel buttons = new HorizontalPanel();
			buttons.add(ok);
			buttons.add(close);
			v.add(buttons);
			setWidget(v);

		}
	}

	private class deleteDialog extends DialogBox {

		public deleteDialog() {

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

					ClientSideSettings.getConnectedAdmin().removeContactFromContactList(
							globalContactArray.get(publicindex).getBoId(), clArray.get(row).getBoId(),
							new AsyncCallback<Void>() {
								public int boIdvonContact = globalContactArray.get(publicindex).getBoId();
								public int boIdvonCL = clArray.get(row).getBoId();

								public void onFailure(Throwable caught) {
									Window.alert("Ops, da ist etwas schief gelaufen!");
								}

								public void onSuccess(Void result) {
									Window.alert("Kontakt erfolgreich von Kontaktliste entfernt");
									dataProvider.getList().remove(publicindex);
									dataProvider.refresh();

								}

							});

					deleteDialog.this.hide();

				}

			});

			abort.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					deleteDialog.this.hide();
					answer = false;
				}

			});
			HorizontalPanel h = new HorizontalPanel();
			
			h.add(delete);
			h.add(abort);
			v.add(h);
			setWidget(v);

		}

	}

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

					ContactList cl = new ContactList();
					cl.setBoId(clArray.get(row).getBoId());
					ClientSideSettings.getConnectedAdmin().deleteContactList(cl, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Ops, da ist etwas schief gelaufen!");
						}

						@Override
						// jede Kontaktliste wird der ListBox
						// hinzugefügt
						public void onSuccess(Void result) {
							Window.alert("Löschen von " + clArray.get(row).getName() + " war erfolgreich!");
							RootPanel.get("content").clear();
							ContactListForm2 neuLaden = new ContactListForm2(row);

						}

					});
					
					deleteContactListDialog.this.hide();

				}

			});

			abort.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					deleteContactListDialog.this.hide();
					answer = false;
				}

			});
			HorizontalPanel h = new HorizontalPanel();
			
			h.add(delete);
			h.add(abort);
			v.add(h);
			setWidget(v);

		}

	}
	
	
	private class sharePartofClClickhandler implements ClickHandler {

		public void onClick(ClickEvent event) {

		}
		// public void onClick(ClickEvent event) {
		// RootPanel.get("content").clear();
		// RootPanel.get("content").add(topPanel);
		// topPanel.clear();
		//
		// TextColumn<Contact> prenameColumn = new TextColumn<Contact>() {
		// public String getValue(Contact contact) {
		// return contact.getPrename();
		// }
		// };
		// TextColumn<Contact> surnameColumn = new TextColumn<Contact>() {
		// public String getValue(Contact contact) {
		// return contact.getSurname();
		// }
		// };
		//
		// topPanel.add(new HTML("<h2> Kontakte von Kontaktliste " +
		// clArray.get(row).getName() + " auswählen </h2>"));
		// topPanel.add(contacttable2);
		//
		// final MultiSelectionModel<Contact> selectionModel = new
		// MultiSelectionModel<Contact>(Contact.KEY_PROVIDER);
		// contacttable2.setSelectionModel(selectionModel,
		// DefaultSelectionEventManager.<Contact>createCheckboxManager());
		// selectionModel.addSelectionChangeHandler(new Handler() {
		// @Override
		// public void onSelectionChange(SelectionChangeEvent event) {
		// set1 = selectionModel.getSelectedSet();
		//
		// }
		// });
		//
		// Column<Contact, Boolean> checkColumn = new Column<Contact,
		// Boolean>(new CheckboxCell(false, false)) {
		// @Override
		// public Boolean getValue(Contact object) {
		// // Get the value from the selection model.
		// return selectionModel.isSelected(object);
		// }
		// };
		//
		// contacttable2.addColumn(checkColumn,
		// SafeHtmlUtils.fromSafeConstant("<br/>"));
		// contacttable2.setColumnWidth(checkColumn, 40, Unit.PX);
		//
		// contacttable2.addColumn(prenameColumn, "Vorname");
		// contacttable2.addColumn(surnameColumn, "Nachname");
		//
		// // List<Contact> listcontacts = result;
		//
		// dataProvider.getList().clear();
		// dataProvider.getList().addAll(globalContactArray);
		// dataProvider.addDataDisplay(contacttable2);
		//
		//
		//
		// userListbox2.setEnabled(true);
		//
		// // multi auswahl freischalten in ListBox
		// userListbox2.ensureDebugId("cwListBox-multiBox");
		// userListbox2.setVisibleItemCount(7);
		// // Alle Kontaktlisten aus DB abrufen
		//
		// // TODO nur KOntaktlisten des aktuellen Users abrufen!
		// ClientSideSettings.getConnectedAdmin().findAllUser(new
		// AsyncCallback<ArrayList<User>>() {
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// Window.alert("Die User konnten nicht geladen werden");
		// }
		//
		// @Override
		// // jede Kontaktliste wird der ListBox hinzugefügt
		// public void onSuccess(ArrayList<User> result) {
		// publicUserArray = result;
		// for (User u : result) {
		// userListbox2.addItem(u.getLogEmail());
		// }
		//
		// }
		//
		// });
		//
		// topPanel.add(contacttable2);
		// topPanel.add(new HTML("<h2> User auswählen, mit denen die
		// Kontaktliste geteilt werden soll </h2>"));
		//
		// topPanel.add(userListbox2);
		//
		// topPanel.add(shareSeletedContactsButton);
		//
		// set1 = selectionModel.getSelectedSet();
		//
		//
		//
		// }
	};

	private class shareSelectedContactsClickhandler implements ClickHandler {

		public void onClick(ClickEvent event) {

		}
		// public void onClick(ClickEvent event) {
		// //Window.alert(Integer.toString(set1.size()));
		//
		// for (int i = 0; i < userListbox2.getItemCount(); i++) {
		// if (userListbox2.isItemSelected(i)) {
		// uArray.add(publicUserArray.get(i));
		// }
		// }
		// //cArray = (ArrayList<Contact>) set1;
		// for(Contact c : set1)
		// {
		// cArray.add(c);
		// }
		//
		//
		// try{
		// ClientSideSettings.getConnectedAdmin().giveContactPermissonToUsers(cArray,
		// uArray, 1, new AsyncCallback<Void>() {
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// Window.alert("Ops, da ist etwas schief gelaufen!");
		// }
		//
		// @Override
		// // jede Kontaktliste wird der ListBox hinzugefügt
		// public void onSuccess(Void result) {
		// Window.alert("Alle Kontakte erfolgreich geteilt!");
		// //Window.alert(Integer.toString(cArray.size()));
		// //Window.alert(Integer.toString(uArray.size()));
		// Window.Location.reload();
		// }
		//
		// });}
		// catch (Exception e)
		// {
		// Window.alert(e.toString());
		// e.printStackTrace(); }
		//
		// }
		//
		// ContactListForm2 s12= new ContactListForm2();
	};

};
