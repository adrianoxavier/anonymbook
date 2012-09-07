package com.anonymbook.server.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

@javax.persistence.Entity
public class Question implements Serializable {

	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;

	private String question;

	private Long questionTimestamp;

	private String author;

	private String recipient;

	private boolean hasAnswer;

	private String answer;

	private Long answerTimestamp;

	@Transient
	private String owner;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Answer> answers;

	public Question(Entity entity) {
		this.key = entity.getKey();
		this.question = (String) entity.getProperty("question");
		this.author = (String) entity.getProperty("author");
		this.recipient = (String) entity.getProperty("recipient");
		this.hasAnswer = (Boolean) entity.getProperty("hasAnswer");
		this.answer = (String) entity.getProperty("answer");
		this.questionTimestamp = (Long) entity.getProperty("questionTimestamp");
		this.answerTimestamp = (Long) entity.getProperty("answerTimestamp");

		if (recipient == null) {
			this.owner = this.author;
		}
	}

	public Question() {
		this(null, null, null, new Date().getTime());
	}

	public Question(String author, String question) {
		this(author, question, null, new Date().getTime());
	}

	public Question(String author, String question, String recipient) {
		this(author, question, recipient, new Date().getTime());
	}

	public Question(String author, String question, String recipient,
			Long timestamp) {
		this.author = author;
		this.question = question;
		this.recipient = recipient;
		this.questionTimestamp = timestamp;
		this.hasAnswer = false;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Long getQuestionTimestamp() {
		return questionTimestamp;
	}

	public void setQuestionTimestamp(Long questionTimestamp) {
		this.questionTimestamp = questionTimestamp;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public boolean hasAnswer() {
		return hasAnswer;
	}

	public void setHasAnswer(boolean hasAnswer) {
		this.hasAnswer = hasAnswer;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;

		setHasAnswer(answer != null);
	}

	public Long getAnswerTimestamp() {
		return answerTimestamp;
	}

	public void setAnswerTimestamp(Long answerTimestamp) {
		this.answerTimestamp = answerTimestamp;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Answer newAnswer(String answer, String author) {
		this.hasAnswer = true;

		if (this.answers == null) {
			this.answers = new LinkedList<Answer>();
		}

		Answer newAnswer = new Answer(author, answer, this.key);
		this.answers.add(newAnswer);

		return newAnswer;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj.getClass().equals(this.getClass()))) {
			return false;
		}
		Question other = (Question) obj;

		return key.equals(other.key);
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append("Question [ ");
		appendIfNotNull(sb, "question", question);
		appendIfNotNull(sb, "question-date", questionTimestamp);
		appendIfNotNull(sb, "author", author);
		appendIfNotNull(sb, "recipient", recipient);
		appendIfNotNull(sb, "hasAnswer", hasAnswer);
		appendIfNotNull(sb, "answer", answer);
		appendIfNotNull(sb, "answer-date", answerTimestamp);
		appendIfNotNull(sb, "answers", answers);
		sb.append("]");

		return sb.toString();
	}

	private void appendIfNotNull(StringBuilder builder, String name,
			Object value) {
		if (value != null) {
			builder.append(name).append("=").append(value).append(" ");
		}
	}

}
