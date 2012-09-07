package com.anonymbook.client.home;

import java.util.List;

import com.anonymbook.client.event.DataCallback;
import com.anonymbook.client.event.QuickQuestionCallBack;
import com.anonymbook.client.quickquestion.QuickQuestionUI;
import com.anonymbook.client.resources.AnonymMessages;
import com.anonymbook.client.resources.AnonymUser;
import com.anonymbook.client.util.Anonym;
import com.anonymbook.toastmessage.ToastMessage;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserQuickQuestion extends Composite {

	@UiField
	HTMLPanel quickQuestionPanel;

	@UiField
	LabelElement nameLabel;

	@UiField
	LabelElement titleLabel;

	private AnonymUser user;

	private AnonymMessages messages = AnonymMessages.instance;

	private static UserQuickQuestionUiBinder uiBinder = GWT
			.create(UserQuickQuestionUiBinder.class);

	interface UserQuickQuestionUiBinder extends
			UiBinder<Widget, UserQuickQuestion> {
	}

	public UserQuickQuestion() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public UserQuickQuestion(AnonymUser user) {
		initWidget(uiBinder.createAndBindUi(this));

		this.user = user;
		this.titleLabel.setInnerText(messages.quickQuestion());
		this.nameLabel.setInnerText(this.user.getName());

		nextFriend();
	}

	private void nextFriend() {
		List<String> friends = user.getFriends();
		setRecipient(friends.get(Random.nextInt(friends.size())));
	}

	public void setRecipient(String recipientId) {
		quickQuestionPanel.clear();
		quickQuestionPanel.add(new QuickQuestionUI(recipientId,
				new QuickQuestionCallBack() {

					@Override
					public void onSubmitClick(String question,
							String recipientId, String recipientName) {
						sendQuestion(question, recipientId, recipientName);
					}

					@Override
					public void onSkipClick() {
						nextFriend();
					}
				}));

	}

	private void sendQuestion(String question, String recipientId,
			final String recipientName) {

		String param = "question=" + URL.encode(question) + "&recipient="
				+ recipientId;

		final String message = messages.questionSent(recipientName);

		Anonym.postAndAlertOnFailure("question/make", param,
				new DataCallback<JSONValue>() {
					@Override
					public void onEvent(JSONValue data) {
						nextFriend();
						ToastMessage.success(message);
					}
				});
	}

	public void setUser(String recipientId) {

	}

}
