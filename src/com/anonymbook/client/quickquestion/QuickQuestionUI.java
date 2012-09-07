package com.anonymbook.client.quickquestion;

import com.anonymbook.client.event.AnonymTextAreaCallBack;
import com.anonymbook.client.event.JavaScriptCallback;
import com.anonymbook.client.event.QuickQuestionCallBack;
import com.anonymbook.client.resources.AnonymMessages;
import com.anonymbook.client.ui.components.AnonymTextArea;
import com.anonymbook.client.ui.components.AskComponent;
import com.anonymbook.client.util.Anonym;
import com.anonymbook.client.util.FB;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class QuickQuestionUI extends Composite {

	@UiField
	AskComponent askComponent;
	
	@UiField
	ImageElement recipientImage;

	@UiField
	Label recipientName;
	
	@UiField
	Button askButton;
	
	@UiField
	Button skipButton;

	private String recipientId;

	private QuickQuestionCallBack callBack;
	
	private AnonymMessages messages = AnonymMessages.instance;
	
	private static QuickQuestionUIUiBinder uiBinder = GWT
			.create(QuickQuestionUIUiBinder.class);

	interface QuickQuestionUIUiBinder extends UiBinder<Widget, QuickQuestionUI> {
	}

	public QuickQuestionUI() {
		initWidget(uiBinder.createAndBindUi(this));
		
		initComponent();
	}
	
	public QuickQuestionUI(String recipientId, QuickQuestionCallBack callBack) {
		initWidget(uiBinder.createAndBindUi(this));
		
		initComponent();
		this.callBack = callBack;
		this.recipientId = recipientId;
		
		setUserData(recipientId);
	}

	private void initComponent() {
		
		askButton.setText(messages.askAnonymous());
		skipButton.setText(messages.skip());
		
		askComponent.hasButton(false);
		askComponent.addAskListener(new AnonymTextAreaCallBack() {
			@Override
			public void onLostFocus() {
			}
			
			@Override
			public void onFocus() {
			}
			
			@Override
			public void onButtonClick(AnonymTextArea textArea) {
			}
		});
	}

	private void setUserData(String recipentId) {
				
		recipientImage.setSrc(FB.image(recipentId, "large"));
		FB.user(recipentId, new JavaScriptCallback() {
			@Override
			public void onEvent(JavaScriptObject data) {
				String name = Anonym.get(data, "name");
				recipientName.setText(name);
			}
		});
	}
	
	@UiHandler("skipButton")
	void handleSkipClick(ClickEvent e) {
		callBack.onSkipClick();
	}
	
	@UiHandler("askButton")
	void handleAskClick(ClickEvent e) {
		callBack.onSubmitClick(askComponent.getText(), recipientId, recipientName.getText());
	}

}
