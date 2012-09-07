package com.anonymbook.elastic;

import com.anonymbook.client.util.DomHelper;

public final class Elastic {

	private Elastic() {
	}

	public static void injectResources() {
		DomHelper.injectJsInnerText(ElasticResources.instance.script().getText());
	}
}
