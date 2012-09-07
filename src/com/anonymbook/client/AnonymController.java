package com.anonymbook.client;

import java.util.LinkedList;

import com.anonymbook.client.controller.InviteFriendsController;
import com.anonymbook.client.event.DataCallback;
import com.anonymbook.client.event.JavaScriptCallback;
import com.anonymbook.client.event.SimpleCallback;
import com.anonymbook.client.home.FriendLeft;
import com.anonymbook.client.home.UserFriends;
import com.anonymbook.client.home.UserHome;
import com.anonymbook.client.home.UserLeft;
import com.anonymbook.client.home.UserQuickQuestion;
import com.anonymbook.client.home.UserWall;
import com.anonymbook.client.request.AnonymRequest;
import com.anonymbook.client.request.AnonymResponse;
import com.anonymbook.client.resources.AnonymUser;
import com.anonymbook.client.ui.AnonymHeader;
import com.anonymbook.client.ui.AnonymLeft;
import com.anonymbook.client.ui.AnonymRight;
import com.anonymbook.client.util.Anonym;
import com.anonymbook.client.util.FB;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.RootPanel;

public class AnonymController {

	private static final int MAX_FRIENDS_IN_LEFT = 6;

	private AnonymUser user;

	private UserHome userHome;

	private AnonymHeader header;

	private AnonymLeft left;

	private AnonymRight right;

	private UserWall wall;

	private UserFriends friends;

	private UserLeft userLeft;

	private FriendLeft friendLeft;

	public AnonymController(String userId, String accessToken) {
		this.user = new AnonymUser(userId, accessToken);

		loadUserFriends();
		checkFirstAccess();
	}

	public void init() {
		FB.user(user.getId(), new JavaScriptCallback() {
			@Override
			public void onEvent(JavaScriptObject response) {
				user.setName(Anonym.get(response, "name"));
				load(user.getId(), user.getName(), null);
			}
		});
	}

	public void checkFirstAccess() {

		AnonymRequest.post("person", new AnonymResponse() {
			@Override
			public void onResponse(JSONValue response) {
				// JSONValue object = response.isObject().get("firstAccess");
				//
				// if (object != null && object.isBoolean().booleanValue()) {
				// LightFace.alert(AnonymMessages.instance.welcomeMessage(),
				// 350, 80);
				// }
			}
		});
	}

	private void loadUserFriends() {
		FB.friends(user.getAccessToken(), new JavaScriptCallback() {
			@Override
			public void onEvent(JavaScriptObject result) {
				if (result == null) {
					return;
				}
				JSONArray friendList = new JSONArray(result);
				LinkedList<String> list = new LinkedList<String>();

				for (int i = 0; i < friendList.size(); i++) {
					list.add(friendList.get(i).isObject().get("id").isString()
							.stringValue());
				}
				user.setFriends(list);
			}
		});
	}

	private void load(final String userId, final String userName,
			final String question) {
		this.userHome = new UserHome();
		this.header = userHome.getHeader();
		this.left = userHome.getLeft();
		this.right = userHome.getRight();

		userHome.addLoadCallback(new SimpleCallback() {
			@Override
			public void onEvent() {
				init(userId, userName, question);
			}
		});

		RootPanel.get().clear();
		RootPanel.get().add(userHome);
				
		FB.scrollTo(0, 0);
	}

	private void init(String userId, String userName, String question) {

		this.wall = new UserWall(user, userId, question);
		this.friends = new UserFriends(user, userId);
		this.friends.setUserName(userName);
		this.userLeft = new UserLeft();
		this.friendLeft = new FriendLeft();

		// define a imagem do usuario
		if (user.getId().equals(userId)) {
			left.setComponent(userLeft);

			userLeft.setProfileImage(FB.image(userId, "large"));
			userLeft.selectWall();

			showFriends(userId, true);
		} else {
			left.setComponent(friendLeft);

			friendLeft.setProfileImage(FB.image(userId, "large"));
			friendLeft.selectWall();
		}

		showWall(userId);
		addListeners(userId);
	}

	public void getUserNameAndLoadView(final String userId,
			final String question) {
		
		FB.user(userId, new JavaScriptCallback() {
			@Override
			public void onEvent(JavaScriptObject response) {
				load(userId, Anonym.get(response, "name"), question);
			}
		});
	}

	private void addListeners(final String userId) {

		DataCallback<String> loadViewHandler = new DataCallback<String>() {
			@Override
			public void onEvent(String userId) {
				getUserNameAndLoadView(userId, null);
			}
		};

		SimpleCallback inviteHandler = new SimpleCallback() {
			@Override
			public void onEvent() {
				InviteFriendsController.showInvite();
			}
		};

		SimpleCallback showFriends = new SimpleCallback() {
			@Override
			public void onEvent() {
				showFriends(userId, false);
			}
		};

		SimpleCallback showQuickQuestion = new SimpleCallback() {
			@Override
			public void onEvent() {
				showQuickQuestion(user, userId);
			}
		};
		
		SimpleCallback showWall = new SimpleCallback() {
			@Override
			public void onEvent() {
				showWall(userId);
			}
		};

		header.addHomeClickListener(new SimpleCallback() {
			@Override
			public void onEvent() {
				getUserNameAndLoadView(user.getId(), null);
			}
		});
		
		header.addHelpClickListener(new SimpleCallback() {
			@Override
			public void onEvent() {
				// TODO help;
			}
		});


		userLeft.addWallMenuClickListener(showWall);
		friendLeft.addWallMenuClickListener(showWall);

		userLeft.addQuickQuestionClickListener(showQuickQuestion);
		header.addQuickQuestionClickListener(showQuickQuestion);
		

		userLeft.addFriendsMenuClickListener(showFriends);
		userLeft.setFriendsListClickCallback(showFriends);

		userLeft.setFriendClickCallback(loadViewHandler);
		wall.setFriendClickCallback(loadViewHandler);
		friends.setFriendClickCallback(loadViewHandler);

		friends.setFriendInviteCallback(inviteHandler);
	}

	private void showFriends(String userId, boolean onLoad) {
		right.setComponent(friends);

		loadFriends(userId, onLoad);
	}

	private void loadFriends(final String userId, final boolean onLoad) {

		FB.friends(user.getAccessToken(), new JavaScriptCallback() {
			@Override
			public void onEvent(JavaScriptObject result) {

				if ((result == null || new JSONObject(result).get("error_code") != null)
						&& user.getFriends().contains(userId)) {
					InviteFriendsController.showInvite(userId);
					return;
				}

				loadFriendList(onLoad, result);
				loadUserFriends();
			}
		});

	}

	private void loadFriendList(boolean onLoad, JavaScriptObject response) {

		if (!onLoad) {
			friends.clearFriendsList();
			friends.addListeners(response);
			friends.addFriends(response);
		}

		userLeft.setFriendsAmount(new JSONArray(response).size());
		userLeft.clearFriendsList();
		userLeft.addFriends(response, MAX_FRIENDS_IN_LEFT);
	}

	private void showWall(String userId) {
		right.setComponent(wall);
	}

	private void showQuickQuestion(AnonymUser user, String userId) {
		UserQuickQuestion page = new UserQuickQuestion(user);
		right.setComponent(page);
	}
}
