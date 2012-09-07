package com.anonymbook.client.event;

public interface QuickQuestionCallBack {	
	
	public void onSubmitClick(String question, String recipientId, String recipientName);
	
	public void onSkipClick();
}
