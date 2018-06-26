package de.hdm.Connected.client.gui;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.MenuBar;

import de.hdm.Connected.shared.bo.User;
import de.hdm.Connected.client.LoginInfo;
import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.Connected.client.Connected_ITProjektSS18;

public class Usermenu extends MenuBar {

	MenuBar menu = new MenuBar();
	
    MenuBar usermenu = new MenuBar(true);
    
    Hyperlink email = new Hyperlink();
    
    Command profil = new Command() {
    	public void execute() {
    		email.setText(ClientSideSettings.getCurrentUser().getLogEmail());
    	}   	
    };
    
    Command logout = new Command() {
		public void execute() {
			//Connected_ITProjektSS18.loginInfo.getLogoutUrl(); loginInfo auf public stellen
			//Window.open(Connected_ITProjektSS18.loginInfo.getLogoutUrl(), "_self", "");
			Connected_ITProjektSS18.loadLogin();
		}
	};
	
	public void onModuleLoad() {
	
		menu.setAutoOpen(true);
	    menu.setWidth("100px");
	    menu.setAnimationEnabled(true);
	    
	    usermenu.setAnimationEnabled(true);
	    
	    usermenu.addItem("Mein Profil", profil);
	    usermenu.addItem("Abmelden", logout);
	   
	}
	
}

	