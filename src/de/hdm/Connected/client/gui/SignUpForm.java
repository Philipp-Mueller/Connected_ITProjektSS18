package de.hdm.Connected.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.client.Connected_ITProjektSS18;
import de.hdm.Connected.client.ReportGenerator.Connected_ITProjektSS18ReportGenerator;
import de.hdm.Connected.shared.bo.User;


public class SignUpForm extends VerticalPanel {
	
	Label emailLabel = new Label("E-Mail-Adresse: ");
	TextBox emailBox = new TextBox();
	//Label passwortLabel = new Label("Passwort: ");
	//PasswordTextBox passwortBox = new PasswordTextBox();
	
	private User newUser = null;
	private Connected_ITProjektSS18 connected = null;
	private Connected_ITProjektSS18ReportGenerator repGen = null;
	
	public SignUpForm (Connected_ITProjektSS18 connected) {
		
		this.connected = connected;
		this.newUser = ClientSideSettings.getCurrentUser();
		
		VerticalPanel VPLabels = new VerticalPanel();
		this.add(VPLabels);
		
		VPLabels.add(emailLabel);
		VPLabels.add(emailBox);
		//VPLabels.add(passwortLabel);  
		//VPLabels.add(passwortBox);
		
		HorizontalPanel HPButtons = new HorizontalPanel();
		
		Button BSignUp = new Button("Account anlegen");
		BSignUp.addClickHandler(new ClickHandler() {
			public void onClick (ClickEvent event) {
				signUp();
			}
		});
		
		HPButtons.add(BSignUp);
		this.add(HPButtons);
		
	}
	
	public void signUp() {
	//	ClientSideSettings.getConnectedAdmin().createUser(emailBox.getText(), 
														//passwortBox.getText(),
													//	new NewUserCallback());	
	}
	
	private class NewUserCallback implements AsyncCallback<User> {

		@Override
		public void onFailure(Throwable caught) {
			ClientSideSettings.getLogger().severe("Neuer Account konnte nicht angelegt werden");
			RootPanel.get("content").clear();
		}

		@Override
		public void onSuccess(User result) {
			ClientSideSettings.getLogger().info("Neuer Account erfolgreich angelegt: "
												+ result.getLogEmail() + " )");
			
		ClientSideSettings.getCurrentUser().setLogEmail(result.getLogEmail());	
		//ClientSideSettings.getCurrentUser().setIsExisting(true);	fehlt im User
		RootPanel.get("usermenu").clear();
		RootPanel.get("content").clear();
		connected.onModuleLoad();
		
		}
	}
	
	public SignUpForm (Connected_ITProjektSS18ReportGenerator repGen) {
		
		this.repGen = repGen;
		this.newUser = ClientSideSettings.getCurrentUser();
		
		VerticalPanel VPLabels = new VerticalPanel();
		VPLabels.add(emailLabel);
		VPLabels.add(emailBox);
		//VPLabels.add(passwortLabel);  
		//VPLabels.add(passwortBox);
		
		HorizontalPanel HPButtons = new HorizontalPanel();
		
		Button BSignUp = new Button("Account anlegen");
		BSignUp.addClickHandler(new ClickHandler() {
			public void onClick (ClickEvent event) {
				signUpForRepGen();
			}
		});
		
		HPButtons.add(BSignUp);
		this.add(HPButtons);
		
	}
	
	public void signUpForRepGen() {
		//ClientSideSettings.getConnectedAdmin().createUser(emailBox.getText(), 
														//passwortBox.getText(),
														//new NewUserRepGenCallback());	
	}
	
	private class NewUserRepGenCallback implements AsyncCallback<User> {

		@Override
		public void onFailure(Throwable caught) {
			ClientSideSettings.getLogger().severe("Neuer Account konnte nicht angelegt werden");
			RootPanel.get("content").clear();
		}

		@Override
		public void onSuccess(User result) {
			ClientSideSettings.getLogger().info("Neuer Account erfolgreich angelegt: "
												+ result.getLogEmail() + " )");
			
		ClientSideSettings.getCurrentUser().setLogEmail(result.getLogEmail());	
		//ClientSideSettings.getCurrentUser().setIsExisting(true);	fehlt im User
		RootPanel.get("usermenu").clear();
		RootPanel.get("content").clear();
		repGen.onModuleLoad();
		
		}
	}

}
