package com.anonymbook.client.home;

import com.anonymbook.client.controller.ShareController;
import com.anonymbook.client.event.AnonymTextAreaCallBack;
import com.anonymbook.client.event.DataCallback;
import com.anonymbook.client.event.JavaScriptCallback;
import com.anonymbook.client.resources.AnonymMessages;
import com.anonymbook.client.resources.AnonymUser;
import com.anonymbook.client.ui.components.AnonymComponent;
import com.anonymbook.client.ui.components.AnonymTextArea;
import com.anonymbook.client.ui.components.AskComponent;
import com.anonymbook.client.ui.question.QuestionModel;
import com.anonymbook.client.util.Anonym;
import com.anonymbook.client.util.FB;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class UserWall extends Composite {

	private static UserWallUiBinder uiBinder = GWT
			.create(UserWallUiBinder.class);

	interface UserWallUiBinder extends UiBinder<HTMLPanel, UserWall> {
	}

	private HTMLPanel panel;
	
	@UiField
	HTMLPanel rootpanel;
	
	@UiField
	Image arrowImage;

	@UiField
	AskComponent askComponent;

	@UiField
	AnonymComponent anonymComponent;

	@UiField
	LabelElement username;
	
	@UiField
	Label arrowLabel;

	private AnonymUser user;

	private String userId;
	
	private AnonymMessages messages = AnonymMessages.instance;

	public UserWall(AnonymUser user, final String userId, String question) {
		panel = uiBinder.createAndBindUi(this);
		initWidget(panel);
		
		arrowImage.setVisible(false);
		arrowLabel.setVisible(false);

		this.user = user;
		this.userId = userId;

		boolean areFriends = true;

		askComponent.setButtonText(messages.askQuestionAnonymously());
		arrowLabel.setText(messages.arrowTip());

		
		// Retirar question do user local.
		if (user.getId().equals(userId)) {
			askComponent.setButtonText(messages.askAndShare());
			arrowTipVisible(true);
			areFriends = false;
		} else if (user.getFriends() != null
				&& !user.getFriends().contains(userId)) {
			askComponent.removeFromParent();
			areFriends = false;
		}

		setUsername();
		anonymComponent.init(user, userId, areFriends, question);

		addListeners();
		
		anonymBasic(user.getId().equals(userId));
	}

	public void arrowTipVisible(boolean visible) {
		if (visible) {
			fadeIn(arrowImage.getElement());
			fadeIn(arrowLabel.getElement());
		}else{
			fadeOut(arrowImage.getElement());
			fadeOut(arrowLabel.getElement());
		}
	}

	private native void fadeIn(Element element) /*-{
		$wnd.$(element).fadeIn();
	}-*/;

	private native void fadeOut(Element element) /*-{
		$wnd.$(element).fadeOut();
	}-*/;

	private void anonymBasic(boolean isCurrent) {
		if(!isCurrent) {
			askComponent.setVisible(false);
		} else{
			rootpanel.getElement().setId(DOM.createUniqueId());

			MouseOverHandler mouseOverHandler = new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					arrowTipVisible(true);
				}
			};

			MouseOutHandler mouseOutHandler = new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent event) {
					arrowTipVisible(false);
				}
			};

			rootpanel.addDomHandler(mouseOverHandler, MouseOverEvent.getType());
			rootpanel.addDomHandler(mouseOutHandler, MouseOutEvent.getType());

		}
	}

	private void addListeners() {
		askComponent.addAskListener(new AnonymTextAreaCallBack() {
			@Override
			public void onFocus() {
			}

			@Override
			public void onLostFocus() {
			}

			@Override
			public void onButtonClick(AnonymTextArea textArea) {
				arrowTipVisible(false);
				
				String question = textArea.getText();
				askComponent.loading(true);
				if (!Anonym.validadeMessage(question)) {
					askComponent.loading(false);
					return;
				}
				if (user.getId().equals(userId)) {
					makePublicQuestion(question);
				} else {
					makeAnonymousQuestion(question);
				}
				askComponent.clearTextArea();
			}

		});
	}

	private void setUsername() {
		FB.user(userId, new JavaScriptCallback() {
			@Override
			public void onEvent(JavaScriptObject response) {
				username.setInnerText((Anonym.get(response, "name")));
			}
		});
	}

	public void setFriendClickCallback(DataCallback<String> dataCallback) {
		anonymComponent.setFriendClickCallback(dataCallback);
	}

	private void makePublicQuestion(final String question) {
		String param = "question=" + URL.encodeQueryString(question);
		
		Anonym.postAndAlertOnFailure("question/make", param,
				new DataCallback<JSONValue>() {
					@Override
					public void onEvent(JSONValue data) {
						askComponent.loading(false);
						anonymComponent.selectAllQuestions();

						ShareController.share(
								new QuestionModel(data.isObject()),
								user.getId());
					}
				});
	}

	private void makeAnonymousQuestion(String question) {
		String param = "question=" + URL.encodeQueryString(question) + "&recipient="
				+ userId;

		Anonym.postAndAlertOnFailure("question/make", param,
				new DataCallback<JSONValue>() {
					@Override
					public void onEvent(JSONValue data) {
						askComponent.loading(false);
						anonymComponent.selectQuestionsByMe();
					}
				});
	}

}
