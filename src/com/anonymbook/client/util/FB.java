package com.anonymbook.client.util;

import com.anonymbook.client.event.JavaScriptCallback;
import com.google.gwt.core.client.JavaScriptObject;

public class FB {

	public static void init(String appId) {
		init(appId, true, true, true);
	}

	public static native void init(String appId, boolean status,
			boolean cookie, boolean xfbml)
	/*-{
		$wnd.FB.init({
			appId : appId,
			status : status,
			cookie : cookie,
			xfbml : xfbml
		});

		$wnd.FB.Canvas.setAutoResize(600);
	}-*/;

	public static native JavaScriptObject getLoginStatus(
			JavaScriptCallback callback)
	/*-{
		$wnd.FB
				.getLoginStatus(function(response) {
					if (response.session) {
						callback.@com.anonymbook.client.event.JavaScriptCallback::onEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(response.session);
					}
				});
	}-*/;

	public static native JavaScriptObject login(String permissions,
			JavaScriptCallback callback)
	/*-{
		$wnd.FB
				.login(
						function(response) {
							if (response.session && response.perms) {
								callback.@com.anonymbook.client.event.JavaScriptCallback::onEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(response.session);
							}
						}, {
							perms : permissions
						});
	}-*/;

	public static native JavaScriptObject logout(JavaScriptCallback callback)
	/*-{
		$wnd.FB
				.logout(function(response) {
					callback.@com.anonymbook.client.event.JavaScriptCallback::onEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(response);
				});
	}-*/;

	public static native JavaScriptObject getSession()
	/*-{
		return $wnd.FB.getSession();
	}-*/;
	
	public static native JavaScriptObject scrollTo(int x, int y)
	/*-{
		return $wnd.FB.Canvas.scrollTo(x,y);
	}-*/;
	
	public static native JavaScriptObject getPageInfo()
	/*-{
		return $wnd.FB.Canvas.getPageInfo();
	}-*/;

	public static native JavaScriptObject apiFql(String q,
			JavaScriptCallback callback)
	/*-{
		$wnd.FB
				.api(
						{
							method : 'fql.query',
							query : q
						},
						function(response) {
							callback.@com.anonymbook.client.event.JavaScriptCallback::onEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(response);
						});
	}-*/;

	public static native void sendFeed(String message, String title, 
			String description, String caption, String picture, String link)
	/*-{
		$wnd.FB.ui({
			method : 'feed',
			name : title,
			link : link,
			caption : caption,
			picture : picture,
			description : description,
			message : message
		}, function(response) {
			if (response && response.post_id) {
				// feed posted.
			}
		});

	}-*/;

	public static class Event {

		public static native JavaScriptObject subscribe(String name,
				JavaScriptCallback callback)
		/*-{
			var cb = function(response) {
				callback.@com.anonymbook.client.event.JavaScriptCallback::onEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(response);
			};

			$wnd.FB.Event.subscribe(name, cb);

			return cb;
		}-*/;

		public static native void unsubscribe(String name,
				JavaScriptObject callback)
		/*-{
			$wnd.FB.Event.unsubscribe(name, callback);
		}-*/;

	}

	public static String image(String userId, String type) {
		return "https://graph.facebook.com/" + userId + "/picture?type=" + type;
	}

	public static native void friends(String accessToken,
			JavaScriptCallback callback)
	/*-{
		$wnd.FB.api(
						'/me/friends',
						{
							acess_token : accessToken
						},
						function(response) {
							callback.@com.anonymbook.client.event.JavaScriptCallback::onEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(response.data);
						});
	}-*/;

	public static native void user(String userId, JavaScriptCallback callback)
	/*-{
		$wnd.FB.api(
						userId,
						function(response) {
							callback.@com.anonymbook.client.event.JavaScriptCallback::onEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(response);
						});
	}-*/;

	public static native void resize() /*-{
		$wnd.FB.Canvas.setSize();
	}-*/;

}
