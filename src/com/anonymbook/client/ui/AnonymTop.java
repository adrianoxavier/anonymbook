package com.anonymbook.client.ui;

import com.anonymbook.client.resources.AnonymMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class AnonymTop extends Composite {

	private static AnonymTopUiBinder uiBinder = GWT.create(AnonymTopUiBinder.class);

	interface AnonymTopUiBinder extends UiBinder<HTMLPanel, AnonymTop> {
	}

	@UiField
	AnchorElement reviewLink;
	
	
	@UiField
	LabelElement reviewLabel;
	
	@UiField
	LabelElement reviewLabelEnd;
	
	public AnonymTop() {
		initWidget(uiBinder.createAndBindUi(this));
		
		reviewLink.setHref("https://www.facebook.com/apps/application.php?id=100923886651872&sk=app_6261817190");
		reviewLabel.setInnerText(AnonymMessages.instance.rating());
		reviewLabelEnd.setInnerText("!");
	}

}
