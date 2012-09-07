package com.anonymbook.tipsy;

import com.anonymbook.client.util.DomHelper;

public final class Tipsy {

	private Tipsy() {
	}

	public static void injectResources() {
		TipsyResources.instance.style().ensureInjected();
		DomHelper.injectJsInnerText(TipsyResources.instance.script().getText());
	}
}
