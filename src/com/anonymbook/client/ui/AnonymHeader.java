package com.anonymbook.client.ui;

import com.anonymbook.client.event.SimpleCallback;
import com.anonymbook.client.resources.AnonymMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class AnonymHeader extends Composite {

	private static AnonymHeaderUiBinder uiBinder = GWT
			.create(AnonymHeaderUiBinder.class);

	interface AnonymHeaderUiBinder extends UiBinder<HTMLPanel, AnonymHeader> {
	}

	@UiField
	Anchor home;
	
	@UiField
	Anchor quickQuestion;
	
	@UiField
	Anchor help;
	
	private AnonymMessages messages = AnonymMessages.instance;

	public AnonymHeader() {
		initWidget(uiBinder.createAndBindUi(this));

		home.setText(messages.home());
		help.setText(messages.help());
		quickQuestion.setText(messages.quickQuestion());
		
		anonymBasic();
	}
	
	private void anonymBasic() {
		quickQuestion.setVisible(false);
		help.setVisible(false);
	}

	public void addHomeClickListener(final SimpleCallback callback) {

		home.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				callback.onEvent();
			}
		});
	}
	
	public void addQuickQuestionClickListener(final SimpleCallback callback) {

		quickQuestion.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				callback.onEvent();
			}
		});
	}
	
	public void addHelpClickListener(final SimpleCallback callback) {

		help.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				callback.onEvent();
			}
		});
	}

}
