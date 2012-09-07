package com.anonymbook.client.event;

import com.anonymbook.client.ui.question.AnswerUI;
import com.anonymbook.client.ui.question.QuestionUI;

public interface QuestionCallBack {

	public void onAnswer(QuestionUI questionUI);

	public void onDelete(QuestionUI questionUI);
	
	public void onSelect(QuestionUI questionUI);

	public void onShare(QuestionUI questionUI);
	
	public void onSharePublic(QuestionUI questionUI);
	
	public void onReSend(QuestionUI questionUI);
	
	public void onAnswerDelete(QuestionUI question, AnswerUI answerUi);
}
