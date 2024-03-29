package com.anonymbook.client;

import java.util.Date;

import com.anonymbook.autoresize.Autoresize;
import com.anonymbook.client.event.JavaScriptCallback;
import com.anonymbook.client.resources.AnonymMessages;
import com.anonymbook.client.util.Anonym;
import com.anonymbook.client.util.DomHelper;
import com.anonymbook.client.util.FB;
import com.anonymbook.lightface.LightFace;
import com.anonymbook.toastmessage.ToastMessage;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class Anonymbook implements EntryPoint {

	private AnonymMessages messages = AnonymMessages.instance;

	private boolean loaded = false;

	private final String appId = "100923886651872";

	@Override
	public void onModuleLoad() {

		if (handleAppRequest()) {
			return;
		}

		LightFace.injectResources();
		Autoresize.injectResources();
		ToastMessage.injectResources();
		
		init();
	}

	private void init() {
		FB.init(appId);

		JavaScriptObject session = FB.getSession();
				
		if (session != null) {
			init(session);
		} else {
			renderLoginButton();
		}

		FB.Event.subscribe("auth.authResponseChange", new JavaScriptCallback() {
			@Override
			public void onEvent(JavaScriptObject session) {
				if (Anonym.get(session, "status").equals("connected")) {
					
					Document.get().getElementById("facebook-login").removeFromParent();
					RootPanel.get().clear();
					
					init(Anonym.getObj(session, "authResponse"));
				}
			}
		});

		FB.Event.subscribe("auth.logout", new JavaScriptCallback() {
			@Override
			public void onEvent(JavaScriptObject data) {
				loaded = false;
				reload();
				loaded = false;
			}
		});
	}

	private void reload() {
		if (!loaded) {
			DomHelper.refresh();
			loaded = true;
		}
	}

	private boolean handleAppRequest() {
		String ref = Window.Location.getParameter("ref");
		String requests = Window.Location.getParameter("request_ids");

		if (ref != null && requests != null) {
			Anonym.redirect("https://www.facebook.com/appcenter/requests");
			return true;
		}
		return false;
	}

	private void renderLoginButton() {
		
		Document doc = Document.get();

		Element loginButton = doc.createElement("fb:login-button");
		loginButton.setInnerText(messages.loginText());
		loginButton.setAttribute("show-face", "true");
		loginButton.setAttribute("max-rows", "1");
		loginButton.setAttribute("width", "400");

		DivElement loginDiv = doc.createDivElement();
		loginDiv.setId("facebook-login");
		loginDiv.appendChild(loginButton);
		loginDiv.getStyle().setProperty("margin", "50px auto");
		loginDiv.getStyle().setProperty("width", "300px");
		loginDiv.getStyle().setProperty("textAlign", "center");

		doc.getBody().appendChild(loginDiv);
	}

	private void init(JavaScriptObject session) {
		
		String uid = Anonym.get(session, "userID");

		if (!hasLocale()) {
			FB.user(uid, new JavaScriptCallback() {
				@Override
				public void onEvent(JavaScriptObject data) {
					setLocale(Anonym.get(data, "locale"));
					reload();
				}
			});
			return;
		}
		
		AnonymController controller = new AnonymController(uid, Anonym.get(session, "accessToken"));

		String user = Window.Location.getParameter("anonym_user");
		String question = Window.Location.getParameter("anonym_question");

		if (user != null) {
			controller.getUserNameAndLoadView(user, question);
		} else {
			controller.init();
		}
	}

	@SuppressWarnings("deprecation")
	private void setLocale(String localeName) {
		String cookieName = LocaleInfo.getLocaleCookieName();

		Date expires = new Date();
		expires.setYear(expires.getYear() + 1);
		Cookies.setCookie(cookieName, localeName, expires);
	}

	private boolean hasLocale() {
		String cookieName = LocaleInfo.getLocaleCookieName();
		return Cookies.getCookie(cookieName) != null;
	}

}
