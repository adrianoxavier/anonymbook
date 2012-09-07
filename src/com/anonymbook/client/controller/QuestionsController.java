package com.anonymbook.client.controller;

import static com.anonymbook.client.util.Anonym.postAndAlertOnFailure;

import com.anonymbook.client.event.BooleanCallback;
import com.anonymbook.client.event.DataCallback;
import com.anonymbook.client.event.JavaScriptCallback;
import com.anonymbook.client.event.QuestionCallBack;
import com.anonymbook.client.event.SimpleCallback;
import com.anonymbook.client.resources.AnonymMessages;
import com.anonymbook.client.resources.AnonymUser;
import com.anonymbook.client.ui.components.OlderQuestion;
import com.anonymbook.client.ui.question.AnswerModel;
import com.anonymbook.client.ui.question.AnswerUI;
import com.anonymbook.client.ui.question.QuestionModel;
import com.anonymbook.client.ui.question.QuestionUI;
import com.anonymbook.client.util.Anonym;
import com.anonymbook.client.util.FB;
import com.anonymbook.lightface.LightFace;
import com.anonymbook.toastmessage.ToastMessage;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.HTMLPanel;

public class QuestionsController implements QuestionCallBack {

	private static final int PAGE_SIZE = 10;

	private AnonymMessages messages = AnonymMessages.instance;

	private HTMLPanel answerBody;

	private OlderQuestion olderQuestion;

	private boolean readOnlyMode;

	private QuestionsPaging questionsPaging;

	private AnonymUser user;

	private String userId;

	private String lastRequest;

	private DataCallback<String> dataCallback;

	public QuestionsController(AnonymUser user, String userId,
			HTMLPanel answerBody, OlderQuestion olderQuestion) {
		this.answerBody = answerBody;
		this.olderQuestion = olderQuestion;
		this.readOnlyMode = false;

		this.user = user;
		this.userId = userId;

		this.questionsPaging = new QuestionsPaging(user, userId, answerBody,
				olderQuestion, this, PAGE_SIZE);

		addListeners();
	}

	private void addListeners() {
		olderQuestion.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				olderQuestion.setStateLoading();
				questionsPaging.setClearBeforeLoad(false);
				questionsPaging.loadQuestions(lastRequest, readOnlyMode);
				questionsPaging.setClearBeforeLoad(true);
			}
		});
	}

	public boolean isReadOnlyMode() {
		return readOnlyMode;
	}

	public void setReadOnlyMode(boolean readOnlyMode) {
		this.readOnlyMode = readOnlyMode;
	}

	@Override
	public void onAnswer(final QuestionUI question) {

		String answer = question.getQuestionAnswer();

		if (!Anonym.validadeMessage(answer)) {
			return;
		}

		String param = "key=" + question.getQuestionKey() + "&answer="
				+ URL.encodeQueryString(answer);

		Anonym.postAndAlertOnFailure("question/answer", param,
				new DataCallback<JSONValue>() {
					@Override
					public void onEvent(JSONValue data) {
						QuestionModel model = question.getModel();
						AnswerModel answer = null;

						if (data.isObject().get("success") != null) {
							answer = new AnswerModel();
							answer.setAnswer(question.getQuestionAnswer());
						} else {
							answer = new AnswerModel(data.isObject());
						}

						answer.setAnswerTsAgo(2000);
						model.getAnswersList().add(0, answer);
						question.updateQuestion(model);
					}
				});
	}

	@Override
	public void onDelete(final QuestionUI question) {

		LightFace.confirm(messages.confirmRemoveQuestion(),
				messages.removeQuestion(), messages.cancel(),
				new BooleanCallback() {
					@Override
					public void onEvent(boolean confirm) {
						if (confirm) {
							removeQuestion(question);
						}
					}
				});
	}

	@Override
	public void onAnswerDelete(final QuestionUI question, final AnswerUI answer) {
		LightFace.confirm(messages.confirmRemoveAnswer(),
				messages.removeAnswer(), messages.cancel(),
				new BooleanCallback() {
					@Override
					public void onEvent(boolean confirm) {
						if (confirm) {
							removeAnswer(question, answer);
						}
					}
				});
	}

	@Override
	public void onSelect(QuestionUI widget) {

		if (widget.getModel().getRecipientId() == null) {
			dataCallback.onEvent(userId);
		} else {
			dataCallback.onEvent(widget.getModel().getRecipientId());
		}
	}

	@Override
	public void onShare(QuestionUI questionUI) {
		final QuestionModel model = questionUI.getModel();

		FB.user(model.getRecipientId(), new JavaScriptCallback() {
			@Override
			public void onEvent(JavaScriptObject response) {
				handleShare(model, Anonym.get(response, "name"));
			}
		});

	}

	@Override
	public void onSharePublic(QuestionUI questionUI) {
		ShareController.share(questionUI.getModel(), user.getId());
	}

	@Override
	public void onReSend(QuestionUI questionUI) {

		String param = "question="
				+ URL.encodeQueryString(questionUI.getModel().getQuestion());

		Anonym.postAndAlertOnFailure("question/make", param,
				new DataCallback<JSONValue>() {
					@Override
					public void onEvent(JSONValue data) {
						ShareController.share(
								new QuestionModel(data.isObject()),
								user.getId(), new BooleanCallback() {
									@Override
									public void onEvent(boolean data) {
										ToastMessage.success(messages
												.questionReSend());
									}
								});
					}
				});
	}

	private void handleShare(QuestionModel model, String name) {

		String link = "http://apps.facebook.com/anonymbook?anonym_user="
				+ model.getRecipientId();
		String picture = "https://lh5.googleusercontent.com/_n1Zhj7j8juY/TbHIjwugWoI/AAAAAAAAAGE/_XcGrLTeChs/s800/anonym-questionr.png";
		String detail = messages.answeredBy(name);
		String answer = model.getAnswersList().get(0).getAnswer();

		FB.sendFeed("", model.getQuestion(), detail, answer, picture, link);
	}

	private void removeQuestion(final QuestionUI question) {

		String request = "question/remove/" + question.getQuestionKey();

		postAndAlertOnFailure(request, new DataCallback<JSONValue>() {
			@Override
			public void onEvent(JSONValue data) {
				answerBody.remove(question);
			}
		});
	}

	private void removeAnswer(final QuestionUI question, final AnswerUI answer) {

		if (answer.getModel().getKey() == null) {
			return;
		}

		String request = "answer/remove/" + answer.getModel().getKey();

		postAndAlertOnFailure(request, new DataCallback<JSONValue>() {
			@Override
			public void onEvent(JSONValue data) {
				question.removeAnswer(answer);
			}
		});
	}

	public void loadQuestion(String requestUrl, SimpleCallback onFailure) {
		olderQuestion.setVisible(false);
		questionsPaging.reset(PAGE_SIZE);
		questionsPaging.loadQuestion(requestUrl, readOnlyMode, onFailure);
	}

	public void loadQuestions(String requestUrl) {
		olderQuestion.setVisible(true);
		questionsPaging.reset(PAGE_SIZE);
		lastRequest = requestUrl;
		questionsPaging.loadQuestions(requestUrl, readOnlyMode);
	}

	public void setFriendClickCallback(DataCallback<String> dataCallback) {
		this.dataCallback = dataCallback;
	}

}
