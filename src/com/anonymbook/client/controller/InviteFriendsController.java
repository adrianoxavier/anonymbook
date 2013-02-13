package com.anonymbook.client.controller;

import com.anonymbook.client.event.BooleanCallback;
import com.anonymbook.client.event.JavaScriptCallback;
import com.anonymbook.client.resources.AnonymMessages;
import com.anonymbook.client.util.Anonym;
import com.anonymbook.client.util.FB;
import com.anonymbook.lightface.LightFace;
import com.google.gwt.core.client.JavaScriptObject;

public final class InviteFriendsController {

	private static AnonymMessages messages = AnonymMessages.instance;

	public static void showInvite() {
		showAppRequest(messages.inviteTitle(), messages.inviteMessage());
	}

	public static void showInvite(final String userId) {

		FB.user(userId, new JavaScriptCallback() {
			@Override
			public void onEvent(JavaScriptObject response) {
				confirmInvite(userId, Anonym.get(response, "name"));
			}
		});

	}

	private static void confirmInvite(final String userId, String userName) {
		String message = messages.friendNotInAnonymMessage(userName);
		LightFace.confirm(message, messages.friendNotInAnonymYesButton(),
				messages.cancel(), new BooleanCallback() {
					@Override
					public void onEvent(boolean data) {
						if (data) {
							showAppRequest(messages.inviteTitle(),
									messages.inviteMessage(), userId);
						}
					}
				});
	}

	private static native void showAppRequest(String title, String message)
	/*-{
		$wnd.FB.ui({
			method : 'apprequests',
			filters : [ 'app_non_users' ],
			message : message,
			title : title
		}, function(result) {
		});
	}-*/;

	private static native void showAppRequest(String title, String message,
			String to)
	/*-{
		$wnd.FB.ui({
			method : 'apprequests',
			message : message,
			to : to,
			title : title
		}, function(result) {
		});
	}-*/;

}
