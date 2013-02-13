package com.anonymbook.lightface;

import com.anonymbook.client.event.BooleanCallback;
import com.anonymbook.client.util.DomHelper;

public final class LightFace {

	private LightFace() {
	}

	public static void injectResources() {
		LightFaceResources.instance.style().ensureInjected();
		DomHelper.injectJsInnerText(LightFaceResources.instance.script().getText());
	}

	public static native void alert(String message)
	/*-{
		$wnd.lighfaceAlert('Anonymbook', message);
	}-*/;
	
	public static native void alert(String message, int width, int height)
	/*-{
		$wnd.lighfaceAlert('Anonymbook', message, width, height);
	}-*/;
	
	public static void confirm(String message,
			BooleanCallback callback){
		confirm(message, "Yes", "No", callback);
	}

	public static native void confirm(String message, String yesText, String noText,
			BooleanCallback callback)
	/*-{
		$wnd.lighfaceConfirm('Anonymbook', message, yesText, noText,
			function(response) {
				callback.@com.anonymbook.client.event.BooleanCallback::onEvent(Z)(response);
			}
		);
	}-*/;
}
