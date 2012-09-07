package com.anonymbook.client.request;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AnonymResponse implements AsyncCallback<JSONValue> {

	@Override
	public void onFailure(Throwable caught) {
		GWT.log("Ocorreu o erro: " + caught);
	}

	@Override
	public void onSuccess(JSONValue result) {
		onResponse(result);
	}

	public abstract void onResponse(JSONValue response);

}
