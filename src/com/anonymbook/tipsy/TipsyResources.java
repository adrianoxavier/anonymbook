package com.anonymbook.tipsy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface TipsyResources extends ClientBundle {

	public static final TipsyResources instance = GWT.create(TipsyResources.class);

	@Source("tipsy.css")
	public CssResource style();
	
	@Source("jquery.tipsy.js")
	public TextResource script();

	@Source("images/tipsy.gif")
	public ImageResource tipsyGif();
}

