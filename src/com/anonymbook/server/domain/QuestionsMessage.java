package com.anonymbook.server.domain;

import java.util.LinkedList;
import java.util.List;

public class QuestionsMessage {

	private List<Question> questions;

	private int amount;

	private long firstTimestamp;

	private long lastTimestamp;

	public QuestionsMessage() {
		this(new LinkedList<Question>(), 0);
	}

	public QuestionsMessage(List<Question> questions, int amount) {
		this.questions = questions;
		this.amount = amount;
		this.lastTimestamp = 0;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public long getFirstTimestamp() {
		return firstTimestamp;
	}

	public void setFirstTimestamp(long firstTimestamp) {
		this.firstTimestamp = firstTimestamp;
	}

	public long getLastTimestamp() {
		return lastTimestamp;
	}

	public void setLastTimestamp(long lastTimestamp) {
		this.lastTimestamp = lastTimestamp;
	}

	@Override
	public String toString() {
		return "Questions [questions=" + questions + ", amount=" + amount
		+ ", firstTimestamp=" + firstTimestamp+ ", lastTimestamp=" + lastTimestamp + "]";
	}

}
