package com.anonymbook.server.util;

import java.util.Comparator;

import com.anonymbook.server.domain.Question;

public class QuestionComparator implements Comparator<Question> {

	private final boolean descending;

	public QuestionComparator(boolean descending) {
		this.descending = descending;
	}

	@Override
	public int compare(Question q1, Question q2) {

		Long timestampQ1 = q1.getQuestionTimestamp();
		Long timestampQ2 = q2.getQuestionTimestamp();
		
		if(descending) {
			return timestampQ2.compareTo(timestampQ1);	
		}

		return timestampQ1.compareTo(timestampQ2);
	}
	
}
