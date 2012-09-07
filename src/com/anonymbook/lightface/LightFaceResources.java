package com.anonymbook.lightface;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface LightFaceResources extends ClientBundle {

	public static final LightFaceResources instance = GWT.create(LightFaceResources.class);

	@Source("LightFace.css")
	public CssResource style();

	@Source("LightFace-min.js")
	public TextResource script();

	@Source("images/b.png")
	public ImageResource background();

	@Source("images/bl.png")
	public ImageResource bottomLeft();

	@Source("images/br.png")
	public ImageResource bottomRight();

	@Source("images/tl.png")
	public ImageResource topLeft();

	@Source("images/tr.png")
	public ImageResource topRight();

	@Source("images/fbLoader.gif")
	public ImageResource loader();

}
