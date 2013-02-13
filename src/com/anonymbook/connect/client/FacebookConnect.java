package com.anonymbook.connect.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window.Location;

public class FacebookConnect implements EntryPoint {

	@Override
	public void onModuleLoad() {
		injectJsSource("https://connect.facebook.net/" + getLocale() + "/all.js");
	}

	private String getLocale() {
		String localeQuery = Location.getParameter(LocaleInfo.getLocaleQueryParam());
		String localeCookie = Cookies.getCookie(LocaleInfo.getLocaleCookieName());
		
		if (localeQuery != null) {
			return localeQuery;
		}
		if (localeCookie != null) {
			return localeCookie;
		}
		
		return "en_US";
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

}
