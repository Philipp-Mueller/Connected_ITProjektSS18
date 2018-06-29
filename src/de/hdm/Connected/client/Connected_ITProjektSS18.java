package de.hdm.Connected.client;

import com.google.appengine.api.mail.MailService.Header;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Anchor;

import de.hdm.Connected.client.gui.ContactForm;
import de.hdm.Connected.client.gui.ContactForm_Test;
import de.hdm.Connected.client.gui.ContactListForm;
import de.hdm.Connected.client.gui.ContactListForm2;
import de.hdm.Connected.client.gui.ContactListForm3;
import de.hdm.Connected.client.gui.ContactSharing;
import de.hdm.Connected.client.gui.ContactsTable;
import de.hdm.Connected.client.gui.StartPage;
import de.hdm.Connected.shared.ConnectedAdminAsync;
import de.hdm.Connected.shared.FieldVerifier;
import de.hdm.Connected.shared.bo.Contact;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.client.LoginInfo;
import de.hdm.Connected.shared.LoginService;
import de.hdm.Connected.shared.LoginServiceAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Connected_ITProjektSS18 implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final ConnectedAdminAsync connectedAdmin = ClientSideSettings.getConnectedAdmin();
	
	/**
	 * Login Panel
	 */
	protected static LoginInfo loginInfo = null;
	public final static String value_URL = Window.Location.getParameter("url");
	private static VerticalPanel loginPanel = new VerticalPanel();
	private static Label loginLabel = new Label(
			"Please sign in to your Google Account to access the Connected application.");
	private static Anchor signInLink = new Anchor("Sign In");
	public static User currentUser = new User();

	
	// Settings
	final Settings settings = new Settings();
	final Welcome welcome = new Welcome();

	/**
	 * create new Panels
	 */

	VerticalPanel vpBasisPanel = new VerticalPanel();
	final static HorizontalPanel welcomePanel = new HorizontalPanel();
	final HorizontalPanel headlinePanel = new HorizontalPanel();
	final HorizontalPanel content = new HorizontalPanel();
	final HorizontalPanel logoutPanel = new HorizontalPanel();
	

	/**
	 * Create new Labels
	 */
	Label headlineLabel = new Label("Connected");

	/**
	 * Create new Buttons
	 */
	Button btnLogOut = new Button("Logout");
	Button zurueckButton = new Button("Zurück");

	static boolean isNew = false;
	private static Storage stockStore = null;
	
	/**
	 * This is the entry point method.
	 */
	
	public void onModuleLoad() {
		
		
		
		stockStore = Storage.getSessionStorageIfSupported();

		if (stockStore != null) {
			if (value_URL != null) {
				stockStore.setItem("url", value_URL);
			}

		}

		btnLogOut.setStylePrimaryName("logOutButton");
		headlineLabel.setStylePrimaryName("headlineLabel");
		logoutPanel.setStylePrimaryName("logoutPanel");
		headlinePanel.setStylePrimaryName("headlinePanel");

		welcomePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		logoutPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		/**
		 * add the widgets
		 */
		headlinePanel.add(headlineLabel);
		logoutPanel.add(btnLogOut);
		logoutPanel.add(zurueckButton);
		vpBasisPanel.add(logoutPanel);
		
		// Menü start
		Command settingDialog = new Command() {
			public void execute() {

				RootPanel.get("content").clear();
				
				RootPanel.get("content").add(settings);
			}
		};

		Command logout = new Command() {
			public void execute() {
				loginInfo.getLogoutUrl();
				Window.open(loginInfo.getLogoutUrl(), "_self", "");
				loadLogin();
			}
		};
		
		// Anzeige der Menü Elemente
		MenuBar menu1 = new MenuBar(true);
		menu1.addItem("Profil", settingDialog);
		menu1.addItem("Abmelden", logout);
		
		// Menü Icon
		MenuBar menu = new MenuBar();
		final String image = "<img src='user.png' height='40px' width='40px'/>";
		SafeHtml addActivityImagePath = new SafeHtml() {
		
			private static final long serialVersionUID = 1L;

			@Override
			public String asString() {
				return image;
			}
		};

		menu.addItem(addActivityImagePath, menu1);
		RootPanel.get("top").add(menu);
		
		// Menü ende
		
		log("Load Login");
		
		// Check login status using login service.
	   LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	      public void onFailure(Throwable error) {
	    	  log("Error: "+ error);
	      }

			public void onSuccess(LoginInfo result) {
				log("UserInfo1: " + result.getEmailAddress());
				loginInfo = result;

				final String mail = loginInfo.getEmailAddress();

				if (loginInfo.isLoggedIn() == true) {
				connectedAdmin.findUserByEmail(mail, new AsyncCallback<User>() {

						@Override
						public void onSuccess(User result) {
							// Ruft den User auf der mit der Google email in der DB eingetragen ist³
							if (result != null) {
								log("User: " + result.getLogEmail());
								currentUser = result;
								settings.run();
								welcome.run();

								RootPanel.get("content").add(welcome);
								

							} else if (mail != null) {
								// Erstellt einen neuen User wenn dieser noch nicht existiert
								connectedAdmin.createUser(loginInfo, new AsyncCallback<User>() {

											@Override
											public void onFailure(Throwable caught) {

											}

											@Override
											public void onSuccess(User result) {
												log("Create new User: "+ result.getName());
												currentUser = result;
												settings.run();
												welcome.run();

												isNew = true;

												RootPanel.get("content").add(welcome);

											}
										});

							}

							

						}

						@Override
						public void onFailure(Throwable caught) {

						}
					});

				} else {
					loadLogin();
				}

			}
		});
	    
		    
		Button newContactButton = new Button("Neuen Kontakt anlegen");
		Button editContactButton = new Button ("Kontakt 8 bearbeiten");
		Button shareContactButton = new Button ("Kontakt teilen");
		Button overviewPageButton = new Button ("Übersichtsseite");


		
		newContactButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("content").clear();
				ContactForm newcontactForm = new ContactForm();
			}

		});
		Button myContactListsButton = new Button("Meine Kontaktlisten");

		myContactListsButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("content").clear();
				ContactListForm3 mycontactlistForm = new ContactListForm3(168);
				//Test_CellTable newform = new Test_CellTable(); 
			}

		});
		
		overviewPageButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("content").clear();
				ContactsTable startPage = new ContactsTable();
				//ContactsTable welli = new ContactsTable();
				//Test_CellTable newform = new Test_CellTable(); 
			}

		});
		
		editContactButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientSideSettings.getConnectedAdmin().findContactById(10, new AsyncCallback<Contact>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Contact result) {
						RootPanel.get("content").clear();
						ContactForm newcontactForm = new ContactForm(result);
						//Test_CellTable newform = new Test_CellTable(); 
					}
					
				});
				
			}

		});
		
		
		shareContactButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientSideSettings.getConnectedAdmin().findContactById(154, new AsyncCallback<Contact>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Contact result) {
						RootPanel.get("content").clear();
						//ContactSharing newContactSharing = new ContactSharing(result);
						//Test_CellTable newform = new Test_CellTable(); 
					}
					
				});
				
			}

		});
		
		
		RootPanel.get("content").add(newContactButton);
		RootPanel.get("content").add(myContactListsButton);
		RootPanel.get("content").add(editContactButton);
		//RootPanel.get("content").add(shareContactButton);
		RootPanel.get("content").add(overviewPageButton);
		HorizontalPanel footer = new HorizontalPanel();
		Anchor startPage = new Anchor ("Startseite", "Connected_ITProjektSS18.html");
		HTML copyrightText1 = new HTML(" | ");
		Anchor reportGeneratorLink = new Anchor (" ReportGenerator", "Connected_ITProjektSS18ReportGenerator.html");
		HTML copyrightText2 = new HTML(" | © 2018 Connected | ");
		Anchor impressumLink = new Anchor("Impressum");
		footer.add(startPage);
		footer.add(copyrightText1);
		footer.add(reportGeneratorLink);
		footer.add(copyrightText2);
		footer.add(impressumLink);
		
		impressumLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("content").clear();
				RootPanel.get("content").add(new HTML("<h2>Impressum nach §5 TMG</h2>"
						+ "<h3>Verantwortlich</h3>"
						+ "<p>Hochschule der Medien<br />"
						+ "Nobelstraße 8<br />"
						+ "70569 Stuttgart<br /></p>"
						+ "<p><strong>Projektarbeit innerhalb des Studiengangs "
						+ "Wirtschaftsinformatik und digitale Medien, "
						+ "IT-Projekt SS 18.</strong></p>"
						+ "<h3>Projektteam</h3>"
						+ "<ul><li>xxx</li>"
						+ "<li>xxx</li>"
						+ "<li>xxx</li>"
						+ "<li>xxx</li>"
						+ "<li>xxx</li>"
						+ "<li>xxx</li></ul>"
						+ "<h3>Kontakt</h3>"
						+ "<p><strong>Telefon:</strong> 0711 8923 10 (Zentrale)</p>"
						+ "<p><strong>Website:</strong> <a href='http://www.hdm-stuttgart.de' target='_blank'>"
						+ "www.hdm-stuttgart.de</a></p>"));
			}
			
		});
		RootPanel.get("footer").add(footer);
	}
	
	public static void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("content").add(loginPanel);
	}
	/**
	 * Diese Methode liest die URL aus dem Sessionstorage aus und gibt diese
	 * zurück
	 * 
	 * @return
	 */
	public static String getValue_URL() {
		return stockStore.getItem(stockStore.key(0));

	}


	/**
	 * Diese Methode gibt den aktuell angemeldeten Nutzer zurück
	 * 
	 * @return
	 */
	public static User getCurrentUser() {
		return currentUser;
	}

	public static boolean isNew() {
		return isNew;
	}

	/**
	 * Diese Methode löscht den beschriebenen Sessionstorage
	 */
	public static void deleteStorage() {
		stockStore.clear();
	}
	
	native void log(String s) /*-{
		console.log(s);
	}-*/;
		  
}
