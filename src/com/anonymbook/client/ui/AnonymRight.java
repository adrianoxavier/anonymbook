package com.anonymbook.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class AnonymRight extends Composite {

	private static AnonymRightUiBinder uiBinder = GWT
			.create(AnonymRightUiBinder.class);

	interface AnonymRightUiBinder extends UiBinder<HTMLPanel, AnonymRight> {
	}

	private HTMLPanel panel;

	public AnonymRight() {
		panel = uiBinder.createAndBindUi(this);
		initWidget(panel);
	}

	public void setComponent(Widget widget) {
		panel.clear();
		panel.add(widget);
	}

}
