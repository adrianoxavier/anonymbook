package com.anonymbook.autoresize;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface AutoresizeResources extends ClientBundle {

	public static final AutoresizeResources instance = GWT.create(AutoresizeResources.class);

	@Source("autoresize.jquery.js")
	public TextResource script();

}
