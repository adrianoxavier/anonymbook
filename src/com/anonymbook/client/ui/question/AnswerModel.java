package com.anonymbook.client.ui.question;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class AnswerModel {

	private String key;

	private String answer;

	private double answerTsAgo;

	private boolean isAuthor;
	
	public AnswerModel() {
	}

	public AnswerModel(JSONObject object) {

		JSONValue key = object.get("key");
		this.key = key.isString().stringValue();

		JSONValue isAuthor = object.get("isAuthor");
		if (isAuthor != null) {
			this.isAuthor = isAuthor.isBoolean().booleanValue();
		}

		if (object.get("answer") != null) {
			JSONString answer = object.get("answer").isString();
			this.answer = answer.stringValue();

			JSONValue answerTS = object.get("timestamp");
			this.answerTsAgo = answerTS.isNumber().doubleValue();
		}

	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public double getAnswerTsAgo() {
		return answerTsAgo;
	}

	public void setAnswerTsAgo(double answerTsAgo) {
		this.answerTsAgo = answerTsAgo;
	}

	public boolean isAuthor() {
		return isAuthor;
	}

	public void setAuthor(boolean isAuthor) {
		this.isAuthor = isAuthor;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AnswerModel [");
		if (key != null) {
			builder.append("key=");
			builder.append(key);
			builder.append(", ");
		}
		if (answer != null) {
			builder.append("answer=");
			builder.append(answer);
			builder.append(", ");
		}
		builder.append("answerTsAgo=");
		builder.append(answerTsAgo);
		builder.append(", isAuthor=");
		builder.append(isAuthor);
		builder.append("]");
		return builder.toString();
	}
	
}
