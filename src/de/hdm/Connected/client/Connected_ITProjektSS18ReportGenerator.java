package de.hdm.Connected.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.Connected.client.gui.ReportGenerator.ReportGeneratorBaseForm;
import de.hdm.Connected.shared.ConnectedAdminAsync;
import de.hdm.Connected.shared.LoginService;
import de.hdm.Connected.shared.LoginServiceAsync;
import de.hdm.Connected.shared.ReportGenerator.ReportGeneratorServiceAsync;
import de.hdm.Connected.shared.bo.User;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Connected_ITProjektSS18ReportGenerator implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	/**
	 * Login Panel
	 */
	protected static LoginInfo loginInfo = null;
	private static VerticalPanel loginPanel = new VerticalPanel();
	private static Label loginLabel = new Label(
			"Please sign in to your Google Account to access the Connected application.");
	private static Anchor signInLink = new Anchor("Sign In");
	public static User currentUser = new User();

	// Settings
	final Settings settings = new Settings();
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final ReportGeneratorServiceAsync reportGeneratorService = ClientSideSettings.getReportGenerator();

	private final ConnectedAdminAsync connectedAdminService = ClientSideSettings.getConnectedAdmin();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		// Menü start
		final Command settingDialog = new Command() {
			public void execute() {

				RootPanel.get("content").clear();
				
				RootPanel.get("content").add(settings);
			}
		};

		final Command logout = new Command() {
			public void execute() {
				Window.open(loginInfo.getLogoutUrl(), "_self", "");
				loadLogin();
			}
		};
		
		String targetUrl = GWT.getHostPageBaseURL()+"Connected_ITProjektSS18ReportGenerator.html";
		
		// Check login status using login service.
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(targetUrl, new AsyncCallback<LoginInfo>() {
			
			@Override
			public void onSuccess(LoginInfo result) {
				log("UserInfo1: " + result.getEmailAddress());
				loginInfo = result;

				final String mail = loginInfo.getEmailAddress();

				if (loginInfo.isLoggedIn() == true) {
					connectedAdminService.findUserByEmail(mail, new AsyncCallback<User>() {

						@Override
						public void onSuccess(User result) {
							// Ruft den User auf der mit der Google email in der DB
							// eingetragen ist
							if (result != null) {
								log("User: " + result.getLogEmail());
								currentUser = result;
								
								// Anzeige der Menü Elemente
								MenuBar menu1 = new MenuBar(true);
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
								

								ReportGeneratorBaseForm rgB = new ReportGeneratorBaseForm(currentUser);
								RootPanel.get("content").add(rgB);


							} else if (mail != null) {
								// Erstellt einen neuen User wenn dieser noch nicht
								// existiert
								connectedAdminService.createUser(loginInfo, new AsyncCallback<User>() {

									@Override
									public void onFailure(Throwable caught) {

									}

									@Override
									public void onSuccess(User result) {
										log("Create new User: " + result.getName());
										currentUser = result;

										ReportGeneratorBaseForm rgB = new ReportGeneratorBaseForm(currentUser);
										RootPanel.get("content").add(rgB);

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
			@Override
			public void onFailure(Throwable error) {
				log("Error: " + error);
			}
		});

		// Footer Attribute
		HorizontalPanel footer = new HorizontalPanel();
		Anchor connectedLink = new Anchor("Connected", "Connected_ITProjektSS18.html");
		HTML copyrightText2 = new HTML(" | ");
		Anchor reportGeneratorLink = new Anchor(" ReportGenerator", "Connected_ITProjektSS18ReportGenerator.html");
		HTML copyrightText = new HTML(" | 2018 Connected | ");
		Anchor impressumLink = new Anchor("Impressum");

        //Footer Impressum
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
                        + "<ul><li>Alexeyeva, Viktoriya</li>"
                        + "<li>Aridag, Burak</li>"
                        + "<li>Bittner, Moritz</li>"
                        + "<li>Müller, Philip</li>"
                        + "<li>Rodrigues Ribeiro, Patricia</li>"
                        + "<li>Semmler, Denise</li></ul>"
                        + "<h3>Kontakt</h3>"
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

	native void log(String s) /*-{
		console.log(s);
	}-*/;
	
	public static void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("content").add(loginPanel);
	}
}