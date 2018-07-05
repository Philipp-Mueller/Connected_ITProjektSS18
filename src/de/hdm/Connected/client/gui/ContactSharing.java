package de.hdm.Connected.client.gui;

import java.awt.Dialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SelectionChangeEvent.HasSelectionChangedHandlers;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Permission;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

public class ContactSharing extends PopupPanel {

	private ListDataProvider<Entry<Property, Value>> dataProvider = new ListDataProvider<Entry<Property, Value>>();
	private ListDataProvider<User> userDataProvider = new ListDataProvider<User>();
	private ListDataProvider<User> receiverUserDataProvider = new ListDataProvider<User>();
	private CellTable<Entry<Property, Value>> propertyValueTable = new CellTable<Entry<Property, Value>>();
	private CellTable<User> usersWithPermission = new CellTable<User>();
	private CellTable<User> receiverUser = new CellTable<User>();
	private Set<Entry<Property, Value>> selectedSet = new HashSet<Entry<Property, Value>>();
	List<Entry<Property, Value>> propertiesAndValues = new ArrayList<Entry<Property, Value>>();
	final MultiSelectionModel<Entry<Property, Value>> selectionModel = new MultiSelectionModel<Entry<Property, Value>>();
	final SingleSelectionModel<User> selectionModel_Single = new SingleSelectionModel<User>();
	ArrayList<User> allUsers = null;
	ArrayList<User> permissionUser = null;
	private User changingUser =  null;
	final ListBox userListBox = new ListBox(true);
	VerticalPanel root = new VerticalPanel();
	VerticalPanel boxPanel = new VerticalPanel();
	HorizontalPanel horizontal = new HorizontalPanel();
	Button shareButton = new Button("Kontakt teilen");
	Button closeButton = new Button("Abbrechen");
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private String selectedUser;
	private Button addButton = new Button("+");
	private Label instruction = new Label();
	
	private Label selectValues = new Label();
	private Label changeValues = new Label();
	// Map<Property, Value> propertyValueMap = new HashMap<Property, Value>();

	public ContactSharing(final Contact sharingContact) {
		this.setAnimationEnabled(true);
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Popup schließen bei Betägigung des Buttons
				center();
				hide();

			}
		});

		receiverUser.setVisible(false);
		selectValues.setVisible(false);
		changeValues.setVisible(false);
		propertyValueTable.setVisible(false);
		root.add(new HTML("<h3> Kontakt <i>" + sharingContact.getPrename() + " " + sharingContact.getSurname()
				+ "</i> teilen</h3><br />"));
		root.add(new HTML("<hr><br />"));
		
		instruction.getElement().setInnerHTML("Bitte einen User auswählen um dessen Berechtigung zu bearbeiten oder <br> rechts einen User eingeben um den Kontakt mit diesem zu teilen.");
		
		root.add(instruction);
		root.add(new HTML("<br />"));

		// root.add(new HTML("Kontakt bereits geteilit mit: <br />"));

		// Anzeigen der User, die bereits Zugriff auf diesen Kontakt haben
		ClientSideSettings.getConnectedAdmin().getPermissionsBySharedObjectId(sharingContact.getId(),
				new AsyncCallback<ArrayList<Permission>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Leider konnten die Permissions des Kontakts nicht abgefragt werden");
					}

					@Override
					public void onSuccess(ArrayList<Permission> result) {
						permissionUser = new ArrayList<User>();
						// User der Permissions abrufen
						for (Permission p : result) {
							ClientSideSettings.getConnectedAdmin().findUserById(p.getReceiverUserID(),
									new AsyncCallback<User>() {

										@Override
										public void onFailure(Throwable caught) {
											Window.alert("Leider konnte der User nicht abgefragt werden");

										}

										@Override
										public void onSuccess(User result) {
											permissionUser.add(result);

										}

									});
						}
						TextColumn<User> nameColumn = new TextColumn<User>() {
							public String getValue(User user) {
								return user.getLogEmail();
							}
						};

						ClickableTextCell deleteButton = new ClickableTextCell() {
							@Override
							public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
								String title = "Berechtigung löschen";
								if (data != null) {
									sb.appendHtmlConstant("<img title='" + title + "' src=" + "/delete.png" + " alt="
											+ "Kontakt löschen" + " height=" + "18" + " width=" + "18" + ">");

								}
							}
						};

						Column<User, String> deleteColumn = new Column<User, String>(deleteButton) {
							public String getValue(User object) {
								return "";
							}
						};
						deleteColumn.setCellStyleNames("iconButton");
						deleteColumn.setFieldUpdater(new FieldUpdater<User, String>() {

							@Override
							public void update(int index, final User object, String value) {
								// DialogBox anzeigen ob man diesen User wirklich die Berechtigungen entziehen möchte.
								
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
								yesButton.addClickHandler(new ClickHandler() {
									@Override
									public void onClick(ClickEvent event) {
										
										ClientSideSettings.getConnectedAdmin().deletePermissionFromContact(object.getId(), sharingContact.getId(), new AsyncCallback<Void>(){

											@Override
											public void onFailure(Throwable caught) {
												Window.alert("Permisson konnte nicht gelöscht werden");
												
											}

											@Override
											public void onSuccess(Void result) {
												Window.alert("Berechtigung wurde " + object.getLogEmail() + " entzogen");
												userDataProvider.getList().remove(object);
												usersWithPermission.redraw();
												agreeDelete.hide();
											}
											
										});
										
										
										
									}
								});
								vpanel.add(new HTML("Wollen Sie diesen User die Berechtigungen für den Kontakt entziehen?"));
								buttonPanel.add(noButton);
								buttonPanel.add(yesButton);
								vpanel.add(buttonPanel);
								agreeDelete.setWidget(vpanel);
								agreeDelete.setGlassEnabled(true);
								agreeDelete.center();
								agreeDelete.show();
							}
						});

							
						

						usersWithPermission.addColumn(nameColumn, "Bereits geteilt mit:");
						usersWithPermission.addColumn(deleteColumn);

					}

				});

		loadAllUser();

		suggestBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				selectedUser = ((SuggestBox) event.getSource()).getText();
			}
		});

		suggestBox.getElement().getStyle().setMarginLeft(20, Unit.PX);

		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				usersWithPermission.setVisible(false);
				changeValues.setVisible(false);
				selectValues.setVisible(true);
				receiverUser.setVisible(true);
				if(!propertyValueTable.isVisible()){
					propertyValueTable.setVisible(true);
				};
				shareButton.setText("Kontakt teilen");
				selectionModel.clear();
				dataProvider.getList().clear();
				dataProvider.getList().addAll(propertiesAndValues);
				propertyValueTable.redraw();

				ClientSideSettings.getConnectedAdmin().findUserByEmail(suggestBox.getText(), new AsyncCallback<User>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("User wurde nicht gefunden");

					}

					@Override
					public void onSuccess(User result) {
						// receiverUserData.add(result);
						suggestBox.setText("");
						receiverUserDataProvider.getList().add(result);
						receiverUserDataProvider.addDataDisplay(receiverUser);

					}

				});

			}

		});

		TextColumn<User> receiveNameColumn = new TextColumn<User>() {
			public String getValue(User user) {
				return user.getLogEmail();
			}
		};

		ClickableTextCell deleteButton = new ClickableTextCell() {
			@Override
			public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
				String title = "Kontakt entfernen";
				if (data != null) {
					sb.appendHtmlConstant("<img title='" + title + "' src=" + "/delete.png" + " alt="
							+ "Kontakt löschen" + " height=" + "18" + " width=" + "18" + ">");

				}
			}
		};

		Column<User, String> reiceiveDeleteColumn = new Column<User, String>(deleteButton) {
			public String getValue(User object) {
				return "";
			}
		};
		reiceiveDeleteColumn.setCellStyleNames("iconButton");
		reiceiveDeleteColumn.setFieldUpdater(new FieldUpdater<User, String>() {

			@Override
			public void update(int index, final User object, String value) {
				// DialogBox anzeigen ob man diesen Kontakt wirklich aus der
				// Liste entfernen will.
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
				yesButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						receiverUserDataProvider.getList().remove(object);
						receiverUser.redraw();
						if(receiverUserDataProvider.getList().size() == 0){
							receiverUser.setVisible(false);
							selectValues.setVisible(false);
							changeValues.setVisible(true);
							usersWithPermission.setVisible(true);
						}
						agreeDelete.hide();
						
					}
				});
				vpanel.add(new HTML("Wollen Sie diesen User aus der Liste entfernen?"));
				buttonPanel.add(noButton);
				buttonPanel.add(yesButton);
				vpanel.add(buttonPanel);
				agreeDelete.setWidget(vpanel);
				agreeDelete.setGlassEnabled(true);
				agreeDelete.center();
				agreeDelete.show();
			}
		});

		receiverUser.addColumn(receiveNameColumn, "Neu teilen mit:");
		receiverUser.addColumn(reiceiveDeleteColumn);
		receiverUserDataProvider.addDataDisplay(receiverUser);

		

		usersWithPermission.setSelectionModel(selectionModel_Single);
		


		selectionModel_Single.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			//SelectionModel_single SelectionChange Handler
			public void onSelectionChange(SelectionChangeEvent event) {
							
				changingUser = selectionModel_Single.getSelectedObject();
				dataProvider.getList().clear();
				dataProvider.getList().addAll(propertiesAndValues);
			    propertyValueTable.redraw();
				//Aktionenn bei anwählen eines Eintrag der Liste.
				if (changingUser != null) {
					shareButton.setText("Teilhaberschaft ändern");
					ClientSideSettings.getConnectedAdmin().findValueAndProperty(sharingContact.getId(),
							changingUser.getId(), new AsyncCallback<Map<Property, Value>>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Ausprägungen und Eigenschaften konnten nicht geladen werden");

								}

								@Override
								public void onSuccess(Map<Property, Value> result) {
									selectionModel.clear();
									receiverUser.setVisible(false);
									changeValues.setVisible(true);
									propertyValueTable.setVisible(true);
									for (Map.Entry<Property, Value> entry : result.entrySet()) {
										if (changingUser.getId() == entry.getValue().getCreatorId()){
											dataProvider.getList().remove(entry);
											propertyValueTable.redraw();
										}else{
										
										for (int i = 0; i < propertiesAndValues.size(); i++) {
											if (entry.getValue().getId() == propertiesAndValues.get(i).getValue()
													.getId()) {
												selectionModel.setSelected(propertiesAndValues.get(i), true);
											}
											
										}
										}
									}

								}

							});

				}
				//Wenn Auwahl durch Ctrl+Click aufgehoben wird, werden auch alle Eigenschaften deselected.
				if(changingUser == null){
					selectionModel.clear();
				}
				
			}
		});
		//alle Eigenschaft auf der der aktuelle Nutzer zugriff hat auswählen
		ClientSideSettings.getConnectedAdmin().findValueAndProperty(sharingContact.getId(), ClientSideSettings.getCurrentUser().getId(),
				new AsyncCallback<Map<Property, Value>>() {

					@Override
					public void onFailure(Throwable caught) {


					}

					@Override
					public void onSuccess(Map<Property, Value> result) {
						// List für DataProvider mit den Properties und Values

						for (Map.Entry<Property, Value> entry : result.entrySet()) {
							propertiesAndValues.add(entry);
						}

						TextColumn<Entry<Property, Value>> propertyColumn = new TextColumn<Entry<Property, Value>>() {
							public String getValue(Entry<Property, Value> property) {
								return property.getKey().getName();
							}
						};
						TextColumn<Entry<Property, Value>> valueColumn = new TextColumn<Entry<Property, Value>>() {
							public String getValue(Entry<Property, Value> value) {
								return value.getValue().getName();
							}
						};

						propertyValueTable.setSelectionModel(selectionModel,
								DefaultSelectionEventManager.<Entry<Property, Value>>createCheckboxManager());

						selectionModel.addSelectionChangeHandler(new Handler() {
							@Override
							public void onSelectionChange(SelectionChangeEvent event) {
								selectedSet = selectionModel.getSelectedSet();

							}
						});

						Column<Entry<Property, Value>, Boolean> checkColumn = new Column<Entry<Property, Value>, Boolean>(
								new CheckboxCell(false, false)) {
							@Override
							public Boolean getValue(Entry<Property, Value> object) {
								// Get the value from the selection model.
								return selectionModel.isSelected(object);
							}
						};

						propertyValueTable.addColumn(checkColumn, "Eigenschaft teilen");
						propertyValueTable.setColumnWidth(checkColumn, 40, Unit.PX);

						propertyValueTable.addColumn(propertyColumn, "Eigenschaft");
						propertyValueTable.addColumn(valueColumn, "Ausprägung");

						dataProvider.getList().clear();
						dataProvider.getList().addAll(propertiesAndValues);
						dataProvider.addDataDisplay(propertyValueTable);

						// multi auswahl freischalten in ListBox
						userListBox.ensureDebugId("cwListBox-multiBox");
						userListBox.setVisibleItemCount(7);

						userDataProvider.getList().clear();
						userDataProvider.getList().addAll(permissionUser);
						userDataProvider.addDataDisplay(usersWithPermission);

						horizontal.getElement().getStyle().setMarginBottom(30, Unit.PX);

						horizontal.add(usersWithPermission);
						horizontal.add(receiverUser);
						horizontal.add(boxPanel);
						Label add = new Label("zusätzlich teilen mit: ");
						add.getElement().getStyle().setMarginLeft(30, Unit.PX);
						boxPanel.add(add);
						HorizontalPanel textPanel = new HorizontalPanel();
						boxPanel.add(textPanel);
						textPanel.add(suggestBox);
						textPanel.add(addButton);

						root.add(horizontal);
						changeValues.getElement().setInnerHTML("Bitte unten die Eigenschaftsberechtigungen ändern.<br />");
						selectValues.getElement().setInnerHTML("Bitte wählen Sie die Eigenschaften aus, die Sie teilen möchten:<br />");
						root.add(selectValues);
						root.add(changeValues);
						root.add(propertyValueTable);

						
						shareButton.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								
							    if(shareButton.getText().equals("Kontakt teilen")){
							    	
								Window.alert(Integer.toString(selectedSet.size()));
								ArrayList<Integer> selectedValues = new ArrayList<Integer>();
								ArrayList<Integer> selectedUsers = new ArrayList<Integer>();
								selectedValues.add(sharingContact.getId());
								Iterator<Entry<Property, Value>> iterator = selectedSet.iterator();

								while (iterator.hasNext()) {
									Map.Entry<Property, Value> entry = iterator.next();
									selectedValues.add(entry.getValue().getId());
								}

								for (int i = 0; i < receiverUser.getVisibleItems().size(); i++) {
									selectedUsers.add(receiverUser.getVisibleItems().get(i).getId());
								}


								ClientSideSettings.getConnectedAdmin().createPermission(ClientSideSettings.getCurrentUser().getId(), selectedValues,
										selectedUsers, new AsyncCallback<Void>() {

											@Override
											public void onFailure(Throwable caught) {
												Window.alert("Der Kontakt konnte nicht geteilt werden");
											}

											@Override
											public void onSuccess(Void result) {
												Window.alert("Der Kontakt wurde geteilt");
											}

										});

							} else {
								ArrayList<Integer> newPermissions = new ArrayList<Integer>();
								for(Entry<Property,Value> entry : selectedSet){
									newPermissions.add(entry.getValue().getId());
								}
								
								ClientSideSettings.getConnectedAdmin().updatePermissionsForUser(newPermissions, sharingContact.getId(), changingUser.getId(),new AsyncCallback<Void>(){

									@Override
									public void onFailure(Throwable caught) {
										Window.alert("Konnte Berechtigungen nicht aktualisieren");
										
									}

									@Override
									public void onSuccess(Void result) {
										Window.alert("Berechtigungen wurden aktualisiert");
										hide();
										
									}
									
								});
								
								
								
							}
							    
							}	    
						});
						root.add(shareButton);
						root.add(closeButton);
						setWidget(root);

					}

				});

	}

	private void loadAllUser() {
		ClientSideSettings.getConnectedAdmin().findAllUser(new AsyncCallback<ArrayList<User>>() {

			@Override
			public void onFailure(Throwable caught) {


			}

			@Override
			public void onSuccess(ArrayList<User> result) {
				oracle.clear();
				for (User u : result) {
					// if(u.getBoId() !=
					// ClientSideSettings.getCurrentUser().getBoId()){
					oracle.add(u.getLogEmail());
					// }
				}

			}

		});
	}

	public ContactSharing(Contact contact, Value value) {

		MyDialog shareNewValue = new MyDialog(contact, value);
		shareNewValue.center();

	}

	private class MyDialog extends DialogBox {

		public MyDialog(final Contact contact, final Value value) {
			final ArrayList<User> permissionUser = new ArrayList<User>();
			VerticalPanel v = new VerticalPanel();
			HorizontalPanel h = new HorizontalPanel();
			// Set the dialog box's caption.
			setText("Neue Eigenschaft für Kontakt " + contact.getPrename() + " " + contact.getSurname()
					+ " an Teilhaber teilen?");

			// Enable animation.
			setAnimationEnabled(true);

			// Enable glass background.
			setGlassEnabled(true);

			final ListBox userPermissionList = new ListBox(true);

			Button noButton = new Button("Nein");
			noButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Window.alert("Neue Eigenschaft wurde gespeichert!");
					MyDialog.this.hide();
					// RootPanel.get("content").clear();
					// ContactForm contactForm = new ContactForm(contact);
				}
			});

			Button yesButton = new Button("Ja, ausgewählten Kontakten teilen");
			yesButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					final ArrayList<User> userArray = new ArrayList<User>();
					for (int i = 0; i < userPermissionList.getItemCount(); i++) {
						if (userPermissionList.isItemSelected(i)) {
							for (User u : permissionUser) {
								if (u.getLogEmail().equals(userPermissionList.getItemText(i))) {
									userArray.add(u);
								}
							}
						}
					}

					ClientSideSettings.getConnectedAdmin().givePermissionToUsers(value.getId(), userArray, ClientSideSettings.getCurrentUser().getId(),
							new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {


								}

								@Override
								public void onSuccess(Void result) {
									Window.alert(Integer.toString(userArray.size()));
									Window.alert("Die Eigenschaft wurde erstellt und geteilt");
									MyDialog.this.hide();
									// RootPanel.get("content").clear();
									ContactForm contactForm = new ContactForm(contact, null, null);

								}

							});
				}
			});

			userPermissionList.ensureDebugId("cwListBox-multiBox");
			userPermissionList.setVisibleItemCount(7);

			v.add(new HTML(
					"Dieser Kontakt wurde schon anderen User geteilt, wollen Sie diese neu erstellte Eigenschaft direkt an einen dieser User teilen?"));

			ClientSideSettings.getConnectedAdmin().getPermissionsBySharedObjectId(contact.getId(),
					new AsyncCallback<ArrayList<Permission>>() {

						@Override
						public void onFailure(Throwable caught) {

						}

						@Override
						public void onSuccess(ArrayList<Permission> result) {

							for (Permission p : result) {
								ClientSideSettings.getConnectedAdmin().findUserById(p.getReceiverUserID(),
										new AsyncCallback<User>() {

											@Override
											public void onFailure(Throwable caught) {
												Window.alert("Leider konnte der User nicht gefunden werden.");

											}

											@Override
											public void onSuccess(User result) {
												userPermissionList.addItem(result.getLogEmail());
												permissionUser.add(result);
											}

										});
							}

						}

					});

			v.add(userPermissionList);
			v.add(h);
			h.add(yesButton);
			h.add(noButton);
			setWidget(v);
			this.show();

		}
	}
}
