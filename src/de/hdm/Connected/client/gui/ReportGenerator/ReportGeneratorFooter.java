package de.hdm.Connected.client.gui.ReportGenerator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class ReportGeneratorFooter extends Widget {

	// Footer Attribute
	private HorizontalPanel footer = new HorizontalPanel();
	private Anchor connectedLink = new Anchor("Connected", "Connected_ITProjektSS18.html");
	private HTML copyrightText2 = new HTML(" | ");
	private Anchor reportGeneratorLink = new Anchor(" ReportGenerator", "Connected_ITProjektSS18ReportGenerator.html");
	private HTML copyrightText = new HTML(" | 2018 Connected | ");
	private Anchor impressumLink = new Anchor("Impressum");

	public ReportGeneratorFooter() {

		// Footer Impressum
		impressumLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("content").getElement().setInnerHTML("");
				RootPanel.get("content")
						.add(new HTML("<h2>Impressum nach §5 TMG</h2>" + "<h3>Verantwortlich</h3>"
								+ "<p>Hochschule der Medien<br />" + "Nobelstraße 8<br />" + "70569 Stuttgart<br /></p>"
								+ "<p><strong>Projektarbeit innerhalb des Studiengangs "
								+ "Wirtschaftsinformatik und digitale Medien, " + "IT-Projekt SS 18.</strong></p>"
								+ "<h3>Projektteam</h3>" + "<ul><li>Alexeyeva, Viktoriya</li>"
								+ "<li>Aridag, Burak</li>" + "<li>Bittner, Moritz</li>" + "<li>Müller, Philip</li>"
								+ "<li>Rodrigues Ribeiro, Patricia</li>" + "<li>Semmler, Denise</li></ul>"
								+ "<h3>Kontakt</h3>" + "<p><strong>Telefon:</strong> 0711 8923 10 (Zentrale)</p>"
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

}
