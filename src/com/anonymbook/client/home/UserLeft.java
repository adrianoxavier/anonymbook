package com.anonymbook.client.home;

import com.anonymbook.client.event.DataCallback;
import com.anonymbook.client.event.SimpleCallback;
import com.anonymbook.client.resources.AnonymMessages;
import com.anonymbook.client.util.DomHelper;
import com.anonymbook.client.util.FB;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;

public class UserLeft extends Composite {

	private static UserLeftUiBinder uiBinder = GWT
			.create(UserLeftUiBinder.class);

	interface UserLeftUiBinder extends UiBinder<HTMLPanel, UserLeft> {
	}

	public static interface Style extends CssResource {
		public String anonymLeftFriendListImage();

		public String anonymLeftFriendListName();

		public String anonymLeftButtonSelected();
	}

	@UiField
	Style style;

	@UiField
	ImageElement profileImage;

	@UiField
	AnchorElement wall;

	@UiField
	AnchorElement friends;

	@UiField
	AnchorElement quickQuestion;

	@UiField
	LabelElement wallLabel;

	@UiField
	LabelElement friendsLabel;

	@UiField
	LabelElement quickQuestionLabel;

	@UiField
	Anchor friendsLink;

	@UiField
	HTMLPanel friendsContainer;

	private HTMLPanel panel;

	private DataCallback<String> friendClickCallback;

	private AnonymMessages messages = AnonymMessages.instance;

	public UserLeft() {
		panel = uiBinder.createAndBindUi(this);
		initWidget(panel);

		wallLabel.setInnerText(messages.questions());
		quickQuestionLabel.setInnerText(messages.quickQuestion());
		friendsLabel.setInnerText(messages.friends());
		friendsLink.setText(messages.friends());

		friendsContainer.getElement().setId(DomHelper.createId());

		setFriendsAmount(0);

		// FIXME remover o quickQuestion.
		quickQuestion.getStyle().setDisplay(Display.NONE);
	}

	public AnchorElement getWall() {
		return wall;
	}

	public AnchorElement getFriends() {
		return friends;
	}

	public AnchorElement getQuickQuestion() {
		return quickQuestion;
	}

	public void selectWall() {
		wall.addClassName(style.anonymLeftButtonSelected());
		friends.removeClassName(style.anonymLeftButtonSelected());
		quickQuestion.removeClassName(style.anonymLeftButtonSelected());
	}

	public void selectFriends() {
		friends.addClassName(style.anonymLeftButtonSelected());
		wall.removeClassName(style.anonymLeftButtonSelected());
		quickQuestion.removeClassName(style.anonymLeftButtonSelected());
	}

	public void selectQuickQuestion() {
		quickQuestion.addClassName(style.anonymLeftButtonSelected());
		friends.removeClassName(style.anonymLeftButtonSelected());
		wall.removeClassName(style.anonymLeftButtonSelected());
	}

	public native void addWallMenuClickListener(SimpleCallback callback)
	/*-{
		var instance = this;
		var wall = instance.@com.anonymbook.client.home.UserLeft::getWall()();

		$wnd.$(wall).click(function() {
			callback.@com.anonymbook.client.event.SimpleCallback::onEvent()();
			instance.@com.anonymbook.client.home.UserLeft::selectWall()();
		});
	}-*/;

	public native void addFriendsMenuClickListener(SimpleCallback callback)
	/*-{
		var instance = this;
		var friends = instance.@com.anonymbook.client.home.UserLeft::getFriends()();

		$wnd.$(friends).click(function() {
			callback.@com.anonymbook.client.event.SimpleCallback::onEvent()();
			instance.@com.anonymbook.client.home.UserLeft::selectFriends()();
		});
	}-*/;

	public native void addQuickQuestionClickListener(SimpleCallback callback)
	/*-{
		var instance = this;
		var quick = instance.@com.anonymbook.client.home.UserLeft::getQuickQuestion()();

		$wnd
				.$(quick)
				.click(
						function() {
							callback.@com.anonymbook.client.event.SimpleCallback::onEvent()();
							instance.@com.anonymbook.client.home.UserLeft::selectQuickQuestion()();
						});
	}-*/;

	public void setFriendClickCallback(DataCallback<String> callback) {
		this.friendClickCallback = callback;
	}

	public void clearFriendsList() {
		remove("#" + friendsContainer.getElement().getId() + " li");
	}

	private native void remove(String selector)
	/*-{
		$wnd.$(selector).remove();
	}-*/;

	public void setFriendsListClickCallback(final SimpleCallback callback) {

		friendsLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selectFriends();
				callback.onEvent();
			}
		});
	}

	public void setProfileImage(String imagePath) {
		profileImage.setSrc(imagePath);
		profileImage.setWidth(169);
	}

	public void setFriendsAmount(int friends) {
		friendsLink.setText("friends (" + friends + ")");
	}

	public void addFriend(final String userId, String userName) {

		ClickHandler handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (friendClickCallback != null) {
					friendClickCallback.onEvent(userId);
				}
			}
		};

		Image image = new Image();
		image.setUrl(FB.image(userId, "square"));
		image.setStyleName(style.anonymLeftFriendListImage());
		image.addDomHandler(handler, ClickEvent.getType());

		Anchor name = new Anchor();
		name.setStyleName(style.anonymLeftFriendListName());
		name.addClickHandler(handler);
		name.setText(userName);

		HTMLPanel li = new HTMLPanel("li", "");
		
		li.add(image);
		li.add(name);
		
		friendsContainer.add(li);
	}

	public void addFriends(JavaScriptObject js, int limit) {
		JSONArray array = new JSONArray(js);
		int size = array.size();

		if (array == null || size == 0) {
			return;
		}

		setFriendsAmount(size);

		int max = (limit > 0 && limit < size) ? limit : size;
		;

		for (int i = 0; i < max; i++) {
			swap(array, i, Random.nextInt(size));
		}

		for (int i = 0; i < max; i++) {
			JSONObject item = array.get(i).isObject();
			JSONValue uid = (item.get("id") != null) ? item.get("id") : item
					.get("uid");

			String id = uid.isString().stringValue();
			String name = item.get("name").isString().stringValue();

			addFriend(id, name);
		}
	}

	private void swap(JSONArray array, int i, int j) {
		JSONValue old = array.get(i);
		array.set(i, array.get(j));
		array.set(j, old);
	}

}
