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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.shared.ConnectedAdminAsync;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.Property;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.bo.Value;

public class ContactsTable extends CellTable {
	
	private ConnectedAdminAsync connectedAdmin = ClientSideSettings.getConnectedAdmin();
	
	//private HorizontalPanel hPanel = new HorizontalPanel();
	
	private CellTable<Contact> cellTable = new CellTable<Contact>();
	private Map<Property, Value> propertyValueMap = null;
	
	ListDataProvider<Contact> dataProvider = new ListDataProvider<Contact>();
	
	List<Contact> contacts = new ArrayList<Contact>();
	SimplePager pager;
	String imageHtml = "<img src=" + "Trash_Can.png" + " alt=" + "Kontakt löschen" + ">";
	boolean buttonPressed;
	
	public ContactsTable() {
		// Create a Pager to control the table.
				SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
				pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
				pager.setDisplay(cellTable);		
		
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		
		ClientSideSettings.getConnectedAdmin().getContactsByUserPermission(2,new AsyncCallback<ArrayList<Contact>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<Contact> result) {
				contacts = result;
				
			ClickableTextCell prenameCell = new ClickableTextCell();	
				
		Column<Contact, String> prenameColumn = new Column<Contact, String> (prenameCell) {
			
			 public String getValue(Contact object) {
				    return object.getPrename();
				  }
			
			};
			
			prenameColumn.setSortable(true);
			
			prenameColumn.setFieldUpdater(new FieldUpdater<Contact, String>(){

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
									ShowContactInfo_Dialog showContact = new ShowContactInfo_Dialog(object);

									showContact.center();
									showContact.show();

								}

							});
					
				}
				
			});
			
			cellTable.addColumn(prenameColumn, "Vorname");
			
		TextColumn<Contact> surnameColumn = new TextColumn<Contact>() {

				@Override
				public String getValue(Contact object) {
						 return object.getSurname();
					}
				};
				
			surnameColumn.setSortable(true);
			cellTable.addColumn(surnameColumn, "Nachname");	
			
			ClickableTextCell shareButton = new ClickableTextCell(){
				 @Override
					public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
					 String title = "Kontakt teilen";
				           if (data != null) {
				               sb.appendHtmlConstant("<img title='" + title + "' src=" + "/share.png" + " alt=" + "Kontakt bearbeiten" + " height=" +"25"+ " width=" + "25"+">");
				                	             
				            }
					 }
			};
			
			Column<Contact,String> shareColumn = new Column<Contact,String> (shareButton) {
				  public String getValue(Contact object) {
				    return "";
				  }
				};
				
		    shareColumn.setCellStyleNames("iconButton");
			shareColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {
				@Override
				public void update(int index, Contact object, String value) {
					buttonPressed = true;
					Window.alert("Hallooo");
					
					
				}		
			});
			
			
			cellTable.addColumn(shareColumn);
			
			
			
		ClickableTextCell updateButton = new ClickableTextCell(){
			 @Override
				public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
				 String title = "Kontakt bearbeiten";
			           if (data != null) {
			               sb.appendHtmlConstant("<img title='" + title + "'src=" + "/edit.png" + " alt=" + "Kontakt bearbeiten" + " height=" +"25"+ " width=" + "25"+">");
			                	             
			            }
				 }
		};
		
		Column<Contact,String> updateColumn = new Column<Contact,String> (updateButton) {
			  public String getValue(Contact object) {
			    return "";
			  }
			};
			
	    updateColumn.setCellStyleNames("iconButton");
	    updateColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {
			@Override
			public void update(int index, Contact object, String value) {
				buttonPressed = true;
				//Window.alert("Hallooo");
				final ContactForm updatePopUp = new ContactForm(object);

				// Enable glass background.
				updatePopUp.setGlassEnabled(true);				
				//updatePopUp.setPopupPosition(200, 300);
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
	
		ClickableTextCell deleteButton = new ClickableTextCell(){
			 @Override
			public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
				 String title = "Kontakt löschen";
		           if (data != null) {
		               sb.appendHtmlConstant("<img title='" + title + "' src=" + "/delete.png" + " alt=" + "Kontakt löschen" + " height=" +"25"+ " width=" + "25"+">");
		                	             
		            }
			 }
			 };
			 
		
		Column<Contact,String> deleteColumn = new Column<Contact,String> (deleteButton) {
			  public String getValue(Contact object) {
			    return "";
			  }
			};
		deleteColumn.setCellStyleNames("iconButton");
		deleteColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {

			@Override
			public void update(int index, final Contact object, String value) {
				// TODO Auto-generated method stub
				//Clickhandler
				User user = new User();
				user.setBoId(2);
				buttonPressed = true;
				ClientSideSettings.getConnectedAdmin().deleteContact(object, user, new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
						for(int i=0; i<contacts.size(); i++){
							if(contacts.get(i).getBoId() == object.getBoId()){
								contacts.remove(i);
							}
						}
						dataProvider.getList().clear();
						dataProvider.getList().addAll(contacts);
						Window.alert("Kontakt " + object.getPrename() + " " + object.getSurname() + " wurde gelöscht");
					}
					
				});
				
				
			}		
		});
		
		
		cellTable.addColumn(deleteColumn);
		dataProvider.getList().clear();
		dataProvider.addDataDisplay(cellTable);
		
		
	/*	final SingleSelectionModel<Contact> selectionModel = new SingleSelectionModel<Contact>();
		cellTable.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			//final PopupPanel contactPopup = new PopupPanel(true, false);
				public void onSelectionChange(SelectionChangeEvent event) {
				final Contact selected = selectionModel.getSelectedObject();
				if (selected != null && !buttonPressed) {
				//Window.alert("You selected: " + selected.prename + " " + selected.surname);
					
					ClientSideSettings.getConnectedAdmin().findValueAndProperty(selected.getBoId(),
							new AsyncCallback<Map<Property, Value>>() {

								public void onFailure(Throwable caught) {
									Window.alert("Ops, da ist etwas schief gelaufen!");
								}

								public void onSuccess(Map<Property, Value> result) {
									propertyValueMap = result;
									// Window.alert(Integer.toString(result.size()));
									// Window.alert(Integer.toString(globalIndex));
									ShowContactInfo_Dialog showContact = new ShowContactInfo_Dialog(selected);

									showContact.center();
									showContact.show();

								}

							});
					
					
				final Anchor selectedContact = new Anchor(selected.getPrename() + selected.getSurname());
				
				     
				HorizontalPanel contactPopupContainer = new HorizontalPanel();
				contactPopupContainer.setSpacing(10);
				final HTML contactInfo = new HTML();
				contactPopupContainer.add(contactInfo);
				
				///contactPopup.setWidget(contactPopupContainer);

				contactInfo.setHTML("Vorname: "+ selected.getPrename() + "<br>" + "Nachname: " + selected.getSurname() + "</i>");

			//  int left = selectedContact.getAbsoluteLeft() + 86;
			//  int top = selectedContact.getAbsoluteTop() + 45;
		      //contactPopup.setPopupPosition(left, top);
			//	contactPopup.show();
			} else if (buttonPressed) {buttonPressed = false;}
		} 
   }); */
		
		 	List<Contact> list = dataProvider.getList();
		    for (Contact Contact : contacts) {
		      list.add (Contact);
		    }
		
		    ListHandler<Contact> columnSortHandler = new ListHandler<Contact>(list);
		    
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
		    
		    cellTable.addColumnSortHandler(columnSortHandler);
		    
		    ListHandler<Contact> columnSortHandler2 = new ListHandler<Contact>(list);
		    
		    columnSortHandler2.setComparator(surnameColumn,
		        new Comparator<Contact>() {
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
		
		
		RootPanel.get("content").add(cellTable);
		RootPanel.get("content").add(pager);
	
		//cellTablePanel.setCellHorizontalAlignment(pager,HasHorizontalAlignment.ALIGN_CENTER);

	}

				
	});

}
	private class ShowContactInfo_Dialog extends PopupPanel {
	
	public ShowContactInfo_Dialog(Contact contact)  {
		//PopUp schließt automatisch wenn daneben geklickt wird
		super(true);
		//ensureDebugId("cwBasicPopup-simplePopup");
			   
		// Enable animation.
		setAnimationEnabled(true);

		// Enable glass background.
		setGlassEnabled(true);
		
		setWidth("300px");

		VerticalPanel v = new VerticalPanel();
		FlexTable contactInfoTable = new FlexTable();
		contactInfoTable.setCellSpacing(20);
		

		contactInfoTable.setWidget(0, 0, new HTML("<strong>Vorname: </strong>"));
		contactInfoTable.setWidget(0, 1, new HTML(contact.getPrename()));
		contactInfoTable.setWidget(1, 0, new HTML("<strong>Nachname: </strong>"));
		contactInfoTable.setWidget(1, 1, new HTML(contact.getSurname()));
		
		for(Map.Entry<Property, Value> entry : propertyValueMap.entrySet()){
			int rowCount = contactInfoTable.getRowCount();
			contactInfoTable.setWidget(rowCount, 0, new HTML("<strong>" + entry.getKey().getName() + ":</strong>"));
			contactInfoTable.setWidget(rowCount, 1, new HTML(entry.getValue().getName()));
		}
		
		v.add(new HTML("<h3> Kontakt: <i>" + contact.getPrename() + " " + contact.getSurname() +"</i></h3><br /><br />"));
		v.add(contactInfoTable);
		setWidget(v);
		
	}

	
}
	
}