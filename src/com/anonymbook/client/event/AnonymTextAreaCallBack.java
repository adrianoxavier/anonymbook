package com.anonymbook.client.event;

import com.anonymbook.client.ui.components.AnonymTextArea;


public interface AnonymTextAreaCallBack {
	
	public void onFocus();
	
	public void onLostFocus();
	
	public void onButtonClick(AnonymTextArea textArea);
}
