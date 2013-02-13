package com.anonymbook.client.home;

import com.anonymbook.client.event.SimpleCallback;
import com.anonymbook.client.resources.AnonymMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class FriendLeft extends Composite {

	private static FriendLeftUiBinder uiBinder = GWT
			.create(FriendLeftUiBinder.class);

	interface FriendLeftUiBinder extends UiBinder<HTMLPanel, FriendLeft> {
	}

	public static interface Style extends CssResource {
		public String anonymLeftButtonSelected();
	}

	@UiField
	Style style;

	@UiField
	ImageElement profileImage;

	@UiField
	AnchorElement wall;

	@UiField
	LabelElement wallLabel;

	private HTMLPanel panel;
	
	private AnonymMessages messages = AnonymMessages.instance;

	public FriendLeft() {
		panel = uiBinder.createAndBindUi(this);
		initWidget(panel);

		wallLabel.setInnerText(messages.questions());
	}

	public AnchorElement getWall() {
		return wall;
	}
	

	public void selectWall() {
		wall.addClassName(style.anonymLeftButtonSelected());
	}
	
	public void selectQuickQuestion() {
		wall.removeClassName(style.anonymLeftButtonSelected());
	}

	public native void addWallMenuClickListener(SimpleCallback callback)
	/*-{
		var instance = this;
		var wall = instance.@com.anonymbook.client.home.FriendLeft::getWall()();

		$wnd.$(wall).click(function() {
			callback.@com.anonymbook.client.event.SimpleCallback::onEvent()();
			instance.@com.anonymbook.client.home.FriendLeft::selectWall()();
		});
	}-*/;
	
	public void setProfileImage(String imagePath) {
		profileImage.setSrc(imagePath);
		profileImage.setWidth(169);
	}

}
