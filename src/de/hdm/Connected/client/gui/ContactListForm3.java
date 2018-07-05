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
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dev.shell.Icons;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.ConnectedAdminAsync;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.ContactList;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

public class ContactListForm3 extends CellTable {
	
	private ConnectedAdminAsync connectedAdmin = ClientSideSettings.getConnectedAdmin();
	
	//private HorizontalPanel hPanel = new HorizontalPanel();
	
	private CellTable<Contact> cellTable = new CellTable<Contact>();
	private Map<Property, Value> propertyValueMap = null;
	
	ListDataProvider<Contact> dataProvider = new ListDataProvider<Contact>();
	
	Button newContactListButton = new Button("Neue Kontaktliste erstellen", new newContactListClickhandler());
	Button shareContactListButton = new Button("<img border='0' src='share.png' width = '25' length = '25'/>");
	Button updateContactListButton = new Button("<img border='0' src='edit.png' width = '25'  length = '25'/>");
	Button deleteContactListButton = new Button("<img border='0' src='delete.png' width = '25' length = '25'/>");
	HorizontalPanel buttonPanel = new HorizontalPanel();
	Label nameLabel = new Label();
	final ListBox userListbox = new ListBox(true);
	ArrayList<User> uArray = new ArrayList<User>();
	ArrayList<User> publicUserArray = null;

	
	List<Contact> contacts = new ArrayList<Contact>();
	SimplePager pager;
	String imageHtml = "<img src=" + "Trash_Can.png" + " alt=" + "Kontakt löschen" + ">";
	boolean buttonPressed;
	ContactList mainContactlist = new ContactList();
	
	public ContactListForm3(final ContactList cl) {
		// Create a Pager to control the table.
		
		mainContactlist = cl;
				SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
				pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
				pager.setDisplay(cellTable);		
		
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		
		
//		ClientSideSettings.getConnectedAdmin().findAllContactlists( new AsyncCallback<ArrayList<ContactList>>(){
//			@Override
//			public void onFailure(Throwable caught){
//				Window.alert("Carmen mag das nicht");
//			}
//			public void onSuccess(ArrayList<ContactList> result){
//
//				for(int i = 0; i<result.size(); i++){
//
//					if(contaclistid == result.get(i).getBoId()){
//						mainContactlist.setBoId(contaclistid);
//						mainContactlist.setName(result.get(i).getName());
//					}
//				}
//			}
//		});
		
		
		
		ClientSideSettings.getConnectedAdmin().findContactsByContactListId(mainContactlist.getBoId(), new AsyncCallback<ArrayList<Contact>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<Contact> result) {
				
				ContactsTable load = new ContactsTable(result, mainContactlist);
				
//				
//				contacts = result;
//				
//			ClickableTextCell prenameCell = new ClickableTextCell();	
//				
//		Column<Contact, String> prenameColumn = new Column<Contact, String> (prenameCell) {
//			
//			 public String getValue(Contact object) {
//				    return object.getPrename();
//				  }
//			
//			};
//			
//			prenameColumn.setSortable(true);
//			
//			prenameColumn.setFieldUpdater(new FieldUpdater<Contact, String>(){
//
//				@Override
//				public void update(int index, final Contact object, String value) {
//					// TODO Auto-generated method stub
//					
//					ClientSideSettings.getConnectedAdmin().findValueAndProperty(object.getBoId(), 2, 
//							new AsyncCallback<Map<Property, Value>>() {
//
//								public void onFailure(Throwable caught) {
//									Window.alert("Ops, da ist etwas schief gelaufen!");
//								}
//
//								public void onSuccess(Map<Property, Value> result) {
//									propertyValueMap = result;
//									// Window.alert(Integer.toString(result.size()));
//									// Window.alert(Integer.toString(globalIndex));
////									ContactInfoForm showContact = new ContactInfoForm(object, result);
////
////									showContact.center();
////									showContact.show();
//
//								}
//
//							});
//					
//				}
//				
//			});
//			
//			cellTable.addColumn(prenameColumn, "Vorname");
//			
//			ClickableTextCell surnameCell = new ClickableTextCell();	
//			
//			Column<Contact, String> surnameColumn = new Column<Contact, String>(surnameCell) {
//				public String getValue(Contact contact) {
//					return contact.getSurname();
//				}
//			};
//			
//			surnameColumn.setSortable(true);
//			
//			surnameColumn.setFieldUpdater(new FieldUpdater<Contact, String>(){
//
//				@Override
//				public void update(int index, final Contact object, String value) {
//					// TODO Auto-generated method stub
//					
//					ClientSideSettings.getConnectedAdmin().findValueAndProperty(object.getBoId(), 2, 
//							new AsyncCallback<Map<Property, Value>>() {
//
//								public void onFailure(Throwable caught) {
//									Window.alert("Ops, da ist etwas schief gelaufen!");
//								}
//
//								public void onSuccess(Map<Property, Value> result) {
//									propertyValueMap = result;
//									// Window.alert(Integer.toString(result.size()));
//									// Window.alert(Integer.toString(globalIndex));
////									ContactInfoForm showContact = new ContactInfoForm(object, result);
////
////									showContact.center();
////									showContact.show();
//
//								}
//
//							});
//					
//				}
//				
//			});
//			cellTable.addColumn(surnameColumn, "Nachname");	
//			
//			ClickableTextCell shareButton = new ClickableTextCell(){
//				 @Override
//					public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
//					 String title = "Kontakt teilen";
//				           if (data != null) {
//				               sb.appendHtmlConstant("<img title='" + title + "' src=" + "/share.png" + " alt=" + "Kontakt bearbeiten" + " height=" +"25"+ " width=" + "25"+">");
//				                	             
//				            }
//					 }
//			};
//			
//			Column<Contact,String> shareColumn = new Column<Contact,String> (shareButton) {
//				  public String getValue(Contact object) {
//				    return "";
//				  }
//				};
//				
//		    shareColumn.setCellStyleNames("iconButton");
//			shareColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {
//				@Override
//				public void update(int index, Contact object, String value) {
//					buttonPressed = true;
//					Window.alert("Hallooo");
//					
//					
//				}		
//			});
//			
//			
//			cellTable.addColumn(shareColumn);
//			
//			
//			
//		ClickableTextCell updateButton = new ClickableTextCell(){
//			 @Override
//				public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
//				 String title = "Kontakt bearbeiten";
//			           if (data != null) {
//			               sb.appendHtmlConstant("<img title='" + title + "'src=" + "/edit.png" + " alt=" + "Kontakt bearbeiten" + " height=" +"25"+ " width=" + "25"+">");
//			                	             
//			            }
//				 }
//		};
//		
//		Column<Contact,String> updateColumn = new Column<Contact,String> (updateButton) {
//			  public String getValue(Contact object) {
//			    return "";
//			  }
//			};
//			
//	    updateColumn.setCellStyleNames("iconButton");
//	    updateColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {
//			@Override
//			public void update(int index, Contact object, String value) {
//				buttonPressed = true;
//				//Window.alert("Hallooo");
//				final ContactForm updatePopUp = new ContactForm(object);
//
//				// Enable glass background.
//				updatePopUp.setGlassEnabled(true);				
//				//updatePopUp.setPopupPosition(200, 300);
//				 updatePopUp.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
//
//		                public void setPosition(int offsetWidth, int offsetHeight) {
//		                    // TODO Auto-generated method stub
//		                    int left = (Window.getClientWidth() - offsetWidth) / 3;
//		                    int top = (Window.getClientHeight() - offsetHeight) / 3;
//
//		                    updatePopUp.setPopupPosition(left, top);
//		                }
//		            });
//
//				updatePopUp.show();
//			
//				
//				  
//			
//			}		
//		});
//		
//		
//		cellTable.addColumn(updateColumn);
//	
//		ClickableTextCell deleteButton = new ClickableTextCell(){
//			 @Override
//			public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
//				 String title = "Kontakt löschen";
//		           if (data != null) {
//		               sb.appendHtmlConstant("<img title='" + title + "' src=" + "/delete.png" + " alt=" + "Kontakt löschen" + " height=" +"25"+ " width=" + "25"+">");
//		                	             
//		            }
//			 }
//			 };
//			 
//		
//		Column<Contact,String> deleteColumn = new Column<Contact,String> (deleteButton) {
//			  public String getValue(Contact object) {
//			    return "";
//			  }
//			};
//		deleteColumn.setCellStyleNames("iconButton");
//		
//		//Das brauch ich
//		deleteColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {
//
//			@Override
//			public void update(int index, final Contact object, String value) {
//				// TODO Auto-generated method stub
//				//Clickhandler
//				User user = new User();
//				user.setBoId(2);
//				buttonPressed = true;
//				
//				deleteDialog deleteD = new deleteDialog(index);
//				deleteD.center();
//				deleteD.show();
//				
////				ClientSideSettings.getConnectedAdmin().deleteContact(object, user, new AsyncCallback<Void>(){
////
////					@Override
////					public void onFailure(Throwable caught) {
////						// TODO Auto-generated method stub
////						
////					}
////
////					@Override
////					public void onSuccess(Void result) {
////						// TODO Auto-generated method stub
////						for(int i=0; i<contacts.size(); i++){
////							if(contacts.get(i).getBoId() == object.getBoId()){
////								contacts.remove(i);
////							}
////						}
////						dataProvider.getList().clear();
////						dataProvider.getList().addAll(contacts);
////						Window.alert("Kontakt " + object.getPrename() + " " + object.getSurname() + " wurde gelöscht");
////					}
////					
////				});
//				
//				
//			}		
//		});
//		
//		
//		cellTable.addColumn(deleteColumn);
//		dataProvider.getList().clear();
//		dataProvider.addDataDisplay(cellTable);
//		
//		
//	/*	final SingleSelectionModel<Contact> selectionModel = new SingleSelectionModel<Contact>();
//		cellTable.setSelectionModel(selectionModel);
//		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
//			//final PopupPanel contactPopup = new PopupPanel(true, false);
//				public void onSelectionChange(SelectionChangeEvent event) {
//				final Contact selected = selectionModel.getSelectedObject();
//				if (selected != null && !buttonPressed) {
//				//Window.alert("You selected: " + selected.prename + " " + selected.surname);
//					
//					ClientSideSettings.getConnectedAdmin().findValueAndProperty(selected.getBoId(),
//							new AsyncCallback<Map<Property, Value>>() {
//
//								public void onFailure(Throwable caught) {
//									Window.alert("Ops, da ist etwas schief gelaufen!");
//								}
//
//								public void onSuccess(Map<Property, Value> result) {
//									propertyValueMap = result;
//									// Window.alert(Integer.toString(result.size()));
//									// Window.alert(Integer.toString(globalIndex));
//									ShowContactInfo_Dialog showContact = new ShowContactInfo_Dialog(selected);
//
//									showContact.center();
//									showContact.show();
//
//								}
//
//							});
//					
//					
//				final Anchor selectedContact = new Anchor(selected.getPrename() + selected.getSurname());
//				
//				     
//				HorizontalPanel contactPopupContainer = new HorizontalPanel();
//				contactPopupContainer.setSpacing(10);
//				final HTML contactInfo = new HTML();
//				contactPopupContainer.add(contactInfo);
//				
//				///contactPopup.setWidget(contactPopupContainer);
//
//				contactInfo.setHTML("Vorname: "+ selected.getPrename() + "<br>" + "Nachname: " + selected.getSurname() + "</i>");
//
//			//  int left = selectedContact.getAbsoluteLeft() + 86;
//			//  int top = selectedContact.getAbsoluteTop() + 45;
//		      //contactPopup.setPopupPosition(left, top);
//			//	contactPopup.show();
//			} else if (buttonPressed) {buttonPressed = false;}
//		} 
//   }); */
//		
//		 	List<Contact> list = dataProvider.getList();
//		    for (Contact Contact : contacts) {
//		      list.add (Contact);
//		    }
//		
//		    ListHandler<Contact> columnSortHandler = new ListHandler<Contact>(list);
//		    
//		    columnSortHandler.setComparator(prenameColumn,
//		        new Comparator<Contact>() {
//		          public int compare(Contact c1, Contact c2) {
//		            if (c1 == c2) {
//		              return 0;
//		            }
//
//		            if (c1 != null) {
//		              return (c2 != null) ? c1.getPrename().compareTo(c2.getPrename()) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    
//		    cellTable.addColumnSortHandler(columnSortHandler);
//		    
//		    ListHandler<Contact> columnSortHandler2 = new ListHandler<Contact>(list);
//		    
//		    columnSortHandler2.setComparator(surnameColumn,
//		        new Comparator<Contact>() {
//		          public int compare(Contact c1, Contact c2) {
//		            if (c1 == c2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (c1 != null) {
//		              return (c2 != null) ? c1.getSurname().compareTo(c2.getSurname()) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    
//		    cellTable.addColumnSortHandler(columnSortHandler2);
//
//		    cellTable.getColumnSortList().push(prenameColumn);
//
//		cellTable.setRowCount(contacts.size(), true);
//		cellTable.setRowData(0, contacts);
//		cellTable.setWidth("70%");
//
//		buttonPanel.setWidth("100%");
//		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//		
//		buttonPanel.add(new HTML("<h2> Kontaktliste: " +  mainContactlist.getName()+ "</h2>"));
//		buttonPanel.add(newContactListButton);
//		//shareContactListButton.setHTML(("<img border='0' src='share.png' />"));
//		shareContactListButton.addClickHandler(new shareCotactListClickhandler());
//		updateContactListButton.addClickHandler(new updateContactListClickhandler());
//		deleteContactListButton.addClickHandler(new deleteContactListClickhandler());
//		buttonPanel.add(shareContactListButton);
//		buttonPanel.add(updateContactListButton);
//		buttonPanel.add(deleteContactListButton);
//
//		RootPanel.get("content").add(buttonPanel);
//		RootPanel.get("content").add(cellTable);
//		RootPanel.get("content").add(pager);
//	
//		//cellTablePanel.setCellHorizontalAlignment(pager,HasHorizontalAlignment.ALIGN_CENTER);

	}

				
	});

}
	//Clickhandler

	private class newContactListClickhandler implements ClickHandler {

		public void onClick(ClickEvent event) {

			NewContactListPopup newi = new NewContactListPopup();
			newi.center();
			newi.show();

		}
	};
	
	private class shareCotactListClickhandler implements ClickHandler {

		public void onClick(ClickEvent event) {

			shareDialog dia = new shareDialog();
			dia.center();
			dia.show();


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

			ClientSideSettings.getConnectedAdmin().givePermissionToUsers(mainContactlist.getBoId(), uArray, ClientSideSettings.getCurrentUser().getBoId(),
					new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Ops, da ist etwas schief gelaufen!");
						}

						@Override
						// jede Kontaktliste wird der ListBox
						// hinzugefügt
						public void onSuccess(Void result) {
							Window.alert("Teilen von " + mainContactlist.getName() + " war erfolgreich!");
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

		}
	}

	private class updateContactListClickhandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			updateDialog dia = new updateDialog();
			dia.center();
			dia.show();
		}
	}
	
	
	//Callbacks
	
	private class createContactListCallback implements AsyncCallback<ContactList> {

		public void onFailure(Throwable caught) {
			Window.alert("Ops, da ist etwas schief gelaufen!");
		}

		public void onSuccess(ContactList result) {
			Window.alert("Contactlist erfolgreich angelegt!");
			// RootPanel.get("content").clear();

		}

	}
	
	//Dialogs
	
	private class MyDialog extends DialogBox {

		public MyDialog() {
			// Set the dialog box's caption.
//			setText(contacts.get(globalIndex).getPrename() + " "
//					+ contacts.get(globalIndex).getSurname());

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
			setText("Kontaktliste " + mainContactlist.getName() + " umbenennen:");

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
							Navigation reloadN = new Navigation();
							RootPanel.get("nav").add(reloadN);
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
			setText("Kontaktliste" + mainContactlist.getName() + "teilen:");

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

					ClientSideSettings.getConnectedAdmin().givePermissionToUsers(mainContactlist.getBoId(), uArray, ClientSideSettings.getCurrentUser().getBoId(),
							new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Ops, da ist etwas schief gelaufen!");
								}

								@Override
								// jede Kontaktliste wird der ListBox
								// hinzugefügt
								public void onSuccess(Void result) {
									Window.alert("Teilen von " + mainContactlist.getName() + " war erfolgreich!");
									
									RootPanel.get("contant").clear();
									ContactListForm3 reload = new ContactListForm3(
											mainContactlist);
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

					ClientSideSettings.getConnectedAdmin().createContactList(nameTextBox.getText(), ClientSideSettings.getCurrentUser().getBoId(),
							new AsyncCallback<ContactList>() {
								@Override
								public void onFailure(Throwable caught) {

								}

								@Override
								public void onSuccess(ContactList result) {
									Window.alert("Erfolgreich erstellt");
									RootPanel.get("content").clear();
									ContactListForm3 reload = new ContactListForm3(result);
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

			delete.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {

					ClientSideSettings.getConnectedAdmin().removeContactFromContactList(
							contacts.get(index).getBoId(), mainContactlist.getBoId(),
							new AsyncCallback<Void>() {
								public int boIdvonContact = contacts.get(index).getBoId();
								public int boIdvonCL = mainContactlist.getBoId();

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
					cl.setBoId(mainContactlist.getBoId());
					ClientSideSettings.getConnectedAdmin().deleteContactList(cl, new AsyncCallback<Void>() {

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
							//ContactListForm2 neuLaden = new ContactListForm2(mainContactlist.getBoId());

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