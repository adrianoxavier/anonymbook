package com.anonymbook.autoresize;

import com.anonymbook.client.util.DomHelper;

public final class Autoresize {

	private Autoresize() {
	}

	public static void injectResources() {
		DomHelper.injectJsInnerText(AutoresizeResources.instance.script().getText());
	}
}
