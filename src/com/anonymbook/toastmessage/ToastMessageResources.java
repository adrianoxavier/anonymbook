package com.anonymbook.toastmessage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface ToastMessageResources extends ClientBundle {

	public static final ToastMessageResources instance = GWT.create(ToastMessageResources.class);

	@Source("jquery.toastmessage.css")
	public CssResource style();

	@Source("jquery.toastmessage.js")
	public TextResource script();

	@Source("images/close.gif")
	public ImageResource close();

	@Source("images/error.png")
	public ImageResource error();

	@Source("images/notice.png")
	public ImageResource notice();

	@Source("images/success.png")
	public ImageResource success();

	@Source("images/warning.png")
	public ImageResource warning();

}
