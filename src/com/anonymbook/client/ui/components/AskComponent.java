package com.anonymbook.client.ui.components;

import com.anonymbook.client.event.AnonymTextAreaCallBack;
import com.anonymbook.client.resources.AnonymMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;

public class AskComponent extends Composite {

	private static AskComponentUiBinder uiBinder = GWT
			.create(AskComponentUiBinder.class);

	@UiField
	AnonymTextArea textArea;

	@UiField
	SpanElement share;

	@UiField
	Element infoText;

	@UiField
	Image loading;

	String id;

	private AnonymMessages messages = AnonymMessages.instance;

	interface AskComponentUiBinder extends UiBinder<HTMLPanel, AskComponent> {
	}

	public AskComponent() {
		initWidget(uiBinder.createAndBindUi(this));
		loading.setVisible(false);

		share.setInnerText(messages.share() + ":");
		infoText.setInnerText(messages.question());

		textArea.setFontSize(new Double(13));

	}

	public void addAskListener(AnonymTextAreaCallBack callback) {
		textArea.setCallBack(callback);
	}

	public void loading(boolean isLoading) {
		if (isLoading) {
			loading.setVisible(true);
		} else {
			loading.setVisible(false);
		}
	}

	public void clearTextArea() {
		textArea.setText("");
	}

	public void setButtonText(String buttonText) {
		textArea.setButtonText(buttonText);
	}

	public void hasButton(boolean hasButton) {
		textArea.hasButton(hasButton);
	}

	public String getText() {
		return textArea.getText();
	}

	public void focus(boolean focus) {
		textArea.focus(focus);
	}

}
