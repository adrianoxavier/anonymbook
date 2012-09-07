package com.anonymbook.client.ui.question;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class QuestionModel {

	private String key;

	private String question;

	private double questionTsAgo;

	private String owner;

	private String recipientId;

	private List<AnswerModel> answersList;

	public QuestionModel(JSONObject question) {

		answersList = new LinkedList<AnswerModel>();

		JSONValue questionKey = question.get("key");
		this.key = questionKey.isString().stringValue();

		JSONValue questionText = question.get("question");
		this.question = questionText.isString().stringValue();

		JSONValue questionTs = question.get("questionTimestamp");
		this.questionTsAgo = questionTs.isNumber().doubleValue();

		JSONValue recipient = question.get("recipient");
		if (recipient != null) {
			this.recipientId = recipient.isString().stringValue();
		}

		JSONValue owner = question.get("owner");
		if (owner != null) {
			this.owner = owner.isString().stringValue();
		}

		JSONValue answer = question.get("answer");
		JSONValue answerTS = question.get("answerTimestamp");

		if (answer != null) {
			AnswerModel model = new AnswerModel();
			model.setAnswer(answer.isString().stringValue());
			model.setAnswerTsAgo(answerTS.isNumber().doubleValue());
			answersList.add(model);
		} else if (question.get("answers") != null) {
			JSONArray answers = question.get("answers").isArray();

			for (int i = 0; i < answers.size(); i++) {
				answersList.add(new AnswerModel(answers.get(i).isObject()));
			}
		}

	}

	public QuestionModel(String key, String question, double questionTsAgo,
			String recipientId, List<AnswerModel> answersList) {
		this.key = key;
		this.question = question;
		this.questionTsAgo = questionTsAgo;
		this.recipientId = recipientId;
		this.answersList = answersList;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public double getQuestionTsAgo() {
		return questionTsAgo;
	}

	public void setQuestionTsAgo(double questionTsAgo) {
		this.questionTsAgo = questionTsAgo;
	}

	public String getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

	public void setAnswersList(List<AnswerModel> answersList) {
		this.answersList = answersList;
	}

	public List<AnswerModel> getAnswersList() {
		return answersList;
	}

	@Override
	public String toString() {
		return "QuestionModel [key=" + key + ", question=" + question
				+ ", questionTsAgo=" + questionTsAgo
				+ ", recipientId=" + recipientId + ", answersList="
				+ answersList.size() + "]";
	}

}
