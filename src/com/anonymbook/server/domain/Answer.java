package com.anonymbook.server.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

@javax.persistence.Entity
public class Answer implements Serializable {

	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;

	private String author;

	private String answer;
	
	private Long timestamp;

	private Key questionKey;
	
	@Transient
	private boolean isAuthor;

	public Answer(Entity entity) {
		this.key = entity.getKey();
		this.author = (String) entity.getProperty("author");
		this.answer = (String) entity.getProperty("answer");
		this.questionKey = (Key) entity.getProperty("questionKey");
		this.timestamp = (Long) entity.getProperty("timestamp");
	}

	public Answer(String author, String answer, Key questionKey) {
		this.author = author;
		this.answer = answer;
		this.questionKey = questionKey;
		this.timestamp = new Date().getTime();
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public boolean isAuthor() {
		return isAuthor;
	}

	public void setAuthor(boolean isAuthor) {
		this.isAuthor = isAuthor;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Key getQuestionKey() {
		return questionKey;
	}

	public void setQuestionKey(Key questionKey) {
		this.questionKey = questionKey;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("Answer [");

		if (key != null) {
			builder.append("key=").append(key).append(", ");
		}
		if (author != null) {
			builder.append("author=").append(author).append(", ");
		}
		if (answer != null) {
			builder.append("answer=").append(answer).append(", ");
		}
		if (timestamp != null) {
			builder.append("timestamp=").append(timestamp).append(", ");
		}
		if (questionKey != null) {
			builder.append("question=").append(questionKey);
		}

		builder.append("]");
		return builder.toString();
	}

}
