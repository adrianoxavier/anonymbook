package com.anonymbook.jquery.tools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface JQToolsExposeResources extends ClientBundle {

	public static final JQToolsExposeResources instance = GWT.create(JQToolsExposeResources.class);

	@Source("jquery.tools.expose.min.js")
	public TextResource script();

}
