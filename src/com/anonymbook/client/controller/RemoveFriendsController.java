package com.anonymbook.client.controller;

import com.anonymbook.client.event.BooleanCallback;
import com.anonymbook.client.event.DataCallback;
import com.anonymbook.client.event.JavaScriptCallback;
import com.anonymbook.client.event.SimpleCallback;
import com.anonymbook.client.resources.AnonymMessages;
import com.anonymbook.client.util.Anonym;
import com.anonymbook.client.util.FB;
import com.anonymbook.lightface.LightFace;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONValue;

public final class RemoveFriendsController {

	private static AnonymMessages messages = AnonymMessages.instance;

	private RemoveFriendsController() {
	}

	public static void removeFriend(final String friendId, final SimpleCallback callback) {

		FB.user(friendId, new JavaScriptCallback() {
			@Override
			public void onEvent(JavaScriptObject response) {
				String name = Anonym.get(response, "name");
				askBeforeRemove(friendId, name, callback);
			}
		});

	}

	private static void askBeforeRemove(final String friendId,
			final String friendName, final SimpleCallback callback) {

		String message = messages.confirmRemoveFriend(friendName);
		
		LightFace.confirm(message, messages.removeFriend(), messages.cancel(), new BooleanCallback() {
			@Override
			public void onEvent(boolean confirm) {
				if (confirm) {
					String request = "friend/remove/" + friendId;
					Anonym.postAndAlertOnFailure(request, new DataCallback<JSONValue>() {
						@Override
						public void onEvent(JSONValue data) {
							callback.onEvent();
						}
					});
				}
			}
		});
	}

}
