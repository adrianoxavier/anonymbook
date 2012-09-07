package com.anonymbook.client.home;

import java.util.HashMap;
import java.util.Map;

import com.anonymbook.client.event.DataCallback;
import com.anonymbook.client.event.SimpleCallback;
import com.anonymbook.client.resources.AnonymMessages;
import com.anonymbook.client.resources.AnonymUser;
import com.anonymbook.client.util.DomHelper;
import com.anonymbook.client.util.FB;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class UserFriends extends Composite {

	private static UserFriendsUiBinder uiBinder = GWT
			.create(UserFriendsUiBinder.class);

	interface UserFriendsUiBinder extends UiBinder<HTMLPanel, UserFriends> {
	}

	public static interface Style extends CssResource {
		public String userFriendListImage();

		public String userFriendListName();

		public String userFriendListXbutton();
	}

	@UiField
	Style style;

	@UiField
	LabelElement nameLabel;

	@UiField
	LabelElement friendsLabel;

	@UiField
	UListElement friendsList;

	@UiField
	LabelElement searchLabel;

	@UiField
	InputElement search;

	private HTMLPanel panel;

	private DataCallback<String> friendClickCallback;

	private SimpleCallback friendInviteCallback;

	private Map<String, LIElement> friendElements;

	private boolean isMine;
	
	private AnonymMessages messages = AnonymMessages.instance;

	public UserFriends(AnonymUser user, String uid) {
		panel = uiBinder.createAndBindUi(this);
		initWidget(panel);

		isMine = uid.equals(user.getId());
		friendElements = new HashMap<String, LIElement>();

		friendsLabel.setInnerText(messages.friends());
		searchLabel.setInnerText(messages.searchByName());
		search.setTitle(messages.typeFriendName());

		friendsList.setId(DomHelper.createId());
	}

	public void addListeners(JavaScriptObject friends) {
		jqueryEvents(search, friends);
	}

	public void clearFriendsList() {
		remove("#" + friendsList.getId() + " li");
	}

	private native void remove(String selector)
	/*-{
		$wnd.$(selector).remove();
	}-*/;

	private void showFriend(String userId) {
		friendsList.appendChild(friendElements.get(userId));
	}

	private native void jqueryEvents(Element field, JavaScriptObject friends)
	/*-{

		var instance = this;

		$wnd.$(field).focus(function() {
			if ($wnd.$(this).val() == $wnd.$(this)[0].title) {
				$wnd.$(this).css({
					'color' : '#333'
				});
				$wnd.$(this).val("");
			}
		});

		$wnd.$(field).blur(function() {
			if ($wnd.$(this).val() == "") {
				$wnd.$(this).css({
					'color' : '#777'
				});
				$wnd.$(this).val($wnd.$(this)[0].title);
			}
		});

		$wnd.$(field).blur();

		$wnd.$(field).autocomplete(
					{
						minLength : 0,
						source : function(request, response) {
							var term = request.term.toLowerCase();
							var result = new $wnd.Array();
							for (i in friends) {
								if ((friends[i].uid || friends[i].id)
										&& friends[i].name
										&& friends[i].name.toLowerCase()
												.indexOf(term) == 0) {
									result.push(friends[i]);
								}
							}
							instance.@com.anonymbook.client.home.UserFriends::clearFriendsList()();
							response(result);
						},
						select : function(event, ui) {
							return false;
						}
					})
					.data("autocomplete")._renderItem = function(ul, item) {
						if(item.uid) {
							instance.@com.anonymbook.client.home.UserFriends::showFriend(Ljava/lang/String;)(item.uid);
						} else {
							instance.@com.anonymbook.client.home.UserFriends::showFriend(Ljava/lang/String;)(item.id);
						}
						// remove a drop down list do resultado do autoComplete.
						ul.remove();
						return null;
					};
	}-*/;

	public void setUserName(String name) {
		nameLabel.setInnerText(name);
	}

	public void setFriendClickCallback(DataCallback<String> callback) {
		this.friendClickCallback = callback;
	}

	public void setFriendInviteCallback(SimpleCallback callback) {
		this.friendInviteCallback = callback;
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

		Document doc = Document.get();

		ImageElement image = doc.createImageElement();
		image.setSrc(FB.image(userId, "square"));
		
		HTMLPanel name = new HTMLPanel("a", "");
		name.addDomHandler(handler, ClickEvent.getType());
		name.getElement().setInnerText(userName);
		panel.add(name);

		HTMLPanel imageDiv = new HTMLPanel("");
		panel.add(imageDiv);
		imageDiv.setStyleName(style.userFriendListImage());
		imageDiv.getElement().appendChild(image);
		imageDiv.addDomHandler(handler, ClickEvent.getType());

		DivElement nameDiv = doc.createDivElement();
		nameDiv.addClassName(style.userFriendListName());
		nameDiv.appendChild(name.getElement());

		LIElement item = doc.createLIElement();
		item.appendChild(imageDiv.getElement());
		item.appendChild(nameDiv);

		friendsList.appendChild(item);
		friendElements.put(userId, item);
	}

	public void addFriends(JavaScriptObject js) {
		JSONArray friendList = new JSONArray(js);

		for (int i = 0; i < friendList.size(); i++) {
			JSONObject item = friendList.get(i).isObject();
			JSONValue uid = (item.get("id") != null) ? item.get("id") : item.get("uid");

			String id = uid.isString().stringValue();
			String name = item.get("name").isString().stringValue();
			
			addFriend(id, name);
		}
	}

}
