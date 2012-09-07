package com.anonymbook.elastic;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface ElasticResources extends ClientBundle {

	public static final ElasticResources instance = GWT.create(ElasticResources.class);

	@Source("jquery.elastic.js")
	public TextResource script();

}
