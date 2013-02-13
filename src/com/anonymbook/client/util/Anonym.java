package com.anonymbook.client.util;

import java.util.Arrays;
import java.util.List;

import com.anonymbook.client.event.DataCallback;
import com.anonymbook.client.event.StringCallback;
import com.anonymbook.client.request.AnonymRequest;
import com.anonymbook.client.request.AnonymResponse;
import com.anonymbook.client.resources.AnonymMessages;
import com.anonymbook.lightface.LightFace;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public final class Anonym {

	private static AnonymMessages messages = AnonymMessages.instance;

	private Anonym() {
	}

	public static native String get(JavaScriptObject object, String key)
	/*-{
		return object[key];
	}-*/;
	
	public static native JavaScriptObject getObj(JavaScriptObject object, String key)
	/*-{
		return object[key];
	}-*/;

	public static native void redirect(String url)
	/*-{
		if ($wnd.parent.frames.length != 0) {
			$wnd.parent.location = url;
		} else {
			$wnd.location = url;
		}
	}-*/;

	public static String getTimeAgo(double answerTSAgo) {

		double auxTime = answerTSAgo;

		auxTime = auxTime / 1000;

		if (auxTime < 60) {
			return messages.secondsAgo((int) auxTime);
		}
		auxTime = auxTime / 60;

		if (auxTime < 60) {
			return messages.minutesAgo((int) auxTime);
		}
		auxTime = auxTime / 60;

		if (auxTime < 24) {
			return messages.hoursAgo((int) auxTime);
		}
		auxTime = auxTime / 24;

		if (auxTime < 30) {
			return messages.daysAgo((int) auxTime);
		}

		return messages.monthsAgo((int) auxTime);
	}

	public static String getUsersInfoQuery(JSONArray array) {
		String query = "SELECT uid, name FROM user WHERE";

		if (array == null || array.size() == 0) {
			return null;
		}

		query += " uid=" + array.get(0).isString().stringValue();
		for (int i = 1; i < array.size(); i++) {
			query += " or uid=" + array.get(i).isString().stringValue();
		}

		return query;
	}

	public static String getAnonymFriendsQuery(String uid) {
		String query = "select uid, name from user where uid in (select uid2 from friend where uid1="
				+ uid + ") and is_app_user=1";

		return query;
	}

	public static String getNonAnonymFriendsQuery(String uid) {
		String query = "select uid, name from user where uid in (select uid2 from friend where uid1="
				+ uid + ") and is_app_user=0";

		return query;
	}

	public static String getFriendsQuery(String uid) {
		String query = "select uid, name from user where uid in (select uid2 from friend where uid1="
				+ uid + ")";

		return query;
	}

	public static void friends(String userId,
			final DataCallback<JSONValue> callback) {

		AnonymRequest.post("friends/get/" + userId, new AnonymResponse() {
			@Override
			public void onResponse(JSONValue result) {
				callback.onEvent(result);
			}
		});
	}

	public static void postAndAlertOnFailure(String request) {
		postAndAlertOnFailure(request, null, null);
	}

	public static void postAndAlertOnFailure(String request,
			DataCallback<JSONValue> callback) {
		postAndAlertOnFailure(request, null, callback);
	}

	public static void postAndAlertOnFailure(String request, String params) {
		postAndAlertOnFailure(request, params, null);
	}

	public static void postAndAlertOnFailure(String request, String params,
			final DataCallback<JSONValue> callback) {
		AnonymRequest.post(request, params, new AnonymResponse() {
			@Override
			public void onResponse(JSONValue response) {
				JSONObject result = response.isObject();
				JSONValue success = result.get("success");

				if (success != null && !success.isBoolean().booleanValue()) {
					JSONValue message = result.get("message");
					LightFace.alert(message.isString().stringValue());
				} else {
					if (callback != null) {
						callback.onEvent(response);
					}
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				LightFace.alert(messages.errorMessage());
			}
		});
	}

	public static native void shortUrl(String url, StringCallback callback)
	/*-{
		var username = "anonymbook";
		var key="R_f65cfdb209f6dbe968abbec33963ce39";
		
		$wnd.$.ajax({
			url : "https://api-ssl.bitly.com/v3/shorten",
			data : {
				longUrl : url,
				apiKey : key,
				login : username
			},
			dataType : "jsonp",
			success : function(v) {
				callback.@com.anonymbook.client.event.StringCallback::onEvent(Ljava/lang/String;)(v.data.url);
			}
		});
			
	}-*/;

	private static List<Character> invalids = Arrays.asList('&');

	public static boolean validadeMessage(String message) {

		if (message == null || message.equals("")) {
			return false;
		}

		return true;
	}
}
