package com.anonymbook.client.util;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.user.client.Window;

public class DomHelper {

	public static void injectJsInnerText(String javaScript) {
		Element element = Document.get().getElementsByTagName("head")
				.getItem(0);
		HeadElement head = HeadElement.as(element);

		ScriptElement script = Document.get().createScriptElement();
		script.setAttribute("language", "javascript");

		script.setText(javaScript);
		head.appendChild(script);
	}

	public static void injectJsSource(String javaScript) {
		Element element = Document.get().getElementsByTagName("head")
				.getItem(0);
		HeadElement head = HeadElement.as(element);

		ScriptElement script = Document.get().createScriptElement();
		script.setAttribute("language", "javascript");

		script.setSrc(javaScript);
		head.appendChild(script);
	}

	public static String createId() {
		return Document.get().createUniqueId();
	}

	public static native void refresh()
	/*-{
		$wnd.history.go();
	}-*/;

	public static void redirect(String string) {
		Window.Location.assign(string);
	}

}
