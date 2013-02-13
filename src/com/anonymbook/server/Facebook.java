package com.anonymbook.server;

import static java.lang.String.format;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public final class Facebook {

	private static final String fbGraphUrl = "https://graph.facebook.com/";

	private static final String fbApiUrl = "https://api.facebook.com/";

	private static final String appId = "100923886651872";

	private static final String appSecret = "91cac1dffb6c379194ef24cbcad46016";

	private static Logger logger = LoggerFactory.getLogger(Facebook.class);

	private static String encode(String url) throws UnsupportedEncodingException {
		return URLEncoder.encode(url, "UTF-8");
	}

	public static String getAccessToken() {

		String token = null;

		try {
			String param = "oauth/access_token?client_id=" + appId + "&grant_type=client_credentials&client_secret=" + appSecret;

			URL url = new URL(fbGraphUrl + param);

			URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();
			HTTPResponse resp = urlFetchService.fetch(new HTTPRequest(url, HTTPMethod.GET, FetchOptions.Builder.doNotValidateCertificate()));

			String result = new String(resp.getContent());

			if (result != null && result.startsWith("access_token")) {
				token = result;
			}
		} catch (Exception exception) {
			token = null;
			logger.error(format("Ocorreu o erro '%s'.", exception));
		}
		return token;
	}

	public static List<String> getFriendsOnAnonym(String uid, String userToken) {

		List<String> friendList = new LinkedList<String>();

		if (userToken == null) {
			return friendList;
		}
		try {
			String query = "select uid from user where uid in (select uid2 from friend where uid1=" + uid + ") and is_app_user=1";
			String param = "method/fql.query?query=" + encode(query) + "&access_token=" + encode(userToken) + "&format=json";

			URL url = new URL(fbApiUrl + param);

			URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();
			HTTPResponse resp = urlFetchService.fetch(new HTTPRequest(url, HTTPMethod.GET, FetchOptions.Builder.doNotValidateCertificate()));

			String result = new String(resp.getContent());

			JsonArray json = new JsonParser().parse(result).getAsJsonArray();

			for (int i = 0; i < json.size(); i++) {
				friendList.add(json.get(i).getAsJsonObject().get("uid").getAsString());
			}

		} catch (Exception exception) {
			logger.error(format("Ocorreu o erro '%s'.", exception));
		}

		return friendList;
	}

	public static String sendAppNotify(String userId, String message) {

		String token = getAccessToken();

		if (token == null) {
			return null;
		}

		String result = null;

		try {
			token = token.split("=")[1];

			String param = userId + "/apprequests?message=" + encode(message) + "&access_token=" + encode(token)+ "&method=post";

			URL url = new URL(fbGraphUrl + param);

			URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();
			HTTPResponse resp = urlFetchService.fetch(new HTTPRequest(url, HTTPMethod.GET, FetchOptions.Builder.doNotValidateCertificate()));

			result = new String(resp.getContent());

		} catch (Exception exception) {
			logger.error(format("Ocorreu o erro '%s'.", exception));
		}

		return result;
	}

	public static JsonElement getAppRequests(String userId) {

		String token = getAccessToken();

		if (token == null) {
			return null;
		}

		JsonElement json = null;

		try {
			token = token.split("=")[1];

			String param = userId + "/apprequests?access_token="
					+ encode(token);

			URL url = new URL(fbGraphUrl + param);

			URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();
			HTTPResponse resp = urlFetchService.fetch(new HTTPRequest(url, HTTPMethod.GET, FetchOptions.Builder.doNotValidateCertificate()));

			String result = new String(resp.getContent());

			json = new JsonParser().parse(result);

		} catch (Exception exception) {
			logger.error(format("Ocorreu o erro '%s'.", exception));
		}

		return json;
	}

	public static JsonElement getAppRequestInfo(String requestId) {

		String token = getAccessToken();

		if (token == null) {
			return null;
		}

		JsonElement json = null;

		try {
			token = token.split("=")[1];

			String param = requestId + "&access_token=" + encode(token);

			URL url = new URL(fbGraphUrl + param);

			URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();
			HTTPResponse resp = urlFetchService.fetch(new HTTPRequest(url, HTTPMethod.GET, FetchOptions.Builder.doNotValidateCertificate()));

			String result = new String(resp.getContent());

			json = new JsonParser().parse(result);

		} catch (Exception exception) {
			logger.error(format("Ocorreu o erro '%s'.", exception));
		}
		return json;
	}

	public static String removeAppRequest(String requestId) {

		String token = getAccessToken();

		if (token == null) {
			return null;
		}

		String result = null;

		try {
			token = token.split("=")[1];

			String param = requestId + "&access_token=" + encode(token);
			URL url = new URL(fbGraphUrl + param);

			URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();
			HTTPResponse resp = urlFetchService.fetch(new HTTPRequest(url, HTTPMethod.DELETE, FetchOptions.Builder.doNotValidateCertificate()));

			result = new String(resp.getContent());

		} catch (Exception exception) {
			logger.error(format("Ocorreu o erro '%s'.", exception));
		}
		return result;
	}

}
