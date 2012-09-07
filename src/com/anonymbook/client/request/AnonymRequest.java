package com.anonymbook.client.request;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.IsSerializable;

public class AnonymRequest implements IsSerializable {

	private static final String SERVER_BASE_URL = Location.getProtocol() + "//anonymbook.appspot.com/service/";

	public static void get(String url, AnonymResponse callback) {
		get(url, null, callback);
	}

	public static void get(String url, String requestData,
			AnonymResponse callback) {
		createRequestBuilder(RequestBuilder.GET, url, requestData, callback,
				true);
	}

	public static void post(String url, AnonymResponse callback) {
		post(url, null, callback);
	}

	public static void post(String url, String requestData,
			AnonymResponse callback) {
		createRequestBuilder(RequestBuilder.POST, url, requestData, callback,
				true);
	}

	public static void externalGet(String url, AnonymResponse callback) {
		createRequestBuilder(RequestBuilder.GET, url, null, callback, false);
	}

	public static void externalPost(String url, AnonymResponse callback) {
		createRequestBuilder(RequestBuilder.POST, url, null, callback, false);
	}

	private static void createRequestBuilder(RequestBuilder.Method method,
			String url, String requestData, final AnonymResponse callback,
			boolean anonymHost) {

		RequestBuilder builder;

		if (anonymHost) {
			builder = new RequestBuilder(method, URL.encode(SERVER_BASE_URL
					+ url));
		} else {
			builder = new RequestBuilder(method, URL.encode(url));
		}

		if (method.equals(RequestBuilder.POST)) {
			builder.setHeader("Content-type",
					"application/x-www-form-urlencoded;charset=UTF-8");
		}

		try {
			builder.sendRequest(requestData, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request,
						Response response) {

					if (response.getStatusCode() != 200)
						callback.onFailure(new RequestException(response
								.getStatusText()));

					String text = response.getText();
					
					if (text != null && !text.equals("")) {
						callback.onSuccess(JSONParser.parseStrict(text));
					} else {
						callback.onFailure(new RuntimeException("Null response."));
					}
				}

				@Override
				public void onError(Request request, Throwable exception) {
					callback.onFailure(exception);
				}
			});
		} catch (Exception ex) {
			callback.onFailure(ex);
		}
	}

}
