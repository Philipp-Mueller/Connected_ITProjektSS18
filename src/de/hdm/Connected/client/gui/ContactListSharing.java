package de.hdm.Connected.client.gui;

import java.util.ArrayList;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
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
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.bo.ContactList;
import de.hdm.Connected.shared.bo.Permission;
import de.hdm.Connected.shared.bo.User;

public class ContactListSharing  extends PopupPanel {
	
	private ListDataProvider<User> userDataProvider = new ListDataProvider<User>();
	private ListDataProvider<User> receiverUserDataProvider = new ListDataProvider<User>();
	private CellTable<User> usersWithPermission = new CellTable<User>();
	private CellTable<User> receiverUser = new CellTable<User>();
   
	final SingleSelectionModel<User> selectionModel_Single = new SingleSelectionModel<User>();
	private ArrayList<User> permissionUser = new ArrayList<User>();
	private ArrayList<Permission> allPermissonForObject = new ArrayList<Permission>();
	private User changingUser =  null;
	private VerticalPanel root = new VerticalPanel();
	private VerticalPanel boxPanel = new VerticalPanel();
	private HorizontalPanel horizontal = new HorizontalPanel();
	private Button shareButton = new Button("Kontaktliste teilen");
	private Button closeButton = new Button("Abbrechen");
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private String selectedUser;
	private Button addButton = new Button("+");
	private Label instruction = new Label();
	private HTML creatorLabel = new HTML();
	private HTML noUserLabel = new HTML();
	
	private Label creator = new Label();
	private Label selectValues = new Label();
	private Label changeValues = new Label();

	public ContactListSharing(final ContactList sharingContactList, final User creator) {
		try {
			this.setAnimationEnabled(true);
			closeButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					// Popup schließen bei Betägigung des Buttons
					center();
					hide();

				}
			});
			// Die Verschiedenen Elemente anzeigen oder ausblenden
					
			
			receiverUser.setVisible(false);
			selectValues.setVisible(false);
			changeValues.setVisible(false);
			
			root.add(new HTML("<h3> Teilhaberschaften für Kontaktliste <i>" + sharingContactList.getName() + " "
					+ "</i>verwalten</h3>"));
			root.add(new HTML("Ersteller: " + creator.getLogEmail() + "<br />"));
			root.add(new HTML("<br /><hr><br />"));
			
			instruction.getElement().setInnerHTML("Bitte einen User auswählen um dessen Berechtigung zu bearbeiten oder <br> rechts einen User eingeben um den Kontakt mit diesem zu teilen.");
			
			noUserLabel.setVisible(false);
			noUserLabel.setHTML("Diese Kontakt ist mit keinem anderen User geteilt");
			
			root.add(instruction);
			root.add(new HTML("<br />"));


			/**Anzeigen der User, die bereits Zugriff auf diesen Kontakt haben*/
			ClientSideSettings.getConnectedAdmin().getPermissionsBySharedObjectId(sharingContactList.getBoId(),
					new AsyncCallback<ArrayList<Permission>>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Leider konnten die Permissions des Kontakts nicht abgefragt werden");
						}

						@Override
						public void onSuccess(ArrayList<Permission> result) {
							if(result.size() == 0){
								noUserLabel.setVisible(true);
							}
							allPermissonForObject =result;
					
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
												usersWithPermission.redraw();
												userDataProvider.getList().add(result);

											}

										});
							}
							
													
							userDataProvider.addDataDisplay(usersWithPermission);
							
							/** CellTable Columns erstellen*/
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
											Permission deletePermission = null;
										
											for(Permission p : allPermissonForObject){
												if(p.getReceiverUserID() == object.getBoId()){
													deletePermission = p;
												}
											}
											
											
											ClientSideSettings.getConnectedAdmin().deletePermission(deletePermission, new AsyncCallback<Void>(){

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
			/** Alle user für die SuggestBox laden, die über das Oracle vorgeschlagen werden */
			loadAllUser();
			/**
			 * suggestBox KeyUpHandler, bei betätigung Vorschläge anzeigen
			 * 
			 */
			suggestBox.addKeyUpHandler(new KeyUpHandler() {
				@Override
				public void onKeyUp(KeyUpEvent event) {
					selectedUser = ((SuggestBox) event.getSource()).getText();
				}
			});

			suggestBox.getElement().getStyle().setMarginLeft(20, Unit.PX);
			/** Bei Hinzufügen des kontakts, schaltet es auf den "kontakt an neuen User" Modus um*/
			addButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					usersWithPermission.setVisible(false);
					changeValues.setVisible(false);
					selectValues.setVisible(true);
					receiverUser.setVisible(true);
					
					shareButton.setText("Kontaktliste teilen");
				


					ClientSideSettings.getConnectedAdmin().findUserByEmail(suggestBox.getText(), new AsyncCallback<User>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("User wurde nicht gefunden");

						}

						@Override
						public void onSuccess(User result) {
						
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
			//CellTable für Empfänge User erstellen
			Column<User, String> reiceiveDeleteColumn = new Column<User, String>(deleteButton) {
				public String getValue(User object) {
					return "";
				}
			};
			reiceiveDeleteColumn.setCellStyleNames("iconButton");
			reiceiveDeleteColumn.setFieldUpdater(new FieldUpdater<User, String>() {
				//dies passiert wenn ein User aus der Liste gelöscht wird
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
			

			/**
			 * Single selection model festlegen. Dieses dient der Auswhal im Celltable, es darf jedoch nur 1 Eintrag makiert werden.
			 */
			selectionModel_Single.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				
				public void onSelectionChange(SelectionChangeEvent event) {
					
					//Aktionenn bei anwählen eines Eintrag der Liste.
					if (changingUser != null) {
									
						changingUser = selectionModel_Single.getSelectedObject();

					}
					
				}
			});
			
			

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
			root.add(noUserLabel);
			changeValues.getElement().setInnerHTML("Bitte unten die Eigenschaftsberechtigungen ändern.<br />");
			selectValues.getElement().setInnerHTML("Bitte wählen Sie die Eigenschaften aus, die Sie teilen möchten:<br />");
			root.add(selectValues);
			root.add(changeValues);


			/**
			 * Dieser ClickHandler legt fest, was passiert wenn ein Kontakt geteilt wird.
			 */
			shareButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					
				    if(shareButton.getText().equals("Kontaktliste teilen")){
				    	
					
					
					ArrayList<User> selectedUsers = new ArrayList<User>();
					
					
					for (int i = 0; i < receiverUser.getVisibleItems().size(); i++) {
						selectedUsers.add(receiverUser.getVisibleItems().get(i));
					}


					ClientSideSettings.getConnectedAdmin().giveContactlistPermissionToUsers(sharingContactList, selectedUsers, ClientSideSettings.getCurrentUser().getBoId(), new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Der Kontakt konnte nicht geteilt werden");
								}

								@Override
								public void onSuccess(Void result) {
									Window.alert("Die Kontaktlist wurde geteilt");
									hide();
								}

							});

				}  
				
				}
			});
			//Widgets dem rootVerticalPanel hinzufügen
			root.add(shareButton);
			root.add(closeButton);
			setWidget(root);
		} catch (Exception e) {
			Window.alert(e.toString());
			e.printStackTrace();
		}

		}


/**
* Laden der Vörschläge in das Oracle. Hier sind es die User an die geteilt werden kann, außer dem eigenen.
*/
private void loadAllUser() {
ClientSideSettings.getConnectedAdmin().findAllUser(new AsyncCallback<ArrayList<User>>() {

@Override
public void onFailure(Throwable caught) {


}

@Override
public void onSuccess(ArrayList<User> result) {
oracle.clear();
for (User u : result) {
	if(u.getBoId() !=
	 ClientSideSettings.getCurrentUser().getBoId()){
	oracle.add(u.getLogEmail());
	 }
}

}

});


}
}
