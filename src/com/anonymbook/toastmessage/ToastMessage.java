package com.anonymbook.toastmessage;

import com.anonymbook.client.util.DomHelper;

public final class ToastMessage {

	private ToastMessage() {
	}

	public static void injectResources() {
		ToastMessageResources.instance.style().ensureInjected();
		DomHelper.injectJsInnerText(ToastMessageResources.instance.script()
				.getText());
	}

	public static native void error(String message) 
	/*-{
		$wnd.loadToastMessage(message, "error", false);
	}-*/;

	public static native void notice(String message) 
	/*-{
		$wnd.loadToastMessage(message, "notice", false);
	}-*/;

	public static native void success(String message) 
	/*-{
		$wnd.loadToastMessage(message, "success", false);
	}-*/;

	public static native void warning(String message) 
	/*-{
		$wnd.loadToastMessage(message, "warning", false);
	}-*/;

}
