package com.anonymbook.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class AnonymLeft extends Composite {

	private static AnonymLeftUiBinder uiBinder = GWT
			.create(AnonymLeftUiBinder.class);

	interface AnonymLeftUiBinder extends UiBinder<HTMLPanel, AnonymLeft> {
	}

	private HTMLPanel panel;

	public AnonymLeft() {
		panel = uiBinder.createAndBindUi(this);
		initWidget(panel);

	}

	public void setComponent(Widget widget) {
		panel.clear();
		panel.add(widget);
	}

}
