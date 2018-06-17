package de.hdm.ReportGenerator.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

import de.hdm.Connected.client.ClientSideSettings;
import de.hdm.ReportGenerator.client.gui.ReportGeneratorBaseForm;
import de.hdm.ReportGenerator.shared.ReportGeneratorServiceAsync;

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
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final ReportGeneratorServiceAsync reportGeneratorService = ClientSideSettings.getReportGenerator();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		ReportGeneratorBaseForm rgB = new ReportGeneratorBaseForm();
		
		RootPanel.get().add(rgB);
		
		
	}
}
