package de.hdm.Connected.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.Connected.client.gui.ReportGenerator.ReportGeneratorBaseForm;
import de.hdm.Connected.client.gui.ReportGenerator.ReportGeneratorFooter;
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
			"Melde dich mit deinem Google-Konto an um Connected Report Generator nutzen zu können.");
	private static Anchor signInLink = new Anchor("Anmelden");
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

		// Aktion die ausgeführt wird, wenn der Logout Knopf betätigt wird.
		final Command logout = new Command() {
			public void execute() {
				Window.open(loginInfo.getLogoutUrl(), "_self", "");
				loadLogin();
			}
		};

		String targetUrl = GWT.getHostPageBaseURL() + "Connected_ITProjektSS18ReportGenerator.html";

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
							// Ruft den User auf der mit der Google email in der
							// DB
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
								// Erstellt einen neuen User wenn dieser noch
								// nicht
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

		ReportGeneratorFooter rgF = new ReportGeneratorFooter();
		RootPanel.get("footer").add(rgF);

	}

	native void log(String s) /*-{
		console.log(s);
	}-*/;

	/**
	 * Statische Hilfsmethode für das Laden des LoginDialogs
	 */
	public static void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("content").add(loginPanel);
	}
}