package com.anonymbook.jquery.tools;

import com.anonymbook.client.util.DomHelper;

public final class JQToolsExpose {

	private JQToolsExpose() {
	}

	public static void injectResources() {
		DomHelper.injectJsInnerText(JQToolsExposeResources.instance.script().getText());
	}
}
