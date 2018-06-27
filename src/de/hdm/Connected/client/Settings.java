package de.hdm.Connected.client;

import com.google.gwt.event.dom.client.ClickEvent;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.Connected.client.gui.StartPage;
import de.hdm.Connected.shared.ConnectedAdminAsync;
import de.hdm.Connected.shared.bo.User;


/**
 * Diese Klasse erzeugt die SettingsView und deren Eventhandler
 * 
 * @author Burak
 */
public class Settings extends VerticalPanel {

	private final static ConnectedAdminAsync connectedAdmin = ClientSideSettings.getConnectedAdmin();

	static VerticalPanel vpSettingsPanel = new VerticalPanel();
	static HorizontalPanel hpHeader = new HorizontalPanel();

	User currentUser = new User();
	public String userFirstName;

	/**
	 * Create the Label
	 */
	Label lbName = new Label("Name");
	Label lbEmail = new Label("E-Mail");
	Label lbAusgabeEmail = new Label("");
	Label lblInfoName = new Label("Bitte vervollständigen Sie Ihren Namen");

	/**
	 * Create the Textbox
	 */
	TextBox tbName = new TextBox();

	/**
	 * Create the Panel
	 */
	HorizontalPanel hpButtonPanel = new HorizontalPanel();
	static HorizontalPanel hpSettings = new HorizontalPanel();

	VerticalPanel vDialog = new VerticalPanel();
	HorizontalPanel hDialog = new HorizontalPanel();
	static VerticalPanel vpRight = new VerticalPanel();

	/**
	 * Create the Button
	 */
	Button btnAbrrechenButton = new Button("Abbrechen");
	Button btnSichernButton = new Button("Speichern");
	Button btnDeleteAccount = new Button("Profil löschen");

	static Label lblHeaderTitel = new Label("Profil Einstellungen");

	static Button btnNo = new Button("Nein");
	static Button btnYes = new Button("Ja");

	/**
	 * Diese Methode generiert die View und wird nach der Instanziierung
	 * aufgerufen
	 */
	protected void run() {
		log("Run Settings");
		hpSettings.setWidth("600px");
		vpSettingsPanel.setWidth("300px");
		vpRight.setWidth("300px");

		currentUser = Connected_ITProjektSS18.getCurrentUser();
		
		hpHeader.setStyleName("headerDetailView");
		hpSettings.setStyleName("showDetailContent");
		lblInfoName.setStyleName("lblInfoName");
		lblHeaderTitel.setStyleName("lblHeaderTitel");
		vpSettingsPanel.setStyleName("vpLeft");
		vpRight.setStyleName("vpRight");
		vDialog.setSpacing(10);
		vDialog.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vDialog.add(hDialog);
		hDialog.add(btnYes);
		hDialog.add(btnNo);

		hpHeader.add(lblHeaderTitel);

		lblInfoName.setVisible(false);
		vpSettingsPanel.add(lblInfoName);
		vpSettingsPanel.add(lbName);
		vpSettingsPanel.add(tbName);
		vpSettingsPanel.add(lbEmail);
		vpSettingsPanel.add(lbAusgabeEmail);

		hpButtonPanel.add(btnAbrrechenButton);
		hpButtonPanel.add(btnSichernButton);
		hpButtonPanel.add(btnDeleteAccount);
		vpSettingsPanel.add(hpButtonPanel);
		hpSettings.add(vpSettingsPanel);
		hpSettings.add(vpRight);
		
		

		tbName.getElement().setPropertyString("placeholder", "Name");
		log("Name: "+currentUser.getName());
		if (currentUser.getName() == null) {
			tbName.setText("");
			
		} else {
			tbName.setText(currentUser.getName());
		}
		
		if(tbName.getText() == ""){
			lblInfoName.setVisible(true);
		}else{
			lblInfoName.setVisible(false);
		}

		lbAusgabeEmail.setText(currentUser.getLogEmail());

		btnAbrrechenButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				RootPanel.get().add(new StartPage());
			}
		});

		btnSichernButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				currentUser.setName(tbName.getText());
				
				
				connectedAdmin.updateUser(currentUser, new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {

//								Homepage.hideView();
								if( tbName.getText() == "" ){
									lblInfoName.setVisible(true);
								}else{
									lblInfoName.setVisible(false);
								}
							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}
						});

			}
		});

		btnDeleteAccount.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub

				final DialogBox dlbQuestion = new DialogBox();

				dlbQuestion.setAnimationEnabled(true);
				dlbQuestion.setText("Sind sie sicher, dass Sie ihr Profil löschen möchten?");
				dlbQuestion.setWidth("300px");
				dlbQuestion.setWidget(vDialog);
				dlbQuestion.setModal(true);
				dlbQuestion.setGlassEnabled(true);
				dlbQuestion.center();

				int width = Window.getClientWidth() / 2;
				int height = Window.getClientHeight() / 2;
				dlbQuestion.setPopupPosition(width, height);
				dlbQuestion.show();

				btnYes.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {

						connectedAdmin.deleteUser(currentUser, new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {

								Window.open(Connected_ITProjektSS18.loginInfo.getLogoutUrl(), "_self", "");
								Window.alert("Ihr Profil wurde gelöscht!");
								Connected_ITProjektSS18.loadLogin();

							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}
						});
						// DialogBox ausblenden
						dlbQuestion.hide();

					}
				});

				btnNo.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {

						// DialogBox ausblenden
						dlbQuestion.hide();

					}
				});

			}
		});
		this.add(hpHeader);
		this.add(hpSettings);

	}
	
	native void log(String s) /*-{
		console.log(s);
	}-*/;

}