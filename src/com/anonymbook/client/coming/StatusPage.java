package com.anonymbook.client.coming;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class StatusPage extends Composite {

	private static StatusPageUiBinder uiBinder = GWT
			.create(StatusPageUiBinder.class);

	interface StatusPageUiBinder extends UiBinder<Widget, StatusPage> {
	}

	@UiField
	Label text;

	public StatusPage(String text) {
		initWidget(uiBinder.createAndBindUi(this));

		this.text.setText(text);
	}

}
