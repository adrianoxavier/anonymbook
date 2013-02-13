package com.anonymbook.server.util;

import java.util.Comparator;
import com.anonymbook.server.domain.Answer;

public class AnswerComparator implements Comparator<Answer> {

	private final boolean descending;

	public AnswerComparator(boolean descending) {
		this.descending = descending;
	}

	@Override
	public int compare(Answer q1, Answer q2) {

		Long timestampA1 = q1.getTimestamp();
		Long timestampA2 = q2.getTimestamp();
		
		if(descending) {
			return timestampA2.compareTo(timestampA1);	
		}

		return timestampA1.compareTo(timestampA2);
	}
	
}
