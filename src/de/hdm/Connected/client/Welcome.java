package de.hdm.Connected.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


import de.hdm.Connected.shared.ConnectedAdminAsync;
import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.shared.ConnectedAdmin;;
/**
 * Diese Klasse generiert die Begrüßungsseite
 * 
 *
 */
public class Welcome extends VerticalPanel {

	private final static ConnectedAdminAsync connectedAdmin = GWT.create(ConnectedAdmin.class);

	static VerticalPanel vpWelcomePanel = new VerticalPanel();
	private static Logger rootLogger = Logger.getLogger("");

	User currentUser = new User();
	//Settings settings = new Settings();
	Label lblWelcome = new Label();
	String firstName = "";
	int userId;

	/**
	 * Create the Panel
	 */
	HorizontalPanel hpButtonPanel = new HorizontalPanel();

	VerticalPanel vDialog = new VerticalPanel();
	HorizontalPanel hDialog = new HorizontalPanel();

	protected void run() {

		this.setStyleName("vpLeft");
		this.setWidth("600px");

		//firstName = Homepage.currentUser.getFirstName();


//		connectedAdmin.findUserByMail(Homepage.currentUser.getLogEmail(), new AsyncCallback<User>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onSuccess(User result) {
//				// TODO Auto-generated method stub
//				rootLogger.log(Level.SEVERE, "result: " + result.getFirstName());
//
//				firstName = result.getFirstName();
//
//			}
//		});


		lblWelcome.setText("Herzlich Willkommen, " + firstName + "!");
		lblWelcome.setStyleName("lblWelcome");

		vDialog.setSpacing(10);
		vDialog.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vDialog.add(hDialog);

		vDialog.add(lblWelcome);

		vpWelcomePanel.add(vDialog);

		this.add(vpWelcomePanel);

	}

}