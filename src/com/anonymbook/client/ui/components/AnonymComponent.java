package com.anonymbook.client.ui.components;

import com.anonymbook.client.controller.QuestionsController;
import com.anonymbook.client.event.DataCallback;
import com.anonymbook.client.event.SimpleCallback;
import com.anonymbook.client.resources.AnonymMessages;
import com.anonymbook.client.resources.AnonymUser;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class AnonymComponent extends Composite {

	private static AnonymComponentUiBinder uiBinder = GWT
			.create(AnonymComponentUiBinder.class);

	interface AnonymComponentUiBinder extends
			UiBinder<HTMLPanel, AnonymComponent> {
	}

	enum Selected {
		ALL_QUESTIONS, QUESTIONS_BY_ME, QUESTIONS_TO_ME
	}

	@UiField
	FilterComponent filter;

	@UiField
	HTMLPanel answerBody;

	@UiField
	OlderQuestion olderQuestion;

	private Selected selected;

	private String userId;

	private QuestionsController questionsController;

	private boolean areFriends;

	private AnonymMessages messages = AnonymMessages.instance;

	public AnonymComponent() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void init(AnonymUser user, String userId, boolean areFriends,
			String question) {
		this.areFriends = areFriends;
		this.userId = userId;
		this.questionsController = new QuestionsController(user, userId,
				answerBody, olderQuestion);

		handleFilter(user, userId, areFriends);
		handleDisplay(user, userId, question);
		addListeners();

		anonymBasic();
	}

	private void anonymBasic() {
		filter.setVisible(false);
	}

	private void handleDisplay(final AnonymUser user, final String userId, String question) {
		if (question != null) {
			filter.selectNone();
			questionsController.loadQuestion("question/true/" + question,
					new SimpleCallback() {
						@Override
						public void onEvent() {
							handleShow(user, userId);
						}
					});
		} else {
			handleShow(user, userId);
		}
	}

	private void handleShow(AnonymUser user, String userId) {
		if (!user.getId().equals(userId)) {
			showQuestionsToMe();
		} else {
			showAllQuestions();
		}
	}

	private void handleFilter(AnonymUser user, String userId, boolean areFriends) {
		if (!user.getId().equals(userId)) {
			this.questionsController.setReadOnlyMode(true);

			if (areFriends) {
				filter.hideAllQuestions();
				filter.hideSeparator1();

				filter.setAskedByOthersText(messages.publicQuestions());
				filter.selectAskedByOthers();
			} else {
				filter.removeFromParent();
			}
		} else {
			filter.selectAllQuestions();
		}
	}

	private void addListeners() {

		filter.setAskedByOthersCallback(new SimpleCallback() {
			@Override
			public void onEvent() {
				showQuestionsToMe();
			}
		});

		filter.setAskedByMeCallback(new SimpleCallback() {
			@Override
			public void onEvent() {
				showQuestionsByMe();
			}
		});

		filter.setAllQuestionsCallback(new SimpleCallback() {
			@Override
			public void onEvent() {
				showAllQuestions();
			}
		});
	}

	private void showQuestionsByMe() {

		if (areFriends) {
			questionsController.loadQuestions("question/made/" + userId
					+ "/true/");
		} else {
			questionsController.loadQuestions("question/made/true/");
		}

		selected = Selected.QUESTIONS_BY_ME;
	}

	private void showQuestionsToMe() {
		questionsController.loadQuestions("question/received/" + userId
				+ "/true/");
		selected = Selected.QUESTIONS_TO_ME;
	}

	private void showAllQuestions() {
		questionsController.loadQuestions("question/true/");
		selected = Selected.ALL_QUESTIONS;
	}

	public void updateQuestionList() {

		if (selected == Selected.QUESTIONS_BY_ME) {
			showQuestionsByMe();
			return;
		}
		if (selected == Selected.QUESTIONS_TO_ME) {
			showQuestionsToMe();
			return;
		}
		showAllQuestions();
	}

	public void selectQuestionsByMe() {

		filter.selectAskedByMe();
		showQuestionsByMe();
	}

	public void selectAllQuestions() {

		filter.selectAllQuestions();
		showAllQuestions();
	}

	public void setFriendClickCallback(DataCallback<String> dataCallback) {
		questionsController.setFriendClickCallback(dataCallback);
	}

}
