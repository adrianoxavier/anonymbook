package com.anonymbook.client.controller;

import com.anonymbook.client.event.DataCallback;
import com.anonymbook.client.event.QuestionCallBack;
import com.anonymbook.client.event.SimpleCallback;
import com.anonymbook.client.request.AnonymRequest;
import com.anonymbook.client.request.AnonymResponse;
import com.anonymbook.client.resources.AnonymUser;
import com.anonymbook.client.ui.components.OlderQuestion;
import com.anonymbook.client.ui.question.QuestionUI;
import com.anonymbook.client.util.Anonym;
import com.anonymbook.client.util.FB;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.HTMLPanel;

public class QuestionsPaging {

	private String userId;

	private final HTMLPanel answerBody;

	private final OlderQuestion olderQuestion;

	private int pageSize;

	private int pageItems;

	private Number lastTimestamp;

	private int totalItems;

	private boolean clearBeforeLoad;

	private QuestionCallBack callback;

	private AnonymUser user;

	public QuestionsPaging(AnonymUser user, String userId,
			HTMLPanel answerBody, OlderQuestion olderQuestion,
			QuestionCallBack callback, int pageSize) {
		this.userId = userId;
		this.user = user;
		this.answerBody = answerBody;
		this.olderQuestion = olderQuestion;
		this.pageSize = pageSize;
		this.callback = callback;
		this.clearBeforeLoad = true;
		this.pageItems = 0;
		this.totalItems = 0;
		this.lastTimestamp = 0;
	}

	public boolean isClearBeforeLoad() {
		return clearBeforeLoad;
	}

	public void setClearBeforeLoad(boolean clearBeforeLoad) {
		this.clearBeforeLoad = clearBeforeLoad;
	}

	public void loadQuestion(String requestUrl, final boolean readOnly,
			final SimpleCallback onFailure) {

		handleClear();

		String request = requestUrl;

		AnonymRequest.post(request, new AnonymResponse() {
			@Override
			public void onResponse(JSONValue response) {
				if (response == null) {
					onFailure.onEvent();
				} else {
					addQuestionToWall(response, readOnly);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				onFailure.onEvent();
			}
		});
	}

	public void loadQuestions(String requestUrl, final boolean readOnly) {

		handleClear();

		String request = requestUrl + lastTimestamp.longValue() + "/"
				+ pageSize;

		Anonym.postAndAlertOnFailure(request, new DataCallback<JSONValue>() {
			@Override
			public void onEvent(JSONValue response) {
				addQuestionsToWall(response, readOnly);
			}
		});
	}

	private void handleClear() {
		if (clearBeforeLoad) {
			answerBody.clear();
		}
	}

	private void addQuestionToWall(JSONValue response, final boolean readOnly) {

		JSONObject result = response.isObject();
		if (result == null) {
			return;
		}

		QuestionUI questionUI = new QuestionUI(user, userId, result, callback);
		if (readOnly) {
			questionUI.setReadOnly();
		} else {
			questionUI.setReSendVisible(false);
		}
		answerBody.add(questionUI);

		FB.resize();
	}

	private void addQuestionsToWall(JSONValue response, final boolean readOnly) {

		JSONObject result = response.isObject();
		if (result.get("questions") == null) {
			return;
		}
		JSONArray array = result.get("questions").isArray();

		controlPaging(array.size(), result.get("amount"),
				result.get("lastTimestamp"));

		QuestionUI questionUI;
		for (int i = 0; i < array.size(); i++) {

			questionUI = new QuestionUI(user, userId, array.get(i).isObject(),
					callback);
			if (readOnly) {
				questionUI.setReadOnly();
			} else {
				questionUI.setReSendVisible(false);
			}
			answerBody.add(questionUI);
		}

		FB.resize();
	}

	private void controlPaging(int itemsLoaded, JSONValue total,
			JSONValue lastTs) {

		this.pageItems = pageItems + itemsLoaded;
		this.totalItems = (int) total.isNumber().doubleValue();
		this.lastTimestamp = (Number) lastTs.isNumber().doubleValue();

		if (totalItems <= pageItems) {
			olderQuestion.setStateEndOlderQuestions();
		} else {
			olderQuestion.setStateOlderQuestion();
		}
	}

	public void reset(int pageSize) {
		this.pageSize = pageSize;
		this.pageItems = 0;
		this.totalItems = 0;
		this.lastTimestamp = 0;

		olderQuestion.setStateEndOlderQuestions();
	}

}
